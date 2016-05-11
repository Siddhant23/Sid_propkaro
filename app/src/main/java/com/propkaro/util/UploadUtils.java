package com.propkaro.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class UploadUtils {
	static String TAG = UploadUtils.class.getSimpleName();
	public static String MultipartFileUploader (File uploadFile1, Bitmap bm, String UrlBase, String jsonData, String userId, String propertyId, boolean isUniqueName, boolean isBitmap){
		 
//      File uploadFile1 = new File("e:/Test/PIC1.JPG");
//      File uploadFile2 = new File("e:/Test/PIC2.JPG");

      try {
  		byte[] byte_data = jsonData.getBytes("UTF-8");
  		String base64 = Base64.encodeToString(byte_data, Base64.DEFAULT);
		if(Utilities.D)Log.d(TAG,"UrlBase=" + UrlBase);
		if(Utilities.D)Log.v(TAG,"jsonData=" + jsonData);
		if(Utilities.D)Log.v(TAG,"base64=" + base64);

          MultipartUtility multipart = new MultipartUtility(UrlBase, "UTF-8");

          multipart.addHeaderField("User-Agent", "CodeJava");
          multipart.addHeaderField("Test-Header", "Header-Value");

          multipart.addFormField("description", "Cool Pictures");
          multipart.addFormField("keywords", "Java,upload,Spring");

          multipart.addFormField("json", base64);
          multipart.addFormField("id", userId);
          multipart.addFormField("property_id", propertyId);
          multipart.addFormField("mob_key", Host.TOCKEN);

//          multipart.addFilePart("fileUpload", uploadFile1);
//          multipart.addFilePart("fileUpload", uploadFile2);
          if(isBitmap){
              multipart.addBitmapPart("image", bm, isUniqueName);
          } else {
        	  if(Utilities.D)Log.v(TAG,"File=" + uploadFile1);
        	  if(Utilities.D)Log.v(TAG,"getFileSize=" + uploadFile1.length());
        	  multipart.addFilePart("file", uploadFile1, isUniqueName);
          }

          List<String> response = multipart.finish();
          
          StringBuffer sb = null;
		    	sb = new StringBuffer(); 
          for (String line : response) {
		    	sb.append(line);
//		    	if(Utilities.D)Log.v(TAG, line);
          }
          return sb.toString();
      } catch (IOException ex) {
          Log.e(TAG, ex.getMessage());
          ex.printStackTrace();
          return null;
      }
  }
	public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
	{
		compressFormat = Bitmap.CompressFormat.JPEG;
	    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
	    image.compress(compressFormat, quality, byteArrayOS);
	    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}

	public static Bitmap decodeBase64(String input)
	{
	    byte[] decodedBytes = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
//	private static String readStream(InputStream in) {
//		BufferedReader reader = null;
//		StringBuilder builder = new StringBuilder();
//		try {
//			reader = new BufferedReader(new InputStreamReader(in));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				builder.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return builder.toString();
//	}
//	public static <OnLoadFinishedListener> void uploadMovie(final HashMap<String, String> dataSource, 
//			final OnLoadFinishedListener finishedListener, final ProgressListener progressListener) {
//		  if (finishedListener != null) {
//		    new Thread(new Runnable() {
//		       public void run() {
//		         try {
//
//		              String boundary = getMD5(dataSource.size()+String.valueOf(System.currentTimeMillis()));
//		              MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
//		              multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);    
//		              multipartEntity.setCharset(Charset.forName("UTF-8"));
//
//		              for (String key : dataSource.keySet()) {
//		                 if (key.equals(MoviesFragmentAdd.USERFILE)) {
//		                    FileBody  userFile = new FileBody(new File(dataSource.get(key)));
//		                    multipartEntity.addPart(key, userFile);
//		                    continue;
//		                 }
//		                 multipartEntity.addPart(key, new StringBody(dataSource.get(key),ContentType.APPLICATION_JSON));
//		              }
//
//		              HttpEntity entity = multipartEntity.build();
//		              HttpURLConnection conn = (HttpsURLConnection) new URL(URL_API + "/video/addForm/").openConnection();
//		              conn.setUseCaches(false);
//		              conn.setDoOutput(true);
//		              conn.setDoInput(true);
//		              conn.setRequestMethod("POST");
//		              conn.setRequestProperty("Accept-Charset", "UTF-8");
//		              conn.setRequestProperty("Connection", "Keep-Alive");
//		              conn.setRequestProperty("Cache-Control", "no-cache");
//		              conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//		              conn.setRequestProperty("Content-length", entity.getContentLength() + "");
//		              conn.setRequestProperty(entity.getContentType().getName(),entity.getContentType().getValue());
//
//		              OutputStream os = conn.getOutputStream();
//		              entity.writeTo(os);
//		              os.close();
//
//		              //Real upload starting here -->>
//
//		              BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//		              //<<--
//
//		              JsonObject request = (JsonObject) gparser.parse(in.readLine());
//		              if (!request.get("error").getAsBoolean()) {
//		              //do something
//		              }
//		              conn.disconnect(); 
//
//		           } catch (Exception e) {
//		            e.printStackTrace();
//		           }
//		         }
//		    }).start();
//
//		  }
//		}
	}
