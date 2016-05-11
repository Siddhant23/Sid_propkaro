package com.propkaro;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.propkaro.interfaces.InterfaceFacebookLogin;
import com.propkaro.interfaces.InterfaceGoogleLogin;
import com.propkaro.util.Host;
import com.propkaro.util.PermissionsUtilities;
import com.propkaro.util.Utilities;
public class SplashFragment extends Fragment implements OnClickListener {
	private final String TAG = SplashFragment.class.getSimpleName();
	private CoordinatorLayout mCoordinator;
	private View view;
	private Context ctx;
	private Timer timer;
	private TextView tv_register_now,tv_frgt_pwd;
	private ImageView imgView;
	private ImageView btn_facebook, btn_google;
	private Button btn_login;
	SharedPreferences mPrefs; 
	private String getUserDataID = "", getCityName = "", getStreetAddress = "", getCountryCode = "", getCountryName = "";
	Double getLat = 0.0, getLng = 0.0;
	EditText et_emailaddr, et_passwd;
	String getPassword = "", getEmail = "", gcmId = "";
	List<Double> locationList = new ArrayList<Double>();

	public SplashFragment() {
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume()");

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());
		if(status == ConnectionResult.SUCCESS){
		Log.v(TAG, "Google Play Services are available");
			
		 if(gcmId.equals(null) || gcmId.isEmpty() || gcmId.length() == 0){
			 GCMRegistrar.checkDevice(getActivity());
			 GCMRegistrar.checkManifest(getActivity());
			 GCMRegistrar.register(getActivity(), Config.GOOGLE_SENDER_ID);
			 gcmId = GCMRegistrar.getRegistrationId(getActivity());
			 if(gcmId.length() == 0){
				 gcmId = GCMRegistrar.getRegistrationId(getActivity());
			 }
			 SharedPreferences.Editor editor = mPrefs.edit();
			 editor.putString("PREF_KEY_GCM_ID", gcmId);
			 editor.commit();
			 Log.v(TAG, "generated GCM_ID==" + gcmId);
		 }
		 
		 if(getCityName.equals("")){
//				try {
//					List<android.location.Address> addsList;
//					addsList = Utilities.locationFinder(ctx);
//					if(addsList != null){
//						if(addsList.size() > 0){
//							getLat = addsList.get(0).getLatitude();
//							getLng = addsList.get(0).getLongitude();
//							getCityName = addsList.get(0).getLocality();
//							getCountryCode = addsList.get(0).getCountryCode();
//							getCountryName = addsList.get(0).getCountryCode();
//							getStreetAddress = addsList.get(0).getAddressLine(0);
//							
//							SharedPreferences.Editor editor = mPrefs.edit();
//							editor.putString("PREF_KEY_LATITUDE", getLat + "");
//							editor.putString("PREF_KEY_LONGITUDE", getLng + "");
//							editor.putString("PREF_KEY_CITY_NAME", getCityName);
//							editor.putString("PREF_KEY_COUNTRY_CODE", getCountryCode);
//							editor.commit();
//						}
//					}
					
//					getPermissionToReadLocation();
//					if(locationList.size() > 0){
//						
//						getLat = locationList.get(0);
//						getLng = locationList.get(1);
//						SharedPreferences.Editor editor = mPrefs.edit();
//						editor.putString("PREF_KEY_LATITUDE", getLat + "");
//						editor.putString("PREF_KEY_LONGITUDE", getLng + "");
//						editor.commit();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
		 }
		//-----------------------getAllContacts--------------------------------		
		} else{	
			Log.v(TAG, "Google Play Services are not available !");
		}
	}//endOnresum
	
