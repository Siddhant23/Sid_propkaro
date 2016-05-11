package com.propkaro;

import java.io.File;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.propkaro.util.Timber;
import com.propkaro.util.Utilities;

public class AppController extends Application {
	public static final String TAG = AppController.class.getSimpleName();
//	private RequestQueue mRequestQueue;
//	ImageLoader mImageLoader;
//	LruBitmapCache mLruBitmapCache;
	private static AppController mInstance;
	Context ctx;
	
	private static final String PROPERTY_ID = "UA-72379327-1";
	public static int GENERAL_TRACKER = 0;
	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}
	public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public AppController() {
		super();
		if(Utilities.D)Log.v(TAG,"InitApplication");
	}

	public synchronized Tracker getTracker(TrackerName appTracker) {
		if (!mTrackers.containsKey(appTracker)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(appTracker, t);
		}
		return mTrackers.get(appTracker);
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
	    super.onCreate();
		if (BuildConfig.DEBUG) {
		      Timber.plant(new Timber.DebugTree());
		    } else {
		      Timber.plant(new CrashReportingTree());
		    }
		if (Utilities.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
//	    int SDK_INT = android.os.Build.VERSION.SDK_INT;
//	    if (SDK_INT > 9){
//	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//	        StrictMode.setThreadPolicy(policy);
//	    }
	mInstance = this;
	ctx = this;
//	MyRequest.init(ctx);
//	setImageLoad(mInstance);
	initLoader(mInstance);

  }//endOncrea
  
	public static synchronized AppController getInstance() {
		return mInstance;
	}
//	public RequestQueue getRequestQueue() {
//		if (mRequestQueue == null) {
//			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//		}
//		return mRequestQueue;
//	}
//	public ImageLoader getImageLoader() {
//		getRequestQueue();
//		if (mImageLoader == null) {
//			mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
//		}
//		return this.mImageLoader;
//	}
//
//	public <T> void addToRequestQueue(Request<T> req, String tag) {
//		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//		getRequestQueue().add(req);
//	}
//	public <T> void addToRequestQueue(Request<T> req) {
//		req.setTag(TAG);
//		getRequestQueue().add(req);
//	}
//	public void cancelPendingRequests(Object tag) {
//		if (mRequestQueue != null) {
//			mRequestQueue.cancelAll(tag);
//		}
//	}
	
	public static void initLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.user_type)
		.showImageForEmptyUri(R.mipmap.user_type)
		.showImageOnFail(R.mipmap.user_type)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new CircleBitmapDisplayer(Color.WHITE, 1))
		.build();
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.defaultDisplayImageOptions(options)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 MiB
		.tasksProcessingOrder(QueueProcessingType.LIFO);
//		config.writeDebugLogs(); 

		ImageLoader.getInstance().init(config.build());
	}
	
	public void setImageLoad(Context context){
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
							.cacheInMemory(true)
							.cacheOnDisc(true)
							.displayer(new CircleBitmapDisplayer())
                       .showImageOnLoading(android.R.color.transparent)
                       .showImageForEmptyUri(R.mipmap.user_type)
                       .showImageOnFail(R.mipmap.user_type)
                       .bitmapConfig(Bitmap.Config.RGB_565)
                       .displayer(new FadeInBitmapDisplayer(550))
                       .build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);

	}
	private void setLoader(Context context) {

		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "UniversalImageLoader/Cache");
		DisplayImageOptions uOptions;
        uOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true).build();

    	ImageLoaderConfiguration uConfig;
        uConfig = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
//                .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .discCache(new UnlimitedDiskCache(cacheDir))
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(uOptions)       
            	  	.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//              	.writeDebugLogs().imageDownloader(new XmppAvatarLoader(this))
//					.enableLogging()
                .build();

    	ImageLoader.getInstance().init(uConfig);
    }

	  /** A tree which logs important information for crash reporting. */
	  private static class CrashReportingTree extends Timber.Tree {
	    @Override protected void log(int priority, String tag, String message, Throwable t) {
	      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
	        return;
	      }

	      FakeCrashLibrary.log(priority, tag, message);

	      if (t != null) {
	        if (priority == Log.ERROR) {
	          FakeCrashLibrary.logError(t);
	        } else if (priority == Log.WARN) {
	          FakeCrashLibrary.logWarning(t);
	        }
	      }
	    }
	  }
}
