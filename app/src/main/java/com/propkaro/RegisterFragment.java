package com.propkaro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {
	private static String TAG = RegisterFragment.class.getSimpleName();
	private CoordinatorLayout mCoordinator;
	String getUserType = "";
	String getFName = "";
	String getEmail = "";
	String getCityName = "";
	String getCompanyName = "";
	String getGcmID = "";
	Button btn_signup;
	EditText et_emailaddr,  et_fName, BTN_CITY;
	Context ctx;
	TinyDB db;
	ArrayList<String> api_city = new ArrayList<String>();
	EditText mSearchView;
	ListView mListView;
	ArrayAdapter<String> adapter_cities;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.register_parent, container, false);
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
//		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf");
		Log.d(TAG, "Fragment.onCreateView()");
		et_emailaddr = (EditText) rView.findViewById(R.id.et_emailaddr);
		et_emailaddr.setText(getEmail);

		et_fName = (EditText) rView.findViewById(R.id.et_fName);
		et_fName.setText(getFName);
		
		final ImageView ic_1 = (ImageView) rView.findViewById(R.id.ic_1);
		final ImageView ic_2 = (ImageView) rView.findViewById(R.id.ic_2);
		final ImageView ic_3 = (ImageView) rView.findViewById(R.id.ic_3);
		ic_1.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString()); 
		        ic_1.setImageResource(R.mipmap.individual_selected_icon);
		        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
		        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
				getUserType = "Individual";
		        return false;
		    }
		    });
		ic_2.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString());
			        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
			        ic_2.setImageResource(R.mipmap.agent_selected_icon);
			        ic_3.setImageResource(R.mipmap.builder_unseelcted_icon);
					getUserType = "Agent"; 
		        return false;
		    }
		    });
		ic_3.setOnTouchListener(new OnTouchListener() {
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i("IMAGE", "motion event: " + event.toString());
			        ic_1.setImageResource(R.mipmap.individual_unselected_icon);
			        ic_2.setImageResource(R.mipmap.agent_unselected_icon);
			        ic_3.setImageResource(R.mipmap.builder_selceted_icon);
					getUserType = "Builder";
		        return false;
		    }
		    });
		btn_signup = (Button) rView.findViewById(R.id.btn_signup);
		btn_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "Clicked on Register");
				getFName = et_fName.getText().toString();
				getEmail = et_emailaddr.getText().toString();
				getFName = Utilities.makeFirstLetterCaps(getFName);

				if(getFName.contains("  ")){
					getFName = getFName.trim();
//					getFirstName = getFirstName.replace("  ", "");
				}

			 if (getFName.length() == 0) {
				 Utilities.Snack(mCoordinator, "Full Name can't be empty");
			 } else	if (getEmail.length() == 0) {
				 Utilities.Snack(mCoordinator, "Email Id can't be empty");
				} else if (!Utilities.checkEmail(getEmail)) {
					 Utilities.Snack(mCoordinator, "Please enter correct email id");
				} else if (BTN_CITY.getText().toString().contains("Select City")) {
					 Utilities.Snack(mCoordinator, "Please select City");
				} else if (getUserType.length() < 1) {
                 Utilities.Snack(mCoordinator, "Please select User Type");
             } else {
//					if (Utilities.isInternetOn(ctx))
//						new RegisterTask().execute("");
				}
			}
		});
	
		BTN_CITY = (EditText) rView.findViewById(R.id.spin_city);
		BTN_CITY.setHint("Select City");
//		BTN_CITY.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				openCityDialog(BTN_CITY, v);
//			}
//		});
//		setFont();
//		if(api_city.size() == 0){
//			if(Utilities.D)Log.v(TAG,"api_allCities is empty!");
////			if(Utilities.isInternetOn(ctx)){
////				new allCitiesTask().execute("");
//			} else
//				getActivity().finish();
////		}
//    else {
//			if(Utilities.D)Log.v(TAG,"api_allCities NOT empty!");
//			try {
//				BTN_CITY.setVisibility(View.VISIBLE);
//				BTN_CITY.setText(getCityName);
//				if(getCityName.length() < 1){
//					BTN_CITY.setText("Select City");
//				}
//			} catch (Exception e) { e.printStackTrace(); }
//		}
		return rView;
	}
