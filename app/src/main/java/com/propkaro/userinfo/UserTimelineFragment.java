package com.propkaro.userinfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.timeline.MyTimelineAdapter;
import com.propkaro.timeline.MyTimelineSetter;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;
public class UserTimelineFragment extends Fragment implements SearchView.OnQueryTextListener, OnClickListener{
	private static final String TAG = UserTimelineFragment.class.getSimpleName();
	public List<MyTimelineSetter> AvailList = new ArrayList<MyTimelineSetter>();
	private ListView mListView;
	private MyTimelineAdapter adapter;
	public static CircularProgressView progressView;
	private static final String KEY_POSITION = "TabProductFragment:Position";
	TextView tv_nodata;
	FragmentActivity fActivity;
	Context ctx;
	Button Btngetdata;
	static int POSITION;
	private SearchView mSearchView;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	View rView;
	Dialog customdialog, shareDialog;

	EditText et_textSomething;
	private SharedPreferences mPrefs;
//	String getMobile_num = "", getEmailID = "", getUserType = "",  
//			UserDetailsScreenFActivity.KEY_GET_USER_ID = "", getFName = "", getLName = "", getProfileImage = "", getHalfImage = "";
	String TIMELINE_API = "";
	private ImageView iv_connection, iv_msg, iv_location;
	private TextView tv_connection198, tv_msg136, tv_location, tv_notification;
	private List<String> listMsgConn = new ArrayList<String>();
	TextView tv_name, tv_userType, tv_company_name, tv_emailid, tv_phon_no, textView_pc;
	ImageView iv_profile;
	Button btn_connect,btn_SendMesssage;

	public static UserTimelineFragment newInstance(int pos) {

		UserTimelineFragment fragment = new UserTimelineFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		UserTimelineFragment.POSITION = pos;
		Log.e(TAG, "Position=" + pos);

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.android_timeline, container, false);
		
		mSearchView = (SearchView)rView.findViewById(R.id.search_view);
		mSearchView.setVisibility(View.GONE);
		
		SearchManager searchManager = (SearchManager) fActivity.getSystemService(Context.SEARCH_SERVICE);
	    mSearchView.setSearchableInfo(searchManager.getSearchableInfo(fActivity.getComponentName()));
		
		progressView = (CircularProgressView) rView.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);

		mListView = (ListView) rView.findViewById(R.id.list_avail_pc);
		mListView.setVisibility(View.GONE);

		ViewGroup headerView = (ViewGroup)inflater.inflate(R.layout.adapter_timeline_post1, mListView, false);
		mListView.addHeaderView(headerView, null, false);

		iv_connection = (ImageView) headerView.findViewById(R.id.iv_connection);
		tv_connection198 = (TextView) headerView.findViewById(R.id.tv_connection198);
		tv_connection198.setText("--");
		iv_msg = (ImageView) headerView.findViewById(R.id.iv_msg);
		tv_msg136 = (TextView) headerView.findViewById(R.id.tv_msg136);
		tv_msg136.setText("--");
		iv_location = (ImageView) headerView.findViewById(R.id.iv_location);
		tv_location = (TextView) headerView.findViewById(R.id.tv_location);
		tv_notification = (TextView) headerView.findViewById(R.id.tv_notification);
		
		iv_connection.setOnClickListener(this);
		tv_connection198.setOnClickListener(this);
		iv_msg.setOnClickListener(this);
		tv_msg136.setOnClickListener(this);
		
		et_textSomething = (EditText) headerView.findViewById(R.id.et_textSomething);
//		btn_postSomething = (Button)headerView.findViewById(R.id.btn_postSomething);
//		btn_postSomething.setOnClickListener(this);
		
		adapter = new MyTimelineAdapter(getActivity(), AvailList);
		mListView.setAdapter(adapter);
		
		mListView.setTextFilterEnabled(false);
