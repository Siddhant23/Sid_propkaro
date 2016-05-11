package com.propkaro.rssfeed;

import java.util.List;


import android.app.Activity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.propkaro.R;
import com.propkaro.propertycenter.AvailPCFragment;

public class RssReaderListAdapter extends ArrayAdapter<RssFeedStructure> {
	List<RssFeedStructure> imageAndTexts1,l = null;
	public static String str_link ="";
	public RssReaderListAdapter(Activity activity, List<RssFeedStructure> imageAndTexts) {
		super(activity, 0, imageAndTexts);
		imageAndTexts1 = imageAndTexts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Activity activity = (Activity) getContext();
		LayoutInflater inflater = activity.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.grid_row_news2, null);
		TextView textView = (TextView) rowView.findViewById(R.id.feed_text);
		TextView timeFeedText = (TextView) rowView.findViewById(R.id.feed_updatetime);
		TextView urlFeedText = (TextView) rowView.findViewById(R.id.feed_url);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.feed_image);
		try {
			Log.d("rssfeed", "imageAndTexts1.get(position).getImgLink() :: "
					+ imageAndTexts1.get(position).getImgLink() + " :: "
					+ imageAndTexts1.get(position).getTitle());
			 str_link = imageAndTexts1.get(position).getBackurl().toString();

//			System.out.println("---------"+str_link);
			textView.setText(imageAndTexts1.get(position).getTitle());
			urlFeedText.setText(imageAndTexts1.get(position).getBackurl());
			
			SpannableString content = new SpannableString(imageAndTexts1.get(position).getPubDate());
			content.setSpan(new UnderlineSpan(), 0, 16, 0);
			timeFeedText.setText(content);
			
//			if (imageAndTexts1.get(position).getImgLink() != null) {
//			      ImageLoader imageLoader = MyVolley.getImageLoader();
//			      imageLoader.get(imageAndTexts1.get(position).getImgLink().toString(), 
//			                     ImageLoader.getImageListener(imageView, 
//			                                                   R.drawable.ic_default, 
//			                                                   R.drawable.ic_default));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.home)
			.showImageForEmptyUri(R.mipmap.home)
			.showImageOnFail(R.mipmap.home)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.build();
			
			ImageLoader.getInstance().displayImage(imageAndTexts1.get(position).getImgLink().toString(), 
					imageView, options, AvailPCFragment.animateFirstListener);
			
					//----------list seperation
//					if (position % 2 == 0) {
//						   convertView.setBackgroundResource(R.drawable.strip_list1); 
//					} else {
//						   convertView.setBackgroundResource(R.drawable.strip_list2);  
//					}
				
//				URL feedImage = new URL(imageAndTexts1.get(position)
//						.getImgLink().toString());
//				if (!feedImage.toString().equalsIgnoreCase("null")) {
//					HttpURLConnection conn = (HttpURLConnection) feedImage
//							.openConnection();
//					InputStream is = conn.getInputStream();
//					Bitmap img = BitmapFactory.decodeStream(is);
//					imageView.setImageBitmap(img);
//				} else {
//					imageView.setBackgroundResource(R.drawable.ic_default);
//				}
//			}
//		} catch (MalformedURLException e) {
//		} catch (IOException e) {

		} catch (Exception e) {e.printStackTrace();}
		return rowView;
	}
}