package com.schoolapp.schoolapp.music.fragments;

import android.Manifest;
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
import android.widget.Button;
import android.widget.ListView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.objects.MusicResolver;
import com.schoolapp.schoolapp.music.adapter.SelectionAdapter;
import com.schoolapp.schoolapp.music.databasehelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class PlaylistSelector extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 0x03;
    private View view;
    private ListView selection;
    private ArrayList<MusicResolver> arrayList;
    private SelectionAdapter sAdapter;
    private Button selected;

    private static String table;
    private static databasehelper db;

    public PlaylistSelector() {
        // Required empty public constructor
    }

    public static PlaylistSelector newInstance(String tab, databasehelper databasehelper) {
        PlaylistSelector fragment = new PlaylistSelector();
        table = tab;
        db = databasehelper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playlist_selector, container, false);
        selection = view.findViewById(R.id.selection);
        selected = view.findViewById(R.id.selected);
        arrayList = new ArrayList<>();
        getSongList();
        Collections.sort(arrayList, new Comparator<MusicResolver>(){
            public int compare(MusicResolver a, MusicResolver b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        sAdapter = new SelectionAdapter(getContext(), arrayList);
        selection.setAdapter(sAdapter);

        selection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MusicResolver mr = sAdapter.getItem(i);
                if(mr.getChecked())mr.setChecked(false);
                else mr.setChecked(true);
                arrayList.set(i, mr);
                sAdapter.notifyDataSetChanged();
            }
        });

        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                getActivity().getSupportFragmentManager().popBackStackImmediate();

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
            int albumid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            //add songs to list
            do {
                long thisalbumid = musicCursor.getLong(albumid);
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                arrayList.add(new MusicResolver(thisId, thisTitle, thisArtist, thisalbumid));
            }
            while (musicCursor.moveToNext());
        }
    }

    private boolean checkPermission() {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //If the app does have this permission, then return true//
        //If the app doesn’t have this permission, then return false//
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void setSelected(){
        int i = 0, max = arrayList.size();
        while (i < max){
            MusicResolver mr = arrayList.get(i);
            if(mr.getChecked()){
                db.addNew(mr.getTitle(), mr.getArtist(), mr.getId(), table, mr.getAlbumid());
            }
            i = i + 1;
        }
    }
}