//	public void openCityDialog(View v, final View v2) {
//
//		final Dialog dialog;
//		dialog = new Dialog(ctx, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge );
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.list_search_dialog);
//
//		mSearchView = (EditText) dialog.findViewById(R.id.ListSearch);
//		mListView = (ListView) dialog.findViewById(R.id.listViewDialog);
//		mListView.setFastScrollEnabled(true);
//
//		 adapter_cities = new ArrayAdapter<String>(
//				getActivity(), android.R.layout.simple_dropdown_item_1line,
//				api_city);
//
//		mListView.setAdapter(adapter_cities);
//
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				int positionOrigional = api_city.indexOf((String) mListView.getItemAtPosition(position));
//
//				try {
//					getCityName = api_city.get(positionOrigional);
//					// mListView.getItemIdAtPosition(position);
//					if (api_city.size()==0) {
//						BTN_CITY.setHint("Select City");
//					}else {
//						Log.e(TAG, getCityName + "  " + positionOrigional);
//						BTN_CITY.setText(getCityName);
////					    BTN_CITY.setText(api_city.get(position));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					Utilities.Snack(mCoordinator, "Some error occurred");
//				}
//				dialog.dismiss();
//				Utilities.hideKeyboard(getActivity());
//			}
//		});
//
//		mListView.setTextFilterEnabled(true);
//
//		// mSearchView.setHint(hint);
//		mSearchView.addTextChangedListener(new TextWatcher() {
//
//			public void afterTextChanged(Editable s) {
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//				adapter_cities.getFilter().filter(s.toString());
//				adapter_cities.notifyDataSetChanged();
//			}
//
//		});
//		dialog.show();
//	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = getActivity();
		db = new TinyDB(ctx);
//		api_city = db.getListString("PREF_KEY_CITIES_ALL");
//
//		getGcmID = mPrefs.getString("PREF_KEY_GCM_ID", "");
//		getCityName = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		if(Utilities.D)Log.v(TAG,"gcmId="+getGcmID);
		if(Utilities.D)Log.v(TAG,"getCityName=" + getCityName);

		Bundle b = this.getArguments();
//		getFName = b.getString("KEY_getName");
	}//endOncrea
	
//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//		String item = parent.getItemAtPosition(position).toString();
//			if(Utilities.D)Log.v(TAG,"postition=" + position + "\nITEM=" + item);
//			getUserType = item;
//	}
//
//	public void onNothingSelected(AdapterView<?> arg0) {
//	}
	
