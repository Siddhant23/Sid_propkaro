package com.propkaro.mandate;

import java.util.ArrayList;



import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class MandateFActivity extends AppCompatActivity {
	private static final String TAG = MandateFActivity.class.getSimpleName();
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public ArrayList<AvailPCSetter> AvailList = new ArrayList<AvailPCSetter>();
	static MandateRecyclerView adapter;
	Button BTN_CITY;
	EditText mSearchEdiText;
	ArrayList<String> api_city = new ArrayList<String>();
	String getCity = "", api_allCities = "";
	double api_latitude = 0, api_longitude = 0;
	TinyDB db;
	ListView mListView;
	String temp_city = "0";
	ArrayAdapter<String> adapter_cities;
	AppCompatActivity fActivity;
	private SharedPreferences mPrefs;
	static boolean isShowReview = true;
	private Context ctx;
	ViewPager mPager;
	private final Handler handler = new Handler();
	LinearLayoutManager mLayoutManager;
	private final Handler mHandler = new Handler();
	private boolean mShowingBack = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		fActivity = this;

		ctx = this;
////		db = new TinyDB(ctx);
//		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//		api_city = db.getListString("PREF_KEY_CITIES_ALL");
//

		setContentView(R.layout.mandate);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getSupportActionBar().setTitle("Mandated Properties");

//		getCity = mPrefs.getString("PREF_KEY_City_type", "");
//		temp_city = getCity;
		
//		Utilities.replaceFragment(MandateFActivity.this, new Mandate_Fragggg(), R.id.container, false, true);
		flipCard();
	}

	public void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				try {
					Utilities.replaceFragment(fActivity, new MandateFragment(), R.id.container, false, true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
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
			// ToastHelper.Custom(getApplicationContext(),
			// "Current page size is saved.", 1);
			if (Utilities.D)
				Log.v(TAG, "page_size_is_saved");
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Availability"
//				, "Requirement" 
				};
//
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
//
		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}
//
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
//			
			case 0:
				return f = new MandateFragment();
//				
////			case 1:
////				return f = new RequireMLFragment();
//			
			default:
				break;
			}
//			// else if(position==2)
//			// f = new TCategorySpecialFragment();
			return f;
		}
	}


	private ActionMode currentActionMode;
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Actions");
			mode.getMenuInflater().inflate(R.menu.action_mode, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; 
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_edit:
				Toast.makeText(MandateFActivity.this, "Mandaingggg!", Toast.LENGTH_SHORT).show();
				mode.finish(); // Action picked, so close the contextual menu
				
				return true;
			case R.id.menu_delete:
				mode.finish();
				
				return true;
			default:
				
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			currentActionMode = null; // Clear current action mode
		}
	};
	// _ACTION_MENU---------------------------
	
	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			intent.getStringExtra(SearchManager.QUERY);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int ii = getSupportFragmentManager().getBackStackEntryCount();
			if(Utilities.D)Log.v(TAG,"getBackStackEntryCount()===" + ii);
			
			if(ii < 2){
				finish(); overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				
			} else 
				getSupportFragmentManager().popBackStack();
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
