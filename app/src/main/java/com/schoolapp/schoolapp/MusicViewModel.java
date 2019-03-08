package com.schoolapp.schoolapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel {
    private final MutableLiveData<MusicResolver> selected = new MutableLiveData<MusicResolver>();

    public void select (MusicResolver musicResolver){

        selected.setValue(musicResolver);
    }

    public LiveData<MusicResolver> getSelected(){
        return selected;
    }

}