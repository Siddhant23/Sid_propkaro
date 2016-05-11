package com.propkaro.util;
 
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public abstract class NetworkHelper{
private static final String TAG = NetworkHelper.class.getSimpleName();
public static boolean Check(Context ctx) {
    ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(
	        Context.CONNECTIVITY_SERVICE);

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
}
