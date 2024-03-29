package com.techwork.music;

import java.util.ArrayList;
import java.util.List;

import com.techwork.changesound.R;
import com.techwork.database.DatabaseHandler;
import com.techwork.database.SongData;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Songs extends ArrayAdapter<SongDetails> {

	private Activity context;
	private ArrayList<SongDetails> song;
	private String typeSort;
	private SongHolder holder;

	public Songs(Activity context, ArrayList<SongDetails> song, String type) {
		super(context, R.layout.playlist_item, song);
		this.context = context;
		this.song = song;
		this.typeSort = type;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View rowView = view;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.playlist_item, null, true);
			holder = new SongHolder();
			holder.songTitle = (TextView) rowView.findViewById(R.id.songTitle);
			holder.Artist = (TextView) rowView.findViewById(R.id.songArtist);
			holder.Album = (TextView) rowView.findViewById(R.id.songAlbum);
			holder.favorite = (ImageButton) rowView
					.findViewById(R.id.btn_favorite);
			holder.imageView = (ImageView) rowView
					.findViewById(R.id.image_music);
			rowView.setTag(holder);
		} else {
			holder = (SongHolder) rowView.getTag();
		}

		holder.songTitle.setText(song.get(position).getSongName());
		holder.Artist.setText(song.get(position).getSongArtist());
		holder.Album.setText(song.get(position).getSongAlbum());

		if (song.get(position).getImage() != null) {
			holder.imageView.setImageBitmap(song.get(position).getImage());
		} else {
			holder.imageView.setImageResource(R.drawable.icon_music);
		}

		DatabaseHandler db = new DatabaseHandler(context);
		holder.favoriteValues = db.getSongData(song.get(position).getSongID())
				.getFavorite();
		if (holder.favoriteValues == 1) {
			holder.favorite.setImageResource(R.drawable.favorite);
			holder.favorite.setBackgroundResource(R.drawable.favorite);
		} else {
			holder.favorite.setImageResource(R.drawable.no_favorite);
			holder.favorite.setBackgroundResource(R.drawable.no_favorite);
		}
		holder.favorite.setTag(position);
		holder.favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(context);
				int pos = (Integer) v.getTag();
				if (db.getSongData(song.get(pos).getSongID()).getFavorite() == 0) {
					db.editFavorite(song.get(pos).getSongID(), 1, typeSort);
					holder.favorite.setImageResource(R.drawable.favorite);
					holder.favorite.setBackgroundResource(R.drawable.favorite);
				} else {
					db.editFavorite(song.get(pos).getSongID(), 0, typeSort);
					holder.favorite.setImageResource(R.drawable.no_favorite);
					holder.favorite
							.setBackgroundResource(R.drawable.no_favorite);
				}
				notifyDataSetChanged();
				db.close();
			}

		});
		notifyDataSetChanged();
		db.close();
		return rowView;
	}

	static class SongHolder {
		ImageButton favorite;
		TextView Album;
		TextView Artist;
		TextView songTitle;
		ImageView imageView;
		int favoriteValues = 0;
	}

	public Bitmap resize(Bitmap bitmap) {
		final int maxSize = 20;
		int outWidth;
		int outHeight;
		int inWidth = bitmap.getWidth();
		int inHeight = bitmap.getHeight();
		if (inWidth > inHeight) {
			outWidth = maxSize;
			outHeight = (inHeight * maxSize) / inWidth;
		} else {
			outHeight = maxSize;
			outWidth = (inWidth * maxSize) / inHeight;
		}

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
				outHeight, false);
		return resizedBitmap;
	}

	@Override
	public Filter getFilter() {

		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null) {
					song.clear();
					@SuppressWarnings("unchecked")
					ArrayList<SongDetails> items = new ArrayList<SongDetails>(
							(ArrayList<SongDetails>) results.values);

					if (items.size() > 0) {
						for (SongDetails item : items) {
							song.add(item);
						}
					}
					notifyDataSetChanged();
				}

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				DatabaseHandler db = new DatabaseHandler(context);
				ArrayList<SongDetails> songs = new ArrayList<SongDetails>();
				List<SongData> listSongs = db.getAllSongDatas();
				for (SongData sd : listSongs) {
					SongDetails song = new SongDetails();
					song.setSongID(sd.getID());
					song.setSongName(sd.getName());
					song.setSongPath(sd.getPath());
					song.setSongArtist(sd.getArtist());
					song.setSongAlbum(sd.getAlbum());
					byte[] data = sd.getImage();
					if (data != null) {
						Bitmap image = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						song.setImage(image);
					} else {
						song.setImage(null);
					}

					songs.add(song);
				}
				FilterResults results = new FilterResults();
				ArrayList<SongDetails> filter = new ArrayList<SongDetails>();
				constraint = constraint.toString().toLowerCase();

				if (constraint != null && constraint.toString().length() > 0) {
					for (int i = 0; i < songs.size(); i++) {
						String strName = songs.get(i).getSongName();
						String strArtist = songs.get(i).getSongArtist();
						if (strName.toLowerCase().contains(
								constraint.toString())
								|| strArtist.toLowerCase().contains(
										constraint.toString())) {
							filter.add(songs.get(i));
						}
					}
				}
				if (constraint == null || constraint.toString().length() == 0) {
					for (int i = 0; i < songs.size(); i++) {
						filter.add(songs.get(i));
					}
				}

				results.count = filter.size();
				results.values = filter;
				return results;
			}
		};
	}
}