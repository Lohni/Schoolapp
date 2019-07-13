package com.schoolapp.schoolapp.music.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.objects.MusicResolver;
import com.schoolapp.schoolapp.music.viewmodel.MusicViewModel;
import com.schoolapp.schoolapp.music.viewmodel.SeekbarViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PlaybackControl extends Fragment {

    private TextView songtitle, songartist, dur, cp;
    private Button play, skipback, skipforward, repeat, shuffle;
    private SeekBar seekBar;
    private ImageView cover;
    private long albumid;

    private int seekto;
    private static int currpos;
    private boolean seekfromuser=false;
    private static boolean isplaying;

    //Shuffle and repeat
    private boolean isshuffle  = false, isrepeat = false;

    public static PlaybackControl newInstance(int pos, boolean playing) {
        PlaybackControl fragment = new PlaybackControl();
        currpos = pos;
        isplaying = playing;
        return fragment;
    }

    //Interface for Play Control
    OnPlayControlChangeListener mChange;
    public interface OnPlayControlChangeListener{
        void OnPlayControlChanged(int changecode);
        void OnPlayControlSeekbarChanged(int seekpos);
        // Stop: 1, Play: 2, Skip: 3, Shuffle: 4;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mChange = (OnPlayControlChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStateChangeListener");
        }
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
                if(!isplaying)play.setBackgroundResource(R.drawable.round_play_arrow_48dp);
                else play.setBackgroundResource(R.drawable.round_pause_48dp);
                albumid = musicResolver.getAlbumid();
                setCover();
                seekBar.setProgress(currpos);
            }
        });
        musicViewModel.getShuffle().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean)shuffle.setBackgroundResource(R.drawable.round_button);
                else shuffle.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playback_control, container, false);
        songtitle = view.findViewById(R.id.title);
        songtitle.setSelected(true);
        songartist = view.findViewById(R.id.artist);
        play = view.findViewById(R.id.playcontrol);
        skipback = view.findViewById(R.id.skipback);
        skipforward = view.findViewById(R.id.skipforward);
        seekBar = view.findViewById(R.id.seekbarcontrol);
        dur = view.findViewById(R.id.songduration);
        cp = view.findViewById(R.id.currentpos);
        cover = view.findViewById(R.id.cover);
        shuffle = view.findViewById(R.id.shuffle);
        repeat = view.findViewById(R.id.repeat);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChange.OnPlayControlChanged(1);
            }
        });
        skipforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChange.OnPlayControlChanged(3);
                setCover();
            }
        });
        skipback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChange.OnPlayControlChanged(2);
                setCover();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekfromuser){
                    seekto = i;
                    mChange.OnPlayControlSeekbarChanged(seekto);
                }
                else seekfromuser=true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isshuffle){
                    shuffle.setBackgroundResource(R.drawable.round_button);
                    isshuffle = true;
                }
                else {
                    shuffle.setBackgroundColor(getResources().getColor(R.color.transparent));
                    isshuffle = false;
                }
                mChange.OnPlayControlChanged(4);
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isrepeat){
                    shuffle.setBackgroundResource(R.drawable.round_button);
                    isrepeat = true;
                }
                else {
                    shuffle.setBackgroundColor(getResources().getColor(R.color.transparent));
                    isrepeat = false;
                }
                mChange.OnPlayControlChanged(5);
            }
        });

        final SeekbarViewModel seekbarViewModel = ViewModelProviders.of(getActivity()).get(SeekbarViewModel.class);
           seekbarViewModel.getCurrpos().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer newpos) {
                seekfromuser=false;
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
            }
        });

        return view;
    }

    public void setCover(){
        try {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
            ContentResolver res = getContext().getContentResolver();
            InputStream in = null;
            in = res.openInputStream(uri);
            Bitmap artwork = BitmapFactory.decodeStream(in);
            cover.setImageBitmap(artwork);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.musik);
            cover.setImageBitmap(bitmap);
        }
    }
}