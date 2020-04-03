package com.iceka.aksimobile.models;

public class Nilai {
    private int highScore;
    private int jawabanBenar;
    private int skor;
    private long timestamp;

    public Nilai() {
    }

    public Nilai(int highScore, int jawabanBenar, int skor, long timestamp) {
        this.highScore = highScore;
        this.jawabanBenar = jawabanBenar;
        this.skor = skor;
        this.timestamp = timestamp;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getJawabanBenar() {
        return jawabanBenar;
    }

    public int getSkor() {
        return skor;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
