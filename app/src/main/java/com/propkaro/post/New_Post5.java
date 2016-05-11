package com.propkaro.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.propkaro.R;
import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

public class New_Post5 extends Fragment implements OnItemSelectedListener{

	static String TAG = New_Post5.class.getSimpleName();
	String getBedrooms = "", getBathrooms = "", getBalconies = "",
			getWashrooms = "", getAdd_prce = "", getMntnce_chrg = "",
			getfloorbldng = "", getTotal_floor_bldng = "", getSuper_Area = "",
			getPlot_Area = "", getCarpet_Area = "", getSuper_Unit_Area = "",
			getCarpet_Unit_Area = "", getPlot_Unit_Area = "", getCrore = "",
			get_thousand = "", get_lakh = "", getAdd_prce_area_unit = "",
			getFreq = "";
	String temp_getfloorbldng = "0", temp_crore = "0", temp_getBedrooms = "0",  temp_getBathrooms = "0", 
			temp_getBalconies = "0", temp_getWashrooms = "0",
			temp_getAdd_prce = "0",   
			temp_getTotal_floor_bldng = "0",  temp_getSuper_Area = "0", 
			temp_getPlot_Area = "0",  temp_getCarpet_Area = "0", 
			temp_getSuper_Unit_Area = "0",  temp_getCarpet_Unit_Area = "0",
			temp_getPlot_Unit_Area ="0",    
			temp_get_thousand = "0",  temp_get_lakh = "0",  temp_get_freq = "0", 
			temp_getAdd_prce_area_unit = "0";
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area,tv_absolute_prce;
	ImageView back1,back0;
	EditText et_super_area, et_carpet_area, et_plot_area, et_absolute_prce,
	et_add_price, et_mntnce_charge,et_bedrooms,et_bathrooms,et_balconics,et_washrooms;
	String getProp_ownership = "", getProp_availblty = "",
			get_furnishing_type = "", get_Transaction_type = "",
			get_Prop_Title = "", get_Descrption = "";
	long getAbsolut_prce ;
	long getCalculateArea = 0;
	long getCalculateArea1=0;
	String getRent_purchase = "", getSubmitType = "", getPropertyType = "",
			getCity = "", getLocation = "", 
//			getProjectName = "",
			getCat_PropType = "", api_allCities = "";
	String api_latitude = "", api_longitude = "";
	Context ctx;
	SharedPreferences mPrefs;
	final Handler mHandler = new Handler();

	Spinner bedrooms, bathrooms, balconics, washrooms, floor_bldng,
	total_floor_bldng, freq, saunit, caunit, paunit, add_price_unit;
	boolean mShowingBack = false;
	Spinner spin_poss, spin_trans_type, spin_pvt_oner, spin_avail,
	spin_furnish;
	View rView;
	Spinner spin_prop_type;
	Button spin_cityType;
	EditText  et_Descrption;
	Typeface RobotoLight;
TextView tv_from_step4,tv_1 ,tv_2,tv_step5;

	String str_spn_carpet_unit[] = { "Select Unit", "Sq.Ft", "Sq.Yards",
			"Sq.Meter", "Acres", "Marla", "Cents", "Bigha", "Kottah", "Kanal",
			"Grounds", "Ares", "Biswa", "Guntha", "Aankadam", "Hectares",
			"Rood", "Chataks", "Perch" };
	String str_spn_plot_unit[] = { "Select Unit", "Sq.Ft", "Sq.Yards",
			"Sq.Meter", "Acres", "Marla", "Cents", "Bigha", "Kottah", "Kanal",
			"Grounds", "Ares", "Biswa", "Guntha", "Aankadam", "Hectares",
			"Rood", "Chataks", "Perch" };
	 
	String str_spn_super_unit[] = { "Select Unit", "Sq.Ft", "Sq.Yards",
			"Sq.Meter", "Acres", "Marla", "Cents", "Bigha", "Kottah", "Kanal",
			"Grounds", "Ares", "Biswa", "Guntha", "Aankadam", "Hectares",
			"Rood", "Chataks", "Perch" };
	String str_spn_total_bldng_floor[] = {"Total floor", "Basement","Lower Ground","Ground",
			"1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40","40+","Multi-Storied" };
	
	String str_spn_floor_bldng[] = {"Property on Floor","Basement","Lower Ground","Ground",
			"1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40","40+","Multi-Storied"  };
	
	String str_spin_owner[] = {"Ownership", "Freehold", "Leasehold",
			"Co-operative Society", "Power of Attorney" };
	String str_spin_avalability[] = { "Availability", "Under construction", "Ready to move" };
	String str_spin_transacion[] = { "Transaction", "Rent", "Resale", "New Booking" };
	  LinearLayout lnr_1, lnr_2, lnr_3, lnr_4, lnr_5, lnr_6, lnr_7, lnr_8, lnr_9;
	  String  temp_getProp_ownership = "0",  temp_getProp_availblty = "0",  temp_get_furnishing_type = "0", 
				temp_get_Transaction_type = "0",temp_get_Possession = "0";
	    private CoordinatorLayout mCoordinator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_5, container, false);
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

		tv_from_step4= (TextView) rView.findViewById(R.id.tv_from_step4);
		tv_from_step4.setText(getSubmitType + " • " + getRent_purchase + " • " + getPropertyType + " • " +
             				getCat_PropType + " • " + getCity);
		 tv_1 = (TextView) rView.findViewById(R.id.tv_1); 
		 tv_2 = (TextView) rView.findViewById(R.id.tv_2); 
		 tv_step5 = (TextView) rView.findViewById(R.id.tv_step5); 

		// ////////////////////////////////////////////////////////
	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		lnr_1 = (LinearLayout) rView.findViewById(R.id.lnr_1);
		lnr_2 = (LinearLayout) rView.findViewById(R.id.lnr_2);
		lnr_3 = (LinearLayout) rView.findViewById(R.id.lnr_3);
		lnr_4 = (LinearLayout) rView.findViewById(R.id.lnr_4);
		lnr_5 = (LinearLayout) rView.findViewById(R.id.lnr_5);
		lnr_6 = (LinearLayout) rView.findViewById(R.id.lnr_6);
		lnr_7 = (LinearLayout) rView.findViewById(R.id.lnr_7);
		lnr_8 = (LinearLayout) rView.findViewById(R.id.lnr_8);
		lnr_9 = (LinearLayout) rView.findViewById(R.id.lnr_9);
