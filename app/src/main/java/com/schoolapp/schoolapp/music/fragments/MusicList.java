package com.schoolapp.schoolapp.music.fragments;

import android.Manifest;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.objects.MusicResolver;
import com.schoolapp.schoolapp.music.Musicplayer;
import com.schoolapp.schoolapp.music.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MusicList extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 0x03 ;

    // TODO: Rename and change types of parameters
    private View view;
    private ArrayList<MusicResolver> arrayList;
    private ListView listView;
    private SongAdapter songAdt;
    private Button nav_drawer;
    Map<String, Integer> mapIndex;

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
        nav_drawer = view.findViewById(R.id.menubttn);
        arrayList = new ArrayList<>();
        getSongList();

        Collections.sort(arrayList, new Comparator<MusicResolver>(){
            public int compare(MusicResolver a, MusicResolver b){
                return a.getTitle().compareToIgnoreCase(b.getTitle());
            }
        });

        listView = view.findViewById(R.id.songlist);
        songAdt = new SongAdapter(getContext(), arrayList);
        listView.setAdapter(songAdt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MusicResolver  currsong = songAdt.getItem(i);
                mSongSelected.OnSongSelected(currsong);
                mCallback.OnSonglistCreated(arrayList);
            }
        });
        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Musicplayer)getActivity()).openDrawer();
            }
        });

        getIndexList();
        displayIndex();

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
        if(musicCursor != null)musicCursor.close();
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

    private void getIndexList() {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < arrayList.size(); i++) {
            MusicResolver item = arrayList.get(i);
            String index = item.getTitle().substring(0,1);
            Character character = index.charAt(0);

            if(character <=64 || character >=123){
                index = "#";
            } else if(character >= 91 && character <= 96)index = "#";
            else if(character >96){
                character = Character.toUpperCase(character);
                index = character.toString();
            }

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

    private void displayIndex() {
        LinearLayout indexLayout = view.findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            if(mapIndex.size() < 24)textView.setTextSize(12);
            else textView.setTextSize(13);
            textView.setText(index);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView selectedIndex = (TextView) view;
                    listView.setSelection(mapIndex.get(selectedIndex.getText()));
                }
            });
            indexLayout.addView(textView);
        }
    }
}
