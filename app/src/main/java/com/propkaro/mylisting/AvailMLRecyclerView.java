package com.propkaro.mylisting;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.GridActivity;
import com.propkaro.R;
import com.propkaro.post.edit.EditPostPropertyActivity;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.propertycenter.DetailsPCFActivity;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.Host;
import com.propkaro.util.SetPreference;
import com.propkaro.util.TinyDB;
import com.propkaro.util.Utilities;

public class AvailMLRecyclerView extends RecyclerView.Adapter<AvailMLRecyclerView.ViewHolder> {
	private FragmentActivity activity;
	private Context ctx; TinyDB db;
	private static final String TAG = AvailMLRecyclerView.class.getSimpleName();
	SetPreference setPreference;
    List<AvailPCSetter> arrayList;
    List<AvailPCSetter> mOriginalValues; // Original Values
    View rootView;
	SharedPreferences mPrefs;
	int location = 0;
	String getEmailID = "", getUserType = "", getUserDataID = "";
    
	public AvailMLRecyclerView(FragmentActivity activity, ArrayList<AvailPCSetter> SubCategorySetter) {
		this.activity = activity;
		this.ctx = activity;
		this.db = new TinyDB(ctx);
		this.arrayList = SubCategorySetter;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
	}
	
	@Override
	public int getItemCount() {
		return arrayList.size();
	}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

    	rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.property_center_list, null);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
		final AvailPCSetter m = arrayList.get(position);

//		try {
//			File file = ImageLoader.getInstance().getDiscCache().get(m.getThumbnailUrl());
//			if(Utilities.D)Log.v(TAG,"" + file.exists());
//
//			if (!file.exists()) {
//				ImageLoader.getInstance().displayImage(m.getThumbnailUrl(), imageView_pc, AvailPCFragment.animateFirstListener);
//			} else {
//				imageView_pc.setImageURI(Uri.parse(file.getAbsolutePath()), AvailPCFragment.animateFirstListener);
//			}
//		} catch (Exception e) { e.printStackTrace(); }

		ImageLoader.getInstance().displayImage(m.getThumbnailUrl(), holder.imageView_pc, AvailPCFragment.animateFirstListener);
		
		holder.textView_pc.setText("");
		holder.textView_pc.setVisibility(View.GONE);
    	
    	if(m.getThumbnailUrl().contains("default")){
    		holder.textView_pc.setVisibility(View.VISIBLE);
    		if((m.getUse_company_name() + "").contains("1")){
        		if(m.getTv_postedBy().length() > 0){
        			holder.textView_pc.getBackground().setColorFilter(m.getColor(), PorterDuff.Mode.SRC_IN);
//        			textView_pc.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, Utilities.getRandomColor(activity)));
        			holder.textView_pc.setText(String.valueOf(m.getTv_postedBy().charAt(0)).toUpperCase());
        		} else {
        			holder.textView_pc.setVisibility(View.GONE);
        		}
    		} else{
    			if(m.getTv_postedBy().length() > 0){
    				holder.textView_pc.getBackground().setColorFilter(m.getColor(), PorterDuff.Mode.SRC_IN);
//      			textView_pc.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, Utilities.getRandomColor(activity)));
    				holder.textView_pc.setText(String.valueOf(m.getTv_postedBy().charAt(0)).toUpperCase());
    			} else {
    				holder.textView_pc.setVisibility(View.GONE);
    			}
    		}
        }
		
    	holder.tv_user_id.setText(m.getUSER_ID() + "");
    	holder.tv_property_id.setText(m.getPROPERTY_ID() + "");
		
    	holder.tv_location.setText(m.getProperty_city());
    	holder.tv_landmark.setText(Utilities.makeFirstLetterCaps(m.getProperty_landmark()));
    	holder.tv_area.setText(m.getTv_area());
    	holder.tv_rate.setText(m.getTv_rate());
		
		if((m.getUse_company_name() + "").contains("1")){
			holder.tv_postedBy.setText(Utilities.makeFirstLetterCaps(m.getCompany_name()+""));
			holder.tv_By_postedBy.setVisibility(View.GONE);
			holder.tv_By_postedBy.setText("By - " + Utilities.makeFirstLetterCaps(m.getTv_postedBy()));
//			if(Utilities.D)Log.v(TAG,"#########check#########m.getCompany_name()"+m.getUse_company_name()+"="+m.getCompany_name());
		} else{
			holder.tv_postedBy.setText(Utilities.makeFirstLetterCaps(m.getTv_postedBy()+""));
//			if(Utilities.D)Log.v(TAG,"#########check#########m.getTv_postedBy()"+m.getUse_company_name()+"="+m.getTv_postedBy());
		}
		
		try {
			String temp_date = " -- ";
//			SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateTime = sdf2.parse("" + m.getTv_postedTime());
//			temp_date = "" + sdf1.format(dateTime);
			holder.tv_postedTime.setText(Utilities.getTimeAgo(dateTime, ctx));
		} catch (Exception e) { e.printStackTrace(); }
		
		holder.tv_userType.setText(Utilities.makeFirstLetterCaps(m.getTv_userType()));
		holder.tv_proprty_listing_parent.setText(m.getTv_proprty_listing_parent());
