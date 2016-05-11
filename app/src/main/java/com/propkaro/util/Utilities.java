package com.propkaro.util;
/* Written by: Hariom Gupta
 * Locality: Delhi(NCR)
 * Sr. Android Developer
 * Ph no: +91-8510887828
 * email: hk.mca08@gmail.com
 * facebook: HariGupta2011
 * skype: hari_gupta11
 * last modified: May07,2016
 * Ver: 4.0
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.propkaro.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public abstract class Utilities {
	public static final boolean D = true;
	private static String TAG = Utilities.class.getSimpleName();
	public static Thread updateThread;

	//--------------------------------FIXME: Following conneciton to be work-----------------
	public static String readJsonGoogle(Context ctx, String getURL){
		HttpsURLConnection mConnection;
		try {
			if(Utilities.D)Log.v(TAG,"f_url[0]=" + getURL);
			
//--------------------part2---------------------------
			SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(null, null, new java.security.SecureRandom());
			
			URL url = new URL(getURL);
            mConnection = (HttpsURLConnection) url.openConnection();
            mConnection.setSSLSocketFactory(sc.getSocketFactory());
            mConnection.setReadTimeout(30000 /* milliseconds */);
            mConnection.setConnectTimeout(30000 /* milliseconds */);
            mConnection.setRequestMethod("GET");
            mConnection.setDoInput(true);
            mConnection.connect();
            int statusCode = mConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
            	if(Utilities.D)Log.d(TAG,"unSuccessfullReading=" + statusCode);
                return null;
            }else{
            	if(Utilities.D)Log.d(TAG,"ReadSuccessfull=" + statusCode);
		        return readFromServer(mConnection);
            }
		} catch (UnsupportedEncodingException e1) { //e1.printStackTrace();
        	if(Utilities.D)Log.e(TAG,"UnsupportedEncodingException=" + e1.getMessage());
            return null;
        } catch (IOException e) {//e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"IOException=" + e.getMessage());
            return null;
        } catch (Exception e) {//e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"NoConnectionError=" + e.getMessage());
            return null;
        }
    }
	public static String readJson(Context ctx, String getURL){
		HttpsURLConnection mConnection;
		try {
			if(Utilities.D)Log.v(TAG,"f_url[0]=" + getURL);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream caInput = new BufferedInputStream(ctx.getAssets().open("ServerCertificate.cer"));
//			InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
			Certificate ca;
			try {
			    ca = cf.generateCertificate(caInput);
			    Log.i(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
			} finally {
			    caInput.close();
			}
			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);
			
			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);
			

			// Create an SSLContext that uses our TrustManager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, tmf.getTrustManagers(), null);
//--------------------part2---------------------------
//			SSLContext sc = SSLContext.getInstance("TLS");
//		    sc.init(null, null, new java.security.SecureRandom());
			
			URL url = new URL(getURL);
            mConnection = (HttpsURLConnection) url.openConnection();
            mConnection.setSSLSocketFactory(sc.getSocketFactory());
            mConnection.setReadTimeout(30000 /* milliseconds */);
            mConnection.setConnectTimeout(30000 /* milliseconds */);
            mConnection.setRequestMethod("GET");
            mConnection.setDoInput(true);
            mConnection.connect();
            int statusCode = mConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
            	if(Utilities.D)Log.d(TAG,"unSuccessfullReading=" + statusCode);
                return null;
            }else{
            	if(Utilities.D)Log.d(TAG,"ReadSuccessfull=" + statusCode);
		        return readFromServer(mConnection);
            }
		} catch (UnsupportedEncodingException e1) { //e1.printStackTrace();
        	if(Utilities.D)Log.e(TAG,"UnsupportedEncodingException=" + e1.getMessage());
            return null;
        } catch (IOException e) {//e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"IOException=" + e.getMessage());
            return null;
        } catch (Exception e) {//e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"NoConnectionError=" + e.getMessage());
            return null;
        }
    }
