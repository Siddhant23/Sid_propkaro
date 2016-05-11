package com.propkaro.timeline;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.propertycenter.DetailsPCFActivity;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.SetPreference;
import com.propkaro.util.Utilities;
public class MyTimelineAdapter extends BaseAdapter {
	private static final String TAG = MyTimelineAdapter.class.getSimpleName();
	Activity activity;
	LayoutInflater inflater;
	SetPreference setPreference;
    List<MyTimelineSetter> arrayList;
    List<MyTimelineSetter> mOriginalValues;
	ImageView menu_delete, menu_deleteProperty;
	Dialog customdialog, shareDialog;
	EditText et_commentSomething;
	Button btn_commentSomething;
	
	TextView btn_like, btn_comment, btn_share;
	TextView temp_like, temp_comment, temp_share;

	Button btn_likeProperty, btn_commentProperty, btn_shareProperty;
	EditText et_textSomething;		
	
	String str_shareContent = "";
	int location = 0;

	private boolean isLoadingMore = false;
	private int total_pages = 0, current_page = 1, lastInScreen = 0, mPrevTotalItemCount = 0, totalItemCount = 0;
	
	String COMMENT_API = "";
	String getLastCommentId = "";
	CommentAdapter commentAdapter;
	List<MyTimelineSetter> commentList = new ArrayList<MyTimelineSetter>();
	ListView commentListview;
	CircularProgressView progressView;

	TextView txtTotalLikes, tv_TotalLikesProperty;
	boolean isExecuteMore = true;
	SharedPreferences mPrefs;
	String getMobile_num = "", getEmailID = "", getUserType = "", getCity = "", getUserDataID = "",  
			getUserName = "", getFName = "", getLName = "", getProfileImage = "", getHalfImage = "";

	public MyTimelineAdapter(Activity activity, List<MyTimelineSetter> SubCategorySetter) {
		this.activity = activity;
		this.arrayList = SubCategorySetter;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		
		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
		getHalfImage = mPrefs.getString("PREF_KEY_HALF_IMAGE", "");
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int location) {
		return arrayList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public boolean hasStableIds(){ 
		return true; 
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.adapter_timeline, null);
//-----------------------LAYOUT FOR SHARE-----------------------------------
		final MyTimelineSetter item = arrayList.get(position);
		
		TextView name = (TextView) convertView.findViewById(R.id.name);
		name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(arrayList.get(position).getPosted_by().equals(MyTimelineFragment.getUserDataID)){
				} else {
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
					activity.startActivity(i); activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
			
		menu_delete = (ImageView)convertView.findViewById(R.id.menu_delete);
		if((item.getPosted_by() + "").equals(MyTimelineFragment.getUserDataID)){
			menu_delete.setVisibility(View.VISIBLE);
		} else {
			menu_delete.setVisibility(View.GONE);
		}
		menu_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPopupDeleteClick(v, position);
			}
		});
		menu_deleteProperty = (ImageView)convertView.findViewById(R.id.menu_deleteProperty);
		if((item.getPosted_by() + "").equals(MyTimelineFragment.getUserDataID)){
			menu_deleteProperty.setVisibility(View.VISIBLE);
		} else {
			menu_deleteProperty.setVisibility(View.GONE);
		}
		menu_deleteProperty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPopupDeleteClick(v, position);
			}
		});
				
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent);

		LinearLayout lnr_shareContent = (LinearLayout) convertView.findViewById(R.id.lnr_shareContent);
		TextView txtShareContent = (TextView) convertView.findViewById(R.id.txtShareContent);
		TextView tv_nameShare = (TextView) convertView.findViewById(R.id.tv_nameShare);
		ImageView iv_sharedContent = (ImageView) convertView.findViewById(R.id.iv_sharedContent);

		TextView txtStatusMsg1 = (TextView) convertView.findViewById(R.id.txtStatusMsg1);
		TextView txtStatusMsg11 = (TextView) convertView.findViewById(R.id.txtStatusMsg11);
		ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);
//		FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

		profilePic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(arrayList.get(position).getPosted_by().equals(MyTimelineFragment.getUserDataID)){
				} else {
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
					activity.startActivity(i); activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		
		txtTotalLikes = (TextView)convertView.findViewById(R.id.txtTotalLikes);
		
		btn_like = (TextView)convertView.findViewById(R.id.btn_like);
		btn_like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
					
					temp_like = (TextView)v;
					location = position;
					
					if(arrayList.get(location).getLike().equals("dislike")){

						temp_like.setTextColor(Color.parseColor("#99000000"));
						new DisLikeTask().execute(item.getActivity_id() + "");
						if(Utilities.D)Log.v(TAG,"DisLiking this post.."+arrayList.get(position).getLike());
					} else if(arrayList.get(location).getLike().equals("like")) {

						temp_like.setTextColor(Color.parseColor("#FF0000FF"));
						new LikeTask().execute(item.getActivity_id() + "");
						if(Utilities.D)Log.v(TAG,"Liking this post.."+arrayList.get(position).getLike());
					} 
				}
			}
		});
		tv_TotalLikesProperty = (TextView)convertView.findViewById(R.id.tv_TotalLikesProperty);
		
		btn_likeProperty = (Button)convertView.findViewById(R.id.btn_likeProperty);
		btn_likeProperty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
					
					temp_like = (TextView)v;
					location = position;
					
					if(arrayList.get(location).getLike().equals("dislike")){

						temp_like.setTextColor(Color.parseColor("#99000000"));
						new DisLikeTask().execute(item.getActivity_id() + "");
						if(Utilities.D)Log.v(TAG,"DisLiking this post.." + arrayList.get(position).getLike());
					} else if(arrayList.get(location).getLike().equals("like")) {

						temp_like.setTextColor(Color.parseColor("#FF0000FF"));
						new LikeTask().execute(item.getActivity_id() + "");
						if(Utilities.D)Log.v(TAG,"Liking this post.."+arrayList.get(position).getLike());
					} 
				}
			}
		});
		btn_comment = (TextView)convertView.findViewById(R.id.btn_comment);
		btn_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialogComment(v, position, item.getComments());
			}
		});
		btn_commentProperty = (Button)convertView.findViewById(R.id.btn_commentProperty);
		btn_commentProperty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialogComment(v, position, item.getComments());
			}
		});
