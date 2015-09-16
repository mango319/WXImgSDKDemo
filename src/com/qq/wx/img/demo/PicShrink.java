package com.qq.wx.img.demo;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class PicShrink {
	private final static String TAG = "PicShrink";
	private static int mMinSideLength = 60000;
	private static int mMaxNumOfPixels = 320 * 480;
	
	public static void setMinSileLength(int minSideLength) {
		mMinSideLength = minSideLength;
	}
	
	public static void setMaxNumOfPixels(int maxNumOfPixels) {
		mMaxNumOfPixels = maxNumOfPixels;
	}

	public static Bitmap compress(String fileName) {
		BitmapFactory.Options opts = new BitmapFactory.Options();  
		opts.inJustDecodeBounds = true;  
		BitmapFactory.decodeFile(fileName, opts);  

		opts.inSampleSize = computeSampleSize(opts);
		opts.inJustDecodeBounds = false;  

		return BitmapFactory.decodeFile(fileName, opts);
	}

	public static Bitmap compress(Context context, Uri uri, int size) {
		BitmapFactory.Options opts = new BitmapFactory.Options();  
		opts.inJustDecodeBounds = true;  
		try {
			BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, opts);

			//opts.inSampleSize = computeSampleSize(opts, 60000, 320 * 480);  
			if (0 < size) {
				opts.inSampleSize = computeSampleSize(opts, 60000, size);  
			}
			opts.inJustDecodeBounds = false;  
		
			return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, opts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,  
	        int minSideLength, int maxNumOfPixels) {  
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
	    Log.d(TAG, "initialSize = " + initialSize);
	  
	    int roundedSize;  
	    if (initialSize <= 8 ) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
	        roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	  
	    return roundedSize;  
	}  
	  
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :  
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 :  
	            (int) Math.min(Math.floor(w / minSideLength),  
	            Math.floor(h / minSideLength));  
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	  
	    if ((maxNumOfPixels == -1) &&  
	            (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}

	private static int computeSampleSize(BitmapFactory.Options options) {  
	    int initialSize = computeInitialSampleSize(options);  
	  
	    int roundedSize;  
	    if (initialSize <= 8 ) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
	        roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	  
	    return roundedSize;  
	}  
	  
	private static int computeInitialSampleSize(BitmapFactory.Options options) {
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	  
	    int lowerBound = (mMaxNumOfPixels == -1) ? 1 :  
	            (int) Math.ceil(Math.sqrt(w * h / mMaxNumOfPixels));  
	    int upperBound = (mMinSideLength == -1) ? 128 :  
	            (int) Math.min(Math.floor(w / mMinSideLength),  
	            Math.floor(h / mMinSideLength));  
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	  
	    if ((mMaxNumOfPixels == -1) &&  
	            (mMinSideLength == -1)) {  
	        return 1;  
	    } else if (mMinSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}
}
