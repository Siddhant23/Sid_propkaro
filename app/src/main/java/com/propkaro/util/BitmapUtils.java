package com.propkaro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

public class BitmapUtils {
    public static Bitmap drawText(Bitmap b, String text, int textWidth, int textSize) {
		// Get text dimensions
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(textSize);
		StaticLayout mTextLayout = new StaticLayout(text, textPaint,
				textWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
 
		// Create bitmap and canvas to draw to
		b = Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), Config.RGB_565);
		Canvas c = new Canvas(b);
 
		// Draw background
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		c.drawPaint(paint);
 
		// Draw text
		c.save();
		c.translate(0, 0);
		mTextLayout.draw(c);
		c.restore();
 
		return b;
	}
	// NEW METHOD FOR PICASA IMAGE LOAD
	public static void loadPicasaImageFromGallery(final Context ctx, Bitmap bitmap, final Uri uri) {
	    String[] projection = {  MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
	    Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
	    if(cursor != null) {
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
	        if (columnIndex != -1) {
                try {
					bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
        }
	    cursor.close();
	}

	public static String getPath1(Context ctx, Uri uri) 
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

	public static String getPath(Context ctx, Uri uri) {
	    String[] projection = {  MediaColumns.DATA};
	    Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
	    if(cursor != null) {
	        //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	        //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	        cursor.moveToFirst();
	        int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	        String filePath = cursor.getString(columnIndex);
	        cursor.close();
	        return filePath;
	    }
	    else 
	        return uri.getPath();// FOR OI/ASTRO/Dropbox etc
	}
	public static Bitmap getBitmap(File f, int reqWidth, int reqHeight) throws MalformedURLException, IOException {
		try{
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, options);
			stream1.close();

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, options);
			stream2.close();
			return bitmap;
		}
		catch (FileNotFoundException e) {} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

}
