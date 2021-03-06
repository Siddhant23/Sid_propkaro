package com.propkaro.mandate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.propkaro.R;

public class Page4Fragment extends Fragment {
	Typeface RobotoLight;
	TextView tv_from_step3,tv_1 ,tv_2,tv_3,tv_4;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.mandate_4, container, false);
    
    RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

    tv_from_step3= (TextView) v.findViewById(R.id.tv_from_step3);
    tv_1 = (TextView) v.findViewById(R.id.tv_1); 
	 tv_2 = (TextView) v.findViewById(R.id.tv_2); 
	 tv_3 = (TextView) v.findViewById(R.id.tv_3); 
	 tv_4 = (TextView) v.findViewById(R.id.tv_4);

    ImageView  iv_forward = (ImageView) v.findViewById(R.id.iv_forward);
    iv_forward.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
//			MainAct.pager.setCurrentItem(3);
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			
      }
  });
    ImageView  iv_back = (ImageView) v.findViewById(R.id.iv_back);
    iv_back.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			PagerActivity.pager.setCurrentItem(2);
			
			
      }
  });
    setFont();
		return v;
	}
	private void setFont() {
//		tv_from_step3.setTypeface(RobotoLight);
		tv_1.setTypeface(RobotoLight);
		tv_2 .setTypeface(RobotoLight);
		tv_3 .setTypeface(RobotoLight);
		tv_4 .setTypeface(RobotoLight);
		
	}

public static Page4Fragment newInstance(String text) {

    Page4Fragment f = new Page4Fragment();
//    Bundle b = new Bundle();
//    b.putString("msg", text);
//
//    f.setArguments(b);

    return f;
}
}