public static String readFromServer(HttpURLConnection connection) throws IOException {
    InputStreamReader stream = null;
    try {
        stream = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(stream);
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        if(Utilities.D)Log.d(TAG,"Response=" + sb.toString());
        return sb.toString();
    } finally {
        if (stream != null) {
            stream.close();
        }
    }
}

	public static String sendData(String UrlBase, String urlParameters){
	    HttpURLConnection httpConn = null;
	    int statusCode = -1;
		try {
			if(Utilities.D)Log.v(TAG,"UrlBase=" + UrlBase);
			if(Utilities.D)Log.v(TAG,"urlParameters=" + urlParameters);

		    URL url = new URL(UrlBase);
		    URLConnection urlConn = url.openConnection();
		    if (!(urlConn instanceof HttpURLConnection)) {
			   	throw new IOException("URL is not an Http URL!");
		    }
		      httpConn = (HttpURLConnection) urlConn;
		      httpConn.setReadTimeout(30000);
		      httpConn.setConnectTimeout(30000);
	         httpConn.setAllowUserInteraction(false);
	         httpConn.setInstanceFollowRedirects(true);
//	         httpConn.setChunkedStreamingMode(1024);
	         httpConn.setRequestMethod("POST");
	         httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					
	         httpConn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	         httpConn.setRequestProperty("Content-Language", "en-US");  
					
	         httpConn.setUseCaches (false);
	         httpConn.setDoInput(true);
	         httpConn.setDoOutput(true);
//		    httpConn.connect();

// 			Send request
		      DataOutputStream wr = new DataOutputStream (httpConn.getOutputStream ());
		      wr.writeBytes (urlParameters);
		      wr.flush ();
		      wr.close ();
		      statusCode = httpConn.getResponseCode();
		      if (statusCode == HttpURLConnection.HTTP_OK) {
			      InputStreamReader stream = null;
			      StringBuffer sb = null;
			      try {
			    	  stream = new InputStreamReader(httpConn.getInputStream());
				      BufferedReader br = new BufferedReader(stream);
				      String line;
				      sb = new StringBuffer();
				      while((line = br.readLine()) != null) {
				    	  sb.append(line);
				      }
				      br.close();
					} catch (IOException e) { e.printStackTrace();
				    } finally {
				        if (stream != null) {
				        	stream.close();
				        }
				    }
			      if(Utilities.D)Log.i(TAG,"ReadSuccessfull=" + statusCode);
			      if(Utilities.D)Log.i(TAG,"Response=" + sb.toString());
			      return sb.toString();
			} else {
				if(Utilities.D)Log.e(TAG,"unSuccessfullReading=" + statusCode);
				return null;
			}
		}catch (MalformedURLException e) { //e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"MalformedURLException=" + e.getMessage());
            return null;
		} catch (UnsupportedEncodingException e) {//e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"UnsupportedEncodingError=" + e.getMessage());
            return null;
	    } catch (Exception e) { //e.printStackTrace();
	    	if(Utilities.D)Log.e(TAG,"NoConnectionError=" + e.getMessage());
	    	return null;
	    } finally {
		      if(httpConn != null) {
		    	  httpConn.disconnect(); 
			  }
	    }
	}
	
	//---------------------------------UploadImage and Bitmap----------------------------------------
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }
	private Bitmap decodeFile(File file){
	        try {
	            BitmapFactory.Options opt = new BitmapFactory.Options();
	            opt.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(file),null,opt);
	            final int REQUIRED_SIZE=70;
	            int width_tmp=opt.outWidth, height_tmp=opt.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            BitmapFactory.Options opte = new BitmapFactory.Options();
	            opte.inSampleSize=scale;
	            return BitmapFactory.decodeStream(new FileInputStream(file), null, opte);
	        } catch (FileNotFoundException e) {e.printStackTrace();}
	        return null;
	    }
	   public static Bitmap getBitmapFromURL(String src) {
	    try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, new java.security.SecureRandom());
	        URL url = new URL(src);
	        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap bm = BitmapFactory.decodeStream(input);
	        return bm;
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
            return null;
		} catch (UnsupportedEncodingException e1) { //e1.printStackTrace();
        	if(Utilities.D)Log.e(TAG,"UnsupportedEncodingException=" + e1.getMessage());
            return null;
	    } catch (IOException e) { 
	    	e.printStackTrace();
	        return null;
	    } catch(Exception e){ 
	    	e.printStackTrace(); 
            return null;
    	}
	}
		public static Bitmap compressImage(Bitmap bitmap) {
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 50, baos);
				int length = baos.size();
				Log.d("compressImg", "before compress  size:" + (length / 1024) + "KB");
				
				final int MIN_BYTE = 100 * 1024;
				// Less than 200K let it
				
				if (length > MIN_BYTE) {
					Log.d("compressImg", "large than 200KB begin compress.");
					// Compression ratio formula, can be self definition
					int quality = (int) (((length - MIN_BYTE) / 10.0 + MIN_BYTE) * 100 / length);
					Log.d("compressImg", "compress rate:" + quality + "%");
					baos.reset();
					
					if (bitmap.compress(CompressFormat.JPEG, quality, baos)) {
						byte[] bs = baos.toByteArray();
						length = bs.length;
						Log.d("compressImage", "compress success new size:" + (length / 1024) + "KB");
						ByteArrayInputStream bais = new ByteArrayInputStream(bs);
						
						return BitmapFactory.decodeStream(bais);
					}
				} else {
					Log.d("compressImage", "Not compress less than 200K size:" + (length / 1024) + "KB");
				}
			} else {
				Log.d("compressImage", "decode bitmap error");
			}
			return bitmap;
		}
	private static void disableConnectionReuseIfNecessary() {
	    // HTTP connection reuse which was buggy pre-froyo
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
	    	Log.i(TAG, "Build.VERSION.BASE_OS) ----"+Build.VERSION.SDK_INT +" " + Build.VERSION_CODES.FROYO);
	        System.setProperty("http.keepAlive", "false");
	    }
	}
	private void enableHttpResponseCache(Context ctx) {
	    try {
	        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
	        File httpCacheDir = new File(ctx.getCacheDir(), "http");
	        Class.forName("android.net.http.HttpResponseCache")
	            .getMethod("install", File.class, long.class)
	            .invoke(null, httpCacheDir, httpCacheSize);
	    } catch (Exception httpResponseCacheNotAvailable) {
	    }
	}
	public static long getFileSize(String imagePath){
		long length = 0;
		try {
			File file = new File(imagePath);
			length = file.length();
			length = length / 1024;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return length;
	}
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
//--------------------------Other options---------------------------------------------	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
	public static BitmapFactory.Options getBitmapOptionsWithoutDecoding(String url){
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(url, opts);
	    return opts;
	}

	public static int getBitmapSizeWithoutDecoding(String url){
	    BitmapFactory.Options opts = getBitmapOptionsWithoutDecoding(url);
	    return opts.outHeight*opts.outWidth*32/(1024*1024*8);
	}

	//ref:http://stackoverflow.com/questions/6073744/android-how-to-check-how-much-memory-is-remaining
	public static double availableMemoryMB(){
	    double max = Runtime.getRuntime().maxMemory()/1024;
	    Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
	    Debug.getMemoryInfo(memoryInfo);
	    return (max - memoryInfo.getTotalPss())/1024;
	}

	public static final long SAFETY_MEMORY_BUFFER = 10;//MB

	public static boolean canBitmapFitInMemory(String path){
	    long size = getBitmapSizeWithoutDecoding(path);
	    if(Utilities.D)Log.d(TAG,"image MB:"+size);
	    return size <= availableMemoryMB() - SAFETY_MEMORY_BUFFER;
	}
	
	public static String getRealPathFromURI (Context ctx, Uri contentUri) {
	     String path = null;
	     String[] proj = { MediaStore.MediaColumns.DATA };

	        if("content".equalsIgnoreCase(contentUri.getScheme ())) {
	                Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
	                if (cursor.moveToFirst()) {
	                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
	                    path = cursor.getString(column_index);
	                }
	                cursor.close();
	                return path;
	            }
	            else if("file".equalsIgnoreCase(contentUri.getScheme())) {
	                return contentUri.getPath();
	            }
	            return null;
	        }
	//-----------------------------end upload bitmap-----------------------	
//-----------------------------Keyboard status--------------------------------
	public void showSoftKeyboard(Context ctx, View view){
	    if(view.requestFocus()){
	        InputMethodManager imm =(InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
	    }
	}
	public void hideSoftKeyboard(Activity ctx, View view){
	    if(view.requestFocus()){
	    	ctx.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    }
	}
	public static void hideKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

//-----------------------------NetworkCheck--------------------------------
	public static boolean NetworkCheck(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			if(Utilities.D)Log.v(TAG,"NETWORK = Wifi");
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			if(Utilities.D)Log.v(TAG,"NETWORK = Mobile");
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			System.out.println("NETWORK = Active network");
			return true;
		}
		if(Utilities.D)Log.v(TAG,"NETWORK = No Network");
		return false;
	}

	public static final boolean isInternetOn(Context ctx) {

		ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

			// Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
			return true;

		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			Toast.makeText(ctx, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}
	
	public static class WakeLocker {
		private static PowerManager.WakeLock wakeLock;

		public static void acquire(Context context) {
			if (wakeLock != null)
				wakeLock.release();

			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, "WakeLock");
			wakeLock.acquire();
		}

		public static void release() {
			if (wakeLock != null)
				wakeLock.release();
			wakeLock = null;
		}
	}
	
    public static List<String> getAccoutInfo(Context ctx){
    	
    	String getName = "", getEmail = "";
    	List<String> accInfo = new ArrayList<String>();
    	
        try{
            Account[] accounts = AccountManager.get(ctx).getAccountsByType("com.google");
            for (Account account : accounts) {
            	if(Utilities.D)Log.d(TAG,"account=Google: " + account);
            	getEmail=account.name;getName=account.type;
                accInfo.add(getEmail);accInfo.add(getName);
            }
       }
        
       catch(Exception e){
            Log.i(TAG, "Error:"+e);
            getName="name";getEmail="name@com.net";
       }
        
       try{
            Account[] accounts = AccountManager.get(ctx).getAccounts();
            for (Account account : accounts) {
//               if(Utilities.D)Log.v(TAG,"Accout===Others==="+account.toString()+"");
            	getEmail=account.name;getName=account.type;
                accInfo.add(getEmail);accInfo.add(getName);
            }
       }
       catch(Exception e){
            Log.w(TAG, "Error:"+e) ;
       }
       
       if(Utilities.D)Log.d(TAG,"accInfo=" + accInfo);
       if(Utilities.D)Log.d(TAG,"accInfo.size()=" + accInfo.size());
    
    return accInfo;
    }
//-------------------------------Map info---------------------------------------
	public static boolean getCircleMarker(Marker marker, Circle circle){
		float[] distance = new float[2];
		
		Location.distanceBetween( marker.getPosition().latitude, marker.getPosition().longitude,
		    circle.getCenter().latitude, circle.getCenter().longitude, distance);

		if( distance[0] > circle.getRadius()  ){
//			if(Utilities.D)Log.v(TAG,"true");
			return false;
		} else {
//			if(Utilities.D)Log.v(TAG,"false");
			return true;
		}
	}

    public static Marker mapMarker(GoogleMap googleMap, LatLng latLng, boolean isClear, String title){
    	Marker marker = null;
    	try {
        	if(isClear)
        		googleMap.clear();
    		
        	MarkerOptions markerOption = new MarkerOptions().position(latLng).title(title);
        	markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        	markerOption.draggable(true);
    		marker = googleMap.addMarker(markerOption);

//    		CameraPosition cameraPosition = new CameraPosition.Builder()
//    				.target(latLng).zoom(15)
//    				.build();
//
//    		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		} catch (Exception e) { e.printStackTrace();}
    	return marker;
    }
    
	public static Circle mapCircle(GoogleMap googleMap, LatLng latLng, boolean isClear) {
		Circle mCircle = null;
		try {
			if(isClear)
				googleMap.clear();
    		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(latLng).zoom(12)
			.build();

    		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    		
    		CircleOptions circleOptions = new CircleOptions();
            // Specifying the center of the circle
            circleOptions.center(latLng);
            // Radius of the circle
            circleOptions.radius(3000);
            // Border color of the circle
            circleOptions.strokeColor(0xAA1789CE);
            // Fill color of the circle
            circleOptions.fillColor(0x881789CE);
            // Border width of the circle
            circleOptions.strokeWidth(2);
            // Adding the circle to the GoogleMap
            mCircle = googleMap.addCircle(circleOptions);                   
//			drawCircle(latLng);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mCircle;
	}
//-------------------------------Location info-----------------------------------
	public static List<android.location.Address> locationFinder(Context ctx) {
		GPSTracker gps = new GPSTracker(ctx);
		List<android.location.Address> addsInfo = new ArrayList<android.location.Address>();
		int count = 0;
		
		if (gps.canGetLocation()) {

			if (gps.getLatitude() == 0.0 && count < 1) {
				locationFinder(ctx); count++;
			} else {
				Geocoder gcd = new Geocoder(ctx, Locale.getDefault());
				try {
					addsInfo = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
					// (28.416436, 77.0362959)

					if(Utilities.D)Log.v(TAG,"LocationList=" + addsInfo);
					if (addsInfo.size() > 0) {
//						if(Utilities.D)Log.v(TAG,"getAddressLine(0)="+addresses.get(0).getAddressLine(0));
//						if(Utilities.D)Log.v(TAG,"getAddressLine(1)="+addresses.get(0).getAddressLine(1));
//						if(Utilities.D)Log.v(TAG,"getLocality="+addresses.get(0).getLocality());
//						if(Utilities.D)Log.v(TAG,"getLocale="+addresses.get(0).getLocale());
//						if(Utilities.D)Log.v(TAG,"getFeatureName="+addresses.get(0).getFeatureName());
//						if(Utilities.D)Log.v(TAG,"getThoroughfare="+addresses.get(0).getThoroughfare());
					}
//					 gettingContinousCurrentLocation();

				} catch (Exception e) { e.printStackTrace(); }
			}
		} else {
			 gps.showSettingsAlert();
		}
		return addsInfo;
	}
    
//	public static List<Double> updateWithNewLocation(Context ctx) {
//    	List<Double> locationInfo = new ArrayList<Double>();
//
//	    LocationManager lm = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
//		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////	    TelephonyManager tm = (TelephonyManager)ctx.getSystemService(ctx.TELEPHONY_SERVICE);
//        boolean isGps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean isNetwork_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
////    	String IMEI_Number = tm.getDeviceId();
////	    String IMSI_Number = tm.getSubscriberId();
////	    String MOBILE_Number1 = tm.getLine1Number();
////	    String networkOperator = tm.getNetworkOperator();
//
//	    int stringId = ctx.getApplicationInfo().labelRes;
//	    String applicationName = ctx.getString(stringId);
//	    int currentapiVersion = Build.VERSION.SDK_INT;
//
//	    String androidId = Settings.Secure.getString(ctx.getContentResolver(),Settings.Secure.ANDROID_ID);
//	    String androidVersion = Build.VERSION.RELEASE;
////------------------------------------------------------------
//    	double lat = 0, lng = 0;
//	    String latLongString = "Unknown";
//	    String addressString = "No address found";
//	    DecimalFormat df = new DecimalFormat("##.00");
//
//        if (!isGps_enabled && !isNetwork_enabled) {
//        }
//
//	    if (location != null) {
//	        lat = location.getLatitude();
//	        lng = location.getLongitude();
//
//	        latLongString = "Lat:" +  df.format(lat) + " Long:" +  df.format(lng);
//	        Geocoder gc = new Geocoder(ctx, Locale.getDefault());
//		    if(Utilities.D)Log.d(TAG," geoCoder=" + gc.toString());
//	    }
//	    locationInfo.add(lat);
//	    locationInfo.add(lng);
//	    if(Utilities.D)Log.d(TAG,"lat="+ lat + " lng=" + lng);
//	    if(Utilities.D)Log.d(TAG,"latLongString="+ latLongString + " addressString=" + addressString);
////	    if(Utilities.D)Log.d(TAG,"IMEI_Number="+ IMEI_Number + " IMSI_Number=" + IMSI_Number + " MOBILE_Number1=" + MOBILE_Number1 + " networkOperator=" + networkOperator);
//	    if(Utilities.D)Log.d(TAG,"applicationName="+ applicationName + " currentapiVersion=" + currentapiVersion + " androidId=" + androidId + " androidVersion=" + androidVersion);
//
//	    return locationInfo;
//	}
	
	   public static void showGPSDisabledAlertToUser(final Context ctx){
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
	        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
	        .setCancelable(false)
	        .setPositiveButton("Goto Settings Page To Enable GPS",
	                new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                ctx.startActivity(callGPSSettingIntent);
	            }
	        });
	        
	        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	            }
	        });
	        AlertDialog alert = alertDialogBuilder.create();
	        alert.show();
	    }
	   
	   public static void getContactEmails(Context context) {
	        String emailIdOfContact = null;
	        int emailType = Email.TYPE_WORK;
	        String contactName = null;

	            ContentResolver cr = context.getContentResolver();
	            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	            if (cur.getCount() > 0) {
	                while (cur.moveToNext()) {
	                    String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
	                    contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	                    // Log.i(TAG,"....contact name....." +
	                    // contactName);

	                    cr.query(
	                            Phone.CONTENT_URI,
	                            null,
	                            Phone.CONTACT_ID
	                                    + " = ?", new String[] { id }, null);

	                    Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + id, null, null);
	                    while (emails.moveToNext()) {
	                        emailIdOfContact = emails.getString(emails.getColumnIndex(Email.DATA));
	                        // Log.i(TAG,"...COntact Name ...."
	                        // + contactName + "...contact Number..."
	                        // + emailIdOfContact);
	                        emailType = emails.getInt(emails.getColumnIndex(Phone.TYPE));
	                    }
	                    emails.close();

	                }
	            }// end of contact name cursor
	            cur.close();
	    }
	   
