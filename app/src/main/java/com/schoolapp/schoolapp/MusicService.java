package com.schoolapp.schoolapp;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<MusicResolver> songs; //For shuffle
    private MusicResolver currsong;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<MusicResolver> theSongs){
        songs=theSongs;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong(){
        player.reset();
        //get song
        //get id
        long songId = currsong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songId);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void setSong(MusicResolver song){
        currsong = song;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void skip(){
        int pos = songs.indexOf(currsong);
        setSong(songs.get(pos+1));
        playSong();
    }

    public MusicResolver getSong(){
        return currsong;
    }

}
