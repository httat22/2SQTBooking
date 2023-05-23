package com.example.abc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.MainActivity;
import com.example.abc.R;
import com.example.abc.models.BookServiceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PaymentGolfActivity extends AppCompatActivity {
    private BookServiceModel bookServiceModel;

    private ImageView imgBackHome;
    private ImageButton btnBack;
    private TextView tvNameTicket, tvPrice, tvRangeDate, tvPriceMul, tvPriceResult,tvPriceTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_golf);
        getSupportActionBar().hide();

        Locale locale = getResources().getConfiguration().locale;
        Log.e("country", locale.getCountry());

        imgBackHome = findViewById(R.id.imgBackHome);
        tvNameTicket = findViewById(R.id.tvNameTicket);
        tvPrice = findViewById(R.id.tvPrice);
        tvRangeDate = findViewById(R.id.tvRangeDate);
        tvPriceMul = findViewById(R.id.tvPriceMul);
        tvPriceResult = findViewById(R.id.tvPriceResult);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        btnBack = findViewById(R.id.btnBack);

        getDataFromReserveGymActivity();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentGolfActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
    private void getDataFromReserveGymActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        bookServiceModel = (BookServiceModel) bundle.get("object_bookServiceModel");


        String stringPrice = "$" + bookServiceModel.getPrice() + "/day";
        String stringTime = bookServiceModel.getDateArrive() + " - " + bookServiceModel.getDateLeave();
        int numOfDate = getNumberOfDate(bookServiceModel.getDateArrive(), bookServiceModel.getDateLeave());
        String stringPriceMul = "$" + bookServiceModel.getPrice() + " x " + numOfDate + " day x " + bookServiceModel.getNumberPerson() + " people";

        int priceResult = bookServiceModel.getPrice() * numOfDate * bookServiceModel.getNumberPerson();
        String stringPriceResult = "$" + priceResult;

        tvNameTicket.setText("Resort Golf Courses Ticket");
        tvPrice.setText(stringPrice);
        tvRangeDate.setText(stringTime);
        tvPriceMul.setText(stringPriceMul);
        tvPriceResult.setText(stringPriceResult);
        tvPriceTotal.setText(stringPriceResult);
    }
    private int getNumberOfDate(String start, String end) {
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