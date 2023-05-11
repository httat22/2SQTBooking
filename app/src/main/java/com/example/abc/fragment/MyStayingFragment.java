package com.example.abc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.adapters.TicketAdapter;
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

public class MyStayingFragment extends Fragment {
    private RecyclerView recMyStaying;
    private List<TicketModel> list;
    private TicketAdapter ticketAdapter;

    ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_staying, container, false);

        recMyStaying = view.findViewById(R.id.recMyStaying);
        shimmerFrameLayout = view.findViewById(R.id.shimmerMyStaying);
        shimmerFrameLayout.startShimmer();
        recMyStaying.setHasFixedSize(true);
        recMyStaying.setLayoutManager(new LinearLayoutManager(getContext()));
        
        list = new ArrayList<>();
        getMyStayingFromRealTimeDatabase();
        ticketAdapter = new TicketAdapter(list, getActivity());
        recMyStaying.setAdapter(ticketAdapter);
        return view;
    }

    private void getMyStayingFromRealTimeDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String userID = user.getUid();
        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recMyStaying.setVisibility(View.VISIBLE);
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    TicketModel ticketModel = (TicketModel) dataSnapshot.getValue(TicketModel.class);
                    list.add(ticketModel);
                }
                ticketAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}