//---------------------		

		btn_share = (TextView)convertView.findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				onPopupShareClick(v, position);
			}
		});
		btn_shareProperty = (Button)convertView.findViewById(R.id.btn_shareProperty);
		btn_shareProperty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				onPopupShareClick(v, position);
			}
		});
		
		//-----------------------LAYOUT FOR CONNECT TO -----------------------------------
		TextView tv_connectTo_name = (TextView) convertView.findViewById(R.id.tv_connectTo_name);
		tv_connectTo_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, UserInfoFActivity.class);
				i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
				activity.startActivity(i);  activity.finish();
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		TextView tv_connectTo_content = (TextView) convertView.findViewById(R.id.tv_connectTo_content);
		TextView tv_connectFrom_name = (TextView) convertView.findViewById(R.id.tv_connectFrom_name);
		TextView timestamp_connectTo = (TextView) convertView.findViewById(R.id.timestamp_connectTo);
		ImageView profilePic_connectTo = (ImageView) convertView.findViewById(R.id.profilePic_connectTo);
		profilePic_connectTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(arrayList.get(position).getPosted_by().equals(MyTimelineFragment.getUserDataID)){
				} else {
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
					activity.startActivity(i); activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		//-----------------------LAYOUT FOR CONNECT FROM-----------------------------------
		//-----------------------LAYOUT FOR PROPERTY --------------------------------------
		ImageView profilePic_property = (ImageView) convertView.findViewById(R.id.profilePic_property);

		profilePic_property.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(arrayList.get(position).getPosted_by().equals(MyTimelineFragment.getUserDataID)){
				} else {
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
					activity.startActivity(i);  activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		
		TextView tv_nameProperty = (TextView) convertView.findViewById(R.id.tv_nameProperty);
		tv_nameProperty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(arrayList.get(position).getPosted_by().equals(MyTimelineFragment.getUserDataID)){
				} else {
					Intent i = new Intent(activity, UserInfoFActivity.class);
					i.putExtra("KEY_GET_USER_ID", arrayList.get(position).getPosted_by());
					activity.startActivity(i);  activity.finish();
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		TextView tv_contentProperty = (TextView) convertView.findViewById(R.id.tv_contentProperty);
		TextView tv_ShareContent_Property = (TextView) convertView.findViewById(R.id.tv_ShareContent_Property);
		TextView timestamp_property = (TextView) convertView.findViewById(R.id.timestamp_property);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		
		TextView tv_user_id = (TextView) convertView.findViewById(R.id.tv_user_id);
		TextView tv_property_id = (TextView) convertView.findViewById(R.id.tv_proprty_id);
		TextView tv_location = (TextView) convertView.findViewById(R.id.tv_location);
		TextView tv_landmark = (TextView) convertView.findViewById(R.id.tv_landmark);
		TextView tv_area = (TextView) convertView.findViewById(R.id.tv_area);
		TextView tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
		TextView tv_postedBy = (TextView) convertView.findViewById(R.id.tv_postedBy);
		TextView tv_userType = (TextView) convertView.findViewById(R.id.tv_userType);
		TextView tv_postedTime = (TextView) convertView.findViewById(R.id.tv_postedTime);
		TextView tv_propertyType = (TextView) convertView.findViewById(R.id.tv_propertyType);
		ImageView iv_favorite = (ImageView) convertView.findViewById(R.id.iv_favorite);
		iv_favorite.setVisibility(View.GONE);
		
		TextView tv_proprty_listing_parent = (TextView) convertView.findViewById(R.id.tv_proprty_listing_parent);
		//-----------------------LAYOUT -----------------------------------------------------
		
		LinearLayout lnr_share = (LinearLayout)convertView.findViewById(R.id.lnr_share);
		LinearLayout lnr_connect_to = (LinearLayout)convertView.findViewById(R.id.lnr_connect_to);
		LinearLayout lnr_property = (LinearLayout)convertView.findViewById(R.id.lnr_property);

		int getActivityTypeId = Integer.parseInt(item.getActivity_type_id());
		String type = item.getType();
		String type_name = item.getType_name();

		btn_like.setText("Like");
		btn_likeProperty.setText("Like");
		
		if(isExecuteMore){

			if(item.getActivity_type_id().contains("1")){
				if(item.getLike().equals("dislike")){
					btn_likeProperty.setTextColor(Color.parseColor("#FF0000FF"));
				} else {
					btn_likeProperty.setTextColor(Color.parseColor("#99000000"));
				}
				
				if(item.getBool_hasLike()){
					btn_likeProperty.setTextColor(Color.parseColor("#FF0000FF"));
				}
				
				if(Integer.parseInt(item.getTotal_likes()) < 1){
					tv_TotalLikesProperty.setVisibility(View.GONE);
				} else if(Integer.parseInt(item.getTotal_likes()) == 1) {
					tv_TotalLikesProperty.setVisibility(View.VISIBLE);
					tv_TotalLikesProperty.setText(item.getTotal_likes() + " " + "Like");
				} else {
					tv_TotalLikesProperty.setVisibility(View.VISIBLE);
					tv_TotalLikesProperty.setText(item.getTotal_likes() + " " + "Likes");
				}
				
				if(item.getBool_enable()){
					btn_likeProperty.setEnabled(true);
					menu_deleteProperty.setEnabled(true);
				} else {
					btn_likeProperty.setEnabled(false);
					menu_deleteProperty.setEnabled(false);
				}
			}
//-------------------------
			if(item.getActivity_type_id().contains("2")){
				if(item.getLike().equals("dislike")){
					btn_like.setTextColor(Color.parseColor("#FF0000FF"));
				} else {
					btn_like.setTextColor(Color.parseColor("#99000000"));
				}

				if(item.getBool_hasLike()){
					btn_like.setTextColor(Color.parseColor("#FF0000FF"));
				}
				
				if(Integer.parseInt(item.getTotal_likes()) < 1){
					txtTotalLikes.setVisibility(View.GONE);
				} else if(Integer.parseInt(item.getTotal_likes()) == 1) {
					txtTotalLikes.setVisibility(View.VISIBLE);
					txtTotalLikes.setText(item.getTotal_likes() + " " + "Like");
				} else {
					txtTotalLikes.setVisibility(View.VISIBLE);
					txtTotalLikes.setText(item.getTotal_likes() + " " + "Likes");
				}				
				
				if(item.getBool_enable()){
					btn_like.setEnabled(true);
					menu_delete.setEnabled(true);
				} else {
					btn_like.setEnabled(false);
					menu_delete.setEnabled(false);
				}
				
			}
			isExecuteMore = true;
		}
//-------------------------------------SETTING DATA-------------------------------------
		if(getActivityTypeId == 1){//PROPERTY
			lnr_property.setVisibility(View.VISIBLE);
			lnr_connect_to.setVisibility(View.GONE);
			lnr_share.setVisibility(View.GONE);

//			LayoutInflater inflate_header = LayoutInflater.from(activity);
//			View inflatedLayout= inflate_header.inflate(R.layout.adapter_timeline_header, null, false);
//			lnr_property.addView(inflatedLayout);
			
			try {
			JSONObject obj = new JSONObject(item.getProperty_details());
//			for (int i = 0; i < reader.length(); i++) {
//				JSONObject obj = reader.getJSONObject(i);
			if(item.getImage().contains("http"))
				ImageLoader.getInstance().displayImage(item.getImage(), profilePic_property, AvailPCFragment.animateFirstListener);
			else
				ImageLoader.getInstance().displayImage(Host.MainUrl + item.getImage(), profilePic_property, AvailPCFragment.animateFirstListener);

			//----------------
			TextView textView_pc = (TextView) convertView.findViewById(R.id.tv_profilePic_property);
	    	textView_pc.setText("");
	    	textView_pc.setVisibility(View.GONE);
	    	
	    	if(item.getImage().contains("default")){
    			if(item.getFname().length() > 0){
    	    		textView_pc.setVisibility(View.VISIBLE);
        			textView_pc.getBackground().setColorFilter(item.getColor(), PorterDuff.Mode.SRC_IN);
    				textView_pc.setText(String.valueOf(item.getFname().charAt(0)).toUpperCase());
    			} else {
    				textView_pc.setVisibility(View.GONE);
    			}
	    	}
			//----------------
			tv_nameProperty.setText(Utilities.makeFirstLetterCaps(item.getFname()) 
					+ " " + Utilities.makeFirstLetterCaps(item.getLname()));
			
			if(Integer.parseInt(item.getShared_by()) != 0){
				tv_ShareContent_Property.setText(item.getShare_content());
			}
			tv_contentProperty.setText(item.getContent());
			String temp_date = " -- ";
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateTime = sdf2.parse("" + item.getTimestamp());
				temp_date = "" + sdf1.format(dateTime);
			} catch (Exception e) { e.printStackTrace(); }
			timestamp_property.setText(temp_date);
			
			tv_user_id.setText(obj.getString("user_id") + "");
			tv_property_id.setText(obj.getString("property_id") + "");
			
			arrayList.get(position).setPROPERTY_ID(Integer.parseInt(obj.getString("property_id") + ""));
			
			tv_location.setText(obj.getString("property_city"));
			tv_landmark.setText(obj.getString("property_location"));
			
			String temp_area = " -- ";
			if (!(obj.getString("property_super_area")).isEmpty() 
					&& !(obj.getString("property_super_area").equals("null"))) {
				temp_area = obj.getString("property_super_area") +" "+obj.getString("property_super_area_unit");
				tv_area.setText(temp_area);
			} else if(!(obj.getString("property_built_area")).isEmpty()
					&& !(obj.getString("property_built_area").equals("null"))) {
				temp_area = obj.getString("property_built_area") +" "+obj.getString("property_built_area_unit");
				tv_area.setText(temp_area);
			} else if(!(obj.getString("property_plot_area")).isEmpty()
					&& !(obj.getString("property_plot_area").equals("null"))) {
				temp_area = obj.getString("property_plot_area") +" "+obj.getString("property_plot_area_unit");
				tv_area.setText(temp_area);
			} else {
				tv_area.setText(temp_area);
			}

			tv_rate.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(Integer.parseInt(obj.getString("expected_price"))));
			tv_postedBy.setText(Utilities.makeFirstLetterCaps(item.getFname()) 
							+ " " + Utilities.makeFirstLetterCaps(item.getLname()));
//			tv_userType.setText("( " + obj.getString("user_type") + " )");
			
			String temp_date1 = " -- ";
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateTime = sdf2.parse("" + obj.getString("property_added_on"));
				temp_date1 = "" + sdf1.format(dateTime);
			} catch (Exception e) { e.printStackTrace(); }
			tv_postedTime.setText("" + temp_date1);
			
			tv_proprty_listing_parent.setText(obj.getString("property_listing_parent"));
			tv_propertyType.setText(obj.getString("property_type_name"));
			
			String temp_str = obj.getString("property_listing_parent") + " of " 
					+ obj.getString("property_type_name") 
					+ " for " 
					+ obj.getString("property_listing_type") + " in " + obj.getString("property_landmark");
				
			if(obj.getString("property_listing_parent").contains("Requirement")){
				final SpannableStringBuilder sb = new SpannableStringBuilder(temp_str);
				final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 0, 0));
				final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); 
				sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
				sb.setSpan(bss, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
				title.setText(sb);

			} else if(obj.getString("property_listing_parent").contains("Availability")){
//				if(Utilities.D)Log.v(TAG,"get--getTv_proprty_listing_parent=="+m.getTv_proprty_listing_parent());
				final SpannableStringBuilder sb = new SpannableStringBuilder(temp_str);
				final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 255));
				final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); 
				sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
				sb.setSpan(bss, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
				title.setText(sb);
			}				
		} catch (JSONException e) { e.printStackTrace(); }
			
		} else if(getActivityTypeId == 2 ){//STATUS*********************************************
			lnr_property.setVisibility(View.GONE);
			lnr_connect_to.setVisibility(View.GONE);
			lnr_share.setVisibility(View.VISIBLE);
			
			try {
				name.setText(item.getFname() + " " + item.getLname());//item.getLname());
				String temp_date1 = " -- ";
				try {
					SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateTime = sdf2.parse("" + item.getTimestamp());
					temp_date1 = "" + sdf1.format(dateTime);
				} catch (Exception e) { e.printStackTrace(); }

				timestamp.setText(temp_date1);
				txtContent.setText(item.getContent());
				txtShareContent.setText(item.getShare_content());
				txtStatusMsg1.setText(item.getStatus());
				
				if(item.getImage().contains("http"))
					ImageLoader.getInstance().displayImage(item.getImage(), profilePic, AvailPCFragment.animateFirstListener);
				else 
					ImageLoader.getInstance().displayImage(Host.MainUrl + item.getImage(), profilePic, AvailPCFragment.animateFirstListener);
					
				//----------------
				TextView textView_pc = (TextView) convertView.findViewById(R.id.tv_profilePic);
		    	textView_pc.setText("");
		    	textView_pc.setVisibility(View.GONE);
		    	
		    	if(item.getImage().contains("default")){
	    			if(item.getFname().length() > 0){
	    	    		textView_pc.setVisibility(View.VISIBLE);
	        			textView_pc.getBackground().setColorFilter(item.getColor(), PorterDuff.Mode.SRC_IN);
	    				textView_pc.setText(String.valueOf(item.getFname().charAt(0)).toUpperCase());
	    			} else {
	    				textView_pc.setVisibility(View.GONE);
	    			}
		    	}
				//----------------
				lnr_shareContent.setVisibility(View.GONE);
				txtStatusMsg1.setVisibility(View.VISIBLE);
				txtShareContent.setVisibility(View.GONE);
					
				if(Integer.parseInt(item.getShared_by()) != 0){
					if(Utilities.D)Log.v(TAG,"item.getShare()==========="+item.getShare());
					if( !((item.getShare()+"").isEmpty()) && !((item.getShare()+"").equals("null"))){
						JSONArray reader = new JSONArray((item.getShare() + ""));
						for (int i = 0; i < reader.length(); i++) {
							JSONObject obj = reader.getJSONObject(i);
							
								lnr_shareContent.setVisibility(View.VISIBLE);
								txtStatusMsg1.setVisibility(View.GONE);
								txtShareContent.setVisibility(View.VISIBLE);

								tv_nameShare.setText(obj.getString("fname") + " " + obj.getString("lname"));
								txtStatusMsg11.setText(obj.getString("status"));

								if(obj.getString("image").contains("http"))
									ImageLoader.getInstance().displayImage(obj.getString("image"), iv_sharedContent, AvailPCFragment.animateFirstListener);
								else
									ImageLoader.getInstance().displayImage(Host.MainUrl + obj.getString("image"), iv_sharedContent, AvailPCFragment.animateFirstListener);
								//----------------
								TextView tv_sharedContent = (TextView) convertView.findViewById(R.id.tv_sharedContent);
								tv_sharedContent.setText("");
								tv_sharedContent.setVisibility(View.GONE);
						    	
						    	if(obj.getString("image").contains("default")){
					    			if(obj.getString("fname").length() > 0){
					    				tv_sharedContent.setVisibility(View.VISIBLE);
					    				tv_sharedContent.getBackground().setColorFilter(item.getColor(), PorterDuff.Mode.SRC_IN);
					    				tv_sharedContent.setText(String.valueOf(obj.getString("fname").charAt(0)).toUpperCase());
					    			} else {
					    				tv_sharedContent.setVisibility(View.GONE);
					    			}
						    	}
								//----------------
							}
						}				
					}

//						if (item.getImage() != null) {
//							feedImageView.setImageUrl(item.getImage(), imageLoader);
//							feedImageView.setVisibility(View.VISIBLE);
//							feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
//										@Override
//										public void onError() {
//										}
//										@Override
//										public void onSuccess() {
//										}
//									});
//						} else {
//							feedImageView.setVisibility(View.GONE);
//					}
			} catch (JSONException e) { e.printStackTrace(); }
			
		} else if(getActivityTypeId == 3){//FOLLOW
			lnr_property.setVisibility(View.GONE);
			lnr_connect_to.setVisibility(View.VISIBLE);
			lnr_share.setVisibility(View.GONE);
			
//			if(item.getStatus().isEmpty() || item.getStatus().equals(null) || item.getStatus().equals("null")){
//				lnr_connect_to.setVisibility(View.GONE);
//			}
			try {
				if(Utilities.D)Log.v(TAG,"item.getShare()====" + (item.getShare()+""));
				JSONArray readerTo = new JSONArray((item.getConnect_to() + ""));
				for (int i = 0; i < readerTo.length(); i++) {
					JSONObject obj = readerTo.getJSONObject(i);
					tv_connectTo_name.setText(obj.getString("fname") + " " + obj.getString("lname"));//item.getLname());
					tv_connectTo_name.setTag(obj.getString("connect_to") + "");
					String temp_date1 = " -- ";
					try {
						SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateTime = sdf2.parse("" + item.getActivity_datetime());
						temp_date1 = "" + sdf1.format(dateTime);
					} catch (Exception e) { e.printStackTrace(); }
					timestamp_connectTo.setText(temp_date1);
					txtStatusMsg1.setText(item.getStatus());//item.getStatus());
					tv_connectTo_content.setText(item.getContent());//item.getContent());
					
					if(item.getImage().contains("http"))
						ImageLoader.getInstance().displayImage(item.getImage(), profilePic_connectTo, AvailPCFragment.animateFirstListener);
					else 
						ImageLoader.getInstance().displayImage(Host.MainUrl + item.getImage(), profilePic_connectTo, AvailPCFragment.animateFirstListener);
				}
				//----------------
				TextView textView_pc = (TextView) convertView.findViewById(R.id.tv_profilePic_connectTo);
		    	textView_pc.setText("");
		    	textView_pc.setVisibility(View.GONE);
		    	
		    	if(item.getImage().contains("default")){
	    			if(item.getPosted_by().length() > 0){
	    	    		textView_pc.setVisibility(View.VISIBLE);
	        			textView_pc.getBackground().setColorFilter(item.getColor(), PorterDuff.Mode.SRC_IN);
	    				textView_pc.setText(String.valueOf(item.getFname().charAt(0)).toUpperCase());
	    			} else {
	    				textView_pc.setVisibility(View.GONE);
	    			}
		    	}
				JSONArray readerFrom = new JSONArray((item.getConnect_from() + ""));
				for (int i = 0; i < readerFrom.length(); i++) {
					JSONObject obj = readerFrom.getJSONObject(i);
					tv_connectFrom_name.setText(obj.getString("fname") + " " + obj.getString("lname"));
					tv_connectFrom_name.setTag(obj.getString("connect_from") + "");
				}
			} catch (JSONException e) { e.printStackTrace(); }
			//-----------------------LAYOUT FOR CONNECT TO -----------------------------------
		} else if(getActivityTypeId == 5){//SHARE
			lnr_property.setVisibility(View.VISIBLE);
			lnr_connect_to.setVisibility(View.VISIBLE);
			lnr_share.setVisibility(View.GONE);
		} else if(getActivityTypeId == 6 ){//IMAGE SHARING*********************************************
			lnr_property.setVisibility(View.GONE);
			lnr_connect_to.setVisibility(View.GONE);
			lnr_share.setVisibility(View.GONE);
		} else {
			lnr_property.setVisibility(View.GONE);
			lnr_connect_to.setVisibility(View.GONE);
			lnr_share.setVisibility(View.GONE);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(arrayList.get(position).getActivity_type_id().contains("1")){
					Intent i = new Intent(activity, DetailsPCFActivity.class);
					i.putExtra("KEY_PROPERTY_ID", (arrayList.get(position).getPROPERTY_ID() + ""));
					activity.startActivity(i);
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					if(Utilities.D)Log.v(TAG,"SubCategorySetter.get(position)==" + arrayList.get(position).getPROPERTY_ID());
				}
			}
		});

		return convertView;
	}//endGetView
	
