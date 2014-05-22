
package com.techwork.videoplayer;

//import android.app.Application;
import java.util.ArrayList;

import com.techwork.changesound.MainActivity;
import com.techwork.changesound.R;
import com.techwork.database.SongData;
import com.techwork.librarytab.Tab;
import com.techwork.librarytab.VideoLibrary;
import com.techwork.music.PlayMusic;
import com.techwork.music.SongsLibrary;
import com.techwork.music.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
//import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;









public class VideoPlayer extends Activity{
	private static final String TAG = "VideoViewDemo";

	private VideoView mVideoView;
//	private EditText mPath;
	//private Handler mHandle = new Handler();
	private TextView title;
	private TextView total;
	private TextView current;
	private ImageButton mPlay;
	private ImageButton mNext;
	private ImageButton mPrev;
	private ImageButton mStop;
	private ImageButton mBackward;
	private ImageButton mForw;
	private ImageButton mLoop;
	private ImageButton mBack;
	private ImageButton btn_shuffle;
	private Handler handler = new Handler();
	private Utilities utils;
	private ImageButton btn_library;
	private int SWIPE_MIN_VELOCITY = 100;
	private int SWIPE_MIN_DISTANCE = 100;
	private int MAX_VOLUME;
	private int MIN_VOLUME = 0;
	private AudioManager audio;
	private SeekBar seekBar;
	private ArrayList<String> titles;
	private boolean isShuffling = false;
	//private String current;
	private int currentMedia = 0;
	@SuppressLint("SdCardPath")
	String pref = "/sdcard/Video/video";
	String post = ".mp4";
	String dir;
	private boolean isLoop=false;
	DisplayMetrics displayMetrics;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.video_player_layout);
		
		mVideoView = (VideoView) findViewById(R.id.videoView1);
		
		
		currentMedia = (int) (Math.random()*5);
		title = (TextView) findViewById(R.id.songName);
		total = (TextView) findViewById(R.id.songTotalDurationLabel);
		current = (TextView) findViewById(R.id.songCurrentDurationLabel);
		dir = new String (pref + currentMedia + post);
		
		seekBar = (SeekBar) findViewById(R.id.songProgressBar);
		mPlay = (ImageButton) findViewById(R.id.btPlay);
		mStop = (ImageButton) findViewById(R.id.btStop);
		mNext = (ImageButton) findViewById(R.id.btNext);
		mPrev = (ImageButton) findViewById(R.id.btPrevious);
		mBackward = (ImageButton) findViewById(R.id.btBackward);
		mForw = (ImageButton) findViewById(R.id.btForward);
		mLoop = (ImageButton) findViewById(R.id.btRepeat);
		mBack = (ImageButton) findViewById(R.id.btn_back);
		btn_shuffle = (ImageButton) findViewById(R.id.btShuffle);
		utils = new Utilities();
		
		btn_library = (ImageButton) findViewById(R.id.btn_playlist);
		titles = new ArrayList<String>();
		titles.add(0, new String("Google musical"));
		titles.add(1, new String("Twitter musical"));
		titles.add(2, new String("Frozen musical"));
		titles.add(3, new String("Princess musical"));
		titles.add(4, new String("Facebook musical"));
		title.setText(titles.get(currentMedia));
		
		//seekBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) this);
		mPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				playVideo();
			}
		});
		mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playNextMedia();
				
			}
		});
		mPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playPrevMedia();
			}
		});

		mStop.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//seekBar.setProgress(0);
				if (mVideoView != null) {
					//current = null;
					mVideoView.stopPlayback();
				}
			}
		});
		mForw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curPost = mVideoView.getCurrentPosition();
				if(curPost + 5000 <= mVideoView.getDuration())
					mVideoView.seekTo(curPost+5000);
				else
					mVideoView.seekTo(mVideoView.getDuration());
			}
		});
		mBackward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curPost = mVideoView.getCurrentPosition();
				if(curPost - 5000 >= 0)
					mVideoView.seekTo(curPost - 5000);
				else
					mVideoView.seekTo(0);
			}
		});
		mLoop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isLoop)
				{
					isLoop = false;
					mLoop.setImageResource(R.drawable.icon_repeat);
					mLoop.setBackgroundResource(R.drawable.icon_repeat);
				}
				else{
					isLoop = true;
					mLoop.setImageResource(R.drawable.icon_repeat_all);
					mLoop.setBackgroundResource(R.drawable.icon_repeat_all);
				}
				
			}
		});
		// click button shuffle
		btn_shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShuffling) {
					isShuffling = false;
					mLoop.setEnabled(true);
					btn_shuffle.setImageResource(R.drawable.icon_shuffle);
					btn_shuffle.setBackgroundResource(R.drawable.icon_shuffle);
				} else {
					isShuffling = true;
					//Repeating = 0;
					mLoop.setEnabled(false);
					btn_shuffle.setImageResource(R.drawable.icon_shuffle_hover);
					btn_shuffle
							.setBackgroundResource(R.drawable.icon_shuffle_hover);
					mLoop.setImageResource(R.drawable.icon_repeat);
					mLoop.setBackgroundResource(R.drawable.icon_repeat);
				}
			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(VideoPlayer.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
				
			}
		});
		btn_library.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						VideoLibrary.class);
				startActivityForResult(intent, 101);
				//startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
		seekBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mVideoView.seekTo(seekBar.getProgress()*mVideoView.getDuration());
			}
		});
		runOnUiThread(new Runnable(){
			public void run() {
				playVideo();
			}
		});
	}

	private void playVideo() {
		try {
			//final String path = mPath.getText().toString();
			final String path = dir;
			Log.v(TAG, "path: " + path);
			if (path == null || path.length() == 0) {
				Toast.makeText(VideoPlayer.this, "File URL/path is empty",
						Toast.LENGTH_LONG).show();

			} else {
				// If the path has not changed, just start the media player
//				if (path.equals(current) && mVideoView != null) {
//					mVideoView.start();
//					mVideoView.requestFocus();
//					return;
//				}
				//current = path;
				displayMetrics = new DisplayMetrics();
				this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				int height = displayMetrics.heightPixels;
				int width = displayMetrics.widthPixels;
				mVideoView.setMinimumHeight(height);
				mVideoView.setMinimumWidth(width);
				mVideoView.setVideoPath(path);
				mVideoView.start();
				
				mVideoView.requestFocus();

			}
			updateProgressBar();
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}
	
	private void playNextMedia(){
		if(currentMedia ==4){
			currentMedia = 0;
		}
		else{
			currentMedia += 1;
		}
		dir = pref + currentMedia + post;
		title.setText(titles.get(currentMedia));
		playVideo();
	}
	private void playPrevMedia(){
		if(currentMedia==0)
			currentMedia = 4;
		else
			currentMedia -=1;
		dir = pref + currentMedia + post;
		title.setText(titles.get(currentMedia));
		playVideo();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentMedia = data.getExtras().getInt("index");
			
			dir = pref + currentMedia + post;
			title.setText(titles.get(currentMedia));
			playVideo();
		}
	}
	
	public void updateProgressBar() {
		handler.postDelayed(update, 100);
	}
	
	public Runnable update = new Runnable() {

		@Override
		public void run() {
			long totalDuration = mVideoView.getDuration();
			long currentDuration = mVideoView.getCurrentPosition();
			total.setText(""
					+ utils.milliSecondsToTimer(totalDuration));
			current.setText("" + utils.milliSecondsToTimer(currentDuration));
			int percentage = (int) utils.getProgressPercentage(currentDuration, totalDuration);
			seekBar.setProgress(percentage);
			handler.postDelayed(this, 100);
		}
		
	};
	

	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(VideoPlayer.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}



