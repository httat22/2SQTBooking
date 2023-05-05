package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abc.R;
import com.example.abc.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtEmail, edtName, edtPassword, edtConfirmPassword, edtPhone, edtAddress;
    Button btnRegistration;
    ProgressBar progressBar;
    LinearLayout linearLayoutSignIn;
    FirebaseAuth mAuth;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        initUI();

        linearLayoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoSignIn();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegistration();
            }
        });
    }

    private void onClickGotoSignIn() {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    private void onClickRegistration() {
        String name, email, password, confirmPassword, phone, address;

        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        address = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegistrationActivity.this, "You need enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegistrationActivity.this, "You need enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegistrationActivity.this, "You need enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "You need enter confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Confirm password is false", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegistrationActivity.this, "You need enter phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(RegistrationActivity.this, "You need enter address", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser() != null) {

                                FirebaseUser user = mAuth.getCurrentUser();
//Lưu thông tin người dùng trên realtime database
                                usersReference = FirebaseDatabase.getInstance().getReference("users");
                                String userId = user.getUid();
                                UserModel userModel = new UserModel(userId, name, email, phone, address);
                                usersReference.child(userId).setValue(userModel);

                                user.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegistrationActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegistrationActivity.this, "on Failure: Email not sent", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Register Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email);
        edtName = findViewById(R.id.edt_name);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        btnRegistration = findViewById(R.id.btn_registration);
        progressBar = findViewById(R.id.progressBar);
        linearLayoutSignIn = findViewById(R.id.layout_sign_in);
    }
}