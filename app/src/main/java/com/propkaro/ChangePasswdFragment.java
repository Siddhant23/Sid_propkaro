package com.propkaro;

import org.json.JSONObject;

import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangePasswdFragment extends Fragment {
	private String TAG = ChangePasswdFragment.class.getSimpleName();

	private Button btn_chnge_pwd;
	private String getNewPassword = "", getCnfirm_pwd = "", getDataID = "", isGridOpen = "", getUserType = "";

	private EditText et_new_pwd, et_cnfirm_pwd;
	private Context ctx; private FragmentActivity fActivity;
	private SharedPreferences mPrefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rView = inflater.inflate(R.layout.change_pwd, container, false);
		Log.d(TAG, "Fragment.onCreateView()");
		Toolbar mToolbar = (Toolbar) rView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//        activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                getActivity().finish();
            }
        });
        activity.getSupportActionBar().setTitle("Change Password");

		et_new_pwd = (EditText) rView.findViewById(R.id.et_new_pwd);
		et_cnfirm_pwd = (EditText) rView.findViewById(R.id.et_cnfirm_pwd);

		btn_chnge_pwd = (Button) rView.findViewById(R.id.btn_chnge_pwd);
		btn_chnge_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.hideKeyboard(getActivity());
				getNewPassword = et_new_pwd.getText().toString().trim();
				getCnfirm_pwd = et_cnfirm_pwd.getText().toString().trim();
				
				if (getNewPassword.length() < 5) {
					Toast.makeText(getActivity(),
						  "Please enter minimum 5 digit password !", Toast.LENGTH_SHORT).show();
				  
				  } else if (!getNewPassword.matches(getCnfirm_pwd)) {
					  Toast.makeText(ctx, "Please confirm password", Toast.LENGTH_SHORT) .show();
				  }	  
				  else if (Utilities.isInternetOn(ctx) == true) {
					  new ChangePasswordTask().execute("");
				  } else
					  if(Utilities.D)Log.v(TAG,"No internet connection.. ");
				}
		});
		
		return rView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = getActivity();
		fActivity = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		
		Bundle b = this.getArguments();
		isGridOpen = b.getString("KEY_isGridOpen", "");
	}
	
	class ChangePasswordTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String userId = "", userName = "", api_message = "";

		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			 dialog = Utilities.showProgressDialog(ctx, ChangePasswordTask.this);
			 dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
		}
		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"getDataID" + getDataID);
			try {
					String CHNG_PWD_USER = "{" 
							+ "\"password\":\"" + getCnfirm_pwd + "\"" 
							+ "}";

					String str_send_json = "{\"by\":{\"id\":\"" + getDataID
							+ "\"}, \"table\":\"registration\", \"params\":"
							+ CHNG_PWD_USER + "}";

					// {by:{id:1,email:'xxx.com'},table:'registration',multiple:1};

				String UrlBase = Host.UpdateUrl + "";
				String jsonString = Utilities.sendData(UrlBase, str_send_json);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			 dialog.dismiss();

			if (api_code == 200) {
				Utilities.replaceFragment(fActivity, new LoginFragment(), R.id.container, true, false);
				Toast.makeText(ctx, "Password Changed Successfully, please login !", Toast.LENGTH_SHORT).show();

			} else if (api_code == 400) {
				Toast.makeText(ctx, api_message, Toast.LENGTH_SHORT).show();
				
			} else {
				Toast.makeText(ctx, "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
			}
			if(isGridOpen.equals("yes")){
				Intent intnt = new Intent(getActivity(), GridActivity.class);
				startActivity(intnt);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			} 
		}
	}
}
