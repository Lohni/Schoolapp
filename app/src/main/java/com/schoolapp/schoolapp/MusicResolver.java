package com.schoolapp.schoolapp;

public class MusicResolver {

    private long id;
    private String title;
    private String artist;
    private boolean ischecked=false;

    public MusicResolver(long id, String title, String artist){
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setChecked(boolean checked){ischecked = checked;}

    public boolean getChecked(){return ischecked;}

}