//		mListView.setOnItemClickListener(new OnItemClickListenerListViewItem());
		setupSearchView(); 		
		
		iv_profile = (ImageView)headerView.findViewById(R.id.iv_profile);
		iv_profile.setImageResource(R.mipmap.user_type);
		
		textView_pc = (TextView) headerView.findViewById(R.id.textView_pc);
    	textView_pc.setText("");
    	textView_pc.setVisibility(View.GONE);
    	
		tv_name = (TextView)headerView.findViewById(R.id.tv_name);
		tv_name.setText("");
		tv_userType = (TextView)headerView.findViewById(R.id.tv_userType);
		tv_userType.setText("");
		tv_company_name = (TextView)headerView.findViewById(R.id.tv_company_name);
		tv_company_name.setText("");
		tv_emailid = (TextView)headerView.findViewById(R.id.tv_emailid);
		tv_emailid.setText("");
		tv_phon_no = (TextView)headerView.findViewById(R.id.tv_phon_no);
		tv_phon_no.setText("");
		btn_connect = (Button)headerView.findViewById(R.id.btn_connect);
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
		btn_SendMesssage= (Button)headerView.findViewById(R.id.btn_SendMesssage);
		btn_SendMesssage.setVisibility(View.GONE);
		btn_SendMesssage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				onClickWhatsApp(v);
				openDialogSendMessage(btn_SendMesssage, v);			
			}
		});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				// if(Utilities.D)Log.v(TAG,"onScrollStateChanged"
				// +"\nscrollState="+scrollState
				// +"\nlistView.getCount()="+listView.getCount());
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItem) {
//				 if(Utilities.D)Log.v(TAG,"onScroll*******" + "\nfirstVisibleItem="
//				 + firstVisibleItem + "\nvisibleItemCount="
//				 + visibleItemCount + "\ntotalItemCount="
//				 + totalItem + "\n************");
				lastInScreen = firstVisibleItem + visibleItemCount;
				totalItemCount = totalItem;
				
				if(Utilities.D)Log.v(TAG,"checking......1");
//				if(Utilities.D)Log.v(TAG,"mPrevTotalItemCount=" + mPrevTotalItemCount);
//				if(Utilities.D)Log.v(TAG,"lastInScreen=" + lastInScreen);
				if (view.getAdapter() != null
						&& (lastInScreen >= totalItemCount)
						&& totalItemCount != mPrevTotalItemCount) {
					
					if(Utilities.D)Log.v(TAG,"checking......2");
//					Log.e(TAG, "current_page =" + current_page + " total_pages=" + total_pages);
					
					if (isLoadingMore == true && current_page <= total_pages) {
						Log.e(TAG, "LoadingMore... Please wait..");
						// mListView.addFooterView(footerView);
						if(Utilities.isInternetOn(ctx))
							new TimelineTask().execute(TIMELINE_API);
					}
				}
			}
		});
		
	    tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		tv_nodata.setVisibility(View.GONE);
		tv_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				new TimelineTask().execute(TIMELINE_API);
			}
		});

		if (AvailList.isEmpty()) {
			current_page = 1;
			Log.e(TAG, "@[adapter.isEmpty()]... current_page=" + current_page);
			new TimelineTask().execute(TIMELINE_API);
		}
			
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
		
		TIMELINE_API = 
				"{" 
					+ "\"id\":" + "\"" + UserInfoFActivity.KEY_GET_USER_ID + "\","
					+ "\"auth_id\":" + "\"" + UserInfoFActivity.getUserDataID + "\""
				+ "}";
		
		if(Utilities.isInternetOn(ctx))
			new MsgConnTask().execute(UserInfoFActivity.KEY_GET_USER_ID);
	};
	
	private void setupSearchView() {
		
 		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setIconifiedByDefault(false);
 		mSearchView.setQueryHint("Search Here");
	}
	
	@Override
	 public boolean onQueryTextChange(String newText) {
			if(Utilities.D)Log.v(TAG,"searching-=" + newText);
	     	if (TextUtils.isEmpty(newText)) {
	     		isLoadingMore = true;
				if(Utilities.D)Log.v(TAG,"emptyText-=" + newText);
//	    		adapter.getFilter().filter(newText.toString());
	            mListView.clearTextFilter();
	            mListView.setAdapter(adapter);
	            adapter.notifyDataSetChanged();
	        } else {
	     		isLoadingMore = false;
				if(Utilities.D)Log.v(TAG,"query-=" + newText);
//	            mListView.setFilterText(newText.toString());
//	    		adapter.getFilter().filter(newText.toString());
	        }
	        return true;
	    }
	 @Override
	    public boolean onQueryTextSubmit(String query) {

			if(Utilities.D)Log.v(TAG,"query-submit=" + query);
//			adapter.getFilter().filter(query.toString());
			
	        return false;
	    }
	
	class TimelineTask extends AsyncTask<String, String, String> {
		int api_code = 0, api_total = 0;
		public String api_message = "", api_data;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			isLoadingMore = false;
			progressView.setVisibility(View.VISIBLE);
			tv_nodata.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String UrlBase = Host.TimelineUrl + "page/" + current_page + "/";
				String jsonString = Utilities.sendData(UrlBase, f_url[0]);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_total = meta.getInt("count");
					total_pages = api_total;
					JSONArray data = (JSONArray) reader.get("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						
//						if(obj.getString("property_listing_parent").contains("Availability")){
						MyTimelineSetter ch = new MyTimelineSetter();
						
						ch.setActivity_id(obj.getString("activity_id"));
						ch.setActivity_type_id(obj.getString("activity_type_id"));
						ch.setEvent_id(obj.getString("event_id"));
						ch.setPosted_by(obj.getString("posted_by"));
						ch.setType(obj.getString("type"));
						ch.setShared_by(obj.getString("shared_by"));
						ch.setActivity_datetime(obj.getString("activity_datetime"));
						ch.setLike(obj.getString("like"));
						ch.setContent(obj.getString("content"));
						ch.setShare_content(obj.getString("share_content"));
						ch.setStatus(obj.getString("status"));
						ch.setTimestamp(obj.getString("timestamp"));
						ch.setType_id(obj.getString("type_id"));
						ch.setType_name(obj.getString("type_name"));
						ch.setId(obj.getString("id"));
						ch.setFname(obj.getString("fname"));
						ch.setLname(obj.getString("lname"));
						ch.setImage(obj.getString("image"));
						
						if(Integer.parseInt(obj.getString("activity_type_id")) == 3){
							ch.setConnect_from(obj.getString("connect_from"));
							ch.setConnect_to(obj.getString("connect_to"));
						}
						
						if(Integer.parseInt(obj.getString("shared_by")) != 0){
							ch.setShare(obj.getString("share"));
						}
						
						if(Integer.parseInt(obj.getString("activity_type_id")) == 1){
							ch.setProperty_details(obj.getString("property_details"));
							ch.setProperty_images(obj.getString("property_images"));
						}
						
						ch.setTotal_likes(obj.getString("total_likes"));
						ch.setComments(obj.getString("comments"));
						ch.setColor(Utilities.getRandomColor(ctx,(obj.getString("fname"))));
						ch.setBool_enable(true);
						ch.setBool_hasLike(false);
						
						AvailList.add(ch);
						}
//					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return f_url[0];
		}

		@Override
		protected void onPostExecute(String file_url) {
			if(Utilities.D)Log.v(TAG,"file_url="+file_url);
			
			progressView.setVisibility(View.GONE);
			isLoadingMore = true;
			if(AvailList.isEmpty()){
				tv_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("Internet problem !");
			}

			if (api_code == 200) {
//				mSearchView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				if(AvailList.isEmpty()){
					tv_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText(api_message);
				}
				mPrevTotalItemCount = totalItemCount;
				current_page = current_page + 1;
				adapter.notifyDataSetChanged();
			} else if(api_code == 400){
				if(AvailList.isEmpty()){
					tv_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText(api_message);
				}
			} else if(api_code == 501){
				if(AvailList.isEmpty()){
					tv_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText(api_message);
				}
			} else {
				if(AvailList.isEmpty()){
					tv_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("Something went wrong, please try again !");
				}
//				Toast.makeText(getActivity(), "Something went wrong, please try again !", Toast.LENGTH_SHORT).show();
			}
