package com.propkaro.userinfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.timeline.MyTimelineSetter;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;
public class UserInfoFragment extends Fragment implements OnClickListener{
	private static final String TAG = UserInfoFragment.class.getSimpleName();
	public List<MyTimelineSetter> AvailList = new ArrayList<MyTimelineSetter>();
	public static CircularProgressView progressView;
	private static final String KEY_POSITION = "TabProductFragment:Position";
	TextView tv_nodata;
	FragmentActivity fActivity;
	Context ctx;
	RelativeLayout rel_nodata;
    private CoordinatorLayout mCoordinator;
	Button btn_call;
	static int POSITION;
	View rView;
	Dialog customdialog, shareDialog;

	private SharedPreferences mPrefs;
//	String getMobile_num = "", getEmailID = "", getUserType = "",  
//			UserDetailsScreenFActivity.KEY_GET_USER_ID = "", getFName = "", getLName = "", getProfileImage = "", getHalfImage = "";
	private List<String> listMsgConn = new ArrayList<String>();
	TextView tv_name, tv_userType, tv_company_name, tv_emailid, tv_phon_no, tv_location, textView_pc;
	LinearLayout lnr_company_name, lnr_location;
	ImageView iv_profile;
	EditText et_textSomething;
	Button btn_connect,btn_SendMesssage;
	LinearLayout lnr_btn;

	public static UserInfoFragment newInstance(int pos) {

		UserInfoFragment fragment = new UserInfoFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		UserInfoFragment.POSITION = pos;
		Log.e(TAG, "Position=" + pos);

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.android_timeline, container, false);
		
		progressView = (CircularProgressView) rView.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);
		
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		rel_nodata = (RelativeLayout) rView.findViewById(R.id.rel_nodata);
		lnr_btn = (LinearLayout) rView.findViewById(R.id.lnr_btn);
		lnr_btn.setVisibility(View.GONE);
		rel_nodata.setVisibility(View.GONE);
		et_textSomething = (EditText) rView.findViewById(R.id.et_textSomething);
//		btn_postSomething = (Button)rView.findViewById(R.id.btn_postSomething);
//		btn_postSomething.setOnClickListener(this);
		
		iv_profile = (ImageView)rView.findViewById(R.id.iv_profile);
		iv_profile.setImageResource(R.mipmap.user_type);
		
		textView_pc = (TextView) rView.findViewById(R.id.textView_pc);
    	textView_pc.setText("");
    	textView_pc.setVisibility(View.GONE);
    	
    	btn_call = (Button)rView.findViewById(R.id.btn_call);
    	btn_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (api_phone_no.isEmpty() || api_phone_no.length() < 10) {
					Utilities.Snack(mCoordinator, "Number not found !");
				} else {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + api_phone_no));
					getActivity().startActivity(intent);
				}
			}
		});
		lnr_company_name = (LinearLayout)rView.findViewById(R.id.lnr_company_name);
		LinearLayout lnr_emailid = (LinearLayout)rView.findViewById(R.id.lnr_emailid);
		lnr_emailid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				String[] TO = {""};
