package com.techwork.music;

import com.techwork.changesound.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_song_details extends Fragment {
	private View view;
	private TextView IfAlbum;
	private TextView IfArtist;
	private TextView IfLength;
	private String Album;
	private String Artist;
	private String Length;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_songs_details_layout,
				container, false);
		IfAlbum = (TextView) view.findViewById(R.id.IfSongsAlbum);
		IfArtist = (TextView) view.findViewById(R.id.IfSongsArtist);
		IfLength = (TextView) view.findViewById(R.id.IfLength);
		Bundle bundle = this.getArguments();
		Album = bundle.getString("album");
		Artist = bundle.getString("artist");
		Length = bundle.getString("length");

		IfAlbum.setText(Album);
		IfArtist.setText(Artist);
		IfLength.setText(Length);
		return view;
	}

	public void upDateTextView(String artist, String album, String length) {
		IfAlbum = (TextView) view.findViewById(R.id.IfSongsAlbum);
		IfAlbum.setText(album);
	}

}