/////////////////////////////////////////////////////////////////////
		et_super_area = (EditText) rView.findViewById(R.id.et_super_area);
		et_carpet_area = (EditText) rView.findViewById(R.id.et_carpet_area);
		et_plot_area = (EditText) rView.findViewById(R.id.et_plot_area);
		
		et_bedrooms = (EditText) rView.findViewById(R.id.et_bedrooms);
		et_bathrooms = (EditText) rView.findViewById(R.id.et_bathrooms);
		et_balconics = (EditText) rView.findViewById(R.id.et_balconics);
		et_washrooms = (EditText) rView.findViewById(R.id.et_washrooms);

		et_Descrption = (EditText) rView.findViewById(R.id.et_Descrption);
		et_absolute_prce = (EditText) rView.findViewById(R.id.et_absolute_prce);
		tv_absolute_prce = (TextView) rView.findViewById(R.id.tv_absolute_prce);
		
		
		et_absolute_prce.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_absolute_prce.setText(Host.rupeeFormat(et_absolute_prce.getText().toString()));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		saunit = (Spinner) rView.findViewById(R.id.spin_super_unit_area);
		saunit.setOnItemSelectedListener(this);
		ArrayAdapter<String> sauAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_spinner_item, str_spn_super_unit);
		sauAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		saunit.setAdapter(sauAdapter);
		saunit.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		caunit = (Spinner) rView.findViewById(R.id.spin_carpet_unit_area);
		caunit.setOnItemSelectedListener(this);
		ArrayAdapter<String> cauAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_spinner_item,
				str_spn_carpet_unit);
		cauAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		caunit.setAdapter(cauAdapter);
		caunit.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;

		paunit = (Spinner) rView.findViewById(R.id.spin_plot_unit_area);
		paunit.setOnItemSelectedListener(this);
		ArrayAdapter<String> pauAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				str_spn_plot_unit);
		pauAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		paunit.setAdapter(pauAdapter);
		paunit.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		
		total_floor_bldng = (Spinner) rView	.findViewById(R.id.spin_total_bldng_floor);
		total_floor_bldng.setOnItemSelectedListener(this);
		ArrayAdapter<String> total_floor_bldngAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_spinner_item,
				str_spn_total_bldng_floor);
		total_floor_bldngAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		total_floor_bldng.setAdapter(total_floor_bldngAdapter);
		total_floor_bldng.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		
		floor_bldng = (Spinner) rView.findViewById(R.id.spin_floor_buildng);
		floor_bldng.setOnItemSelectedListener(this);
		ArrayAdapter<String> floorbldngAdapter = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_spinner_item,
				str_spn_floor_bldng);
		floorbldngAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		floor_bldng.setAdapter(floorbldngAdapter);	
		
		floor_bldng.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		spin_avail = (Spinner) rView.findViewById(R.id.spin_avail);
		spin_avail.setOnItemSelectedListener(this);

		// // Creating adapter for spinner
		ArrayAdapter<String> bathAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				str_spin_avalability);

		bathAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_avail.setAdapter(bathAdapter);
		
		spin_avail.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		spin_pvt_oner = (Spinner) rView.findViewById(R.id.spin_pvt_oner);
		spin_pvt_oner.setOnItemSelectedListener(this);

		// Creating adapter for spinner
		ArrayAdapter<String> bedAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				str_spin_owner);

		bedAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_pvt_oner.setAdapter(bedAdapter);
		
		spin_pvt_oner.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		spin_trans_type = (Spinner) rView.findViewById(R.id.spin_trans_type);
		spin_trans_type.setOnItemSelectedListener(this);

		// // Creating adapter for spinner
		ArrayAdapter<String> balAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				str_spin_transacion);

		balAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spin_trans_type.setAdapter(balAdapter);
		spin_trans_type.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            	Utilities.hideKeyboard(getActivity());
                return false;
            }
        }) ;
		/////////////////////////////////////////////////////////
