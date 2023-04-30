package com.example.abc.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.adapters.RoomAdapter;
import com.example.abc.models.RoomModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListRoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<RoomModel> roomModelList;
    RoomAdapter roomAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);

        recyclerView = findViewById(R.id.roomRec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomModelList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this, roomModelList);
        recyclerView.setAdapter(roomAdapter);

        getRoomModelListFromRealTimeDatabase();

    }
    private void getRoomModelListFromRealTimeDatabase() {
//        Cach 1
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (roomModelList != null) {
//                    roomModelList.clear();
//                }
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    RoomModel roomModel = (RoomModel) dataSnapshot.getValue(RoomModel.class);
//                    roomModelList.add(roomModel);
//                }
//                roomAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomModel roomModel = snapshot.getValue(RoomModel.class);
                if (roomModel != null) {
                    roomModelList.add(roomModel);
                    roomAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomModel roomModel = snapshot.getValue(RoomModel.class);
                if (roomModel == null || roomModelList == null || roomModelList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < roomModelList.size(); i++) {
                    if (roomModel.getId() == roomModelList.get(i).getId()) {
                        roomModelList.set(i, roomModel);
                        break;
                    }
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RoomModel roomModel = snapshot.getValue(RoomModel.class);
                if (roomModel == null || roomModelList == null || roomModelList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < roomModelList.size(); i++) {
                    if (roomModel.getId() == roomModelList.get(i).getId()) {
                        roomModelList.remove(roomModelList.get(i));
                        break;
                    }
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}