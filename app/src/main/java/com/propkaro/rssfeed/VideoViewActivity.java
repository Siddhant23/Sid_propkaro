package com.propkaro.rssfeed;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.propkaro.R;
//import com.parse.ParseAnalytics;
public class VideoViewActivity extends AppCompatActivity implements OnClickListener{

private VideoEnabledView webView;
private VideoEnabledWebChromeClient webChromeClient;

private boolean fullscreen=false;
public final static int MENU_FULLSCREEN_ON = 1113;
public final static int MENU_FULLSCREEN_OFF = 1114;
public final static int MENU_HOMEPAGE = 1115;
public final static int MENU_PAUSE = 1116;
public final static int MENU_RESUME = 1117;
public final static int MENU_REFRESH = 1118;
public final static int MENU_GO_BACK = 1119;
public final static int MENU_GO_FORWARD = 1120;
public final static int MENU_CLEAR_CACHE = 1121;
public final static int MENU_CLEAR_HISTORY = 1122;
public final static int MENU_DOWNLOADS = 1123;
public final static int MENU_ABOUT = 1124;
public final static int MENU_EXIT = 1125;
//private GoogleAnalytics mInstance;
//private Tracker mTracker,mTracker1,mTracker2;
int currApiV = Build.VERSION.SDK_INT;
//private WebView webView;
	WebBackForwardList mWebBackForwardList;
	private EditText urlEditText;
	private ProgressBar progress;
	private String TAG=VideoViewActivity.INPUT_METHOD_SERVICE;
	private String urls="",title = "", historyUrl="";
	private List<String> urlList = new ArrayList<String>();
	private Bundle extras;
	ImageView openUrl;
    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private CoordinatorLayout mCoordinator;

//	private ProgressDialog progressDialog;
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ParseAnalytics.trackAppOpened(getIntent());
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Get the GoogleAnalytics singleton.
//	    mInstance = GoogleAnalytics.getInstance(this);
//	    mTracker = mInstance.getDefaultTracker();
////	    mTracker1 = mInstance.getTracker("UA-53073451-1");
//	    mTracker2 = mInstance.getTracker("UA-53073451-6");
////	    mInstance.setDefaultTracker(mTracker1);
//	    mInstance.setDefaultTracker(mTracker2);
        extras=this.getIntent().getExtras();
       if(extras!=null){
           urls=extras.getString("getURL", "");
           title=extras.getString("getTitle", "");
        }
//============================================
//        webView=new VideoEnabledWebView(this);
//        if (savedInstanceState != null) {
//            webView.restoreState(savedInstanceState);
//        } 
//        else {    
////          webView.loadUrl("http://www.xmaaza.in/");
//          webView.loadUrl(urls);
//        }
//============================================		        
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        setFullscreen();//setViews here
        setContentView(R.layout.videoview);
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
        getSupportActionBar().setTitle(title);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

