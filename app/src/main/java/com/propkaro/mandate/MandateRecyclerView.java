package com.propkaro.mandate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.propkaro.GridActivity;
import com.propkaro.R;
import com.propkaro.util.CircularProgressView;
import com.propkaro.util.Host;
import com.propkaro.util.SetPreference;
import com.propkaro.util.Utilities;

public class MandateRecyclerView extends RecyclerView.Adapter<MandateRecyclerView.ViewHolder> {
	private Context ctx;
	private final static int FADE_DURATION = 100;
	private final static String TAG = MandateRecyclerView.class.getSimpleName();
	SetPreference setPreference;
    View rootView;
    Typeface RobotoLight;

    static FragmentActivity activity;
	SharedPreferences mPrefs;
	int location = 0;
	String getMobileNum = "", getFullName = "", getEmailID = "", getUserType = "", getUserDataID = "";
    
	List<MandateSetter> arrayList;      
    List<MandateSetter> mOriginalValues; // Original Values
    
	public MandateRecyclerView(FragmentActivity activity, ArrayList<MandateSetter> SubCategorySetter) {
//		public AvailMLRecyclerView(FragmentActivity activity, ArrayList<AvailPCSetter> SubCategorySetter) {
			this.activity = activity;
			this.ctx = activity;
			this.arrayList = SubCategorySetter;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		getMobileNum = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getFullName = mPrefs.getString("PREF_KEY_FIRST_NAME", "") + " " + mPrefs.getString("PREF_KEY_LAST_NAME", "");
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

    	rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mandat_list, null);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
		final MandateSetter m = arrayList.get(position);
//		setScaleAnimation(holder.itemView);
		String rent_sale = "";
		rent_sale = " • " + " to " + m.getProperty_listing_type();
		String floor = " • "  + m.getProperty_floor() +  " Floor ";
		if(Utilities.isStringInt(m.getProperty_floor())) {
			floor = " • "  + Utilities.ordinal(Integer.parseInt(m.getProperty_floor())) +  " Floor ";
		}
		String bedrooms = " • " + m.getNo_of_bedrooms() +  " BHK " ;
		if(m.getProperty_floor().length() < 1 || m.getProperty_floor().contains("--"))
			floor = "";
		if(m.getNo_of_bedrooms().length() < 1 || m.getNo_of_bedrooms().contains("--"))
			bedrooms = "";
		
