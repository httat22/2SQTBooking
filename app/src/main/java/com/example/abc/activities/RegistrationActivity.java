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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

    private EditText edtEmail, edtName, edtPassword, edtConfirmPassword, edtPhone, edtAddress;
    private TextView tvNameError, tvEmailError, tvPhoneError, tvAddressError, tvPasswordError, tvConfirmError, tvConfirmEqualError;
    private CardView cardOne, cardTwo, cardThree, cardFour, btnRegister;
    private boolean isAtLeast9 = false, hasUpperCase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false;
    ProgressBar progressBar;
    LinearLayout llSignIn;
    FirebaseAuth mAuth;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        initUI();

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegistration();
            }
        });
        inputChange();
    }


    private void onClickRegistration() {
        String name, email, password, confirmPassword, phone, address;

        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        address = edtAddress.getText().toString().trim();

        if (name.length() > 0 && email.length() > 0 && password.length() > 0 && confirmPassword.length() > 0 &&
                phone.length() > 0 && address.length() > 0 && password.equals(confirmPassword)) {
            if (isRegistrationClickable) {
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
        } else {
            if (name.length() == 0) {
                tvNameError.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(email)) {
                tvEmailError.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(phone)) {
                tvPhoneError.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(address)) {
                tvAddressError.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(password)) {
                tvPasswordError.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                tvConfirmError.setVisibility(View.VISIBLE);
            }
            if (!password.equals(confirmPassword)) {
                tvConfirmEqualError.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkEmptyField(String name, String email, String phone, String address, String password, String confirmPassword) {
        if (name.length() > 0 && tvNameError.getVisibility() == View.VISIBLE) {
            tvNameError.setVisibility(View.GONE);
        }
        if (email.length() > 0 && tvEmailError.getVisibility() == View.VISIBLE) {
            tvEmailError.setVisibility(View.GONE);
        }
        if (phone.length() > 0 && tvPhoneError.getVisibility() == View.VISIBLE) {
            tvPhoneError.setVisibility(View.GONE);
        }
        if (address.length() > 0 && tvAddressError.getVisibility() == View.VISIBLE) {
            tvAddressError.setVisibility(View.GONE);
        }
        if (password.length() > 0 && tvPasswordError.getVisibility() == View.VISIBLE) {
            tvPasswordError.setVisibility(View.GONE);
        }
        if (confirmPassword.length() > 0 && tvConfirmError.getVisibility() == View.VISIBLE) {
            tvConfirmError.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void passwordCheck() {
        String name, email, password, confirmPassword, phone, address;

        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        confirmPassword = edtConfirmPassword.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        address = edtAddress.getText().toString().trim();

        checkEmptyField(name, email, phone, address, password, confirmPassword);

        if (password.length() >= 9) {
            isAtLeast9 = true;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isAtLeast9 = false;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        if (password.matches("(.*[A-Z].*)")) {
            hasUpperCase = true;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUpperCase = false;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasNumber = false;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        if (password.matches("^(?=.*[_.()@]).*$")) {
            hasSymbol = true;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasSymbol = false;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        checkAllData(name, email, phone, address, password, confirmPassword);
    }

    @SuppressLint("ResourceType")
    private void checkAllData(String name, String email, String phone, String address, String password, String confirmPassword) {
        if (isAtLeast9 && hasUpperCase && hasNumber && hasSymbol && email.length() > 0 && name.length() > 0 &&
                password.length() > 0 && confirmPassword.length() > 0 && phone.length() > 0 && address.length() > 0) {
            isRegistrationClickable = true;
            btnRegister.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isRegistrationClickable = false;
            btnRegister.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }

    private void inputChange() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
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
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        btnRegister = findViewById(R.id.btnRegister);
        llSignIn = findViewById(R.id.llSignIn);
        progressBar = findViewById(R.id.progressBar);

        tvNameError = findViewById(R.id.tvNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPhoneError = findViewById(R.id.tvPhoneError);
        tvAddressError = findViewById(R.id.tvAddressError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        tvConfirmError = findViewById(R.id.tvConfirmError);
        tvConfirmEqualError = findViewById(R.id.tvConfirmEqualError);

        cardOne = findViewById(R.id.cardOne);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        cardFour = findViewById(R.id.cardFour);
    }
}