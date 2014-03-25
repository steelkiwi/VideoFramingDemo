package com.skd.videoframing.utils;

import java.io.File;

import com.skd.videoframing.R;

import android.content.Context;
import android.os.Environment;

public class StorageUtils {

	public static File getFile(Context ctx) {
		if (!checkExternalStorageAvailable()) { return null; }
		
		File dir = new File(Environment.getExternalStorageDirectory(), ctx.getString(R.string.app_name));
		if (dir.exists() || dir.mkdirs()) {
			return new File(dir, String.format("frame_%s.jpg", System.currentTimeMillis()));
		}
		return null;
	}
	
	private static boolean checkExternalStorageAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		}
		else {
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return (mExternalStorageAvailable && mExternalStorageWriteable);
	}
	
}
