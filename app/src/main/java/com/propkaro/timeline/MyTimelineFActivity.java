package com.propkaro.timeline;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.propkaro.AppController;
import com.propkaro.AppController.TrackerName;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;
import com.propkaro.R;
public class MyTimelineFActivity extends AppCompatActivity {
	private static final String TAG = MyTimelineFActivity.class.getSimpleName();
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	RelativeLayout rel;
	SearchView search;
	Context ctx;
	SharedPreferences mPrefs;
	String getUserDataID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getActionBar().setCustomView(R.layout.actionbbbar_timeline_activity);
		setContentView(R.layout.user_detail_tabs_pager_timeline);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//        activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        getSupportActionBar().setTitle("My Timeline");

		Tracker t = ((AppController) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Timeline Screen");
		t.send(new HitBuilders.AppViewBuilder().build());
		ctx = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
				
		//----------------------------------------new design------------------------------
//		ImageView ic_my_message = (ImageView)findViewById(R.id.ic_my_message);
//		ic_my_message.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG, "onOptionMenu++++++++ clicks.........");
//				if(Utilities.isInternetOn(ctx)){
//					Intent i = new Intent(MyTimelineFActivity.this, MessageFActivity.class);
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				}
//			}
//		});
//		
//		ImageView ic_friend_request = (ImageView) findViewById(R.id.ic_friend_request);
//		ic_friend_request.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG, "onOptionMenu++++++++ clicks.........");
//				if(Utilities.isInternetOn(ctx)){
//					Intent i = new Intent(MyTimelineFActivity.this, NotificationFActivity.class);
//					i.putExtra("KEY_GET_TAB", "FRIEND_REQUEST");
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				}
//			}
//		});
//
//		ImageView ic_notification = (ImageView) findViewById(R.id.ic_notification);
//		ic_notification.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG, "onOptionMenu++++++++ clicks.........");
//				if(Utilities.isInternetOn(ctx)){
//					Intent i = new Intent(MyTimelineFActivity.this, NotificationFActivity.class);
//					i.putExtra("KEY_GET_TAB", "NOTIFICATION");
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				}
//			}
//		});
		//--------------------------------------end--new design------------------------------

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
	}

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
	Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, 10000); //
    }
    public void stoptimertask(View v) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());
                        
                        new AllNotificationTask().execute(getUserDataID);
                        
                        if(Utilities.D)Log.v(TAG,"Timer:"+strDate);
                    }
                });
            }
        };
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	if(Utilities.D)Log.v(TAG,"onPause()");
    	stoptimertask(v);
    	isTimerStart = false;
    };
    boolean isTimerStart = false;
	View v = null;

	@Override
	protected void onResume() {
		super.onResume();
    	if(Utilities.D)Log.v(TAG,"onResume()");
    	isTimerStart = true;
//    	startTimer();
	}
	@Override
	protected void onStop() {
		super.onStop(); 
		GoogleAnalytics.getInstance(MyTimelineFActivity.this).reportActivityStop(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(MyTimelineFActivity.this).reportActivityStart(this);
	}
	
	class AllNotificationTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		JSONArray api_messages_user, api_connections_user, api_notifications_user; 
		public String api_message = "", api_data = "";
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			stoptimertask(v);
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				String ALL_NOTIFICATION_API = 
						"{" 
						+ "\"id\":"
						+ "\"" + f_url[0] + "\""
						+ "}";

				String UrlBase = Host.AllNotificationsUrl + "";
				String jsonString = Utilities.sendData(UrlBase, ALL_NOTIFICATION_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					JSONObject data = (JSONObject) reader.get("data");
					api_data = data + "";
					api_messages_user = (JSONArray)data.get("messages");
					api_connections_user = (JSONArray)data.get("connections");
					api_notifications_user = (JSONArray)data.get("notifications");
					
					if(Utilities.D)Log.v(TAG,"api_messages_user=" + api_messages_user);
					if(Utilities.D)Log.v(TAG,"api_connections_user=" + api_connections_user);
					if(Utilities.D)Log.v(TAG,"api_notifications_user=" + api_notifications_user);
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
			
			if (api_code == 200) {
			}
			if(isTimerStart)
				startTimer();
		}
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "TimeLine" };

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
			// if(position==0)
			// f = new TCategoryFeaturedFragment();
			Fragment f = new Fragment();
			switch (position) {
			case 0:
				return f = new MyTimelineFragment();
			default:
				break;
			}
			return f;
		}
	}
	
	public void onPopupButtonClick(View button) {
		PopupMenu popup = new PopupMenu(this, button);
		popup.getMenuInflater().inflate(R.menu.popup_logout, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				Toast.makeText(getApplicationContext(), "Signing out..", 1).show();
				return true;
			}
		});
		popup.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}