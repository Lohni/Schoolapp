package com.schoolapp.schoolapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SeekbarViewModel extends ViewModel {
    private final MutableLiveData<Integer> duration = new MutableLiveData<>();
    private final MutableLiveData<Integer> currpos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isplaying = new MutableLiveData<>();

    public void setDuration(Integer dur){
        duration.setValue(dur);
    }
    public void setIsplaying(Boolean isplaying) {this.isplaying.setValue(isplaying);}
    public void setCurrpos(Integer cpos){
        currpos.setValue(cpos);
    }

    public LiveData<Integer> getDuration(){return duration;}
    public LiveData<Integer> getCurrpos(){return currpos;}
    public LiveData<Boolean> getIsplaying(){return isplaying;}
}
