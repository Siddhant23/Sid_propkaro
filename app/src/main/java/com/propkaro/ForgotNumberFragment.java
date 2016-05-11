package com.propkaro;

import org.json.JSONObject;

import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ForgotNumberFragment extends Fragment {
	private String TAG = ForgotNumberFragment.class.getSimpleName();

	private Button btn_forgot;
	private String getOTP = "", getFullName = "", getFName = "", getLName = "", getEmail = "", getPassword = "",
			getMobNumber = "", getUserType = "", getUserDataID = "", getToDo = "";

	private EditText et_mob_number;
	private Context ctx;
	private SharedPreferences mPrefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.forgot_pwd, container, false);
		if(Utilities.D)Log.d(TAG, "Fragment.onCreateView()");

		TextView tv_title = (TextView) rView.findViewById(R.id.tv_title);
		tv_title.setText("Mobile number Verification");
		TextView tv_sub_title = (TextView) rView.findViewById(R.id.tv_sub_title);
		tv_sub_title.setText("Enter your Mobile Number \nand we'll send you One Time Password");
		et_mob_number = (EditText) rView.findViewById(R.id.et_email_address);
		et_mob_number.setInputType(InputType.TYPE_CLASS_NUMBER);
		et_mob_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
		et_mob_number.setHint("Please enter mobile number");
		
		btn_forgot = (Button) rView.findViewById(R.id.btn_forgot);
		btn_forgot.setText("SUBMIT");
		btn_forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMobNumber = et_mob_number.getText().toString().trim();

				if (getMobNumber.length() == 0) {
					Toast.makeText(getActivity(), "Please enter mobile number!", Toast.LENGTH_SHORT).show();

				} else if (getMobNumber.length() < 10) {
					Toast.makeText(getActivity(), "Please enter correct mobile number!", Toast.LENGTH_SHORT).show();
				} else{
					if (Utilities.isInternetOn(ctx) == true) {
						new AddMobileNumberTask().execute("");
					} else
						if(Utilities.D)Log.v(TAG,"No internet connection.. ");
					}
				}
		});
		
		return rView;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		Bundle b = this.getArguments();
		getMobNumber = b.getString("KEY_getMobNumber");
		getUserDataID = b.getString("KEY_dataID");
		getOTP = b.getString("KEY_getOTP");
		getFName = b.getString("KEY_getFName");
		getLName = b.getString("KEY_getLName");
		getEmail = b.getString("KEY_getEmail");
		getPassword = b.getString("KEY_getPassword");
		getUserType = b.getString("KEY_getUserType");
		getToDo = b.getString("KEY_toDo");

		if(Utilities.D)Log.d(TAG, "=====getOTP=" + getOTP);
		if(Utilities.D)Log.d(TAG, "=====getUserDataID=" + getUserDataID);
		if(Utilities.D)Log.d(TAG, "=====getFName=" + getFName);
		if(Utilities.D)Log.d(TAG, "=====getLName=" + getLName);
		if(Utilities.D)Log.d(TAG, "=====getEmail=" + getEmail);
		if(Utilities.D)Log.d(TAG, "=====getMobNumber=" + getMobNumber);
		if(Utilities.D)Log.d(TAG, "=====getUserType=" + getUserType);
		if(Utilities.D)Log.d(TAG, "=====getToDo=" + getToDo);
	}
		class AddMobileNumberTask extends AsyncTask<String, String, String> {
			int api_code = 0;
			String api_message = "", api_userType = "", 
					api_userName = "", api_mobileNum = "", api_otp = "";
			Dialog dialog;
			
			@Override
			protected void onCancelled(String result) {
				super.onCancelled(result);
				
				Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				dialog = Utilities.showProgressDialog(getActivity(), AddMobileNumberTask.this);
				dialog.show();
			}
			
			@Override
			protected String doInBackground(String... f_url) {
				try {
						if(Utilities.D)Log.d(TAG, "ET_MOBILE nUMBER== " + et_mob_number.getText().toString());
						
						String MOBILE_NUMBER_API = 
								"{\"by\":{\"email\":\""
						+ getEmail + "\"},\"table\":\"registration\",\"params\":{\"phone_no\":\""
						+ et_mob_number.getText().toString() + "\"}}";

					String UrlBase = Host.UpdateUrl + "";
					String jsonString = Utilities.sendData(UrlBase, MOBILE_NUMBER_API);
					JSONObject reader = new JSONObject(jsonString);
					JSONObject meta = (JSONObject) reader.get("meta");

					api_code = meta.getInt("code");
					api_message = meta.getString("message");
					
					if(api_code == 200){
//						JSONObject data = (JSONObject) reader.get("data");
//						api_id = data.getString("id");
//						api_phone_no = data.getString("phone_no");
//						api_user_type = data.getString("user_type");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}//endOncreaView

			@Override
			protected void onPostExecute(String file_url) {
				dialog.dismiss();
				
				if(Utilities.D)Log.v(TAG,"api_code==" + api_code);
				if (api_code == 200) {
					
					Bundle args = new Bundle();
					args.putString("KEY_getMobNumber", api_mobileNum);
					args.putString("KEY_getName", api_userName);
					args.putString("KEY_getEmail", getEmail);
					args.putString("KEY_dataID", getUserDataID);
					args.putString("KEY_getUserType", api_userType);
					args.putString("KEY_getOTP", api_otp);
					args.putString("KEY_toDo", "NUMBER");
					
					Fragment f = new OTPFragmgent();
					f.setArguments(args);
					
					Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
					Toast.makeText(getActivity(), "OTP code sent by sms and mail !", Toast.LENGTH_SHORT).show();
					
					new FRGTPWDTask().execute("");
					
				} else if (api_code == 301) { 
					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else if (api_code == 400) { //501, 401
					Bundle args = new Bundle();
					args.putString("KEY_getMobNumber", api_mobileNum);
					args.putString("KEY_getName", api_userName);
					args.putString("KEY_getEmail", getEmail);
					args.putString("KEY_dataID", getUserDataID);
					args.putString("KEY_getUserType", api_userType);
					args.putString("KEY_getOTP", api_otp);
					args.putString("KEY_toDo", "NUMBER");
					
					Fragment f = new OTPFragmgent();
					f.setArguments(args);
					
					Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
					Toast.makeText(getActivity(), "OTP code sent by sms and mail !", Toast.LENGTH_SHORT).show();

					new FRGTPWDTask().execute("");
					
//					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else if (api_code == 501) { //501 (Not Active)
					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else {
					Toast.makeText(getActivity(), "User Not registered !", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		class FRGTPWDTask extends AsyncTask<String, String, String> {
			int api_code = 0;
			String api_message = "", api_userId = "", api_userType = "";
			
			@Override
			protected void onCancelled(String result) {
				super.onCancelled(result);
			}
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
			}
			
			@Override
			protected String doInBackground(String... f_url) {
				try {
						if(Utilities.D)Log.d(TAG, "et_emailaddr== " + et_mob_number.getText().toString());
						
						String str_send_json = "{\"phone_no\":\""
								+ et_mob_number.getText().toString()
//								+ "\", \"password\":\""
//								+ et_passwd.getText().toString()
//								+ "\", \"submit\":\"login\"" +
								+ "\"}";

					String UrlBase = Host.ForgotUrl + "";
					String jsonString = Utilities.sendData(UrlBase, str_send_json);
					JSONObject reader = new JSONObject(jsonString);
					JSONObject meta = (JSONObject) reader.get("meta");

					api_code = meta.getInt("code");
					api_message = meta.getString("message");
					
					if(api_code == 200){
//						JSONObject data = (JSONObject) reader.get("data");
//						api_id = data.getString("id");
//						api_phone_no = data.getString("phone_no");
//						api_user_type = data.getString("user_type");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}//endOncreaView

			@Override
			protected void onPostExecute(String file_url) {
				if(Utilities.D)Log.v(TAG,"api_code==" + api_code);
				if (api_code == 200) {
				} else if (api_code == 301) { 
//					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else if (api_code == 400) { //501, 401
//					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else if (api_code == 501) { //501 (Not Active)
//					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
					
				} else {
//					Toast.makeText(getActivity(), "User Not registered !", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