public static List<CsvContactSetter> readContacts(Context ctx){
	List<CsvContactSetter> contactInfo = new ArrayList<CsvContactSetter>();
	Cursor cur = null, cursor = null, cursor1 = null;
	String idx = "", name = "", phone = "", email = "";
    try {
 	 ContentResolver cr = ctx.getContentResolver();
   	 cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
       	while (cur.moveToNext()) {
       		idx = "";
       		name = "";
       		phone = "";
       		email = "";

		        idx = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		        name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//		        phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//		        Log.e(TAG, "Mobile Num: "+phone);

		        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//		        	System.out.println("name : " + name + ", ID : " + id);

		        	// get the phone number
		        	Cursor pCur = cr.query(Phone.CONTENT_URI,null,
		         		    			   Phone.CONTACT_ID +" = ?",
		         		    			   new String[]{idx}, null);
        	        while (pCur.moveToNext()) {
        	        	  phone = pCur.getString(
        	        			 pCur.getColumnIndex(Phone.NUMBER));
//        	        	  System.out.println("phone" + phone);
        	        }
        	        pCur.close();

        	        // get email and type
        	       Cursor emailCur = cr.query(
        	    			Email.CONTENT_URI,
        	    			null,
        	    			Email.CONTACT_ID + " = ?",
        	    			new String[]{idx}, null);
    	    		while (emailCur.moveToNext()) {
    	    		    // This would allow you get several email addresses
    	    	            // if the email addresses were stored in an array
    	    		    email = emailCur.getString(
    	    	                      emailCur.getColumnIndex(Email.DATA));
//    	    	 	    String emailType = emailCur.getString(
//    	    	                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

//    	    		  System.out.println("Email " + email + " Email Type : " + emailType);
        	    	}
       	    	emailCur.close();

	       		CsvContactSetter c = new CsvContactSetter();
	            c.setIdStr(idx);
	            c.setFirstName(name);
	            c.setMobNumber(phone);
	            c.setEmailIdx(email);
  	            contactInfo.add(c);
      		if(Utilities.D)Log.v(TAG,"name:" + name);
      		if(Utilities.D)Log.v(TAG,"phone:" + phone);
  	        if(Utilities.D)Log.v(TAG,"email:" + email);
		        }
       		}
        }
