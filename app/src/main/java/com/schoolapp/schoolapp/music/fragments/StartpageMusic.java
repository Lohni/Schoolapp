package com.schoolapp.schoolapp.music.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.Musicplayer;


public class StartpageMusic extends Fragment {

    private View view;
    private Button favourite, allsongs, playlists, opendrawer;
    private static FragmentManager fm;

    public StartpageMusic() {
        // Required empty public constructor
    }

    public static StartpageMusic newInstance(FragmentManager fragmentManager) {
        fm = fragmentManager;
        StartpageMusic fragment = new StartpageMusic();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_startpage_music, container, false);
        playlists = view.findViewById(R.id.playlistbttn);
        allsongs = view.findViewById(R.id.allsongbttn);
        favourite = view.findViewById(R.id.favouritesbttn);
        opendrawer = view.findViewById(R.id.menubttn);
        playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(getId(), new PlaylistViewPager()).addToBackStack("Main").commit();
            }
        });
        allsongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(getId(), new MusicList()).addToBackStack("Main").commit();
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Musicplayer)getActivity()).openDrawer();
            }
        });

        return view;
    }
}