package com.iceka.aksimobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextView SignUp, lupaPwd;
    private Button loginBtn;
    private EditText input_email, input_password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        login();
    }

    private void login() {
        SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_signup=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_signup);
            }
        });

        lupaPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent_pwd=new Intent(LoginActivity.this, ResetPasswordActivity.class);
//                startActivity(intent_pwd);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailUser = input_email.getText().toString().trim();
                final String passwordUser = input_password.getText().toString().trim();

                //validasi email dan password
                // jika email kosong
                if (emailUser.isEmpty()) {
                    input_email.setError("Email tidak boleh kosong");
                }
                // jika email not valid
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                    input_email.setError("Email tidak valid");
                }
                // jika password kosong
                else if (passwordUser.isEmpty()) {
                    input_password.setError("Password tidak boleh kosong");
                }
                //jika password kurang dari 6 karakter
                else if (passwordUser.length() < 6) {
                    input_password.setError("Password minimal terdiri dari 6 karakter");
                } else {
                    auth.signInWithEmailAndPassword(emailUser, passwordUser)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // ketika gagal locin maka akan do something
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "Gagal login karena " + task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", emailUser);
                                        bundle.putString("pass", passwordUser);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                                .putExtra("emailpass", bundle));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void initView() {
        SignUp = findViewById(R.id.link_signup);
        lupaPwd = findViewById(R.id.link_pwd);
        loginBtn = findViewById(R.id.btn_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        auth = FirebaseAuth.getInstance();
    }
}
