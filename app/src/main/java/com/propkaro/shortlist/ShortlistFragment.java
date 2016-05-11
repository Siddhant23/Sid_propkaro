package com.propkaro.shortlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.propkaro.GridActivity;
import com.propkaro.R;
import com.propkaro.filters.FilterFActivity;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class ShortlistFragment extends Fragment {
	private static final String TAG = ShortlistFragment.class.getSimpleName();
	public static ArrayList<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	private RecyclerView mRecyclerView;
	public static ShortlistRecyclerView adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout mCoordinator;
    static RelativeLayout rel_nodata;
    static TextView tv_nodata;
    static ImageView iv_nodata;
	FragmentActivity fActivity;
	Context ctx;TinyDB db;
	static int POSITION;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	int pastVisiblesItems, visibleItemCount;
	LinearLayoutManager mLayoutManager;
	String API_NEWS_FEED = "";
	View rView;
	CircularProgressView progressView;

	public static ShortlistFragment newInstance(int pos) {
		ShortlistFragment fragment = new ShortlistFragment();
		Bundle b = new Bundle();
		fragment.setArguments(b);

		ShortlistFragment.POSITION = pos;
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
		if(db.getBoolean("shouldReload", false)){
			if(Utilities.isInternetOn(ctx)){
				current_page = 1;
				AvailList.clear();
				new ShortlistTask().execute(API_NEWS_FEED);
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
		ctx = getActivity();db = new TinyDB(ctx);
		fActivity = getActivity();
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.android_propertycenter_availability1, container, false);
		progressView = (CircularProgressView) rView.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);
		
        swipeRefreshLayout = (SwipeRefreshLayout) rView.findViewById(R.id.swipe_refresh_layout);
        mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);
		mRecyclerView = (RecyclerView) rView.findViewById(R.id.list_avail_pc);
		mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	    adapter = new ShortlistRecyclerView(getActivity(), AvailList);
		mRecyclerView.setAdapter(adapter);
//		mRecyclerView.smoothScrollToPosition(0);
//		mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
		mRecyclerView.setLayoutManager(mLayoutManager);
		int []colors = {
				android.R.color.holo_blue_dark, android.R.color.holo_red_dark,
                android.R.color.holo_green_dark, android.R.color.holo_orange_dark, 
                android.R.color.holo_purple, android.R.color.holo_red_dark, 
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
					new ShortlistTask().execute(API_NEWS_FEED);
				}
			}
		});
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
            	AvailList.clear();
        		if (AvailList.isEmpty()) {
        			if(Utilities.isInternetOn(ctx)){
        				current_page = 1;
        				AvailList.clear();
        				new ShortlistTask().execute(API_NEWS_FEED);
        			} else {
        				rel_nodata.setVisibility(View.VISIBLE);
        				tv_nodata.setText("No Internet Connection, try again ?");
        			}
        		}
            }
        }
        );
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

		    	if(dy > 0) {
		            visibleItemCount = mLayoutManager.getChildCount();
		            totalItemCount = mLayoutManager.getItemCount();
		            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

		            if (isLoadingMore && current_page <= total_pages) {
		                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
							if(Utilities.isInternetOn(ctx))
								new ShortlistTask().execute(API_NEWS_FEED);
		                }
		            }
		        }
		    }
		});
		
		TextView tv_addLinsting= (TextView)rView.findViewById(R.id.tv_addLinsting);
		tv_addLinsting.setVisibility(View.VISIBLE);
		tv_addLinsting.setText("Filter Now");
		tv_addLinsting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(ctx)){
	            	Intent i = new Intent(ctx, FilterFActivity.class);
	            	startActivity(i);
				}
			}
		});
		iv_nodata = (ImageView)rView.findViewById(R.id.iv_nodata);
		iv_nodata.setImageResource(R.mipmap.bg_no_shortlisting);
		tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		rel_nodata = (RelativeLayout)rView.findViewById(R.id.rel_nodata);
		rel_nodata.setVisibility(View.GONE);
		rel_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				if(Utilities.isInternetOn(ctx)){
					current_page = 1;
					new ShortlistTask().execute(API_NEWS_FEED);
				} else {
					rel_nodata.setVisibility(View.VISIBLE);
					tv_nodata.setText("No Internet Connection, try again ?");
				}
			}
		});

		return rView;
	}//endOncreaView
		
		class ShortlistTask extends AsyncTask<String, String, String> {
			int api_code = 0, api_total = 0, api_lastProperty = 0;
			public String api_message = "", api_data = "";

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				isLoadingMore = false;
				swipeRefreshLayout.setRefreshing(true);
				progressView.setVisibility(View.GONE);
				rel_nodata.setVisibility(View.GONE);
			}

			@Override
			protected String doInBackground(String... f_url) {
				try {
					API_NEWS_FEED = "{\"id\":\"" + GridActivity.getUserDataID + "\"}";
					String UrlBase = Host.ShortListUrl + "page/" + current_page + "/";
					String jsonString = Utilities.sendData(UrlBase, API_NEWS_FEED);
					JSONObject reader = new JSONObject(jsonString);

					JSONObject meta = (JSONObject) reader.get("meta");
					api_code = meta.getInt("code");
					api_message = meta.getString("message");

					if (api_code == 200) {
						api_total = meta.getInt("total_pages");
						total_pages = api_total;
						
						JSONArray data = (JSONArray) reader.get("data");
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.getJSONObject(i);
							Host.parseShortlist(ctx, obj, AvailList, false);
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return f_url[0];
			}

			@Override
			protected void onPostExecute(String file_url) {
				
				swipeRefreshLayout.setRefreshing(false);
				progressView.setVisibility(View.GONE);
				isLoadingMore = true;

				if (api_code == 200) {
					mPrevTotalItemCount = totalItemCount;
					current_page = current_page + 1;
					adapter.notifyDataSetChanged();
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No shortlist found !");
					}
				} else if(api_code == 400){
					isLoadingMore = false;
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("Enough Choices \nFind Your Match !");
					}
					Utilities.Snack(mCoordinator, "No shortlist found !");
				} else {
					if(AvailList.isEmpty()){
						rel_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("Something went wrong, please try again !");
					}
					Utilities.Snack(mCoordinator, "Something went wrong, please try again !");
				}
			}
		}
		public static void checkEmptyList(){
			if(AvailList.isEmpty()){
				rel_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("Enough Choices \nFind Your Match !");
			} else {
				rel_nodata.setVisibility(View.GONE);
			}
		}
	}