//			if(Utilities.isInternetOn(ctx))
//				new MsgConnTask().execute(UserDetailsScreenFActivity.KEY_GET_USER_ID);
			
		}
	}
	
//	class MsgConnTask extends AsyncTask<String, String, String> {
//		int api_code = 0;
//		public String api_message = "", api_data;
//		Dialog dialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... f_url) {
//			try {
//					String MESSAGE_API = 
//							"{" 
//							+ "\"id\":"
//							+ "\"" + f_url[0] + "\""
//							+ "}";
//
//				String UrlBase = Host.getUserDetailsUrl + "";
//				String jsonString = Utilities.sendData(UrlBase, MESSAGE_API);
//				JSONObject reader = new JSONObject(jsonString);
//
//				JSONObject meta = (JSONObject) reader.get("meta");
//				api_code = meta.getInt("code");
//				api_message = meta.getString("message");
//				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);
//
//				if (api_code == 200) {
//					listMsgConn.clear();
//					
//					JSONObject data = (JSONObject) reader.get("data");
//					
//					listMsgConn.add(data.getString("total_connected") + "");
//					listMsgConn.add(data.getString("total_messages") + "");
//					listMsgConn.add(data.getString("total_properties") + "");
//					
//					if(Utilities.D)Log.v(TAG,"listMsgConn=" + listMsgConn);
//				} else {
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String file_url) {
//			super.onPostExecute(file_url);
//			
//			if(api_code == 200){
//				tv_connection198.setText(listMsgConn.get(0));
//				tv_msg136.setText(listMsgConn.get(1));
//				tv_notification.setText(listMsgConn.get(2));
//			}
//		}
//	}

//	public class FilterListFragment extends ListFragment {
//	  
//	  @Override  
//	  public void onListItemClick(ListView l, View v, int position, long id) {  
//		  if(Utilities.D)Log.v(TAG,"listfragment clicks");
//	  }  
//	  
//	  @Override  
//	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
////	   ArrayAdapter<String> adapter_temp = new ArrayAdapter<String>(  
////	     inflater.getContext(), android.R.layout.simple_list_item_1, numbers_text);  
//			adapter = new MyTimelineAdapter(getActivity(), AvailList);
//			setListAdapter(adapter);  
//	   
//	   return super.onCreateView(inflater, container, savedInstanceState);  
//	  }  
//	 }  
	
	class SendMessageTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;
		String timeString = "", statusMsg = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressView.setVisibility(View.VISIBLE);
//			btn_SendMesssage.setText("Sending..");
			btn_SendMesssage.setEnabled(false);
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
					+ et_textSomething.getText().toString() + "\",\"timestamp\":\""
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
				tv_connection198.setText(listMsgConn.get(0));
				tv_msg136.setText(listMsgConn.get(1));
				tv_notification.setText(listMsgConn.get(2));

				if(Utilities.isInternetOn(ctx))
					new UserDetailsTask().execute(UserInfoFActivity.KEY_GET_USER_ID);
			}
		}
	}
	
	class UserDetailsTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data = "", api_id = "", api_email = "", 
				api_user_type = "", api_userName = "", api_fName = "", api_lName = "", api_about_u = "", api_phone_no = "",
				api_image = "", api_half_image = "", api_city = "", api_country_code = "", api_company_name = "", 
				api_company_address = "", api_use_company_name = "", api_show_phone = "", 
				api_activate = "", api_facebook_id = "", api_google_id = "", api_registered_on = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			
			mListView.setVisibility(View.VISIBLE);
			if(api_code == 200){
				tv_location.setText(api_city);
				String getFullName = api_fName + " " + api_lName;
				tv_name.setText(getFullName);
				tv_userType.setText("(" + Utilities.makeFirstLetterCaps(api_user_type) + ")");
				tv_company_name.setText(api_company_name);
				
				if(api_user_type.contains("individual")) {
					tv_emailid.setVisibility(View.GONE);
					tv_phon_no.setVisibility(View.GONE);
				} else {
					tv_emailid.setVisibility(View.VISIBLE);
					tv_phon_no.setVisibility(View.VISIBLE);
				}
				
				tv_emailid.setText(api_email);
				tv_phon_no.setText("(" + api_phone_no + ")");
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
		    	
				new CheckFriendTask().execute("");
				
				if(UserInfoFActivity.KEY_GET_USER_ID.equals(UserInfoFActivity.getUserDataID)){
					btn_SendMesssage.setVisibility(View.GONE);
				}
			}
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
		if(v == tv_connection198 || v == iv_connection){
//			Intent i = new Intent(getActivity(), UserConnectionFActivity.class);
//			i.putExtra("KEY_GET_USER_ID", UserDetailsScreenFActivity.KEY_GET_USER_ID);
//			startActivity(i);
//			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			getActivity().finish();
//		}else if(v == tv_msg136 || v == iv_msg){
//			Intent i = new Intent(getActivity(), MessageFActivity.class);
//			startActivity(i);
//			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			getActivity().finish();
//		}else if(v == btn_postSomething){
//			String getPostText = et_textSomething.getText().toString();
//			if(!getPostText.isEmpty()){
//				if(Utilities.isInternetOn(ctx))
//					new PostSomethingTask().execute(getPostText);
//			}else{
//				Toast.makeText(ctx, "Pease write something.. !", 1).show();
//			}
		}
	}
}