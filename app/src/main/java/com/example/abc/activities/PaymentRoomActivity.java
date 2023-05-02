package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abc.MainActivity;
import com.example.abc.R;

public class PaymentRoomActivity extends AppCompatActivity {

    ImageView imageLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_room);
        getSupportActionBar().hide();

        imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentRoomActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}