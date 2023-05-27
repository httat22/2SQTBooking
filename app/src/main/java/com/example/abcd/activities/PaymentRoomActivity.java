package com.example.abcd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abcd.MainActivity;
import com.example.abcd.R;
import com.example.abcd.models.BookRoomModel;
import com.example.abcd.models.RoomTypeModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentRoomActivity extends AppCompatActivity {

    private ImageView imageLogo, imageView;
    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;
    private ImageButton btnBack;

    private TextView tvNameRoom, tvPrice, tvRangeDate, tvPriceMul, tvPriceResult, tvPriceTotal, tvNumberPerson, tvDatePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_room);
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

        String nameRoom = "Room " + roomTypeModel.getRoom();
        tvNameRoom.setText(nameRoom);

        String numberSingle = "2 Adults";
        String numberDouble = "4 Adults";

        String stringPrice = "$" + roomTypeModel.getPrice() + "/night";
        String stringTime = bookRoomModel.getDateArrive() + " - " + bookRoomModel.getDateLeave();
        int numOfDate = getNumberOfDate(bookRoomModel.getDateArrive(), bookRoomModel.getDateLeave());
        String stringPriceMul = "$" + roomTypeModel.getPrice() + " x " + numOfDate + " /night";
        int priceResult = roomTypeModel.getPrice()*numOfDate;
        bookRoomModel.setTotalPayment(priceResult);
        String stringPriceResult = "$" + priceResult;


        tvPrice.setText(stringPrice);
        tvRangeDate.setText(stringTime);
        tvPriceMul.setText(stringPriceMul);
        tvPriceResult.setText(stringPriceResult);
        tvPriceTotal.setText(stringPriceResult);

        if (roomTypeModel.getRoomType().equals("single")) {
            tvNumberPerson.setText(numberSingle);
        } else {
            tvNumberPerson.setText(numberDouble);
        }


        long timeInMilliseconds = System.currentTimeMillis();
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