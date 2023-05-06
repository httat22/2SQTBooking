package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.abc.MainActivity;
import com.example.abc.R;
import com.example.abc.models.BookServiceModel;

public class PaymentTennisActivity extends AppCompatActivity {

    private BookServiceModel bookServiceModel;

    private ImageView imgBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_tennis);

        getSupportActionBar().hide();

        getDataFromReserveGymActivity();

        imgBackHome = findViewById(R.id.imgBackHome);

        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentTennisActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void getDataFromReserveGymActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        bookServiceModel = (BookServiceModel) bundle.get("object_bookServiceModel");
    }
}