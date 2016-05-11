package com.propkaro.notifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.propkaro.R;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.SetNotityPreference;
import com.propkaro.util.Utilities;

public class NotifyGCMFragment extends Fragment {
	private static final String TAG = NotifyGCMFragment.class.getSimpleName();
	private static final String KEY_POSITION = "AvailFragment:Position";
	private List<NotifyGCMSetter> AvailList = new ArrayList<NotifyGCMSetter>();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private RecyclerView mRecyclerView;
	public static NotifyGCMRecyclerView adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout mCoordinator;
    private BroadcastReceiver receiver;
	private SetNotityPreference setPreference;
    static RelativeLayout rel_nodata;
    TextView tv_nodata;
	Context ctx;
	static int POSITION;
	CircularProgressView progressView;
	LinearLayoutManager mLayoutManager;
	String getUserDataID = "";
	View rView;

	public static NotifyGCMFragment newInstance(int pos) {
		NotifyGCMFragment fragment = new NotifyGCMFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		NotifyGCMFragment.POSITION = pos;
		Log.e(TAG, "Position=" + pos);

		return fragment;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter("ReceivedServicve"));
		AvailList = setPreference.getFavorites(ctx);
		if (AvailList == null)
			AvailList = new ArrayList<NotifyGCMSetter>();
		
			if (AvailList.size() == 0) {
				rel_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("No New Notification !");
			} else if (AvailList != null) {
			    adapter = new NotifyGCMRecyclerView(getActivity(), AvailList);
				mRecyclerView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				rel_nodata.setVisibility(View.GONE);
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
		setPreference = new SetNotityPreference();
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		
		receiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	Log.v(TAG, "---------------onReceive()");
		    	try {
			    	if(intent.getAction().equals("ReceivedServicve")){
				    	NotifyGCMSetter m = new NotifyGCMSetter();
						m.setPROPERTY_ID(intent.getExtras().getInt("gcm_property_id"));
						m.setUSER_ID(intent.getExtras().getInt("gcm_user_id"));
						m.setThumbnailUrl(intent.getExtras().getString("gcm_thumb"));
						m.setImageUrl(intent.getExtras().getString("gcm_image"));
						m.setTitle(intent.getExtras().getString("gcm_title"));
						m.setMessage(intent.getExtras().getString("gcm_message"));
						m.setType(intent.getExtras().getString("gcm_type"));
						m.setTime(intent.getExtras().getString("gcm_time"));
						m.setColor(intent.getExtras().getInt("gcm_color"));
						m.setIsBoolEnable(intent.getExtras().getBoolean("gcm_bool_enable"));
						AvailList.add(0, m);
				    	Log.v(TAG, "---------------m=" + m);
					    adapter = new NotifyGCMRecyclerView(getActivity(), AvailList);
						mRecyclerView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						rel_nodata.setVisibility(View.GONE);
			    	}
				} catch (Exception e) {e.printStackTrace();}
		    }
		};
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
		mRecyclerView.setLayoutManager(mLayoutManager);
		
        swipeRefreshLayout.setEnabled(false);
//        swipeRefreshLayout.setRefreshing(true);
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
//        				new NewsFeedTask().execute(API_NEWS_FEED);
//        			} else {
//        				rel_nodata.setVisibility(View.VISIBLE);
//        				tv_nodata.setText("No Internet Connection, try again ?");
//        			}
//        		}
//            }
//        }
//        );
        
//		ViewGroup headerView = (ViewGroup)inflater.inflate(R.layout.layout_multicity, mRecyclerView, false);
//		mRecyclerView.addHeaderView(headerView, null, false);
		
		tv_nodata = (TextView)rView.findViewById(R.id.tv_nodata);
		tv_nodata.setText("No New Notification !");
		rel_nodata = (RelativeLayout)rView.findViewById(R.id.rel_nodata);
		rel_nodata.setVisibility(View.GONE);
//		rel_nodata.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
//				if(Utilities.isInternetOn(ctx)) {
//					new NewsFeedTask().execute(API_NEWS_FEED);
//				} else {
//					rel_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("No Internet Connection, try again ?");
//				}
//			}
//		});
		//-------------------------load data---------------------------
//		AvailList = setPreference.getFavorites(ctx);
//		setPreference.emptyFavorites(ctx);
//		Log.i(TAG, "AvailList============================================" + AvailList);
//		for(int i = 0; i<5; i++){
//			NotifyGCMSetter m = new NotifyGCMSetter();
//			m.setPROPERTY_ID(i);
//			m.setUSER_ID(i);
//			m.setThumbnailUrl("thumb----------" + i);
//			m.setImageUrl("image----------" + i);
//			m.setTitle("title----------" + i);
//			m.setMessage("message----------" + i);
//			m.setType("type----------" + i);
//			m.setTime("time----------" + i);
//			m.setColor(i);
//			m.setIsBoolEnable(false);
//			Log.i(TAG, "AvailList============================================" + i);
//			AvailList.add(m);
//			setPreference.addFavorite(ctx, AvailList.get(i));
//		}
		AvailList = setPreference.getFavorites(ctx);
		if (AvailList == null) {
//			showAlert(getResources().getString(R.string.no_favorites_items),
//					getResources().getString(R.string.no_favorites_msg));
			rel_nodata.setVisibility(View.VISIBLE);
			tv_nodata.setText("No New Notification !");

		} else {
			if (AvailList.size() == 0) {
//				showAlert(
//						getResources().getString(R.string.no_favorites_items),
//						getResources().getString(R.string.no_favorites_msg));
				rel_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText("No New Notification !");
			} else if (AvailList != null) {
			    adapter = new NotifyGCMRecyclerView(getActivity(), AvailList);
				mRecyclerView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				rel_nodata.setVisibility(View.GONE);
			}
		}		
		
		return rView;
	}//endOncreaView
}