//			      String[] CC = {""};
//			      Intent emailIntent = new Intent(Intent.ACTION_SEND);
//			      emailIntent.setData(Uri.parse("mailto:"));
//			      emailIntent.setType("text/plain");
//			      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//			      emailIntent.putExtra(Intent.EXTRA_CC, CC);
//			      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//			      emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//		         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		         
				String recipient=tv_emailid.getText().toString();
				String body = "";
				String subject="";
				Intent emailIntent= new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", recipient, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
				emailIntent.putExtra(Intent.EXTRA_TEXT, body);
				try{
					if(Utilities.checkEmail(recipient))
						startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));				    
					else
				    	Utilities.Snack(mCoordinator, "Email is not valid.");
				} catch (android.content.ActivityNotFoundException ex) {
			    }
			}
		});
		LinearLayout lnr_phon_no = (LinearLayout)rView.findViewById(R.id.lnr_phon_no);
		lnr_phon_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (api_phone_no.isEmpty() || api_phone_no.length() < 10) {
					Utilities.Snack(mCoordinator, "Number not found !");
				} else {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + api_phone_no));
					getActivity().startActivity(intent);
				}
			}
		});

		lnr_location = (LinearLayout)rView.findViewById(R.id.lnr_location);
		tv_location = (TextView)rView.findViewById(R.id.tv_location);
		tv_location.setText("");
		tv_name = (TextView)rView.findViewById(R.id.tv_name);
		tv_name.setText("");
		tv_userType = (TextView)rView.findViewById(R.id.tv_userType);
		tv_userType.setText("");
		tv_company_name = (TextView)rView.findViewById(R.id.tv_company_name);
		tv_company_name.setText("");
		tv_emailid = (TextView)rView.findViewById(R.id.tv_emailid);
		tv_emailid.setText("");
		tv_phon_no = (TextView)rView.findViewById(R.id.tv_phon_no);
		tv_phon_no.setText("");
		btn_connect = (Button)rView.findViewById(R.id.btn_connect);
		btn_connect.setVisibility(View.GONE);
		btn_connect.setText("");
		btn_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(ctx)){
					new ConnectFriendTask().execute("");
				}
			}
		});
		btn_SendMesssage= (Button)rView.findViewById(R.id.btn_SendMesssage);
		btn_SendMesssage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				onClickWhatsApp(v);
				openDialogSendMessage(btn_SendMesssage, v);			
			}
		});
		if(Utilities.isInternetOn(ctx))
			new UserDetailsTask().execute(UserInfoFActivity.KEY_GET_USER_ID);

		return rView;
	}

	 void openDialogSendMessage(View v, final View v2){

	 		shareDialog = new Dialog(ctx);
//	 		shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 		shareDialog.setTitle("Send Message");
	 		shareDialog.setContentView(R.layout.adapter_timeline_post_only);
	 		shareDialog.setCancelable(true);
	 		shareDialog.setOnKeyListener(new Dialog.OnKeyListener() {

	 			@Override
	 			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
	 				if (keyCode == KeyEvent.KEYCODE_BACK) {
	 					Log.v("TAG", "Dialog.KEYCODE_BACK.....########..........");
	 					shareDialog.cancel();
	 					Utilities.hideKeyboard(fActivity);
	 				}
	 				return true;
	 			}
	 		});
	 		
	 		et_textSomething = (EditText)shareDialog.findViewById(R.id.et_textSomething);	
	 		Button btn_shareFinal = (Button)shareDialog.findViewById(R.id.btn_shareFinal);
	 		Button btn_shareCancel = (Button)shareDialog.findViewById(R.id.btn_shareCancel);
	 		btn_shareFinal.setText("SEND");
	 		
	 		btn_shareFinal.setOnClickListener(new OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				
					Utilities.hideKeyboard(fActivity);
						
	 				if(et_textSomething.getText().toString().length() == 0){
	 					
	     				Toast.makeText(ctx, "Please write something..", Toast.LENGTH_SHORT).show();
	 				}else {
     					if(Utilities.D)Log.v(TAG,"R.id.btn_share");
     					if(Utilities.isInternetOn(ctx))
     						new SendMessageTask().execute("");
     						shareDialog.cancel();
     						Toast.makeText(ctx, "Sending message...", Toast.LENGTH_SHORT).show();
	 					}
	 				}
	 			});
	 		btn_shareCancel.setOnClickListener(new OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				Utilities.hideKeyboard(fActivity);
	 				shareDialog.cancel();
	 			}
	 		});

	 		shareDialog.show();
			
		    }//endCcustomdialog

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fActivity = getActivity();
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
//		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
//		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
//		UserDetailsScreenFActivity.KEY_GET_USER_ID = mPrefs.getString("PREF_KEY_USER_DataID", "");
//		
//		UserDetailsScreenFActivity.KEY_GET_USER_ID = mPrefs.getString("PREF_KEY_USER_NAME", "");
//		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
//		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
//		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
//		getHalfImage = mPrefs.getString("PREF_KEY_HALF_IMAGE", "");
//		if(Utilities.D)Log.v(TAG,"getMobile_num="+getMobile_num);
//		if(Utilities.D)Log.v(TAG,"getEmailID="+getEmailID);
//		if(Utilities.D)Log.v(TAG,"getUserType="+getUserType);
//		if(Utilities.D)Log.v(TAG,"getUserID="+UserDetailsScreenFActivity.KEY_GET_USER_ID);
//		if(Utilities.D)Log.v(TAG,"UserDetailsScreenFActivity.KEY_GET_USER_ID="+UserDetailsScreenFActivity.KEY_GET_USER_ID);
//		if(Utilities.D)Log.v(TAG,"getProfileImage="+getProfileImage);
	};
	
	class SendMessageTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;
		String timeString = "", statusMsg = "", tempTextSomething = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressView.setVisibility(View.VISIBLE);
