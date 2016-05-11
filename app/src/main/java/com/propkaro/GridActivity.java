package com.propkaro;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.AppController.TrackerName;
import com.propkaro.chat.ComingSoonFragment;
import com.propkaro.facilitation.FacilitationCenterFActivity;
import com.propkaro.filters.FilterFActivity;
import com.propkaro.mandate.MandateFActivity;
import com.propkaro.mylisting.MyListingFActivity;
import com.propkaro.notifications.NotifyGCMFragment;
import com.propkaro.post.PostPropertyActivity;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.propertycenter.AvailPCRecyclerView;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.shortlist.ShortlistFragment;
import com.propkaro.timeline.MyTimelineFActivity;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.CsvContactSetter;
import com.propkaro.util.CsvFileWriter;
import com.propkaro.util.GPSTracker;
import com.propkaro.util.Host;
import com.propkaro.util.PermissionsUtilities;
import com.propkaro.util.TinyDB;
import com.propkaro.util.UploadUtils;
import com.propkaro.util.UtilMaterial;
import com.propkaro.util.Utilities;


public class GridActivity extends AppCompatActivity 
implements NavigationView.OnNavigationItemSelectedListener, OnClickListener, SearchView.OnQueryTextListener {
	private String TAG = GridActivity.class.getSimpleName();
    private static final int TOTAL_OFF_SCREENS = 3;
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private CoordinatorLayout mCoordinator;
//    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public static FloatingActionButton mFab;
    private Toolbar mToolbar;
    public static ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private Context ctx; TinyDB db;
    private AppCompatActivity activity;
	private SharedPreferences mPrefs;
	public static String getMobile_num = "", getEmailID = "", getUserType = "", getUserDataID = "", gcmId = "", 
			getUserName = "", getFName = "", getLName = "", getCity = "", getProfileImage = "", getSearchQuery = "";
	private List<String> listMsgConn = new ArrayList<String>();
	private boolean hasContacts = false;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;
    private boolean mUserSawDrawer = false;
	private TextView tv_profile_drawer, tv_name_d, tv_userType_d, tv_userExpt_d;
	private RelativeLayout rl_profile_image_d;
	private ImageView iv_profile_d;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private RelativeLayout rl_plus;
	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	public ArrayList<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	private File f = null;
	private CircularProgressView progressView;
	private RelativeLayout rel_searchview;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private AvailPCRecyclerView adapter;
//	private boolean loading = true;
	int pastVisiblesItems, visibleItemCount;
	private SearchView mSearchView;
	private TextView tv_nodata;
	String api_availability = 
			"{\"by\":{" + 
//			"\"property_listing_parent\":[\"" 
//					+ "Availability" 
//			+ "\"]" +
			"\"keywords\":\"" + "" + "\"" + 
//			"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
//			"\"user_id\":\"" + "20" + "\"" + 
					"}}";//SearchUrl
    private final List<String> mFragmentTitleList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ctx = this; activity = this;
		db = new TinyDB(ctx);
        setContentView(R.layout.activity_grid);
		Tracker t = ((AppController) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Screen");
		t.send(new HitBuilders.AppViewBuilder().build());
		
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
		hasContacts = mPrefs.getBoolean("PREF_KEY_HAS_CONTACTS", false);
		gcmId = mPrefs.getString("PREF_KEY_GCM_ID", "");
		
		if(Utilities.D)Log.i(TAG,"#getUserDataID: " + getUserDataID);
		if(Utilities.D)Log.i(TAG,"getEmailID: " + getEmailID);
		if(Utilities.D)Log.i(TAG,"getMobile_num: " + getMobile_num);
		if(Utilities.D)Log.i(TAG,"hasContacts: " + hasContacts);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//        }
//        mToolbar.setNavigationIcon(R.drawable.back);
//        mToolbar.setLogo(R.drawable.ic_launcher);
//        mToolbar.setTitle("Title");
//        mToolbar.setSubtitle("Subtitle");
        getSupportActionBar().setTitle("Propkaro");
        UtilMaterial.setToolbarTitle(mToolbar);
//-----------Drawer settings-----------
        mDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_drawer, mDrawer, false);
        mDrawer.addHeaderView(headerView);
		Typeface RobotoLight = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf"); 
		Typeface RobotoRegular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf"); 
        
		iv_profile_d = (ImageView)headerView.findViewById(R.id.iv_profile_d);
        rl_profile_image_d = (RelativeLayout) headerView.findViewById(R.id.rl_profile_image_d);
    	tv_profile_drawer = (TextView) headerView.findViewById(R.id.tv_profile_d);
		tv_name_d = (TextView)headerView.findViewById(R.id.tv_name_d);
		tv_userType_d = (TextView)headerView.findViewById(R.id.tv_userType_d);
		tv_userExpt_d = (TextView)headerView.findViewById(R.id.tv_userExpt_d);
		
		tv_name_d.setTypeface(RobotoRegular);
		tv_userType_d.setTypeface(RobotoLight);
		tv_userExpt_d.setTypeface(RobotoLight);
		
		iv_profile_d.setOnClickListener(this);
		rl_profile_image_d.setOnClickListener(this);
		tv_profile_drawer.setOnClickListener(this);
		tv_name_d.setOnClickListener(this);
		tv_userType_d.setOnClickListener(this);
		
    	tv_profile_drawer.setVisibility(View.GONE);
    	
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
//        mSelectedId = savedInstanceState == null ? R.id.menu_profile : savedInstanceState.getInt(SELECTED_ITEM_ID);
//        navigate(mSelectedId);
      //-----------Drawer settings ends-----------
        rl_plus = (RelativeLayout)findViewById(R.id.rl_plus);
        rl_plus.setOnClickListener(this);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setOffscreenPageLimit(TOTAL_OFF_SCREENS);
//      mPager.setAdapter(mAdapter);
        setupViewPager(mPager);
        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        
        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mPager);
        
        setupTabIcons();
        
        getSupportActionBar().setTitle("Property Center");
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Log.e(TAG, "++++++++onPageSelected");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
	            getSupportActionBar().setTitle(mFragmentTitleList.get(mPager.getCurrentItem()));
	            switch(mPager.getCurrentItem()){
	            case 0:
	            	mFab.show();
	            	break;
	            case 1:
	            	mFab.hide();
	            	break;
	            case 2:
	            	mFab.hide();
	            	break;
	            case 3:
	            	mFab.hide();
	            	break;
	            }
			}
		});
        
