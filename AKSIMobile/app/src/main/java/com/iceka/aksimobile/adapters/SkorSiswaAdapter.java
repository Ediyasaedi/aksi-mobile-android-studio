package com.iceka.aksimobile.adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.R;
import com.iceka.aksimobile.models.Nilai;
import com.iceka.aksimobile.models.Wacana;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.List;

public class SkorSiswaAdapter extends RecyclerView.Adapter<SkorSiswaAdapter.MyViewHolder> {

    private List<Wacana> wacanaList;
    private List<Nilai> nilaiList;
    private Context mContext;
    private SharedPrefManager mSharedPreferences;

    public SkorSiswaAdapter(Context context, List<Wacana> wacanas) {
        this.mContext = context;
        this.wacanaList = wacanas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_skor_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        mSharedPreferences = new SharedPrefManager(mContext);

        Wacana wacana = wacanaList.get(position);
//        Nilai nilai = nilaiList.get(position);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference hasilReference = firebaseDatabase.getReference().child("hasil");
        hasilReference.child(mSharedPreferences.getUser().getUid()).child(wacana.getJudul()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Nilai nilai = dataSnapshot.getValue(Nilai.class);
                    Log.i("MYTAG", "nilai : " + nilai.getSkor());
                    holder.tvScore.setText(String.valueOf(nilai.getSkor()));
                } else {
                        holder.tvScore.setText(String.valueOf(0));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Log.i("MYTAG", "test : " + mSharedPreferences.getUser().getUid());
        Log.i("MYTAG", "wacananya : " + wacana.getJudul());


        holder.tvWacana.setText(wacana.getJudul());
//        if (nilaiList.size() > wacanaList.size()) {
//        holder.tvScore.setText(String.valueOf(0));
//        } else {
//            holder.tvScore.setText(String.valueOf(nilai.getSkor()));
//        }
    }

    @Override
    public int getItemCount() {
        return wacanaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWacana;
        private TextView tvScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWacana = itemView.findViewById(R.id.tv_wacana);
            tvScore = itemView.findViewById(R.id.tvskorhasil);
        }
    }
}
