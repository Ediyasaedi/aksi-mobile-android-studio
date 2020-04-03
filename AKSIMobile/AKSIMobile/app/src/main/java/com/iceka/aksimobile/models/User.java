package com.iceka.aksimobile.models;

public class User {

    private String uid;
    private String username;
    private String email;
    private String sekolah;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String uid, String username, String email, String sekolah) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.sekolah = sekolah;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getSekolah() {
        return sekolah;
    }
}
