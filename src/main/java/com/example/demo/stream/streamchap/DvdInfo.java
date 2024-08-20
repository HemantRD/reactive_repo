package com.example.demo.stream.streamchap;

public class DvdInfo {
    String title;
    String genre;
    String leadActor;

    DvdInfo(String t, String g, String a) {
        title = t;
        genre = g;
        leadActor = a;
    }

    public String toString() {
        return title + " / " + genre + " / " + leadActor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }
}
