package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.RoomTypeModel;
import com.example.abc.models.TicketModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerDetailReservedRoomActivity extends AppCompatActivity {
    private TextView tvRoomName, tvPrice, tvDatePayment, tvNameUser, tvIdUser, tvTimeBooked, tvNumberPerson, tvTotal;
    private ImageView btnBack, imageRoom;
    private TicketModel ticketModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_payment_room_manager);
        getSupportActionBar().hide();

        tvRoomName = findViewById(R.id.tvRoomName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDatePayment = findViewById(R.id.tvDatePayment);
        tvNameUser = findViewById(R.id.tvNameUser);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvTimeBooked = findViewById(R.id.tvTimeBooked);
        tvNumberPerson = findViewById(R.id.tvNumberPerson);
        tvTotal = findViewById(R.id.tvTotal);
        btnBack = findViewById(R.id.btnBack);
        imageRoom = findViewById(R.id.imageRoom);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerDetailReservedRoomActivity.super.onBackPressed();
            }
        });
        getDataFromBookRoomDetail();
    }

    private void getDataFromBookRoomDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        ticketModel = (TicketModel) bundle.get("object_ticketModel");

        Glide.with(this).load(ticketModel.getImageURL()).into(imageRoom);
        String nameRoom = "Room " + ticketModel.getRoomId();


        String numberSingle = "2 Adults";
        String numberDouble = "4 Adults";

        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = "Date of payment: " + sdf.format(date);

        String stringPrice = "$" + ticketModel.getPrice();
        String stringTime = ticketModel.getDateArrive() + " - " + ticketModel.getDateLeave();

        tvRoomName.setText(nameRoom);
        tvNameUser.setText(ticketModel.getUserName());
        tvIdUser.setText(ticketModel.getUserId());
        tvTimeBooked.setText(stringTime);
        tvDatePayment.setText(formattedDate);
        tvTotal.setText(stringPrice);
        if (ticketModel.getNumberPerson() == 2) {
            tvNumberPerson.setText(numberSingle);
        } else {
            tvNumberPerson.setText(numberDouble);
        }
    }
}