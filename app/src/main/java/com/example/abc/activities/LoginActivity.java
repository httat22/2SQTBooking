package com.example.abc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.abc.MainActivity;
import com.example.abc.ManagerActivity;
import com.example.abc.R;
import com.example.abc.fragmentManager.AccountFragment;
import com.example.abc.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private CardView btnSignIn;
    private TextView tvEmailError, tvPasswordError;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private boolean isRegistrationClickable = false;
    private LinearLayout llForgotPassword, llSignUp;
    private final int MAX_LOGIN_ATTEMPTS = 5;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        initUI();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSingIn();
            }
        });

        inputChange();
    }

    private void onClickSingIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String email, password;
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (email.length() > 0 && password.length() > 0) {
            if (isRegistrationClickable) {
                if ((email.equals("admin1@gmail.com") && password.equals("admin1@com")) ||
                        (email.equals("admin2@gmail.com") && password.equals("admin2@com")) ||
                        (email.equals("admin3@gmail.com") && password.equals("admin3@com"))) {
                    Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(getApplication(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                }
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
        if (email.length() > 0 && password.length() > 0) {
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
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return new String(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}