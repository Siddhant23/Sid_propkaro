package com.propkaro.notifications;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.propkaro.R;

public class PlacesAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<GoogleSetter> SubCategorySetter;
	private static final String TAG = PlacesAdapter.class.getSimpleName();
	
	public PlacesAdapter(Activity activity, List<GoogleSetter> SubCategorySetter) {
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
			convertView = inflater.inflate(R.layout.adapter_google_item, null);
		
		TextView  tv_place = (TextView) convertView.findViewById(R.id.tv_place);
		GoogleSetter m = SubCategorySetter.get(position);
		tv_place.setText(m.getDescription());		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String _id = SubCategorySetter.get(position).getId();
				}
		});
 
		return convertView;
	}//endGetView
}
 