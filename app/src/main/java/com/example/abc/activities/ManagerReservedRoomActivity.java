package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.abc.R;
import com.example.abc.adapters.ReserveRoomAdapter;
import com.example.abc.models.TicketModel;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class ManagerReservedRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<TicketModel> list;
    private ReserveRoomAdapter reserveRoomAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("list_staying");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reserved_room);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.reserveRoomListRec);
        shimmerFrameLayout = findViewById(R.id.shimmer);

        shimmerFrameLayout.startShimmer();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        getDataFromRealTimeDatabase();
        reserveRoomAdapter = new ReserveRoomAdapter(this, list);
        recyclerView.setAdapter(reserveRoomAdapter);
    }

    private void getDataFromRealTimeDatabase() {
        databaseReference.orderByChild("dateBooked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    TicketModel ticketModel = (TicketModel) dataSnapshot.getValue(TicketModel.class);
                    if (ticketModel != null) {
                        String dateLeave = ticketModel.getDateLeave();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date date = dateFormat.parse(dateLeave);
                            Calendar currentCalendar = Calendar.getInstance();
                            Date currentDate = currentCalendar.getTime();
                            if (date.compareTo(currentDate) > 0 && ticketModel.getNameType().equals("Booking Room")) {
                                list.add(0, ticketModel);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                reserveRoomAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}