//	public void getPermissionToReadLocation() {
////		if (Build.VERSION.SDK_INT < 23) {
////        	if(Utilities.D)Log.e(TAG, "checking....VERSION.SDK_INT < 23");
////			locationList = Utilities.updateWithNewLocation(ctx);
//////		    return;
////		}
//    	if(Utilities.D)Log.e(TAG, "Go....Further***********");
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        	if(Utilities.D)Log.e(TAG, "checking....permissions should grant or not***********");
//            if (getActivity().shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            	if(Utilities.D)Log.e(TAG, "checking....permissions     denied-----##################----");
//				locationList = Utilities.updateWithNewLocation(ctx);
//            }
//            getActivity().requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, Host.READ_LOCATION_PERMISSIONS_REQUEST);
//        } else {
//        	if(Utilities.D)Log.e(TAG, "Not cchecking....ContextCompat.checkSelfPermission***********");
//			locationList = Utilities.updateWithNewLocation(ctx);
//        }
//    }
	
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    	if(Utilities.D)Log.e(TAG, "checking....per");
    	
        if (requestCode == Host.ACCOUNTS_PERMISSIONS_REQUEST) {
			InterfaceGoogleLogin GPlusLogin = (InterfaceGoogleLogin) getActivity();
			GPlusLogin.LoginToGoogleFragment();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.frag_new__splash, container, false);
		mCoordinator = (CoordinatorLayout) view.findViewById(R.id.root_coordinator);
		Typeface RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

		et_emailaddr = (EditText) view.findViewById(R.id.et_emailaddr);
		et_emailaddr.setTypeface(RobotoLight);
		et_passwd = (EditText) view.findViewById(R.id.et_passwd);
		et_passwd.setTypeface(RobotoLight);

		tv_register_now = (TextView) view.findViewById(R.id.tv_register_now);
		tv_register_now.setTypeface(RobotoLight);

		
		tv_register_now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putString("KEY_getName", "");
				args.putString("KEY_getFame", "");
				args.putString("KEY_getLame", "");
				args.putString("KEY_getEmail", "");
				args.putString("KEY_dataID", "");
				args.putString("KEY_done", "");
				
				Fragment f = new RegisterFragment();
				f.setArguments(args);

				Utilities.addFragment(getActivity(), f, R.id.container, true, true);
			}
		});