//		NumberFormat f = NumberFormat.getInstance(Locale.ENGLISH);
//		if (f instanceof DecimalFormat) {
//		    ((DecimalFormat) f).applyPattern("##0,000.0");
//		}
//		f.format(123456 / 10.0);
//		try {
//			DecimalFormat formatter = new DecimalFormat("##,##,###");
//			getAbsolut_prce = Long.parseLong(formatter.format(1000000));
////			getAbsolut_prce = Long.parseLong( mPrefs.getString("PREF_KEY_getAbsolut_prce",""));
//			et_absolute_prce.setText(getAbsolut_prce + "");
//		} catch (NumberFormatException e) {
//			System.out.print(e);
//		}
		

		Log.v(TAG, "click-----------crash");
		getBedrooms = mPrefs.getString("PREF_KEY_getBedrooms", "");
		et_bedrooms.setText(getBedrooms+"");

		getBalconies = mPrefs.getString("PREF_KEY_getBalconies", "");
		et_balconics.setText(getBalconies+"");

		getBathrooms = mPrefs.getString("PREF_KEY_getBathrooms", "");
		et_bathrooms.setText(getBathrooms+"");

		getWashrooms = mPrefs.getString("PREF_KEY_getWashrooms", "");
		et_washrooms.setText(getWashrooms+"");
		
		get_Descrption = mPrefs.getString("PREF_KEY_get_Descrption", "");
		et_Descrption.setText(get_Descrption+"");
		
		try {
		
		getAbsolut_prce = Long.parseLong( mPrefs.getString("PREF_KEY_getAbsolut_prce",""));
		
		et_absolute_prce.setText(getAbsolut_prce+"");
		} catch (NumberFormatException e) {
			System.out.print(e);
		}
		getSuper_Area = mPrefs.getString("PREF_KEY_getSuper_Area", "");
		et_super_area.setText(getSuper_Area+"");
		
		getCarpet_Area = mPrefs.getString("PREF_KEY_getCarpet_Area", "");
		et_carpet_area.setText(getCarpet_Area+"");
		
		getPlot_Area = mPrefs.getString("PREF_KEY_getPlot_Area", "");
		et_plot_area.setText(getPlot_Area+"");
		
		Log.e(TAG, "getPlot_Area=@@=========="+getPlot_Area);
		
		getfloorbldng = mPrefs.getString("PREF_KEY_getfloorbldng", "");
		temp_getfloorbldng = getfloorbldng;
		
		getTotal_floor_bldng = mPrefs.getString("PREF_KEY_getTotal_floor_bldng", "");
		temp_getTotal_floor_bldng = getTotal_floor_bldng;
		
		
		getSuper_Unit_Area = mPrefs.getString("PREF_KEY_getSuper_Unit_Area", "");
		temp_getSuper_Unit_Area = getSuper_Unit_Area;
		
		
		getCarpet_Unit_Area = mPrefs.getString("PREF_KEY_getCarpet_Unit_Area", "");
		temp_getCarpet_Unit_Area = getCarpet_Unit_Area;
		
		
		getPlot_Unit_Area = mPrefs.getString("PREF_KEY_getPlot_Unit_Area", "");
		temp_getPlot_Unit_Area = getPlot_Unit_Area;

		
		getProp_ownership = mPrefs.getString("PREF_KEY_getProp_ownership", "");
		temp_getProp_ownership = getProp_ownership;
		
		getProp_availblty = mPrefs.getString("PREF_KEY_getProp_availblty", "");
		temp_getProp_availblty = getProp_availblty;
		
		get_Transaction_type = mPrefs.getString("PREF_KEY_get_Transaction_type", "");
		temp_get_Transaction_type = get_Transaction_type;
		////////////////////////////////////////////////////////
		

		back0 = (ImageView) rView.findViewById(R.id.back0);
		back0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG,"onclicking ...");
				SharedPreferences.Editor editor = mPrefs.edit();
			    Log.e(TAG, "et_plot_area.getText().toString()"+ et_plot_area.getText().toString());

				editor.putString("PREF_KEY_getSuper_Area", et_super_area.getText().toString()+"");
				editor.putString("PREF_KEY_getPlot_Area", et_plot_area.getText().toString()+"");
				editor.putString("PREF_KEY_getCarpet_Area", et_carpet_area.getText().toString()+"");
				editor.putString("PREF_KEY_getAbsolut_prce", et_absolute_prce.getText().toString()+"");
				editor.putString("PREF_KEY_get_Descrption", et_Descrption.getText().toString()+"");
				editor.putString("PREF_KEY_getfloorbldng", floor_bldng.getSelectedItem().toString());
				editor.putString("PREF_KEY_getTotal_floor_bldng", total_floor_bldng.getSelectedItem().toString());
				
				editor.putString("PREF_KEY_get_Transaction_type", spin_trans_type.getSelectedItem().toString());
				editor.putString("PREF_KEY_getProp_availblty", spin_avail.getSelectedItem().toString());
				editor.putString("PREF_KEY_getProp_ownership", spin_pvt_oner.getSelectedItem().toString());

				editor.putString("PREF_KEY_getBedrooms",et_bedrooms.getText().toString()+"");
				editor.putString("PREF_KEY_getBathrooms",et_bathrooms.getText().toString()+"");
				editor.putString("PREF_KEY_getBalconies", et_balconics.getText().toString()+"");
				editor.putString("PREF_KEY_getWashrooms", et_washrooms.getText().toString()+"");

				editor.putString("PREF_KEY_getSuper_Unit_Area", saunit.getSelectedItem().toString());
				editor.putString("PREF_KEY_getCarpet_Unit_Area", caunit.getSelectedItem().toString());
				editor.putString("PREF_KEY_getPlot_Unit_Area", paunit.getSelectedItem().toString());

				editor.commit();
	
				getActivity().getSupportFragmentManager().popBackStack();

				
			}
		});
		back1 = (ImageView) rView.findViewById(R.id.back1);
		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
     			getSuper_Area = et_super_area.getText().toString();
     			getCarpet_Area = et_carpet_area.getText().toString();
     			getPlot_Area = et_plot_area.getText().toString();
				get_Descrption = et_Descrption.getText().toString();
     			getBedrooms = et_bedrooms.getText().toString();
     			getBathrooms = et_bathrooms.getText().toString();
     			getBalconies = et_balconics.getText().toString();
     			getWashrooms = et_washrooms.getText().toString();
     			getCalculateArea = Long.parseLong("" + getCalculateArea);
				
     			try{
				getAbsolut_prce = Long.parseLong(et_absolute_prce.getText().toString());
//				 et_absolute_prce.setText("" + getAbsolut_prce);
				} catch (NumberFormatException numberEx) {
					System.out.print(numberEx);
				 }

				try {
//				if (!(getSuper_Area.isEmpty())){
					getCalculateArea1 = Long.parseLong("" +((getAbsolut_prce) / getCalculateArea));
//				}
				} catch (ArithmeticException arithEx)  {
					System.out.print(arithEx);

				} catch (NumberFormatException numberEx) {
					System.out.print(numberEx);
				 }
				
				Bundle args = new Bundle();
/////////////////////////////////post 1////////////////////////////
				args.putLong("KEY_Calc", getCalculateArea1);
				args.putString("KEY_Rent_purchase", getRent_purchase);
				args.putString("KEY_Submit_type", getSubmitType);
				args.putString("KEY_Property_type", getPropertyType);
				args.putString("KEY_City_type", getCity);
				args.putString("KEY_Cat_PropType", getCat_PropType);
				args.putString("KEY_Location_type", getLocation);
