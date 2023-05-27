package com.example.abcd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abcd.R;
import com.example.abcd.models.TicketModel;
import com.example.abcd.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ManagerDetailReservedRoomActivity extends AppCompatActivity {
    private TextView tvRoomName, tvPrice, tvDatePayment, tvNameUser, tvIdUser, tvTimeBooked,
            tvNumberPerson, tvTotal, tvPriceMul,tvPriceResult;
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
        tvPriceMul = findViewById(R.id.tvPriceMul);
        tvPriceResult = findViewById(R.id.tvPriceResult);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerDetailReservedRoomActivity.super.onBackPressed();
            }
        });
        tvNameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetailUser();
            }
        });
        getDataFromBookRoomDetail();
    }

    private void onClickGotoDetailUser() {

        String path = ticketModel.getUserId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel != null) {
                    Intent intent = new Intent(getApplication(), ManagerDetailUserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object_userModel", userModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getDataFromBookRoomDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        ticketModel = (TicketModel) bundle.get("object_ticketModel");
        Glide.with(this).load(ticketModel.getImageURL()).into(imageRoom);
        String nameRoom = "Room " + ticketModel.getRoomId();

        int numOfDate = getNumberOfDate(ticketModel.getDateArrive(), ticketModel.getDateLeave());
        String stringPrice = "$" + ticketModel.getPrice()/numOfDate + "/night";

        String numberSingle = "2 Adults";
        String numberDouble = "4 Adults";

        String priceMul = "$" + ticketModel.getPrice()/numOfDate + " x " + numOfDate + " nights";
        String priceResult = "$" + ticketModel.getPrice();

        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = "Date of payment: " + sdf.format(date);

        String stringTime = ticketModel.getDateArrive() + " - " + ticketModel.getDateLeave();

        tvPriceMul.setText(priceMul);
        tvPriceResult.setText(priceResult);

        tvPrice.setText(stringPrice);
        tvRoomName.setText(nameRoom);
        tvNameUser.setText(ticketModel.getUserName());
        tvIdUser.setText(ticketModel.getUserId());
        tvTimeBooked.setText(stringTime);
        tvDatePayment.setText(formattedDate);
        tvTotal.setText(priceResult);
        if (ticketModel.getNumberPerson() == 2) {
            tvNumberPerson.setText(numberSingle);
        } else {
            tvNumberPerson.setText(numberDouble);
        }
    }
    public int getNumberOfDate(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

        return (int) differenceInDays;
    }
}