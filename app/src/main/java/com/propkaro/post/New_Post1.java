package com.propkaro.post;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.propkaro.R;
import com.propkaro.util.Utilities;

public class New_Post1 extends Fragment implements OnItemSelectedListener,OnTouchListener{

	static String TAG = New_Post1.class.getSimpleName();
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area;
	ImageView iv_availability,iv_requirement,back1,back0;
	Typeface RobotoLight;

	int getAbsolut_prce = 0, getAbsolut_prce1, getAbsolut_prce2,
			getAbsolut_prce3;
	float getCalculateArea = 0;
	String getRent_purchase = "", getSubmitType = "", getPropertyType = "",
			getCity = "", getLocation = "", 
//			getProjectName = "",
			getCat_PropType = "", api_allCities = "";
	Context ctx;
	CardView card_view_availability,card_view_req;
	final Handler mHandler = new Handler();

	Spinner bathrooms, bedrooms, balconics, washrooms, saunit, caunit, paunit,
			core, lakh, thounsands;
	boolean mShowingBack = false;

	AutoCompleteTextView atv_location, atv_locality;
	private SharedPreferences mPrefs;

	View rView;
	GoogleMap googleMap;
	RadioButton rb1, rb2, rb3, rb4;
	Spinner spin_prop_type;
	Button spin_cityType;

	String spin_onaer[] = { "Availability", "Requirement" };

	String spin_prop_type1[] = { "Residential Apartment", "Residential Land",
			"Independent House/Villa", "Independent/Builder Floor",
			"Farmhouse", "Studio Apartment", "Service Apartment",
			"Residential Collaboration", "Other" };

	String spin_prop_type2[] = { "Commercial Office/Space", "Commercial Shops",
			"Commercial Land/Inst. Land", "Commercial Showrooms",
			"Agriculture/Farm Land", "Factory", "Warehouse",
			"Office in IT Park", "Hotel/Resorts", "Guest-House/Banquet-Halls",
			"Space in Retail Mall", "Office in Business Park",
			"Business center", "Manufacturing", "Cold Storage", "Time Share",
			"Industrial Lands/Plots", "Commercial Collaboration",
			"Preleased Property", "Paying Guest", "Others" };

	List<String> api_city = new ArrayList<String>();
	EditText mSearchView;
	ListView mListView;
	  ArrayAdapter<String> adapter_cities;
	    private CoordinatorLayout mCoordinator;
TextView   tv_1 ,tv_2,tv_3,tv_avalbility,tv_reqrement ,tv_step1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_1, container, false);

	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		// ////////////////////////////////////////////////////////
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 
		 tv_1 = (TextView) rView.findViewById(R.id.tv_1); 
		 tv_2 = (TextView) rView.findViewById(R.id.tv_2); 
		 tv_3 = (TextView) rView.findViewById(R.id.tv_3); 
		 tv_avalbility = (TextView) rView.findViewById(R.id.tv_avalbility); 
		 tv_reqrement = (TextView) rView.findViewById(R.id.tv_reqrement); 
		 tv_step1 = (TextView) rView.findViewById(R.id.tv_step1); 
		card_view_availability = (CardView) rView.findViewById(R.id.card_view_availability); 
		iv_availability = (ImageView) rView.findViewById(R.id.iv_availability); 
		  RelativeLayout rl_availability = (RelativeLayout) rView.findViewById(R.id.rl_availability);
		iv_requirement = (ImageView) rView.findViewById(R.id.iv_requirement); 

		card_view_req = (CardView) rView.findViewById(R.id.card_view_req); 
		RelativeLayout rl_requirement = (RelativeLayout) rView.findViewById(R.id.rl_requirement);
		
		 rl_requirement.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				  	
					getSubmitType =  "Requirement";
//					iv_requirement.setBackgroundResource(R.color.Gray);
			        Log.i("iv_requirement==",getSubmitType );                                            

//					iv_availability.setBackgroundResource(R.color.white);
					Bundle args = new Bundle();

					args.putString("KEY_Submit_type", getSubmitType);

					Fragment f = new New_Post2();
					f.setArguments(args);
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);
				      break;
				      
				    case MotionEvent.ACTION_MOVE:
				      break;
				     
				    default:
				      break;
				    }
					return true;
				 }
		    });
		 
		 rl_availability.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ResourceAsColor") @Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				    	getSubmitType = "Availability"; 
//						iv_availability.setBackgroundResource(R.color.Gray);
//						iv_requirement.setBackgroundResource(R.color.white);

					Bundle args = new Bundle();

					args.putString("KEY_Submit_type", getSubmitType);

					Fragment f = new New_Post2();
					f.setArguments(args);
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);
				      break;
				      
				    case MotionEvent.ACTION_MOVE:
				      break;
				   
				    }
					return true;
				 }
		    });
		back0 = (ImageView) rView.findViewById(R.id.back0);
		back0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
				alertDialogBuilder.setTitle("Alert");
				alertDialogBuilder
						.setMessage("Are you sure to cancel post?")
						.setCancelable(true);
				
							alertDialogBuilder.setNegativeButton("CANCEL",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

							alertDialogBuilder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						            	
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
								dialog.cancel();
								
								getActivity().finish();
				    			
								getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
							}
						});
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
			}
		});
		
		back1 = (ImageView) rView.findViewById(R.id.back1);

		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 
						Utilities.Snack(mCoordinator, "Please select Listing Type");
//				}
			Log.d(TAG, "getSubmitType==" + getSubmitType);
		 
			}
		});
		setFont();
		return rView;
	}
	// //////////////////////////////////////////////
	private void setFont() {
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_3 .setTypeface(RobotoLight);
	   tv_step1.setTypeface(RobotoLight);
		tv_avalbility .setTypeface(RobotoLight);
		tv_reqrement .setTypeface(RobotoLight);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		getSubmitType = getActivity().getIntent().getStringExtra("KEY_Submit_type");

		if(Utilities.D)Log.v(TAG,"getSubmitType=" + getSubmitType);

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
					Utilities.addFragment(getActivity(), new New_Post2(), R.id.container, true, true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		String item = parent.getItemAtPosition(position).toString();
			if(Utilities.D)Log.v(TAG,"postition=" + position + "\nITEM=" + item);
			getSubmitType = item;
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return false;
	}

	  
}