package com.propkaro.userinfo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.GridActivity;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.propertycenter.AvailPCSetter;
import com.propkaro.propertycenter.DetailsPCFActivity;
import com.propkaro.shortlist.ShortlistFragment;
import com.propkaro.util.Host;
import com.propkaro.util.SetPreference;
import com.propkaro.util.Utilities;

public class UserPCRecyclerView extends RecyclerView.Adapter<UserPCRecyclerView.ViewHolder> {
	private FragmentActivity activity;
	private Context ctx;
	private final static int FADE_DURATION = 100;
	private final static String TAG = UserPCRecyclerView.class.getSimpleName();
	SetPreference setPreference;
    List<AvailPCSetter> arrayList;      
    List<AvailPCSetter> mOriginalValues; // Original Values
    View rootView;
	SharedPreferences mPrefs;
	int location = 0;
	String getEmailID = "", getUserType = "", getUserDataID = "";
    
	public UserPCRecyclerView(FragmentActivity activity, ArrayList<AvailPCSetter> SubCategorySetter) {
		this.activity = activity;
		this.ctx = activity;
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
//    	holder.tv_rate.setText(m.getExpected_unit_price());
		
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
		
		if(m.getIsShortlisted())
			holder.iv_pin.setImageResource(R.mipmap.pin_blue);
		else
			holder.iv_pin.setImageResource(R.mipmap.ic_tab_prop_pin);
			
		holder.rl_img_temp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
//					if(arrayList.get(position).getUSER_ID() == Integer.parseInt(GridActivity.getUserDataID)){
//					} else {
//						Intent i = new Intent(activity, UserDetailsScreenFActivity.class);
//						i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getUSER_ID() + "");
//						activity.startActivity(i);
//						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//					}
				}
			}
		});
		holder.tv_postedBy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
//					if(arrayList.get(position).getUSER_ID() == Integer.parseInt(GridActivity.getUserDataID)){
//					} else {
//						Intent i = new Intent(activity, UserDetailsScreenFActivity.class);
//						i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getUSER_ID() + "");
//						activity.startActivity(i);
//						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//					}
				}
			}
		});
		holder.iv_pin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity) && m.getIsBoolEnable()){
					location = position;
					if(arrayList.get(position).getIsShortlisted())
						new RemoveShortlistTask().execute(m.getPROPERTY_ID() + "");
					else
						new AddShortlistTask().execute(m.getPROPERTY_ID() + "");
				}
			}
		});
		holder.lnr_pin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity) && m.getIsBoolEnable()){
					location = position;
					if(arrayList.get(position).getIsShortlisted())
						new RemoveShortlistTask().execute(m.getPROPERTY_ID() + "");
					else
						new AddShortlistTask().execute(m.getPROPERTY_ID() + "");
				}
			}
		});
		holder.lnr_msg.setOnClickListener(new OnClickListener() {
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
                           + m.getProperty_type_name_parent()//Residential
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
		holder.lnr_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m.getPhone_no().isEmpty()) {
					Utilities.Snack(holder.mCoordinator, "Number not found !");
				} else {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + m.getPhone_no()));
					activity.startActivity(intent);
				}
			}
		});
		holder.iv_favorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				ImageView button = (ImageView) v.findViewById(R.id.iv_favorite);
//
//				String tag = button.getTag().toString();
//				if (tag.equalsIgnoreCase("grey")) {
//					sharedPreference.addFavorite(activity, products.get(position));
//					Toast.makeText(activity,
//							activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
//
//					button.setTag("red");
//					button.setImageResource(R.drawable.heart_red);
//				} else {
//-------------------------setting preferences-----------------------------				
				setPreference = new SetPreference();
//				setPreference.removeFavorite(activity, arrayList.get(position));
//				arrayList.remove(position);
//				AvailPCRecyclerView.this.notifyDataSetChanged();
				
				if (arrayList.size() > 0) {
					setPreference = new SetPreference();
					setPreference.addFavorite(activity, arrayList.get(position));
					// if(Utilities.D)Log.v(TAG,"AvailList.get(0)=" + AvailList.get(0));
				} else
					Toast.makeText(ctx, "Something went wrong, please try again !", Toast.LENGTH_SHORT).show();
