package com.skd.videoframing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.skd.videoframing.async.FrameExtractorTask;
import com.skd.videoframing.utils.StorageUtils;

public class FrameActivity extends ActionBarActivity {
	
	public static final String PATH_ARG = "pathArg";
	public static final String TIME_ARG = "timeArg";
	private ImageView frameImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame);

		String path = "";
		int time = 0;
		if (getIntent() != null) {
			path = getIntent().getStringExtra(PATH_ARG);
			time = getIntent().getIntExtra(TIME_ARG, 0);
		}
		
		frameImg = (ImageView) findViewById(R.id.frame);
		
		FrameExtractorTask task = new FrameExtractorTask(frameImg, time);
		task.execute(path);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.frame_menu, menu);
        
        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				saveVideoFrame();
				return true;
			}
		});
        
        return true;
	}
	
	private void saveVideoFrame() {
		Bitmap bm = ((BitmapDrawable)frameImg.getDrawable()).getBitmap();
		if (bm == null) { return; }
		
		File f = StorageUtils.getFile(FrameActivity.this);
		if (f == null) {
			Toast.makeText(FrameActivity.this, getString(R.string.saveNoStorage), Toast.LENGTH_SHORT).show();
			return; 
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			bm.compress(CompressFormat.JPEG, 100, fos);
			MediaScannerConnection.scanFile(FrameActivity.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
			Toast.makeText(FrameActivity.this, String.format(getString(R.string.saved), f.getPath()), Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