        // This will handle downloading. It requires Gingerbread, though
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        // This is where downloaded files will be written, using the package name isn't required
        // but it's a good way to communicate who owns the directory
        final File destinationDir = new File (Environment.getExternalStorageDirectory(), getPackageName());
        if (!destinationDir.exists()) {
            destinationDir.mkdir(); // Don't forget to make the directory if it's not there
        }
        
//      initializeUrlField();
		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);
		urlEditText = (EditText) findViewById(R.id.urlField);
		urlEditText.setText(urls);
		
	    webView = (VideoEnabledView) findViewById(R.id.webView);
	    // Initialize the VideoEnabledWebChromeClient and set event handlers
	    View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
	    ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
	    View loadingView = getLayoutInflater().inflate(R.layout.video_loading, null); // Your own view, read class comments

	    webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
	    {
	        // Subscribe to standard events, such as onProgressChanged()...
	        @Override
	        public void onProgressChanged(WebView view, int progress)
	        {
	            // Your code...
				VideoViewActivity.this.setValue(progress);
	        }
	    };
	    webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
	    {
	        @Override
	        public void toggledFullscreen(boolean fullscreen)
	        {
	            // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
	            if (fullscreen)
	            {
	                WindowManager.LayoutParams attrs = getWindow().getAttributes();
	                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	                attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	                getWindow().setAttributes(attrs);
	                if (Build.VERSION.SDK_INT >= 14)
	                {
	                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	                }
	            }
	            else
	            {
	                WindowManager.LayoutParams attrs = getWindow().getAttributes();
	                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
	                attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	                getWindow().setAttributes(attrs);
	                if (Build.VERSION.SDK_INT >= 14)
	                {
	                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
	                }
	            }

	        }
            
           // openFileChooser for Android 3.0+
           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){ 
               
               // Update message
               mUploadMessage = uploadMsg;
               try{   
                   // Create AndroidExampleFolder at sdcard
                   File imageStorageDir = new File(
                                          Environment.getExternalStoragePublicDirectory(
                                          Environment.DIRECTORY_PICTURES)
                                          , "AndroidExampleFolder");
                                           
                   if (!imageStorageDir.exists()) {
                       // Create AndroidExampleFolder at sdcard
                       imageStorageDir.mkdirs();
                   }
                    
                   // Create camera captured image file path and name
                   File file = new File(
                                   imageStorageDir + File.separator + "IMG_"
                                   + String.valueOf(System.currentTimeMillis())
                                   + ".jpg");
                                    
                   mCapturedImageURI = Uri.fromFile(file);
                    
                   // Camera capture image intent
                   final Parcelable captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                  
                   ((Intent) captureIntent).putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                   
                   Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                   i.addCategory(Intent.CATEGORY_OPENABLE);
                   i.setType("image/*");
                    
                   // Create file chooser intent
                   Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                    
                   // Set camera intent to file chooser
                   chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });
                    
                   // On select image call onActivityResult method of activity
                   startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                    
                 }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Exception:"+e,Toast.LENGTH_LONG).show();
                }
           }
            
           // openFileChooser for Android < 3.0
           public void openFileChooser(ValueCallback<Uri> uploadMsg){openFileChooser(uploadMsg, "");
           }
            
           //openFileChooser for other Android versions
           public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType,String capture) {
                                       
               openFileChooser(uploadMsg, acceptType);
           }

           // The webPage has 2 filechoosers and will send a
           // console message informing what action to perform,
           // taking a photo or updating the file
            
           public boolean onConsoleMessage(ConsoleMessage cm) { 
                  
               onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
               return true;
           }
            
           public void onConsoleMessage(String message, int lineNumber, String sourceID) {
               //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);
                
           }
	    });
	    
       //override the web client to open all links in the same webview
		webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyWebChromeClient());

		mWebBackForwardList = webView.copyBackForwardList();
		
		WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDefaultZoom(ZoomDensity.MEDIUM);
        settings.setSupportMultipleWindows(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
 
        settings.setPluginState(PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(1);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);  
        //true makes the Webview have a normal viewport such as a normal desktop browser
        //when false the webview will have a viewport constrained to it's own dimensions
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(false);
        settings.setPluginState(PluginState.ON);
        settings.setLoadsImagesAutomatically(true);
        settings.setDomStorageEnabled(true);  
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        //---------------------aextera
        //loads the WebView completely zoomed out
//----------------end extras--------------------
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.onWindowFocusChanged(true);
//		 webView.pageDown(true);
//		 webView.pageUp(true);
//		 webView.performLongClick();
    	webView.clearCache(true);
       	webView.clearFormData();
    	webView.clearHistory();

        webView.setScrollbarFadingEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setScrollBarStyle(WebView.KEEP_SCREEN_ON);
		webView.requestFocus(View.FOCUS_DOWN); // http://android24hours.blogspot.cz/2011/12/android-soft-keyboard-not-showing-on.html
        
		if(!urls.isEmpty()){
            if(URLUtil.isValidUrl(urls)){
			    webView.loadUrl(urls);
            }else{
//				urls = "https://www.google.com/search?q=" + urls.replace(" ", "+");
    			urls = "https://www.propkaro.com";
			    webView.loadUrl(urls);
//            	Toast.makeText(getApplicationContext(), "Invalid url!", 1).show();
            }
        }else{
//			urls = "https://www.google.com";
			urls = "https://www.propkaro.com";
		    webView.loadUrl(urls);
//        	Toast.makeText(getApplicationContext(), "Empty url!", 1).show();
        }
//		webView.setBackgroundColor(Color.parseColor("#FFFFFF99"));
//		webView.setBackgroundColor(0);
//		webView.setBackgroundResource(R.drawable.bg_web);
		       
		webView.setId(5);  
        webView.setInitialScale(0);  
        webView.requestFocus();  
        webView.requestFocusFromTouch();
		setKeyboardVisibilityForUrl(false);
		VideoViewActivity.this.progress.setProgress(0);
		webView.setDownloadListener(new DownloadListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				System.out.println("web_download_starting_code......");

//        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        manager.enqueue(request);
//**************METHOD SECOND*****************************        
//                Uri source = Uri.parse(url);
//                DownloadManager.Request request = new DownloadManager.Request(source);//hari_code for download
//                // appears the same in Notification bar while downloading
//                request.setDescription("Downloading...");
////                request.setTitle("File");
//                final File destinationDir = new File (Environment.getExternalStorageDirectory(), getPackageName());
//                if (!destinationDir.exists()) {
//                    destinationDir.mkdir(); // Don't forget to make the directory if it's not there
//                }
////                // Use the same file name for the destination
//                File destinationFile = new File (destinationDir, source.getLastPathSegment());
//                request.setDestinationUri(Uri.fromFile(destinationFile));
////----------------------------------------splitting
//                String[] words = url.split("/");
//                		String target = words[words.length-1];
////---------------------------------------------------                    
//                // save the file in the "Downloads" folder of SDCARD
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, target);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                	request.allowScanningByMediaScanner();
//                	request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                }
//                // get download service and enqueue file
//                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                manager.enqueue(request);
//                Toast.makeText(getApplicationContext(), "Download starting..", 1);
//**************END_METHOD_SECOND*****************************        
//	mProgressDialog = new ProgressDialog(VideoViewActivity.this, R.style.TransparentProgressDialog);
//	//mProgressDialog.setTitle("File");
//	mProgressDialog.setMessage("Downloading..");
//	mProgressDialog.setIndeterminate(true);
//	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	mProgressDialog.setCancelable(false);
//
//	//execute this when the downloader must be fired
//	final DownloadTask downloadTask = new DownloadTask(VideoViewActivity.this);
//	downloadTask.execute(url);
//
//	mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//    	mProgressDialog.dismiss();
//    	downloadTask.cancel(true);
//    }
//});
//
//mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        downloadTask.cancel(true);
//    }
//});
//***********************METHOD THRID************************8        
			MimeTypeMap mtm = MimeTypeMap.getSingleton();
			DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			Uri downloadUri = Uri.parse(url);

			// get file name. if filename exists in contentDisposition, use it. otherwise, use the last part of the url.
			String fileName = downloadUri.getLastPathSegment();
			int pos = 0;

			if ((pos = contentDisposition.toLowerCase().lastIndexOf("filename=")) >= 0) {
			fileName = contentDisposition.substring(pos + 9);
			pos = fileName.lastIndexOf(";");
			if (pos > 0) {
			fileName = fileName.substring(0, pos - 1);
				}
			}

			// predict MIME Type
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
			String mimeType = mtm.getMimeTypeFromExtension(fileExtension);

			// request saving in Download directory
			Request req = new Request(downloadUri);
			req.setTitle(fileName);
			req.setDescription(url);
			req.setMimeType(mimeType);  
			req.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName);
			Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS).mkdirs();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                	req.allowScanningByMediaScanner();
                	req.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
			// request in download manager
			downloadManager.enqueue(req);
        	Toast.makeText(getApplicationContext(), "File saved in download folder.", 1).show();