//			btn_SendMesssage.setText("Sending..");
			btn_SendMesssage.setEnabled(false);
			tempTextSomething = et_textSomething.getText().toString();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				statusMsg = f_url[0];
				Date date = new Date();
				timeString = new Timestamp(date.getTime()).toString();
				
					String POST_SOMETHING_API = 
							"{\"table\":\"messages\",\"params\":{\"message_to\":\""
					+ UserInfoFActivity.KEY_GET_USER_ID + "\",\"message_from\":\""
					+ UserInfoFActivity.getUserDataID + "\",\"message\":\""
					+ tempTextSomething + "\",\"timestamp\":\""
					+ timeString + "\"}}";
					
//							"{\"table\":\"activities\",\"params\":{\"activity_type_id\":\"2\",\"event_id\":0,\"posted_by\":\"" 
//									+ getUserDataID + "\",\"activity_datetime\":\"" 
//									+ timeString + "\",\"content\":\"Status Updated\",\"status\":\"" 
//									+ statusMsg + "\"}}";
					
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, POST_SOMETHING_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					String api_data = reader.getString("data");
					
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
			
			btn_SendMesssage.setEnabled(true);
			if(api_code == 200){
				et_textSomething.setText("");
//				progressView.setVisibility(View.GONE);
				
				Toast.makeText(ctx, api_message, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(ctx, api_message, Toast.LENGTH_LONG).show();
			}
		}
	}

	class MsgConnTask extends AsyncTask<String, String, String> {
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

				String UrlBase = Host.getUserDetailsUrl + "";
				String jsonString = Utilities.sendData(UrlBase, MESSAGE_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					listMsgConn.clear();
					
					JSONObject data = (JSONObject) reader.get("data");
					
					listMsgConn.add(data.getString("total_connected") + "");
					listMsgConn.add(data.getString("total_messages") + "");
					listMsgConn.add(data.getString("total_properties") + "");
					
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
			
			if(api_code == 200){
//				tv_connection198.setText(listMsgConn.get(0));
//				tv_msg136.setText(listMsgConn.get(1));
//				tv_notification.setText(listMsgConn.get(2));
			}
		}
	}
	String api_phone_no = "";
	class UserDetailsTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data = "", api_id = "", api_email = "", 
				api_user_type = "", api_userName = "", api_fName = "", api_lName = "", api_about_u = "", 
				api_image = "", api_half_image = "", api_city = "", api_country_code = "", api_company_name = "", 
				api_company_address = "", api_use_company_name = "", api_show_phone = "", 
				api_activate = "", api_facebook_id = "", api_google_id = "", api_registered_on = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressView.setVisibility(View.VISIBLE);
			rel_nodata.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String GET_USER_DETAILS_API = 
						"{\"table\":\"registration\",\"by\":{\"id\":\""
				+ f_url[0] + "\"},\"multiple\":\"1\"}";

