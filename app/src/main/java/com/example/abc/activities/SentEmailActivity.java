package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.abc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SentEmailActivity extends AppCompatActivity {
    private LinearLayout llSignIn, llResentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_email);
        getSupportActionBar().hide();

        llSignIn = findViewById(R.id.llSignIn);
        llResentEmail = findViewById(R.id.llResentEmail);

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentEmailActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        llResentEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SentEmailActivity.this, "Email has be sent!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}