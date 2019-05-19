package com.schoolapp.schoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {
    private ArrayList<String> songs, playlistsize;
    private LayoutInflater songInf;

    public PlaylistAdapter(Context c, ArrayList<String> theSongs, ArrayList<String> playlistsize) {
        songs = theSongs;
        this.playlistsize = playlistsize;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(!songs.isEmpty())return songs.size();
        else return 0;
    }

    @Override
    public String getItem(int arg0) {
        return songs.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.playlistview, parent, false);
        //get title and artist views
        final TextView playlist = songLay.findViewById(R.id.playlisttitle);
        final TextView size = songLay.findViewById(R.id.songsize);
        //get song using position
        String currSong = songs.get(position);
        String playsize = playlistsize.get(position);
        //get title and artist strings
        playlist.setText(currSong);
        size.setText(playsize + " songs");
        //set position as tag
        songLay.setTag(position);
        //songLay.setClickable(true);

        return songLay;
    }
}
