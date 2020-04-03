package com.iceka.aksimobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.models.Artikel;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    private TextView judul;
    private TextView konten;
    private ImageView gambar;
    private Button btnNext;

    private DatabaseReference artikelReference;

    private List<Artikel> artikelList = new ArrayList<>();

    private int current;

    private SharedPrefManager mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        judul = findViewById(R.id.tv_judul_wacana_artikel);
        konten = findViewById(R.id.tv_konten_artikel);
        gambar = findViewById(R.id.img_artikel);
        btnNext = findViewById(R.id.btnNext);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        artikelReference = firebaseDatabase.getReference().child("artikel");


        mSharedPreferences = new SharedPrefManager(this);

        getData();
    }

    private void getData() {
        artikelReference.child(mSharedPreferences.getWacana()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Artikel artikel = snapshot.getValue(Artikel.class);
                    artikelList.add(artikel);
                }
//                Toast.makeText(ArticleActivity.this, "shared pref : " + mSharedPreferences.getTotalArtikel(), Toast.LENGTH_SHORT).show();

                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadData() {
        current = mSharedPreferences.getCurrent();
        if (current < artikelList.size()) {
            final Artikel artikel = artikelList.get(current);
            judul.setText(artikel.getJudul());
            konten.setText(artikel.getKonten());
            Glide.with(getApplicationContext())
                    .load(artikel.getGambarUrl())
                    .into(gambar);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (posisi < total) {
//                        mSharedPreferences.setCurrent(posisi);
//                        Log.i("MYTAG", "setCurrent : " + mSharedPreferences.getCurrent());
//                        Intent intent = new Intent(ArticleActivity.this, InstruksiActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "berakhir", Toast.LENGTH_SHORT).show();
//                    }
                    mSharedPreferences.setArtikelId(artikel.getId());
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
//            btnNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(ArticleActivity.this, QuizActivity.class);
//                    intent.putExtra("artikelId", artikel.getId());
//                    intent.putExtra("wacana", artikel.getWacana());
//                    startActivity(intent);
//                    Toast.makeText(ArticleActivity.this, "ceks", Toast.LENGTH_SHORT).show();
//                }
//            });
            current++;
        }
    }
}
