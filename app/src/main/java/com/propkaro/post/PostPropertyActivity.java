package com.propkaro.post;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.propkaro.R;
import com.propkaro.util.Utilities;

public class PostPropertyActivity extends AppCompatActivity {
	private static final String TAG = PostPropertyActivity.class.getSimpleName();
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private final Handler mHandler = new Handler();
	RelativeLayout rel;
	SearchView search;
	Filter filter;
	private boolean mShowingBack = false;
	FragmentActivity fActivity;
	private SharedPreferences mPrefs;
	static boolean isShowReview = true;
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		isShowReview = true;
		
		setContentView(R.layout.post_property);
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
    			showAlertFinish(fActivity, "Alert", "Are you sure to cancel post?", true);
            }
        });
        getSupportActionBar().setTitle("Post Listing");
		
		fActivity = this;
		flipCard();
	}//endOncrea

	public void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {

				try {
					Utilities.replaceFragment(fActivity, new New_Post1(), R.id.container, false, true);
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
@Override
protected void onDestroy() {
	
	SharedPreferences.Editor editor = mPrefs.edit();

	editor.putString("PREF_KEY_Rent_purchase", "");
	editor.putString("PREF_KEY_Submit_type", "");
	editor.putString("PREF_KEY_Property_type", "");
	editor.putString("PREF_KEY_City_type", "");
	editor.putString("PREF_KEY_Location_type", "");
	editor.putString("PREF_KEY_ProjectName_type", "");
	editor.putString("PREF_KEY_Cat_PropType", "");
	
	///////////////////PREF POST 2
	
	editor.putString("PREF_KEY_getSuper_Area","");
	editor.putString("PREF_KEY_getPlot_Area", "");
	editor.putString("PREF_KEY_getCarpet_Area", "");
	editor.putString("PREF_KEY_getAbsolut_prce", "" + "");
	editor.putString("PREF_KEY_getAdd_prce", "");
	editor.putString("PREF_KEY_getMntnce_chrg", "");
	editor.putString("PREF_KEY_getfloorbldng", "");
	editor.putString("PREF_KEY_getTotal_floor_bldng", "");

	editor.putString("PREF_KEY_getBedrooms", "");
	editor.putString("PREF_KEY_getBathrooms", "");
	editor.putString("PREF_KEY_getBalconies", "");
	editor.putString("PREF_KEY_getWashrooms", "");

	editor.putString("PREF_KEY_getCrore", "");
	editor.putString("PREF_KEY_get_lakh", "");
	editor.putString("PREF_KEY_get_thousand", "");

	editor.putString("PREF_KEY_getSuper_Unit_Area", "");
	editor.putString("PREF_KEY_getCarpet_Unit_Area", "");
	editor.putString("PREF_KEY_getPlot_Unit_Area", "");
	editor.putString("PREF_KEY_getAdd_prce_area_unit",	"");
	editor.putString("PREF_KEY_getFreq", "");
	
	editor.putString("PREF_getProp_ownership", "");
	editor.putString("PREF_KEY_getProp_availblty", "");
	editor.putString("PREF_KEY_get_furnishing_type", "");
	editor.putString("PREF_KEY_get_Transaction_type", "");
	editor.putString("PREF_KEY_get_Possession", "");
	editor.putString("PREF_KEY_get_Prop_Title", "");
	editor.putString("PREF_KEY_get_Descrption", "");
	
	editor.putString("PREF_KEY_getAmenities", "");

	editor.commit();
	super.onDestroy();
}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			showAlertFinish(fActivity, "Alert", "Are you sure to cancel post?", true);
			
			SharedPreferences.Editor editor = mPrefs.edit();

			editor.putString("PREF_KEY_Rent_purchase", "");
			editor.putString("PREF_KEY_Submit_type", "");
			editor.putString("PREF_KEY_Property_type", "");
			editor.putString("PREF_KEY_City_type", "");
			editor.putString("PREF_KEY_Location_type", "");
			editor.putString("PREF_KEY_ProjectName_type", "");
			editor.putString("PREF_KEY_Cat_PropType", "");
			
			///////////////////PREF POST 2
//			Log.e(TAG, "et_plot_area.getText().toString()"+ et_plot_area.getText().toString());
			
			editor.putString("PREF_KEY_getSuper_Area","");
			editor.putString("PREF_KEY_getPlot_Area", "");
			editor.putString("PREF_KEY_getCarpet_Area", "");
			editor.putString("PREF_KEY_getAbsolut_prce", "" + "");
			editor.putString("PREF_KEY_getAdd_prce", "");
			editor.putString("PREF_KEY_getMntnce_chrg", "");
			editor.putString("PREF_KEY_getfloorbldng", "");
			editor.putString("PREF_KEY_getTotal_floor_bldng", "");

			editor.putString("PREF_KEY_getBedrooms", "");
			editor.putString("PREF_KEY_getBathrooms", "");
			editor.putString("PREF_KEY_getBalconies", "");
			editor.putString("PREF_KEY_getWashrooms", "");

			editor.putString("PREF_KEY_getCrore", "");
			editor.putString("PREF_KEY_get_lakh", "");
			editor.putString("PREF_KEY_get_thousand", "");

			editor.putString("PREF_KEY_getSuper_Unit_Area", "");
			editor.putString("PREF_KEY_getCarpet_Unit_Area", "");
			editor.putString("PREF_KEY_getPlot_Unit_Area", "");
			editor.putString("PREF_KEY_getAdd_prce_area_unit",	"");
			editor.putString("PREF_KEY_getFreq", "");
			
			editor.putString("PREF_getProp_ownership", "");
			editor.putString("PREF_KEY_getProp_availblty", "");
			editor.putString("PREF_KEY_get_furnishing_type", "");
			editor.putString("PREF_KEY_get_Transaction_type", "");
			editor.putString("PREF_KEY_get_Possession", "");
			editor.putString("PREF_KEY_get_Prop_Title", "");
			editor.putString("PREF_KEY_get_Descrption", "");
			
			editor.putString("PREF_KEY_getAmenities", "");

			editor.commit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	 public static void showAlertFinish(final Activity ctx, String title, String msg, boolean cancelable){
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
	        alertDialogBuilder.setTitle(title);
	        alertDialogBuilder.setMessage(msg)
	        .setCancelable(cancelable)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	            	dialog.cancel();
	    			ctx.finish(); ctx.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	            }
	        });
	        
	        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	            }
	        });
	        AlertDialog alert = alertDialogBuilder.create();
	        alert.show();
	    }
}