//			VideoViewActivity.this.finish();
			System.out.println("//***************************************END DOWNLOAD CODE");
			}
		});

		webView.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event){
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					switch(keyCode){
						case KeyEvent.KEYCODE_BACK:
//							if(webView.canGoBack()){
//								webView.goBack();
//					            mWebBackForwardList = webView.copyBackForwardList();
//					            if (mWebBackForwardList.getCurrentIndex() > 0){
//					            	historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
//					            	urlEditText.setText(historyUrl);
//					            }
//					            System.out.println("w_back---historyUrl="+historyUrl);
//					        } else {
//					            // Let the system handle the back button
//					        	Intent i = new Intent(MyViewActivity.this,GridViewActivity.class);
//					        	startActivity(i);finish();
//					        }
					}
				}return false;
			}
		});

		webView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
//			              // show action bar
//		                ((Activity) getApplicationContext()).getActionBar().show();
		                break;
					case MotionEvent.ACTION_UP:
//		                // hide action bar
//		                ((Activity) getApplicationContext()).getActionBar().hide();
		                if(!v.hasFocus()){
							v.requestFocus();
						}break;
						
						default : break;
				}return false;
			}
		});
		
		openUrl = (ImageView) findViewById(R.id.goButton);
		openUrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setKeyboardVisibilityForUrl(false);
				urls = urlEditText.getText().toString().trim();
			    if (urls != null) {
			        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
			        noCacheHeaders.put("Pragma", "no-cache");
			        noCacheHeaders.put("Cache-Control", "no-cache");
					if(!urls.isEmpty()){
		                if(URLUtil.isValidUrl(urls)){
						    webView.loadUrl(urls, noCacheHeaders);
		                }else{
//							urls = "https://www.google.com/search?q=" + urls.replace(" ", "+");//
							if(!urls.startsWith("www.")&&!urls.startsWith("http://")&&!urls.startsWith("https://")){
								urls = "www."+urls;
							}
							if(!urls.startsWith("http://")&&!urls.startsWith("https://")){
								urls = "http://"+urls;
							}
		                    webView.loadUrl(urls);
		                }
		            }else{
		    			urls = "https://www.google.com";
		    		    webView.loadUrl(urls);
		            	Toast.makeText(getApplicationContext(), "Empty url", 1).show();
		            }
			    }
			    VideoViewActivity.this.progress.setProgress(0);
				webView.getSettings().setJavaScriptEnabled(true);
			}
		});
		urlEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				urlEditText.setSelection(0, urlEditText.getText().length() - 1);
			}
		});
		
	}//endOncrea
    
	@Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
     if(requestCode==FILECHOOSER_RESULTCODE) { 
            if (null == this.mUploadMessage) {
                return;
            }
           Uri result=null;
           try{
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    // retrieve from the private variable if the intent is null
                    result = intent == null ? mCapturedImageURI : intent.getData();
                }
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), "activity :"+e,Toast.LENGTH_LONG).show();
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
     	}
    }
	private void setFullscreen(){
      if (fullscreen) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		if (currApiV > Build.VERSION_CODES.JELLY_BEAN) webView.getFitsSystemWindows();
      } else {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		if (currApiV > Build.VERSION_CODES.JELLY_BEAN) webView.getFitsSystemWindows();
        }
