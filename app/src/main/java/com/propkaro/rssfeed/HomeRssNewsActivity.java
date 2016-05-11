package com.propkaro.rssfeed;

import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.propkaro.R;
import com.propkaro.util.Utilities;

public class HomeRssNewsActivity extends Activity implements 
		AsyncTaskCompletionListener, OnClickListener, OnItemClickListener{
	
	static String TAG = HomeRssNewsActivity.class.getSimpleName();
	private ListView listview;
	public static String getURL = "";
	String _subCategory = "", _category = "";
	ImageView _iv_back, _iv_share;
	TextView _tv_heading_desc;
	Intent in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rss_reader);
		in = this.getIntent();
	    getURL = in.getStringExtra(("_getURL"));
	    Log.v("TAG", "CheckUrlHomeNews=" + getURL);
	    
	    _subCategory = in.getStringExtra(("_subCategory"));
	    _category = in.getStringExtra(("_category"));
	
//		 adview=new AdView(this, null);
//		 adview.setAdSize(AdSize.BANNER);
//		 adview.setAdUnitId(AD_UNIT_ID);
		listview=(ListView)findViewById(R.id.rssfeed_listview);
		listview.setOnItemClickListener(this);
		
		if(Utilities.isInternetOn(getApplicationContext()))
			new RssFeedAsyncTask(this).execute(getURL);
		else
			Log.v(getURL, "No Internet Connection !");
	    _iv_back = (ImageView) findViewById(R.id.iv_back);
		_iv_back.setOnClickListener(this);
	//	_iv_share = (ImageView) findViewById(R.id.iv_share);
		//_iv_share.setOnClickListener(this);
	//	_tv_heading_desc = (TextView) findViewById(R.id.tv_heading_desc);
	//	_tv_heading_desc.setText(_category+" >> "+_subCategory);
	}//endOncrea

	@Override
	public void onTaskComplete(List<RssFeedStructure> result) {
		RssReaderListAdapter _adapter= new RssReaderListAdapter(HomeRssNewsActivity.this, result);
		listview.setAdapter(_adapter);
	}
	@Override
	public void onClick(View v) {
		if(v == _iv_back){
			HomeRssNewsActivity.this.finish();
//		} else if(v == _iv_home){
//			SearchListDetailsActivity.this.finish();
//			Intent home = new Intent(SearchListDetailsActivity.this, HomeActivity.class);
//			startActivity(home);
		/*} else if(v == _iv_share){
			System.out.println("sharing.......");
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//			sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//		    sharingIntent.setType("image/*");
		    // For a file in shared storage.  For data in private storage, use a ContentProvider.
//		    Uri uri = Uri.fromFile(getFileStreamPath(pathToImage));
//		    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
		    sharingIntent.setType("text/plain");
			String shareBody = "sharing....... texts";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CrazyApp Sharing...");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		if(Utilities.D)Log.v(TAG,"RssReaderListAdapter.str_link===" + RssReaderListAdapter.str_link);
		Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class)
		.putExtra("getURL", RssReaderListAdapter.str_link);
       	startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(Utilities.D)Log.v(TAG,"event=" + event + "*" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
