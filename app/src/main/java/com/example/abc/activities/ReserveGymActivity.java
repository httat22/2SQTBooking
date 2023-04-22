package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abc.R;

public class ReserveGymActivity extends AppCompatActivity {

    Button btnBookGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_gym);

        btnBookGym = findViewById(R.id.btn_bookGym);
        btnBookGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), GymPaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}