//      setContentView(R.layout.myview);
    }
@Override
public void onConfigurationChanged(Configuration newConfig){        
	super.onConfigurationChanged(newConfig);
	if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		if (currApiV > Build.VERSION_CODES.JELLY_BEAN) webView.getFitsSystemWindows();
	} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
		if (currApiV > Build.VERSION_CODES.JELLY_BEAN) webView.getFitsSystemWindows();
	}
}
@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    webView.saveState(outState);
	Toast.makeText(getApplicationContext(), "Current page size is saved.", 1).show();
	}
@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  if (savedInstanceState != null) {
	  webView.restoreState(savedInstanceState);
		Toast.makeText(getApplicationContext(), "Current page size is saved.", 1).show();
  }
  else {    
	  webView.loadUrl(urls);
  }
}
@Override
protected void onPause() {  
    super.onPause();
//    unregisterReceiver(completeReceiver);
}
@Override
protected void onResume() {
//    IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//    registerReceiver(completeReceiver, completeFilter);
    super.onResume();
}


//@Override
//protected void onPause() {
//    super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
//    webView.onPause();
//}
//@Override
//protected void onResume() {
//    super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
//    webView.onResume();
//}
@Override
protected void onRestart() {
    super.onRestart();    //To change body of overridden methods use File | Settings | File Templates.
//    webView.onResume();
}
@Override
protected void onDestroy() {
    super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
//    webView.destroy();
}
@Override
protected void onStop() {
    super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
//    EasyTracker.getInstance(this).activityStop(this); 
//    webView.stopLoading();
//    if (inCustomView()) {
//        hideCustomView();
//    }
}
@Override
public void onStart() {
  super.onStart();
//  EasyTracker.getInstance(this).activityStart(this); 
}
	//to find progress value
