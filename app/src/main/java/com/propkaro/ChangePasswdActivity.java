package com.propkaro;

import org.json.JSONObject;

import com.propkaro.util.Host;
import com.propkaro.util.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class ChangePasswdActivity extends AppCompatActivity {
	private String TAG = ChangePasswdActivity.class.getSimpleName();

	private Button btn_chnge_pwd;
	private String getNewPassword = "", getCnfirm_pwd = "", getDataID = "";

	private EditText et_new_pwd, et_cnfirm_pwd;
	private Context ctx;
	private SharedPreferences mPrefs;
    private CoordinatorLayout mCoordinator;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ctx = this; mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		setContentView(R.layout.change_pwd);
	    mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      getWindow().setStatusBarColor(getResources().getColor(R.color.propkaro_color));
//  }
//        activity.mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        getSupportActionBar().setTitle("Change Password");

		et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
		et_cnfirm_pwd = (EditText) findViewById(R.id.et_cnfirm_pwd);

		Bundle b = getIntent().getExtras();
		getDataID = b.getString("KEY_getDataID", "");

		Log.d(TAG, "=====et_new_pwd=" + getNewPassword);
		Log.d(TAG, "=====et_cnfirm_pwd=" + getCnfirm_pwd);
		Log.d(TAG, "=====@getDataID=" + getDataID);

		btn_chnge_pwd = (Button) findViewById(R.id.btn_chnge_pwd);
		btn_chnge_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNewPassword = et_new_pwd.getText().toString().trim();
				getCnfirm_pwd = et_cnfirm_pwd.getText().toString().trim();
				
				if (getNewPassword.length() < 5) {
					Utilities.Snack(mCoordinator, "Please enter minimum 5 digit password !");
				  
				  } else if (!getNewPassword.matches(getCnfirm_pwd)) {
		    			Utilities.Snack(mCoordinator, "Please confirm password");
				  }	  
				  else if (Utilities.isInternetOn(ctx) == true) {
					  new ChangePasswordTask().execute("");

				} else
					if(Utilities.D)Log.v(TAG,"No internet connection.. ");
			}
		});
	};

	class ChangePasswordTask extends AsyncTask<String, String, String> {
		int api_code = 0;
		public String userId = "", userName = "", api_message = "";

		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			 dialog = Utilities.showProgressDialog(ChangePasswdActivity.this, ChangePasswordTask.this);
			 dialog.show();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Utilities.Snack(mCoordinator, "Request cancelled !");
		}
		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"getDataID" + getDataID);
			try {
					String CHNG_PWD_USER = "{" + "\"password\":\""
							+ getCnfirm_pwd + "\"}";

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
				Utilities.Snack(mCoordinator, "Password Changed: " + api_message);
				
			} else if (api_code == 400) {
				Utilities.Snack(mCoordinator, "api_message");
				
			} else {
				Utilities.Snack(mCoordinator, "Something went wrong, please try again later !");
			}
			ChangePasswdActivity.this.finish();
		}
	}
}
