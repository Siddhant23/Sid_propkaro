package com.propkaro.post;

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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.propkaro.R;
import com.propkaro.util.Utilities;

public class New_Post2 extends Fragment implements OnItemSelectedListener,OnTouchListener{

	static String TAG = New_Post2.class.getSimpleName();
	TextView prop_username, prop_date, prop_city, prop_description, prop_price,
			prop_area;
	CardView card_view_availability,card_view_req;

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
	RadioButton rb1, rb2, rb3, rb4;
	Spinner spin_prop_type;


		ImageView iv_rent,iv_sale,back1,back0;
private	 CoordinatorLayout mCoordinator;
Typeface RobotoLight;
TextView tv_from_step1, tv_1 ,tv_2,tv_rent,tv_sale ,tv_step2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.post_new_2, container, false);
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 
		 tv_1 = (TextView) rView.findViewById(R.id.tv_1); 
		 tv_2 = (TextView) rView.findViewById(R.id.tv_2); 
		 tv_rent = (TextView) rView.findViewById(R.id.tv_rent);
		 tv_sale = (TextView) rView.findViewById(R.id.tv_sale);
		 tv_step2 = (TextView) rView.findViewById(R.id.tv_step2); 

		tv_from_step1= (TextView) rView.findViewById(R.id.tv_from_step1);
		tv_from_step1.setText(getSubmitType);
	    mCoordinator = (CoordinatorLayout) rView.findViewById(R.id.root_coordinator);

		// ////////////////////////////////////////////////////////
		card_view_availability = (CardView) rView.findViewById(R.id.card_view_availability); 
		iv_rent = (ImageView) rView.findViewById(R.id.iv_rent); 
		final RelativeLayout rl_rent = (RelativeLayout) rView.findViewById(R.id.rl_rent);
		iv_sale = (ImageView) rView.findViewById(R.id.iv_sale); 

		card_view_req = (CardView) rView.findViewById(R.id.card_view_req); 
		
		 tv_sale = (TextView) rView.findViewById(R.id.tv_sale); 
		tv_sale.setText("");
		
		if(getSubmitType.contains("Requirement")){

			iv_sale.setBackgroundResource(R.mipmap.img_se_purchase1);
			tv_sale.setText("Purchase");
			getRent_purchase =  "Purchase";


    	}  else if(getSubmitType.contains("Availability")){
				iv_sale.setBackgroundResource(R.mipmap.sell_icon);
				tv_sale.setText("Sale");
				getRent_purchase =  "Sale";
    	}
		
		 RelativeLayout rl_sale = (RelativeLayout) rView.findViewById(R.id.rl_sale);
		 rl_sale.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				    	if(getSubmitType.contains("Requirement")){

							iv_sale.setBackgroundResource(R.mipmap.img_se_purchase1);
							tv_sale.setText("Purchase");
							getRent_purchase =  "Purchase";


				    	}  else if(getSubmitType.contains("Availability")){
								iv_sale.setBackgroundResource(R.mipmap.sell_icon);
								tv_sale.setText("Sale");
								getRent_purchase =  "Sale";
				    	}
							 
					Bundle args = new Bundle();

					args.putString("KEY_Rent_purchase", getRent_purchase);
					args.putString("KEY_Submit_type", getSubmitType);

					Fragment f = new New_Post3();
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
		 
		 rl_rent.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ResourceAsColor") @Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				    	getRent_purchase = "Rent"; 
//						iv_rent.setBackgroundResource(R.color.Gray);
//
//				    	iv_sale.setBackgroundResource(R.color.white);

					Bundle args = new Bundle();

					args.putString("KEY_Rent_purchase", getRent_purchase);
					args.putString("KEY_Submit_type", getSubmitType);
					Fragment f = new New_Post3();
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

				editor.commit();
				////*********************************///////////////////////////////////   
				getActivity().getSupportFragmentManager().popBackStack();

			}
		});
		back1 = (ImageView) rView.findViewById(R.id.back1);
		back1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.Snack(mCoordinator, "Please select Posting Type");


				}
		});
		setFont();
		return rView;
	}
	private void setFont() {
		tv_from_step1.setTypeface(RobotoLight);
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_step2.setTypeface(RobotoLight);
		tv_sale .setTypeface(RobotoLight);
		tv_rent .setTypeface(RobotoLight);
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
					Utilities.addFragment(getActivity(), new New_Post3(), R.id.container, true, true);

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
			getRent_purchase = item;
			getSubmitType = item;
			

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
	
		Bundle b = this.getArguments();
	
	getSubmitType = b.getString("KEY_Submit_type");
	getRent_purchase = getActivity().getIntent().getStringExtra("KEY_Rent_purchase");

	
	if(Utilities.D)Log.d(TAG, "=====getSubmitType=" + getSubmitType);
	if(Utilities.D)Log.d(TAG, "=====getRent_purchase=" + getRent_purchase);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	};
}
	