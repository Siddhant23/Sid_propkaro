package com.propkaro.mylisting;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.propkaro.R;
import com.propkaro.anim.DepthPageTransformer;
import com.propkaro.util.Utilities;

public class MyListingFActivity extends AppCompatActivity {
	private static final String TAG = MyListingFActivity.class.getSimpleName();
//	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private final Handler handler = new Handler();
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private Context ctx;
//	private ImageView iv_filter, plus_sign30, iv_pinFavorite;
	private SharedPreferences mPrefs;
	private String getMobile_num = "", getEmailID = "", getUserType = "", getUserDataID = "", 
			getUserName = "", getFName = "", getLName = "", getCity = "", getProfileImage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = this;
		setContentView(R.layout.tabs_pager_view_prop_center);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//	      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//	      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//	      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
	//  }
//	        activity.mToolbar.setNavigationIcon(R.drawable.back);
	        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
//	                onBackPressed();
	                finish();
	            }
	        });
	        getSupportActionBar().setTitle("My Listings");
			
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
			
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setPageTransformer(true, new DepthPageTransformer());

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
//---------------------------------------------Drawer items---------------------		

	}//endOncrea

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
			if(Utilities.D)Log.v(TAG,"page_size_is_saved");
		}
	}

	@Override
	protected void onStop() {
		super.onStop(); // To change body of overridden methods use File |
						// Settings | File Templates.
	}

	@Override
	public void onStart() {
		super.onStart();
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
				return f = new AvailMLFragment();
				
//			case 1:
//				return f = new RequireMLFragment();
			
			default:
				break;
			}
			// else if(position==2)
			// f = new TCategorySpecialFragment();
			return f;
		}
	}

	// --------------------------------------------ACTION_MENU---------------------------
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.card_flip, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		return super.onPrepareOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Log.d(TAG, "Item: " + item.getTitle());
//		Log.d(TAG, "ItemId: " + item.getItemId());
//		View menuItemView = findViewById(R.id.action_add);
//		
//		switch (item.getItemId()) {
//		case R.id.action_add:
//			onPopupButtonClick(menuItemView);
//			// if (currentActionMode != null) { return false; }
//			// startActionMode(modeCallBack);
//
//			break;
//
//		// case R.id.menuitem2:
//		// Toast.makeText(this, "Test 2 Clicked", Toast.LENGTH_SHORT).show();
//		// break;
//
//		default:
//			break;
//		}
//		// if (item.getItemId() == android.R.id.home) {
//		//
//		// }
//		return super.onOptionsItemSelected(item);
//	}

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
				Toast.makeText(MyListingFActivity.this, "Editing!", Toast.LENGTH_SHORT).show();
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
 