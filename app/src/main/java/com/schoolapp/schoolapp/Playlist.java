package com.schoolapp.schoolapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Playlist extends Fragment implements PlaylistNameInterface {

    //Database
    private databasehelper databasehelper;
    //private Cursor data;

    private View view;

    private Button addplaylist;
    private ListView playlist;
    //Listview Adapter
    private PlaylistAdapter pAdapter;
    //ArrayLists for Listview
    private ArrayList<String> playlisttitle, songsize;
    private ArrayList<Integer> selected;

    private PlaylistNameInterface pni;

    public Playlist() {
        // Required empty public constructor
    }

    public static Playlist newInstance() {
        Playlist fragment = new Playlist();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pni = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        addplaylist = view.findViewById(R.id.addplaylist);
        playlist = view.findViewById(R.id.playlist);
        databasehelper = new databasehelper(getActivity());

        //GetPlaylists();g
        playlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        playlist.setLongClickable(true);
        populateListView();

        //OnClickListeners
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        addplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(databasehelper.newTable())Toast.makeText(getActivity(),"Playlist created", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(),"Playlist already exists", Toast.LENGTH_SHORT).show();
                populateListView();
            }
        });
        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletePlaylist(pAdapter.getItem(position), position);
                return true;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void populateListView(){
        Cursor songs = databasehelper.getTables();
        songsize = new ArrayList<>();
        playlisttitle = new ArrayList<>();
        while(songs.moveToNext()){
            if(!songs.getString(1).equals("android_metadata")){
                playlisttitle.add(songs.getString(1));
                if(databasehelper.getTableSize(songs.getString(1)) == null)songsize.add("0");
                else  songsize.add(databasehelper.getTableSize(songs.getString(1)));
            }
        }
        pAdapter = new PlaylistAdapter(getActivity(), playlisttitle, songsize);
        playlist.setAdapter(pAdapter);
        pAdapter.setPlaylistNameInterface(pni);
    }

    private boolean deletePlaylist(final String name, final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databasehelper.deleteTable(name);
                playlisttitle.remove(position);
                pAdapter.notifyDataSetChanged();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }

    @Override
    public void OnPlaylistNameChanged(String newname, String oldname) {
        if(databasehelper.renameTable(newname, oldname))Toast.makeText(getActivity(), "Rename successfull", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), "Rename failed", Toast.LENGTH_SHORT).show();
    }
}
