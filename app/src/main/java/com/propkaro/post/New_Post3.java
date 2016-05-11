package com.propkaro.post;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
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

public class New_Post3 extends Fragment implements OnTouchListener,OnItemSelectedListener{

	static String TAG = New_Post3.class.getSimpleName();
	String getBedrooms = "", getBathrooms = "", getBalconies = "",
			getWashrooms = "", getAdd_prce = "", getMntnce_chrg = "",
			getfloorbldng = "", getTotal_floor_bldng = "", getSuper_Area = "",
			getPlot_Area = "", getCarpet_Area = "", getSuper_Unit_Area = "",
			getCarpet_Unit_Area = "", getPlot_Unit_Area = "", getCrore = "",
			get_thousand = "", get_lakh = "", getAdd_prce_area_unit = "",
			getFreq = "";
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area;

	int getAbsolut_prce = 0, getAbsolut_prce1, getAbsolut_prce2,
			getAbsolut_prce3;
	float getCalculateArea = 0;
	String getRent_purchase = "", getSubmitType = "", getPropertyType = "",
			getCity = "", getLocation = "", 
//			getProjectName = "",
			getCat_PropType = "", api_allCities = "";
	Context ctx;
	SharedPreferences mPrefs;

	final Handler mHandler = new Handler();

	Spinner bathrooms, bedrooms, balconics, washrooms, saunit, caunit, paunit,
			core, lakh, thounsands;
	boolean mShowingBack = false;

	// static final String TAG_DES = "hotel_description";

	AutoCompleteTextView atv_location, atv_locality;
	// static final String TAG_DES = "hotel_description";

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
	CardView card_view_availability,card_view_req;
	ImageView iv_commercial,iv_residential,back1,back0;
    private CoordinatorLayout mCoordinator;
    TextView tv_from_step2, tv_1 ,tv_2,tv_comm,tv_resd,tv_step3 ;
Typeface RobotoLight;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_3, container, false);
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

	 tv_from_step2= (TextView) rView.findViewById(R.id.tv_from_step2);
		tv_from_step2.setText(getSubmitType + " � " + getRent_purchase);
		 tv_1 = (TextView) rView.findViewById(R.id.tv_1); 
		 tv_2 = (TextView) rView.findViewById(R.id.tv_2);
		 tv_step3 = (TextView) rView.findViewById(R.id.tv_step3);
		 tv_resd = (TextView) rView.findViewById(R.id.tv_resd);
		 tv_comm = (TextView) rView.findViewById(R.id.tv_comm);
		 
	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		// ////////////////////////////////////////////////////////
		card_view_availability = (CardView) rView.findViewById(R.id.card_view_availability); 
		iv_commercial = (ImageView) rView.findViewById(R.id.iv_commercial); 
		final RelativeLayout rl_commercial = (RelativeLayout) rView.findViewById(R.id.rl_commercial);
		iv_residential = (ImageView) rView.findViewById(R.id.iv_residential); 

		card_view_req = (CardView) rView.findViewById(R.id.card_view_req); 
		 RelativeLayout rl_residential = (RelativeLayout) rView.findViewById(R.id.rl_residential);
//		
		 rl_residential.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				  	
					getPropertyType =  "Residential";
//					iv_residential.setBackgroundResource(R.color.Gray);
//
//					iv_commercial.setBackgroundResource(R.color.white);
					Bundle args = new Bundle();

					args.putString("KEY_Rent_purchase", getRent_purchase);
					args.putString("KEY_Submit_type", getSubmitType);
					args.putString("KEY_Property_type", getPropertyType);

					Fragment f = new New_Post4();
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
		 
		 rl_commercial.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ResourceAsColor") @Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				    	getPropertyType = "Commercial"; 
//						iv_commercial.setBackgroundResource(R.color.Gray);
//
//				    	iv_residential.setBackgroundResource(R.color.white);

					Bundle args = new Bundle();

					args.putString("KEY_Rent_purchase", getRent_purchase);
					args.putString("KEY_Submit_type", getSubmitType);
					args.putString("KEY_Property_type", getPropertyType);
					Fragment f = new New_Post4();
					f.setArguments(args);
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);
				      break;
				      
				    case MotionEvent.ACTION_MOVE:
				      break;
				    case MotionEvent.ACTION_UP:
				      
				      break;
				    case MotionEvent.ACTION_CANCEL:
				      break;
				    default:
				      break;
				    }
					return true;
				 }
		    });
		back0 = (ImageView) rView.findViewById(R.id.back0);
		back0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG,"onclicking ...");
				SharedPreferences.Editor editor = mPrefs.edit();
				
				///////////////////PREF POST 1
				editor.putString("PREF_KEY_Rent_purchase", getRent_purchase);
				editor.putString("PREF_KEY_Submit_type", getSubmitType);
				editor.putString("PREF_KEY_Property_type", getPropertyType);
//				editor.putString("PREF_KEY_City_type", getCity);
//				editor.putString("PREF_KEY_Location_type", getLocation);
//				editor.putString("PREF_KEY_Cat_PropType", getCat_PropType);

				editor.commit();
				////*********************************///////////////////////////////////   
				getActivity().getSupportFragmentManager().popBackStack();

			}
		});
		back1 = (ImageView) rView.findViewById(R.id.back1);

		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.Snack(mCoordinator, "Please select Property Type");

				}
		});

		setFont();
		return rView;
	}
	private void setFont() {
		tv_from_step2.setTypeface(RobotoLight);
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_step3.setTypeface(RobotoLight);
		tv_resd .setTypeface(RobotoLight);
		tv_comm .setTypeface(RobotoLight);
	}
	// //////////////////////////////////////////////

	 

	public void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		mHandler.post(new Runnable() {
			@Override
			public void run() {

				try {
					Utilities.addFragment(getActivity(), new New_Post4(), R.id.container, true, true);

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
			getPropertyType = item;
			getSubmitType = item;
			getRent_purchase = item;
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getPropertyType = getActivity().getIntent().getStringExtra("KEY_Property_type");

	Bundle b = this.getArguments();
	getSubmitType = b.getString("KEY_Submit_type");
	getRent_purchase = b.getString("KEY_Rent_purchase");

	if(Utilities.D)Log.d(TAG, "=====getSubmitType=" + getSubmitType);
	if(Utilities.D)Log.d(TAG, "=====getRent_purchase=" + getRent_purchase);
	if(Utilities.D)Log.d(TAG, "=====getPropertyType=" + getPropertyType);

	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	};
}
	