package com.iceka.aksimobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iceka.aksimobile.models.Nilai;
import com.iceka.aksimobile.utils.SharedPrefManager;

public class SkorResultActivity extends AppCompatActivity {

    private TextView tvHelloUSer;
    private TextView tvScore;
    private TextView tvHighScore;
    private TextView tvWacana;
    private Button btnSave;
    private Button btnTry;

    private String score;

    private SharedPrefManager mSharedPreferences;

    private DatabaseReference hasilReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor_result);

        tvScore = findViewById(R.id.tV_resultSkor_value);
        tvHighScore = findViewById(R.id.tv_highscore_value);
        btnSave = findViewById(R.id.btn_Save);
        btnTry = findViewById(R.id.btn_Try);
        tvHelloUSer = findViewById(R.id.tv_hello_user);
        tvWacana = findViewById(R.id.tv_wacana_dipilih);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        hasilReference = firebaseDatabase.getReference().child("hasil");

        mSharedPreferences = new SharedPrefManager(this);

        tvHelloUSer.setText("Hi, " + mSharedPreferences.getUser().getUsername());
        tvWacana.setText("Wacana : "+ mSharedPreferences.getWacana());
        Log.i("MYTAG", "hasil akhir : " + mSharedPreferences.getScore());
        tvScore.setText(String.valueOf(mSharedPreferences.getScore()*5));
        tvHighScore.setText(String.valueOf(mSharedPreferences.getHighScore()));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                startActivity(new Intent(SkorResultActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }

    private void saveData() {
        int nilaiHasil = mSharedPreferences.getScore() * 5;
        long timestamp = System.currentTimeMillis();
        Nilai nilai = new Nilai(mSharedPreferences.getHighScore(), mSharedPreferences.getScore(), nilaiHasil, timestamp);
        hasilReference.child(mSharedPreferences.getUser().getUid()).child(mSharedPreferences.getWacana()).setValue(nilai);
    }
}
