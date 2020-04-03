package com.iceka.aksimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.models.Artikel;
import com.iceka.aksimobile.models.Nilai;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class InstruksiActivity extends AppCompatActivity {


    TextView title, description;
    ImageView mainImageView;
    Button btnStart;

    private DatabaseReference artikelReference;
    private DatabaseReference hasilReference;

    private List<Artikel> artikelList = new ArrayList<>();

    private int current;

    private SharedPrefManager mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruksi);

        mainImageView = findViewById(R.id.MainImageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        btnStart = findViewById(R.id.btnStart);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        artikelReference = firebaseDatabase.getReference().child("artikel");
        hasilReference = firebaseDatabase.getReference().child("hasil");

        mSharedPreferences = new SharedPrefManager(this);

        getData();
        cekDataSiswa();
    }

    private void getData() {
        artikelReference.child(mSharedPreferences.getWacana()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSharedPreferences.setTotalArtikel((int)dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Artikel artikel = snapshot.getValue(Artikel.class);
                    artikelList.add(artikel);
                }
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadData() {
        current = mSharedPreferences.getCurrent();
//        Log.i("MYTAG", "current  : " + current);
        if (current < artikelList.size()) {
            Artikel artikel = artikelList.get(current);
            description.setText(artikel.getInstruksi());
            title.setText(mSharedPreferences.getWacana());
            Glide.with(getApplicationContext())
                    .load(mSharedPreferences.getThumbnail())
                    .into(mainImageView);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    loadData();
                    mSharedPreferences.setCurrent(current);
                    Intent intent = new Intent(InstruksiActivity.this, ArticleActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
//            tes++;
        } /*else {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(InstruksiActivity.this, ArticleActivity.class);
                    intent.putExtra("wacana", contoh);
                    startActivity(intent);
                }
            });
        }*/
    }

    private void cekDataSiswa() {
        hasilReference.child(mSharedPreferences.getUser().getUid()).child(mSharedPreferences.getWacana()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Nilai nilai = dataSnapshot.getValue(Nilai.class);
                    mSharedPreferences.setHighScore(nilai.getHighScore());
                    Toast.makeText(InstruksiActivity.this, "ada : " + nilai.getSkor(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InstruksiActivity.this, "gaada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