//	private class MyWebChromeClient extends VideoEnabledWebChromeClient {	
//		@Override
//		public void onProgressChanged(WebView view, int newProgress) {			
//			MyViewActivity.this.setValue(newProgress);
//			super.onProgressChanged(view, newProgress);
//		}
////		//---------------JavaScript Alerts in ChromeClients
////		  //display alert message in Web View
//		  @Override
//		     public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//		         Log.d(TAG, message);
//		         new AlertDialog.Builder(view.getContext())
//		          .setTitle("JavaScript Alert !")
//		          .setMessage(message)
//		          .setCancelable(true)
//		          .show();
//		         result.confirm();
//		         return true;
//		     }
//		  @Override
//			public boolean onJsConfirm(WebView view, String url, String message,
//						final JsResult result) {
//				new AlertDialog.Builder(MyViewActivity.this)
//				       .setTitle("JavaScript Confirm Alert !")
//				       .setMessage(message)
//				       .setPositiveButton("OK",
//				           new AlertDialog.OnClickListener() {
//			            public void onClick(DialogInterface dialog, int which) {
//				                     // do your stuff here
//				                     result.confirm();
//				               }
//				           }).setCancelable(false).create().show();
//					return true;
//				}
//			@Override
//			public boolean onJsPrompt(WebView view, String url, String message,
//					String defaultValue, final JsPromptResult result) {
//				new AlertDialog.Builder(MyViewActivity.this)
//				       .setTitle("JavaScript Prompt Alert !")
//				       .setMessage(message)
//				       .setPositiveButton("OK",
//				           new AlertDialog.OnClickListener() {
//			            public void onClick(DialogInterface dialog, int which) {
//				                     // do your stuff here
//				                     result.confirm();
//				               }
//				           }).setCancelable(false).create().show();
//					return true;
//				}
//	}//endWEBchromeClient
	
