package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.abc.R;
import com.example.abc.adapters.ReserveUserAdapter;
import com.example.abc.models.ReserveUserModel;
import com.example.abc.models.UserModel;
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

public class ManagerListUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ReserveUserModel> list;
    private ReserveUserAdapter reserveUserAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reserving_user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list_user);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.reserveRoomListRec);
        shimmerFrameLayout = findViewById(R.id.shimmer);

        shimmerFrameLayout.startShimmer();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        getDataFromRealTimeDatabase();
        reserveUserAdapter = new ReserveUserAdapter(this, list);
        recyclerView.setAdapter(reserveUserAdapter);
    }
    private void getDataFromRealTimeDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ReserveUserModel reserveUserModel = (ReserveUserModel) dataSnapshot.getValue(ReserveUserModel.class);
                    list.add(reserveUserModel);
                }
                reserveUserAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}