package com.propkaro.rssfeed;

import java.util.List;

import com.propkaro.util.Utilities;


import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;


public class RssFeedAsyncTask extends
		AsyncTask<String, Void, List<RssFeedStructure>> {
//	private ProgressDialog Dialog;
	Dialog dialog;
	String response = "";
	List<RssFeedStructure> rssStr;
	Activity _context;
	private AsyncTaskCompletionListener callback;

	public RssFeedAsyncTask(Activity _context) {
		this._context = _context;
		this.callback = (AsyncTaskCompletionListener) _context;
	}

	@Override
	protected void onPreExecute() {
//		Dialog = new ProgressDialog(_context);
//		Dialog.setMessage("Loading...");
//		Dialog.show();
		dialog = Utilities.showProgressDialog(_context);
		dialog.show();

	}

	@Override
	protected List<RssFeedStructure> doInBackground(String... urls) {
		try {
			String feed = urls[0];
			XmlHandler rh = new XmlHandler();
			rssStr = rh.getLatestArticles(feed);
		} catch (Exception e) {
		}
		return rssStr;

	}

	@Override
	protected void onPostExecute(List<RssFeedStructure> result) {
//		Dialog.dismiss();
		dialog.dismiss();
		callback.onTaskComplete(result);

	}

}
