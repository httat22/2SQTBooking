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
import com.example.abc.models.RoomTypeModel;
import com.example.abc.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmBookRoomActivity extends AppCompatActivity {
    private Button btnConfirm;
    private ImageButton btnBack;
    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;
    private TextView tvTest;
    private ImageView imageView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_book_room);

        getSupportActionBar().hide();

        btnConfirm = findViewById(R.id.btn_confirmNow);
        btnBack = findViewById(R.id.btnBack);
        tvTest = findViewById(R.id.tvTest);
        imageView = findViewById(R.id.imageRoomConfirm);

        getDataFromBookRoomDetail();

        tvTest.setText(bookRoomModel.toString());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddRealtimeDatabase();
                Intent intent = new Intent(getApplication(), PaymentRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_roomTypeModel", roomTypeModel);
                bundle.putSerializable("object_bookRoomModel", bookRoomModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void onClickAddRealtimeDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId, nameType, time, imageURL, ticketId, description;
        int price, numberPerson;

        userId = user.getUid();
        nameType = "Booking Room";
        time = bookRoomModel.getDateArrive() + " - " + bookRoomModel.getDateLeave();
        imageURL = roomTypeModel.getImageURL();
        ticketId = "2SQT" + System.currentTimeMillis();
        price = bookRoomModel.getPrice();
        numberPerson = 2;
        description = "Room 301";
        TicketModel ticketModel = new TicketModel(userId, nameType, time, imageURL, price, numberPerson, ticketId, description, "checkIn");
        databaseReference.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromBookRoomDetail() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomTypeModel");
        bookRoomModel = (BookRoomModel) bundle.get("object_bookRoomModel");
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageView);
    }
}