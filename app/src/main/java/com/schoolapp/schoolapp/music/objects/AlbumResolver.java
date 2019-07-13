package com.schoolapp.schoolapp.music.objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class AlbumResolver {
    private long albumID;
    private String Name, Artist;
    private ArrayList<MusicResolver> content;
    private Bitmap cover;

    public AlbumResolver(long ID, String Name, String Artist, ArrayList<MusicResolver> content, Bitmap cover){
        this.albumID = ID;
        this.Name = Name;
        this.Artist = Artist;
        this.content = content;
        this.cover = cover;
    }

    public long getAlbumID() {
        return albumID;
    }

    public String getName() {
        return Name;
    }

    public String getArtist() {
        return Artist;
    }

    public ArrayList<MusicResolver> getContent() {
        return content;
    }

    public int getContentSize(){
        return content.size();
    }

    public Bitmap getCover() {
        return cover;
    }
}
