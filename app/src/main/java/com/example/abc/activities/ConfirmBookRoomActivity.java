package com.example.abc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.InfoRoomBookedModel;
import com.example.abc.models.ReserveUserModel;
import com.example.abc.models.RoomTypeModel;
import com.example.abc.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConfirmBookRoomActivity extends AppCompatActivity {
    private Button btnConfirm;
    private ImageButton btnBack;
    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;
    private ImageView imageView;
    private TextView tvNameRoom, tvPrice, tvRangeDate, tvPriceMul, tvPriceResult, tvPriceTotal, tvNumberPerson;
    private DatabaseReference databaseReference;
    private final DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
    private final DatabaseReference dateBookedRef = FirebaseDatabase.getInstance().getReference("statistics_room");
    private DatabaseReference listStayingRef = FirebaseDatabase.getInstance().getReference("list_staying");
    private DatabaseReference reserveUserRef = FirebaseDatabase.getInstance().getReference("reserving_user");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_book_room);

        getSupportActionBar().hide();

        btnConfirm = findViewById(R.id.btn_confirmNow);
        btnBack = findViewById(R.id.btnBack);
        imageView = findViewById(R.id.imageRoomConfirm);
        tvNameRoom = findViewById(R.id.tvNameRoom);
        tvPrice = findViewById(R.id.tvPrice);
        tvRangeDate = findViewById(R.id.tvRangeDate);
        tvPriceMul = findViewById(R.id.tvPriceMul);
        tvPriceResult = findViewById(R.id.tvPriceResult);
        tvPriceTotal = findViewById(R.id.tvPriceTotal);
        tvNumberPerson = findViewById(R.id.tvNumberPerson);

        getDataFromBookRoomDetail();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onClickAddRealtimeDatabase();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(getApplication(), PaymentRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_roomTypeModel", roomTypeModel);
                bundle.putSerializable("object_bookRoomModel", bookRoomModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void onClickAddRealtimeDatabase() throws ParseException {
        databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
        onClickAddTimeToRealTimeDatabase();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId, nameType, imageURL, ticketId, description, roomId, userName;
        int price, numberPerson;

        long currentTime = System.currentTimeMillis();

        userId = user.getUid();
        userName = user.getDisplayName();
        nameType = "Booking Room";
        imageURL = roomTypeModel.getImageURL();
        ticketId = "Room" + roomTypeModel.getRoomId() + System.currentTimeMillis();
        price = bookRoomModel.getTotalPayment();
        if (roomTypeModel.getRoomType().equals("single")) {
            numberPerson = 2;
        } else {
            numberPerson = 4;
        }
        description = roomTypeModel.getRoom();
        roomId = roomTypeModel.getRoomId();
        TicketModel ticketModel = new TicketModel(userId, nameType, bookRoomModel.getDateArrive(), bookRoomModel.getDateLeave(),
                imageURL, price, numberPerson, ticketId, description, "checkIn", roomId, currentTime, userName);
        databaseReference.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Booking successful!", Toast.LENGTH_SHORT).show();
            }
        });

        listStayingRef.child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Booking successful!", Toast.LENGTH_SHORT).show();
            }
        });

        InfoRoomBookedModel infoRoomBookedModel = new InfoRoomBookedModel(roomId, bookRoomModel.getDateArrive(),
                bookRoomModel.getDateLeave(), numberPerson, currentTime, userId);
        dateBookedRef.child(roomId).push().setValue(infoRoomBookedModel);

        ReserveUserModel reserveUserModel = new ReserveUserModel(userId, userName, user.getEmail(), "");
        reserveUserRef.child(userId).setValue(reserveUserModel);
    }

    private void onClickAddTimeToRealTimeDatabase() {
        String s1 = (String) bookRoomModel.getDateArrive();
        String s2 = (String) bookRoomModel.getDateLeave();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(s1);
            endDate = sdf.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<String> list = new ArrayList<>();

        while (calendar.getTime().before(endDate)) {
            Date currentDate = calendar.getTime();
            String dateString = sdf.format(currentDate);
            list.add(dateString);
            calendar.add(Calendar.DATE, 1);
        }
        list.add(sdf.format(endDate));
        String pathParent = String.valueOf(roomTypeModel.getRoomType());
        String path = String.valueOf(roomTypeModel.getRoomId());
        for (String s : list) {
            timeRef.child(pathParent).child(path).push().setValue(s);
        }
    }

    private void getDataFromBookRoomDetail() {
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