package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.abc.MainActivity;
import com.example.abc.R;

public class ReserveGolfActivity extends AppCompatActivity {
    ImageButton btnBack, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_golf);

        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), MainActivity.class));
                finishAffinity();
            }
        });
    }
}