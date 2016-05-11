package com.propkaro.util;

/* Written by: Hariom Gupta
 * Locality: Delhi(NCR)
 * Sr. Android Developer
 * Ph no: +91-8510887828
 * email: hk.mca08@gmail.com
 * facebook: HariGupta2011
 * skype: hari_gupta11
 * last modified: Mar15,2015
 * Ver: 3.3
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class UtilMaterial {
	private static String TAG = Utilities.class.getSimpleName();

	public static void setToolbarTitle(@NonNull final Toolbar toolbar) {
		final CharSequence title = toolbar.getTitle();
		final ArrayList<View> outViews = new ArrayList<View>(1);
		toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
		if (!outViews.isEmpty()) {
			final TextView titleView = (TextView) outViews.get(0);
			titleView.setGravity(Gravity.LEFT);
			final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			toolbar.requestLayout();
			Log.i(TAG, "also you can use titleView for changing font:");
			// titleView.setTypeface(Typeface);
		}
	}
}