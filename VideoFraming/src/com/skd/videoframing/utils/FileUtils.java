package com.skd.videoframing.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class FileUtils {
	
	/*
	 * from http://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
	 */
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getRealPath(Context ctx, Uri uri) { //TODO add case when build < kitkat and is not a document
		// Will return "image:x*"
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Video.Media.DATA };     

		// where id is equal to             
		String sel = MediaStore.Video.Media._ID + "=?";

		Cursor cursor = ctx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
		                          					   column, sel, new String[]{ id }, null);

		String filePath = "";

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
		    filePath = cursor.getString(columnIndex);
		}   

		cursor.close();
		
		return filePath;
	}
	
}
