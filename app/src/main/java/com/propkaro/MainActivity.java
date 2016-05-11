package com.propkaro;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.propkaro.AppController.TrackerName;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

public class MainActivity extends AppCompatActivity {
	private static String TAG = MainActivity.class.getSimpleName();
	private Context ctx;
    private CoordinatorLayout mCoordinator;

	FragmentActivity fActivity;
	private SharedPreferences mPrefs;
	private boolean mShowingBack = false;
	String gcmId;
	String getCityName = "", getStreetAddress = "", getCountryCode = "", getCountryName = "";
	Dialog dialog_google;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this; fActivity = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		int SDK_INT = Build.VERSION.SDK_INT;
	    if (SDK_INT > 9){
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }
		setContentView(R.layout.activity_main);
		if(Utilities.D)Log.v(TAG,"--Checking Splash--");
		Tracker t = ((AppController) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Splash Login Screen");
		t.send(new HitBuilders.AppViewBuilder().build());
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

		String getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		String getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		String getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		String getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		gcmId = mPrefs.getString("PREF_KEY_GCM_ID", "");
		 if(gcmId.length() == 0){
			 GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
			 gcmId = GCMRegistrar.getRegistrationId(this);
			 if(gcmId.length() == 0){
				 gcmId = GCMRegistrar.getRegistrationId(this);
				 if(Utilities.D)Log.i(TAG,"@gcmId="+gcmId);
			 }
			 SharedPreferences.Editor editor = mPrefs.edit();
			 editor.putString("PREF_KEY_GCM_ID", gcmId);
			 editor.commit();
		 }
		getCityName = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getCountryCode = mPrefs.getString("PREF_KEY_COUNTRY_CODE", "");
		
		if(Utilities.D)Log.v(TAG,"getMobile_num="+getMobile_num);
		if(Utilities.D)Log.v(TAG,"getEmailID="+getEmailID);
		if(Utilities.D)Log.v(TAG,"getCityName="+getCityName);
		if(Utilities.D)Log.v(TAG,"gcmId="+gcmId);
		
//		 if (Build.VERSION.SDK_INT >= 23) {
//			 if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//		     } else {
//                 requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE, ctx.getApplicationContext(), ctx);
//             }
//         } else {
//         }
//------------------------------Main screen_layout----------------------------------------

		if(getEmailID == ""){//GET_CONDITION_TO_LOGIN_OR---
			Utilities.replaceFragment(fActivity, new RegisterNumberFragment(), R.id.container, false, true);
			if (Utilities.NetworkCheck(ctx))
				new DATA_UPDATED_TASK().execute("");
		} else {
//			Intent intnt = new Intent(getApplicationContext(), GridActivity.class);
//			startActivity(intnt);finish();
		}
		Utilities.ShortcutIcon(ctx, MainActivity.this, getResources().getString(R.string.app_name));
	}//endOncrea

//	class InitPropkaroTask extends AsyncTask<String, String, String> {
//		int api_code = 0;
//		public String api_message = "", api_dataID = "";
//		private Dialog dialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			
//			dialog = Utilities.showProgressDialog(ctx, InitPropkaroTask.this);
//			dialog.show();
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
////			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
//		}
//		@Override
//		protected String doInBackground(String... f_url) {
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String file_url) {
//			dialog.dismiss();
//			
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int ii = getSupportFragmentManager().getBackStackEntryCount();
			if(Utilities.D)Log.v(TAG,"getBackStackEntryCount()===" + ii);
			
			if(ii < 2){
				finish(); overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				
			} else 
				getSupportFragmentManager().popBackStack();
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
//------------------FacebookLogin---------------------
//------------------------------------End Google Login---------------------------------------------

	class UpdateGcmIdTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data, api_count="";
 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			gcmId = mPrefs.getString("PREF_KEY_GCM_ID", "");
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String GCM_API = 
						"{\"table\":\"registration\",\"by\":{\"email\":\""
						+ f_url[0] +"\"},\"params\":{\"gcm_id\":\"" + gcmId + "\"}}";
				String UrlBase = Host.UpdateUrl + "";
				String jsonString = Utilities.sendData(UrlBase, GCM_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					String data = reader.getString("data");
					if(Utilities.D)Log.v(TAG,"data="+data);
				} else {
			}
		} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
//			dialog.dismiss();

			if (api_code == 200) {
				if(Utilities.D)Log.v(TAG,"GCM id updated successfully!");
			} else {
			}
		}
	}
	
	public class DATA_UPDATED_TASK extends AsyncTask<String, String, JSONObject> {

		int api_code = 0;
		String api_message = "";
		int VERSION_UPDATES = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected JSONObject doInBackground(String... F_URL) {
				try {
					String str_send_json = "{\"by\":{\"setting_name\":\""
							+ "DATA_UPDATED"
							+ "\"}" +
							",\"table\":\"settings\",\"multiple\":\"All\"}";
					String UrlBase = Host.GetUrl;
					String jsonString = Utilities.sendData(UrlBase, str_send_json);
					
					if (jsonString != null) {
						JSONObject reader = new JSONObject(jsonString);

						JSONObject meta = (JSONObject) reader.get("meta");
						api_code = meta.getInt("code");
						api_message = meta.getString("message");

						JSONArray data = (JSONArray) reader.getJSONArray("data");
						JSONObject o = data.getJSONObject(0);
						VERSION_UPDATES = Integer.parseInt(o.getString("setting_value"));
					}
				} catch (Exception e) {
					e.printStackTrace();
			}
			JSONObject json = null;
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				int currentVersion = pInfo.versionCode;

				Log.v(TAG, "------------VERSION_UPDATES code=" + VERSION_UPDATES);
				Log.v(TAG, "------------versionUpdated code=" + currentVersion);

					if(currentVersion >= VERSION_UPDATES ){	
						Log.v(TAG, "------------app version not need to update(correc version condition)=" + currentVersion);
					} else {
						if(Utilities.D)Log.v(TAG,"------------app version need to update(show dialog to update)=" + currentVersion);
						showAlertFinish(MainActivity.this, "Version update required", "A new update of Propkaro is available. Please update to enhance your experience.", false);
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//    	if(Utilities.D)Log.e(TAG, "checking....per");
//        if (requestCode == Host.ACCOUNTS_PERMISSIONS_REQUEST) {
//        	if(Utilities.D)Log.e(TAG, "checking....ACCOUNTS_PERMISSIONS_REQUEST");
//			LoginToGoogleFragment();
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

	  public void showAlertFinish(final Activity ctx, String title, String msg, boolean cancelable){
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
	        alertDialogBuilder.setTitle(title);
	        alertDialogBuilder.setMessage(msg)
	        .setCancelable(cancelable)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	            	if(Utilities.D)Log.v(TAG,"------------app version (Dialog click ok button)=" );
					
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.propkaro")));
					} catch (Exception e) {
						Toast.makeText(ctx, "Playstore not found in your phone", Toast.LENGTH_LONG).show();
					}
//					finish();
//	            	dialog.cancel();
//	    			ctx.finish(); ctx.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	            }
	        });
	        
	        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
//	                dialog.cancel();
	    			ctx.finish(); ctx.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

	            }
	        });
	        AlertDialog alert = alertDialogBuilder.create();
	        alert.show();
	    }
}
