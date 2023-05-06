package com.example.abc.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.abc.R;
import com.example.abc.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class FastBookingFragment extends Fragment {

    private Button btnCheckIn, btnCheckOut, btnBooking;
    View view;

    private DatabaseReference databaseReference;
    private String dateArrive, dateLeave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fast_order, container, false);

        btnCheckIn = view.findViewById(R.id.btn_checkIn);
        btnCheckOut = view.findViewById(R.id.btn_checkOut);
        btnBooking = view.findViewById(R.id.btnBooking);

        pickTimeDuration();
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getContext(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickBooking();
            }
        });
        return view;
    }

    private void onClickBooking() {
        databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId, nameType, time, imageURL, ticketId, description;
        int price, numberPerson;
        userId = user.getUid();
        nameType = "Booking Room";
        time = dateArrive + " - " + dateLeave;
        imageURL = "https://firebasestorage.googleapis.com/v0/b/sqtbooking-cc92e.appspot.com/o/hotels%2Fhomepagepropose1.jpg?alt=media&token=e01dc77e-3bdb-4378-9b47-5b44fa3c2f6d";
        ticketId = "2SQT" + System.currentTimeMillis();
        price = 500;
        numberPerson = 2;
        description = "Room 402";
        TicketModel ticketModel = new TicketModel(userId, nameType, time, imageURL, price, numberPerson, ticketId, description, "checkIn");
        databaseReference.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickTimeDuration() {
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btnCheckIn.setText(String.format(Locale.getDefault(), "%d/%d/%d", day, month, year));
                                dateArrive = String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btnCheckOut.setText(String.format(Locale.getDefault(), "%d/%d/%d", day, month, year));
                                dateLeave = String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
