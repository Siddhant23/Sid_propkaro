package com.propkaro.facilitation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.propkaro.R;

public class FacilitationAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FacilitationSetter> SubCategorySetter;
	// ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private static final String TAG = FacilitationAdapter.class.getSimpleName();

	public FacilitationAdapter(Activity activity, List<FacilitationSetter> SubCategorySetter) {
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
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.facilitataion_center_list,
					null);

		// if (imageLoader == null)
		// imageLoader = AppController.getInstance().getImageLoader();
		// NetworkImageView thumbNail = (NetworkImageView)
		// convertView.findViewById(R.id.thumbnail);

		
		Typeface RobotoLight = Typeface.createFromAsset(activity.getAssets(),"fonts/Roboto-Light.ttf"); 

		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_location = (TextView) convertView.findViewById(R.id.tv_location);
		TextView tv_contact = (TextView) convertView.findViewById(R.id.tv_contact);
		
		tv_name.setTypeface(RobotoLight);
		tv_location.setTypeface(RobotoLight);
		tv_contact .setTypeface(RobotoLight);


		FacilitationSetter m = SubCategorySetter.get(position);

		tv_name.setText(m.getTv_name() + "");
		tv_location.setText(m.getTv_location() + "");
		tv_contact.setText(m.getContact() + "");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				Intent i = new Intent(activity, VideoViewActivity.class);
//				i.putExtra("getURL", SubCategorySetter.get(position).getLink());
//				i.putExtra("getTitle", SubCategorySetter.get(position).getTv_name());
//				activity.startActivity(i);
//				activity.overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
			}
		});

		// ***************hari_changing date into desired date
		// value*************
		// if(Utilities.D)Log.v(TAG,"Parsing Input" );
		// ***************hari_changing end_date into desired date
		// value*************
		// if (position % 2 == 0) {
		// convertView.setBackgroundResource(R.drawable.strip_list1);
		// } else {
		// convertView.setBackgroundResource(R.drawable.strip_list2);
		// }
		// if (position % 2 == 1) {
		// convertView.setBackgroundColor(ctx.getResources().getColor(R.color.list_row_color1));
		// } else {
		// convertView.setBackgroundColor(ctx.getResources().getColor(R.color.list_row_color2));
		// }
		return convertView;
	}// endGetView

	public void filter(String charText) {
		charText = charText.toString();
		SubCategorySetter.clear();

		if (charText.length() == 0) {
			SubCategorySetter.addAll(SubCategorySetter);
		} else {
			for (Object geObject : SubCategorySetter) {

			}
		}
	}

	private void doButtonOneClickActions(int rowNumber) {
		System.out.println("position=" + rowNumber);
	}
}
