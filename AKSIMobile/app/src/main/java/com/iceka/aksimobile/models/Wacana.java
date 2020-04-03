package com.iceka.aksimobile.models;

public class Wacana {

    private String judul;
    private String gambarUrl;
    private String id;

    public Wacana() {
    }

    public Wacana(String id, String judul, String gambarUrl) {
        this.id = id;
        this.judul = judul;
        this.gambarUrl = gambarUrl;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }
}
