package com.iceka.aksimobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceka.aksimobile.adapters.WacanaAdapter;
import com.iceka.aksimobile.models.User;
import com.iceka.aksimobile.models.Wacana;
import com.iceka.aksimobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private DatabaseReference userReference;
    private DatabaseReference wacanaReference;

    private TextView welcomeUser;
    private RecyclerView recyclerView;

    List<Wacana> wacanaList = new ArrayList<>();

    private SharedPrefManager mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeUser = findViewById(R.id.wlcmsg_txt);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_main);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.tvHeaderName);
        TextView sekolah = headerView.findViewById(R.id.tvHeaderAsalSekolah);

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
//        navigationView.setNavigationItemSelectedListener(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("users");
        wacanaReference = firebaseDatabase.getReference().child("wacana");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();

        mSharedPreferences = new SharedPrefManager(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        username.setText(mSharedPreferences.getUser().getUsername());
        sekolah.setText(mSharedPreferences.getUser().getSekolah());

        mSharedPreferences.clearReference();

        Log.i("MYTAG", "OnCreate");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MYTAG", "OnRestart");
        mSharedPreferences.clearReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            getData();
        }
    }

    private void getData() {
        userReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                welcomeUser.setText("Hi, " + user.getUsername());
                mSharedPreferences.userInput(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        wacanaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wacanaList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Wacana wacana  = snapshot.getValue(Wacana.class);
                    wacanaList.add(wacana);
                    WacanaAdapter adapter = new WacanaAdapter(getApplicationContext(), wacanaList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }


    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


}
