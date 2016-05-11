package com.propkaro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class Help_SupprtActivity extends AppCompatActivity {

	Button tv_email_info, tv_9717798987, tv_toll_free;
	TextView textView1,textView2,textView3;
	Typeface RobotoLight;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.help_support);
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
        getSupportActionBar().setTitle("Help & Support");
		RobotoLight = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Roboto-Light.ttf"); 

       
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);

		tv_email_info = (Button) findViewById(R.id.tv_email_info);

		tv_9717798987 = (Button) findViewById(R.id.tv_9717798987);
		tv_9717798987.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:+919717798987"));
				startActivity(intent);
			}
		});

		tv_toll_free = (Button) findViewById(R.id.tv_tollFree);
		tv_toll_free.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:18005324747"));
				startActivity(intent);
			}
		});
		tv_email_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(getApplicationContext(),
				// "Click On Email Button", 1).show();
				Intent email = new Intent(Intent.ACTION_SEND);
				email.setType("message/rfc822");

				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "info@propkaro.com" });

				startActivity(Intent.createChooser(email, "Send Mail"));
				getSendEmailIntent(getApplicationContext(), "", "", "");

			}
		});
		 textView1.setTypeface(RobotoLight);
		 textView2.setTypeface(RobotoLight);
		 textView3.setTypeface(RobotoLight);
	        tv_toll_free.setTypeface(RobotoLight);
			tv_9717798987 .setTypeface(RobotoLight);
			tv_email_info.setTypeface(RobotoLight);

	}

	public Intent getSendEmailIntent(Context context, String email,
			String subject, String body) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		try {
			// Explicitly only use Gmail to send
			emailIntent.setClassName("com.google.android.gm",
					"com.google.android.gm.ComposeActivityGmail");

			emailIntent.setType("message/rfc822");

			// Add the recipients
			if (email != null)
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "info@propkaro.com" });

			if (subject != null)
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						subject);

			if (body != null)
				emailIntent.putExtra(Intent.EXTRA_TEXT,
						Html.fromHtml(body));

			// Add the attachment by specifying a reference to our custom
			// ContentProvider
			// and the specific file of interest
			// emailIntent.putExtra(
			// Intent.EXTRA_STREAM,
			// Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"
			// + fileName));

			return emailIntent;
			// myContext.startActivity(emailIntent);
		} catch (Exception e) {
			emailIntent.setType("message/rfc822");

			// Add the recipients
			if (email != null)
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "info@propkaro.com" });

			if (subject != null)
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						subject);

			if (body != null)
				emailIntent.putExtra(Intent.EXTRA_TEXT,
						Html.fromHtml(body));

			// myContext.startActivity(Intent.createChooser(emailIntent,
			// "email Via"));
			return emailIntent;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showSoftKeyboard(View view) {
		if (view.requestFocus()) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}
}
