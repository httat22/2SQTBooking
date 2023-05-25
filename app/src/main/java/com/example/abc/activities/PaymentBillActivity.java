package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abc.MainActivity;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.RoomTypeModel;
import com.example.abc.models.TicketModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentBillActivity extends AppCompatActivity {

    private ImageView imageLogo, imageView;
    private TicketModel ticketModel;
    private ImageButton btnBack;

    private TextView tvNameRoom, tvPrice, tvRangeDate, tvPriceMul, tvPriceResult, tvPriceTotal, tvNumberPerson, tvDatePayment;

    public PaymentBillActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bill);
        getSupportActionBar().hide();

        imageLogo = findViewById(R.id.imageLogo);
        imageView = findViewById(R.id.imageView);
        tvNameRoom = findViewById(R.id.tvNameRoom);
        tvPrice = findViewById(R.id.tvPrice);
        tvRangeDate = findViewById(R.id.tvRangeDate);
        tvPriceMul = findViewById(R.id.tvPriceMul);
        tvPriceResult = findViewById(R.id.tvPriceResult);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        tvNumberPerson = findViewById(R.id.tvNumberPerson);
        btnBack = findViewById(R.id.btnBack);
        tvDatePayment = findViewById(R.id.tvDatePayment);

        getDataFromConfirmBookRoom();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentBillActivity.super.onBackPressed();
            }
        });

    }

    private void getDataFromConfirmBookRoom() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        ticketModel = (TicketModel) bundle.get("object_ticketModel");
        Glide.with(this).load(ticketModel.getImageURL()).into(imageView);

        String nameRoom;
        String numberPerson;

        int numOfDate = getNumberOfDate(ticketModel.getDateArrive(), ticketModel.getDateLeave());

        String stringPrice;
        String stringTime = ticketModel.getDateArrive() + " - " + ticketModel.getDateLeave();

        String stringPriceMul;
        if (ticketModel.getNameType().equals("Booking Room")) {
            nameRoom = "Room " + ticketModel.getDescription();
            numberPerson = ticketModel.getNumberPerson() + " Adults";
            stringPrice = "$" + ticketModel.getPrice()/numOfDate + "/night";
            stringPriceMul = "$" + ticketModel.getPrice()/numOfDate + " x " + numOfDate + " nights";
        } else {
            nameRoom = "Ticket " + ticketModel.getNameType();
            numberPerson = "1 Adult";
            stringPrice = "$" + ticketModel.getPrice()/numOfDate + "/day";
            stringPriceMul = "$" + ticketModel.getPrice()/numOfDate + " x " + numOfDate + " days x 1 people";
        }

        tvNameRoom.setText(nameRoom);
        tvNumberPerson.setText(numberPerson);
        String stringPriceResult = "$" + ticketModel.getPrice();

        tvPrice.setText(stringPrice);
        tvRangeDate.setText(stringTime);
        tvPriceMul.setText(stringPriceMul);
        tvPriceResult.setText(stringPriceResult);
        tvPriceTotal.setText(stringPriceResult);


        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = "Date of payment: " + sdf.format(date);
        tvDatePayment.setText(formattedDate);
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