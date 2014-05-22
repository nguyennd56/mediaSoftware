package com.techwork.librarytab;

import com.techwork.changesound.R;
import com.techwork.music.SongsLibrary;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Tab extends TabActivity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, SongsLibrary.class);
		
		spec = tabHost.newTabSpec("tab1").setIndicator("Music Library", res.getDrawable(R.drawable.video_icon))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this,  VideoLibrary.class);
		spec = tabHost.newTabSpec("tab2").setIndicator("Video Library", res.getDrawable(R.drawable.img))
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0);
	}
}
