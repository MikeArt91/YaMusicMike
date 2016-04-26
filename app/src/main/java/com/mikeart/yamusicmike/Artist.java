package com.mikeart.yamusicmike;

import java.util.ArrayList;

public class Artist {

    private int id, albums, tracks;
    private String name, coverSmallUrl, coverBigUrl, link, description;
    private ArrayList<String> genres;

    public Artist(){
    }

    public Artist(int id, String name, ArrayList<String> genres, int albums, int tracks, String coverSmallUrl, String coverBigUrl, String link, String description) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.albums = albums;
        this.tracks = tracks;
        this.coverSmallUrl = coverSmallUrl;
        this.coverBigUrl = coverBigUrl;
        this.link = link;
        this.description = description;

    }

    public int getId() {return id;}
    public void setId(int id) { this.id = id;}

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }
    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public int getAlbums() { return albums; }
    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public int getTracks() {
        return tracks;
    }
    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public String getCoverSmallUrl() {
        return coverSmallUrl;
    }
    public void setCoverSmallUrl(String coverSmallUrl) {
        this.coverSmallUrl = coverSmallUrl;
    }

    public String getCoverBigUrl() {
        return coverBigUrl;
    }
    public void setCoverBigUrl(String coverBigUrl) {
        this.coverBigUrl = coverBigUrl;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }



}
