package com.techwork.record;


public interface RecorderInterface {
	public String record(String fileName, int freq, int format, boolean recording);
	public void playRecord(String mFileName, int freq);
}
