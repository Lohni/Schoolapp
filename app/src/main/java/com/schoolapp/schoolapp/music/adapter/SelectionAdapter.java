package com.schoolapp.schoolapp.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.objects.MusicResolver;

import java.util.ArrayList;

public class SelectionAdapter extends BaseAdapter {

    private ArrayList<MusicResolver> songs;
    private LayoutInflater songInf;

    public SelectionAdapter(Context c, ArrayList<MusicResolver> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public MusicResolver getItem(int arg0) {
        return songs.get(arg0);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, final View convertview, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertview;
        if(view == null){
            view = songInf.inflate(R.layout.songselection, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.songtitle = view.findViewById(R.id.song_title);
            viewHolder.songartist = view.findViewById(R.id.song_artist);
            viewHolder.selected = view.findViewById(R.id.checkBox);
            viewHolder.selected.setClickable(false);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            //viewHolder.selected.getTag(i);
        }


        //viewHolder.selected.setTag(i);
        viewHolder.songtitle.setText(getItem(i).getTitle());
        viewHolder.songartist.setText(getItem(i).getArtist());

        viewHolder.selected.setChecked(songs.get(i).getChecked());

        return view;
    }

    public ArrayList<MusicResolver> getSelected(){
        ArrayList<MusicResolver> selected = new ArrayList<>();
        int i = 0;

        while(songs.get(i) != null)
        {
            MusicResolver mr = getItem(i);
            if(mr.getChecked())selected.add(mr);
            i = i + 1;
        }

        return selected;
    }

    private static class ViewHolder{
        TextView songtitle;
        TextView songartist;
        CheckBox selected;
    }
}