//		holder.tv_propertyType.setText(m.getProperty_type_name_parent());
//		
//		String str = m.getTv_proprty_listing_parent() + " of " + m.getProperty_type_name_parent() 
//				+ " for " 
//				+ m.getProperty_listing_type() + " in " + m.getProperty_landmark();
			
		String str2 = m.getProperty_listing_type();

		if(m.getProperty_listing_type().contains("Sale")){
			final SpannableStringBuilder sb2 = new SpannableStringBuilder(str2);
			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(206, 147, 34));
			final BackgroundColorSpan fcs2 = new BackgroundColorSpan(R.drawable.linearlayout_bg_prop_center_gold);
			holder.rent.setBackgroundResource(R.drawable.linearlayout_bg_prop_center_gold);

			final StyleSpan bss = new StyleSpan(Typeface.BOLD);
			sb2.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			sb2.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			holder.rent.setText(sb2);

		} else if(m.getProperty_listing_type().contains("Rent")){
//			if(Utilities.D)Log.v(TAG,"get--getTv_proprty_listing_parent=="+m.getTv_proprty_listing_parent());
			final SpannableStringBuilder sb2 = new SpannableStringBuilder(str2);
			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 171, 93));
			final BackgroundColorSpan fcs2 = new BackgroundColorSpan(R.drawable.linearlayout_bg_prop_center);
			holder.rent.setBackgroundResource(R.drawable.linearlayout_bg_prop_center);

			final StyleSpan bss = new StyleSpan(Typeface.BOLD);
			sb2.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			sb2.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			holder.rent.setText(sb2);
			
		} else if(m.getProperty_listing_type().contains("Purchase")){
			final SpannableStringBuilder sb2 = new SpannableStringBuilder(str2);
			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(34, 94, 206));
			final BackgroundColorSpan fcs2 = new BackgroundColorSpan(R.drawable.linearlayout_bg_prop_center_blue);
			holder.rent.setBackgroundResource(R.drawable.linearlayout_bg_prop_center_blue);

			final StyleSpan bss = new StyleSpan(Typeface.BOLD);
			sb2.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			sb2.setSpan(bss, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 

			holder.rent.setText(sb2);
		}
		
		String str = m.getTv_proprty_listing_parent();
		String rent_sale = "";
		
		if(m.getTv_proprty_listing_parent().equals("Requirement")){
//			final SpannableStringBuilder sb = new SpannableStringBuilder(str);
//			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 0, 0));
//			final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); 
//			sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
//			sb.setSpan(bss, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
//			title.setText(sb);
			final SpannableStringBuilder sb = new SpannableStringBuilder(str);
			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(247,39,55));
			final BackgroundColorSpan fcs2 = new BackgroundColorSpan(R.drawable.linearlayout_bg_prop_center_red);
			  holder.title.setBackgroundResource(R.drawable.linearlayout_bg_prop_center_red);
			  
			final StyleSpan bss = new StyleSpan(Typeface.BOLD);
			sb.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			sb.setSpan(bss, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			
			holder.title.setText(sb);
			rent_sale = " • " + " To " + m.getProperty_listing_type();

		} else if(m.getTv_proprty_listing_parent().equals("Availability")){
//			if(Utilities.D)Log.v(TAG,"get--getTv_proprty_listing_parent=="+m.getTv_proprty_listing_parent());
			final SpannableStringBuilder sb = new SpannableStringBuilder(str);
			final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 171, 93));
			final BackgroundColorSpan fcs2 = new BackgroundColorSpan(R.drawable.linearlayout_bg_prop_center);
			  holder.title.setBackgroundResource(R.drawable.linearlayout_bg_prop_center);

			final StyleSpan bss = new StyleSpan(Typeface.BOLD);
			sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
			sb.setSpan(bss, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 

			holder.title.setText(sb);
			rent_sale = " • " + " For " + m.getProperty_listing_type();
		}
		String floor = " • "  + m.getProperty_floor() +  " Floor ";
		if(Utilities.isStringInt(m.getProperty_floor())) {
			floor = " • "  + Utilities.ordinal(Integer.parseInt(m.getProperty_floor())) +  " Floor ";
		}
		String bedrooms = " • " + m.getNo_of_bedrooms() +  " BHK " ;
		if(m.getProperty_floor().length() < 1 || m.getProperty_floor().contains("--"))
			floor = "";
		if(m.getNo_of_bedrooms().length() < 1 || m.getNo_of_bedrooms().contains("--"))
			bedrooms = "";
		holder.tv_propertyType.setText(                           
				m.getProperty_type_name()
				+  rent_sale
				+ floor
				+ bedrooms
				);