//		if(db.getBoolean("PREF_KEY_bool_hasFilter", false)){
	        GridActivity.mFab.setRippleColor(Color.RED);
	        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.Orange));
//		} else{
//	        GridActivity.mFab.setRippleColor(Color.GREEN);
//	        GridActivity.mFab.setBackgroundTintList(getResources().getColorStateList(R.color.YellowGreen));
//		}
        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent i = new Intent(GridActivity.this, FilterFActivity.class);
            	startActivity(i);
            }
        });

        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
//        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.title_activity_grid));
//----Search Query--------
		progressView = (CircularProgressView) findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);
		
		rel_searchview = (RelativeLayout)findViewById(R.id.rel_searchview);
		rel_searchview.setVisibility(View.GONE);

		mRecyclerView = (RecyclerView) findViewById(R.id.list_avail_pc);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
	    adapter = new AvailPCRecyclerView(this, AvailList);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
		    @Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		        if(dy > 0) //check for scroll down
		        {
		            visibleItemCount = mLayoutManager.getChildCount();
		            totalItemCount = mLayoutManager.getItemCount();
		            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

		            if (isLoadingMore && current_page <= total_pages) 
		            {
		                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) 
		                {
		                	isLoadingMore = false;
							if(Utilities.isInternetOn(ctx))
								new Availability_PC_Task().execute(api_availability);
		                }
		            }
		        }
		    }
		});
		
	    tv_nodata = (TextView)findViewById(R.id.tv_nodata);
		tv_nodata.setVisibility(View.GONE);
		tv_nodata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "@LoadingButtonClickData=current_page=" + current_page);
				new Availability_PC_Task().execute(api_availability);
			}
		});

		mSearchView = (SearchView)findViewById(R.id.search_view);
		mSearchView.setSubmitButtonEnabled(false);
		mSearchView.setIconifiedByDefault(true);
		mSearchView.onActionViewCollapsed();
 		mSearchView.setQueryHint("Search here..");
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        		
	    mSearchView.setOnQueryTextListener(this);
	    mSearchView.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
			
			@Override
			public void onViewDetachedFromWindow(View v) {
				if(Utilities.D)Log.v(TAG,"---onViewDetachedFromWindow()");
				rel_searchview.setVisibility(View.GONE);
			}
			
			@Override
			public void onViewAttachedToWindow(View v) {
				if(Utilities.D)Log.v(TAG,"--onViewAttachedToWindow()");
				rel_searchview.setVisibility(View.GONE);
			}
		});
	    mSearchView.setOnCloseListener(new OnCloseListener() {
			
			@Override
			public boolean onClose() {
				if(Utilities.D)Log.v(TAG,"--setOnCloseListener()");
				rel_searchview.setVisibility(View.GONE);
				AvailList.clear();
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
				return false;
			}
		});
    }//endOncrea
    
	@Override
	 public boolean onQueryTextChange(String newText) {
			getSearchQuery = newText;
     		db.putString("PREF_SEARCH_QUERY", getSearchQuery);
			if(Utilities.D)Log.v(TAG,"searching-=" + getSearchQuery);
			
	     	if (TextUtils.isEmpty(newText)) {
	     		rel_searchview.setVisibility(View.GONE);
				AvailList.clear();
				current_page = 1;
//	     		isLoadingMore = false;
//				if(Utilities.D)Log.v(TAG,"emptyText-=" + newText);
//				api_availability = "{\"by\":{" + 
////						"\"property_listing_parent\":[\"" + "Availability" + "\"]" +
//						"\"keywords\":\"" + newText + "\"" +
////						"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
////						"\"user_id\":\"" + "20" + "\"" + 
//								"}}";
//				if(Utilities.isInternetOn(ctx)){
//					new Availability_PC_Task().execute(api_availability);
//				}
	        } else {
	     		isLoadingMore = false;
				if(Utilities.D)Log.v(TAG,"query-=" + newText);
//	            mRecyclerView.setFilterText(newText.toString());
//	    		adapter.getFilter().filter(newText.toString());

				api_availability = "{\"by\":{" + 
//						"\"property_listing_parent\":[\"" + "Availability" + "\"]" +
						"\"keywords\":\"" + newText + "\"" +
//						"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
//						"\"user_id\":\"" + "20" + "\"" + 
								"}}";
				if(newText.length() > 2)
					if(Utilities.isInternetOn(ctx)){
						AvailList.clear();
						current_page = 1;
						new Availability_PC_Task().execute(api_availability);
					}
					else
						if(Utilities.D)Log.v(TAG,"No internet connection !");
				else
					if(Utilities.D)Log.v(TAG,"Search text should be at least 3 characters !");
	        }
	        return true;
	    }

		@Override
	    public boolean onQueryTextSubmit(String query) {

    		isLoadingMore = false;
			if(Utilities.D)Log.v(TAG,"query-=" + query);
//			adapter.getFilter().filter(query.toString());

			api_availability = "{\"by\":{" + 
//					"\"property_listing_parent\":[\"" + "Availability" + "\"]" +
					"\"keywords\":\"" + query + "\"" +
//					"\"property_type_name\":\"" + "Residential Apartment" + "\"" +
//					"\"user_id\":\"" + "20" + "\"" + 
							"}}";
			if(query.length() > 2)
				if(Utilities.isInternetOn(ctx)){
					AvailList.clear();
					current_page = 1;
					new Availability_PC_Task().execute(api_availability);
				}
				else
					if(Utilities.D)Log.v(TAG,"No internet connection !");
			else
				if(Utilities.D)Log.v(TAG,"Search text should be at least 3 characters !");
	        return false;
	    }
		
		class Availability_PC_Task extends AsyncTask<String, String, String> {
			int api_code = 0, api_total = 0;
			public String api_message = "", api_data;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				isLoadingMore = false;
				rel_searchview.setVisibility(View.VISIBLE);
				progressView.setVisibility(View.VISIBLE);
				tv_nodata.setVisibility(View.GONE);
			}

			@Override
			protected String doInBackground(String... f_url) {

				try {
					String UrlBase = Host.SearchUrl + "page/" + current_page + "/";
					String jsonString = Utilities.sendData(UrlBase, f_url[0]);
					JSONObject reader = new JSONObject(jsonString);

					JSONObject meta = (JSONObject) reader.get("meta");
					api_code = meta.getInt("code");
					api_message = meta.getString("message");

					if (api_code == 200) {
						api_total = meta.getInt("total");
						total_pages = api_total;
						if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);
						
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
				if(Utilities.D)Log.v(TAG,"file_url="+file_url);
				
				progressView.setVisibility(View.GONE);
				isLoadingMore = true;

				if (api_code == 200) {
					if(AvailList.isEmpty()){
						tv_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
					mPrevTotalItemCount = totalItemCount;
					current_page = current_page + 1;
					adapter.notifyDataSetChanged();
				} else if(api_code == 400){
					isLoadingMore = false;
					if(AvailList.isEmpty()){
						tv_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("No listings found !");
					}
//					Toast.makeText(getActivity(), api_message, Toast.LENGTH_SHORT).show();
				} else {
					if(AvailList.isEmpty()){
						tv_nodata.setVisibility(View.VISIBLE);
						tv_nodata.setText("Something went wrong, please try again !");
					}
//					Toast.makeText(getActivity(), "Something went wrong, please try again !", Toast.LENGTH_SHORT).show();
				}
			}
		}

//--end search-------
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String 
			rMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE),
					rTitle = intent.getExtras().getString(CommonUtilities.EXTRA_TITLE);
			Utilities.WakeLocker.acquire(getApplicationContext());
			if(Utilities.D)Log.v(TAG,"rMessage=" + rMessage + "\n");
			if(Utilities.D)Log.v(TAG,"rTitle=" + rTitle + "\n");

