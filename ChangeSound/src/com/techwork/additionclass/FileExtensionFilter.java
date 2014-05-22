package com.techwork.additionclass;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".mp4") || name.endsWith(".MP4"));
	}
}
