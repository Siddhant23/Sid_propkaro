package com.propkaro;

import com.propkaro.util.Utilities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.SearchView;


public class EditProfileActivity extends AppCompatActivity {
	private static final String TAG = EditProfileActivity.class.getSimpleName();
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private final Handler mHandler = new Handler();
	RelativeLayout rel;
	SearchView search;
	Filter filter;
	private boolean mShowingBack = false;
	FragmentActivity fActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fActivity = this;
		setContentView(R.layout.post_property);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setVisibility(View.GONE);

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
					Utilities.replaceFragment(fActivity, new EditProfileFragment(), R.id.container, false, true);
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
			if(Utilities.D)Log.v(TAG,"page_size_is_saved");
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