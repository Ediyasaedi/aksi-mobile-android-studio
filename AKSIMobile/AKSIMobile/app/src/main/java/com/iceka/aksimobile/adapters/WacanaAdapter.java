package com.iceka.aksimobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iceka.aksimobile.InstruksiActivity;
import com.iceka.aksimobile.R;
import com.iceka.aksimobile.models.Wacana;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.List;

public class WacanaAdapter extends RecyclerView.Adapter<WacanaAdapter.MyViewHolder> {

    private List<Wacana> wacanaList;
    private Context mContext;
    private SharedPrefManager mSharedPreferences;

    public WacanaAdapter(Context context, List<Wacana> wacanas) {
        this.mContext = context;
        this.wacanaList = wacanas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wacana, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mSharedPreferences = new SharedPrefManager(mContext);
        final Wacana wacana = wacanaList.get(position);

        Glide.with(mContext)
                .load(wacana.getGambarUrl())
                .into(holder.thumbnail);
        holder.judul.setText(wacana.getJudul());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences.setWacana(wacana.getJudul());
                mSharedPreferences.setThumbnail(wacana.getGambarUrl());
//                Log.i("MYTAG", "pref in adapter : " + wacana.getJudul() +mSharedPreferences.getWacana());
                Intent intent = new Intent(mContext, InstruksiActivity.class);
//                intent.putExtra("wacana", wacana.getJudul());
//                intent.putExtra("thumbnail", wacana.getGambarUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wacanaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView judul;
        private CardView cardView;
        private LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_wacana);
            judul = itemView.findViewById(R.id.tv_judul_wacana);
            cardView = itemView.findViewById(R.id.card_view);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