///////////////////////////////////////////////////////////////////////
				
				///////////////////////////post 2////////////////////////////
				args.putString("KEY_getSuper_Area", getSuper_Area);
				args.putString("KEY_getPlot_Area", getPlot_Area);
				args.putString("KEY_getCarpet_Area", getCarpet_Area);
				args.putLong("KEY_getAbsolut_prce",  getAbsolut_prce);
				args.putString("KEY_getfloorbldng", getfloorbldng);
				args.putString("KEY_getTotal_floor_bldng", getTotal_floor_bldng);

				
				args.putString("KEY_getBedrooms", getBedrooms);
				args.putString("KEY_getBathrooms", getBathrooms);
				args.putString("KEY_getBalconies", getBalconies);
				args.putString("KEY_getWashrooms", getWashrooms);
				args.putString("KEY_get_Descrption", get_Descrption);

				args.putString("KEY_getSuper_Unit_Area", getSuper_Unit_Area);
				args.putString("KEY_getCarpet_Unit_Area", getCarpet_Unit_Area);
				args.putString("KEY_getPlot_Unit_Area", getPlot_Unit_Area);
				
				args.putString("KEY_get_Transaction_type", get_Transaction_type);
				args.putString("KEY_getProp_availblty",getProp_availblty );
				args.putString("KEY_getProp_ownership", getProp_ownership);

				args.putString("KEY_api_latitude", api_latitude + "");
				args.putString("KEY_api_longitude", api_longitude + "");
			
				////////////////////////////post 3////////////////////
				Fragment f = new New_Post6();
				f.setArguments(args);
				if (getRent_purchase.contains("Rent")&& getCat_PropType.equals("Residential Apartment")) {	
					Log.d(TAG, "getRent_purchaseXXX1==" + getRent_purchase);
					
					if (et_absolute_prce.getText().toString().length() == 0) {
					  	Utilities.Snack(mCoordinator, "Please enter expected price" );
				
					} else if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				  	} else if (et_super_area.getText().toString().length() == 0) {
						Utilities.Snack(mCoordinator, "Please select Super area " );
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );
	
					
					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator,
								"Please select unit area");

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type" );
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);
//--------------------------------------go to fragemnt 6-------------------------
					} else	if (getRent_purchase.contains("Rent")&& getCat_PropType.equals("Residential Land")) {	
						Log.d(TAG, "getRent_purchaseXXX==" + getRent_purchase);
						if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator, "Please enter Absolut price");

				   }	else  if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select plot area" );
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator,
								"Please select unit area");
					
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,"Please enter Transaction Type" );
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);
						//--------------------------------------go to fragemnt 6-------------------------

					} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Independent House/Villa")) {
						Log.d(TAG, "getRent_purchaseZZZ==" + getRent_purchase);

					 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,"Please enter expected price" );
					} else if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);
					
				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Independent House/Villa")) {
					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");

					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
						
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);
					//--------------------------------------go to fragemnt 6-------------------------

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Independent/Builder Floor")) {
					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator, "Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);
					//--------------------------------------go to fragemnt 6-------------------------

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Farmhouse")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);
					//--------------------------------------go to fragemnt 6-------------------------

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Studio Apartment")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");

					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&& getCat_PropType.contains("Service Apartment")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");

					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Residential Collaboration")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Plot area ");
						   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.equalsIgnoreCase("Other")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Plot area ");
						   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

					// commercial wali//
				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Commercial Land/Inst. Land")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Plot area ");
						   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Commercial Office/Space")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else		if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Commercial Shops")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
							
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Factory")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Commercial Showrooms")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Agriculture/Farm Land")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select plot area ");
						   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Industrial Lands/Plots")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Plot area ");
						   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Warehouse")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Preleased Property")) {
					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Office in IT Park")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container, true, true);

				} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Office in Business Park")) {

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Guest-House/Banquet-Halls")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");

					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");

					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Hotel/Resorts")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");

					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Paying Guest")) {
				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
						Utilities.Snack(mCoordinator, "Please enter Bedrooms");
					} else if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Business center")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Manufacturing")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Cold Storage")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else		if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Time Share")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Commercial Collaboration")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
					} else if (getPlot_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.equalsIgnoreCase("Others")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");
					} else
						Utilities.addFragment(getActivity(), f, R.id.container,
								true, true);

			} else	if (getRent_purchase.contains("Rent")&&getCat_PropType.contains("Space in Retail Mall")) {


				 if (et_absolute_prce.length() ==0) {
								Utilities.Snack(mCoordinator,
										"Please enter expected price" );
							}else	if (et_super_area.length() == 0) {
								Utilities.Snack(mCoordinator,
										"Please select super area ");
							} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
								Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

							} else if (getSuper_Unit_Area.contains("Select Unit")) {
								Utilities.Snack(mCoordinator, "Please select unit");

					} else if (getAdd_prce_area_unit.contains("Select")) {
						Utilities.Snack(mCoordinator,
								"Please enter Add price Unit");
					} else if (get_Transaction_type.contains("Transaction")) {
						Utilities.Snack(mCoordinator,
								"Please enter Transaction Type");

					} else
						Utilities.addFragment(getActivity(), f, R.id.container,	true, true);
					
					 ////////////////SALE////////////////////////////////
					
			} else	if (getRent_purchase.contains("Sale")&& getCat_PropType.equals("Residential Apartment")) {	
				Log.d(TAG, "getRent_purchaseXXX1==" + getRent_purchase);

					Log.d(TAG, "getCat_PropTypeXXX1==" + getCat_PropType);

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	 if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area " );
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator,
							"Please select unit area");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type" );
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&& getCat_PropType.equals("Residential Land")) {	
					Log.d(TAG, "getRent_purchaseXXX==" + getRent_purchase);

				Log.d(TAG, "getCat_PropTypeXXX==" + getCat_PropType);

				if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator, "Please enter expected price" );
				}else if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator, "Please select plot area" );
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit area");

				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator, "Please enter Add price Unit" );
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,"Please enter Transaction Type" );
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator, "Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator, "Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Independent House/Villa")) {
					Log.d(TAG, "getRent_purchaseZZZ==" + getRent_purchase);

					 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);
				
			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Independent House/Villa")) {
				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
					
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Independent/Builder Floor")) {
				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Farmhouse")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Studio Apartment")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&& getCat_PropType.contains("Service Apartment")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else		if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Residential Collaboration")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );

				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.equalsIgnoreCase("Other")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

				// commercial wali//
			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Commercial Land/Inst. Land")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );

				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Commercial Office/Space")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Commercial Shops")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Factory")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Commercial Showrooms")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Agriculture/Farm Land")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );

				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Industrial Lands/Plots")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Plot area ");
					   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );

				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Warehouse")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else		if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Preleased Property")) {
				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Office in IT Park")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else		if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container, true, true);

			} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Office in Business Park")) {

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Guest-House/Banquet-Halls")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else		if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );


				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");

				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Hotel/Resorts")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");

				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Paying Guest")) {
			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
					Utilities.Snack(mCoordinator, "Please enter Bedrooms");
				} else if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Business center")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else			if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Manufacturing")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Cold Storage")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Time Share")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Commercial Collaboration")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else		if (et_plot_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
				} else if (getPlot_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );

				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.equalsIgnoreCase("Others")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
					Utilities.Snack(mCoordinator,
							"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

				} else if (getSuper_Unit_Area.contains("Select Unit")) {
					Utilities.Snack(mCoordinator, "Please select unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);

		} else	if (getRent_purchase.contains("Sale")&&getCat_PropType.contains("Space in Retail Mall")) {


			 if (et_absolute_prce.length() ==0) {
							Utilities.Snack(mCoordinator,
									"Please enter expected price" );
						}else	if (et_super_area.length() == 0) {
							Utilities.Snack(mCoordinator,
									"Please select super area ");
						} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
							Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

						} else if (getSuper_Unit_Area.contains("Select Unit")) {
							Utilities.Snack(mCoordinator, "Please select unit");

				} else if (getAdd_prce_area_unit.contains("Select")) {
					Utilities.Snack(mCoordinator,
							"Please enter Add price Unit");
				} else if (get_Transaction_type.contains("Transaction")) {
					Utilities.Snack(mCoordinator,
							"Please enter Transaction Type");
				} else if (getProp_ownership.contains("Ownership")) {
					Utilities.Snack(mCoordinator,
							"Please enter Ownership Type" );
				} else if (getProp_availblty.contains("Availability")) {
					Utilities.Snack(mCoordinator,
							"Please enter Availability Type" );
				} else
					Utilities.addFragment(getActivity(), f, R.id.container,
							true, true);
				
////////////////////////////////PURCHASE/////////////////////////////////////////////
	
		} else	if (getRent_purchase.contains("Purchase")&& getCat_PropType.equals("Residential Apartment")) {	
			Log.d(TAG, "getRent_purchaseXXX1==" + getRent_purchase);

				Log.d(TAG, "getCat_PropTypeXXX1==" + getCat_PropType);

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	 if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area " );
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator,
						"Please select unit area");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type" );
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&& getCat_PropType.equals("Residential Land")) {	
				Log.d(TAG, "getRent_purchaseXXX==" + getRent_purchase);

			Log.d(TAG, "getCat_PropTypeXXX==" + getCat_PropType);

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else  if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select plot area" );
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator,
						"Please select unit area");

			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit" );
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,"Please enter Transaction Type" );
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);
//	

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Independent House/Villa")) {
				Log.d(TAG, "getRent_purchaseZZZ==" + getRent_purchase);

				 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");
			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);
			
		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Independent House/Villa")) {
			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
				
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Independent/Builder Floor")) {
			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");
			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Farmhouse")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");
			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Studio Apartment")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");

			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&& getCat_PropType.contains("Service Apartment")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else		if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");

			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Residential Collaboration")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");


			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );

			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.equalsIgnoreCase("Other")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

			// commercial wali//
		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Commercial Land/Inst. Land")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");

			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );

			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Commercial Office/Space")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Commercial Shops")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Factory")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Commercial Showrooms")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Agriculture/Farm Land")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );

			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Industrial Lands/Plots")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Plot area ");
				   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );

			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Warehouse")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Preleased Property")) {
			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");
			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");

			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Office in IT Park")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container, true, true);

		} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Office in Business Park")) {

			 if (et_absolute_prce.length() ==0) {
					Utilities.Snack(mCoordinator,
							"Please enter expected price" );
				}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
				} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Guest-House/Banquet-Halls")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );


			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");

			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Hotel/Resorts")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else		if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");

			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Paying Guest")) {
		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_bedrooms.length() == 0) {
				Utilities.Snack(mCoordinator, "Please enter Bedrooms");
			} else if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Business center")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Manufacturing")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else		if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Cold Storage")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Time Share")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Commercial Collaboration")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_plot_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select plot area ");
			   } else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_plot_area.getText().toString())) {
					Utilities.Snack(mCoordinator, "Expected Price must be greater than Plot Area" );
			} else if (getPlot_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );

			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.equalsIgnoreCase("Others")) {

		 if (et_absolute_prce.length() ==0) {
				Utilities.Snack(mCoordinator,
						"Please enter expected price" );
			}else	if (et_super_area.length() == 0) {
				Utilities.Snack(mCoordinator,
						"Please select Super area ");
			} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
				Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

			} else if (getSuper_Unit_Area.contains("Select Unit")) {
				Utilities.Snack(mCoordinator, "Please select unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);

	} else	if (getRent_purchase.contains("Purchase")&&getCat_PropType.contains("Space in Retail Mall")) {


		 if (et_absolute_prce.length() ==0) {
						Utilities.Snack(mCoordinator,
								"Please enter expected price" );
					}else	if (et_super_area.length() == 0) {
						Utilities.Snack(mCoordinator,
								"Please select super area ");
					} else if (Long.parseLong(et_absolute_prce.getText().toString()) < Long.parseLong(et_super_area.getText().toString())) {
						Utilities.Snack(mCoordinator, "Expected Price must be greater than Super Area" );

					} else if (getSuper_Unit_Area.contains("Select Unit")) {
						Utilities.Snack(mCoordinator, "Please select unit");
			} else if (getAdd_prce_area_unit.contains("Select")) {
				Utilities.Snack(mCoordinator,
						"Please enter Add price Unit");
			} else if (get_Transaction_type.contains("Transaction")) {
				Utilities.Snack(mCoordinator,
						"Please enter Transaction Type");
			} else if (getProp_ownership.contains("Ownership")) {
				Utilities.Snack(mCoordinator,
						"Please enter Ownership Type" );
			} else if (getProp_availblty.contains("Availability")) {
				Utilities.Snack(mCoordinator,
						"Please enter Availability Type" );
			} else
				Utilities.addFragment(getActivity(), f, R.id.container,
						true, true);
				}  else {

					Log.d(TAG, "getCalculateArea==" + getCalculateArea);
					Log.d(TAG, "getCalculateArea1==" + getCalculateArea1);
					Log.d(TAG, "getAbsolut_prce==" + getAbsolut_prce);
					Log.d(TAG, "getSuper_Area==" + getSuper_Area);
					Log.d(TAG, "getPlot_Area==" + getPlot_Area);
					Log.d(TAG, "getCarpet_Area==" + getCarpet_Area);
					Log.d(TAG, "getSuper_Unit_Area==" + getSuper_Unit_Area);
					Log.d(TAG, "getCarpet_Unit_Area==" + getCarpet_Unit_Area);
					Log.d(TAG, "getPlot_Unit_Area==" + getPlot_Unit_Area);
				}
				
			}
			
		});

		setFont();
		return rView;
	}
	private void setFont() {
		tv_from_step4.setTypeface(RobotoLight);
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_step5.setTypeface(RobotoLight);
		et_super_area .setTypeface(RobotoLight);
		et_carpet_area .setTypeface(RobotoLight);
		et_plot_area .setTypeface(RobotoLight);
		
		et_bedrooms .setTypeface(RobotoLight);
		et_bathrooms .setTypeface(RobotoLight);
		et_balconics .setTypeface(RobotoLight);
		et_washrooms .setTypeface(RobotoLight);

		et_Descrption .setTypeface(RobotoLight);
		et_absolute_prce .setTypeface(RobotoLight);
		tv_absolute_prce .setTypeface(RobotoLight);

	}
	// //////////////////////////////////////////////

	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);

	Bundle b = this.getArguments();
	getSubmitType = b.getString("KEY_Submit_type");
	getRent_purchase = b.getString("KEY_Rent_purchase");
	getPropertyType = b.getString("KEY_Property_type");
	getCat_PropType = b.getString("KEY_Cat_PropType");
	getCity = b.getString("KEY_City_type");
	getLocation = b.getString("KEY_Location_type");

	api_latitude = b.getString("KEY_api_latitude");
	api_longitude = b.getString("KEY_api_longitude");

	if(Utilities.D)Log.d(TAG, "=====getSubmitType=" + getSubmitType);
	if(Utilities.D)Log.d(TAG, "=====getRent_purchase=" + getRent_purchase);
	if(Utilities.D)Log.d(TAG, "=====getPropertyType=" + getPropertyType);
	if(Utilities.D)Log.d(TAG, "=====getCat_PropType=" + getCat_PropType);
	if(Utilities.D)Log.d(TAG, "=====getCity=" + getCity);
	if(Utilities.D)Log.d(TAG, "=====getLocation=" + getLocation);

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
					Utilities.addFragment(getActivity(), new New_Post6(), R.id.container, true, true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		String item = parent.getItemAtPosition(position).toString();

		Log.d("TAG", "item===" + item + "\nposition====" + position);

		getSuper_Area = et_super_area.getText().toString();
		getCarpet_Area = et_carpet_area.getText().toString();
		getPlot_Area = et_plot_area.getText().toString();
		
		if (getRent_purchase.contains("Rent")) {
		if (getCat_PropType.contains("Residential Apartment")) {

			if(Utilities.D)Log.v(TAG,"---------------------2-----------------" + getPropertyType);
			et_washrooms.setVisibility(View.GONE);
			lnr_5.setVisibility(View.GONE);
			lnr_7.setVisibility(View.GONE);
			lnr_8.setVisibility(View.GONE);
			lnr_9.setVisibility(View.GONE);
			if (!(getSuper_Area.isEmpty()))
				getCalculateArea = Long.parseLong("" + getSuper_Area);
		}
		}
	 if (getRent_purchase.contains("Rent")) {
		  if (getCat_PropType.contains("Residential Land")) {

			if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
			lnr_2.setVisibility(View.GONE);
			lnr_3.setVisibility(View.GONE);
			lnr_4.setVisibility(View.GONE);
			lnr_5.setVisibility(View.GONE);
			lnr_6.setVisibility(View.GONE);
			lnr_7.setVisibility(View.GONE);
			lnr_9.setVisibility(View.GONE);

		if (!(getPlot_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" +getPlot_Area);
		}
	 }
	    		if (getRent_purchase.contains("Rent")) {
		if (getCat_PropType.contains("Independent House/Villa")) {
		et_washrooms.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
		}
	   }
	  	
	    		 if (getRent_purchase.contains("Rent")) {
	    		     if (getCat_PropType.contains("Independent/Builder Floor")) {
	    		    					Log.e(TAG, "getCat_PropType@@@=====##"+getCat_PropType);
	    		    					et_washrooms.setVisibility(View.GONE);
	    		    					lnr_5.setVisibility(View.GONE);
	    		    					lnr_7.setVisibility(View.GONE);
	    		    					lnr_8.setVisibility(View.GONE);
	    		    					lnr_9.setVisibility(View.GONE);
	    		    					if (!(getSuper_Area.isEmpty()))
	    		    						getCalculateArea = Long.parseLong("" + getSuper_Area);
	    		    			 }
	    		    				  		}	    		
	   	
	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Farmhouse")) {
		et_washrooms.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	  		}
	if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Studio Apartment")) {
		et_washrooms.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	}
	if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Service Apartment")) {
		et_washrooms.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	}
		if (getRent_purchase.contains("Rent")) {
if (getCat_PropType.contains("Residential Collaboration")) {
		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getPlot_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getPlot_Area);
		// add_price_unit.setSelection(position);
}
	}	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.equalsIgnoreCase("Other")) {

		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getPlot_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getPlot_Area);

 }
 // end of part1-Residential type

	} 
		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Commercial Office/Space")) {
		if(Utilities.D)Log.v(TAG,"---------------------4-----------------" + getPropertyType);
		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 
  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Commercial Shops")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
  		}
	if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Commercial Land/Inst. Land")) {

		if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong(getPlot_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Commercial Showrooms")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }

	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Agriculture/Farm Land")) {


		if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getPlot_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getPlot_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Industrial Lands/Plots")) {

		if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong(getPlot_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Factory")) {

		lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	lnr_9.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Warehouse")) {
		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Office in IT Park")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Hotel/Resorts")) {
		if(Utilities.D)Log.v(TAG,"---------------------2-----------------" + getPropertyType);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Guest-House/Banquet-Halls")) {

		if(Utilities.D)Log.v(TAG,"---------------------2-----------------" + getPropertyType);
		et_balconics.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Space in Retail Mall")) {

	 lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
		}
	} 	
	if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Office in Business Park")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
	}
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Business center")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Manufacturing")) {
		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Cold Storage")) {

		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }	
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Time Share")) {
		lnr_2.setVisibility(View.GONE);
		et_balconics.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Commercial Collaboration")) {

		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getPlot_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getPlot_Area);
