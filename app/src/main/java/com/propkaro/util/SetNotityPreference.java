package com.propkaro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.propkaro.notifications.NotifyGCMSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetNotityPreference {
	public static final String TAG = SetNotityPreference.class.getSimpleName();
	public static final String PREFS_NAME = "NotifyGCMSetter_APP";
	public static final String FAVORITES = "NotifyGCMSetter_Notification";
	
	public SetNotityPreference() {
		super();
	}

	public void saveFavorites(Context context, List<NotifyGCMSetter> favorites) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = settings.edit();
		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);
		editor.putString(FAVORITES, jsonFavorites);
		editor.commit();
	}

	public void addFavorite(Context context, NotifyGCMSetter property) {
		List<NotifyGCMSetter> fav = getFavorites(context);
		boolean shouldChange = true;
		if (fav == null)
			fav = new ArrayList<NotifyGCMSetter>();
//		for(int i = 0; i < fav.size(); i++){
//			if(property.getPROPERTY_ID() == fav.get(i).getPROPERTY_ID()){
//				shouldChange = false;
//				if(Utilities.D)Log.v(TAG,"This property won't be added");
//			}
//		}
		if(Utilities.D)Log.v(TAG,"NOTIFICATION_ITEM: " + property);
		if(shouldChange){
			fav.add(0, property);
			saveFavorites(context, fav);
			if(Utilities.D)Log.v(TAG,"NOTIFICATION ITEM ADDED: " + fav);
		}else
			Toast.makeText(context, "Property already added !", Toast.LENGTH_SHORT).show();
	}

	public void removeFavorite(Context context, NotifyGCMSetter property) {
		List<NotifyGCMSetter> fav = getFavorites(context);
		if(Utilities.D)Log.v(TAG,"NOTIFICATION: " + property);
		if (fav != null) {
			fav.remove(property);
			saveFavorites(context, fav);
			if(Utilities.D)Log.v(TAG,"NOTIFICATION=REMOVED:"+fav);
		}
	}

	public List<NotifyGCMSetter> getFavorites(Context context) {
		SharedPreferences settings;
		List<NotifyGCMSetter> fav;

		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			NotifyGCMSetter[] favoriteItems = gson.fromJson(jsonFavorites, NotifyGCMSetter[].class);

			fav = Arrays.asList(favoriteItems);
			fav = new ArrayList<NotifyGCMSetter>(fav);
		} else
			return null;

		return (List<NotifyGCMSetter>) fav;
	}

	public void emptyFavorites (Context context) {
//		SharedPreferences settings;
//		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//		List<NotifyGCMSetter> favorites = new ArrayList<NotifyGCMSetter>();
//
//		if (settings.contains(FAVORITES)) {
//			String jsonFavorites = settings.getString(FAVORITES, null);
//			Gson gson = new Gson();
//			NotifyGCMSetter[] favoriteItems = gson.fromJson(jsonFavorites,
//					NotifyGCMSetter[].class);
//
//			favorites = Arrays.asList(favoriteItems);
//			favorites = new ArrayList<NotifyGCMSetter>(favorites);
//		} else {
//			favorites = new ArrayList<NotifyGCMSetter>(favorites);
//		}
		List<NotifyGCMSetter> fav = getFavorites(context);
		if (fav == null)
			fav = new ArrayList<NotifyGCMSetter>();

		fav.clear();
		saveFavorites(context, fav);
		if(Utilities.D)Log.v(TAG,"All notification removed successfully: " + fav);
	}
	public int sizeFavorites (Context context) {
		List<NotifyGCMSetter> fav = getFavorites(context);
		if (fav == null){
			fav = new ArrayList<NotifyGCMSetter>();
			return 0;
		} else
			return fav.size();
	}
}