//	@Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
////            	if (results.count == 0) {
////                    notifyDataSetInvalidated();
////                } else {
//                	arrayList = (List<MyTimelineSetter>) results.values; // has the filtered values
//                    notifyDataSetChanged();  // notifies the data with new filtered values
////                }
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                List<MyTimelineSetter> filteredArrList = new ArrayList<MyTimelineSetter>();
//                
//                if (mOriginalValues == null) {
//                    System.out.println("");
//                    mOriginalValues = new ArrayList<MyTimelineSetter>(arrayList); // saves the original data in mOriginalValues
//                }
//
//                /********
//                 * 
//                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//                 *  else does the Filtering and returns FilteredArrList(Filtered)  
//                 *
//                 ********/
//                if (constraint == null || constraint.length() == 0) {
//                    // set the Original result to return  
//                    results.count = mOriginalValues.size();
//                    results.values = mOriginalValues;
//                } 
//                else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        MyTimelineSetter data = mOriginalValues.get(i);
//                        if (
//                        		data.getFname().toLowerCase().contains(constraint.toString()) ||
//                        		data.getLname().toLowerCase().contains(constraint.toString()) ||
//                        		data.getStatus().toLowerCase().contains(constraint.toString()) ||
//                        		data.getTimestamp().toLowerCase().contains(constraint.toString()) ||
//                        		data.getContent().toLowerCase().contains(constraint.toString()) 
//                        		) {
//                            filteredArrList.add(data);
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = filteredArrList.size();
//                    results.values = filteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
//	public static CharSequence highlightText(String search, String originalText) {
//	    if (search != null && !search.equalsIgnoreCase("")) {
//	        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
//	        int start = normalizedText.indexOf(search);
//	        if (start < 0) {
//	            return originalText;
//	        } else {
//	            Spannable highlighted = new SpannableString(originalText);
//	            while (start >= 0) {
//	                int spanStart = Math.min(start, originalText.length());
//	                int spanEnd = Math.min(start + search.length(), originalText.length());
//	                highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//	                start = normalizedText.indexOf(search, spanEnd);
//	            }
//	            return highlighted;
//	        }
//	    }
//	    return originalText;
//	}
//--------------------------Comment Like Tasks-------------------------------start here-----------	
    void openDialogShare(View v, final int position){

    		shareDialog = new Dialog(activity);
//    		shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		shareDialog.setTitle("Share");
    		shareDialog.setContentView(R.layout.adapter_timeline_post_only);
    		shareDialog.setCancelable(true);
    		shareDialog.setOnKeyListener(new Dialog.OnKeyListener() {

    			@Override
    			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
    				if (keyCode == KeyEvent.KEYCODE_BACK) {
    					Log.v("TAG", "Dialog.KEYCODE_BACK.....########..........");
    					shareDialog.cancel();
    				}
    				return true;
    			}
    		});
    		
    		et_textSomething = (EditText)shareDialog.findViewById(R.id.et_textSomething);	
    		Button btn_shareFinal = (Button)shareDialog.findViewById(R.id.btn_shareFinal);
    		Button btn_shareCancel = (Button)shareDialog.findViewById(R.id.btn_shareCancel);
    		
    		btn_shareFinal.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				
    				if(et_textSomething.getText().toString().length() == 0){
        				Toast.makeText(activity, "Please write something..", Toast.LENGTH_SHORT).show();
    				}else {
        				shareDialog.cancel();
        				
        				if(v.getId() == R.id.btn_share){
        					if(Utilities.D)Log.v(TAG,"R.id.btn_share");
        					new SharePostTask().execute(position + "");
            				Toast.makeText(activity, "Sharing..", Toast.LENGTH_SHORT).show();
        				}else {
        					if(Utilities.D)Log.v(TAG,"R.id.btn_shareProperty");
        					new SharePropertyTask().execute(position + "");
            				Toast.makeText(activity, "Sharing..", Toast.LENGTH_SHORT).show();
        				}
    				}
    			}
    		});
    		btn_shareCancel.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
