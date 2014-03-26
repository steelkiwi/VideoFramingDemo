package com.skd.videoframing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.skd.videoframing.OnFrameClickListener;

public class ImageUtils {

	private static final int SCALE_COEF = 2;
	
	public static Bitmap getScaledBitmap(Bitmap frame_orig) {
		int w_orig = frame_orig.getWidth();
    	int h_orig = frame_orig.getHeight();
    	float ratio_orig = (float) w_orig / h_orig;
    	
    	int w_new = w_orig / SCALE_COEF;
    	int h_new = (int) ((float) w_new / ratio_orig);
    	
    	return Bitmap.createScaledBitmap(frame_orig, w_new, h_new, true);
	}
	
	public static ImageView createFrameImage(Context ctx, Bitmap frame, int cur, final OnFrameClickListener listener) {
		final ImageView frameImg = new ImageView(ctx);
		frameImg.setTag(cur);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		frameImg.setLayoutParams(lp);
		frameImg.setImageBitmap(frame);
		frameImg.setScaleType(ScaleType.CENTER_CROP);
		frameImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onFrameClicked((Integer) frameImg.getTag());
				}
			}
		});
		return frameImg;
	}
	
}