//				String GET_USER_DETAILS_API = 
//				"{" 
//				+ "\"id\":"
//				+ "\"" + f_url[0] + "\""
//				+ "}";

				String UrlBase = Host.GetUrl + "";
				String jsonString = Utilities.sendData(UrlBase, GET_USER_DETAILS_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONObject data = (JSONObject) reader.get("data");
					api_id = data.getString("id");
					api_phone_no = data.getString("phone_no");
					api_email = data.getString("email");
					api_user_type = data.getString("user_type");
					
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
			
			progressView.setVisibility(View.GONE);
			if(api_code == 200){
				lnr_btn.setVisibility(View.VISIBLE);
				btn_SendMesssage.setVisibility(View.VISIBLE);
				rel_nodata.setVisibility(View.VISIBLE);
				String getFullName = api_fName + " " + api_lName;
				tv_name.setText(getFullName);
				btn_call.setText("Call " + getFullName);
				tv_userType.setText("(" + Utilities.makeFirstLetterCaps(api_user_type) + ")");
				
				if(api_company_name.length() < 1 || api_company_name.isEmpty())
					lnr_company_name.setVisibility(View.GONE);
				
				if(api_city.length() < 1 || api_city.isEmpty())
					lnr_location.setVisibility(View.GONE);

				tv_company_name.setText(api_company_name);
				tv_location.setText(api_city);
				tv_emailid.setText(api_email);
				tv_phon_no.setText(api_phone_no);
				if(api_image.contains("http")){
					ImageLoader.getInstance().displayImage(api_image, iv_profile, AvailPCFragment.animateFirstListener);
				} else{
					ImageLoader.getInstance().displayImage(Host.MainUrl + api_image, iv_profile, AvailPCFragment.animateFirstListener);
				}
				
		    	if(api_image.contains("default")){
		    		if(getFullName.length() > 0){
		        		textView_pc.setVisibility(View.VISIBLE);
		    			textView_pc.getBackground().setColorFilter(Utilities.getRandomColor(ctx,api_fName), PorterDuff.Mode.SRC_IN);
		    			textView_pc.setText(String.valueOf(getFullName.charAt(0)).toUpperCase());
		    		} else {
		        		textView_pc.setVisibility(View.GONE);
		    		}
		    	}
		    	
//				new CheckFriendTask().execute("");
				
				if(UserInfoFActivity.KEY_GET_USER_ID.equals(UserInfoFActivity.getUserDataID)){
					btn_SendMesssage.setVisibility(View.GONE);
				}
			}
//			if(Utilities.isInternetOn(ctx))
//				new MsgConnTask().execute(UserDetailsScreenFActivity.KEY_GET_USER_ID);
		}
	}
	class ConnectFriendTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data,api_count="";
//		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			btn_connect.setText("Requesting..");
			btn_SendMesssage.setText("Send Message");
			btn_connect.setEnabled(false);
//			dialog = Utilities.showProgressDialog(activity);
//			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String CONNECT_FRIEND_API = 
						"{\"table\":\"connections\",\"params\":{\"connect_to\":\""
				+ UserInfoFActivity.KEY_GET_USER_ID + "\",\"connect_from\":\""
				+ UserInfoFActivity.getUserDataID + "\"}}";

				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, CONNECT_FRIEND_API);
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
		} catch (Exception e) {
			e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
//			dialog.dismiss();

			if (api_code == 200) {
				btn_connect.setText("Request Sent");
				btn_connect.setEnabled(false);
//				SubCategorySetter.remove(position);
//				notifyDataSetChanged();
			} else {
				btn_connect.setText("Request failed, try again?");
				btn_connect.setEnabled(true);
			}
		}
	}

	class CheckFriendTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data, api_count = "";
		String api_connection_id = "", api_is_approved = "";
//		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			btn_connect.setText("Checking..");
			btn_connect.setEnabled(false);
//			dialog = Utilities.showProgressDialog(getActivity());
//			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String CONNECT_FRIEND_API = 
						"{\"table\":\"connections\",\"by\":{\"connect_to\":\"" 
				+ UserInfoFActivity.KEY_GET_USER_ID +"\",\"connect_from\":\"" 
				+ UserInfoFActivity.getUserDataID + "\"},\"multiple\":\"1\"}";

//				String CONNECT_FRIEND_API = 
//						"{\"table\":\"connections\",\"params\":{\"connect_to\":\""
//				+ UserDetailsScreenFActivity.KEY_GET_USER_ID + "\",\"connect_from\":\""
//				+ UserDetailsScreenFActivity.getUserDataID + "\"}}";

				String UrlBase = Host.GetUrl + "";
				String jsonString = Utilities.sendData(UrlBase, CONNECT_FRIEND_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONObject data = (JSONObject) reader.get("data");
					api_connection_id = data.getString("connection_id");
					api_is_approved = data.getString("is_approved");
					if(Utilities.D)Log.v(TAG,"data="+data);
				} else {
			}
		} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
