package com.example.abcd.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abcd.R;
import com.example.abcd.adapters.TicketAdapter;
import com.example.abcd.models.TicketModel;
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

public class MyStayingFragment extends Fragment {
    private RecyclerView recMyStaying;
    private List<TicketModel> list;
    private TicketAdapter ticketAdapter;
    private ImageView imgEmptyCart;
    private TextView tvEmpty;

    ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_staying, container, false);


        imgEmptyCart = view.findViewById(R.id.imgEmptyCart);
        tvEmpty = view.findViewById(R.id.tvEmpty);
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
        databaseReference.child(userID).orderByChild("dateBooked").addValueEventListener(new ValueEventListener() {
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
                if (list.size() == 0) {
                    imgEmptyCart.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    imgEmptyCart.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                }
                ticketAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}