//		// add_price_unit.setSelection(position);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Preleased Property")) {
//
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.contains("Paying Guest")) {
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
	} 	  		if (getRent_purchase.contains("Rent")) {
 if (getCat_PropType.equalsIgnoreCase("Others")) {
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		lnr_9.setVisibility(View.GONE);

		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
	}
	}
		 //////////////////SALE////////////////////////////////
		if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
			if (getCat_PropType.contains("Residential Apartment")) {
				Log.d("TAG", "getRent_purchase===" + "Sale wala");
				et_washrooms.setVisibility(View.GONE);
				lnr_5.setVisibility(View.GONE);
				lnr_7.setVisibility(View.GONE);
				lnr_8.setVisibility(View.GONE);
				if (!(getSuper_Area.isEmpty()))
					getCalculateArea = Long.parseLong("" + getSuper_Area);
			}
		}
		
		if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
	   if (getCat_PropType.contains("Residential Land")) {
			Log.d("TAG", "getRent_purchase===" + "Sale wala Land");

		if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
		lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_6.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);

	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong(getPlot_Area);
	}
} 		
		if (getRent_purchase.contains("Sale")) {
	if (getCat_PropType.contains("Independent House/Villa")|| (getRent_purchase).contains("Purchase")) {
	et_washrooms.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
	}
} 
if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {

if (getCat_PropType.contains("Independent/Builder Floor")) {
	
	et_washrooms.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
} 
if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
if (getCat_PropType.contains("Farmhouse")) {

	et_washrooms.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
} 
if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
if (getCat_PropType.contains("Studio Apartment")) {
	et_washrooms.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
}  if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
if (getCat_PropType.contains("Service Apartment")) {
	et_washrooms.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Residential Collaboration")) {
	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);

	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getPlot_Area);
	// add_price_unit.setSelection(position);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.equalsIgnoreCase("Other")) {

	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }

} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
if (getCat_PropType.contains("Commercial Office/Space")) {
	if(Utilities.D)Log.v(TAG,"---------------------4-----------------" + getPropertyType);
	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Commercial Shops")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Commercial Land/Inst. Land")) {

	if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);