//	 @Override
//	 public boolean onKeyDown(int keyCode, KeyEvent event){
//		 if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//			 webView.goBack();
//			 return true;
//		 }
//		 return super.onKeyDown(keyCode, event);
//	 }
	 
	 //to find start loading and finished data status
	private class MyWebViewClient extends WebViewClient{
        @SuppressLint({ "InlinedApi", "DefaultLocale", "NewApi" })  
        @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            System.out.println("3----------Loading.......Url="+url);
			 Log.w(TAG, "Processing webview url click...");
//------------------------------------------------			 
//		        if (url.endsWith(".apk") || url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || 
//		        		url.endsWith(".mp3") || url.endsWith(".mp4")|| url.endsWith(".3gp")) {
//                    Uri source = Uri.parse(url);
//                    DownloadManager.Request request = new DownloadManager.Request(source);//hari_code for download
//                    // appears the same in Notification bar while downloading
//                    request.setDescription("Downloading...");
////                    request.setTitle("File");
//                    final File destinationDir = new File (Environment.getExternalStorageDirectory(), getPackageName());
//                    if (!destinationDir.exists()) {
//                        destinationDir.mkdir(); // Don't forget to make the directory if it's not there
//                    }
////                    // Use the same file name for the destination
//                    File destinationFile = new File (destinationDir, source.getLastPathSegment());
//                    request.setDestinationUri(Uri.fromFile(destinationFile));
////----------------------------------------splitting
//                    String[] words = url.split("/");
//                    		String target = words[words.length-1];
////---------------------------------------------------                    
//                    // save the file in the "Downloads" folder of SDCARD
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, target);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    	request.allowScanningByMediaScanner();
//                    	request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    }
//                    // get download service and enqueue file
//                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                    manager.enqueue(request);
//		        }
//-------------------hari to handle download manager in webview code
//	         if (url.contains("rtsp")) {
//	                Uri uri = Uri.parse(url);
//	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//	                startActivity(intent);
//	         }
		        //----------------------------------------
		        webView.loadUrl(url);
//		        urls=webView.getUrl());
             if (url != null) {
                 urlEditText.setText(url);
             	}
             return true;
		    }
 
		 @Override
		public void onPageFinished(WebView view, String url) {
//			 urlList.add(0, url);
				super.onPageFinished(view, url);
				Log.w(TAG, "Finished loading URL: " + url);
//	            urlEditText.setText(webView.getTitle());
				progress.setVisibility(View.GONE);
				VideoViewActivity.this.progress.setProgress(100);
				//-------------------handeling blank page
				if ("about:blank".equals(url) && view.getTag() != null){
					Log.w(TAG, "about:blank");
			        view.loadUrl(view.getTag().toString());
			    }
			    else{
					Log.w(TAG, "not:blank");
			        view.setTag(url);
			    }
	            super.onPageFinished(view, url);
	      }//endOnPageFinished()
		 
		 @Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.w(TAG, "Starting loading URL: " + url);
	            if (url != null) {
                 urlEditText.setText(url);
             	}
			super.onPageStarted(view, url, favicon);
			
            mWebBackForwardList = webView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0){
            	historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()).getUrl();
//            	urlEditText.setText(historyUrl);
   			 System.out.println("------hari----1-----started_hostoryUrl="+historyUrl);
            }
//			initializeUrlField();
			progress.setVisibility(View.VISIBLE);
			VideoViewActivity.this.progress.setProgress(0);
//			progressDialog = new ProgressDialog(MyViewActivity.this);
//	        progressDialog.setMessage("Loading ...");
//	        progressDialog.setIndeterminate(false);
//	        progressDialog.setCancelable(false);
//	        progressDialog.setCanceledOnTouchOutside(false);
//	        progressDialog.show();
		}//endOnpagefinsished
		 
	 @Override
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
          Log.e(TAG, "Error: "+errorCode + description);
//          Toast.makeText(getApplicationContext(), "Error! ", 1).show();
          System.out.println("error here.........");
          super.onReceivedError(view, errorCode, description, failingUrl);
		 }
		    @Override
		    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
		    	Log.e(TAG, "SslError: "+error );
