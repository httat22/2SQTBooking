package com.example.abcd.fragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abcd.R;
import com.example.abcd.adapters.NotificationAdapter;
import com.example.abcd.models.TicketModel;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<TicketModel> list;
    private NotificationAdapter notificationAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("list_staying");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.notificationRec);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);

        shimmerFrameLayout.startShimmer();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        getDataFromRealTimeDatabase();
        notificationAdapter = new NotificationAdapter(getActivity(), list);
        recyclerView.setAdapter(notificationAdapter);
        return view;
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
                            if (date.compareTo(currentDate) > 0) {
                                list.add(0, ticketModel);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}