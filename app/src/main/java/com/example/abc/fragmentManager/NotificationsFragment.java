package com.example.abc.fragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.adapters.NotificationAdapter;
import com.example.abc.models.TicketModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
                    list.add(0, ticketModel);
                }
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}