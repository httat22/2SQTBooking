package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.abc.R;
import com.example.abc.models.RoomTypeModel;
import com.example.abc.models.TicketModel;
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

public class ReserveFastBookingActivity extends AppCompatActivity {

    private Button btnCheckIn, btnCheckOut;
    private CardView btnBooking, btnSingleRoom, btnDoubleRoom;
    private boolean isSingleRoom = false, isDoubleRoom = false;
    private String dateArrive, dateLeave;
    private final DatabaseReference userStayingRef = FirebaseDatabase.getInstance().getReference("user_staying");;
    private final DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
    private final DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("Room");

    private List<String> listRangeTime;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_fast_booking);
        getSupportActionBar().hide();
        initUI();

        pickRoomType();
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getApplication(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isSingleRoom && !isDoubleRoom) {
                    Toast.makeText(getApplication(), "You need choose room type", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder myDialog = new AlertDialog.Builder(ReserveFastBookingActivity.this);
                myDialog.setTitle("Booking confirmation");
                myDialog.setMessage("Are you sure you want to book room?");
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onClickBooking();
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });
    }

    private void initUI() {
        btnCheckIn = findViewById(R.id.btn_checkIn);
        btnCheckOut = findViewById(R.id.btn_checkOut);
        btnBooking = findViewById(R.id.btnBooking);
        btnSingleRoom = findViewById(R.id.btnSingleRoom);
        btnDoubleRoom = findViewById(R.id.btnDoubleRoom);
    }

    private void pickRoomType() {
        btnSingleRoom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                isSingleRoom = true;
                isDoubleRoom = false;
                btnSingleRoom.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                btnDoubleRoom.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
            }
        });
        btnDoubleRoom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                isSingleRoom = false;
                isDoubleRoom = true;
                btnSingleRoom.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
                btnDoubleRoom.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            }
        });
    }

    private void onClickBooking() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        getListRangeTime();
        if (user == null) {
            return;
        }
        String path;
        if (isDoubleRoom) {
            path = "double";
        } else {
            path = "single";
        }
        timeRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean check = true;
                boolean roomOut = true;
                for (DataSnapshot innerSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot timeSnapshot : innerSnapshot.getChildren()) {
                        String stringDate = timeSnapshot.getValue(String.class);
                        for (String time : listRangeTime) {
                            if (time.equals(stringDate)) {
                                check = false;
                                break;
                            }
                        }
                        if (!check) break;
                    }
                    if (check) {
                        roomOut = false;
                        String roomId = innerSnapshot.getKey();
                        bookRoomFastOrder(roomId);
                        break;
                    } else {
                        check = true;
                    }
                }
                if (roomOut) {
                    Toast.makeText(getApplication(), "The room is out", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bookRoomFastOrder(String roomId) {
        roomRef.child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel == null) return;

                onClickAddTimeToRealTimeDatabase(roomId, roomTypeModel.getRoomType());

                String userId, nameType, time, imageURL, ticketId, description;
                int price, numberPerson;

                userId = user.getUid();
                nameType = "Booking Room";
                time = dateArrive + " - " + dateLeave;
                imageURL = roomTypeModel.getImageURL();
                ticketId = "Fast" + roomTypeModel.getRoomId() + System.currentTimeMillis();
                price = roomTypeModel.getPrice();
                numberPerson = 2;
                description = roomTypeModel.getRoom();
                TicketModel ticketModel = new TicketModel(userId, nameType, time, imageURL, price, numberPerson, ticketId, description, "checkIn");
                userStayingRef.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickAddTimeToRealTimeDatabase(String roomId, String roomType) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(dateArrive);
            endDate = sdf.parse(dateLeave);
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
        String pathParent = String.valueOf(roomType);
        String path = String.valueOf(roomId);
        for (String s : list) {
            timeRef.child(path).push().setValue(s);
        }
    }

    private void getListRangeTime() {
        listRangeTime = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(dateArrive);
            endDate = sdf.parse(dateLeave);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        assert startDate != null;
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            Date currentDate = calendar.getTime();
            String dateString = sdf.format(currentDate);
            listRangeTime.add(dateString);
            calendar.add(Calendar.DATE, 1);
        }
        assert endDate != null;
        listRangeTime.add(sdf.format(endDate));
    }

    public void pickCheckOutDate_click(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                dateLeave = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                btnCheckOut.setText(dateLeave);
            }
        }, year, month, day);
        dpd.show(getSupportFragmentManager(), "DatePickerDialog");
        dpd.setMinDate(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] holidays = {};
        Date date = null;
        for (String holiday : holidays) {

            try {
                date = sdf.parse(holiday);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar = dateToCalendar(date);
            System.out.println(calendar.getTime());

            List<Calendar> dates = new ArrayList<>();
            dates.add(calendar);
            Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
            dpd.setDisabledDays(disabledDays1);
        }
    }

    public void pickCheckInDate_click(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                dateArrive = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                btnCheckIn.setText(dateArrive);
            }
        }, year, month, day);
        dpd.show(getSupportFragmentManager(), "DatePickerDialog");
        dpd.setMinDate(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] holidays = {};
        Date date = null;
        for (int i = 0;i < holidays.length; i++) {

            try {
                date = sdf.parse(holidays[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar = dateToCalendar(date);
            System.out.println(calendar.getTime());

            List<Calendar> dates = new ArrayList<>();
            dates.add(calendar);
            Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
            dpd.setDisabledDays(disabledDays1);
        }
    }

    @NonNull
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}