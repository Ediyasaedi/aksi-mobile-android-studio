package com.iceka.aksimobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.adapters.SkorSiswaAdapter;
import com.iceka.aksimobile.models.Nilai;
import com.iceka.aksimobile.models.Wacana;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mUsername;
    private TextView mAsalSekolah;
    private ImageView mGotoBeranda;

    private List<Wacana> wacanaList = new ArrayList<>();
    private List<Nilai> nilaiList = new ArrayList<>();
    private SharedPrefManager mSharedPreferences;

    private DatabaseReference wacanaReference;
    private DatabaseReference hasilReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_siswa);

        mRecyclerView = findViewById(R.id.rv_skorCollection);
        mUsername = findViewById(R.id.tvUsername);
        mAsalSekolah = findViewById(R.id.tvAsalSekolah);
        mGotoBeranda = findViewById(R.id.gotoberanda);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        wacanaReference = firebaseDatabase.getReference().child("wacana");
        hasilReference = firebaseDatabase.getReference().child("hasil");

        mSharedPreferences = new SharedPrefManager(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mUsername.setText(mSharedPreferences.getUser().getUsername());
        mAsalSekolah.setText(mSharedPreferences.getUser().getSekolah());

        getData();
        gotoberanda();
    }

    private void gotoberanda() {
        mGotoBeranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        wacanaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Wacana wacana = snapshot.getValue(Wacana.class);
                    wacanaList.add(wacana);

                    hasilReference.child(mSharedPreferences.getUser().getUid()).child(wacana.getJudul()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Nilai nilai = snapshot.getValue(Nilai.class);
                                nilaiList.add(nilai);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    SkorSiswaAdapter adapter = new SkorSiswaAdapter(getApplicationContext(), wacanaList);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onBackPressed(){
        Intent gotoHome = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(gotoHome);
        finish();
    }
}
