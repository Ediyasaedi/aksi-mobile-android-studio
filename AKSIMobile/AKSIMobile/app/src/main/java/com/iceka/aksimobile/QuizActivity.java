package com.iceka.aksimobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.models.Soal;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView mScoreView, mQuestionView;
    private Button mBtnChoice1, mBtnChoice2, mBtnChoice3, mBtnChoice4;

    private DatabaseReference soalReference;

    private List<Soal> soalList = new ArrayList<>();

    private String mAnswer;
    private int mScore;
    private int mQuestionNumber = 0;
    private String artikelId;
    private String wacana;
    private int total;
    private int current;

    private int score = 0;
    private int currentHighScore;

    private SharedPrefManager mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mScoreView = findViewById(R.id.tV_skor);
        mQuestionView = findViewById(R.id.tV_questions);
        mBtnChoice1 = findViewById(R.id.btnChoice1);
        mBtnChoice2 = findViewById(R.id.btnChoice2);
        mBtnChoice3 = findViewById(R.id.btnChoice3);
        mBtnChoice4 = findViewById(R.id.btnChoice4);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        soalReference = firebaseDatabase.getReference().child("soal");

        mSharedPreferences = new SharedPrefManager(this);

        artikelId = getIntent().getStringExtra("artikelId");
        wacana = getIntent().getStringExtra("wacana");

        total = mSharedPreferences.getTotalArtikel();
        current = mSharedPreferences.getCurrent() + 1;

        mScore = mSharedPreferences.getScore();
        Log.i("MYTAG", "mscore : " + mScore);

        getData();
    }

    private void getData() {
        soalReference.child(mSharedPreferences.getWacana()).child(mSharedPreferences.getArtikelId()).child("soal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Soal soal = snapshot.getValue(Soal.class);
                    soalList.add(soal);
                }
                updateQuestion();
                updateScore();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateQuestion() {
        if (mQuestionNumber < soalList.size()) {
            Soal soal1 = soalList.get(mQuestionNumber);
            mQuestionView.setText(soal1.getPertanyaan());
            mBtnChoice1.setText(soal1.getOption1());
            mBtnChoice2.setText(soal1.getOption2());
            mBtnChoice3.setText(soal1.getOption3());
            mBtnChoice4.setText(soal1.getOption4());
            mAnswer = soal1.getJawaban();
            mQuestionNumber++;
        } else {
            if (current < total) {
                mSharedPreferences.setCurrent(current);
                mSharedPreferences.setScore(score);
                startActivity(new Intent(getApplicationContext(), InstruksiActivity.class));
                Log.i("MYTAG", "Hasil sementara : " + score);
            } else {
                mSharedPreferences.setScore(mScore + score);
                if (mSharedPreferences.getHighScore() < (mScore + score) * 5) {
                    mSharedPreferences.setHighScore((mScore + score) * 5);
                }
                Log.i("MYTAG", "hasil : " + mSharedPreferences.getScore());
                startActivity(new Intent(getApplicationContext(), SkorResultActivity.class));
//                Toast.makeText(this, "Quiz selesai menuju skor activity", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateScore() {
        mScoreView.setText("Skor saat ini: " + score + "/" + soalList.size());
    }

    public void onClick(View view) {
        Button answer = (Button) view;
        if (answer.getText().toString().equals(mAnswer)) {
            score = score + 1;
            Toast.makeText(QuizActivity.this, "Benar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(QuizActivity.this, "Salah", Toast.LENGTH_SHORT).show();
        }
        updateScore();
        updateQuestion();
    }
}