//--------------------------------Set onclicking listener actions-----------------------------    	
		holder.rl_img_temp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
					if(arrayList.get(position).getUSER_ID() == Integer.parseInt(GridActivity.getUserDataID)){
					} else {
						Intent i = new Intent(activity, UserInfoFActivity.class);
						i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getUSER_ID() + "");
						activity.startActivity(i);
						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
				}
			}
		});
		holder.tv_postedBy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
					if(arrayList.get(position).getUSER_ID() == Integer.parseInt(GridActivity.getUserDataID)){
					} else {
						Intent i = new Intent(activity, UserInfoFActivity.class);
						i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getUSER_ID() + "");
						activity.startActivity(i);
						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
				}
			}
		});
		
		holder.lnr_btnsNewsFeed.setVisibility(View.GONE);
		holder.lnr_tabsBottomMyListing.setVisibility(View.VISIBLE);
		
		holder.lnr_share_listing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PackageManager pm = activity.getPackageManager();
				try {
					Intent waIntent = new Intent(Intent.ACTION_SEND);
					waIntent.setType("text/plain");

					String text = 
							"\uD83D\uDC49"
							+ " "
                           + m.getTv_proprty_listing_parent()//Availability  \uD83D for pin red
                           + " for "
                           + (m.getProperty_listing_type()//Sale
//                           +"\u260e"
                           + " \n"
                           + " \n"
//                           +"\uDCCC"
                           + "\uD83C\uDFE0"
                           + " "
                           + m.getProperty_type_name()//Residential
                           + " \n"
                           + " \n"
                           +"\uD83D\uDCB0"
                           + " "
		                       + m.getTv_rate()
//		                       + " @ "
//		                       + m.getAdd_price()
//		                       + " / "
//		                       + m.getAdd_price_unit()
                           + " \n"
                           + " \n"
//                           + " of "
                           + "\uD83D\uDCCC"
                           + " "
                           + m.getProperty_landmark()
                           + " \n"
                           + " \n"
                           +"\u260e"
                           + " "
                           + "Call : "
                           + " "
                           + m.getTv_postedBy()
                           + " "
                           + "@ "
                           + m.getPhone_no()
                           + "\n"
                           + " \nhttp://bit.ly/PKshare"
	                   );
//					PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
					// Check if package exists or not. If not then code
					// in catch block will be called
//					waIntent.setPackage("com.whatsapp");

					waIntent.putExtra(Intent.EXTRA_TEXT, text);
					activity.startActivity(Intent.createChooser(waIntent, "Share with"));

				} catch (Exception e) {e.printStackTrace();}
