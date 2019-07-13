package com.schoolapp.schoolapp.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.objects.AlbumResolver;

import java.util.ArrayList;

public class AlbumAdapter extends BaseAdapter {

    private ArrayList<AlbumResolver> albumlist;
    private LayoutInflater albumInf;
    private Context context;


    public AlbumAdapter(Context c,ArrayList<AlbumResolver> album){
        albumlist=album;
        albumInf = LayoutInflater.from(c);
        context = c;
    }

    @Override
    public int getCount() {
        return albumlist.size();
    }

    @Override
    public AlbumResolver getItem(int i) {
        return albumlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, final View convertview, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view = convertview;

        if(view == null){
            view = albumInf.inflate(R.layout.albumitem, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.albumname = view.findViewById(R.id.albumname);
            viewHolder.albumsize = view.findViewById(R.id.albumsize);
            viewHolder.cover = view.findViewById(R.id.cover);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.albumname.setText(getItem(i).getName());
        viewHolder.albumsize.setText(getItem(i).getContentSize() + " songs");
        viewHolder.cover.setImageBitmap(getItem(i).getCover());

        return view;
    }

    private static class ViewHolder{
        TextView albumname;
        TextView albumsize;
        ImageView cover;
    }
}

