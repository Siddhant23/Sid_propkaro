package com.propkaro.notifications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.propkaro.R;
import com.propkaro.propertycenter.DetailsPCFActivity;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.SetNotityPreference;
import com.propkaro.util.Utilities;

public class NotifyGCMRecyclerView extends RecyclerView.Adapter<NotifyGCMRecyclerView.ViewHolder> {
	private FragmentActivity activity;
	private Context ctx;
	private static final String TAG = NotifyGCMRecyclerView.class.getSimpleName();
	SetNotityPreference setPreference;
    List<NotifyGCMSetter> arrayList;      
    List<NotifyGCMSetter> mOriginalValues; // Original Values
	SharedPreferences mPrefs;
	int location = 0;
	String getEmailID = "", getUserType = "", getUserDataID = "";
    
	public NotifyGCMRecyclerView(FragmentActivity activity, List<NotifyGCMSetter> availList) {
		this.activity = activity;
		this.ctx = activity;
		this.arrayList = availList;
		
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

    	View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_my_message_item1, null);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
		final NotifyGCMSetter m = arrayList.get(position);

//		ImageLoader.getInstance().displayImage(m.getThumbnailUrl(), holder.imageView_pc, AvailPCFragment.animateFirstListener);
		
		holder.textView_pc.setText("");
		holder.textView_pc.setVisibility(View.GONE);
		holder.tv_message.setText(Utilities.makeFirstLetterCaps(m.getMessage()));
    	
    	if(m.getThumbnailUrl().contains("default")){
    		holder.textView_pc.setVisibility(View.VISIBLE);
			if(m.getMessage().length() > 0){
				holder.textView_pc.getBackground().setColorFilter(m.getColor(), PorterDuff.Mode.SRC_IN);
//  			textView_pc.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, Utilities.getRandomColor(activity)));
				holder.textView_pc.setText(String.valueOf(m.getMessage().charAt(0)).toUpperCase());
			} else {
				holder.textView_pc.setVisibility(View.GONE);
    		}
        }
		
		try {
			String temp_date = " -- ";
//			SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateTime = sdf2.parse("" + m.getTime());
//			temp_date = "" + sdf1.format(dateTime);
			holder.tv_time.setText(Utilities.getTimeAgo(dateTime, ctx));
		} catch (Exception e) { e.printStackTrace(); }
		
		holder.iv_remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetNotityPreference setPreference = new SetNotityPreference();
				setPreference.removeFavorite(activity, arrayList.get(position));
				arrayList.remove(position);
				NotifyGCMRecyclerView.this.notifyDataSetChanged();
				
				if(arrayList.isEmpty()){
					NotifyGCMFragment.rel_nodata.setVisibility(View.VISIBLE);
				}
			}
		});
		holder.itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.e(TAG, "arrayList.get(position).getType()=="+arrayList.get(position).getType());
				if(Utilities.isInternetOn(activity)){
					if(arrayList.get(position).getType().contains("shortlist")
							|| arrayList.get(position).getType().contains("message") ){
						Intent i = new Intent(activity, UserInfoFActivity.class);
						i.putExtra("KEY_GET_USER_ID", m.getUSER_ID() + "");
						activity.startActivity(i);
						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}else if(arrayList.get(position).getType().contains("requirement")){
//						if(!Host.checkUserIds(getUserDataID, m.getUSER_ID() + "")){
						Intent i = new Intent(activity, DetailsPCFActivity.class);
						i.putExtra("KEY_PROPERTY_ID", arrayList.get(position).getPROPERTY_ID() + "");
						activity.startActivity(i);
						activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//						}
					}
				}
			}
		});
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_pc, iv_remove;
    	TextView textView_pc, tv_title, tv_message, tv_time;
        CoordinatorLayout mCoordinator;
    	
        public ViewHolder(View itemView) {
            super(itemView);
            
            mCoordinator = (CoordinatorLayout) itemView.findViewById(R.id.root_coordinator);
    		textView_pc = (TextView) itemView.findViewById(R.id.textView_pc);
    		imageView_pc = (ImageView) itemView.findViewById(R.id.imageView_pc);
    		tv_title = (TextView) itemView.findViewById(R.id.tv_title);
    		tv_message = (TextView) itemView.findViewById(R.id.tv_message);
    		iv_remove = (ImageView) itemView.findViewById(R.id.iv_remove);
    		tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
 