//-------------------------end of method ---------------------------------------
  	            
//    	cursor = ctx.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
////    	cursor1 = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//    	
////        String id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
////        cursor2 = ctx.getContentResolver().query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + id, null, null);
//        
////        int contactIdIdx = cursor.getColumnIndex(Phone._ID);
////        int nameIdx = cursor.getColumnIndex(Phone.DISPLAY_NAME);
////        int phoneNumberIdx = cursor.getColumnIndex(Phone.NUMBER);
////        int photoIdIdx = cursor.getColumnIndex(Phone.PHOTO_ID);
////        int EmailIdx = cursor.getColumnIndex(ContactsContract.Data._ID);
//        
////    	String[] projection = new String[] {
////                ContactsContract.Contacts._ID,
////                ContactsContract.Data.DISPLAY_NAME,
////                ContactsContract.CommonDataKinds.Email.DATA,
////                ContactsContract.CommonDataKinds.Phone.DATA, };
//    	
////        cursor1 = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
////                projection, null, null, "");
////        int idColumn = cursor.getColumnIndex(ContactsContract.Data._ID);
//        int nameColumn = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
////        int emailColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//        int phoneColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
//        
//        cursor.moveToFirst();
//        do {
//        	CsvContactSetter c = new CsvContactSetter();
//            
////          c.setId(cursor.getInt(idColumn));
//            c.setFirstName(cursor.getString(nameColumn));
//            c.setMobNumber(cursor.getString(phoneColumn));
////            c.setEmailIdx(cursor.getString(emailColumn));
//            if(Utilities.D)Log.v(TAG,"nameColumn=" + cursor.getString(nameColumn));
//            if(Utilities.D)Log.v(TAG,"phoneColumn=" + cursor.getString(phoneColumn));
////          if(Utilities.D)Log.v(TAG,"emailColumn=" + cursor.getString(emailColumn));
//
//            contactInfo.add(c);
//            //...
//        } while (cursor.moveToNext());  
//--------------------------seperation------------------------        
        } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (cur != null) {
            cur.close();
        }
    }
    if(Utilities.D)Log.d(TAG,"contactInfo="+contactInfo);
	if(Utilities.D)Log.v(TAG,"contactList.size()==" + contactInfo.size());
	
    return contactInfo;
}
//--------------------------------Apps Manager--------------------------------
public static void ShortcutIcon(Context ctx, Activity activity, String appName) {
	String PREFS_NAME = "PREFS_NAME";
	String PREF_KEY_SHORTCUT_ADDED = "PREF_KEY_SHORTCUT_ADDED";
	
	SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	boolean shortCutWasAlreadyAdded = mPrefs.getBoolean(PREF_KEY_SHORTCUT_ADDED, false);
	if (shortCutWasAlreadyAdded)
		return;

	Intent shortcutIntent = new Intent(ctx, activity.getClass());
	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	Intent addIntent = new Intent();
	addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
	addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(ctx, R.mipmap.ic_launcher));
	addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	ctx.sendBroadcast(addIntent);

	SharedPreferences.Editor editor = mPrefs.edit();
	editor.putBoolean(PREF_KEY_SHORTCUT_ADDED, true);
	editor.commit();
	if(Utilities.D)Log.d(TAG,"Added Successfully");
}

