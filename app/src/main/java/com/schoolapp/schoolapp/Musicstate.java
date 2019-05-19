package com.schoolapp.schoolapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    private boolean isplaying = true;

    private int seekto;

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
                    isplaying = true;
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
        seekBar = view.findViewById(R.id.seekbarcontrol);
        dur = view.findViewById(R.id.songduration);
        cp = view.findViewById(R.id.currentpos);
        songinfos = view.findViewById(R.id.lineartitle);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                else seekfromuser=false;
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
        seekbarViewModel.getIsplaying().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean)play.setBackgroundResource(R.drawable.round_pause_48dp);
                else play.setBackgroundResource(R.drawable.round_play_arrow_48dp);
                isplaying = aBoolean;
            }
        });

        songinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.parentcontainer, PlaybackControl.newInstance(seekBar.getProgress(), isplaying)).addToBackStack("unknown").commit();
            }
        });

        return view;
    }
}