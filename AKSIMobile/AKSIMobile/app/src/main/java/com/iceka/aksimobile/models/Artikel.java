package com.iceka.aksimobile.models;

public class Artikel {
    private String instruksi;
    private String konten;
    private String gambarUrl;
    private String judul;
    private String id;
    private String wacana;
    private long timestamp;

    public Artikel() {
    }

    public Artikel(String instruksi, String konten, String gambarUrl, String judul) {
        this.instruksi = instruksi;
        this.konten = konten;
        this.gambarUrl = gambarUrl;
        this.judul = judul;
    }

    public Artikel(String instruksi, String konten, String gambarUrl, String judul, String id, String wacana) {
        this.instruksi = instruksi;
        this.konten = konten;
        this.gambarUrl = gambarUrl;
        this.judul = judul;
        this.id = id;
        this.wacana = wacana;
    }

    public String getInstruksi() {
        return instruksi;
    }

    public String getKonten() {
        return konten;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public String getJudul() {
        return judul;
    }

    public String getId() {
        return id;
    }

    public String getWacana() {
        return wacana;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
