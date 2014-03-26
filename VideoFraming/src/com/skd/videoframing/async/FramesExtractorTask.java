package com.skd.videoframing.async;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.skd.videoframing.Frame;
import com.skd.videoframing.OnFrameClickListener;
import com.skd.videoframing.utils.ImageUtils;
import com.skd.videoframing.utils.SharedPrefsUtils;

public class FramesExtractorTask extends AsyncTask<String, Float, ArrayList<Frame>> {

	private final int delta_time; //in microsecs
	
	private final WeakReference<LinearLayout> framesViewReference;
	private final WeakReference<OnFrameClickListener> listenerReference;
	private ProgressDialog progressDlg;
	
	public FramesExtractorTask(LinearLayout framesView, OnFrameClickListener listener) {
		this.delta_time = SharedPrefsUtils.getFramesFrequency(framesView.getContext())*1000000; //in microsecs
		System.out.println(delta_time);
		framesViewReference = new WeakReference<LinearLayout>(framesView);
		listenerReference = new WeakReference<OnFrameClickListener>(listener);
		createDialog(framesView.getContext());
	}
	
	@Override
    protected void onPreExecute() {
		showProgress();
    }
	
	@Override
	protected ArrayList<Frame> doInBackground(String... params) {
		FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
	    mmr.setDataSource(params[0]);

	    String s_duration = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
	    int duration = getVideoDuration(s_duration);
	    
	    ArrayList<Frame> frames = new ArrayList<Frame>();
	    for (int i=0; i<=duration; i+=delta_time) {
	    	Bitmap frame_orig = mmr.getFrameAtTime(i, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
	    	if (frame_orig == null) { 
	    		setProgress(i, duration);
		    	continue; 
		    }
	    	Frame frame = new Frame();
	    	frame.setBm(ImageUtils.getScaledBitmap(frame_orig));
	    	frame.setTime(i);
	    	frames.add(frame);
	    	setProgress(i, duration);
	    }
	    return frames;
	}
	
	private int getVideoDuration(String s_duration) {
		int duration = 0;
	    try {
	    	duration = Integer.parseInt(s_duration); //in millisecs
	    } catch (NumberFormatException e) {
	    	e.printStackTrace();
	    }
	    duration *= 1000; //in microsecs
	    return duration;
	}

	@Override
	protected void onPostExecute(ArrayList<Frame> result) {
		if (isCancelled() && result != null) {
			for (int i=0, len=result.size(); i<len; i++) {
				result.get(i).getBm().recycle();
			}
			result.clear();
			result = null;
		}
		if (result == null || result.size() <= 0) { 
			hideProgress();
			return; 
		}
		
		OnFrameClickListener listener = null;
		if (listenerReference != null) {
			listener = listenerReference.get();
		}
		
		if (framesViewReference != null) {
			LinearLayout framesView = framesViewReference.get();
			for (int i=0, len=result.size(); i<len; i++) {
			    framesView.addView(ImageUtils.createFrameImage(framesView.getContext(),
													    		result.get(i).getBm(),
													    		result.get(i).getTime(),
													    		listener));
			}
		}
		
		hideProgress();
	}

	private void setProgress(int cur, int duration) {
		publishProgress((float)(cur + delta_time) / duration * 100);
	}
	
	@Override
	protected void onProgressUpdate(Float... values) {
		if (progressDlg != null) {
			progressDlg.setProgress(values[0].intValue());
		}
	}
	
	private Dialog createDialog(Context ctx) {
		progressDlg = new ProgressDialog(ctx);
		progressDlg.setMessage("Fetching video frames..");
		progressDlg.setMax(100);
		progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDlg.setCancelable(false);
		progressDlg.getWindow().setGravity(Gravity.CENTER);
        return progressDlg;
	}
	
	private void showProgress() {
		if (progressDlg != null) {
			progressDlg.show();
		}
	}
	
	private void hideProgress() {
		if (progressDlg != null && progressDlg.isShowing()) {
			progressDlg.dismiss();
        }
	}
	
}
