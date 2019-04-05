package com.schoolapp.schoolapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SeekbarViewModel extends ViewModel {
    private final MutableLiveData<Integer> duration = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> currpos = new MutableLiveData();

    public void setDuration(Integer dur){
        duration.setValue(dur);
    }

    public void setCurrpos(Integer cpos){
        currpos.setValue(cpos);
    }

    public LiveData<Integer> getDuration(){return duration;}
    public LiveData<Integer> getCurrpos(){return currpos;}

}
