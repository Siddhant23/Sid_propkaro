package com.propkaro.mylisting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.propkaro.R;
import com.propkaro.post.PostPropertyActivity;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

public class AvailMLFragment extends Fragment {
	private static final String TAG = AvailMLFragment.class.getSimpleName();
	private static final String KEY_POSITION = "AvailFragment:Position";
	public ArrayList<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private static int OFFSET = 0;
	private RecyclerView mRecyclerView;
	public AvailMLRecyclerView adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout mCoordinator;
    RelativeLayout rel_nodata;
    ImageView iv_nodata;
    TextView tv_nodata;
	Context ctx;
	static int POSITION;
	CircularProgressView progressView;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	int pastVisiblesItems, visibleItemCount;
	LinearLayoutManager mLayoutManager;
	String getUserDataID = "";
	String API_NEWS_FEED = "";
	String REFRESH_API = "";
	View rView;

	public static AvailMLFragment newInstance(int pos) {
		AvailMLFragment fragment = new AvailMLFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		AvailMLFragment.POSITION = pos;
		Log.e(TAG, "Position=" + pos);

		return fragment;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(AvailList.isEmpty()){
			if(Utilities.isInternetOn(ctx)){
				current_page = 1;
				AvailList.clear();
				new NewsFeedTask().execute(API_NEWS_FEED);
			} else {
				rel_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("No No Internet Connection, \n\ntry again ?");
			}
		}
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();

		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		API_NEWS_FEED = 
				"{\"by\": {\"user_id\": \"" + getUserDataID 
				+ "\"}, \"order_by\": \"property_added_on\", " +
				"\"table\": \"properties as p\", " +
				"\"multiple\": \"All\", " +
				"\"join\": {\"registration as r\": {\"r.id\": \"p.user_id\"}}}";
		if(Utilities.D)Log.v(TAG,"API_NEWS_FEED=" + API_NEWS_FEED);
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.android_propertycenter_availability1, container, false);
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		progressView = (CircularProgressView) rView.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);
		
        swipeRefreshLayout = (SwipeRefreshLayout) rView.findViewById(R.id.swipe_refresh_layout);
		mRecyclerView = (RecyclerView) rView.findViewById(R.id.list_avail_pc);
		mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	    adapter = new AvailMLRecyclerView(getActivity(), AvailList);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
//        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//			
//			@Override
//			public void onRefresh() {
//				if(Utilities.isInternetOn(ctx)){
//					current_page = 1;
//					AvailList.clear();
//					new NewsFeedTask().execute(API_NEWS_FEED);
//				}
//			}
//		});
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//        		if (AvailList.isEmpty()) {
//        			Log.e(TAG, "@[adapter.isEmpty()]... current_page=" + current_page);
//        			
//        			if(Utilities.isInternetOn(ctx)){
//        				current_page = 1;
//        				AvailList.clear();
//        				new NewsFeedTask().execute(API_NEWS_FEED);
//        			} else {
//        				rel_nodata.setVisibility(View.VISIBLE);
//        				tv_nodata.setText("No No Internet Connection, \ntry again ?");
//        			}
//        		}
//            }
//        }
//        );
        
//		ViewGroup headerView = (ViewGroup)inflater.inflate(R.layout.layout_multicity, mRecyclerView, false);
//		mRecyclerView.addHeaderView(headerView, null, false);
		
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		        if(dy > 0) {
		            visibleItemCount = mLayoutManager.getChildCount();
		            totalItemCount = mLayoutManager.getItemCount();
		            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

		            if (isLoadingMore  && current_page <= total_pages) {
		                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
							if(Utilities.isInternetOn(ctx))
								new NewsFeedTask().execute(API_NEWS_FEED);
		                }
		            }
		        }
		    }
		});
		
		iv_nodata = (ImageView)rView.findViewById(R.id.iv_nodata);
		iv_nodata.setImageResource(R.mipmap.bg_no_listing);
		tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		TextView tv_addLinsting= (TextView)rView.findViewById(R.id.tv_addLinsting);
		tv_addLinsting.setVisibility(View.VISIBLE);
		tv_addLinsting.setText("Add Listing");
		tv_addLinsting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(ctx)){
					Intent i = new Intent(getActivity(), PostPropertyActivity.class);
					startActivity(i);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		
		rel_nodata = (RelativeLayout)rView.findViewById(R.id.rel_nodata);
		rel_nodata.setVisibility(View.GONE);
		rel_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				if(Utilities.isInternetOn(ctx)) {
					new NewsFeedTask().execute(API_NEWS_FEED);
				} 
			}
		});
		
		return rView;
	}//endOncreaView
		
		class NewsFeedTask extends AsyncTask<String, String, String> {
			int api_code = 0, api_total = 0, api_lastProperty = 0;
			public String api_message = "", api_data = "";

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				isLoadingMore = false;
				progressView.setVisibility(View.VISIBLE);
				rel_nodata.setVisibility(View.GONE);
			}

			@Override
			protected String doInBackground(String... f_url) {

				try {
					String UrlBase = Host.SearchUrl 
							+ "page/" + current_page + "/";
					String jsonString = Utilities.sendData(UrlBase, f_url[0]);
					JSONObject reader = new JSONObject(jsonString);

					JSONObject meta = (JSONObject) reader.get("meta");
					api_code = meta.getInt("code");
					api_message = meta.getString("message");

					if (api_code == 200) {
						api_total = meta.getInt("count");
						total_pages = api_total;
						api_lastProperty = meta.getInt("lastProperty");
		                
						if (api_lastProperty > OFFSET)
		                    OFFSET = api_lastProperty;
		                
						if(Utilities.D)Log.v(TAG,"offSet-=" + OFFSET);
						
						JSONArray data = (JSONArray) reader.get("data");
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.getJSONObject(i);
							Host.parseProperty(ctx, obj, AvailList, false);
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return f_url[0];
			}

			@Override
			protected void onPostExecute(String file_url) {
				
				progressView.setVisibility(View.GONE);
				isLoadingMore = true;

				if (api_code == 200) {
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
					mPrevTotalItemCount = totalItemCount;
					current_page = current_page + 1;
					adapter.notifyDataSetChanged();

				} else if(api_code == 400){
					isLoadingMore = false;
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
					Utilities.Snack(mCoordinator, api_message);
				} else {
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("Something went wrong, \nplease try again !");
					}
					Utilities.Snack(mCoordinator, "Something went wrong !");
				}
			}
		}//endAvaillist
	}