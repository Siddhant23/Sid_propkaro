package com.propkaro;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
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

import java.net.URLEncoder;
import java.util.ArrayList;

public class RegisterNumberFragment extends Fragment {
	private String TAG = RegisterNumberFragment.class.getSimpleName();

	private Button btn_forgot;
	private String getOTP = "", getFullName = "", getFName = "", getLName = "", getEmail = "", getPassword = "",
			getMobNumber = "", getUserType = "", getUserDataID = "", getToDo = "";

	private EditText et_mob_number;
	private Context ctx; TinyDB db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.fragment_reg_num, container, false);
		if(Utilities.D)Log.d(TAG, "Fragment.onCreateView()");

		TextView tv_title = (TextView) rView.findViewById(R.id.tv_title);
		tv_title.setText("Welcome to PropKaro");
		tv_title.setVisibility(View.GONE);
		TextView tv_sub_title = (TextView) rView.findViewById(R.id.tv_sub_title);
		tv_sub_title.setText("Enter your Mobile Number \nand we'll send you One Time Password");
		tv_sub_title.setVisibility(View.VISIBLE);
		et_mob_number = (EditText) rView.findViewById(R.id.et_email_address);
		et_mob_number.setInputType(InputType.TYPE_CLASS_NUMBER);
		et_mob_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(et_mob_number.length() > 9){
					Utilities.hideKeyboard(getActivity());
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		et_mob_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
		et_mob_number.setHint("Enter Mobile Number");
		
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
						new RegisterNumberTask().execute(Host.INDIA_CODE + getMobNumber);
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
		ctx = getActivity(); db = new TinyDB(ctx);
	}

	class RegisterNumberTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		ArrayList<String> citiesList = new ArrayList<String>();
		String api_message = "", api_mobileNum = "";
		Dialog dialog;

		@Override
		protected void onCancelled(String result) {
			super.onCancelled(result);
			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = Utilities.showProgressDialog(getActivity(), RegisterNumberTask.this);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
//				R_TOKEN = Utilities.getSha1Base64(Utilities.getCombination(f_url[0]));
				String R_TOKEN = Utilities.getSha256Base64(Utilities.getCombination(f_url[0]));
				String PARAMS = "r_token=" + URLEncoder.encode(R_TOKEN, "UTF-8")
				+ "&phone_no=" + URLEncoder.encode(f_url[0], "UTF-8");
				String UrlBase = Host.MainUrl + Host.UserRegister;
				String jsonString = Utilities.sendData(UrlBase, PARAMS);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject head = (JSONObject) reader.get("head");
				api_code = head.getInt("code");
				api_message = head.getString("message");

				if(api_code == 200){
					JSONObject body = (JSONObject) reader.get("body");
					JSONArray cities = (JSONArray) body.get("cities");
					for (int j = 0; j < cities.length(); j++) {
						citiesList.add((String) cities.get(j));
					}
					db.putListString("PREF_KEY_CITIES_ALL", citiesList);
				}
			} catch (Exception e) { e.printStackTrace(); }

			return f_url[0];
		}
		@Override
		protected void onPostExecute(String f_url) {
			dialog.dismiss();
			if(Utilities.D)Log.v(TAG,"api_code==" + api_code);
			if (api_code == 200) {
				Bundle args = new Bundle();
				args.putString("KEY_getMobNumber", f_url);
				Fragment f = new OTPFragmgent();
				f.setArguments(args);
				Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
				Toast.makeText(getActivity(), "OTP code sent by sms !", Toast.LENGTH_SHORT).show();
			} else if (api_code == 400) {
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			} else if (api_code == 420) { //501, 401
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
			}
		}
	}
	}
