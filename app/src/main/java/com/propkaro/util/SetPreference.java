package com.propkaro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.propkaro.propertycenter.AvailPCSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPreference {
	public static final String TAG = SetPreference.class.getSimpleName();
	public static final String PREFS_NAME = "AvailPCSetter_APP";
	public static final String FAVORITES = "AvailPCSetter_NewsFeed";
	
	public SetPreference() {
		super();
	}

	public void saveFavorites(Context context, List<AvailPCSetter> favorites) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = settings.edit();
		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);
		editor.putString(FAVORITES, jsonFavorites);
		editor.commit();
	}

	public void addFavorite(Context context, AvailPCSetter property) {
		List<AvailPCSetter> fav = getFavorites(context);
		boolean shouldChange = true;
		if (fav == null)
			fav = new ArrayList<AvailPCSetter>();
		for(int i = 0; i < fav.size(); i++){
			if(property.getPROPERTY_ID() == fav.get(i).getPROPERTY_ID()){
				shouldChange = false;
				if(Utilities.D)Log.v(TAG,"This property won't be added");
			}
		}
		if(Utilities.D)Log.v(TAG,"ITEM: " + property);
		if(shouldChange){
			fav.add(property);
			saveFavorites(context, fav);
			if(Utilities.D)Log.v(TAG,"ITEM ADDED: " + fav);
		}else
			if(Utilities.D)Log.v(TAG,"ITEM ALREADY ADDED: " + fav);
	}

	public void removeFavorite(Context context, AvailPCSetter property) {
		ArrayList<AvailPCSetter> fav = getFavorites(context);
		if(Utilities.D)Log.v(TAG,"ITEM: " + property);
		if (fav != null) {
			fav.remove(property);
			saveFavorites(context, fav);
			if(Utilities.D)Log.v(TAG,"ITEM=REMOVED:"+fav);
		}
	}

	public ArrayList<AvailPCSetter> getFavorites(Context context) {
		SharedPreferences settings;
		List<AvailPCSetter> fav;

		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			AvailPCSetter[] favoriteItems = gson.fromJson(jsonFavorites, AvailPCSetter[].class);

			fav = Arrays.asList(favoriteItems);
			fav = new ArrayList<AvailPCSetter>(fav);
		} else
			return null;

		return (ArrayList<AvailPCSetter>) fav;
	}
	public void emptyFavorites (Context context) {
//		SharedPreferences settings;
//		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//		List<AvailPCSetter> favorites = new ArrayList<AvailPCSetter>();
//
//		if (settings.contains(FAVORITES)) {
//			String jsonFavorites = settings.getString(FAVORITES, null);
//			Gson gson = new Gson();
//			AvailPCSetter[] favoriteItems = gson.fromJson(jsonFavorites,
//					AvailPCSetter[].class);
//
//			favorites = Arrays.asList(favoriteItems);
//			favorites = new ArrayList<AvailPCSetter>(favorites);
//		} else {
//			favorites = new ArrayList<AvailPCSetter>(favorites);
//		}
		List<AvailPCSetter> fav = getFavorites(context);
		if (fav == null)
			fav = new ArrayList<AvailPCSetter>();

		fav.clear();
		saveFavorites(context, fav);
		if(Utilities.D)Log.v(TAG,"All items removed successfully: " + fav);
	}
	public int sizeFavorites (Context context) {
		List<AvailPCSetter> fav = getFavorites(context);
		if (fav == null){
			fav = new ArrayList<AvailPCSetter>();
			return 0;
		} else
			return fav.size();
	}
}