//-------------------------------STRING BUILDER EMAIL CHECK-------------------------------
public static boolean checkEmail(String email) {
	return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
}
public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
		.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
				+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
				+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

public static String makeFirstLetterCaps(String str){
	
	if(!(str.isEmpty())){
		 if(str.length() > 0){
				StringBuilder sb = new StringBuilder();
				sb.append(str);
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0))); 
//				if(Utilities.D)Log.v(TAG,"makeFirstCapsFirstLetter=" + sb.toString());
				return sb.toString();
		 } else
			return str;
	} else
		return str;
}
//--------------------------====Colors---------------------------------
public static int getRandomColor(Context ctx, String str){
	char ch = 'a';
	if(str.length() > 0)
		ch = str.charAt(0);
//	Log.e(TAG, "str-------"+str+" ch------------------"+ch);
	
	Random rnd = new Random();
	int[] androidColors = ctx.getResources().getIntArray(R.array.androidcolors);
	int randomColor = androidColors[rnd.nextInt(androidColors.length)];
	
	switch (ch) {
	case 'a':
		randomColor = androidColors[0];
		break;
	case 'b':
		randomColor = androidColors[1];
		break;
	case 'c':
		randomColor = androidColors[2];
		break;
	case 'd':
		randomColor = androidColors[3];
		break;
	case 'e':
		randomColor = androidColors[4];
		break;
	case 'f':
		randomColor = androidColors[5];
		break;
	case 'g':
		randomColor = androidColors[6];
		break;
	case 'h':
		randomColor = androidColors[7];
		break;
	case 'i':
		randomColor = androidColors[8];
		break;
	case 'j':
		randomColor = androidColors[9];
		break;
	case 'k':
		randomColor = androidColors[10];
		break;
	case 'l':
		randomColor = androidColors[11];
		break;
	case 'm':
		randomColor = androidColors[12];
		break;
	case 'n':
		randomColor = androidColors[13];
		break;
	case 'o':
		randomColor = androidColors[14];
		break;
	case 'p':
		randomColor = androidColors[15];
		break;
	case 'q':
		randomColor = androidColors[16];
		break;
	case 'r':
		randomColor = androidColors[17];
		break;
	case 's':
		randomColor = androidColors[18];
		break;
	case 't':
		randomColor = androidColors[19];
		break;
	case 'u':
		randomColor = androidColors[20];
		break;
	case 'v':
		randomColor = androidColors[21];
		break;
	case 'w':
		randomColor = androidColors[22];
		break;
		
	case 'x':
		randomColor = androidColors[23];
		break;
	case 'y':
		randomColor = androidColors[24];
		break;
	case 'z':
		randomColor = androidColors[25];
		break;
		
	case 'A':
		randomColor = androidColors[0];
		break;
	case 'B':
		randomColor = androidColors[1];
		break;
	case 'C':
		randomColor = androidColors[2];
		break;
	case 'D':
		randomColor = androidColors[3];
		break;
	case 'E':
		randomColor = androidColors[4];
		break;
	case 'F':
		randomColor = androidColors[5];
		break;
	case 'G':
		randomColor = androidColors[6];
		break;
	case 'H':
		randomColor = androidColors[7];
		break;
	case 'I':
		randomColor = androidColors[8];
		break;
	case 'J':
		randomColor = androidColors[9];
		break;
	case 'K':
		randomColor = androidColors[10];
		break;
	case 'L':
		randomColor = androidColors[11];
		break;
	case 'M':
		randomColor = androidColors[12];
		break;
	case 'N':
		randomColor = androidColors[13];
		break;
	case 'O':
		randomColor = androidColors[14];
		break;
	case 'P':
		randomColor = androidColors[15];
		break;
	case 'Q':
		randomColor = androidColors[16];
		break;
	case 'R':
		randomColor = androidColors[17];
		break;
	case 'S':
		randomColor = androidColors[18];
		break;
	case 'T':
		randomColor = androidColors[19];
		break;
	case 'U':
		randomColor = androidColors[20];
		break;
	case 'V':
		randomColor = androidColors[21];
		break;
	case 'W':
		randomColor = androidColors[22];
		break;
		
	case 'X':
		randomColor = androidColors[23];
		break;
	case 'Y':
		randomColor = androidColors[24];
		break;
	case 'Z':
		randomColor = androidColors[25];
		break;
		
	default:
		break;
	}
	
	return randomColor;
}
//-----------------------Encripting_HASHING_SHA1_SHA256_BASE64----------------------
public static String getSha1Base64(String base) {
	String sha1 = "";
	try {
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(base.getBytes("UTF-8"));
		sha1 = byteToHex(crypt.digest());

		byte[] byte_data = sha1.getBytes("UTF-8");
		String base64 = Base64.encodeToString(byte_data, Base64.DEFAULT);

		Log.w(TAG, "sha1, sha1.toString()=" + sha1);
		Log.w(TAG, "base, base64=" + base64);

		return base64;
	}
	catch(NoSuchAlgorithmException e) {
		e.printStackTrace();
		return null;
	}
	catch(UnsupportedEncodingException e) {
		e.printStackTrace();
		return null;
	}
}
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	public static String getCombination(String mobNum){
		String COMBINATION = "";
		for(int i = 0; i < mobNum.length(); i++) {
			COMBINATION = COMBINATION + mobNum.charAt(i) + Host.SECRET.charAt(i);
		}
		return COMBINATION;
	}
	public static String getCombinationColon(String password){
		String COMBINATION = "";
		Log.e(TAG, "password="+password);
		for(int i = 0; i < password.length(); i++) {
			COMBINATION = COMBINATION + password.charAt(i);
			COMBINATION = COMBINATION + "::";
			COMBINATION = COMBINATION + Host.SECRET.charAt(i);
		}
		Log.e(TAG, "COMBINATION="+COMBINATION);
		return COMBINATION;
	}
