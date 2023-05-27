package com.example.abcd.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.abcd.R;
import com.example.abcd.models.InfoRoomBookedModel;
import com.example.abcd.models.ReserveUserModel;
import com.example.abcd.models.RoomTypeModel;
import com.example.abcd.models.TicketModel;
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

public class FastBookingFragment extends Fragment {

    private Button btnCheckIn, btnCheckOut;
    private CardView btnDoubleRoom, btnSingleRoom, btnBooking;
    private boolean isSingleRoom = false, isDoubleRoom = false;
    View view;

    private DatabaseReference userStayingRef = FirebaseDatabase.getInstance().getReference("user_staying");
    private DatabaseReference listStayingRef = FirebaseDatabase.getInstance().getReference("list_staying");
    private final DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
    private final DatabaseReference dateBookedRef = FirebaseDatabase.getInstance().getReference("statistics_room");
    private DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("Room");
    private DatabaseReference reserveUserRef = FirebaseDatabase.getInstance().getReference("reserving_user");
    private String dateArrive, dateLeave;
    private List<String> listRangeTime;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fast_order, container, false);

        btnCheckIn = view.findViewById(R.id.btn_checkIn);
        btnCheckOut = view.findViewById(R.id.btn_checkOut);
        btnBooking = view.findViewById(R.id.btnBooking);
        btnDoubleRoom = view.findViewById(R.id.btnDoubleRoom);
        btnSingleRoom = view.findViewById(R.id.btnSingleRoom);

        pickTimeDuration();
        pickRoomType();

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArrive == null || dateLeave == null) {
                    Toast.makeText(getContext(), "You need choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isSingleRoom && !isDoubleRoom) {
                    Toast.makeText(getContext(), "You need choose room type", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
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
        return view;
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
        if (isSingleRoom) {
            path = "single";
        } else {
            path = "double";
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
                    Toast.makeText(getContext(), "The room is out", Toast.LENGTH_SHORT).show();
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

                String userId, nameType, imageURL, ticketId, description, roomId, userName;
                int price, numberPerson;

                long currentTime = System.currentTimeMillis();

                userId = user.getUid();
                userName = user.getDisplayName();
                nameType = "Booking Room";
                imageURL = roomTypeModel.getImageURL();
                ticketId = "Fast" + roomTypeModel.getRoomId() + System.currentTimeMillis();
                price = roomTypeModel.getPrice();
                if (roomTypeModel.getRoomType().equals("single")) {
                    numberPerson = 2;
                } else {
                    numberPerson = 4;
                }
                description = roomTypeModel.getRoom();
                roomId = roomTypeModel.getRoomId();
                TicketModel ticketModel = new TicketModel(userId, nameType, dateArrive, dateLeave, imageURL,
                        price, numberPerson, ticketId, description, "checkIn",  roomId, currentTime, userName);
                userStayingRef.child(userId).child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                    }
                });

                listStayingRef.child(ticketId).setValue(ticketModel, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                    }
                });

                InfoRoomBookedModel infoRoomBookedModel = new InfoRoomBookedModel(roomId, dateArrive, dateLeave, numberPerson, currentTime, userId);
                dateBookedRef.child(roomId).push().setValue(infoRoomBookedModel);
                ReserveUserModel reserveUserModel = new ReserveUserModel(userId, userName, user.getEmail(), "");
                reserveUserRef.child(userId).setValue(reserveUserModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            timeRef.child(roomType).child(path).push().setValue(s);
        }
    }

    private void pickTimeDuration() {
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                dpd.show(getChildFragmentManager(), "DatePickerDialog");
                dpd.setMinDate(calendar);
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                dpd.show(getChildFragmentManager(), "DatePickerDialog");
                dpd.setMinDate(calendar);
            }
        });
    }
}
