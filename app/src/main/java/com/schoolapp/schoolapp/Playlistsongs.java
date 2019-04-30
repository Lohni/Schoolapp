package com.schoolapp.schoolapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Playlistsongs extends Fragment{

    private static final String DESCRIBABLE_KEY = "describable_key" ;
    private View view;
    private static databasehelper db;

    private TextView playlist;
    private ListView playlistsongs;
    private ArrayList<MusicResolver> songlist;
    private static String table;
    private Button add;
    public SongAdapter songAdapter;

    public Playlistsongs() {
        // Required empty public constructor
    }

    public static Playlistsongs newInstance(String tab) {
        table = tab;
        Playlistsongs fragment = new Playlistsongs();
        return fragment;
    }

    OnPlaylistSongSelectedListener mSongSelected;
    public interface OnPlaylistSongSelectedListener{
        void OnPlaylistSongSelected(MusicResolver currsong);
        void setPlaylist(ArrayList<MusicResolver> playlist);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
           mSongSelected = (OnPlaylistSongSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSongSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playlistsongs, container, false);
        playlistsongs = view.findViewById(R.id.playlistsongs);
        playlist = view.findViewById(R.id.table);
        add = view.findViewById(R.id.addsongs);
        playlist.setText(table);
        db = new databasehelper(getActivity());
        getSonglist();
        songAdapter = new SongAdapter(getContext(),songlist);
        playlistsongs.setAdapter(songAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(table != null){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, PlaylistSelector.newInstance(table, db)).addToBackStack("Playlist").commit();
                }
            }
        });

        playlistsongs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteItem(table, songAdapter.getItem(i).getId(), i);
                return true;
            }
        });


        playlistsongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSongSelected.setPlaylist(songlist);
                mSongSelected.OnPlaylistSongSelected(songAdapter.getItem(i));
            }
        });
        return view;
    }

    private void getSonglist(){
        songlist = new ArrayList<>();
        Cursor data =db.getListContents(table);
        if(data != null){
            while(data.moveToNext()){
                long thisId = data.getLong(0);
                String thisTitle = data.getString(1);
                String thisArtist = data.getString(2);
                songlist.add(new MusicResolver(thisId,thisTitle,thisArtist));
            }
        } else {
            //Toast.makeText(getActivity(), "Table doesnt exist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteItem(final String name, final long id, final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(db.deleteItem(name, id)){
                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                    getSonglist();
                    songAdapter.remove(position);
                    songAdapter.notifyDataSetChanged();
                }
                else Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }
}