//			lblMessage.append(newMessage + "\n");			
//			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			Utilities.WakeLocker.release();
		}
	};

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
		GoogleAnalytics.getInstance(GridActivity.this).reportActivityStop(this);
 		db.putString("PREF_SEARCH_QUERY", "");
    }

    @Override
    public void onStart() {
      super.onStart();
		GoogleAnalytics.getInstance(GridActivity.this).reportActivityStart(this);
    }
    
	@Override
    protected void onDestroy() {
		super.onDestroy();
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			if(Utilities.D)Log.e(TAG, "unRegister Receiver Error> " + e.getMessage());
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
                        
//                		if(Utilities.isInternetOn(ctx))
//                        new AllNotificationTask().execute(getUserDataID);
                        
                        if(Utilities.D)Log.v(TAG,"Time:" + strDate);
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
    	startTimer();
    	
		if(db.getBoolean("shouldReload", false)){
			mPager.setCurrentItem(0);
		}
		if(mPrefs.getString("PREF_KEY_LATITUDE", "").length() == 0){
			double getLat = 0.0, getLng =0.0;
			GPSTracker gps = new GPSTracker(GridActivity.this);
	        if(gps.canGetLocation()){
	        	SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_LATITUDE", gps.getLatitude() + "");
				editor.putString("PREF_KEY_LONGITUDE", gps.getLongitude() + "");
				editor.commit();
	        }else{
//	        	gps.showSettingsAlert();
	        }
			Log.e(TAG, "getLat: " + getLat + "/getLng: " + getLng );
		}
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");

		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
		
		if(Utilities.D)Log.v(TAG,"getMobile_num="+getMobile_num);
		if(Utilities.D)Log.v(TAG,"getEmailID="+getEmailID);
		if(Utilities.D)Log.v(TAG,"getUserType="+getUserType);
		if(Utilities.D)Log.v(TAG,"getUserID="+getUserDataID);
		if(Utilities.D)Log.v(TAG,"getCity="+getCity);
		if(Utilities.D)Log.v(TAG,"getUserName="+getUserName);
		if(Utilities.D)Log.v(TAG,"getProfileImage="+getProfileImage);

		tv_name_d.setText(Utilities.makeFirstLetterCaps(getUserName));
		tv_userType_d.setText(Utilities.makeFirstLetterCaps(getUserType));
		ImageLoader.getInstance().displayImage(getProfileImage, iv_profile_d, AvailPCFragment.animateFirstListener);

		tv_profile_drawer.setVisibility(View.GONE);
		if(getProfileImage.contains("default")){
    		if(getUserName.length() > 0){
        		tv_profile_drawer.setVisibility(View.VISIBLE);
        		tv_profile_drawer.getBackground().setColorFilter(Utilities.getRandomColor(ctx, getUserName), PorterDuff.Mode.SRC_IN);
        		tv_profile_drawer.setText(String.valueOf(getUserName.charAt(0)).toUpperCase());
    		} else {
    			tv_profile_drawer.setVisibility(View.GONE);
    		}
    	}
		
//		TextView tv_favourite = (TextView) slide_me.findViewById(R.id.tv_favourite);
//		SetPreference setPreference = new SetPreference();
//		int fav = setPreference.sizeFavorites(activity);
//		if(fav < 1)
//			tv_favourite.setText("Favourite List");
//		else
//			tv_favourite.setText("Favourite List (" + fav + ")");
		
		if(Utilities.isInternetOn(ctx))
			new UserDetailsTask().execute(getUserDataID);
	}//endOn---
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popup_logout, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AvailPCFragment(), "Property Center");
        adapter.addFrag(new ShortlistFragment(), "Shortlisted");
        adapter.addFrag(new ComingSoonFragment(), "Chat");
        adapter.addFrag(new NotifyGCMFragment(), "Notifications");
        viewPager.setAdapter(adapter);
    }
    
    private void setupTabIcons() {
    	 
        ImageView tab1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.design_layout_tab_icon, null);
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_newsfeed, 0, 0);
        tab1.setImageResource(R.mipmap.ic_tab_feed);
        mTabLayout.getTabAt(0).setCustomView(tab1);
 
        ImageView tab2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.design_layout_tab_icon, null);
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_shortlist, 0, 0);
        tab2.setImageResource(R.mipmap.ic_tab_pin);
        mTabLayout.getTabAt(1).setCustomView(tab2);
 
        ImageView tab3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.design_layout_tab_icon, null);
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_chat, 0, 0);
        tab3.setImageResource(R.mipmap.ic_tab_chat);
        mTabLayout.getTabAt(2).setCustomView(tab3);

        ImageView tab4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.design_layout_tab_icon, null);
