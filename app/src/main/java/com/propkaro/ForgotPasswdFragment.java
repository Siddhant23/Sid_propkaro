package com.propkaro;

import org.json.JSONObject;

import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ForgotPasswdFragment extends Fragment {
	private String TAG = ForgotPasswdFragment.class.getSimpleName();
	private CoordinatorLayout mCoordinator;

	private Button btn_forgot;
	private String getEmailAddress = "", getDataID = "";
	private EditText et_email_address;
	private Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.forgot_pwd, container, false);
		Log.d(TAG, "Fragment.onCreateView()");

		ctx = getActivity();
		mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		et_email_address = (EditText) rView.findViewById(R.id.et_email_address);
		btn_forgot = (Button) rView.findViewById(R.id.btn_forgot);
		btn_forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.hideKeyboard(getActivity());
				getEmailAddress = et_email_address.getText().toString().trim();

				if (getEmailAddress.length() == 0) {
					Utilities.Snack(mCoordinator, "Enter correct email address ");

				} else if (!Utilities.checkEmail(getEmailAddress)) {
					
					Utilities.Snack(mCoordinator, "Enter correct email address ");
				} else {
					if (Utilities.NetworkCheck(ctx) == true) {
						new FRGTPWDTask().execute("");
					} else
						Utilities.Snack(mCoordinator, "Oops! Internet Required");
					}
				}
		});
		
		return rView;
		};

		class FRGTPWDTask extends AsyncTask<String, String, String> {
			int api_code = 0;
			String api_message = "", api_userId = "", api_userType = "", 
					api_userName = "", api_mobileNum = "", api_otp = "";
			Dialog dialog;
			
			@Override
			protected void onCancelled(String result) {
				super.onCancelled(result);
				
				Utilities.Snack(mCoordinator, "Request cancelled !");
			}
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				dialog = Utilities.showProgressDialog(getActivity(), FRGTPWDTask.this);
				dialog.show();
			}
			
			@Override
			protected String doInBackground(String... f_url) {
				try {
						Log.d(TAG, "et_emailaddr== " + et_email_address.getText().toString());
						
						String str_send_json = "{\"email\":\""
								+ et_email_address.getText().toString()
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
				dialog.dismiss();
				
				if(Utilities.D)Log.v(TAG,"api_code==" + api_code);
				if (api_code == 200) {
					Bundle args = new Bundle();
					args.putString("KEY_getMobNumber", api_mobileNum);
					args.putString("KEY_getName", api_userName);
					args.putString("KEY_getEmail", getEmailAddress);
					args.putString("KEY_dataID", api_userId);
					args.putString("KEY_getUserType", api_userType);
					args.putString("KEY_getOTP", api_otp);
					args.putString("KEY_toDo", "FORGOT");
					
					Fragment f = new OTPFragmgent();
					f.setArguments(args);
					
					Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
					Utilities.Snack(mCoordinator, "OTP code sent to sms and mail !");
					
				} else if (api_code == 301) { 
					Utilities.Snack(mCoordinator, api_message);
					
				} else if (api_code == 400) { //501, 401
					Utilities.Snack(mCoordinator, api_message);
					
				} else if (api_code == 501) { //501 (Not Active)
					Utilities.Snack(mCoordinator, api_message);
					
				} else {
					Utilities.Snack(mCoordinator, "User Not registered !");
				}
			}
		}
}
