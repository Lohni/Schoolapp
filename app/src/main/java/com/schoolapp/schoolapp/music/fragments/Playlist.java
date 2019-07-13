package com.schoolapp.schoolapp.music.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.Musicplayer;
import com.schoolapp.schoolapp.music.adapter.PlaylistAdapter;
import com.schoolapp.schoolapp.music.databasehelper;

import java.util.ArrayList;

public class Playlist extends Fragment {

    //Database
    private com.schoolapp.schoolapp.music.databasehelper databasehelper;

    private View view;

    private Button addplaylist, nav_drawer;
    private ListView playlist;
    //Listview Adapter
    private PlaylistAdapter pAdapter;
    //ArrayLists for Listview
    private ArrayList<String> playlisttitle, songsize;
    private String newtable;

    public Playlist() {
        // Required empty public constructor
    }



    public static Playlist newInstance(PlaylistselectedListener p) {
        Playlist fragment = new Playlist();
        psL = p;
        return fragment;
    }

    static PlaylistselectedListener psL;
    public interface PlaylistselectedListener{
        void onPlaylistSelected(String table);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        addplaylist = view.findViewById(R.id.addplaylist);
        playlist = view.findViewById(R.id.playlist);
        databasehelper = new databasehelper(getActivity());
        nav_drawer = view.findViewById(R.id.menubttn);
        populateListView();

        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Musicplayer)getActivity()).openDrawer();
            }
        });
        addplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newtable = input.getText().toString();
                        if(databasehelper.newTable(newtable)){
                            Toast.makeText(getActivity(),"Playlist created", Toast.LENGTH_SHORT).show();
                            playlisttitle.add(newtable);
                            pAdapter.notifyDataSetChanged();
                        }
                        else Toast.makeText(getActivity(),"Playlist already exists", Toast.LENGTH_SHORT).show();
                        populateListView();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                psL.onPlaylistSelected(pAdapter.getItem(i));
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
    public void onDestroy() {
        super.onDestroy();
        databasehelper.close();
    }
}
