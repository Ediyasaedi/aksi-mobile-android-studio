package com.iceka.aksimobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceka.aksimobile.models.User;
import com.iceka.aksimobile.utils.SharedPrefManager;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginTxt;
    private FirebaseAuth auth;
    private DatabaseReference userReference;
    private FirebaseUser firebaseUser;
    private EditText input_nama, input_email, input_sekolah, input_password, input_cfmpassword;
    private Button btn_register;

    private SharedPrefManager mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("users");

        mSharedPreferences = new SharedPrefManager(this);

        initView();

        registerUser();

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_signin=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_signin);
            }
        });
    }

    private void registerUser() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampung imputan user
                String emailUser = input_email.getText().toString().trim();
                String passwordUser = input_password.getText().toString().trim();
                String cpmpassUser = input_cfmpassword.getText().toString().trim();

                //validasi email dan password
                // jika email kosong
                if (emailUser.isEmpty()){
                    input_email.setError("Email tidak boleh kosong");
                }
                // jika email not valid
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    input_email.setError("Email tidak valid");
                }
                // jika password kosong
                else if (passwordUser.isEmpty()){
                    input_password.setError("Password tidak boleh kosong");
                }
                //jika password kurang dari 6 karakter
                else if (passwordUser.length() < 6){
                    input_password.setError("Password minimal terdiri dari 6 karakter");
                }
                else if (!passwordUser.equals(cpmpassUser)){
                    input_cfmpassword.setError("Password salah");
                }
                else {
                    //create user dengan firebase auth
                    auth.createUserWithEmailAndPassword(emailUser,passwordUser)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //jika gagal register do something
                                    if (!task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,
                                                "Register gagal karena "+ task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }else {
                                        //jika sukses akan menuju ke login activity
                                        Toast.makeText(RegisterActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        firebaseUser = auth.getCurrentUser();
                                        createUserData();
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        loginTxt = findViewById(R.id.link_signin);
        input_nama = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_sekolah = findViewById(R.id.input_sekolah);
        input_password = findViewById(R.id.input_password);
        input_cfmpassword = findViewById(R.id.input_cfmpassword);
        btn_register = findViewById(R.id.btn_register);
        auth = FirebaseAuth.getInstance();
    }

    private void createUserData() {

        User user = new User(firebaseUser.getUid(), input_nama.getText().toString(), firebaseUser.getEmail(), input_sekolah.getText().toString());
        mSharedPreferences.userInput(user);
        userReference.child(firebaseUser.getUid()).setValue(user);

    }
}
