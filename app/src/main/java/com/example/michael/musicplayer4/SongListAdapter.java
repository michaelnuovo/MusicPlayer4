package com.example.michael.musicplayer4;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListAdapter extends ArrayAdapter<SongObject> {

    Context context;
    int layoutResourceId;
    ArrayList<SongObject> data = null;

    public SongListAdapter(Context context, int layoutResourceId, ArrayList<SongObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        Log.v("TAG","Inside song list adapter constructor");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        // if (row == null) { // <- Disable recycle logic
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        SongHolder songHolder = new SongHolder();

        /* Set references to xml objects */

        //songHolder.albumArt = (ImageView) row.findViewById(R.id.album_art);
        //songHolder.album = (TextView) row.findViewById(R.id.album);
        songHolder.artist = (TextView) row.findViewById(R.id.artist);
        songHolder.title = (TextView) row.findViewById(R.id.title);
        //songHolder.data = (TextView) row.findViewById(R.id.data);
        songHolder.duration = (TextView) row.findViewById(R.id.duration);

        //     row.setTag(holder); // <- Disable recycle logic
        // } else { // <- Disable recycle logic
        //     holder = (SongHolder) row.getTag(); // <- Disable recycle logic
        // } // <- Disable recycle logic

        SongObject songObject = data.get(position); // Maps array list object properties to adapter "object model"

        /* Map song object properties to xml objects */

        //SongHolder.albumArt.setBackground(songObject.albumArt); //setBackgroundResource(R.drawable.ready)
        //SongHolder.albumArt.setImageBitmap(BitmapFactory.decodeFile(songObject.albumArtURI));
        //SongHolder.album.setText(songs.album + " - " + songs.title);
        SongHolder.artist.setText(songObject.artist);
        SongHolder.title.setText(songObject.title);
        //SongHolder.data.setText("data" + songs.data);
        SongHolder.duration.setText(
                DateUtils.formatElapsedTime(
                        Long.parseLong(songObject.duration)/1000
                )); // song duration returned in ms is converted to hh:mm:ss format

        Log.v("TAG","Row object returned from list adapter");
        return row;
    }

    static class SongHolder {
        static ImageView albumArt;
        //static TextView album;
        static TextView artist;
        static TextView title;
        //static TextView data;
        static TextView duration;
    }
}