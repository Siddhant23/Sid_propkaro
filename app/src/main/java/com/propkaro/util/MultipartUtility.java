package com.propkaro.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server. 
 * @author www.codejava.net
 *
 */
public class MultipartUtility {
	private static String TAG = MultipartUtility.class.getSimpleName();
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpsURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
 
    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;
         
        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        SSLContext sc = null;
        try {
    		sc = SSLContext.getInstance("TLS");
    	    sc.init(null, null, new java.security.SecureRandom());
		} catch (Exception e) {
		} 
	    
        URL url = new URL(requestURL);
        httpConn = (HttpsURLConnection) url.openConnection();
        httpConn.setSSLSocketFactory(sc.getSocketFactory());
        httpConn.setReadTimeout(30000);
		httpConn.setConnectTimeout(30000);
		httpConn.setRequestMethod("POST");
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("ENCTYPE", "multipart/form-data");
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
//        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10 (.NET CLR 3.5.30729)");
//        httpConn.setRequestProperty("Content-Language", "en-US");  
        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
        httpConn.setRequestProperty("Test", "Bonjour");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }
 
    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }
    /*
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addBitmapPart(String fieldName, Bitmap bitmap, boolean isUniqueName) throws IOException {
		String fileName = "";
		if(isUniqueName)
			fileName = String.format("androidPic_%d.jpg", new Date().getTime());
		else
			fileName = "androidPic.jpg";
		if(Utilities.D)Log.v(TAG, "fileName: " + fileName);

    	writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos);
        byte[] bitmapdata = bos.toByteArray();
        int lng = bitmapdata.length;
        outputStream.write(bitmapdata);
        outputStream.flush();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile, boolean isUniqueName)throws IOException {
        String fileName = uploadFile.getName();
		if(isUniqueName)
			fileName = String.format("androidFile_%d.csv", new Date().getTime());
		else
			fileName = "androidCsvFile.csv";
		if(Utilities.D)Log.v(TAG, "fileName: " + fileName);

        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                        .append(LINE_FEED);
        if(Utilities.D)Log.i(TAG, "GuessFileType="+URLConnection.guessContentTypeFromName(fileName));
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int statusCode = httpConn.getResponseCode();
        if (statusCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        	if(Utilities.D)Log.d(TAG,"Read Successfull=" + statusCode);
			if(Utilities.D)Log.d(TAG,"Response=" + response);
        } else {
			if(Utilities.D)Log.d(TAG,"unSuccessfull Reading=" + statusCode);
            throw new IOException("Server returned non-OK status: " + statusCode);
        }
 
        return response;
    }
}