//        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_notification, 0, 0);
        tab4.setImageResource(R.mipmap.ic_tab_notification);
        mTabLayout.getTabAt(3).setCustomView(tab4);
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
 
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
 
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
 
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
 
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            getSupportActionBar().setTitle(title);
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
//-------start drawer settings----
    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        mUserSawDrawer = true;
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {
    	
    	switch(mSelectedId){
    	case R.id.menu_prop:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			break;
			
    	case R.id.menu_timeline:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			if(Utilities.isInternetOn(ctx)){
				Intent i = new Intent(GridActivity.this, MyTimelineFActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
			
    	case R.id.menu_profile:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks........menu_profile.");
			
//			if(Utilities.isInternetOn(ctx)){
//				Fragment mFragment = fm.findFragmentByTag("menu_profile");
//		        if (mFragment == null) 
//					Utilities.replaceCompatFragment(activity, new Edit_ProfileFragment(), R.id.frameContainer, "menu_profile", true, true);
//			}

			if(Utilities.isInternetOn(ctx)){
				Intent i = new Intent(GridActivity.this, EditProfileActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
			
    	case R.id.menu_myList:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");

			if(Utilities.isInternetOn(ctx)){
				Intent i = new Intent(GridActivity.this, MyListingFActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
			
    	case R.id.menu_favorite:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			break;

    	case R.id.menu_notification:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			mPager.setCurrentItem(3);
			
			break;

    	case R.id.menu_post_listing:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ PostListing.........");
			if(Utilities.isInternetOn(ctx)){
				Intent i = new Intent(GridActivity.this, PostPropertyActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			
			break;

    	case R.id.menu_mandated:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			if(Utilities.isInternetOn(ctx)){
				Intent facilitationIntent = new Intent(GridActivity.this, MandateFActivity.class);
				startActivity(facilitationIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			
			break;

    	case R.id.menu_facilitation:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			if(Utilities.isInternetOn(ctx)){
				Intent facilitationIntent = new Intent(GridActivity.this, FacilitationCenterFActivity.class);
				startActivity(facilitationIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;

    	case R.id.menu_videos:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
//			Intent videoIntent = new Intent(GridActivity.this, VideoListActivity.class);
//			startActivity(videoIntent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;

    	case R.id.menu_rate:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.d(TAG, "onOptionMenu++++++++ clicks.........");
			try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.propkaro")));
			} catch (Exception e) {
				Utilities.Snack(mCoordinator, "Playstore not found in your phone");
			}
			break;

    	case R.id.menu_support:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.v(TAG,"menu_helpAndSupport-----------");
			Intent iii = new Intent(GridActivity.this, Help_SupprtActivity.class);
			startActivity(iii);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;


    	case R.id.menu_logout:
            mDrawerLayout.closeDrawer(GravityCompat.START);
			if(Utilities.D)Log.v(TAG,"menu_logout-----------");
			Utilities.Snack(mCoordinator, "Signing out..");
			
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("PREF_KEY_ACTIVATE", "");
			editor.putString("PREF_KEY_MOB_NUM", "");
			editor.putString("PREF_KEY_EMAIL_ID", "");
			editor.putString("PREF_KEY_CITY_NAME", "");
//			editor.putString("PREF_KEY_USER_TYPE", "");
			editor.putBoolean("PREF_KEY_HAS_CONTACTS", false);
			editor.commit();
			
			Intent logoutIntent = new Intent(GridActivity.this, MainActivity.class);
    		startActivity(logoutIntent);finish();
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;

		default:
			break;
    	}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
        	if(mPager.getCurrentItem() != 0)
        		mPager.setCurrentItem(0);
        	else
        		super.onBackPressed();
        }
    }
//ends drawer settings--------------------------
//----------------------starts all services--apis----------------------    
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
//				tv_notification.setText(listMsgConn.get(2));
//
//				TextView tv_my_listings = (TextView) slide_me.findViewById(R.id.tv_my_listings);
//				TextView tv_my_connections = (TextView) slide_me.findViewById(R.id.tv_my_connections);
//				tv_my_connections.setText("My Connections " + "( " + listMsgConn.get(0) + " )");
//				tv_my_listings.setText("My Listings " + "( " + listMsgConn.get(2) + " )");
			} else 
				if(Utilities.D)Log.v(TAG,"Something went wrong !");
			
			//------------------------getAllContacts		
			hasContacts = mPrefs.getBoolean("PREF_KEY_HAS_CONTACTS", false);
			if(Utilities.D)Log.v(TAG,"@hasContacts=" + hasContacts);
			if(hasContacts == false){
				if(Utilities.isInternetOn(ctx))
					if(PermissionsUtilities.getPermissionReadContact(activity))
						if(PermissionsUtilities.getPermissionWriteDisk(activity))
							new UploadCsvTask().execute(f);
			 }else 
				 if(Utilities.D)Log.v(TAG,"else case: hasContacts=" + hasContacts);
			 //--------------------end getallContacts----check version------------------
				if (Utilities.NetworkCheck(ctx))
					new DATA_UPDATED_TASK().execute("");
			 //--------------------end gettingVersioningInfo--------------------
		}
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
////				TextView tv_All_notify = (TextView) slide_me.findViewById(R.id.tv_All_notify);
//				TextView tv_myNotifications = (TextView) findViewById(R.id.tv_myNotifications);
//				TextView tv_myFriendRequest = (TextView) findViewById(R.id.tv_myFriendRequest);
//				TextView tv_my_messages = (TextView) findViewById(R.id.tv_my_messages);
//
////				tv_All_notify.setVisibility(View.GONE);
//				tv_myNotifications.setVisibility(View.GONE);
//				tv_myFriendRequest.setVisibility(View.GONE);
//				tv_my_messages.setVisibility(View.GONE);
//
////				int temp_notification_count = 0;
////				temp_notification_count = (api_notifications_user.length()
////						+ api_connections_user.length() + api_messages_user.length());
////				if(temp_notification_count > 0){
////					tv_All_notify.setVisibility(View.VISIBLE);
////					tv_All_notify.setText(temp_notification_count + "");
////				}
//
//				if(api_notifications_user.length() > 0){
//					tv_myNotifications.setVisibility(View.VISIBLE);
//				}
//				tv_myNotifications.setText(api_notifications_user.length() + "");
//
//				if(api_connections_user.length() > 0){
//					tv_myFriendRequest.setVisibility(View.VISIBLE);
//				}
//				tv_myFriendRequest.setText(api_connections_user.length() + "");
//
//				if(api_messages_user.length() > 0){
//					tv_my_messages.setVisibility(View.VISIBLE);
//				}
//				tv_my_messages.setText(api_messages_user.length() + "");
//
//				tv_msg136.setText(api_messages_user.length() + "");
//
////				BadgeView badge = new BadgeView(ctx, tv_myFriendRequest);
////				badge.setText("1");
////				badge.show();
			}
			if(isTimerStart)
				startTimer();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	    if(Utilities.D)Log.v(TAG, "Check Result----------hari--------------");
	    switch (requestCode) {
	        case Host.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
	            {
	            Map<String, Integer> perms = new HashMap<String, Integer>();
	            // Initial
	            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
	            perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
	            
	            // Fill with results
	            for (int i = 0; i < permissions.length; i++)
	                perms.put(permissions[i], grantResults[i]);
	            
	            // Check for ACCESS_FINE_LOCATION
	            if ( perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
	                    && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED 
	                    ) {
	                // All Permissions Granted
	            	if(Utilities.isInternetOn(ctx))
	            		new UploadCsvTask().execute(f);
	            } else {
	                // Permission Denied
	                Toast.makeText(GridActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
	            	}
	            }
	            break;
	        default:
	            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	    }
	}
	
	List<CsvContactSetter> contactList = new ArrayList<CsvContactSetter>();
	class UploadCsvTask extends AsyncTask<File, Void, Void> {
		int api_code;
		String api_message, api_image;
		File file;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if(Utilities.D)Log.v(TAG,"onPreExecute(): --");
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
//			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
			if(Utilities.D)Log.v(TAG,"Request cancelled !");
		}
		protected Void doInBackground(File... files) {
			try {
	    		contactList = Utilities.readContacts(ctx);
				CsvFileWriter.exportEmailInCSV(ctx, contactList);
				
	    	    File sdRoot = Environment.getExternalStorageDirectory();
	    	    file = new File(sdRoot, "/.CSV/androidCsvFile.csv");	
			} catch (IOException e) { e.printStackTrace(); }
			
//			file = files[0];
			if (file == null)
				return null;
			
			try {
				GridActivity.this.setProgress(0);
				if(Utilities.D)Log.v(TAG,"doInBackground=");
				String urlBase = Host.UploadCsvUrl;
				String params = "{\"id\":\"" + getUserDataID + "\"}";
				String jsonData = "{\"table\":\"registration\", \"params\":" + params + "}";

				Bitmap bm = null;
				String jsonString;
				jsonString = UploadUtils.MultipartFileUploader(file, bm, urlBase, jsonData, getUserDataID, "", true, false);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_image = reader.getString("image");
				} else {
				}
			} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			
			if(Utilities.D)Log.v(TAG,"onProgressUpdate=" + values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
//			dialog.dismiss();
			if(Utilities.D)Log.v(TAG,"onPostExecute(): CSV Upload -> " + api_message);

			if(api_code == 200){
//				if(Utilities.D)Log.v(TAG,"getProfileImage: " + Host.MainUrl 
//						+"/csv-secure-files-5348694123487865413468453187864/"
//						+ api_image);
				
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putBoolean("PREF_KEY_HAS_CONTACTS", true);
				editor.commit();

				boolean deleted = file.delete();
//				boolean deleted = Utilities.deleteDirectory(file);
				if(Utilities.D)Log.v(TAG,"file.delete(): " + deleted);
			}
		}
	}//endUploadCsvTask
	
	class UserDetailsTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data = "", api_id = "", 
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
			
			if(api_code == 200){
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_MOB_NUM", api_phone_no);
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
				
				getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
				getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
				getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
				getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
				
				getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
				getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
				getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
				getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
				getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");

				tv_name_d.setText(getUserName);
				tv_userType_d.setText(Utilities.makeFirstLetterCaps(getUserType));

				ImageLoader.getInstance().displayImage(getProfileImage, iv_profile_d, AvailPCFragment.animateFirstListener);

        		tv_profile_drawer.setVisibility(View.GONE);
				if(getProfileImage.contains("default")){
		    		if(getUserName.length() > 0){
		        		tv_profile_drawer.setVisibility(View.VISIBLE);
		        		tv_profile_drawer.getBackground().setColorFilter(Utilities.getRandomColor(ctx, getUserName), PorterDuff.Mode.SRC_IN);
		        		tv_profile_drawer.setText(String.valueOf(getUserName.charAt(0)).toUpperCase());
		    		} else {
		    			tv_profile_drawer.setVisibility(View.GONE);
		    		}
		    	}
				
				if(Utilities.isInternetOn(ctx))
					new MsgConnTask().execute(getUserDataID);
			}
		}
	}//endUserDetailsTask

	public class DATA_UPDATED_TASK extends AsyncTask<String, String, JSONObject> {

		int api_code = 0;
		String api_message = "";
		int VERSION_UPDATES = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected JSONObject doInBackground(String... F_URL) {
			
				try {
					String str_send_json = "{\"by\":{\"setting_name\":\""
							+ "DATA_UPDATED"
							+ "\"}" +
							",\"table\":\"settings\",\"multiple\":\"All\"}";
					String UrlBase = Host.GetUrl;
					String jsonString = Utilities.sendData(UrlBase, str_send_json);
					
					if (jsonString.length() != 0) {
						JSONObject reader = new JSONObject(jsonString);

						JSONObject meta = (JSONObject) reader.get("meta");
						api_code = meta.getInt("code");
						api_message = meta.getString("message");

						JSONArray data = (JSONArray) reader.getJSONArray("data");
						JSONObject o = data.getJSONObject(0);
						VERSION_UPDATES = Integer.parseInt(o.getString("setting_value"));
					}
				} catch (Exception e) {
					e.printStackTrace();
			}
			JSONObject json = null;
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if(api_code == 200){
					PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					int currentVersion = pInfo.versionCode;

					Log.v(TAG, "------------VERSION_UPDATES code=" + VERSION_UPDATES);
					Log.v(TAG, "------------versionUpdated code=" + currentVersion);

					if(currentVersion >= VERSION_UPDATES ){	
						Log.v(TAG, "------------app version not need to update(correc version condition)=" + currentVersion);
					} else {
						if(Utilities.D)Log.v(TAG,"------------app version need to update(show dialog to update)=" + currentVersion);
						showAlertDialog(GridActivity.this, "Version update required", 
								"A new update of Propkaro is available. Please update to enhance your experience.", false);
					}
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(message);

		if (status != null)
			alertDialog.setIcon((status) ? R.mipmap.ic_success : R.mipmap.ic_fail);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				if(Utilities.D)Log.v(TAG, "------------app version (Dialog click ok button)=" );
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.propkaro")));
				} catch (Exception e) {
					Utilities.Snack(mCoordinator, "Playstore not found in your phone");
				}
			}
		});
		alertDialog.show();
	}
    
//stating on click------------------------------    
	@Override
	public void onClick(View v) {
		if(v == tv_profile_drawer || v == iv_profile_d || v == rl_profile_image_d){
            mDrawerLayout.closeDrawer(GravityCompat.START);
			Intent i = new Intent(ctx, UserInfoFActivity.class);
			i.putExtra("KEY_GET_USER_ID", getUserDataID);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		} else if(v == tv_name_d){
		} else if(v == tv_userType_d){
		} else if(v == tv_userExpt_d){
		} else if(v == rl_plus){
			if(Utilities.isInternetOn(ctx)){
				Intent i = new Intent(GridActivity.this, PostPropertyActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		}
	}
}