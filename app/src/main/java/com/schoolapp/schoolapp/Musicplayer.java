package com.schoolapp.schoolapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class Musicplayer extends AppCompatActivity implements  MusicList.OnSonglistCreatedListener, MusicList.OnSongSelectedListener, Musicstate.OnStateChangeListener {

    protected ViewPager mViewPager;
    private View view;
    private ArrayList<MusicResolver> arrayList;

    //For Music Service
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private int songposnr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        loadFragment( new Musicstate());
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //view = findViewById(R.id.line);
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

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.playstate, fragment);
        ft.commit();
    }


    @Override
    public void OnSonglistCreated(ArrayList<MusicResolver> songlist) {
        arrayList = songlist;
    }

    @Override
    public void OnSongSelected(MusicResolver currsong) {
        setSelectedSong(currsong);
    }

    private void setSelectedSong(MusicResolver currsong){
        musicSrv.setSong(currsong);
        musicSrv.playSong();
    }

    @Override
    public void OnStateChanged(int changecode) {
        switch(changecode){
            case 1 : {
                if(musicSrv.isPng())musicSrv.pausePlayer();
                else musicSrv.go();
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = MusicList.newInstance();
                    break;
                case 1:
                    fragment = MusicList.newInstance();
                    break;
            }
            return fragment;
        }
        
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}

