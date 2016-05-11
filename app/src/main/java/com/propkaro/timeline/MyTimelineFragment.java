package com.propkaro.timeline;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.propkaro.post.PostPropertyActivity;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;
public class MyTimelineFragment extends Fragment implements SearchView.OnQueryTextListener, OnClickListener{
	private static final String TAG = MyTimelineFragment.class.getSimpleName();
	static final String KEY_POSITION = "TabProductFragment:Position";
	List<MyTimelineSetter> AvailList = new ArrayList<MyTimelineSetter>();
	ListView mListView;
	MyTimelineAdapter adapter;
	static CircularProgressView progressView;
//	TextView tv_nodata;
	FragmentActivity fActivity;
	Context ctx;
	Button Btngetdata;
	static int POSITION;
	private SearchView mSearchView;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	View rView;
	EditText et_textSomething;
	Button btn_postSomething;
	private SharedPreferences mPrefs;
	String getMobile_num = "", getEmailID = "", getUserType = "", getCity = "",  
			getUserName = "", getFName = "", getLName = "", getProfileImage = "", getHalfImage = "";
	static String getUserDataID = "";
	String TIMELINE_API = "";
	private ImageView iv_connection, iv_msg, iv_location;
	private TextView tv_connection198, tv_msg136, tv_location, tv_notification;
	private List<String> listMsgConn = new ArrayList<String>();

	public static MyTimelineFragment newInstance(int pos) {

		MyTimelineFragment fragment = new MyTimelineFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		MyTimelineFragment.POSITION = pos;
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

		ViewGroup headerView = (ViewGroup)inflater.inflate(R.layout.adapter_timeline_post, mListView, false);
		mListView.addHeaderView(headerView, null, false);

		iv_connection = (ImageView) headerView.findViewById(R.id.iv_connection);
		tv_connection198 = (TextView) headerView.findViewById(R.id.tv_connection198);
		tv_connection198.setText("--");
		iv_msg = (ImageView) headerView.findViewById(R.id.iv_msg);
		tv_msg136 = (TextView) headerView.findViewById(R.id.tv_msg136);
		tv_msg136.setText("--");
		iv_location = (ImageView) headerView.findViewById(R.id.iv_location);
		tv_location = (TextView) headerView.findViewById(R.id.tv_location);
		tv_location.setText("--");
		tv_location.setText(getCity);
		tv_notification = (TextView) headerView.findViewById(R.id.tv_notification);
		
		iv_connection.setOnClickListener(this);
		tv_connection198.setOnClickListener(this);
		iv_msg.setOnClickListener(this);
		tv_msg136.setOnClickListener(this);
		
		ImageView imageView_pc = (ImageView) headerView.findViewById(R.id.iv_profilePost);
		ImageLoader.getInstance().displayImage(getProfileImage, imageView_pc, AvailPCFragment.animateFirstListener);
		
		TextView tv_profilePost = (TextView) headerView.findViewById(R.id.tv_profilePost);
		tv_profilePost.setText("");
		tv_profilePost.setVisibility(View.GONE);
    	
    	if(getProfileImage.contains("default")){
    		if(getUserName.length() > 0){
    			tv_profilePost.setVisibility(View.VISIBLE);
    			tv_profilePost.getBackground().setColorFilter(Utilities.getRandomColor(ctx,getUserName), PorterDuff.Mode.SRC_IN);
    			tv_profilePost.setText(String.valueOf(getUserName.charAt(0)).toUpperCase());
    		} else {
    			tv_profilePost.setVisibility(View.GONE);
    		}
    	}

		TextView textView_pc = (TextView) headerView.findViewById(R.id.textView_pc);
		textView_pc.setText("");
		textView_pc.setVisibility(View.GONE);
    	
    	if(getProfileImage.contains("default")){
    		if(getUserName.length() > 0){
    			textView_pc.setVisibility(View.VISIBLE);
    			textView_pc.getBackground().setColorFilter(Utilities.getRandomColor(ctx,getUserName), PorterDuff.Mode.SRC_IN);
    			textView_pc.setText(String.valueOf(getUserName.charAt(0)).toUpperCase());
    		} else {
    			textView_pc.setVisibility(View.GONE);
    		}
    	}

		et_textSomething = (EditText) headerView.findViewById(R.id.et_textSomething);
		btn_postSomething = (Button)headerView.findViewById(R.id.btn_postSomething);
		btn_postSomething.setOnClickListener(this);
		
		adapter = new MyTimelineAdapter(getActivity(), AvailList);
		mListView.setAdapter(adapter);
		
		mListView.setTextFilterEnabled(false);
//		mListView.setOnItemClickListener(new OnItemClickListenerListViewItem());
		setupSearchView(); 		
	    
		TextView tv_name = (TextView)headerView.findViewById(R.id.tv_name);
		TextView tv_userType = (TextView)headerView.findViewById(R.id.tv_userType);
		tv_name.setText(getUserName);
		tv_userType.setText("( " + Utilities.makeFirstLetterCaps(getUserType) + " )");
		
		
		ImageView iv_profile = (ImageView) headerView.findViewById(R.id.iv_profile);
		ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, AvailPCFragment.animateFirstListener);
		
//		try {
//			File file = ImageLoader.getInstance().getDiscCache().get(getProfileImage);
//			if(Utilities.D)Log.v(TAG,"" + file.exists());
//			if (!file.exists()) {
//				DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc().build();
//				ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, options);
//			} else {
//				iv_profile.setImageURI(Uri.parse(file.getAbsolutePath()));
//			}
//		} catch (Exception e) { e.printStackTrace(); }
		