//			dialog.dismiss();

			btn_SendMesssage.setVisibility(View.VISIBLE);
			if (api_code == 200) {
				if(api_is_approved.equals("1")){
					btn_connect.setText("Connected");
					btn_connect.setEnabled(false);
					btn_connect.setVisibility(View.VISIBLE);

//					SubCategorySetter.remove(position);
//					notifyDataSetChanged();
				} else if(api_is_approved.equals("0")){
					btn_connect.setText("Request sent");
					btn_connect.setEnabled(false);
					btn_connect.setVisibility(View.VISIBLE);
				}
			} else if (api_code == 400){//FIXME : TO BE ASK FROM SACHIN
				if(Utilities.D)Log.v(TAG,"No activities found !");
				btn_connect.setText("Connect");
				btn_connect.setEnabled(true);
				btn_connect.setVisibility(View.GONE);
				
				new ReverseCheckFriendTask().execute("");
			} else {
				btn_connect.setVisibility(View.GONE);
				if(Utilities.D)Log.v(TAG,"Something went wrong !");
			}
			
			if(UserInfoFActivity.KEY_GET_USER_ID.equals(UserInfoFActivity.getUserDataID)){
				btn_connect.setVisibility(View.GONE);
				btn_SendMesssage.setVisibility(View.GONE);
			}
		}
	}

	class ReverseCheckFriendTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data, api_count = "";
		String api_connection_id = "", api_is_approved = "";
//		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			btn_connect.setText("Checking..");
			btn_connect.setEnabled(false);
//			dialog = Utilities.showProgressDialog(getActivity());
//			dialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String CONNECT_FRIEND_API = 
						"{\"table\":\"connections\",\"by\":{\"connect_to\":\"" 
				+ UserInfoFActivity.getUserDataID +"\",\"connect_from\":\"" 
				+ UserInfoFActivity.KEY_GET_USER_ID + "\"},\"multiple\":\"1\"}";

//				String CONNECT_FRIEND_API = 
//						"{\"table\":\"connections\",\"params\":{\"connect_to\":\""
//				+ UserDetailsScreenFActivity.KEY_GET_USER_ID + "\",\"connect_from\":\""
//				+ UserDetailsScreenFActivity.getUserDataID + "\"}}";

				String UrlBase = Host.GetUrl + "";
				String jsonString = Utilities.sendData(UrlBase, CONNECT_FRIEND_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					JSONObject data = (JSONObject) reader.get("data");
					api_connection_id = data.getString("connection_id");
					api_is_approved = data.getString("is_approved");
					if(Utilities.D)Log.v(TAG,"data="+data);
				} else {
			}
		} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
//			dialog.dismiss();

			btn_SendMesssage.setVisibility(View.VISIBLE);

			if (api_code == 200) {
				if(api_is_approved.equals("1")){
					btn_connect.setText("Connected");
					btn_connect.setEnabled(false);
					btn_connect.setVisibility(View.VISIBLE);

//					SubCategorySetter.remove(position);
//					notifyDataSetChanged();
				} else if(api_is_approved.equals("0")){
					btn_connect.setText("Request sent");
					btn_connect.setEnabled(false);
					btn_connect.setVisibility(View.VISIBLE);
				}
			} else if (api_code == 400){//FIXME : TO BE ASK FROM SACHIN
				if(Utilities.D)Log.v(TAG,"No activities found !");
				btn_connect.setText("Connect");
				btn_connect.setEnabled(true);
				btn_connect.setVisibility(View.VISIBLE);
				
			} else {
				btn_connect.setVisibility(View.GONE);
				if(Utilities.D)Log.v(TAG,"Something went wrong !");
			}
			
			if(UserInfoFActivity.KEY_GET_USER_ID.equals(UserInfoFActivity.getUserDataID)){
				btn_connect.setVisibility(View.GONE);
				btn_SendMesssage.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
	}
}