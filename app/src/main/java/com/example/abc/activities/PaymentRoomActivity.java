package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abc.MainActivity;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.RoomTypeModel;

public class PaymentRoomActivity extends AppCompatActivity {

    ImageView imageLogo, imageView;
    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_room);
        getSupportActionBar().hide();

        imageLogo = findViewById(R.id.imageLogo);
        imageView = findViewById(R.id.imageView);

        getDataFromConfirmBookRoom();
        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentRoomActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void getDataFromConfirmBookRoom() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomTypeModel");
        bookRoomModel = (BookRoomModel) bundle.get("object_bookRoomModel");
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageView);
    }
}