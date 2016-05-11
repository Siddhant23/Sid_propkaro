package com.propkaro;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class OTPFragmgent extends Fragment {
	private String TAG = OTPFragmgent.class.getSimpleName();
	private Button btn_verify;
	private TextView btn_resend;
	private String getOTP = "", getMobNumber = "";
	private EditText et_otp;
	private Context ctx; TinyDB db;
	boolean isRegistered = false;
	private SmsListener receivers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rView = inflater.inflate(R.layout.fragment_reg_otp, container, false);
		if(Utilities.D)Log.d(TAG, "Fragment.onCreateView()");
		
		et_otp = (EditText) rView.findViewById(R.id.et_phon_no);
		et_otp.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(et_otp.length() > 5){
					Utilities.hideKeyboard(getActivity());
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		TextView btn_edit_number = (TextView) rView.findViewById(R.id.btn_edit_number);
		btn_edit_number.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		btn_verify = (Button) rView.findViewById(R.id.btn_verify);
		btn_verify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.hideKeyboard(getActivity());
				getOTP = et_otp.getText().toString();
				
				if (Utilities.isInternetOn(ctx) == true) {
					if (getOTP.length() > 5) {
					
						new OTPTask().execute(getOTP);
					}else
						Toast.makeText(ctx, "Please check your OTP and try again !", Toast.LENGTH_SHORT).show();
				} else
					if(Utilities.D)Log.v(TAG,"No internet connection.. ");
			}
		});

		btn_resend = (TextView) rView.findViewById(R.id.btn_resend);
		btn_resend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			Toast.makeText(ctx, "Resending..", Toast.LENGTH_SHORT).show();
			if(Utilities.isInternetOn(ctx))
				new ResendOTPTask().execute(getMobNumber);
			}
		});

		return rView;
	}// endOncreaView

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegistered) {
			Log.e(TAG, "super.onDestroy()=Unregister");
			getActivity().unregisterReceiver(receivers);
			isRegistered = false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "super.onResume()=register");

		receivers = new SmsListener();
		getActivity().registerReceiver(receivers, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		isRegistered = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(TAG, "super.onPause()=Unregister");
		if (isRegistered) {
			getActivity().unregisterReceiver(receivers);
			isRegistered = false;
		}
	}

	public class SmsListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Utilities.hideKeyboard(getActivity());
			if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
				Bundle bundle = intent.getExtras();
				SmsMessage[] msgs = null;
				if (bundle != null) {
					try {
						Object[] pdus = (Object[]) bundle.get("pdus");
						msgs = new SmsMessage[pdus.length];
						
						for (int i = 0; i < msgs.length; i++) {
//							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//								String format = bundle.getString("format");
//								msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
//							} else {
							msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//							String msg_from = msgs[i].getOriginatingAddress();
							// Log.v("TAG", "msg_from=" + msg_from);

							String msgBody = msgs[i].getMessageBody();
							 Log.v("TAG", "msgBody=" + msgBody);
							 
							 if (msgBody == null || msgBody.length() < 6) {
							 } else {
								 if(msgBody.contains("Password")){
									 getOTP = msgBody.substring(msgBody.length() - 6);
									 if(Utilities.D)Log.v(TAG,"Found OTP Receiver: " + getOTP);
									 et_otp.setText("" + getOTP);
									 if (Utilities.isInternetOn(ctx)) {
										btn_verify.setEnabled(false);
										new OTPTask().execute(getOTP);
									 }
								 }
							 }
						}
					} catch (Exception e) {
						Log.d("Exception caught", e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = getActivity(); db = new TinyDB(ctx);
		Bundle b = this.getArguments();
		getMobNumber = b.getString("KEY_getMobNumber");
		if(Utilities.D)Log.d(TAG, "getMobNumber=" + getMobNumber);
	};

	public class MyCountDownTimer extends CountDownTimer {
		  public MyCountDownTimer(long startTime, long interval) {
			  super(startTime, interval);
			  btn_resend.setTextColor(Color.parseColor("#FFFF0000"));
		  }
		  @Override
		  public void onTick(long millisUntilFinished) {
			  btn_resend.setTextColor(Color.parseColor("#FFFF0000"));
			  btn_resend.setText("Can resend OTP in " + String.format("%02d:%02d", 
	                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
	                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
	                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
		  }
		  @Override
		  public void onFinish() {
			  btn_resend.setTextColor(Color.parseColor("#FF0000FF"));
			  SpannableString content = new SpannableString("Resend OTP");
			  content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			  btn_resend.setText(content);
			  btn_resend.setEnabled(true);
		  } ;
	}

	class OTPTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_password = "", api_phone_no = "", api_property_type = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(getActivity(), OTPTask.this);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String R_TOKEN = Utilities.getSha256Base64(Utilities.getCombination(getMobNumber));
				String PARAMS = "r_token=" + URLEncoder.encode(R_TOKEN, "UTF-8")
						+ "&phone_no=" + URLEncoder.encode(getMobNumber, "UTF-8")
						+ "&code=" + URLEncoder.encode(f_url[0], "UTF-8");
				String UrlBase = Host.MainUrl + Host.UserOTP;
				String jsonString = Utilities.sendData(UrlBase, PARAMS);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject head = (JSONObject) reader.get("head");
				api_code = head.getInt("code");
				api_message = head.getString("message");

				if(api_code == 200){
					JSONObject body = (JSONObject) reader.get("body");
					api_phone_no = body.getString("phone_no");
					api_password = body.getString("password");
					api_property_type = body.getString("property_type");
				}
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();
			
			if (api_code == 200) {
				db.putString("PREF_KEY_MOB_NUM", api_phone_no);
				db.putString("PREF_KEY_PASSWORD", api_password);
				db.putString("PREF_KEY_PROPERTY_TYPE", api_property_type);
				new LoginTask().execute(api_password);


				Intent i = new Intent(getActivity(), GridActivity.class);
				startActivity(i);

//				Bundle args = new Bundle();
//				args.putString("KEY_getMobNumber", api_phone_no);
//				Fragment f = new RegisterFragment();
//				f.setArguments(args);
//				Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);

				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			} else if(api_code == 400){
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			} else if(api_code == 420){
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			}
		}
	}

	class LoginTask extends AsyncTask<String, String, String> {
		public int api_code = 0;
		public String api_message = "", api_token = "", api_user = "";
		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = Utilities.showProgressDialog(getActivity(), LoginTask.this);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String PASSWORD = Utilities.getSha256Base64(Utilities.getCombinationColon(f_url[0]));
				String PARAMS = "password=" + URLEncoder.encode(PASSWORD, "UTF-8")
						+ "&phone_no=" + URLEncoder.encode(getMobNumber, "UTF-8");
				String UrlBase = Host.MainUrl + Host.UserLogin;
				String jsonString = Utilities.sendData(UrlBase, PARAMS);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject head = (JSONObject) reader.get("head");
				api_code = head.getInt("code");
				api_message = head.getString("message");
				if(api_code == 200){
					JSONObject body = (JSONObject) reader.get("body");
					api_token = head.getString("token");
					api_user = head.getString("user");
					Log.e(TAG, "API_USER_LOGIN="+api_user);
					db.putString("PREF_KEY_TOKEN", api_token);
					db.putString("PREF_KEY_USER_DETAILS", api_user);
				}
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();
			if (api_code == 200) {
				//FIXME: TO GO WHERE??
//				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			} else if(api_code == 400){
//				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			} else if(api_code == 420){
//				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			} else {
//				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			}
		}
	}
	class ResendOTPTask extends AsyncTask<String, String, String> {
		public int api_code = 0;
		public String api_message = "", api_token = "", api_user = "";
		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = Utilities.showProgressDialog(getActivity(), ResendOTPTask.this);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String R_TOKEN = Utilities.getSha256Base64(Utilities.getCombination(f_url[0]));
				String PARAMS = "r_token=" + URLEncoder.encode(R_TOKEN, "UTF-8")
						+ "&phone_no=" + URLEncoder.encode(f_url[0], "UTF-8");
				String UrlBase = Host.MainUrl + Host.UserOTPResend;
				String jsonString = Utilities.sendData(UrlBase, PARAMS);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject head = (JSONObject) reader.get("head");
				api_code = head.getInt("code");
				api_message = head.getString("message");
				if(api_code == 200){
				}
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			dialog.dismiss();
			if (api_code == 200) {
				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
