package com.example.abc.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.abc.R;

import java.util.Calendar;
import java.util.Locale;

public class FastOrderFragment extends Fragment {

    Button btnCheckIn, btnCheckOut, btnBooking;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fast_order, container, false);

        btnCheckIn = view.findViewById(R.id.btn_checkIn);
        btnCheckOut = view.findViewById(R.id.btn_checkOut);
        btnBooking = view.findViewById(R.id.btnBooking);

        pickTimeDuration();

        onClickBooking();

        return view;
    }

    private void onClickBooking() {

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
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
