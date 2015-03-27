package com.muslimmarry.helpers;


import android.graphics.Bitmap;
import android.util.Log;

public class ImageManager {
	
	public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();

	    Log.v("myTag", "Width and height are " + width + "--" + height);

	    if (width > height) {
	        // landscape
	        float ratio = (float) width / maxWidth;
	        width = maxWidth;
	        height = (int)(height / ratio);
	    } else if (height > width) {
	        // portrait
	        float ratio = (float) height / maxHeight;
	        height = maxHeight;
	        width = (int)(width / ratio);
	    } else {
	        // square
	        height = maxHeight;
	        width = maxWidth;
	    }
	    
	    Log.v("myTag", "after scaling Width and height are " + width + "--" + height);

	    bm = Bitmap.createScaledBitmap(bm, width, height, true);
	    return bm;
	}
}
