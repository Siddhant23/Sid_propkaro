package com.propkaro;

import org.json.JSONObject;

import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment {
	String TAG = LoginFragment.class.getSimpleName();
    private CoordinatorLayout mCoordinator;
	Button btn_login, btn_skip;
	EditText et_emailaddr, et_passwd;
	Context ctx;
	SharedPreferences mPrefs;
	TextView tv_forgot_password;
	String getPassword = "", getEmail = "", gcmId = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.fragment_login, container, false);
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		Log.d(TAG, "Fragment.onCreate()");
		
		et_emailaddr = (EditText) rView.findViewById(R.id.et_emailaddr);
		et_passwd = (EditText) rView.findViewById(R.id.et_passwd);
		tv_forgot_password = (TextView) rView.findViewById(R.id.frgt_pwd);
		btn_login = (Button) rView.findViewById(R.id.btn_login);
		btn_skip = (Button) rView.findViewById(R.id.btn_skip);

		tv_forgot_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Utilities.replaceFragment(getActivity(), new ForgotPasswdFragment(), R.id.container, true, false);
			}
		});
		
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.d(TAG, "Clicked on login");
				getEmail = et_emailaddr.getText().toString().trim();
				getPassword = et_passwd.getText().toString().trim();

				if (getEmail.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter correct email id");
				} else if (!Utilities.checkEmail(getEmail)) {
					Utilities.Snack(mCoordinator, "Please enter correct email id");
				} else if (getPassword.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter password");
				} else {
					String url = "http://propkaro.com/api/loginUserAPI.php?"
							+ "email=" + getEmail + "&" + "password="
							+ getPassword;

					if (Utilities.isInternetOn(ctx))
						new LoginTask().execute(url);
					else
						Log.d(TAG, "Please check network connection.");
				}
			}
		});

		return rView;
	}//endOncreaView

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		gcmId = mPrefs.getString("PREF_KEY_GCM_ID", "");
	}

	class LoginTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_id = "", 
				api_user_type = "", api_userName = "", api_fName = "", api_lName = "", api_about_u = "", api_phone_no = "",
				api_image = "", api_half_image = "", api_city = "", api_country_code = "", api_company_name = "", 
				api_company_address = "", api_use_company_name = "", api_show_phone = "", 
				api_activate = "", api_facebook_id = "", api_google_id = "", api_registered_on = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = Utilities.showProgressDialog(ctx, LoginTask.this);
			dialog.show();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Utilities.Snack(mCoordinator, "Request cancelled !");
		}
		
		@Override
		protected String doInBackground(String... f_url) {
			try {
					Log.d(TAG, "et_emailaddr== " + et_emailaddr.getText().toString());
					Log.d(TAG, "et_passwd== " + et_passwd.getText().toString());
					
//					String str_send_json = "{\"email\":\"abc@xyz.com\", \"password\":\"hhhhhh\", \"submit\":\"login\"}";

					String LOGIN_API = 
							"{" +
							"\"email\":\""
							+ et_emailaddr.getText().toString()
							+ "\", \"password\":\""
							+ et_passwd.getText().toString()
							+ "\", \"submit\":\"login\"" 
							+ "}";
				String UrlBase = Host.LoginUrl + "";
//				String jsonString = Utilities.excutePost(UrlBase, LOGIN_API);
				String jsonString = Utilities.sendData(UrlBase, LOGIN_API);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				
				if(api_code == 200){
					JSONObject data = (JSONObject) reader.get("data");
					api_id = data.getString("id");
					api_phone_no = data.getString("phone_no");
					api_user_type = data.getString("user_type");
					api_activate = data.getString("activate");
					
					api_fName = Utilities.makeFirstLetterCaps(data.getString("fname"));
					api_lName = Utilities.makeFirstLetterCaps(data.getString("lname"));
					api_userName = api_fName + " " + api_lName;
					
					api_about_u = data.getString("about_u");
					api_country_code = data.getString("country_code");
					api_company_name = data.getString("company_name");
					api_use_company_name = data.getString("use_company_name");
					api_company_address = data.getString("company_address");
					if(data.getString("image").contains("http"))
						api_image = data.getString("image");
					else
						api_image = Host.MainUrl + data.getString("image");
					
					api_half_image = data.getString("image");
					api_city = data.getString("city");
					api_facebook_id = data.getString("facebook_id");
					api_google_id = data.getString("google_id");
//					if(Utilities.D)Log.v(TAG,"api_id=" + api_id);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}//endOncreaView

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();
			
			if (api_code == 200) {
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_ACTIVATE", api_activate);
				editor.putString("PREF_KEY_MOB_NUM", api_phone_no);
				editor.putString("PREF_KEY_EMAIL_ID", getEmail);
				editor.putString("PREF_KEY_USER_DataID", api_id);
				editor.putString("PREF_KEY_USER_TYPE", api_user_type);
				editor.putString("PREF_KEY_USER_NAME", api_userName);
				editor.putString("PREF_KEY_FIRST_NAME", api_fName);
				editor.putString("PREF_KEY_LAST_NAME", api_lName);
				editor.putString("PREF_KEY_CITY_NAME", api_city);
				
				editor.putString("PREF_KEY_ABOUT_U", api_about_u);
				editor.putString("PREF_KEY_COMPANY_CODE", api_country_code);
				editor.putString("PREF_KEY_COMPANY_NAME", api_company_name);
				editor.putString("PREF_KEY_USE_COMPANY_NAME", api_use_company_name);
				editor.putString("PREF_KEY_COMPANY_ADDRESS", api_company_address);
				
				editor.putString("PREF_KEY_FACEBOOK_ID", api_facebook_id);
				editor.putString("PREF_KEY_GOOGLE_ID", api_google_id);
				editor.putString("PREF_KEY_PROFILE_IMAGE", api_image);
				editor.putString("PREF_KEY_HALF_IMAGE", api_half_image);
				editor.commit();
				
				Intent intnt = new Intent(getActivity(), GridActivity.class);
				startActivity(intnt);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				
//				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
				
				new UpdateGcmIdTask().execute("");
				
			} else if (api_code == 400) { //501, 401
				Utilities.Snack(mCoordinator, "Invalid credential: " + api_message);
			} else if (api_code == 501) { //501 (Not Active)
				Utilities.Snack(mCoordinator, api_message);
			} else {
				Utilities.Snack(mCoordinator, "Something went wrong, please try again !");
			}
		}
	}
	
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
						+ getEmail +"\"},\"params\":{\"gcm_id\":\"" + gcmId + "\"}}";
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
}
