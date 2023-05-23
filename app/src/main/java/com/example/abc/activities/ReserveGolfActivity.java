package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.abc.MainActivity;
import com.example.abc.R;
import com.example.abc.models.BookServiceModel;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReserveGolfActivity extends AppCompatActivity {
    private ImageButton btnBack;

    private Button btnCheckIn, btnCheckOut, btnBookGolf, btnNumberPerson;

    private String dateArrive, dateLeave;
    private BookServiceModel bookServiceModel;
    private DatabaseReference databaseReference;
    private final DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("time_golf_booked");
    private DatabaseReference listStayingRef = FirebaseDatabase.getInstance().getReference("list_staying");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_golf);

        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        btnCheckIn = findViewById(R.id.btn_check_in);
        btnCheckOut = findViewById(R.id.btn_check_out);
        btnBookGolf = findViewById(R.id.btnBookGolf);
        btnNumberPerson = findViewById(R.id.btnNumberPerson);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnBookGolf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getApplication(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickAddRealtimeDatabase();

                Intent intent = new Intent(ReserveGolfActivity.this, PaymentGolfActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_bookServiceModel", bookServiceModel);
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

    private void onClickAddRealtimeDatabase() {
        getBookServiceModel();
        databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
        onClickAddTimeToRealTimeDatabase();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId, nameType, imageURL, ticketId, description, userName;
        int numberPerson;

        long currentTime = System.currentTimeMillis();

        userId = user.getUid();
        userName = user.getDisplayName();
        nameType = "Golf";
        imageURL = "https://firebasestorage.googleapis.com/v0/b/sqtbooking-cc92e.appspot.com/o/golfs%2Fgolf2.jpg?alt=media&token=4c5170e1-d16b-47f7-b596-01ef862176f4";
        ticketId = "Golf" + userId + System.currentTimeMillis();

        int numOfDate = getNumberOfDate(bookServiceModel.getDateArrive(), bookServiceModel.getDateLeave());
        numberPerson = bookServiceModel.getNumberPerson();

        int totalPayment = numberPerson*numOfDate*bookServiceModel.getPrice();
        bookServiceModel.setTotalPayment(totalPayment);

        description = "";
        TicketModel ticketModel = new TicketModel(userId, nameType, bookServiceModel.getDateArrive(),
                bookServiceModel.getDateLeave(), imageURL, totalPayment, numberPerson,
                ticketId, description, "reserved", currentTime, userName);
        databaseReference.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
            }
        });

        listStayingRef.child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickAddTimeToRealTimeDatabase() {
        String s1 = (String) bookServiceModel.getDateArrive();
        String s2 = (String) bookServiceModel.getDateLeave();
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
        for (String s : list) {
            timeRef.push().setValue(s);
        }
    }

    private void getBookServiceModel() {
        bookServiceModel = new BookServiceModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        bookServiceModel.setUserId(user.getUid());

        int numberPerson = Integer.parseInt((String) btnNumberPerson.getText());
        bookServiceModel.setNumberPerson(numberPerson);

        bookServiceModel.setPrice(50);
        bookServiceModel.setDateArrive(dateArrive);
        bookServiceModel.setDateLeave(dateLeave);
    }

    public void pickCheckOutDate_click() {
        List<String> disableTime = new ArrayList<>();
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//                Date date = null;
//                for (String time : disableTime) {
//                    try {
//                        date = sdf.parse(time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    calendar = dateToCalendar(date);
//
//                    List<Calendar> dates = new ArrayList<>();
//                    dates.add(calendar);
//                    Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
//                    dpd.setDisabledDays(disabledDays1);
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pickCheckInDate_click() {
        List<String> disableTime = new ArrayList<>();
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//                Date date = null;
//                for (String time : disableTime) {
//                    try {
//                        date = sdf.parse(time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    calendar = dateToCalendar(date);
//
//                    List<Calendar> dates = new ArrayList<>();
//                    dates.add(calendar);
//                    Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
//                    dpd.setDisabledDays(disabledDays1);
//                }
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