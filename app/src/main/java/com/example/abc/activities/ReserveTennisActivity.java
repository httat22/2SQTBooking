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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class ReserveTennisActivity extends AppCompatActivity {

    private ImageButton btnBack, btnHome;

    private Button btnCheckIn, btnCheckOut, btnBookTennis;
    private String dateArrive, dateLeave;
    private BookServiceModel bookServiceModel;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_tennis);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
//        btnHome = findViewById(R.id.btnHome);
        btnCheckIn = findViewById(R.id.btn_check_in);
        btnCheckOut = findViewById(R.id.btn_check_out);
        btnBookTennis = findViewById(R.id.btnBookTennis);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        btnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplication(), MainActivity.class));
//                finishAffinity();
//            }
//        });
        btnBookTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getApplication(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                getBookServiceModel();
                onClickAddRealtimeDatabase();
                Intent intent = new Intent(ReserveTennisActivity.this, PaymentTennisActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_bookServiceModel", bookServiceModel);
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
        nameType = "Tennis ticket";
        time = bookServiceModel.getDateArrive() + " - " + bookServiceModel.getDateLeave();
        imageURL = "https://firebasestorage.googleapis.com/v0/b/sqtbooking-cc92e.appspot.com/o/tennis%2Ftennis2.jpg?alt=media&token=ea267312-95f2-46c6-98d3-fcf3fdd11c22";
        ticketId = "Tennis" + System.currentTimeMillis();
        price = bookServiceModel.getPrice();
        numberPerson = 2;
        description = "";
        TicketModel ticketModel = new TicketModel(userId, nameType, time, imageURL, price, numberPerson, ticketId, description, "reserved");
        databaseReference.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBookServiceModel() {
        bookServiceModel = new BookServiceModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        bookServiceModel.setUserId(user.getUid());
        bookServiceModel.setNumberPerson(2);
        bookServiceModel.setPrice(1000);
        bookServiceModel.setDateArrive(dateArrive);
        bookServiceModel.setDateLeave(dateLeave);
    }
    public void pickCheckInDate_click(View view) {
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

    public void pickCheckOutDate_click(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btnCheckOut.setText(String.format(Locale.getDefault(), "%d/%d/%d", day, month, year));
                        dateLeave = String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}