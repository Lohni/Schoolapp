package com.schoolapp.schoolapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Musicstate extends Fragment{

    //Initialize Layout-Objects
    private TextView songtitle, songartist, dur,cp;
    private Button play, skip;
    private SeekBar seekBar;
    private LinearLayout songinfos;

    //
    private int playID = R.drawable.round_pause_48dp;
    private int seekto, currpos = 0, duration;

    private boolean seekfromuser=false;

    public Musicstate() {
        // Required empty public constructor
    }

    public static Musicstate newInstance(String param1, String param2) {
        Musicstate fragment = new Musicstate();
        return fragment;
    }

    //Interface for Play Control
    OnStateChangeListener mChange;
    public interface OnStateChangeListener{
        void OnStateChanged(int changecode);
        void OnSeekbarChanged(int seekpos);
        // Stop: 1, Play: 2, Skip: 3, Shuffle: 4;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mChange = (OnStateChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStateChangeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Communication(Viewmodel) between MusicList and Musicstate
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MusicViewModel musicViewModel = ViewModelProviders.of(getActivity()).get(MusicViewModel.class);
        musicViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<MusicResolver>() {
            @Override
            public void onChanged(@Nullable MusicResolver musicResolver) {
                    songtitle.setText(musicResolver.getTitle());
                    songartist.setText(musicResolver.getArtist());
                    play.setBackgroundResource(R.drawable.round_pause_48dp);
                    playID= R.drawable.round_pause_48dp;
                    seekBar.setProgress(0);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicstate, container, false);

        songtitle = view.findViewById(R.id.songtitle);
        songtitle.setSelected(true);
        songartist = view.findViewById(R.id.songartist);
        play = view.findViewById(R.id.play);
        skip = view.findViewById(R.id.skip);
        seekBar = view.findViewById(R.id.seekBar);
        dur = view.findViewById(R.id.duration);
        cp = view.findViewById(R.id.currpos);
        songinfos = view.findViewById(R.id.lineartitle);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonimage();
                mChange.OnStateChanged(1);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mChange.OnStateChanged(3);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(!seekfromuser){
                    seekto = i;
                    mChange.OnSeekbarChanged(seekto);
                }
                seekfromuser=false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekbarViewModel seekbarViewModel = ViewModelProviders.of(getActivity()).get(SeekbarViewModel.class);
        seekbarViewModel.getCurrpos().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer newpos) {
                seekfromuser=true;
                seekBar.setProgress(newpos);
                float d = newpos/1000;
                d = d/60;
                int min = (int)d;
                float seconds = (d -min)*60;
                int sec = (int)seconds;
               if(sec < 10)cp.setText(min + ":0" + sec);
               else cp.setText(min + ":" + sec);
            }
        });

        seekbarViewModel.getDuration().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer durationn) {
                float d = durationn /1000;
                d = d/60;
                int min = (int)d;
                float seconds = (d - min)*60;
                int sec = (int)seconds;
                dur.setText(min + ":" + sec);
                seekBar.setMax(durationn);
            }
        });

        songinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Absolute Playback Control in Development", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //Change Button Imsage after Interaction
    public void buttonimage(){
        switch(playID){
            case R.drawable.round_play_arrow_48dp:  play.setBackgroundResource(R.drawable.round_pause_48dp); playID= R.drawable.round_pause_48dp; break;
            case R.drawable.round_pause_48dp:       play.setBackgroundResource(R.drawable.round_play_arrow_48dp);playID= R.drawable.round_play_arrow_48dp; break;
        }
    }
}