package com.propkaro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InviteActivity extends AppCompatActivity {
	private static final int PICK_CONTACT = 0;
	private Button  btn_Whatsapp, btn_Sms, btn_Email;
	TextView tv_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite);
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
        getSupportActionBar().setTitle("Facilitation Center");

//		btn_Phonebook = (Button) findViewById(R.id.btn_Phonebook);
		btn_Whatsapp = (Button) findViewById(R.id.btn_Whatsapp);
		btn_Sms = (Button) findViewById(R.id.btn_Sms);
		btn_Email = (Button) findViewById(R.id.btn_Email);

//		btn_Phonebook.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(Intent.ACTION_PICK,
//						Contacts.CONTENT_URI);
//
//				startActivityForResult(intent, PICK_CONTACT);
//			}
//		});

		btn_Whatsapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),
				// "Click On Whatsapp  Button",1).show();

				onClickWhatsApp(v);
			}
		});
		
		
		btn_Sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(getApplicationContext(),
				// "Click On SMS Button", 1).show();
				Uri tlf = Uri.parse("smsto:" + "");
				Intent c = new Intent(Intent.ACTION_VIEW, tlf);
				c.setData(tlf);

				c.putExtra("sms_body", "Hi, you are invited to join the biggest peer to peer real estate network. https://play.google.com/store/apps/details?id=com.propkaro");
				startActivity(c);
				
			}
		});

		btn_Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(getApplicationContext(),
				// "Click On Email Button", 1).show();
				String message =  "Hi, you are invited to join the biggest peer to peer real estate network. https://play.google.com/store/apps/details?id=com.propkaro";

				Intent email = new Intent(Intent.ACTION_SEND);
				
				email.setType("message/rfc822");
				email.putExtra(Intent.EXTRA_SUBJECT, "Share PropKaro");

 				email.putExtra(Intent.EXTRA_TEXT, message);
				email.putExtra(Intent.EXTRA_EMAIL,new String[] {  });
				startActivity(Intent.createChooser(email, "Send Mail"));
				getSendEmailIntent(getApplicationContext(), "", "", "");

			}
		});
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
						new String[] {  });

			if (subject != null)
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Hi, you are invited to join the biggest peer to peer real estate network. https://play.google.com/store/apps/details?id=com.propkaro");

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
						new String[] {  });

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
	}// endOncreate

	/*
	 * private void showGPSDisabledAlertToUser(){ AlertDialog.Builder
	 * alertDialogBuilder = new AlertDialog.Builder(this);
	 * alertDialogBuilder.setMessage
	 * ("GPS is disabled in your device. Would you like to enable it?")
	 * .setCancelable(false)
	 * .setPositiveButton("Goto Settings Page To Enable GPS", new
	 * DialogInterface.OnClickListener(){ public void onClick(DialogInterface
	 * dialog, int id){ Intent callGPSSettingIntent = new Intent(
	 * android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	 * startActivity(callGPSSettingIntent); } });
	 * alertDialogBuilder.setNegativeButton("Cancel", new
	 * DialogInterface.OnClickListener(){ public void onClick(DialogInterface
	 * dialog, int id){ dialog.cancel(); } }); AlertDialog alert =
	 * alertDialogBuilder.create(); alert.show(); }
	 */
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// TODO Fetch other Contact details as you want to use

				}
			}
			break;
		}
	}

	public void onClickWhatsApp(View view) {

		PackageManager pm = getPackageManager();
		try {

			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			String text = "Hi, you are invited to join the biggest peer to peer real estate network. https://play.google.com/store/apps/details?id=com.propkaro";

			PackageInfo info = pm.getPackageInfo("com.whatsapp",
					PackageManager.GET_META_DATA);
			// Check if package exists or not. If not then code
			// in catch block will be called
			waIntent.setPackage("com.whatsapp");

			waIntent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(waIntent, "Share with"));

		} catch (NameNotFoundException e) {
			Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
					.show();
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
