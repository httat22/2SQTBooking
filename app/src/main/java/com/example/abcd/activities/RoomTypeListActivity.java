package com.example.abcd.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abcd.R;
import com.example.abcd.adapters.RoomTypeAdapter;
import com.example.abcd.models.RoomTypeModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<RoomTypeModel> roomTypeModelList;
    RoomTypeAdapter roomTypeAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");

    ShimmerFrameLayout shimmerFrameLayout;

    public RoomTypeListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);

        recyclerView = findViewById(R.id.roomRec);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        roomTypeModelList = new ArrayList<>();
        roomTypeAdapter = new RoomTypeAdapter(this, roomTypeModelList);
        recyclerView.setAdapter(roomTypeAdapter);

        getRoomModelListFromRealTimeDatabase();

    }

    private void getRoomModelListFromRealTimeDatabase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    roomTypeModelList.add(roomTypeModel);
                    roomTypeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel == null || roomTypeModelList == null || roomTypeModelList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < roomTypeModelList.size(); i++) {
                    if (roomTypeModel.getRoomId() == roomTypeModelList.get(i).getRoomId()) {
                        roomTypeModelList.set(i, roomTypeModel);
                        break;
                    }
                }
                roomTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel == null || roomTypeModelList == null || roomTypeModelList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < roomTypeModelList.size(); i++) {
                    if (roomTypeModel.getRoomId() == roomTypeModelList.get(i).getRoomId()) {
                        roomTypeModelList.remove(roomTypeModelList.get(i));
                        break;
                    }
                }
                roomTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roomTypeAdapter != null) {
            roomTypeAdapter.release();
        }
    }
}