//		
		tv_frgt_pwd = (TextView) view.findViewById(R.id.tv_frgt_pwd);
		tv_frgt_pwd.setTypeface(RobotoLight);
		
		tv_frgt_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Utilities.addFragment(getActivity(), new ForgotPasswdFragment(), R.id.container, true, true);
			}
		});
		
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.d(TAG, "Clicked on login");
				Utilities.hideKeyboard(getActivity());
				getEmail = et_emailaddr.getText().toString().trim();
				getPassword = et_passwd.getText().toString().trim();

				if (getEmail.length() == 0) {
					Utilities.Snack(mCoordinator, "Enter correct email address ");
				} else if (!Utilities.checkEmail(getEmail)) {
					Utilities.Snack(mCoordinator, "Enter correct email address");
				} else if (getPassword.length() == 0) {
					Utilities.Snack(mCoordinator, "Password can't be blank");
				} else {
					String url = "http://propkaro.com/api/loginUserAPI.php?"
							+ "email=" + getEmail + "&" + "password="
							+ getPassword;

					if (Utilities.NetworkCheck(ctx))
						new LoginTask().execute(url);
					else
						Utilities.Snack(mCoordinator, "Oops! Internet Required");
				}
			}
		});

		btn_facebook = (ImageView) view.findViewById(R.id.btn_facebook);
		btn_facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InterfaceFacebookLogin FbLogin = (InterfaceFacebookLogin) getActivity();
				FbLogin.loginToFacebookFragment();
			}
		});
		
		btn_google = (ImageView) view.findViewById(R.id.btn_google_plus);
		btn_google.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_google.setSelected(true);
				if(PermissionsUtilities.getPermissionToReadAccounts(getActivity())){
					try {
						InterfaceGoogleLogin GPlusLogin = (InterfaceGoogleLogin) getActivity();
						GPlusLogin.LoginToGoogleFragment();
					} catch (Exception e) {e.printStackTrace();}
				}
			}
		});
		
		final LinearLayout LoginBox = (LinearLayout) view.findViewById(R.id.rl_loginbox);
		final LinearLayout LoginBox2 = (LinearLayout) view.findViewById(R.id.rl_3);
		LoginBox.setVisibility(View.INVISIBLE);
		LoginBox2.setVisibility(View.INVISIBLE);
		imgView = (ImageView) view.findViewById(R.id.animationImage);
		animate();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						
						imgView.setBackgroundResource(R.mipmap.pkframe72);
						LoginBox.setVisibility(View.VISIBLE);
						LoginBox2.setVisibility(View.VISIBLE);
						Animation animFade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
						LoginBox.startAnimation(animFade);
						Log.v(TAG, "Animation Finished4");
					}
				});
			}
		};
		timer = new Timer();
		timer.schedule(task, 2600);
		
		return view;
	}//endOncreaView
	
	@Override
	public void onStop() {
		super.onStop();
		
		timer.cancel();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		gcmId = mPrefs.getString("PREF_KEY_GCM_ID", "");
		getCityName = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getUserDataID = "";
		Log.v(TAG, "gcmId=" + gcmId);
		Log.v(TAG, "getCityName=" + getCityName);
		Log.v(TAG, "getUserDataID=" + getUserDataID);
	}

	@Override
	public void onClick(View v) {
		// cf = (CallFunction) getActivity();
		// cf.callFlip();
	}
	
	private void animate() {
		imgView.setVisibility(View.VISIBLE);
		imgView.setBackgroundResource(R.drawable.frame_animation);

		AnimationDrawable frameAnimation = (AnimationDrawable) imgView.getBackground();
		if (frameAnimation.isRunning()) {
			frameAnimation.stop();
		} else {
			frameAnimation.stop();
			frameAnimation.start();
		}
	}
	
	class LoginTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_id = "", 
				api_user_type = "", api_userName = "", api_fName = "", api_lName = "", api_about_u = "", api_phone_no = "",
				api_image = "", api_half_image = "", api_city = "", api_country_code = "", api_company_name = "", 
				api_company_address = "", api_use_company_name = "", api_show_phone = "",
				api_activate = "", api_facebook_id = "", api_google_id = "", api_registered_on = "";
		Dialog dialog;
		String temp_email = "", temp_password = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = Utilities.showProgressDialog(ctx, LoginTask.this);
			dialog.show();
			temp_email = et_emailaddr.getText().toString();
			temp_password = et_passwd.getText().toString();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Utilities.Snack(mCoordinator, "Request cancelled !");
		}
		
		@Override
		protected String doInBackground(String... f_url) {
			try {
//					String str_send_json = "{\"email\":\"abc@xyz.com\", \"password\":\"hhhhhh\", \"submit\":\"login\"}";
					String LOGIN_API =
							"{" +
							"\"email\":\""
							+ temp_email
							+ "\", \"password\":\""
							+ temp_password
							+ "\", \"submit\":\"login\"" 
							+ "}";
				String UrlBase = Host.LoginUrl + "";
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
//					Log.v(TAG, "api_id=" + api_id);
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
				Utilities.Snack(mCoordinator, "Incorrect Login Details");
			} else if (api_code == 501) { //501 (Not Active)
				Utilities.Snack(mCoordinator, "Reset Password to activate your account" );
			} else {
				Utilities.Snack(mCoordinator, "Contact: info@propkaro.com");
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
				Log.v(TAG, "api_message-=" + api_message);

				if (api_code == 200) {
					String data = reader.getString("data");
					Log.v(TAG, "data="+data);
				} else {
			}
		} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
//			dialog.dismiss();

			if (api_code == 200) {
				Log.v(TAG, "GCM id updated successfully!");
			} else {
			}
		}
	}
}