public static String getSha256Base64(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
		byte[] byte_data = hexString.toString().getBytes("UTF-8");
		String base64 = Base64.encodeToString(byte_data, Base64.DEFAULT);

		Log.w(TAG, "sha256, hexString.toString()=" + hexString.toString());
		Log.w(TAG, "base, base64=" + base64);

        return base64;
    } catch(Exception ex){
    	ex.printStackTrace();
    	return null;
//       throw new RuntimeException(ex);
    }
}

public static String printKeyHash(Activity context) {
	PackageInfo packageInfo;
	String key = null;
	try {
		String packageName = context.getApplicationContext().getPackageName();
		packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
		Log.e("Package Name=", context.getApplicationContext().getPackageName());

		for (Signature signature : packageInfo.signatures) {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(signature.toByteArray());
			key = new String(Base64.encode(md.digest(), 0));
			// String key = new String(Base64.encodeBytes(md.digest()));
			Log.d("Key Hash:", key);
		}
	} catch (NameNotFoundException e1) {
		Log.e("Name not found", e1.toString());
	} catch (NoSuchAlgorithmException e) {
		Log.e("No such an algorithm:", e.toString());
	} catch (Exception e) {
		Log.e("Exception:", e.toString());
	}
	return key;
}
public static boolean isStringInt(String s) {
    try {
        Integer.parseInt(s);
        return true;
    } catch (NumberFormatException ex){
        return false;
    }
}
public static String ordinal(int i) {
    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
    switch (i % 100) {
    case 11:
    case 12:
    case 13:
        return i + "th";
    default:
        return i + sufixes[i % 10];

    }
}
//-------------------------------Date-Time Utils--------------------------------
public static Date currentDate() {
    Calendar calendar = Calendar.getInstance();
    return calendar.getTime();
}
public static String getTimeAgo(Date date, Context ctx) {

    if(date == null) {
        return null;
    }

    long time = date.getTime();

    Date curDate = currentDate();
    long now = curDate.getTime();
    if (time > now || time <= 0) {
        return null;
    }

    int dim = getTimeDistanceInMinutes(time);

    String timeAgo = null;

    if (dim == 0) {
        timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " +  ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
    } else if (dim == 1) {
        return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
    } else if (dim >= 2 && dim <= 44) {
        timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
    } else if (dim >= 45 && dim <= 89) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_an)+ " " + ctx.getResources().getString(R.string.date_util_unit_hour);
    } else if (dim >= 90 && dim <= 1439) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
    } else if (dim >= 1440 && dim <= 2519) {
        timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
    } else if (dim >= 2520 && dim <= 43199) {
        timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
    } else if (dim >= 43200 && dim <= 86399) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_month);
    } else if (dim >= 86400 && dim <= 525599) {
        timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
    } else if (dim >= 525600 && dim <= 655199) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_year);
    } else if (dim >= 655200 && dim <= 914399) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_over) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_year);
    } else if (dim >= 914400 && dim <= 1051199) {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_almost) + " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
    } else {
        timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
    }

    return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
}

