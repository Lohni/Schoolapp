package com.schoolapp.schoolapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Musicstate extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView songtitle, songartist;
    private Button play, skip;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Musicstate() {
        // Required empty public constructor
    }

    public static Musicstate newInstance(String param1, String param2) {
        Musicstate fragment = new Musicstate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MusicViewModel musicViewModel = ViewModelProviders.of(getActivity()).get(MusicViewModel.class);
        musicViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<MusicResolver>() {
            @Override
            public void onChanged(@Nullable MusicResolver musicResolver) {
                    songtitle.setText(musicResolver.getTitle());
                    songartist.setText(musicResolver.getArtist());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicstate, container, false);

        songtitle = view.findViewById(R.id.songtitle);
        songartist = view.findViewById(R.id.songartist);
        play = view.findViewById(R.id.play);
        skip = view.findViewById(R.id.skip);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

}
