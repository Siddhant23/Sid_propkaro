package com.propkaro.mandate;

import android.graphics.Typeface;
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
import com.propkaro.util.Utilities;

public class Page1Fragment extends Fragment {
	static String TAG = Page1Fragment.class.getSimpleName();
	Typeface RobotoLight;
	TextView tv_from_step3,tv_1 ,tv_2,tv_3,tv_4;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mandate_1, container, false);
		RobotoLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf"); 

		     tv_from_step3= (TextView) v.findViewById(R.id.tv_from_step3);
		     tv_1 = (TextView) v.findViewById(R.id.tv_1); 
			 tv_2 = (TextView) v.findViewById(R.id.tv_2); 
			 tv_3 = (TextView) v.findViewById(R.id.tv_3); 
			 tv_4 = (TextView) v.findViewById(R.id.tv_4);
          ImageView   iv_forward = (ImageView) v.findViewById(R.id.iv_forward);
          iv_forward.setOnClickListener(new OnClickListener() {

  			@Override
  			public void onClick(View v) {
				if(Utilities.D)Log.v(TAG, "click-----------secondfrag1");
				PagerActivity.pager.setCurrentItem(1);
				if(Utilities.D)Log.v(TAG, "click-----------secondfrag");
            }
        });
          ImageView   iv_back = (ImageView) v.findViewById(R.id.iv_back);
          iv_back.setOnClickListener(new OnClickListener() {

      		@Override
      		public void onClick(View v) {
      			getActivity().finish();
    			getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
          setFont();
  		return v;
  	}
  	private void setFont() {
//  		tv_from_step3.setTypeface(RobotoLight);
  		tv_1.setTypeface(RobotoLight);
  		tv_2 .setTypeface(RobotoLight);
  		tv_3 .setTypeface(RobotoLight);
  		tv_4 .setTypeface(RobotoLight);
  		
  	}
    public static Page1Fragment newInstance(String text) {

        Page1Fragment f = new Page1Fragment();
        Bundle b = new Bundle();
//        b.putString("msg", text);

//        f.setArguments(b);

        return f;
    }

}