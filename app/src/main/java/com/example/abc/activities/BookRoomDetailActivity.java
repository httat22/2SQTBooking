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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.BookRoomModel;
import com.example.abc.models.RoomTypeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookRoomDetailActivity extends AppCompatActivity{
    private Button btnBookNow, btnCheckIn, btnCheckOut;
    private ImageButton btnBack;
    private ImageView imageRoom;
    private TextView tvNameRoom, tvPrice, tvNumberPerson;

    private RoomTypeModel roomTypeModel;
    private BookRoomModel bookRoomModel;
    private String dateArrive, dateLeave;
    private final DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
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
        bookRoomModel.setRoomId(roomTypeModel.getRoomId());
        bookRoomModel.setPrice(roomTypeModel.getPrice());
        bookRoomModel.setDateArrive(dateArrive);
        bookRoomModel.setDateLeave(dateLeave);
    }

    private void getObjectRoomModel() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomTypeModel");
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageRoom);
        tvNameRoom.setText(roomTypeModel.getRoom());
        String stringPrice = roomTypeModel.getPrice() + "$";
        tvPrice.setText(stringPrice);

        String numberSingle = "2 Adults";
        String numberDouble = "4 Adults";
        if (roomTypeModel.getRoomType().equals("single")) {
            tvNumberPerson.setText(numberSingle);
        } else {
            tvNumberPerson.setText(numberDouble);
        }
    }

    private void initUI() {
        btnBookNow = findViewById(R.id.btn_bookNow);
        btnCheckIn = findViewById(R.id.btn_check_in);
        btnCheckOut = findViewById(R.id.btn_check_out);
        btnBack = findViewById(R.id.btnBack);
        imageRoom = findViewById(R.id.imageRoom);
        tvNameRoom = findViewById(R.id.tvNameRoom);
        tvPrice = findViewById(R.id.tvPrice);
        tvNumberPerson = findViewById(R.id.tvNumberPerson);
    }

    public void pickCheckInDate_click() {
        List<String> disableTime = new ArrayList<>();
        String path = String.valueOf(roomTypeModel.getRoomId());
        timeRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot time: snapshot.getChildren()) {
                    String s = time.getValue(String.class);
                    disableTime.add(s);
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateArrive = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        btnCheckIn.setText(dateArrive);
                    }
                }, year, month, day);
                dpd.show(getSupportFragmentManager(), "DatePickerDialog");
                dpd.setMinDate(calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                for (String time : disableTime) {
                    try {
                        date = sdf.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    calendar = dateToCalendar(date);

                    List<Calendar> dates = new ArrayList<>();
                    dates.add(calendar);
                    Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
                    dpd.setDisabledDays(disabledDays1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pickCheckOutDate_click() {
        List<String> disableTime = new ArrayList<>();
        String path = String.valueOf(roomTypeModel.getRoomId());
        timeRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot time: snapshot.getChildren()) {
                    String s = time.getValue(String.class);
                    disableTime.add(s);
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateLeave = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        btnCheckOut.setText(dateLeave);
                    }
                }, year, month, day);
                dpd.show(getSupportFragmentManager(), "DatePickerDialog");
                dpd.setMinDate(calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                for (String time : disableTime) {
                    try {
                        date = sdf.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    calendar = dateToCalendar(date);

                    List<Calendar> dates = new ArrayList<>();
                    dates.add(calendar);
                    Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
                    dpd.setDisabledDays(disabledDays1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @NonNull
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}