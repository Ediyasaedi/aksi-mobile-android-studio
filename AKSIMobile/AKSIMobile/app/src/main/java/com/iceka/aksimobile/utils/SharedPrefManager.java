package com.iceka.aksimobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iceka.aksimobile.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "spName";

    private static final String KEY_NAME = "spName";
    private static final String KEY_EMAIL = "spEmail";
    private static final String KEY_UID = "spUid";
    private static final String KEY_SEKOLAH = "spSekolah";
    private static final String TOTAL_ARTIKEL = "spArtikel";
    private static final String TOTAL_SOAL = "spSoal";
    private static final String CURRENT_ARTIKEL = "spPosArtikel";
    private static final String CURRENT_SOAL = "spPosSoal";
    private static final String HIGH_SCORE = "spHighScore";
    private static final String WACANA = "spWacana";
    private static final String THUMBNAIL = "spThumbnail";
    private static final String SCORE = "spScore";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        spEditor = mSharedPreferences.edit();
    }

    public void userInput(User user) {
        spEditor.putString(KEY_UID, user.getUid());
        spEditor.putString(KEY_NAME, user.getUsername());
        spEditor.putString(KEY_EMAIL, user.getEmail());
        spEditor.putString(KEY_SEKOLAH, user.getSekolah());
        spEditor.apply();
    }

    public User getUser() {
        return new User(
                mSharedPreferences.getString(KEY_UID, null),
                mSharedPreferences.getString(KEY_NAME, null),
                mSharedPreferences.getString(KEY_EMAIL, null),
                mSharedPreferences.getString(KEY_SEKOLAH, null)
        );
    }

    public void setTotalArtikel(int i) {
        spEditor.putInt(TOTAL_ARTIKEL, i);
        spEditor.apply();
    }

    public int getTotalArtikel() {
        return mSharedPreferences.getInt(TOTAL_ARTIKEL, 0);
    }

    public void setCurrent(int i) {
        spEditor.putInt(TOTAL_SOAL, i);
        spEditor.apply();
    }

    public int getCurrent() {
        return mSharedPreferences.getInt(TOTAL_SOAL, 0);
    }

    public void clearReference() {
        spEditor.clear();
        spEditor.apply();
    }

    public void setWacana(String wacana) {
        spEditor.putString(WACANA, wacana);
        spEditor.apply();
    }

    public String getWacana() {
        return mSharedPreferences.getString(WACANA, "");
    }

    public void setThumbnail(String thumbnail) {
        spEditor.putString(THUMBNAIL, thumbnail);
        spEditor.apply();
    }

    public String getThumbnail() {
        return mSharedPreferences.getString(THUMBNAIL, "");
    }

    public void setArtikelId(String id) {
        spEditor.putString(CURRENT_ARTIKEL, id);
        spEditor.apply();
    }

    public String getArtikelId() {
        return mSharedPreferences.getString(CURRENT_ARTIKEL, "");
    }

    public void setScore(int score) {
        spEditor.putInt(SCORE, score);
        spEditor.apply();
    }

    public int getScore() {
        return mSharedPreferences.getInt(SCORE, 0);
    }

    public void setHighScore(int highScore) {
        spEditor.putInt(HIGH_SCORE, highScore);
        spEditor.apply();
    }

    public int getHighScore() {
        return mSharedPreferences.getInt(HIGH_SCORE, 0);
    }
}
