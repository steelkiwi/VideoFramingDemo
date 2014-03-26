package com.skd.videoframing;

import android.graphics.Bitmap;

public class Frame {
	
	private Bitmap bm;
	private int time;
	
	public Bitmap getBm() {
		return bm;
	}
	public void setBm(Bitmap bm) {
		this.bm = bm;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
}