//    				lnrShareBox.setVisibility(View.GONE);
    				shareDialog.cancel();
    			}
    		});

    		shareDialog.show();
    		
    }//endCcustomdialog
    
	//------------------------
    
    void openDialogComment(View v, final int position, final String comments){
    	if(Utilities.D)Log.v(TAG,"comments=="+comments);
    	
		customdialog = new Dialog(activity);
//		customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customdialog.setTitle("Comments");
		customdialog.setContentView(R.layout.dialog_2_adapter_timeline_post);
		customdialog.setCancelable(true);
		customdialog.setOnKeyListener(new Dialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.v("TAG", "Dialog.KEYCODE_BACK.....########..........");
					customdialog.cancel();
					commentList.clear();
				}
				return true;
			}
		});
		
		progressView = (CircularProgressView) customdialog.findViewById(R.id.progressView);
		Utilities.startAnimationThreadStuff(500, progressView);
		progressView.setIndeterminate(true);
		progressView.setVisibility(View.GONE);

		commentListview = (ListView) customdialog.findViewById(R.id.list_my_comments);
		commentAdapter = new CommentAdapter(activity, commentList);
		commentListview.setAdapter(commentAdapter);
		commentListview.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	commentListview.setSelection(commentListview.getCount());
	        }
	    }, 500);
		commentListview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView,
					int scrollState) {
				// if(Utilities.D)Log.v(TAG,"onScrollStateChanged"
				// +"\nscrollState="+scrollState
				// +"\nlistView.getCount()="+listView.getCount());
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItem) {
//				 if(Utilities.D)Log.v(TAG,"onScroll*******" + "\nfirstVisibleItem="
//				 + firstVisibleItem + "\nvisibleItemCount="
//				 + visibleItemCount + "\ntotalItemCount="
//				 + totalItem + "\n************");
				lastInScreen = firstVisibleItem + visibleItemCount;
				totalItemCount = totalItem;
				
				if(Utilities.D)Log.v(TAG,"checking......1");
//				if(Utilities.D)Log.v(TAG,"mPrevTotalItemCount=" + mPrevTotalItemCount);
//				if(Utilities.D)Log.v(TAG,"lastInScreen=" + lastInScreen);
				if (view.getAdapter() != null
						&& (lastInScreen >= totalItemCount)
						&& totalItemCount != mPrevTotalItemCount) {
					
					if(Utilities.D)Log.v(TAG,"checking......2");
//					Log.e(TAG, "current_page =" + current_page + " total_pages=" + total_pages);
					
					if (isLoadingMore == true && current_page <= total_pages) {
						Log.e(TAG, "LoadingMore... Please wait..");
						// mListView.addFooterView(footerView);
						
						COMMENT_API = "{\"activity_id\":\""
						+ arrayList.get(position).getActivity_id() + "\",\"last_comment_id\":\""
						+ getLastCommentId + "\"}";
						
						if(Utilities.isInternetOn(activity))
							new CommentMoreTask().execute(COMMENT_API);
							
					}
				}
			}
		});
		et_commentSomething = (EditText) customdialog.findViewById(R.id.et_commentSomething);
		/////////////////PARSING  ////////////////////////////////////////
			if(comments.length() > 0){
				try {
					JSONArray commentArray = new JSONArray(comments);
					for(int i = 0; i<commentArray.length();i++){
						JSONObject obj = commentArray.getJSONObject(i);
						if(i == commentArray.length() - 1){
							getLastCommentId = obj.getString("comment_id");
						}
					}
				} catch (Exception e) { e.printStackTrace(); }
			}
		getLastCommentId = "0";
		
		commentListview.setVisibility(View.GONE);
		COMMENT_API = "{\"activity_id\":\""
		+ arrayList.get(position).getActivity_id() + "\",\"last_comment_id\":"
		+ getLastCommentId + "}";
		
		if(Utilities.isInternetOn(activity))
			new CommentMoreTask().execute(COMMENT_API);
		
		///////////////////////////////////////
		btn_commentSomething = (Button)customdialog.findViewById(R.id.btn_commentSomething);
		btn_commentSomething.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String getCommentText = et_commentSomething.getText().toString().replace("\n", " ");
				if(!getCommentText.isEmpty()){
					if(Utilities.isInternetOn(activity))
						new CommentTask().execute(position+"");
				}else{
					Toast.makeText(activity, "Please write something.. !", Toast.LENGTH_SHORT).show();
				}
			}
		});
		customdialog.show();
    }//endCcustomdialog
    
	class CommentMoreTask extends AsyncTask<String, String, String> {
		int api_code = 0, api_total = 0;
		public String api_message = "", api_data;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			isLoadingMore = false;
			progressView.setVisibility(View.VISIBLE);
//			tv_nodata.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String UrlBase = Host.GetAllCommentsUrl + "page/" + current_page + "/";
				String jsonString = Utilities.sendData(UrlBase, f_url[0]);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_total = meta.getInt("total_pages");
					total_pages = api_total;
					JSONArray data = (JSONArray) reader.get("data");
					for(int i = 0; i<data.length();i++){
						JSONObject obj = data.getJSONObject(i);
						MyTimelineSetter ch = new MyTimelineSetter();

						if(obj.getString("image").contains("http"))
							ch.setImageUrl(obj.getString("image"));
						else
							ch.setImageUrl(Host.MainUrl + obj.getString("image"));
						
						if(obj.getString("image").contains("http"))
							ch.setThumbnailUrl(obj.getString("image"));
						else
							ch.setThumbnailUrl(Host.MainUrl + obj.getString("image"));

						if (obj.getString("fname").equals("")
								|| obj.getString("fname").equals(null)
								|| obj.getString("fname").equals("null")) {
							ch.setFname(" -- ");
						} else {
							ch.setFname(Utilities.makeFirstLetterCaps(obj.getString("fname")) 
									+ " " + Utilities.makeFirstLetterCaps(obj.getString("lname")));
						}
						
						if (obj.getString("comment_by").equals("")
								|| obj.getString("comment_by").equals(null)
								|| obj.getString("comment_by").equals("null")) {
							ch.setComment_by(" -- ");
						} else {
							ch.setComment_by(obj.getString("comment_by"));
						}
						
						if (obj.getString("comment_date").equals("")
								|| obj.getString("comment_date").equals(null)
								|| obj.getString("comment_date").equals("null")) {
							ch.setActivity_datetime(" -- ");
						} else {
							ch.setActivity_datetime(obj.getString("comment_date"));
						}
						
						if (obj.getString("comment_id").equals("") 
								|| obj.getString("comment_id").equals(null)
								|| obj.getString("comment_id").equals("null")) {
						} else {
							ch.setcomment_id(Integer.parseInt(obj.getString("comment_id")));
						}

//						if(i == data.length() - 1)
//							getLastCommentId = obj.getString("comment_id");
						
						if (obj.getString("activity_comment").equals("") 
								|| obj.getString("activity_comment").equals(null)
								|| obj.getString("activity_comment").equals("null")) {
						} else {
							ch.setActivity_comment(obj.getString("activity_comment"));
						}
						ch.setColor(Utilities.getRandomColor(activity,obj.getString("fname")));
						
						commentList.add(0, ch);
						}
//					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return f_url[0];
		}

		@Override
		protected void onPostExecute(String file_url) {
//			if(Utilities.D)Log.v(TAG,"file_url="+file_url);
			
			commentListview.setVisibility(View.VISIBLE);
			progressView.setVisibility(View.GONE);
			isLoadingMore = true;
//			if(AvailList.isEmpty()){
//				tv_nodata.setVisibility(View.VISIBLE);
//				tv_nodata.setText("Internet problem !");
//			}

			if (api_code == 200) {
//				mSearchView.setVisibility(View.VISIBLE);
//				mListView.setVisibility(View.VISIBLE);
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("No data found !");
//				}
				mPrevTotalItemCount = totalItemCount;
				current_page = current_page + 1;
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						commentAdapter.notifyDataSetChanged();
					}
				});
				
			} else if(api_code == 400){
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("No data found !");
//				}
			} else if(api_code == 501){
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText(api_message);
//				}
			} else {
//				if(AvailList.isEmpty()){
//					tv_nodata.setVisibility(View.VISIBLE);
//					tv_nodata.setText("Something went wrong, please try again !");
//				}
//				Toast.makeText(getActivity(), "Something went wrong, please try again !", Toast.LENGTH_SHORT).show();
			}
		}
	}//endMoreCommentTask

	public void onPopupDeleteClick(View button, final int position) {
		PopupMenu popup = new PopupMenu(activity, button);
		popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				if(Utilities.isInternetOn(activity)){
					location = position;
					new DeletePostTask().execute(position + "");
					Toast.makeText(activity, "Deleting..", Toast.LENGTH_SHORT).show();
				}
				
				return true;
			}
		});
		popup.show();
	}
	
	class DeletePostTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;
		int position = 0;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
