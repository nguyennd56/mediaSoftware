package com.techwork.changesound;
/*
 * Name: Start Up
 * Type: Activity
 * Description: This activity's function is for checking library which is empty or not 
 * 												and loading music file on sdCard if exists
 * 
 */
import java.io.File;

import com.techwork.additionclass.FileExtensionFilter;
import com.techwork.changesound.R;
import com.techwork.database.DatabaseHandler;
import com.techwork.database.SongData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.Window;

public class StartApp extends Activity {
	@SuppressLint("SdCardPath")
	final String MEDIA_PATH = new String("/sdcard/ChangeSound/Music/");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);


		setContentView(R.layout.starting_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Thread loaddingThread = new Thread() {
			public void run() {
				try {
					
					DatabaseHandler db = new DatabaseHandler(StartApp.this);
					/*
					 * Check database
					 */
					//Case 1: database is empty
					if (db.getSongDatasCount() == 0){
						//Check home folder to find song file
						File home = new File(MEDIA_PATH);
						if (home.listFiles(new FileExtensionFilter()).length > 0) {
							// filter to get song information
							for (File file : home.listFiles(new FileExtensionFilter())) {
								// filter to get song information
								String songName = file.getName().substring(0,
										(file.getName().length() - 4));
								String songPath = file.getPath();
								MediaMetadataRetriever media = new MediaMetadataRetriever();
								media.setDataSource(songPath);
								byte[] data;
								if (media.getEmbeddedPicture() != null) {
									data = media.getEmbeddedPicture();
								} else {
									data = null;
								}
								String songArtist = media
										.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
								String songAlbum = media
										.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
								media.release();
								// push song data from file into song data object
								db.addSongData(new SongData(songName, songPath,
										songArtist, songAlbum, data,0));
							}
						}
					}
					//Case 2: database is not empty
					else{
						sleep(2000);
					}
					
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
					finish();
				}
			}
		};
		loaddingThread.start();
	}

};
