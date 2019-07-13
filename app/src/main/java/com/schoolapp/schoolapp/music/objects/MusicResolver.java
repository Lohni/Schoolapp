package com.schoolapp.schoolapp.music.objects;

public class MusicResolver {

    private long id, albumid;
    private String title;
    private String artist;
    private boolean ischecked=false;

    public MusicResolver(long id, String title, String artist, long albumid){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumid = albumid;
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

    public long getAlbumid(){return albumid;}

}
