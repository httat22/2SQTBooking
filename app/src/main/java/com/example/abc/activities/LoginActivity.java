package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abc.MainActivity;
import com.example.abc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignIn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView tvForgotPassword;
    LinearLayout linearLayoutRegistration;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null && mAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        initUI();
        linearLayoutRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSingIn();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickSingIn() {
        String email, password;
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "You need enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "You need enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "Sing in Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign in Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        btnSignIn = findViewById(R.id.btn_SignIn);
        progressBar = findViewById(R.id.progressBar);
        linearLayoutRegistration = findViewById(R.id.layout_registration);
        tvForgotPassword = findViewById(R.id.tv_forgotPassword);
    }
}