    	holder.tv_property_id.setText("Property ID : " + m.getNEW_PROPERTY_ID() + "");
    	holder.tv_landmark.setText(Utilities.makeFirstLetterCaps(m.getProperty_landmark()));
    	holder.tv_area.setText(m.getTv_area());
    	holder.tv_city.setText(m.getProperty_city());
    	holder.tv_rate.setText("" + m.getTv_rate());
    	holder.tv_revenue.setText("Broker Revenue :  " + m.getTv_revenue());
    	holder.tv_propertyType.setText(                           
				m.getProperty_type_name_parent()
				+  rent_sale
				+ floor
				+ bedrooms
				);
    	holder.lnr_grab_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utilities.isInternetOn(activity)){
					openGrabDealDialog(v, holder.mCoordinator, m.getPROPERTY_ID() + "");
				}
			}
		});
		holder.btn_grab_deal.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
					openGrabDealDialog(holder.itemView, holder.mCoordinator, m.getPROPERTY_ID() + "");
 				}
 		});
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    class ViewHolder extends RecyclerView.ViewHolder {

    	TextView  tv_landmark, tv_area, tv_rate,  tv_propertyType,  tv_property_id, tv_revenue,tv_city;
    	CircularProgressView progressView;
    	LinearLayout lnr_grab_deal;
    	Button btn_grab_deal;
        private CoordinatorLayout mCoordinator;

        public ViewHolder(final View itemView) {
            super(itemView);
            mCoordinator = (CoordinatorLayout) itemView.findViewById(R.id.root_coordinator);
    		RobotoLight = Typeface.createFromAsset(this.itemView.getContext().getAssets(),"fonts/Roboto-Light.ttf"); 
    		
//    		mCoordinator = (CoordinatorLayout) itemView.findViewById(R.id.root_coordinator);
//    		Utilities.startAnimationThreadStuff(500, progressView);
//     		progressView.setIndeterminate(true);
//     		progressView.setVisibility(View.GONE);
    		tv_property_id = (TextView) itemView.findViewById(R.id.tv_property_id);
    		tv_landmark = (TextView) itemView.findViewById(R.id.tv_landmark);
    		tv_propertyType= (TextView) itemView.findViewById(R.id.tv_propertyType);
    		tv_rate= (TextView) itemView.findViewById(R.id.tv_rate);
    		tv_area= (TextView) itemView.findViewById(R.id.tv_area);
    		tv_city= (TextView) itemView.findViewById(R.id.tv_city);
    		tv_revenue= (TextView) itemView.findViewById(R.id.tv_revenue);
    		lnr_grab_deal= (LinearLayout) itemView.findViewById(R.id.lnr_grab_deal);
    		btn_grab_deal= (Button) itemView.findViewById(R.id.btn_grab_deal);
    		tv_property_id .setTypeface(RobotoLight);
    		tv_landmark .setTypeface(RobotoLight);
    		tv_area .setTypeface(RobotoLight);
    		tv_city.setTypeface(RobotoLight);
    		tv_rate.setTypeface(RobotoLight);
    		tv_propertyType .setTypeface(RobotoLight);
    		tv_revenue.setTypeface(RobotoLight);
    		btn_grab_deal.setTypeface(RobotoLight);
        }
    }

    String getMessage = "";
     protected void openGrabDealDialog(View view, final CoordinatorLayout mCoordinator, final String getPropertyID) {
    	 
    	final Dialog dialog = new Dialog(activity);
   		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
   		dialog.setContentView(R.layout.custom_dialog_grab_deal);
   		Button btn_signup = (Button)dialog.findViewById(R.id.btn_signup);
   		EditText et_fName = (EditText)dialog.findViewById(R.id.et_fName);
   		EditText et_emailaddr = (EditText)dialog.findViewById(R.id.et_emailaddr);
   		EditText et_mobileNum = (EditText)dialog.findViewById(R.id.et_mobileNum);

		et_fName.setEnabled(false);
		et_emailaddr.setEnabled(false);
		et_mobileNum.setEnabled(false);

		et_fName.setTypeface(RobotoLight);
		et_emailaddr.setTypeface(RobotoLight);
		et_mobileNum .setTypeface(RobotoLight);
		btn_signup .setTypeface(RobotoLight);

		et_fName.setText(getFullName);
   		et_emailaddr.setText(getEmailID);
   		et_mobileNum.setText(getMobileNum);
   		final EditText et_message = (EditText)dialog.findViewById(R.id.et_message);
   		btn_signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(et_message.getText().toString().length()==0 ){
					Toast.makeText(ctx, "Message can't be empty", Toast.LENGTH_SHORT).show();
				} else if(et_message.getText().toString().length() > 1){
					getMessage = et_message.getText().toString();
					if(Utilities.isInternetOn(activity)){
						new PostMandateTask(mCoordinator).execute(getPropertyID);
				   		dialog.dismiss();
					}
				}
			}
		});

   		dialog.show();
   	}
    
	class PostMandateTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String api_message = "", api_data;
		Dialog dialog;
		CoordinatorLayout mCoordinator;
		
		PostMandateTask(CoordinatorLayout mCoordinator){
			this.mCoordinator = mCoordinator;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utilities.Snack(mCoordinator, "Requesting..");
		}

		@Override
		protected String doInBackground(String... f_url) {
			try {
				Date date = new Date();
				String timeString = new Timestamp(date.getTime()).toString();
				String getFullName = GridActivity.getFName +" " + GridActivity.getLName, 
						getEmail = GridActivity.getEmailID, getMobileNum = GridActivity.getMobile_num;
				String PARAMS = "{\"name\":\"" + getFullName + "\",\"email\":\"" + 
				getEmail + "\",\"contact\":\""+ getMobileNum 
				+ "\",\"message\":\"" + getMessage 
				+ "\",\"timestamp\":\"" + timeString 
				+ "\",\"property_id\":\"" + f_url[0] + "\"}";
				
				String jsonData = "{\"table\":\"mandate_enq\",\"params\":" + PARAMS + "}";
				String jsonString = Utilities.sendData(Host.PostUrl, jsonData);
				JSONObject reader = new JSONObject(jsonString);

				JSONObject meta = (JSONObject) reader.get("meta");
				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					JSONObject data = (JSONObject) reader.get("data");
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
//				Toast.makeText(activity, "Your Request Posted Successfully !", 1).show();
				Utilities.Snack(mCoordinator, "Your Request Posted Successfully !");
			}
		}
	}
}
 