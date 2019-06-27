package com.schoolapp.schoolapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Album extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 0x03;
    private ArrayList<AlbumResolver> albumList;
    private ListView listView;
    private AlbumAdapter albumAdapter;
    private Button nav_drawer;

    public Album() {
        // Required empty public constructor
    }

    public static Album newInstance() {
        Album fragment = new Album();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        listView = view.findViewById(R.id.albumlist);
        nav_drawer = view.findViewById(R.id.menubttn);
        albumList = new ArrayList<>();
        getAlbumList();
        albumAdapter = new AlbumAdapter(getContext(), albumList);
        listView.setAdapter(albumAdapter);


        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Musicplayer)getActivity()).openDrawer();
            }
        });
        return view;
    }

    private void getAlbumList() {
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
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int album = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int lastalbum = 0;

            //add songs to list
            do {
                long thisalbumid = musicCursor.getLong(albumid);
                String thisArtist = musicCursor.getString(artistColumn);
                String AlbumName = musicCursor.getString(album);
                Uri artUri = ContentUris.withAppendedId(sArtworkUri, thisalbumid);
                if(lastalbum < thisalbumid){
                    try {
                        InputStream in = null;
                        in = contentResolver.openInputStream(artUri);
                        Bitmap artwork = BitmapFactory.decodeStream(in);
                        albumList.add(new AlbumResolver(thisalbumid, AlbumName, thisArtist, getAlbumContent(thisalbumid), artwork));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.musik);
                        albumList.add(new AlbumResolver(thisalbumid, AlbumName, thisArtist, getAlbumContent(thisalbumid), bitmap));
                    }
                    lastalbum++;
                }
            }
            while (musicCursor.moveToNext());
        }
        if(musicCursor != null)musicCursor.close();
    }

    private ArrayList<MusicResolver> getAlbumContent(long AlbumID){
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
            ArrayList<MusicResolver> content = new ArrayList<>();
        if(content!=null && cursor.moveToFirst()) {
            //get columns
            int titleColumn = cursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumid = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            //add songs to list
            do {
                long thisalbumid = cursor.getLong(albumid);
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                if (thisalbumid == AlbumID)
                    content.add(new MusicResolver(thisId, thisTitle, thisArtist, thisalbumid));
            }
            while (cursor.moveToNext());
        }
            return content;
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
}
