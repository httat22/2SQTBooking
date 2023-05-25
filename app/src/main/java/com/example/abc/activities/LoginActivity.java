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
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && mAuth.getCurrentUser().isEmailVerified()) {
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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String email, password;
        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        if (email.length() > 0 && password.length() > 0) {
            if (isRegistrationClickable) {
                    userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                UserModel userModel = childSnapshot.getValue(UserModel.class);
                                assert userModel != null;
                                String salt = userModel.getSalt();
                                String userId = userModel.getUserId();
                                String saltedPassword = password + salt;
                                String hashedPassword = hash(saltedPassword) + salt;
                                progressBar.setVisibility(View.VISIBLE);
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressBar.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    if (user != null) {
                                                        if (user.isEmailVerified()) {
                                                            userRef.child(userId).child("isLogin").setValue(true);
                                                            userRef.child(userId).child("lastFailedLoginTime")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            Long lastFailedLoginTime = snapshot.getValue(Long.class);
                                                                            if (lastFailedLoginTime != null && System.currentTimeMillis() - lastFailedLoginTime < 60 * 1000) {
                                                                                long timeDuration = (System.currentTimeMillis() - lastFailedLoginTime) / 60 / 1000 + 1;
                                                                                String s = "Wait " + timeDuration + " minutes to login";
                                                                                Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                userRef.child(user.getUid()).child("loginAttempts").setValue(0);
                                                                                Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                                                                startActivity(intent);
                                                                                finishAffinity();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                                        }
                                                                    });


                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                } else {
                                                    userRef.child(userId).child("lastFailedLoginTime")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    Long lastFailedLoginTime = snapshot.getValue(Long.class);
                                                                    if (lastFailedLoginTime != null && System.currentTimeMillis() - lastFailedLoginTime < 60 * 1000) {
                                                                        long timeDuration = (System.currentTimeMillis() - lastFailedLoginTime) / 60 / 1000 + 1;
                                                                        String s = "Wait " + timeDuration + " minutes to login";
                                                                        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        userRef.child(userId)
                                                                                .child("loginAttempts")
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        Integer loginAttempts = snapshot.getValue(Integer.class);
                                                                                        loginAttempts++;
                                                                                        Toast.makeText(getApplication(), String.valueOf(loginAttempts), Toast.LENGTH_SHORT).show();
                                                                                        userRef.child(userId).child("loginAttempts").setValue(loginAttempts);
//
                                                                                        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                                                                                            long currentTimeMillis = System.currentTimeMillis();
                                                                                            userRef.child(userId)
                                                                                                    .child("lastFailedLoginTime").setValue(currentTimeMillis);

                                                                                            userRef.child(userId).child("loginAttempts").setValue(0);
                                                                                            Toast.makeText(getApplication(), "You entered the wrong password beyond the allowed limit, please wait 10 minutes to continue.", Toast.LENGTH_SHORT).show();
                                                                                        } else {
                                                                                            // Hiển thị thông báo cho người dùng về số lần đăng nhập sai còn lại.
                                                                                            String message = "Email or password is incorrect! You have " + (MAX_LOGIN_ATTEMPTS - loginAttempts) + " more password attempts";
                                                                                            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                                    }
                                                                                });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
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
        llForgotPassword = findViewById(R.id.llForgotPassword);
        llSignUp = findViewById(R.id.llSignUp);
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