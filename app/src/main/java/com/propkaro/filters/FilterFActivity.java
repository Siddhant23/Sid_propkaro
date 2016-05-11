package com.propkaro.filters;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.propkaro.R;
import com.propkaro.post.PostPropertyActivity;
import com.propkaro.util.Utilities;

public class FilterFActivity extends AppCompatActivity{
	private static final String TAG = FilterFActivity.class.getSimpleName();
	private final Handler handler = new Handler();
	private Context ctx;
	private SharedPreferences mPrefs;
	FragmentActivity fActivity;
	Fragment f;
	ImageView iv_connection, iv_msg, iv_location;
	TextView tv_connection198, tv_msg136, tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this; mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		fActivity = this;
		setContentView(R.layout.my_message_activity);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		SearchView search_view = (SearchView) findViewById(R.id.search_view);
		search_view.setVisibility(View.GONE);
		RelativeLayout rl_plus = (RelativeLayout) findViewById(R.id.rl_plus);
		rl_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FilterFActivity.this, PostPropertyActivity.class);
				startActivity(i);
			}
		});
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	int ii = getSupportFragmentManager().getBackStackEntryCount();
				if(Utilities.D)Log.v(TAG,"getBackStackEntryCount()===" + ii);
				
				if(ii < 2){
					finish(); 
				} else 
					getSupportFragmentManager().popBackStack();
            }
        });
        getSupportActionBar().setTitle("Find Your Match");
        
		Utilities.replaceFragment(fActivity, new FilterNewFragment(), R.id.container, true, true);
	}// endOncreaView

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

	/*
	 * @Override public void onStart() { super.onStart(); }
	 */

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

		private final String[] TITLES = { "Availability", "Requirement" };

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
//				return f = new FilterItemFragment();

			default:
				break;
			}
			// else if(position==2)
			// f = new TCategorySpecialFragment();
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
				Toast.makeText(FilterFActivity.this, "Editing!", Toast.LENGTH_SHORT).show();
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

	// --------------------------------------------END
	
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
