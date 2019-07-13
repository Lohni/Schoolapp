package com.schoolapp.schoolapp.music.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EQViewModel extends ViewModel {

    private final MutableLiveData<Short[]> Bandlevel = new MutableLiveData<>();

    public void setBandlvl(Short[] bandlvl){Bandlevel.setValue(bandlvl);}

    public LiveData<Short[]> getBandlevel(){return Bandlevel;}

}