//		          Toast.makeText(getApplicationContext(), "Error! ", 1).show();
	        handler.proceed();
		    }
	}
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu){
	    super.onCreateOptionsMenu(menu);
//	    TransactionReceiver.retrieveTransactionInfo(this.getApplicationContext());
//	    menu.add(0, MENU_FULLSCREEN_ON, 0, R.string.menu_fullscreen_on);
//	    menu.add(0, MENU_FULLSCREEN_OFF, 0, R.string.menu_fullscreen_off);
	    menu.add(0, MENU_HOMEPAGE, 0, "Home");
//	    menu.add(0, MENU_PAUSE, 0, "Pause");
//	    menu.add(0, MENU_RESUME, 0, "Resume");
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    menu.add(0, MENU_GO_BACK, 0, "Go Back");
	    menu.add(0, MENU_GO_FORWARD, 0, "Go Forward");
	    menu.add(0, MENU_CLEAR_CACHE, 0, "Clear Cache?");
	    menu.add(0, MENU_CLEAR_HISTORY, 0, "Clear History?");
	    menu.add(0, MENU_DOWNLOADS, 0, "Downloads");
	    menu.add(0, MENU_ABOUT, 0, "About");
	    menu.add(0, MENU_EXIT, 0, "Exit");
//	    menu.add(0, MENU_SOUND_ON, 0, R.string.menu_sound_on);
//	    menu.add(0, MENU_SOUND_OFF, 0, R.string.menu_sound_off);
	    
	    return true;
	  }
	  
	  @Override
	  public boolean onPrepareOptionsMenu(Menu menu){
	    super.onPrepareOptionsMenu(menu);
//	    menu.findItem(MENU_FULLSCREEN_ON).setVisible(!fullscreen);
//	    menu.findItem(MENU_FULLSCREEN_OFF).setVisible(fullscreen);
	    menu.findItem(MENU_HOMEPAGE).setVisible(true);
//	    menu.findItem(MENU_PAUSE).setVisible(true);
//	    menu.findItem(MENU_RESUME).setVisible(true);
	    menu.findItem(MENU_REFRESH).setVisible(true);
	    menu.findItem(MENU_GO_BACK).setVisible(true);
	    menu.findItem(MENU_GO_FORWARD).setVisible(true);
	    menu.findItem(MENU_CLEAR_CACHE).setVisible(true);
	    menu.findItem(MENU_CLEAR_HISTORY).setVisible(true);
	    menu.findItem(MENU_DOWNLOADS).setVisible(true);
	    menu.findItem(MENU_ABOUT).setVisible(true);
	    menu.findItem(MENU_EXIT).setVisible(true);
//	    menu.findItem(MENU_SOUND_ON).setVisible(!getSoundOn());
//	    menu.findItem(MENU_SOUND_OFF).setVisible(getSoundOn());
	    return true;
	  }

	  @Override
	  public boolean onOptionsItemSelected(MenuItem item){
	    switch (item.getItemId()) {
	    case MENU_FULLSCREEN_ON:
	      fullscreen = true;
	      setFullscreen();
	      return true;
	    case MENU_FULLSCREEN_OFF:
	      fullscreen = false;
	      setFullscreen();
	      return true;
	    case MENU_HOMEPAGE:controlHomePage();return true;
//	    case MENU_PAUSE:controlPause();return true;
//	    case MENU_RESUME:controlResume();return true;
	    case MENU_REFRESH:controlReload();return true;
	    case MENU_GO_BACK:controlBack();return true;
	    case MENU_GO_FORWARD:controlForward();return true;
	    case MENU_CLEAR_CACHE:controlClearCache();return true;
	    case MENU_CLEAR_HISTORY:controlClearHistory();return true;
	    case MENU_DOWNLOADS:showDownloads(webView);return true;
	    case MENU_ABOUT:controlAboutPage();return true;
	    case MENU_EXIT:VideoViewActivity.this.finish();return true;
	    }
	    return false;
	  }
//-------------------hari end menu here
    private void initializeUrlField() {
        urlEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId != EditorInfo.IME_ACTION_GO) && (event == null ||
                        event.getKeyCode() != KeyEvent.KEYCODE_ENTER ||
                        event.getKeyCode() != KeyEvent.ACTION_DOWN)) {
                    return false;
                }
                webView.loadUrl(urlEditText.getText().toString());
                urlEditText.clearFocus();
                setKeyboardVisibilityForUrl(false);
                webView.requestFocus();
                return true;
            }
        });
        urlEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setKeyboardVisibilityForUrl(hasFocus);
