package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.example.abc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private TextView tvEmailError;
    private CardView btnSignIn;
    private ProgressBar progressBar;
    private LinearLayout llSignIn;
    private boolean isRegistrationClickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        edtEmail = findViewById(R.id.edtEmail);
        tvEmailError = findViewById(R.id.tvEmailError);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressBar = findViewById(R.id.progressBar);
        llSignIn = findViewById(R.id.llSignIn);

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String email = edtEmail.getText().toString().trim();

                if (email.length() > 0) {
                    if (isRegistrationClickable) {
                        progressBar.setVisibility(View.VISIBLE);
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(ForgotPasswordActivity.this, SentEmailActivity.class);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                } else {
                    if (email.length() == 0) {
                        tvEmailError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        inputChange();
    }
    @SuppressLint("ResourceType")
    private void checkEmptyField() {
        String email;
        email = String.valueOf(edtEmail.getText());

        if (email.length() > 0 && tvEmailError.getVisibility() == View.VISIBLE) {
            tvEmailError.setVisibility(View.GONE);
        }
        if ( email.length() > 0 ) {
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
    }
}