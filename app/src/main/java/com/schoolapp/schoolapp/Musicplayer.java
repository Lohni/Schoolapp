package com.schoolapp.schoolapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class Musicplayer extends AppCompatActivity
        implements  MusicList.OnSonglistCreatedListener, MusicList.OnSongSelectedListener, Musicstate.OnStateChangeListener, PreparedInterface, Playlistsongs.OnPlaylistSongSelectedListener{

    private ArrayList<MusicResolver> arrayList;

    //For Music Service
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    //Init Viewmodel
    private SeekbarViewModel seekbarViewModel;
    private MusicViewModel musicViewModel;

    private PreparedInterface preparedInterface;
    private Boolean isOnPause = false;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        loadMusicstate( new Musicstate());
        loadStartpage();
        // Set up the ViewPager with the sections adapter.
        seekbarViewModel = ViewModelProviders.of(this).get(SeekbarViewModel.class);
        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        preparedInterface = this;
    }

     Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isOnPause)seekUpdation();

        }
    };

    public void seekUpdation(){
        seekbarViewModel.setCurrpos(musicSrv.getPosn());
        mHandler.postDelayed(runnable, 500);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(arrayList);
            musicSrv.setInterface(preparedInterface);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void loadMusicstate(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.playstate, fragment);
        ft.commit();
    }

    private void loadStartpage() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, StartpageMusic.newInstance(fm));
        ft.commit();
    }

    @Override
    public void OnSonglistCreated(ArrayList<MusicResolver> songlist) {
        arrayList = songlist;
    }

    @Override
    public void OnSongSelected(MusicResolver currsong) {
        musicSrv.setSong(currsong);
        musicSrv.playSong();
        musicViewModel.select(musicSrv.getSong());
        isOnPause = false;
    }

    @Override
    public void OnStateChanged(int changecode) {
        switch(changecode){
            case 1 : {
                if(musicSrv.isPng()){musicSrv.pausePlayer(); isOnPause=true;}
                else {musicSrv.go(); isOnPause=false; seekUpdation();}
                break;
            }
            case 2 : break;
            case 3 : {
                      musicSrv.skip();
                      MusicViewModel mVM = ViewModelProviders.of(this).get(MusicViewModel.class);
                      mVM.select(musicSrv.getSong());
                      break;
            }
        }
    }

    @Override
    public void OnSeekbarChanged(int seekpos) {
        musicSrv.seek(seekpos);
    }

    @Override
    public void onPreparedListener(Boolean prepared) {
        seekbarViewModel.setDuration(musicSrv.getDur());
        seekUpdation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnPause = true;
        mHandler.removeCallbacks(runnable);
        playIntent=null;
        if (musicConnection != null) {
            unbindService(musicConnection);
        }
    }

    @Override
    public void OnPlaylistSongSelected(MusicResolver currsong) {
        musicSrv.pausePlayer();
        musicSrv.setSong(currsong);
        musicSrv.playSong();
        musicViewModel.select(musicSrv.getSong());
    }

    @Override
    public void setPlaylist(ArrayList<MusicResolver> playlist) {
        arrayList = playlist;
        musicSrv.setList(arrayList);
    }
}

