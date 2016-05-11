package com.propkaro.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.propkaro.R;
import com.propkaro.post.PostPropertyActivity;
import com.propkaro.util.Utilities;

public class ComingSoonFragment extends Fragment {
	private static final String TAG = ComingSoonFragment.class.getSimpleName();
	private static final String KEY_POSITION = "TabProductFragment:Position";
	static int POSITION;
	Context ctx;
	View rView;

	public static ComingSoonFragment newInstance(int pos) {
		ComingSoonFragment fragment = new ComingSoonFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_POSITION, pos);
		fragment.setArguments(b);

		ComingSoonFragment.POSITION = pos;
		Log.e(TAG, "+Position=" + pos);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rView = inflater.inflate(R.layout.coming_soon_fragment, container, false);
		TextView tv_nodata = (TextView) rView.findViewById(R.id.tv_nodata);
		tv_nodata.setTextSize(18);
		tv_nodata.setText("Chat and Call \nComing Soon!");
		ImageView iv_nodata = (ImageView) rView.findViewById(R.id.iv_nodata);
		iv_nodata.setImageResource(R.mipmap.bg_no_message);
		TextView tv_addLinsting = (TextView) rView.findViewById(R.id.tv_addLinsting);
		tv_addLinsting.setVisibility(View.VISIBLE);
		tv_addLinsting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(ctx)){
					Intent i = new Intent(ctx, PostPropertyActivity.class);
					startActivity(i);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		
		//--------------- Button Clicks----------------------------
 
		return rView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getActivity();
	};
}