//			 Utilities.showProgressDialog(activity, DeletePostTask.this);
//			 dialog.show();
			 
			MyTimelineFragment.progressView.setVisibility(View.VISIBLE);
			arrayList.get(location).setBool_enable(false);
			notifyDataSetChanged();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(activity, "Request cancelled !", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				position = Integer.parseInt(f_url[0]);
				
				String DELETE_POST_API = 
						"{\"table\":\"activities\",\"by\":{\"activity_id\":\"" 
								+ arrayList.get(position).getActivity_id() + "\"}}";
				
				String UrlBase = Host.DeleteUrl + "";
				String jsonString = Utilities.sendData(UrlBase, DELETE_POST_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					String api_data = reader.getString("data");
					
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

//			dialog.cancel();
			MyTimelineFragment.progressView.setVisibility(View.GONE);
			if(api_code == 200){
				
				Toast.makeText(activity, api_message, Toast.LENGTH_LONG).show();
				arrayList.remove(position);
				notifyDataSetChanged();
			}
		}
	}
	
	class LikeTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(Utilities.D)Log.v(TAG,"onPreExecute=======LikeTask");
			arrayList.get(location).setBool_enable(false);
			arrayList.get(location).setBool_hasLike(true);
			notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"doInBackground=======LikeTask");
			try {
				String getActivity_id = f_url[0];
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
					String LIKE_API = 
							"{\"table\":\"activity_likes\",\"params\":{\"activity_id\":\""
					+ getActivity_id + "\",\"liked_by\":\"" 
					+ MyTimelineFragment.getUserDataID + "\",\"like_datetime\":\""
					+ timeString + "\"}}";
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, LIKE_API);
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
			if(Utilities.D)Log.v(TAG,"onPostExecute=======LikeTask");

			if(api_code == 200){
				
				arrayList.get(location).setLike("dislike");
				arrayList.get(location).setTotal_likes("" + (Integer.parseInt(arrayList.get(location).getTotal_likes()) + 1));
//				Toast.makeText(activity, api_message, 1).show();
			} else {
				
				arrayList.get(location).setLike("like");
//				Toast.makeText(activity, api_message, 1).show();
			}
			arrayList.get(location).setBool_enable(true);
			arrayList.get(location).setBool_hasLike(false);
			notifyDataSetChanged();
		}
	}
	class DisLikeTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
