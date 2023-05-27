package com.example.abcd.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abcd.MainActivity;
import com.example.abcd.R;
import com.example.abcd.models.BookServiceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentTennisActivity extends AppCompatActivity {

    private BookServiceModel bookServiceModel;

    private ImageView imgBackHome;
    private ImageButton btnBack;
    private TextView tvNameTicket, tvPrice, tvRangeDate, tvPriceMul, tvPriceResult,tvPriceTotal, tvDatePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_tennis);

        getSupportActionBar().hide();

        imgBackHome = findViewById(R.id.imgBackHome);
        tvNameTicket = findViewById(R.id.tvNameTicket);
        tvPrice = findViewById(R.id.tvPrice);
        tvRangeDate = findViewById(R.id.tvRangeDate);
        tvPriceMul = findViewById(R.id.tvPriceMul);
        tvPriceResult = findViewById(R.id.tvPriceResult);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        btnBack = findViewById(R.id.btnBack);
        tvDatePayment = findViewById(R.id.tvDatePayment);

        getDataFromReserveGymActivity();
        btnBack.setOnClickListener(new View.OnClickListener() {
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
        String stringPrice = "$" + bookServiceModel.getPrice() + "/day";
        String stringTime = bookServiceModel.getDateArrive() + " - " + bookServiceModel.getDateLeave();
        int numOfDate = getNumberOfDate(bookServiceModel.getDateArrive(), bookServiceModel.getDateLeave());
        String stringPriceMul = "$" + bookServiceModel.getPrice() + " x " + numOfDate + " day x " + bookServiceModel.getNumberPerson() + " people";

        int priceResult = bookServiceModel.getPrice() * numOfDate * bookServiceModel.getNumberPerson();
        String stringPriceResult = "$" + priceResult;

        tvNameTicket.setText("Tennis Courses Club Ticket");
        tvPrice.setText(stringPrice);
        tvRangeDate.setText(stringTime);
        tvPriceMul.setText(stringPriceMul);
        tvPriceResult.setText(stringPriceResult);
        tvPriceTotal.setText(stringPriceResult);
        long timeInMilliseconds = System.currentTimeMillis();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = "Date of payment: " + sdf.format(date);
        tvDatePayment.setText(formattedDate);

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