	    Button btn_select_city = (Button)rView.findViewById(R.id.btn_select_city);
	    btn_select_city.setText("POST PROPERTY");
	    btn_select_city.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(Utilities.D)Log.v(TAG,"btn_select_city");
				Intent i = new Intent(getActivity(), PostPropertyActivity.class);
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				getActivity().finish();
			}
		});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView,
					int scrollState) {
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
		
//	    tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
//		tv_nodata.setVisibility(View.GONE);
//		tv_nodata.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
//				new TimelineTask().execute(TIMELINE_API);
//			}
//		});

		if (AvailList.isEmpty()) {
			current_page = 1;
			Log.e(TAG, "@[adapter.isEmpty()]... current_page=" + current_page);
			new TimelineTask().execute(TIMELINE_API);
		}
			
		return rView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fActivity = getActivity();
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		
		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
		getHalfImage = mPrefs.getString("PREF_KEY_HALF_IMAGE", "");
		if(Utilities.D)Log.v(TAG,"getMobile_num="+getMobile_num);
		if(Utilities.D)Log.v(TAG,"getEmailID="+getEmailID);
		if(Utilities.D)Log.v(TAG,"getUserType="+getUserType);
		if(Utilities.D)Log.v(TAG,"getUserID="+getUserDataID);
		if(Utilities.D)Log.v(TAG,"getUserName="+getUserName);
		if(Utilities.D)Log.v(TAG,"getProfileImage="+getProfileImage);
		
		TIMELINE_API = 
				"{" 
					+ "\"id\":" + "\"" + getUserDataID + "\","
					+ "\"auth_id\":" + "\"" + getUserDataID + "\""
				+ "}";
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
//			tv_nodata.setVisibility(View.GONE);
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
						ch.setThumbnailUrl(obj.getString("image"));
						
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
						ch.setColor(Utilities.getRandomColor(ctx,obj.getString("fname")));
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
			
			mListView.setVisibility(View.VISIBLE);
			progressView.setVisibility(View.GONE);
			isLoadingMore = true;
//			if(AvailList.isEmpty()){
//				tv_nodata.setVisibility(View.VISIBLE);
//				tv_nodata.setText("Internet problem !");
//			}

			if (api_code == 200) {
//				mSearchView.setVisibility(View.VISIBLE);
//				mListView.setVisibility(View.VISIBLE);
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("No data found !");
//				}
				mPrevTotalItemCount = totalItemCount;
				current_page = current_page + 1;
				adapter.notifyDataSetChanged();
			} else if(api_code == 400){
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("No data found !");
//				}
			} else if(api_code == 501){
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText(api_message);
//				}
			} else {
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("Something went wrong, please try again !");
//				}
//				Toast.makeText(getActivity(), "Something went wrong, please try again !", Toast.LENGTH_SHORT).show();
			}
			if(Utilities.isInternetOn(ctx))
				new MsgConnTask().execute(getUserDataID);
			
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
			}
		}
	}

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
//	
	class PostSomethingTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;
		String timeString = "", statusMsg = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressView.setVisibility(View.VISIBLE);
			btn_postSomething.setText("Posting..");
			btn_postSomething.setEnabled(false);
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				statusMsg = f_url[0];
				Date date = new Date();
				timeString = new Timestamp(date.getTime()).toString();
				
					String POST_SOMETHING_API = 
							"{\"table\":\"activities\",\"params\":{\"activity_type_id\":\"2\",\"event_id\":0,\"posted_by\":\"" 
									+ getUserDataID + "\",\"activity_datetime\":\"" 
									+ timeString + "\",\"content\":\"Status Updated\",\"status\":\"" 
									+ statusMsg + "\"}}";
					
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, POST_SOMETHING_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					api_data = reader.getString("data");
					
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
//				tv_nodata.setVisibility(View.GONE);
				et_textSomething.setText("");
				btn_postSomething.setText("POST");
				btn_postSomething.setEnabled(true);
				progressView.setVisibility(View.GONE);
				
				MyTimelineSetter ch = new MyTimelineSetter();
				ch.setEvent_id("0");
				ch.setActivity_type_id("2");
				ch.setPosted_by(getUserDataID);
				ch.setTotal_likes("0");
				ch.setLike("like");
				ch.setShared_by("0");
				ch.setFname(getFName);
				ch.setLname(getLName);
				ch.setImage(getHalfImage);
				Log.e(TAG, "getHalfImage="+getHalfImage);
				
				ch.setComments("");
				ch.setcomment_id(0);
				ch.setActivity_id(api_data);
				ch.setStatus(statusMsg);
				ch.setContent("Status Updated");
				ch.setTimestamp(timeString);
				ch.setColor(Utilities.getRandomColor(ctx,getFName));
				ch.setBool_enable(true);
				ch.setBool_hasLike(false);

				AvailList.add(0, ch);
				if(getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});
				Toast.makeText(ctx, api_message, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == tv_connection198 || v == iv_connection){
//			Intent i = new Intent(getActivity(), MyConnectionFActivity.class);
//			startActivity(i); 
//			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			getActivity().finish();
//		}else if(v == tv_msg136 || v == iv_msg){
//			Intent i = new Intent(getActivity(), MessageFActivity.class);
//			startActivity(i);
//			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			getActivity().finish();
		}else if(v == btn_postSomething){
			Utilities.hideKeyboard(fActivity);
			String getPostText = et_textSomething.getText().toString().replace("\n", " ");
			if(!getPostText.isEmpty()){
				if(Utilities.isInternetOn(ctx))
					new PostSomethingTask().execute(getPostText);
			}else{
//				Toast.makeText(ctx, "Please write something.. !", 1).show();
				final Toast toast = Toast.makeText(ctx, "Please write something.. !", Toast.LENGTH_SHORT);
			    toast.show();

			    Handler handler = new Handler();
			        handler.postDelayed(new Runnable() {
			           @Override
			           public void run() {
			               toast.cancel(); 
			           }
			    }, 500);
			}
		}
	}
}