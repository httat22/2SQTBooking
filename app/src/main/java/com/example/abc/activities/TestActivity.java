package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.abc.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_user);
        getSupportActionBar().hide();
    }
}