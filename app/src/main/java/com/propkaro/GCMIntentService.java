package com.propkaro;

import static com.propkaro.CommonUtilities.SENDER_ID;
import static com.propkaro.CommonUtilities.displayMessage;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;

//import com.category.notifications.NotifyGCMSetter;
//import com.category.propertycenter.DetailsPCFActivity;
//import com.category.userinfo.UserInfoFActivity;
import com.google.android.gcm.GCMBaseIntentService;
import com.propkaro.notifications.NotifyGCMSetter;
import com.propkaro.util.SetNotityPreference;
import com.propkaro.util.Utilities;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = GCMIntentService.class.getSimpleName();
	Context context;
	String message, title;
	Bitmap bmp = null;
	
	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		if(Utilities.D)Log.i(TAG, "@Device registered: regId = " + registrationId);
		SharedPreferences mPrefs; 
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("PREF_KEY_GCM_ID", registrationId);
		editor.commit();
		displayMessage(context, "registred with GCM");
		ServerUtilities.register(context, "name", "email", registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if(Utilities.D)Log.i(TAG, "Device unregistered");
//		displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		JSONObject o = null;
		this.context = context;
		int user_id = 0, property_id = 0;
		String type = "", image = "";
		String message_to = "", message_from = "";
		String connect_to = "", connect_from = "";
		try {
			String base64 = intent.getExtras().getString("message");
			byte[] data = Base64.decode(base64, Base64.DEFAULT);
			String jsonString = new String(data, "UTF-8");
//			jsonString = "{\"name\":\"gupta\",\"image\":\"https://www.propkaro.com/images/1.jpg\",\"message\":\"Hi how are you?\",\"type\":\"brodcast\"}";
			Log.w(TAG, "JsonString: " + jsonString);
			message = jsonString;
			if (jsonString != null) {
				o = new JSONObject(jsonString);
				type = o.getString("type");
				
				if(type.equals("messages")){
					message_to = o.getString("message_to");
					message_from = o.getString("message_from");
					title = o.getString("name");
					type = o.getString("type");
					message = o.getString("message");
					
					title = "Propkaro";
					user_id = Integer.parseInt(message_from);
					message = o.getString("name") + ": " + message;
				} else if(type.equals("connect_req")){
					connect_to = o.getString("connect_to");
					title = o.getString("name");
					type = o.getString("type");

					title = "Propkaro";
					message = o.getString("name") + " wants to connect with you!";
				} else if(type.equals("connect_res")){
					connect_from = o.getString("connect_from");
					title = o.getString("name");
					type = o.getString("type");

					title = "Propkaro";
					message = "You are now connected with " + o.getString("name") + ", make the most of it.";
				} else if(type.equals("brodcast")){
					title = o.getString("name");
					type = o.getString("type");
					image = o.getString("image");
					message = o.getString("message");
				} else if(type.equals("notification")){
					title = "Propkaro";
					message = title + " liked your status";
					
				} else if(type.equals("shortlist")){
					title = Utilities.makeFirstLetterCaps(o.getString("title"));
					type = o.getString("type");
					user_id = o.getInt("user_id");
//					property_id = o.getInt("property_id");
					message = Utilities.makeFirstLetterCaps(o.getString("message"));
				} else if(type.equals("requirement")){
					title = Utilities.makeFirstLetterCaps(o.getString("title"));
					type = o.getString("type");
					user_id = o.getInt("user_id");
					property_id = o.getInt("property_id");
					message = Utilities.makeFirstLetterCaps(o.getString("message"));
				}
				
//				if(Utilities.D)Log.v(TAG,"connect_to=" + connect_to);
//				if(Utilities.D)Log.v(TAG,"message_to=" + message_to);
//				if(Utilities.D)Log.v(TAG,"message_from=" + message_from);
				if(Utilities.D)Log.v(TAG,"title=" + title);
				if(Utilities.D)Log.v(TAG,"type=" + type);
				if(Utilities.D)Log.v(TAG,"message=" + message);
				try {
					SetNotityPreference setPreference = new SetNotityPreference();
					Date date = new Date();
					String timeString = new Timestamp(date.getTime()).toString();
					NotifyGCMSetter m = new NotifyGCMSetter();
					m.setUSER_ID(user_id);
					m.setPROPERTY_ID(property_id);
					m.setThumbnailUrl("thumb----------");
					m.setImageUrl("image----------");
					m.setTitle(title);
					m.setMessage(message);
					m.setType(type);
					m.setTime(timeString);
					m.setColor(0);
					m.setIsBoolEnable(true);
					setPreference.addFavorite(context, m);
					Intent i = new Intent("ReceivedServicve");
					i.putExtra("gcm_user_id", user_id);
					i.putExtra("gcm_property_id", property_id);
					i.putExtra("gcm_thumb", "thumb----------");
					i.putExtra("gcm_image", "image----------");
					i.putExtra("gcm_title", title);
					i.putExtra("gcm_message", message);
					i.putExtra("gcm_type", type);
					i.putExtra("gcm_time", timeString);
					i.putExtra("gcm_color", 0);
					i.putExtra("gcm_bool_enable", true);
				    this.sendBroadcast(i);
				} catch (Exception e) {e.printStackTrace();}
				
//				FIXME: String hari_del = "https://www.propkaro.com/images/1.jpg";
				if(type.contains("brodcast")){
			        if(image.length() < 1){
						generateNotification_new(context, message, title, type, user_id, property_id);
			        }else{
						new NotifyTask().execute(image);
			        }
				}
				else{
					generateNotification_new(context, message, title, type, user_id, property_id);
				}
					
				displayMessage(context, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		if(Utilities.D)Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		if(Utilities.D)Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		if(Utilities.D)Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	private void generateNotification_new(Context context, String message, String title, String type, int user_id, int property_id) {
		String str_title = title, app_name = context.getString(R.string.app_name);
		long when = System.currentTimeMillis();
//		Random r = new Random(4);
		int mNotificationId = 1;//r.nextInt();
//		int numMessages = 0;
		Intent resultIntent = null;
		
		if(type.contains("message")){
//			resultIntent = new Intent(context, UserInfoFActivity.class);
//			resultIntent.putExtra("KEY_GET_USER_ID", user_id + "");
		} else if(type.contains("connect_req")){
			resultIntent = new Intent(context, MainActivity.class);
			resultIntent.putExtra("KEY_NOTIFY_TYPE", "NOTIFY2");
			resultIntent.putExtra("KEY_GET_TAB", "FRIEND_REQUEST");
		} else if(type.contains("connect_res")){
			resultIntent = new Intent(context, MainActivity.class);
			resultIntent.putExtra("KEY_NOTIFY_TYPE", "NOTIFY3");
			resultIntent.putExtra("KEY_GET_TAB", "NOTIFICATION");
		} else if(type.contains("notification")){
			resultIntent = new Intent(context, MainActivity.class);
			resultIntent.putExtra("KEY_NOTIFY_TYPE", "NOTIFY4");
			resultIntent.putExtra("KEY_GET_TAB", "NOTIFICATION");
		} else if(type.contains("brodcast")){
			resultIntent = new Intent(context, MainActivity.class);
			resultIntent.putExtra("KEY_NOTIFY_TYPE", "NOTIFY5");
		} else if(type.contains("shortlist")){
//			resultIntent = new Intent(context, UserInfoFActivity.class);
//			resultIntent.putExtra("KEY_GET_USER_ID", user_id + "");
		} else if(type.contains("requirement")){
//			resultIntent = new Intent(context, DetailsPCFActivity.class);
//			resultIntent.putExtra("KEY_PROPERTY_ID", property_id + "");
		}
		resultIntent.setAction(Intent.ACTION_MAIN);
		resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		try {
		    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//			Notification notification = mBuilder
		    mBuilder.setTicker(message).setWhen(when).setAutoCancel(true).setContentTitle(str_title).setContentText(message)
					.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//					.setPriority(NotificationCompat.PRIORITY_MAX)
					.setSmallIcon(getNotificationIcon());

//		    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//		    String[] events = new String[1];
//		    events[0] = new String(message);
//		    inboxStyle.setBigContentTitle(str_title);
//		    for (int i=0; i < events.length; i++) {
//		       inboxStyle.addLine(events[i]);
//		    }
//		    mBuilder.setStyle(inboxStyle);
		    
			mBuilder.setStyle(
					new NotificationCompat.BigTextStyle()
					.setBigContentTitle(app_name)
					.setSummaryText(str_title)
					.bigText(message))
					.setLights(Color.GREEN, 300, 300)
					.setVibrate(new long[] { 100, 250 , 250 })
					.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

			mBuilder.setContentIntent(resultPendingIntent);

		    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(mNotificationId, mBuilder.build());
		} catch (Exception e) { e.printStackTrace(); }
	}
	private int getNotificationIcon() {
	    boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
	    return useWhiteIcon ? R.mipmap.ic_notify_small : R.mipmap.ic_launcher;
	}

	private void noti_temp(Context context, String message, String title, Bitmap bm) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	     Intent resultIntent = new Intent(context, MainActivity.class);
	     TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	     stackBuilder.addParentStack(MainActivity.class);
	     stackBuilder.addNextIntent(resultIntent);
	     resultIntent.setAction(Intent.ACTION_MAIN);
	     resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	     
	     PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//	     notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	     RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_notify);
	     expandedView.setImageViewBitmap(R.id.iv_notify, bm);
//	     expandedView.setImageViewBitmap(R.id.iv_notify_app_icon, bm);
//	     expandedView.setBitmap(R.layout.custom_notify, "android:src", bm);
	     expandedView.setTextViewText(R.id.tv_notify_title, title);
	     expandedView.setTextViewText(R.id.tv_notify_details, message);
	     expandedView.setTextViewText(R.id.tv_notify_time, 
	    		 (String) DateFormat.format("hh:mm aaa",Calendar.getInstance().getTime()));
	     
	     Notification builder = new Notification.Builder(context)
//	     .setPriority(NotificationCompat.PRIORITY_MAX)
          .setSmallIcon(getNotificationIcon())
          .setWhen(System.currentTimeMillis())
          .setAutoCancel(true)
          .setDefaults(Notification.DEFAULT_ALL)
          .setTicker(message)//.setNumber(++numMessages)
          .setContentIntent(resultPendingIntent)
          .setContentText(message)
          .setContentTitle(title).build();
	     builder.bigContentView = expandedView;
	     
//	     Notification notification = new Notification.BigPictureStyle()
//	     .bigPicture(
//	       BitmapFactory.decodeResource(getResources(), bm)).build();

//	        builder.setLatestEventInfo(context, title, message, resultPendingIntent);
	        builder.flags |= Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP;
	        builder.flags |= Notification.FLAG_AUTO_CANCEL;
	        builder.defaults=Notification.FLAG_ONLY_ALERT_ONCE+Notification.FLAG_AUTO_CANCEL;
	        builder.defaults |= Notification.DEFAULT_LIGHTS;
	        builder.defaults |= Notification.DEFAULT_SOUND;
	        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
	        builder.defaults |= Notification.DEFAULT_VIBRATE;
	        notificationManager.notify(0, builder);
	}

	private void noti_temp1(Context context, String message, String title, Bitmap bm) {
	     Intent resultIntent = new Intent(context, MainActivity.class);
	     TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	     stackBuilder.addNextIntent(resultIntent);
	     resultIntent.setAction(Intent.ACTION_MAIN);
	     resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	     resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	     resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	     
	     PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//	     notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	     RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_notify);
	     expandedView.setImageViewBitmap(R.id.iv_notify, bm);
//	     expandedView.setImageViewBitmap(R.id.iv_notify_app_icon, bm);
//	     expandedView.setBitmap(R.layout.custom_notify, "android:src", bm);
	     expandedView.setTextViewText(R.id.tv_notify_title, title);
	     expandedView.setTextViewText(R.id.tv_notify_details, message);
	     expandedView.setTextViewText(R.id.tv_notify_time, 
	    		 (String) DateFormat.format("hh:mm aaa",Calendar.getInstance().getTime()));
	     
	     NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	     builder.setPriority(NotificationCompat.PRIORITY_MAX)
	     .setTicker(title)//.setNumber(++numMessages)
         .setContentTitle(title).setContentText(message)
         .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
         .setSmallIcon(getNotificationIcon())
         .setWhen(System.currentTimeMillis())
         .setAutoCancel(true)

//         .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
         .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm))
         
         .setContentIntent(resultPendingIntent)
	     .setContent(expandedView)
//         .setDefaults(Notification.DEFAULT_ALL)
					.setLights(Color.GREEN, 300, 300)
					.setVibrate(new long[] { 100, 250, 250 , 250 , 250 , 250 })
					.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
 		 .setColor(Color.RED)
//		 .setNumber()
         .setContentInfo("Propkaro");
	     
	        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	        notificationManager.notify(0, builder.build());
	}
	class NotifyTask extends AsyncTask<String, String, String> {
		int api_code = 0, getPropertyID = 0;
		public String api_message = "", api_data;
			
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... f_url) {
			if(Utilities.D)Log.v(TAG,"doInBackground=======Notify");
			try {
				bmp = Utilities.getBitmapFromURL(f_url[0]);
			} catch (Exception e) { e.printStackTrace(); }
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			if(Utilities.D)Log.v(TAG,"PostExe()=======notify_download+bitmap");
        	noti_temp(context, message, title, bmp);
		}
	}
}