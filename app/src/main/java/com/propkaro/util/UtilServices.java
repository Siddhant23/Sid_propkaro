package com.propkaro.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.propkaro.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/* Written by: Hariom Gupta
 * Android Developer
 * Ph no: +91-8510887828
 * email: hk.mca08@gmail.com
 * skype: hari_gupta11
 * last modified: 12Nov2015
 */

public class UtilServices {
	private static String TAG = UtilServices.class.getSimpleName();
	static Context ctx;
	static List<String> listMsgConn = new ArrayList<String>();
	static TextView tv_msg, tv_conn, tv_notify;

	public static List<String> getMessage(Activity cx, String id){
		ctx = cx;
//		tv_msg = (TextView)cx.findViewById(R.id.tv_msg136);
//		tv_conn = (TextView)cx.findViewById(R.id.tv_connection198);
//		tv_notify = (TextView)cx.findViewById(R.id.tv_notification);
		
		if(Utilities.isInternetOn(ctx))
			new AllNotificationTask().execute(id);

		return listMsgConn;
	}

	static class AllNotificationTask extends AsyncTask<String, String, String> {
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
					String MESSAGE_API =
							"{"
							+ "\"id\":"
							+ "\"" + f_url[0] + "\""
							+ "}";

				String UrlBase = Host.AllNotificationsUrl + "";
				String jsonString = Utilities.sendData(UrlBase, MESSAGE_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					listMsgConn.clear();
					
					JSONObject data = (JSONObject) reader.get("data");
					JSONArray api_messages_user = (JSONArray)data.get("messages");
					JSONArray api_connections_user = (JSONArray)data.get("connections");
					JSONArray api_notifications_user = (JSONArray)data.get("notifications");
					
					listMsgConn.add(api_messages_user.length()+"");
					listMsgConn.add(api_connections_user.length()+"");
					listMsgConn.add(api_notifications_user.length()+"");
					
					if(Utilities.D)Log.v(TAG,"listMsgConn=" + listMsgConn);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			tv_msg.setText(listMsgConn.get(0));
			tv_conn.setText(listMsgConn.get(1));
			tv_notify.setText(listMsgConn.get(2));
		}
	}
}