if (!(getPlot_Area.isEmpty()))
	getCalculateArea = Long.parseLong(getPlot_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Commercial Showrooms")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);

 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Agriculture/Farm Land")) {


	if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getPlot_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Industrial Lands/Plots")) {

	if(Utilities.D)Log.v(TAG,"---------------------3-----------------" + getPropertyType);
	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);

if (!(getPlot_Area.isEmpty()))
	getCalculateArea = Long.parseLong(getPlot_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Factory")) {

	lnr_2.setVisibility(View.GONE);
et_balconics.setVisibility(View.GONE);
lnr_4.setVisibility(View.GONE);
lnr_5.setVisibility(View.GONE);
lnr_7.setVisibility(View.GONE);
lnr_8.setVisibility(View.GONE);

if (!(getSuper_Area.isEmpty()))
	getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Warehouse")) {
	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Office in IT Park")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Hotel/Resorts")) {
	if(Utilities.D)Log.v(TAG,"---------------------2-----------------" + getPropertyType);
	et_balconics.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Guest-House/Banquet-Halls")) {

	if(Utilities.D)Log.v(TAG,"---------------------2-----------------" + getPropertyType);
	et_balconics.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Space in Retail Mall")) {

	 lnr_2.setVisibility(View.GONE);
		lnr_3.setVisibility(View.GONE);
		lnr_4.setVisibility(View.GONE);
		lnr_5.setVisibility(View.GONE);
		lnr_7.setVisibility(View.GONE);
		lnr_8.setVisibility(View.GONE);
		if (!(getSuper_Area.isEmpty()))
			getCalculateArea = Long.parseLong("" + getSuper_Area);
		}
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Office in Business Park")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Business center")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);
	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Manufacturing")) {
	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Cold Storage")) {

	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }	
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Time Share")) {
	lnr_2.setVisibility(View.GONE);
	et_balconics.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Commercial Collaboration")) {

	lnr_2.setVisibility(View.GONE);
	lnr_3.setVisibility(View.GONE);
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_6.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);

	if (!(getPlot_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getPlot_Area);
//	// add_price_unit.setSelection(position);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Preleased Property")) {
//
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.contains("Paying Guest")) {
		lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
 }
} if (getRent_purchase.contains("Sale")|| (getRent_purchase).contains("Purchase")) {
 if (getCat_PropType.equalsIgnoreCase("Others")) {
	lnr_4.setVisibility(View.GONE);
	lnr_5.setVisibility(View.GONE);
	lnr_7.setVisibility(View.GONE);
	lnr_8.setVisibility(View.GONE);

	if (!(getSuper_Area.isEmpty()))
		getCalculateArea = Long.parseLong("" + getSuper_Area);
}

}		
		/////////////////////////////////////////////////////////
	
	// ---------------------------end ------------------------------