//				} catch (NameNotFoundException e) {
//					Utilities.Snack(holder.mCoordinator, "WhatsApp not Installed !");
			}
		});
		
    	holder.lnr_edit_property.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.e(TAG, "m.getPROPERTY_ID())="+m.getPROPERTY_ID());

				Intent i = new Intent(activity, EditPostPropertyActivity.class);

				// i.putExtra("KEY_USER_ID", user_id);
				// i.putExtra("KEY_url_thumbnail", url_thumbnail);
				// i.putExtra("KEY_url_image", url_image);
				//

				// ///////////post 1////////////////////////
				i.putExtra("KEY_PropertyID", ("" + m.getPROPERTY_ID()));
				i.putExtra("KEY_PropertyImage", m.getThumbnailUrl());

				i.putExtra("KEY_api_latitude", m.getProperty_latitude()+"");
				i.putExtra("KEY_api_longitude", m.getProperty_longitude()+"");

				i.putExtra("KEY_Submit_type", m.getTv_proprty_listing_parent());
				i.putExtra("KEY_Rent_purchase", m.getProperty_listing_type());
				i.putExtra("KEY_Cat_PropType", m.getProperty_type_name());
				i.putExtra("KEY_Property_type", m.getProperty_type_name_parent());
				i.putExtra("KEY_ProjectName_type", m.getProperty_location());
				i.putExtra("KEY_City_type", m.getProperty_city());
				i.putExtra("KEY_Location_type", m.getProperty_landmark());

				// ///////////post 2////////////////////////

				i.putExtra("KEY_property_area", m.getProperty_area());
				i.putExtra("KEY_getBathrooms", m.getNo_of_bathrooms());
				i.putExtra("KEY_getBedrooms", m.getNo_of_bedrooms());
				i.putExtra("KEY_getBalconies", m.getNo_of_balcony());
				i.putExtra("KEY_getWashrooms", m.getNo_of_washrooms());
				i.putExtra("KEY_property_super_area", m.getProperty_super_area());
				i.putExtra("KEY_property_carpet_area", m.getProperty_built_area());
				i.putExtra("KEY_property_plot_area", m.getProperty_plot_area());

				i.putExtra("KEY_getSuper_Unit_Area", m.getProperty_super_area_unit());
				i.putExtra("KEY_getCarpet_Unit_Area", m.getProperty_built_area_unit());
				i.putExtra("KEY_getPlot_Unit_Area", m.getProperty_plot_area_unit());

				i.putExtra("KEY_getfloorbldng", m.getProperty_floor());
				i.putExtra("KEY_getTotal_floor_bldng", m.getBuilding_total_floors());

				Log.e(TAG, "getExpected_price==============" + m.getExpected_unit_price());
				
				i.putExtra("KEY_getAbsolut_prce", Long.parseLong(m.getExpected_unit_price()));
				i.putExtra("KEY_add_price", m.getAdd_price());
				i.putExtra("KEY_getAdd_prce_area_unit", m.getAdd_price_unit());

				i.putExtra("KEY_getMntnce_chrg", m.getProperty_maintenance_amount());
				i.putExtra("KEY_getFreq", m.getProperty_maintenance_frequency());

				// ///////////post 3////////////////////////

				i.putExtra("KEY_property_possession", m.getProperty_possession());
				i.putExtra("KEY_furnishing_type", m.getProperty_furnishing_type());
				i.putExtra("KEY_Transaction_type", m.getTransaction_type());
				i.putExtra("KEY_Prop_ownership", m.getProperty_ownership());
				i.putExtra("KEY_Prop_availblty", m.getProperty_availability());
				i.putExtra("KEY_property_description", m.getProperty_description());
				i.putExtra("KEY_property_title", m.getTv_title());
				i.putExtra("KEY_Amenities", m.getAmenities());

				activity.startActivity(i);
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				activity.finish();
			}
		});
    	holder.lnr_repost_property.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG,"lnr_repost_property=====");
				Toast.makeText(ctx, "Reposting Property..", Toast.LENGTH_SHORT).show();
				if(Utilities.isInternetOn(ctx))
					new RepostPropertyTask().execute(m.getPROPERTY_ID() + "");
			}
		});
    	holder.lnr_delete_property.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG,"lnr_delete_property=====");
		        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		        alertDialogBuilder.setTitle("Alert");
		        alertDialogBuilder.setMessage("Are you sure to delete this property ?")
		        .setCancelable(true)
		        .setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int id){
		            	dialog.cancel();
		            	
						if(Utilities.isInternetOn(ctx))
							new DeletePropertyTask().execute(m.getPROPERTY_ID() + "");
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
		});
		
		holder.itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(Utilities.isInternetOn(activity)){
					int _temp = (arrayList.get(position).getUSER_ID());
					Intent i = new Intent(activity, DetailsPCFActivity.class);
					i.putExtra("KEY_PROPERTY_ID", (m.getPROPERTY_ID() + ""));
					activity.startActivity(i);
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//					if(Utilities.D)Log.v(TAG,"SubCategorySetter.get(position)=" + arrayList.get(position));
				}
			}
		});
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_pc, iv_favorite, iv_pin;
    	TextView title, tv_location, tv_landmark, tv_area, tv_rate, tv_postedBy, tv_By_postedBy, tv_userType, tv_postedTime, 
    	tv_propertyType, tv_proprty_listing_parent, tv_user_id, tv_property_id, textView_pc, rent;
    	LinearLayout lnr_pin, lnr_msg, lnr_call, lnr_btnsNewsFeed, lnr_tabsBottomMyListing, 
    	lnr_share_listing, lnr_edit_property, lnr_repost_property, lnr_delete_property;
    	RelativeLayout rl_img_temp;
        CoordinatorLayout mCoordinator;
    	
        public ViewHolder(View itemView) {
            super(itemView);
            
            Typeface RobotoLight = Typeface.createFromAsset(this.itemView.getContext().getAssets(),"fonts/Roboto-Light.ttf"); 
            mCoordinator = (CoordinatorLayout) itemView.findViewById(R.id.root_coordinator);
    		textView_pc = (TextView) itemView.findViewById(R.id.textView_pc);
    		imageView_pc = (ImageView) itemView.findViewById(R.id.imageView_pc);
    		title = (TextView) itemView.findViewById(R.id.title);
    		rent = (TextView) itemView.findViewById(R.id.rent);
    		tv_user_id = (TextView) itemView.findViewById(R.id.tv_user_id);
    		tv_property_id = (TextView) itemView.findViewById(R.id.tv_proprty_id);
    		iv_favorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
    		tv_location = (TextView) itemView.findViewById(R.id.tv_location);
    		tv_landmark = (TextView) itemView.findViewById(R.id.tv_landmark);
    		tv_area = (TextView) itemView.findViewById(R.id.tv_area);
    		tv_rate = (TextView) itemView.findViewById(R.id.tv_rate);
    		tv_postedBy = (TextView) itemView.findViewById(R.id.tv_postedBy);
    		tv_By_postedBy = (TextView) itemView.findViewById(R.id.tv_By_postedBy);
    		tv_By_postedBy.setVisibility(View.GONE);
    		tv_userType = (TextView) itemView.findViewById(R.id.tv_userType);
    		tv_postedTime = (TextView) itemView.findViewById(R.id.tv_postedTime);
    		tv_propertyType = (TextView) itemView.findViewById(R.id.tv_propertyType);

    		tv_proprty_listing_parent = (TextView) itemView.findViewById(R.id.tv_proprty_listing_parent);
    		iv_favorite.setVisibility(View.GONE);
    		iv_pin = (ImageView) itemView.findViewById(R.id.iv_pin);
    		
    		lnr_btnsNewsFeed = (LinearLayout) itemView.findViewById(R.id.lnr_btnsNewsFeed);
    		lnr_pin = (LinearLayout) itemView.findViewById(R.id.lnr_pin);
    		lnr_msg = (LinearLayout) itemView.findViewById(R.id.lnr_msg);
    		lnr_call = (LinearLayout) itemView.findViewById(R.id.lnr_call);
    		lnr_tabsBottomMyListing = (LinearLayout) itemView.findViewById(R.id.lnr_tabsBottomMyListing);

    		lnr_share_listing = (LinearLayout) itemView.findViewById(R.id.lnr_share_listing);
    		lnr_edit_property = (LinearLayout) itemView.findViewById(R.id.lnr_edit_property);
    		lnr_repost_property = (LinearLayout) itemView.findViewById(R.id.lnr_repost_property);
    		lnr_delete_property = (LinearLayout) itemView.findViewById(R.id.lnr_delete_property);
    		rl_img_temp = (RelativeLayout) itemView.findViewById(R.id.rl_img_temp);
    		
    		title.setTypeface(RobotoLight);
    		rent.setTypeface(RobotoLight);
    		tv_user_id .setTypeface(RobotoLight);
    		tv_property_id .setTypeface(RobotoLight);
    		tv_location.setTypeface(RobotoLight);
    		tv_landmark .setTypeface(RobotoLight);
    		tv_area .setTypeface(RobotoLight);
    		tv_rate.setTypeface(RobotoLight);
    		tv_postedBy .setTypeface(RobotoLight);
    		tv_userType.setTypeface(RobotoLight);
    		tv_postedTime.setTypeface(RobotoLight);
    		tv_propertyType .setTypeface(RobotoLight);
    		tv_proprty_listing_parent .setTypeface(RobotoLight);
    		
        }
    }
	class RepostPropertyTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... f_url) {
			Date date = new Date();
			String timeString = new Timestamp(date.getTime()).toString();

			try {
				String jsonData = "{\"table\":\"properties\",\"by\":{\"property_id\":\""+
						f_url[0] + "\"},\"params\":{\"property_added_on\":\""+
						timeString + "\"}}";
//				String jsonData = "{\"table\":\"properties\",\"by\":{\"property_id\":\""+
//						(AvailList.get(0).getPROPERTY_ID() + "")+"\"}}";
				String jsonString = Utilities.sendData(Host.UpdateUrl, jsonData);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
//					JSONObject data = (JSONObject) reader.get("data");
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return api_message;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);

			db.putBoolean("shouldReload", true);
			Toast.makeText(ctx, "Property Reposted: " + api_message, Toast.LENGTH_SHORT).show();
		}
	}
	class DeletePropertyTask extends AsyncTask<String, String, String> {
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
				String jsonData = "{\"table\":\"properties\",\"by\":{\"property_id\":\"" + f_url[0] + "\"}}";
				String jsonString = Utilities.sendData(Host.DeleteUrl, jsonData);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
//					JSONObject data = (JSONObject) reader.get("data");
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return api_message;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			if(api_code == 200){
				db.putBoolean("shouldReload", true);
				Toast.makeText(ctx, "Property Deleted !", Toast.LENGTH_SHORT).show();
				activity.finish(); activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		}
	}
}
 