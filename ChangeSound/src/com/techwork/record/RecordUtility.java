package com.techwork.record;

import java.io.File;

import android.os.Environment;

public class RecordUtility {
	public static String getFileName(String format){
		String fileName;
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, RecordConstant.AUDIO_RECORDER_FOLDER);
	
		if (!file.exists()) {
			file.mkdirs();
		}
	
		fileName = file.getAbsolutePath() + "/" + System.currentTimeMillis()
				+ format;
		return (fileName);
	}
}
