package com.example.abc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.abc.MainActivity;
import com.example.abc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private CardView btnSignIn;
    private TextView tvEmailError, tvPasswordError;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private boolean isRegistrationClickable = false;
    private LinearLayout llForgotPassword, llSignUp;

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
        llSignUp.setOnClickListener(new View.OnClickListener() {
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

        llForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        inputChange();
    }

    private void onClickSingIn() {
        String email, password;
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (email.length() > 0 && password.length() > 0) {
            if (isRegistrationClickable) {
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
        } else {
            if (email.length() == 0) {
                tvEmailError.setVisibility(View.VISIBLE);
            }
            if (password.length() == 0) {
                tvPasswordError.setVisibility(View.VISIBLE);
            }
        }


    }

    @SuppressLint("ResourceType")
    private void checkEmptyField() {
        String email, password;
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (email.length() > 0 && tvEmailError.getVisibility() == View.VISIBLE) {
            tvEmailError.setVisibility(View.GONE);
        }
        if (password.length() > 0 && tvPasswordError.getVisibility() == View.VISIBLE) {
            tvPasswordError.setVisibility(View.GONE);
        }
        if ( email.length() > 0 && password.length() > 0) {
            isRegistrationClickable = true;
            btnSignIn.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isRegistrationClickable = false;
            btnSignIn.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }
    private void inputChange() {
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEmptyField();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEmptyField();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressBar = findViewById(R.id.progressBar);
        llForgotPassword = findViewById(R.id.llForgotPassword);
        llSignUp = findViewById(R.id.llSignUp);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
    }
}