//		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if(Utilities.D)Log.v(TAG,"onPreExecute=======DisLikeTask");
			arrayList.get(location).setBool_enable(false);
			arrayList.get(location).setBool_hasLike(true);
			notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"doInBackground=======DisLikeTask");
			String getActivity_id = f_url[0];
			try {
				String DISLIKE_API = "{\"table\":\"activity_likes\",\"by\":{\"activity_id\":\""
						+ getActivity_id + "\",\"liked_by\":\"" + MyTimelineFragment.getUserDataID + "\"}}";

				String UrlBase = Host.DeleteUrl + "";
				String jsonString = Utilities.sendData(UrlBase, DISLIKE_API);
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
			if(Utilities.D)Log.v(TAG,"onPostExecute=======DisLikeTask");

			if(api_code == 200){
				
				arrayList.get(location).setLike("like");
				arrayList.get(location).setTotal_likes("" + (Integer.parseInt(arrayList.get(location).getTotal_likes()) - 1));
//				Toast.makeText(activity, api_message, 1).show();
			} else {
				
				arrayList.get(location).setLike("dislike");
//				Toast.makeText(activity, api_message, 1).show();
			}
			arrayList.get(location).setBool_enable(true);
			arrayList.get(location).setBool_hasLike(false);
			notifyDataSetChanged();
		}
	}
	class CommentTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data;
		String temp_commentSomething = "";
		Dialog dialog;
		int position = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			btn_postSomething.setText("sending..");
			dialog = Utilities.showProgressDialog(activity);
			dialog.show();
			temp_commentSomething = et_commentSomething.getText().toString().replace("\n", " ");
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				position = Integer.parseInt(f_url[0]);				
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();

				String COMMENT_API = 
						"{\"table\":\"activity_comments\",\"params\":{\"comment_on_activity_id\":\""
								+ arrayList.get(position).getActivity_id() + "\",\"activity_comment\":\""
								+ temp_commentSomething + "\",\"comment_by\":\""
								+ MyTimelineFragment.getUserDataID + "\",\"comment_date\":\""
								+ timeString + "\"}}";
				
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, COMMENT_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					getLastCommentId = reader.getString("data");
					if(Utilities.D)Log.v(TAG,"getLastCommentId===="+getLastCommentId);
					
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
			dialog.dismiss();
			btn_commentSomething.setText("SEND");

			if (api_code == 200) {
//				customdialog.cancel();
				if(commentList.size() > 0){
					Log.i(TAG, "Check;ing errror here.......");
					MyTimelineSetter ch = new MyTimelineSetter();
					ch.setImage(getProfileImage);
					ch.setThumbnailUrl(getProfileImage);
					ch.setFname(getUserName);
					ch.setComment_by(getUserDataID);

					Date date = new Date();
					String timeString = new Timestamp(date.getTime()).toString();
					
					ch.setActivity_datetime(timeString);
					ch.setcomment_id(0);
					ch.setActivity_comment(et_commentSomething.getText().toString().replace("\n", " "));
					ch.setColor(Utilities.getRandomColor(activity,getUserName));

					commentList.add(ch);
					commentListview.setSelection(commentAdapter.getCount() - 1);
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(activity != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										commentAdapter.notifyDataSetChanged();
									}
								});
						}
					});
				} else {
					Log.i(TAG, "Check;ing errror here.......else");
					MyTimelineSetter ch = new MyTimelineSetter();
					ch.setImageUrl(getProfileImage);
					ch.setThumbnailUrl(getProfileImage);
					ch.setFname(getUserName);
					ch.setComment_by(getUserDataID);

					Date date = new Date();
					String timeString = new Timestamp(date.getTime()).toString();
					
					ch.setActivity_datetime(timeString);
					ch.setcomment_id(0);
					ch.setActivity_comment(et_commentSomething.getText().toString().replace("\n", " "));
					ch.setColor(Utilities.getRandomColor(activity,getUserName));

					commentList.add(ch);
					commentListview.setSelection(commentAdapter.getCount() - 1);
					if(activity != null)
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								commentAdapter.notifyDataSetChanged();
							}
						});
				}
				et_commentSomething.setText("");
