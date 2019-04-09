package com.schoolapp.schoolapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {
    private ArrayList<String> songs, playlistsize;
    private LayoutInflater songInf;
    PlaylistNameInterface pni;

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
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = songInf.inflate(R.layout.playlistview, null);
            viewHolder.playlisttitle = (EditText) convertView.findViewById(R.id.playlisttitle);
            viewHolder.songsize = convertView.findViewById(R.id.songsize);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.playlisttitle.setText(songs.get(position));
        viewHolder.playlisttitle.setId(position);
        viewHolder.songsize.setText(playlistsize.get(position) + " songs");
        viewHolder.songsize.setId(position);

        viewHolder.playlisttitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    final int position = v.getId();
                    final EditText playlisttitle = (EditText) v;
                    String oldname = songs.get(position);
                    if(!oldname.equals(playlisttitle.getText().toString())){
                        songs.set(position, playlisttitle.getText().toString());
                        pni.OnPlaylistNameChanged(playlisttitle.getText().toString(), oldname);
                    }
                }
            }
        });
        return convertView;
    }

    public void setPlaylistNameInterface(PlaylistNameInterface playlistNameInterface){pni = playlistNameInterface;}
}

    class ViewHolder {
        TextView songsize;
        EditText playlisttitle;
    }