package com.schoolapp.schoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter implements Filterable {


    private ArrayList<MusicResolver> songs, mDisplayedValues;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<MusicResolver> theSongs){
        songs=theSongs;
        mDisplayedValues=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDisplayedValues.size();
    }

    @Override
    public MusicResolver getItem(int arg0) {
        return songs.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder{
        TextView songtitle, songartist;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View view = convertView;
        if(view == null){
            view = songInf.inflate(R.layout.song, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.songtitle = view.findViewById(R.id.song_title);
            viewHolder.songartist = view.findViewById(R.id.song_artist);
            view.setTag(viewHolder);
        } else viewHolder = (ViewHolder) view.getTag();

        viewHolder.songtitle.setText(mDisplayedValues.get(i).getTitle());
        viewHolder.songartist.setText(mDisplayedValues.get(i).getArtist());

        return view;
    }

    public void remove(int position){
        songs.remove(position);
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<MusicResolver>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<MusicResolver> FilteredArrList = new ArrayList<MusicResolver>();

                if (songs == null) {
                    songs = new ArrayList<MusicResolver>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = songs.size();
                    results.values = songs;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < songs.size(); i++) {
                        String data = songs.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(songs.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
