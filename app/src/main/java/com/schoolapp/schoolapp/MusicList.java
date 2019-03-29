package com.schoolapp.schoolapp;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MusicList extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 0x03 ;

    // TODO: Rename and change types of parameters
    private View view;
    private ArrayList<MusicResolver> arrayList;
    private ListView listView;
    private SongAdapter songAdt;


    private MusicViewModel musicViewModel;

    public MusicList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MusicList newInstance() {
        MusicList fragment = new MusicList();
        return fragment;
    }

    OnSonglistCreatedListener mCallback;
    public interface OnSonglistCreatedListener{
         void OnSonglistCreated(ArrayList<MusicResolver> songlist);
    }

    OnSongSelectedListener mSongSelected;
    public interface OnSongSelectedListener{
         void OnSongSelected(MusicResolver currsong);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSonglistCreatedListener) activity;
            mSongSelected = (OnSongSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSonglistCreatedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_list, container, false);

        arrayList = new ArrayList<>();
        getSongList();
        mCallback.OnSonglistCreated(arrayList);
        Collections.sort(arrayList, new Comparator<MusicResolver>(){
            public int compare(MusicResolver a, MusicResolver b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        listView = view.findViewById(R.id.songlist);
        songAdt = new SongAdapter(getContext(), arrayList);
        listView.setAdapter(songAdt);
        songAdt.notifyDataSetChanged();

        musicViewModel = ViewModelProviders.of(getActivity()).get(MusicViewModel.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MusicResolver  currsong = songAdt.getItem(i);
                mSongSelected.OnSongSelected(currsong);
                musicViewModel.select(currsong);
            }
        });
        return view;
    }

    private void getSongList() {
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();

        if (Build.VERSION.SDK_INT >= 23) {
            //Check whether your app has access to the READ permission//
            if (checkPermission()) {
                //If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
                Log.e("permission", "Permission already granted.");
            } else {
                //If your app doesn’t have permission to access external storage, then call requestPermission//
                requestPermission();
            }
        }

            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                arrayList.add(new MusicResolver(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }



    }



    private boolean checkPermission() {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //If the app does have this permission, then return true//
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //If the app doesn’t have this permission, then return false//
            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

}



}