private static int getTimeDistanceInMinutes(long time) {
      long timeDistance = currentDate().getTime() - time;
      return Math.round((Math.abs(timeDistance) / 1000) / 60);
}
//------------------------------------------ends date time 
public static GradientDrawable backgroundWithBorder(int bgcolor, int brdcolor) {
    GradientDrawable gdDefault = new GradientDrawable();
    gdDefault.setColor(bgcolor);
    gdDefault.setStroke(2, brdcolor);
    gdDefault.setCornerRadii(new float[] { 6, 6, 0, 0, 0, 0, 6, 6 });
    return gdDefault;
}
//-------------------------------Fragments---------------------------------
public static void addFragment(FragmentActivity fActivity, Fragment f, int container, boolean isAnimate, boolean isBackstack) {

	FragmentTransaction transection = fActivity.getSupportFragmentManager().beginTransaction();
	if (!f.isAdded()) {
		try {
			if(isBackstack){
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.add(container, f)
				.addToBackStack(null)
				.commit();
			}else {			
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.add(container, f)
				.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	} else
		transection.show(f);
}

public static void addCompatFragment(AppCompatActivity fActivity, Fragment f, int container, String tag, boolean isAnimate, boolean isBackstack) {

	FragmentTransaction transection = fActivity.getSupportFragmentManager().beginTransaction();
	if (!f.isAdded()) {
		try {
			if(isBackstack){
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.add(container, f, tag)
				.addToBackStack(null)
				.commit();
			}else {			
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.add(container, f, tag)
				.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	} else
		transection.show(f);
}

public static void replaceFragment(FragmentActivity fActivity, Fragment f, int container, boolean isAnimate, boolean isBackstack) {

	FragmentTransaction transection = fActivity.getSupportFragmentManager().beginTransaction();
	if (!f.isAdded()) {
		try {
			if(isBackstack){
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.replace(container, f)
				.addToBackStack(null)
				.commit();
			} else {
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.replace(container, f)
				.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else
		transection.show(f);
}

public static void replaceCompatFragment(AppCompatActivity fActivity, Fragment f, int container, String tag, boolean isAnimate, boolean isBackstack) {

	FragmentTransaction transection = fActivity.getSupportFragmentManager().beginTransaction();
	if (!f.isAdded()) {
		try {
			if(isBackstack){
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.replace(container, f, tag)
				.addToBackStack(null)
				.commit();
			} else {
				if(Utilities.D)Log.v(TAG,"isBackstack="+isBackstack);
				if(isAnimate)
					transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop);
				transection.replace(container, f, tag)
				.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else
		transection.show(f);
}

public static void removeFragment(FragmentActivity fActivity, Fragment f) {

	FragmentTransaction transection = fActivity.getSupportFragmentManager().beginTransaction();
	if (!f.isAdded()) {
		try {
			transection.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter_pop, R.anim.exit_pop)
			.remove(f)
			.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else
		transection.show(f);
}

//---------------------------END----Fragments---------------------------------

	// -----------------------Dialog Manager-------------------------------
	public static void OpenDialog(Context ctx, int layout) {
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(layout);
		final Window window = dialog.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}

	public static void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.mipmap.ic_success : R.mipmap.ic_fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	   public static void showAlertFinish(final Activity ctx, String title, String msg, boolean cancelable){
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
	        alertDialogBuilder.setTitle(title);
	        alertDialogBuilder.setMessage(msg)
	        .setCancelable(cancelable)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	            	dialog.cancel();
	    			ctx.finish(); ctx.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	            }
	        });
	        
	        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	            }
	        });
	        AlertDialog alert = alertDialogBuilder.create();
	        alert.show();
	    }

	// ----------------------ThatsIt-----------------------------------------
		public static void Custom(Context context, String message, int duration) {
		    for (int i = 0; i < duration; i++) {
		        Toast toast = new Toast(context);
		        toast.setDuration(Toast.LENGTH_SHORT);
		        toast.setGravity(Gravity.BOTTOM, 0, 0);
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View view = inflater.inflate(R.layout.custom_toast, null);
		        TextView textViewToast = (TextView) view.findViewById(R.id.textViewToast);
		        textViewToast.setText(message);
		        toast.setView(view);
		        toast.show();
		    	}
			}
		public static void Snack(CoordinatorLayout mCoordinator, String message) {
		final Snackbar snack = Snackbar.make(mCoordinator, message, Snackbar.LENGTH_LONG);
//		snack.setActionTextColor(Color.RED);
//		snack.setAction("DISMISS", new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				snack.dismiss();
//			}
//		});
		View snackbarView = snack.getView();
//		snackbarView.setBackgroundColor(Color.DKGRAY);
		snackbarView.setBackgroundColor(Color.parseColor("#1789ce"));
		snackbarView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				snack.dismiss();
			}
		});
		TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.WHITE);
		snack.show();
		}
		
	public static void setActionbarTextColor(Activity activity, int color) {
		Field mActionViewField;
		try {
			mActionViewField = activity.getActionBar().getClass().getDeclaredField("mActionView");
			mActionViewField.setAccessible(true);
			Object mActionViewObj = mActionViewField.get(activity.getActionBar());

			Field mTitleViewField = mActionViewObj.getClass().getDeclaredField("mTitleView");
			mTitleViewField.setAccessible(true);
			Object mTitleViewObj = mTitleViewField.get(mActionViewObj);

			TextView mActionBarTitle = (TextView) mTitleViewObj;
			mActionBarTitle.setTextColor(color);
			// Log.i("field", mActionViewObj.getClass().getName());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView, BaseAdapter listAdapter) {
//		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter(); 
           if (listAdapter == null) {
               // pre-condition
               return;
           }

           int totalHeight = 0;
           for (int i = 0; i < listAdapter.getCount(); i++) {
               View listItem = listAdapter.getView(i, null, listView);
               listItem.measure(0, 0);
               totalHeight += listItem.getMeasuredHeight();
           }

           ViewGroup.LayoutParams params = listView.getLayoutParams();
           params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
           listView.setLayoutParams(params);
           listView.requestLayout();
       }
	
    public static void startAnimationThreadStuff(long delay, final CircularProgressView progressView) {
        if(updateThread != null && updateThread.isAlive())
            updateThread.interrupt();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start animation after a delay so there's no missed frames while the app loads up
                progressView.setProgress((int) 0f);
                progressView.startAnimation(); // Alias for resetAnimation, it's all the same
                // Run thread to update progress every half second until full
                updateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressView.getProgress() < progressView.getMaxProgress() && !Thread.interrupted()) {
                            // Must set progress in UI thread
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressView.setProgress(progressView.getProgress() + 10);
                                }
                            });
                            SystemClock.sleep(250);
                        }
                    }
                });
                updateThread.start();
            }
        }, delay);
    }
    
    public static Dialog showProgressDialog(final Context context, final AsyncTask<String, String, String> asynTask) {

		CircularProgressView progressView;
		final Dialog dialog;
		
		dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(R.layout.custom_progress_dialog);
	    
	    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					
					asynTask.cancel(true);
					dialog.dismiss();
					if(Utilities.D)Log.v(TAG,"Dialog cancelled on KEYCODE_BACK");
				}
				return false;
			}
		});

		progressView = (CircularProgressView) dialog.findViewById(R.id.progressView);
		
		startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		
	    dialog.setCancelable(false);
	    
		return dialog;
	}
    public static Dialog showProgressDialog(final Context context) {

		CircularProgressView progressView;
		final Dialog dialog;
		
		dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(R.layout.custom_progress_dialog);
	    
	    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					
					if(Utilities.D)Log.v(TAG,"Dialog cancelled on KEYCODE_BACK");
				}
				return false;
			}
		});

		progressView = (CircularProgressView) dialog.findViewById(R.id.progressView);
		
		startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		
	    dialog.setCancelable(false);
	    
		return dialog;
	}
	public static final String[] IMAGES = new String[] {
		"https://lh4.googleusercontent.com/-e9NHZ5k5MSs/URqvMIBZjtI/AAAAAAAAAbs/1fV810rDNfQ/s1024/Yosemite%252520Tree.jpg",
		// Light images
		"http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
		// Special cases
		"http://cdn.urbanislandz.com/wp-content/uploads/2011/10/MMSposter-large.jpg", // Very large image
		"file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
		"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
		"drawable://" + R.mipmap.user_type, // Image from drawables
		"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_�?_мышами_воевал.png", // Link with UTF-8
		"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
		"http://bit.ly/soBiXr", // Redirect link
		"http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
		"http://wrong.site.com/corruptedLink", // Wrong link
	};

	private Utilities() {}
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
}