//                mNextButton.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
                if (!hasFocus) {
                	urlEditText.setText(webView.getUrl());
                }
                if(hasFocus){
                    ((EditText)v).selectAll();
                }
            }
        });
    }
    private void setKeyboardVisibilityForUrl(boolean visible) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visible) {
            imm.showSoftInput(urlEditText, InputMethodManager.SHOW_IMPLICIT);
            urlEditText.setSelection(urlEditText.getText().length() - 1, 0);
            } else {
            imm.hideSoftInputFromWindow(urlEditText.getWindowToken(), 0);
        }
    }
	 public class JavaScriptInterface {
	     Context mContext;
	 
	     // Instantiate the interface and set the context
	     JavaScriptInterface(Context c) {
	         mContext = c;
	     }
	      
	     //using Javascript to call the finish activity
	     public void closeMyActivity() {
	         finish();
	     }
	 }
	public void setValue(int progress) {
		this.progress.setProgress(progress);		
	}
	private void controlBack(){
		if(webView.canGoBack()) webView.goBack();
		else Toast.makeText(getApplicationContext(), "Can't go back.", 1).show();
		}
	private void controlForward(){
		if(webView.canGoForward()) webView.goForward();
		else Toast.makeText(getApplicationContext(), "Can't go forward.", 1).show();
			}
	private void controlStop(){
		webView.stopLoading();
		Toast.makeText(getApplicationContext(), "Loading stopped.", 1).show();
		}
	private void controlReload(){
		webView.reload();
		Toast.makeText(getApplicationContext(), "Start Loading...", 1).show();
	}
	private void controlPause(){
		webView.onPause();
		Toast.makeText(getApplicationContext(), "Loading paused.", 1).show();
		}
	private void controlResume(){
		webView.onResume();
		Toast.makeText(getApplicationContext(), "Loading resumed.", 1).show();
	}
	private void controlDestroy(){webView.destroy();}
	private void controlHomePage(){
//		webView.loadUrl(urls);
//    	Intent i = new Intent(VideoViewActivity.this, GridActivity.class);
//    	startActivity(i);finish();
		}
	private void controlAboutPage(){
		webView.getTitle();
		Toast.makeText(getApplicationContext(), ""+webView.getTitle(), 1).show();
		}
	private void controlClearCache(){
		webView.clearCache(true);
		Toast.makeText(getApplicationContext(), "Cache cleared.", 1).show();
	}
	private void controlClearHistory(){
		webView.clearHistory();
		Toast.makeText(getApplicationContext(), "History cleared.", 1).show();
	}
    public void showDownloads(View view) {
		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Intent ii = new Intent();
        //try more options to show downloading , retrieving and complete
        ii.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(ii);
		Toast.makeText(getApplicationContext(), "Oppening...", 1).show();
    }
    @Override
    public void onBackPressed() {
      if (!webChromeClient.onBackPressed())
      {
        if(webView.canGoBack()) {
            webView.goBack();
	            mWebBackForwardList = webView.copyBackForwardList();
	            if (mWebBackForwardList.getCurrentIndex() > 0){
	            	historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
	            	urlEditText.setText(historyUrl);
	      		 System.out.println("------hari----onBackPressed_hostoryUrl="+historyUrl+"index="+mWebBackForwardList.getCurrentIndex());
	            }
        } else {
            // Let the system handle the back button
//        	Intent i = new Intent(VideoViewActivity.this, GridActivity.class);
//        	startActivity(i);finish();
        	webView.clearCache(true);
           	webView.clearFormData();
        	webView.clearHistory();
        	//for clear cookies
            CookieSyncManager.createInstance(this);         
            CookieManager cookieManager = CookieManager.getInstance();        
            cookieManager.removeAllCookie();
            super.onBackPressed();
        }
      }
    }
	@Override
	public void onClick(View v) {
	}
}