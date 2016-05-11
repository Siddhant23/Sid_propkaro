package com.propkaro;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONObject;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.propertycenter.AvailPCFragment;
import com.propkaro.util.BitmapUtils;
import com.propkaro.util.Host;
import com.propkaro.util.UploadUtils;
import com.propkaro.util.Utilities;

public class UploadPicFragment extends Fragment {
	static String TAG = UploadPicFragment.class.getSimpleName();
	private Button btn_selectPic, btn_uploadPic, btn_skipPic;
	TextView tv_text;
	Context ctx;
	private SharedPreferences mPrefs;
	private String getMobile_num = "", getEmailID = "", getUserType = "", 
			getUserName = "", getFName = "", getLName = "", getCity = "", getProfileImage = "";
	static String getUserDataID = "";
	private String isGridOpen = "";
	private static final int SELECT_PICTURE = 0, RESULT_LOAD_IMAGE = 1;
	private ImageView iv_profile;
	private static int RESULT_LOAD_IMG = 1;
	String imgDecodableString;
	static Bitmap bm;
	File file;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rView = inflater.inflate(R.layout.fragment_reg_upload_pic, container, false);

		btn_selectPic = (Button)rView.findViewById(R.id.btn_selectPic);
		btn_uploadPic = (Button)rView.findViewById(R.id.btn_uploadPic);
		btn_skipPic = (Button)rView.findViewById(R.id.btn_skipPic);
		iv_profile = (ImageView) rView.findViewById(R.id.iv_profile);
		iv_profile.setImageResource(R.mipmap.user_type);
		if(!getProfileImage.contains("default")){
			btn_selectPic.setText("CHANGE PICTURE");
			ImageLoader.getInstance().displayImage(getProfileImage, iv_profile, AvailPCFragment.animateFirstListener);
		}
		
		btn_selectPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Utilities.D)Log.v(TAG,"Onclick selecting..");
//				pickPhoto(arg0);
				loadImagefromGallery(arg0);
			}
		});
		
		btn_uploadPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Utilities.D)Log.v(TAG,"Onclick uploading..");
				
				if(Utilities.isInternetOn(ctx)){
					try {
						BitmapDrawable drawable = (BitmapDrawable) iv_profile.getDrawable();
						bm = drawable.getBitmap();
//						if(bm != null)
							sendPhoto(Utilities.compressImage(bm));
//						else
//							Toast.makeText(ctx, "Please select a photo", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ctx, "Please select a photo", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		btn_skipPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Utilities.D)Log.v(TAG,"Onclick skipping..");
				
				if(isGridOpen.equals("yes")){
					Intent intnt = new Intent(getActivity(), GridActivity.class);
					startActivity(intnt);
					getActivity().finish();
					getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				}else {
					getActivity().getSupportFragmentManager().popBackStack();
				}
			}
		});
		
		return rView;
	}//endOncreateView

	public void loadImagefromGallery(View view) {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && null != data) {

				Uri imageUri = data.getData();
				file = new File(BitmapUtils.getPath1(ctx, imageUri));
				Log.d(TAG, "File=" + file);
				
		        try {
		        	bm = decodeUri(imageUri);
//		        	bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//		        	bm = BitmapFactory.decodeFile(BitmapUtils.getPath(ctx, imageUri));
//					bm = Bitmap.createScaledBitmap(bm, 600, 600, true);
		        } catch (OutOfMemoryError exception) { exception.printStackTrace();
		            Toast.makeText(ctx, "Out of memory error !", Toast.LENGTH_SHORT).show();
		        }
				
				iv_profile.setImageBitmap(bm);
				iv_profile.setScaleType(ScaleType.CENTER);

				btn_selectPic.setText("CHANGE PICTURE");

			} else {
				Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
		}
	}
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 300;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
               || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);

    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		getMobile_num = mPrefs.getString("PREF_KEY_MOB_NUM", "");
		getEmailID = mPrefs.getString("PREF_KEY_EMAIL_ID", "");
		getUserType = mPrefs.getString("PREF_KEY_USER_TYPE", "");
		getUserDataID = mPrefs.getString("PREF_KEY_USER_DataID", "");

		getCity = mPrefs.getString("PREF_KEY_CITY_NAME", "");
		getUserName = mPrefs.getString("PREF_KEY_USER_NAME", "");
		getFName = mPrefs.getString("PREF_KEY_FIRST_NAME", "");
		getLName = mPrefs.getString("PREF_KEY_LAST_NAME", "");
		getProfileImage = mPrefs.getString("PREF_KEY_PROFILE_IMAGE", "");
		
		Bundle b = this.getArguments();
//		getUserDataID = b.getString("KEY_getDataID");
		isGridOpen = b.getString("KEY_isGridOpen");
		
		if(Utilities.D)Log.v(TAG,"getDataID=" + getUserDataID);
		if(Utilities.D)Log.v(TAG,"getUserType=" + getUserType);
	}//endOncrea
	
	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadPicTask().execute(bitmap);
	}
	
	private class UploadPicTask extends AsyncTask<Bitmap, Void, Void> {
		int api_code;
		String api_message, api_image;
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			Toast.makeText(ctx, "Uploading please wait..", Toast.LENGTH_SHORT).show();
			dialog = Utilities.showProgressDialog(ctx);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						
						UploadPicTask.this.cancel(true);
						dialog.dismiss();
						if(Utilities.D)Log.v(TAG,"Dialog cancelled on KEYCODE_BACK");
					}
					return false;
				}
			});

			dialog.show();
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(ctx, "Request cancelled !", Toast.LENGTH_SHORT).show();
		}
		protected Void doInBackground(Bitmap... bitmaps) {
			if (bitmaps[0] == null)
				return null;
			
			try {
				getActivity().setProgress(0);
				if(Utilities.D)Log.v(TAG,"doInBackground=");

				String params = "{\"id\":\"" + getUserDataID + "\"}";
				String jsonData = "{\"table\":\"registration\", \"params\":" + params + "}";

				String jsonString = UploadUtils.MultipartFileUploader(file, bm, Host.UploadProfileUrl, jsonData, getUserDataID, "", true, true);
//				String jsonString = Utilities.uploadPic(bm, Host.UploadProfileUrl, jsonData, getUserDataID, "", true);
				JSONObject reader = new JSONObject(jsonString);
				JSONObject meta = (JSONObject) reader.get("meta");

				api_code = meta.getInt("code");
				api_message = meta.getString("message");

				if (api_code == 200) {
					api_image = reader.getString("image");
				} else {
				}
			} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			
			if(Utilities.D)Log.v(TAG,"onProgressUpdate="+values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			if(Utilities.D)Log.v(TAG,"onPostExecute(): Uploaded succesfully !");
			Toast.makeText(ctx, "Pic Uploaded: " + api_message, Toast.LENGTH_SHORT).show();

			if(api_code == 200){
				getProfileImage = Host.MainUrl + api_image;
				if(Utilities.D)Log.v(TAG,"getProfileImage: " + getProfileImage);
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("PREF_KEY_PROFILE_IMAGE", getProfileImage);
				editor.commit();
			}
			if(isGridOpen.equals("yes")){
				Intent intnt = new Intent(getActivity(), GridActivity.class);
				startActivity(intnt);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}else {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		}
	}
}
