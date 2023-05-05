package com.example.abc.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.RoomTypeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookRoomDetailActivity extends AppCompatActivity{
    private Button btnBookNow, btnCheckIn, btnCheckOut;
    private ImageButton btnBack;
    private ImageView imageRoom;

    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;
    private String dateArrive, dateLeave;
    public BookRoomDetailActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room_detail);

        getSupportActionBar().hide();

        initUI();

        getObjectRoomModel();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getApplication(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                pushBookRoomModelToConfirmBookRoomActivity();
                Intent intent = new Intent(BookRoomDetailActivity.this, ConfirmBookRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_roomTypeModel", roomTypeModel);
                bundle.putSerializable("object_bookRoomModel", bookRoomModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCheckInDate_click();
            }
        });
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCheckOutDate_click();
            }
        });
    }

    private void pushBookRoomModelToConfirmBookRoomActivity() {
        bookRoomModel = new BookRoomModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        bookRoomModel.setUserId(user.getUid());
        bookRoomModel.setRoomId(roomTypeModel.getId());
        bookRoomModel.setPrice(Integer.parseInt(roomTypeModel.getPrice()));
        bookRoomModel.setDateArrive(dateArrive);
        bookRoomModel.setDateLeave(dateLeave);
    }

    private void getObjectRoomModel() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomModel");
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageRoom);
    }

    private void initUI() {
        btnBookNow = findViewById(R.id.btn_bookNow);
        btnCheckIn = findViewById(R.id.btn_check_in);
        btnCheckOut = findViewById(R.id.btn_check_out);
        btnBack = findViewById(R.id.btnBack);
        imageRoom = findViewById(R.id.imageRoom);
    }

    public void pickCheckInDate_click() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btnCheckIn.setText(String.format(Locale.getDefault(), "%d/%d/%d", day, month, year));
                        dateArrive = String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void pickCheckOutDate_click() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btnCheckOut.setText(String.format(Locale.getDefault(),"%d/%d/%d", day, month, year));
                        dateLeave = String.format(Locale.getDefault(),"%d/%d/%d", day, month, year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}