//	class allCitiesTask extends AsyncTask<String, String, String> {
//		int api_code = 0;
//		String api_message = "", api_data;
//		Dialog dialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			BTN_CITY.setVisibility(View.GONE);
////			dialog = Utilities.showProgressDialog(getActivity(), allCitiesTask.this);
//			dialog.show();
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//		}
//
//		@Override
//		protected String doInBackground(String... f_url) {
//			try {
//				String CITIES_10 =  "{\"table\":\"cities\",\"by\":{\"is_active\":\"1\"},\"type\":\"ASC\",\"order_by\":\"location_city\",\"multiple\":\"All\"}";
//				String CITIES_API =  "{\"table\":\"cities\",\"by\":{\"is_active\":\"0\"},\"type\":\"ASC\",\"order_by\":\"location_city\",\"multiple\":\"All\"}";
//
//				String jsonString = Utilities.sendData(Host.TestCitySelect, CITIES_10);
//				db.putString("PREF_KEY_CITIES10", jsonString);
//
//				JSONObject reader = new JSONObject(jsonString);
//				JSONObject meta = (JSONObject) reader.get("meta");
//				api_code = meta.getInt("code");
//				api_message = meta.getString("message");
//				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);
//
//				if (api_code == 200) {
//					JSONArray data = (JSONArray) reader.get("data");
//					for (int i = 0; i < data.length(); i++) {
//						JSONObject obj = data.getJSONObject(i);
//						api_city.add(obj.getString("location_city"));
//					}
//			//-----------------------------------------------------------
//					jsonString = Utilities.sendData(Host.TestCitySelect, CITIES_API);
//					JSONObject readers = new JSONObject(jsonString);
//					JSONObject metas = (JSONObject) readers.get("meta");
//					if (metas.getInt("code") == 200) {
//						JSONArray datas = (JSONArray) readers.get("data");
//						for (int i = 0; i < datas.length(); i++) {
//							JSONObject obj = datas.getJSONObject(i);
//							api_city.add(obj.getString("location_city"));
//						}
//					}
//				}
//			} catch (Exception e) { e.printStackTrace(); }
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String file_url) {
//			dialog.dismiss();
//
//			if (api_code == 200) {
//				db.putListString("PREF_KEY_CITIES_ALL", api_city);
//				if (api_city.size() != 0) {
//					BTN_CITY.setVisibility(View.VISIBLE);
//				}
//			} else {
//				Toast.makeText(ctx, "No cities found, please try again !", Toast.LENGTH_SHORT).show();
//				getActivity().finish();
//			}
//		}
//	}
	String api_dataID = "";
//	class RegisterTask extends AsyncTask<String, String, String> {
//		int api_code = 0;
//		public String userId = "", userName = "", api_message = "", api_otp = "";
//		Dialog dialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			dialog = Utilities.showProgressDialog(ctx, RegisterTask.this);
//			dialog.show();
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//			Utilities.Snack(mCoordinator, "Request cancelled !");
//		}
//		@Override
//		protected String doInBackground(String... f_url) {
//
//				try {
//					if(getDone.contains("signup")){
//						String SOCIAL_REGISTER_USER =
//								"{\"fname\":\""
//								+ Utilities.makeFirstLetterCaps(getFName)
//
////								+ "\", \"lname\":\""
////								+ Utilities.makeFirstLetterCaps(getLName)
//
//								+ "\", \"gcm_id\":\""
//								+ getGcmID
//
//								+ "\", \"email\":\""
//								+ getEmail
//
//								+ "\", \"city\":\""
//								+ getCityName
//
//								+ "\", \"password\":\""
//								+ getPassword
//
//								+ "\", \"phone_no\":\""
//								+ getMobile
//
//								+ "\", \"user_type\":\""
//								+ getUserType.toLowerCase()
//
//								+ "\"}";
//
//						String UPDATE_API =
//								"{\"table\":\"registration\",\"by\":{\"email\":\""
//								+ getEmail +"\"},\"params\":" + SOCIAL_REGISTER_USER + "}";
//						String jsonString = Utilities.sendData(Host.UpdateUrl, UPDATE_API);
//
//						JSONObject reader = new JSONObject(jsonString);
//						JSONObject meta = (JSONObject) reader.get("meta");
//
//						api_code = meta.getInt("code");
//						api_message = meta.getString("message");
//
//					} else {
//						String REGISTER_USER =
//								"{\"fname\":\""
//								+ Utilities.makeFirstLetterCaps(getFName)
//
////								+ "\", \"lname\":\""
////								+ Utilities.makeFirstLetterCaps(getLName)
//
//								+ "\", \"gcm_id\":\""
//								+ getGcmID
//
//								+ "\", \"email\":\""
//								+ getEmail
//
//								+ "\", \"city\":\""
//								+ getCityName
//
//								+ "\", \"password\":\""
//								+ getPassword
//
//								+ "\", \"phone_no\":\""
//								+ getMobile
//
//								+ "\", \"user_type\":\""
//								+ getUserType.toLowerCase()
//
//								+ "\"}";
//
//						String str_send_json = "{\"table\":\"registration\", \"params\":" + REGISTER_USER + "}";
//						String jsonString = Utilities.sendData(Host.PostUrl, str_send_json);
//						JSONObject reader = new JSONObject(jsonString);
//						JSONObject meta = (JSONObject) reader.get("meta");
//
//						api_code = meta.getInt("code");
//						api_message = meta.getString("message");
//
//						if (api_code == 200) {
//							api_dataID = reader.getString("data");
//						}
//					}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String file_url) {
//
//			dialog.dismiss();
//
//			if (api_code == 200) {
//				Bundle args = new Bundle();
//				args.putString("KEY_getMobNumber", getMobile);
//				args.putString("KEY_getFName", getFName);
////				args.putString("KEY_getLName", getLName);
//				args.putString("KEY_getEmail", getEmail);
//				args.putString("KEY_getPassword", getPassword);
//				args.putString("KEY_getUserType", getUserType);
//				args.putString("KEY_dataID", api_dataID);
//				args.putString("KEY_getOTP", api_otp);
//				args.putString("KEY_toDo", "REGISTER");
//
//				Fragment f = new OTPFragmgent();
//				f.setArguments(args);
//
//				Utilities.replaceFragment(getActivity(), f, R.id.container, true, false);
//				Toast.makeText(getActivity(), "Please verify your number !", Toast.LENGTH_SHORT).show();
//				if(getDone.contains("signup")){
//					new VerifyMobTask().execute("");
//				}
//
//			} else if(api_code == 401){
//
//				getActivity().getSupportFragmentManager().popBackStack();
////				Utilities.replaceFragment(getActivity(), (new LoginFragment()), R.id.container, true, false);
//				Utilities.Snack(mCoordinator, "Email already registered, please login !");
//				Toast.makeText(ctx, "Email already registered, please login !", Toast.LENGTH_LONG).show();
//			} else {
//				Utilities.Snack(mCoordinator, "Registration failed !");
//			}
//		}
//	}
//	@Override
//	public void onClick(View v) {
//	}
//	class VerifyMobTask extends AsyncTask<String, String, String> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... f_url) {
//			try {
//				String VERIFY_API =
//						"{\"phone_no\":\""
//								+ getMobile
//								+ "\"}";
//				Utilities.sendData(Host.ForgotUrl, VERIFY_API);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String file_url) {
//			super.onPostExecute(file_url);
//			if(Utilities.D)Log.e(TAG, "CHECK HARI");
//		}
//	}
}