package com.propkaro.timeline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.userinfo.UserInfoFActivity;
import com.propkaro.util.Utilities;

public class CommentAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<MyTimelineSetter> SubCategorySetter;
	private static final String TAG = CommentAdapter.class.getSimpleName();
	
	public CommentAdapter(Activity activity, List<MyTimelineSetter> SubCategorySetter) {
		this.activity = activity;
		this.SubCategorySetter = SubCategorySetter;
	}

	@Override
	public int getCount() {
		return SubCategorySetter.size();
	}

	@Override
	public Object getItem(int location) {
		return SubCategorySetter.get(location);
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
			convertView = inflater.inflate(R.layout.adapter_comment_item, null);
		
		ImageView imageView_pc = (ImageView) convertView.findViewById(R.id.imageView_pc);
		TextView textView_pc = (TextView) convertView.findViewById(R.id.textView_pc);

		TextView tv_commentBy = (TextView) convertView.findViewById(R.id.tv_commentBy);
		TextView tv_timestamp = (TextView) convertView.findViewById(R.id.tv_timestamp);
		TextView  tv_comment= (TextView) convertView.findViewById(R.id.tv_comment);
		MyTimelineSetter m = SubCategorySetter.get(position);
		
//		Log.e(TAG, "m.getThumbnailUrl()======="+m.getThumbnailUrl());
//		try {
//			File file = ImageLoader.getInstance().getDiscCache().get(m.getThumbnailUrl());
//			if (!file.exists()) {
//				
//				DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc().build();
//				ImageLoader.getInstance().displayImage(m.getThumbnailUrl(), imageView_pc, options);
//			} else {
//				
//				imageView_pc.setImageURI(Uri.parse(file.getAbsolutePath()));
//			}
//		} catch (Exception e) { e.printStackTrace(); }

		ImageLoader.getInstance().displayImage(m.getThumbnailUrl(), imageView_pc, AvailPCFragment.animateFirstListener);
		
    	textView_pc.setText("");
    	textView_pc.setVisibility(View.GONE);
    	
    	if(m.getThumbnailUrl().contains("default")){
    		if(m.getFname().length() > 0){
        		textView_pc.setVisibility(View.VISIBLE);
			textView_pc.getBackground().setColorFilter(m.getColor(), PorterDuff.Mode.SRC_IN);
   			textView_pc.setText(String.valueOf(m.getFname().charAt(0)).toUpperCase());
    		} else {
        		textView_pc.setVisibility(View.GONE);
    		}
    	}
        
//		tv_userType.setText(Utilities.makeFirstLetterCaps(m.getTv_userType()));
//		if((m.getUse_company_name() + "").contains("1")){
//			tv_postedBy.setText(m.getCompany_name()+"");
////			if(Utilities.D)Log.v(TAG,"#########check#########m.getCompany_name()"+m.getUse_company_name()+"="+m.getCompany_name());
//		} else{
//			tv_postedBy.setText(Utilities.makeFirstLetterCaps(m.getTv_postedBy()));
////			if(Utilities.D)Log.v(TAG,"#########check#########m.getTv_postedBy()"+m.getUse_company_name()+"="+m.getTv_postedBy());
//		}
		tv_commentBy.setText(Utilities.makeFirstLetterCaps(m.getFname()));
		tv_timestamp.setText(m.getActivity_datetime() + "");
		tv_comment.setText( m.getActivity_comment()  + "");
		String temp_date = " -- ";
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat( "MMM dd, yyyy hh:mm aa");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateTime = sdf2.parse("" + m.getActivity_datetime());
			temp_date = "" + sdf1.format(dateTime);
		} catch (Exception e) { e.printStackTrace(); }
		
		tv_timestamp.setText(temp_date);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String _temp = (SubCategorySetter.get(position).getComment_by());
				Intent i = new Intent(activity, UserInfoFActivity.class);
				i.putExtra("KEY_GET_USER_ID", (_temp + ""));
				activity.startActivity(i);			
				}
		});
 
		return convertView;
	}//endGetView
	
//	public void filter(String charText) {
//		 charText = charText.toString();
//		 SubCategorySetter.clear();
//	  
//	if (charText.length() == 0) {
//		SubCategorySetter.addAll(SubCategorySetter);
//		 } else {
//		 for (Object geObject : SubCategorySetter) {
//		 
//		 }}
//		}
}
 