package com.propkaro.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
/* Written by: Hariom Gupta
 * Android Developer
 * Ph no: +91-8510887828
 * email: hk.mca08@gmail.com
 * skype: hari_gupta11
 * last modified: 12Nov2015
 */

public class UtilService {
	private static String TAG = UtilService.class.getSimpleName();
	static Context ctx;
	static String Response, UrlBase;

	public static String getMessage(Activity cx, String UrlBase, String API){
		ctx = cx;
		UtilService.UrlBase = UrlBase;
		
		if(Utilities.isInternetOn(ctx))
			new MsgConnTask().execute(API);
		
		return Response;
	}
	
	static class MsgConnTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {

				String jsonString = Utilities.sendData(UrlBase, f_url[0]);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					JSONObject data = (JSONObject) reader.get("data");
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return api_message;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			
			Response = api_message;
			if(Utilities.D)Log.v(TAG,"Response=" + Response);
		}
	}
}