//				Toast.makeText(activity, "Comment: " + api_message, Toast.LENGTH_LONG).show();
			}			
		}
	}//endCommentTask
	
	public void onPopupShareClick(final View button, final int position) {
		PopupMenu popup = new PopupMenu(activity, button);
		popup.getMenuInflater().inflate(R.menu.popup_share, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(Utilities.D)Log.v(TAG,"-------R.id.btn_share---------");
				openDialogShare(button, position);
				
				return true;
			}
		});

		popup.show();
	}
	
	class SharePostTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		String temp_textSomeThing = "";
//		Dialog dialog;
		int position = 0;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
//			MyTimelineFragment.progressView.setVisibility(View.VISIBLE);
			btn_share.setEnabled(false);
			temp_textSomeThing = et_textSomething.getText().toString();
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				position = Integer.parseInt(f_url[0]);
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
				String SHARE_POST_API = 
						"{\"table\":\"activities\",\"params\":{\"status\":\""
				+ arrayList.get(position).getStatus() + "\",\"activity_type_id\":\"2\",\"content\":\""
				+ arrayList.get(position).getContent() + "\",\"share_content\":\""
				+ temp_textSomeThing + "\",\"event_id\":\""
				+ arrayList.get(position).getEvent_id() + "\",\"posted_by\":\""
				+ getUserDataID +"\",\"shared_by\":\""
				+ arrayList.get(position).getPosted_by() + "\",\"type\":\"Shared\",\"activity_datetime\":\""
				+ timeString + "\"}}";

				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, SHARE_POST_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					String api_data = reader.getString("data");
					
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
			
			btn_share.setEnabled(true);
			if(api_code == 200){
//				MyTimelineFragment.progressView.setVisibility(View.GONE);

				MyTimelineSetter ch = new MyTimelineSetter();
				ch.setEvent_id(arrayList.get(position).getEvent_id());
				ch.setActivity_type_id(arrayList.get(position).getActivity_type_id());
				ch.setPosted_by(getUserDataID);
				ch.setTotal_likes(0 + "");
				ch.setLike("like");
				ch.setShared_by(arrayList.get(position).getPosted_by());
				ch.setShare_content(arrayList.get(position).getShare_content());

				ch.setFname(getFName);
				ch.setLname(getLName);
				ch.setImage(getProfileImage);
				ch.setStatus(arrayList.get(position).getStatus());
				ch.setContent(arrayList.get(position).getContent());
				
				String temp_share = "[{\"share_content\":\""
				+ et_textSomething.getText().toString() +"\",\"lname\":\""
				+ arrayList.get(position).getLname() + "\",\"activity_id\":\""
				+ arrayList.get(position).getActivity_id() +"\",\"status\":\""
				+ arrayList.get(position).getStatus() +"\",\"image\":\""
				+ arrayList.get(position).getImage() + "\",\"event_id\":\""
				+ arrayList.get(position).getEvent_id() + "\",\"activity_images\":null,\"posted_by\":\""
				+ arrayList.get(position).getPosted_by() + "\",\"like\":\"0\",\"type\":\"Shared\",\"id\":\""
				+ getUserDataID + "\",\"timestamp\":\""
				+ arrayList.get(position).getTimestamp() + "\",\"content\":\""
				+ arrayList.get(position).getContent() + "\",\"activity_datetime\":\""
				+ arrayList.get(position).getActivity_datetime() + "\",\"activity_type_id\":\""
				+ arrayList.get(position).getActivity_type_id() + "\",\"shared_by\":\""
				+ arrayList.get(position).getShared_by() + "\",\"fname\":\""
				+ arrayList.get(position).getFname() + "\"}]";
				
				ch.setShare(temp_share);
				ch.setShare_content(et_textSomething.getText().toString());
				ch.setComments(arrayList.get(position).getComments());
				ch.setcomment_id(arrayList.get(position).getcomment_id());

				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
				ch.setTimestamp(timeString);
				
				ch.setColor(Utilities.getRandomColor(activity,getFName));
				
				arrayList.add(0, ch);
				notifyDataSetChanged();
				
				Toast.makeText(activity, "Shared: "+api_message, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activity, "Shared: "+api_message, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	class SharePropertyTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		String api_message = "", api_data, content = "";
		String tempTextSomething = "";
//		Dialog dialog;
		int position = 0;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
//			MyTimelineFragment.progressView.setVisibility(View.VISIBLE);
			btn_shareProperty.setEnabled(false);
			tempTextSomething = et_textSomething.getText().toString();
		}

		@Override
		protected String doInBackground(String... f_url) {
			
			try {
				position = Integer.parseInt(f_url[0]);
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
				content = arrayList.get(position).getContent();
				if(content.contains("Posted")){
					content = "Shared " 
							+ arrayList.get(position).getFname() + " " + arrayList.get(position).getLname() 
							+ " Property";
				} 

				String SHARE_POST_API = 
						"{\"table\":\"activities\",\"params\":{\"activity_type_id\":\""
				+ arrayList.get(position).getActivity_type_id() +"\",\"event_id\":\""
				+ arrayList.get(position).getEvent_id() +"\",\"posted_by\":\""
				+ getUserDataID +"\",\"activity_datetime\":\""
				+ timeString +"\",\"share_content\":\""
				+ tempTextSomething +"\",\"shared_by\":\""
				+ arrayList.get(position).getPosted_by() + "\",\"type\":\"Shared\",\"status\":\""
				+ arrayList.get(position).getStatus() +"\",\"content\":\""
				+ content
				+ "\"}}";
				
				String UrlBase = Host.PostUrl + "";
				String jsonString = Utilities.sendData(UrlBase, SHARE_POST_API);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");
				if(Utilities.D)Log.v(TAG,"api_message-=" + api_message);

				if (api_code == 200) {
					
					String api_data = reader.getString("data");
					
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
			
			btn_shareProperty.setEnabled(true);
			if(api_code == 200){
//				MyTimelineFragment.progressView.setVisibility(View.GONE);

				MyTimelineSetter ch = new MyTimelineSetter();
				ch.setEvent_id(arrayList.get(position).getEvent_id());
				ch.setActivity_type_id(arrayList.get(position).getActivity_type_id());
				ch.setPosted_by(getUserDataID);
				ch.setTotal_likes(0 + "");
				ch.setLike("like");
				ch.setShared_by(arrayList.get(position).getPosted_by());
				ch.setShare_content(arrayList.get(position).getShare_content());
				
				ch.setFname(getFName);
				ch.setLname(getLName);
				ch.setImage(getProfileImage);
				ch.setStatus(arrayList.get(position).getStatus());
				ch.setContent(content);
//						arrayList.get(position).getContent());

				String temp_share = "[{\"share_content\":\""
				+ et_textSomething.getText().toString() +"\",\"lname\":\""
				+ arrayList.get(position).getLname() + "\",\"activity_id\":\""
				+ arrayList.get(position).getActivity_id() +"\",\"status\":\""
				+ arrayList.get(position).getStatus() +"\",\"image\":\""
				+ arrayList.get(position).getImage() + "\",\"event_id\":\""
				+ arrayList.get(position).getEvent_id() + "\",\"activity_images\":null,\"posted_by\":\""
				+ arrayList.get(position).getPosted_by() + "\",\"like\":\"0\",\"type\":\"Shared\",\"id\":\""
				+ getUserDataID + "\",\"timestamp\":\""
				+ arrayList.get(position).getTimestamp() + "\",\"content\":\""
				+ arrayList.get(position).getContent() + "\",\"activity_datetime\":\""
				+ arrayList.get(position).getActivity_datetime() + "\",\"activity_type_id\":\""
				+ arrayList.get(position).getActivity_type_id() + "\",\"shared_by\":\""
				+ arrayList.get(position).getShared_by() + "\",\"fname\":\""
				+ arrayList.get(position).getFname() + "\"}]";
				
				ch.setShare(temp_share);
				ch.setShare_content(et_textSomething.getText().toString());
				ch.setComments(arrayList.get(position).getComments());
				ch.setcomment_id(arrayList.get(position).getcomment_id());
				ch.setProperty_details(arrayList.get(position).getProperty_details());

				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				
				ch.setTimestamp(timeString);
				
				ch.setColor(Utilities.getRandomColor(activity,getFName));

				arrayList.add(0, ch);
				notifyDataSetChanged();
				
				Toast.makeText(activity, "Shared: "+api_message, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activity, "Shared: "+api_message, Toast.LENGTH_LONG).show();
			}
		}
	}
}
 