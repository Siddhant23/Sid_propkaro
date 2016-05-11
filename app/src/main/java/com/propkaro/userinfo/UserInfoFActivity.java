package com.propkaro.userinfo;

import java.util.List;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.propkaro.AppController;
import com.propkaro.AppController.TrackerName;
import com.propkaro.util.Utilities;
import com.propkaro.MainActivity;
import com.propkaro.R;
public class UserInfoFActivity extends AppCompatActivity {
	private static final String TAG = UserInfoFActivity.class.getSimpleName();
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	RelativeLayout rel;
	SearchView search;
	Filter filter ;
	Context ctx; Activity activity;
	private SharedPreferences mPrefs;
	static String KEY_GET_USER_ID = "", getUserDataID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = this;activity = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		setContentView(R.layout.user_detail_tabs_pager_view);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//      activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
            	List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

            	if(taskList.get(0).numActivities > 1){
            		Log.e(TAG, taskList.get(0).numActivities + " Not Null");
        			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            	} else {
            		Log.e(TAG, taskList.get(0).numActivities + " NULL");
            		Intent i = new Intent(UserInfoFActivity.this, MainActivity.class);
            		startActivity(i); finish();
            	}
            }
        });
        getSupportActionBar().setTitle("User Details");

		Tracker t = ((AppController) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("User Details Screen");
		t.send(new HitBuilders.AppViewBuilder().build());
		Bundle extras=this.getIntent().getExtras();
		if(extras != null){
	    	KEY_GET_USER_ID = extras.getString("KEY_GET_USER_ID", "");
		}
		Log.e(TAG, "KEY_GET_USER_ID=" +KEY_GET_USER_ID);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
		
	}//endOncrea

	@Override
	protected void onResume() {
		super.onResume();
		
//		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
//		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
//		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
//
//		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
//		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
//		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
//		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
//		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
//		if(Utilities.D)Log.v(TAG,"getMobile_num="+getMobile_num);
//		if(Utilities.D)Log.v(TAG,"getEmailID="+getEmailID);
//		if(Utilities.D)Log.v(TAG,"getUserType="+getUserType);
		if(Utilities.D)Log.v(TAG,"getUserID="+getUserDataID);
//		if(Utilities.D)Log.v(TAG,"getCity="+getCity);
//		if(Utilities.D)Log.v(TAG,"getUserName="+getUserName);
//		if(Utilities.D)Log.v(TAG,"getProfileImage="+getProfileImage);

	}//ENDoncrea
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putInt("currentColor", currentColor);
		outState.putBoolean("category_frag_added", true);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			// currentColor = savedInstanceState.getInt("currentColor");
			// changeColor(currentColor);
			if(Utilities.D)Log.v(TAG,"page_size_is_saved");
		}
	}

	@Override
	protected void onStop() {
		super.onStop(); 
		GoogleAnalytics.getInstance(UserInfoFActivity.this).reportActivityStop(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(UserInfoFActivity.this).reportActivityStart(this);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "User Profile", "User Listings" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = new Fragment();
			switch (position) {
			case 0:
				return f = new UserInfoFragment();
			case 1:
				return f = new UserMLFragment();
			default:
				break;
			}
			return f;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
        	ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        	List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        	if(taskList.get(0).numActivities > 1){
    			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        	} else {
        		Intent i = new Intent(UserInfoFActivity.this, MainActivity.class);
        		startActivity(i);finish();
        	}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}