if(Utilities.D)Log.v(TAG,"temp_getfloorbldng============="+temp_getfloorbldng);
if (parent.getId() == R.id.spin_floor_buildng) {
	Log.d(TAG, "getfloorbldng===" + getfloorbldng);
	getfloorbldng = item;

	for (int i = 0; i < str_spn_floor_bldng.length; i++) {
		if (temp_getfloorbldng.equals(str_spn_floor_bldng[i])) {
			floor_bldng.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spn_floor_bldng[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getfloorbldng);
			temp_getfloorbldng = "";

		}
	}

}

if (parent.getId() == R.id.spin_total_bldng_floor) {
	Log.d(TAG, "getTotal_floor_bldng===" + getTotal_floor_bldng);
	getTotal_floor_bldng = item;
	for (int i = 0; i < str_spn_total_bldng_floor.length; i++) {
		if (temp_getTotal_floor_bldng
				.equals(str_spn_total_bldng_floor[i])) {
			total_floor_bldng.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spn_total_bldng_floor[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getTotal_floor_bldng);
			temp_getTotal_floor_bldng = "";

		}
	}
}

// --------------
if (parent.getId() == R.id.spin_super_unit_area) {
	Log.d(TAG, "getSuper_Unit_Area===" + getSuper_Unit_Area);
	getSuper_Unit_Area = item;
	for (int i = 0; i < str_spn_super_unit.length; i++) {
		if (temp_getSuper_Unit_Area.equals(str_spn_super_unit[i])) {
			saunit.setSelection(i);
			if(Utilities.D)Log.v(TAG,"saunit[i]=" + str_spn_super_unit[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getSuper_Unit_Area);
			temp_getSuper_Unit_Area = "";

		}
	}

	// ?????????????????
//	add_price_unit.setSelection(position);
}
if (parent.getId() == R.id.spin_carpet_unit_area) {
	Log.d(TAG, "Carpet_Unit_Area===" + getCarpet_Unit_Area);
	getCarpet_Unit_Area = item;
	for (int i = 0; i < str_spn_carpet_unit.length; i++) {
		if (temp_getCarpet_Unit_Area.equals(str_spn_carpet_unit[i])) {
			caunit.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spn_carpet_unit[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getCarpet_Unit_Area);
			temp_getCarpet_Unit_Area = "";

		}
	}
	}

if (parent.getId() == R.id.spin_plot_unit_area) {
	Log.d(TAG, "getPlot_Unit_Area===" + getPlot_Unit_Area);
	getPlot_Unit_Area = item;
	
	for (int i = 0; i < str_spn_plot_unit.length; i++) {
		if (temp_getPlot_Unit_Area.equals(str_spn_plot_unit[i])) {
			paunit.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spn_plot_unit[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getPlot_Unit_Area);
			temp_getPlot_Unit_Area = "";

		}
	}

}

if (parent.getId() == R.id.spin_pvt_oner) {
	Log.d("TAG", "getProp_ownership===" + getProp_ownership);
	getProp_ownership = item;
	
	for (int i = 0; i < str_spin_owner.length; i++) {
		if (temp_getProp_ownership.equals(str_spin_owner[i])) {
			spin_pvt_oner.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spin_owner[i]);
			if(Utilities.D)Log.v(TAG,"temp_getProp_ownership="
					+ temp_getProp_ownership);
			temp_getProp_ownership = "";

		}
	}
}
if (parent.getId() == R.id.spin_trans_type) {
	Log.d("TAG", "get_Transaction_type===" + get_Transaction_type);
	get_Transaction_type = item;
	if (getRent_purchase.contains("Rent")) {	
		Log.d(TAG, "getRent_purchaseXXX1==" + getRent_purchase);
		spin_trans_type.setSelection(1);

	}
	for (int i = 0; i < str_spin_transacion.length; i++) {
		if (temp_get_Transaction_type.equals(str_spin_transacion[i])) {
			spin_trans_type.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_trans_type[i]=" + str_spin_transacion[i]);
			if(Utilities.D)Log.v(TAG,"temp_get_Transaction_type="
					+ temp_get_Transaction_type);
			temp_get_Transaction_type = "";

		}
	}

}

if (parent.getId() == R.id.spin_avail) {
	Log.d("TAG", "getProp_availblty===" + getProp_availblty);
	getProp_availblty = item;
	for (int i = 0; i < str_spin_avalability.length; i++) {
		if (temp_getProp_availblty.equals(str_spin_avalability[i])) {
			spin_avail.setSelection(i);
			if(Utilities.D)Log.v(TAG,"spin_city[i]=" + str_spin_avalability[i]);
			if(Utilities.D)Log.v(TAG,"temp_city=" + temp_getProp_availblty);
			temp_getProp_availblty = "";

		}
	}
	}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
} 
 