//-------------------------setting preferences-----------------------------				
//					button.setTag("grey");
//					button.setImageResource(R.drawable.heart_grey);
//					Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
//				}
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
//		setScaleAnimation(holder.itemView);
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_pc, iv_favorite, iv_pin;
    	TextView title, tv_location, tv_landmark, tv_area, tv_rate, tv_postedBy, tv_By_postedBy, tv_userType, tv_postedTime, 
    	tv_propertyType, tv_proprty_listing_parent, tv_user_id, tv_property_id, textView_pc, rent;
    	LinearLayout lnr_pin, lnr_msg, lnr_call, lnr_btnsNewsFeed;
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
    		iv_favorite.setVisibility(View.GONE);
    		iv_pin = (ImageView) itemView.findViewById(R.id.iv_pin);
    		lnr_pin = (LinearLayout) itemView.findViewById(R.id.lnr_pin);
    		lnr_pin.setVisibility(View.GONE);
    		lnr_btnsNewsFeed = (LinearLayout)itemView.findViewById(R.id.lnr_btnsNewsFeed);
    		lnr_btnsNewsFeed.setWeightSum(4);
    		lnr_msg = (LinearLayout) itemView.findViewById(R.id.lnr_msg);
    		lnr_call = (LinearLayout) itemView.findViewById(R.id.lnr_call);
    		lnr_call.setGravity(Gravity.CENTER);
    		
    		
    		rl_img_temp = (RelativeLayout)itemView.findViewById(R.id.rl_img_temp);
    		tv_proprty_listing_parent = (TextView) itemView.findViewById(R.id.tv_proprty_listing_parent);
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
//------------------------------seperatjor----------------------    
	class AddShortlistTask extends AsyncTask<String, String, String> {
		int api_code = 0, getPropertyID = 0;
		public String api_message = "", api_data;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			arrayList.get(location).setIsBoolEnable(false);
			if(arrayList.get(location).getIsShortlisted())
				arrayList.get(location).setIsShortlisted(false);
			else
				arrayList.get(location).setIsShortlisted(true);
			notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"doInBackground=======ShortlistTask");
			getPropertyID = Integer.parseInt(f_url[0]);
			try {
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				String addSHORTLIST_API = 
						"{\"table\":\"shortlist\",\"params\":{\"user_id\":\"" + 
								getUserDataID + "\",\"property_id\":\"" + 
								getPropertyID + "\",\"timestamp\":\"" + 
								timeString+"\"}}";
				
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, addSHORTLIST_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					String data = reader.getString("data");
					String ExtraUrlHit = "https://www.propkaro.com:3000/shortlist/"
							+ getPropertyID 
							+ "/%20" + GridActivity.getFName.replaceAll(" ", "")
							+ "/%20" + GridActivity.getLName.replaceAll(" ", "") 
							+ "/" + GridActivity.getUserDataID;
					Utilities.readJson(ctx, ExtraUrlHit);
				} 
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			arrayList.get(location).setIsBoolEnable(true);

			if(api_code == 200){
				arrayList.get(location).setIsShortlisted(true);
				ShortlistFragment.AvailList.add(0, arrayList.get(location));
				ShortlistFragment.adapter.notifyDataSetChanged();
				ShortlistFragment.checkEmptyList();
			} else {
				arrayList.get(location).setIsShortlisted(false);
			}
			notifyDataSetChanged();
		}
	}
	
	class RemoveShortlistTask extends AsyncTask<String, String, String> {
		int api_code = 0, getPropertyID = 0;
		public String api_message = "", api_data;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			arrayList.get(location).setIsBoolEnable(false);
			if(arrayList.get(location).getIsShortlisted())
				arrayList.get(location).setIsShortlisted(false);
			else
				arrayList.get(location).setIsShortlisted(true);
			notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... f_url) {
			getPropertyID = Integer.parseInt(f_url[0]);
			try {
				String DELETE_API = 
						"{\"table\":\"shortlist\",\"by\":{\"" 
								+ "user_id\":\"" + getUserDataID 
								+ "\", \"property_id\":\"" + getPropertyID 
								+ "\"}}";
				String UrlBase = Host.DeleteUrl + "";
				String jsonString = Utilities.sendData(UrlBase, DELETE_API);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					String data = reader.getString("data");
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			arrayList.get(location).setIsBoolEnable(true);

			if(api_code == 200){
				arrayList.get(location).setIsShortlisted(false);
			    if(activity instanceof GridActivity){
					for(int i = 0; i < ShortlistFragment.AvailList.size(); i++){
						if(ShortlistFragment.AvailList.get(i).getPROPERTY_ID() == getPropertyID){
							ShortlistFragment.AvailList.remove(i);
							ShortlistFragment.adapter.notifyDataSetChanged();
							ShortlistFragment.checkEmptyList();
							if(Utilities.D)Log.v(TAG,"onPost=======ShortlistDeleted");
						}
					}
	            }
			} else {
				arrayList.get(location).setIsShortlisted(true);
			}
			notifyDataSetChanged();
		}
	}
}
 