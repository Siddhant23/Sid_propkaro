package com.propkaro.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.propkaro.GridActivity;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCRecyclerView;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

public class FilterFragment extends Fragment {
	private static final String TAG = FilterFragment.class.getSimpleName();
	private static final String KEY_POSITION = "AvailFragment:Position";
	public ArrayList<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private static int OFFSET = 0;
	private RecyclerView mRecyclerView;
	public AvailPCRecyclerView adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout mCoordinator;
    RelativeLayout rel_nodata;
    TextView tv_nodata;
	Context ctx;
	static int POSITION;
	CircularProgressView progressView;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	int pastVisiblesItems, visibleItemCount;
	LinearLayoutManager mLayoutManager;

	String API_NEWS_FEED = 
			"{\"by\":" +
			"{" + 
//			"\"property_listing_parent\":[\"" 
//					+ "Availability" 
//			+ "\"]" + 
			"\"keywords\":\"" + "" + "\"" + 
//			"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
//			"\"user_id\":\"" + "20" + "\"" + 
					"}, " +
					"\"auth_id\":\"" + GridActivity.getUserDataID + "\"" + 
					"}";//SearchUrl
	String REFRESH_API = "";
	View rView;

	public static FilterFragment newInstance(int pos) {
		FilterFragment fragment = new FilterFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		FilterFragment.POSITION = pos;
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

		Bundle b = this.getArguments();
		API_NEWS_FEED = b.getString("KEY_API_FILTER");
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
	    adapter = new AvailPCRecyclerView(getActivity(), AvailList);
		mRecyclerView.setAdapter(adapter);
//		mRecyclerView.smoothScrollToPosition(0);
//		mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
		mRecyclerView.setLayoutManager(mLayoutManager);
		int [] colors = {
				android.R.color.holo_blue_dark, android.R.color.holo_red_dark,
                android.R.color.holo_green_dark, android.R.color.holo_orange_dark, 
                android.R.color.holo_blue_dark, android.R.color.holo_red_dark, 
                android.R.color.holo_green_dark, android.R.color.holo_orange_dark, 
		};
		swipeRefreshLayout.setColorSchemeResources(colors);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
    			if(Utilities.isInternetOn(ctx)){
    				current_page = 1;
    				AvailList.clear();
    				new NewsFeedTask().execute(API_NEWS_FEED);
//    				new citiesTask().execute("");
    			} else {
    				rel_nodata.setVisibility(View.VISIBLE);
    				tv_nodata.setText("No Internet Connection, try again ?");
    			}
			}
		});
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
        		if (AvailList.isEmpty()) {
        			Log.e(TAG, "@[adapter.isEmpty()]... current_page=" + current_page);
        			
        			if(Utilities.isInternetOn(ctx)){
        				current_page = 1;
        				new NewsFeedTask().execute(API_NEWS_FEED);
//        				new citiesTask().execute("");
        			} else {
        				rel_nodata.setVisibility(View.VISIBLE);
        				tv_nodata.setText("No Internet Connection, try again ?");
        			}
        		}
            }
        }
        );
        
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
		
		ImageView iv_nodata = (ImageView)rView.findViewById(R.id.iv_nodata);
		iv_nodata.setImageResource(R.mipmap.bg_no_listing);
		tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		rel_nodata = (RelativeLayout)rView.findViewById(R.id.rel_nodata);
		rel_nodata.setVisibility(View.GONE);
		rel_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				if(Utilities.isInternetOn(ctx)) {
					new NewsFeedTask().execute(API_NEWS_FEED);
				} else {
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("No Internet Connection, try again ?");
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
					
//					SetPreference setPreference =new SetPreference();
//					setPreference.addFavorite(ctx, AvailList.get(0));
//					setPreference.addFavorite(ctx, AvailList.get(1));
//					setPreference.addFavorite(ctx, AvailList.get(2));
//					AvailList.clear();
//					AvailList = setPreference.getFavorites(ctx);
//					adapter.notifyDataSetChanged();
//					Log.e(TAG, "-----------"+AvailList);
//					for(int i = 0; i<AvailList.size(); i++){
//						Log.i(TAG, "-----------"+AvailList.get(i)+"-----------");
//					}

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
						tv_nodata.setText("Something went wrong, please try again !");
					}
					Utilities.Snack(mCoordinator, "Something went wrong !");
				}
			}
		}//endAvaillist
		
		class CheckRefreshTask extends AsyncTask<String, String, String> {
			int api_code = 0, api_total = 0, api_lastProperty = 0;
			public String api_message = "", api_data = "";

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				isLoadingMore = false;
		        swipeRefreshLayout.setRefreshing(true);
				rel_nodata.setVisibility(View.GONE);
			}

			@Override
			protected String doInBackground(String... f_url) {
				try {
			    	REFRESH_API = 
			    			"{\"by\":" +
			    					"{" + 
			    					"\"keywords\":\"" + "" + "\"" + 
			    							"}, " +
			    							"\"auth_id\":\"" + GridActivity.getUserDataID + "\"," + 
			    							"\"last\":\"" + OFFSET + "\"" + 
			    							"}";
					String UrlBase = Host.SearchUrl;
					String jsonString = Utilities.sendData(UrlBase, REFRESH_API);
					JSONObject reader = new JSONObject(jsonString);

					JSONObject meta = (JSONObject) reader.get("meta");
					api_code = meta.getInt("code");
					api_message = meta.getString("message");

					if (api_code == 200) {
						api_total = meta.getInt("total");
						total_pages = api_total;
						api_lastProperty = meta.getInt("lastProperty");
		                
						if (api_lastProperty > OFFSET)
		                    OFFSET = api_lastProperty;
		                
						JSONArray data = (JSONArray) reader.get("data");
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.getJSONObject(i);
							Host.parseProperty(ctx, obj, AvailList, true);
						}
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return f_url[0];
			}

			@Override
			protected void onPostExecute(String file_url) {
				
		        swipeRefreshLayout.setRefreshing(false);
				isLoadingMore = true;

				if (api_code == 200) {
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
					adapter.notifyDataSetChanged();
				} else if(api_code == 400){
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
					Utilities.Snack(mCoordinator, "Up-to-date !");
				} else {
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("Something went wrong, please try again !");
					}
					Utilities.Snack(mCoordinator, "Something went wrong !");
				}
			}
		}//endRefreshlist
	}