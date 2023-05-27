package com.example.abcd.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.abcd.R;
import com.example.abcd.adapters.RoomManagerAdapter;
import com.example.abcd.models.RoomTypeModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ManagerListRoomActivity extends AppCompatActivity {
    private RecyclerView roomRecManager;
    private AppCompatButton btnAddRoom;
    private List<RoomTypeModel> roomTypeModelList;
    private RoomManagerAdapter roomManagerAdapter;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    private ShimmerFrameLayout shimmerFrameLayout;
    public ManagerListRoomActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list_room);
        getSupportActionBar().hide();

        roomRecManager = findViewById(R.id.roomRecManager);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        shimmerFrameLayout.startShimmer();
        roomRecManager.setHasFixedSize(true);
        roomRecManager.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        roomRecManager.addItemDecoration(dividerItemDecoration);

        roomTypeModelList = new ArrayList<>();
        roomManagerAdapter = new RoomManagerAdapter(this, roomTypeModelList, new RoomManagerAdapter.IClickListener() {
            @Override
            public void onClickUpdateItem(RoomTypeModel roomTypeModel) {
                Intent intent = new Intent(ManagerListRoomActivity.this, ManagerUpdateRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_roomTypeModel", roomTypeModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            @Override
            public void onClickDeleteItem(RoomTypeModel roomTypeModel) {
                onClickDeleteData(roomTypeModel);
            }
        });
        
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerListRoomActivity.this, ManagerAddRoomActivity.class);
                startActivity(intent);
            }
        });

        roomRecManager.setAdapter(roomManagerAdapter);

        getRoomModelListFromRealTimeDatabase();
    }
    private void onClickDeleteData(RoomTypeModel roomTypeModel) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure want to delete this room?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child(roomTypeModel.getRoomId()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(ManagerListRoomActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void getRoomModelListFromRealTimeDatabase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override

            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    roomRecManager.setVisibility(View.VISIBLE);
                    roomTypeModelList.add(roomTypeModel);
                    roomManagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomTypeModel roomTypeModel = snapshot.getValue(RoomTypeModel.class);
                if (roomTypeModel == null || roomTypeModelList == null || roomTypeModelList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < roomTypeModelList.size(); i++) {
                    if (roomTypeModel.getRoomId().equals(roomTypeModelList.get(i).getRoomId())) {
                        roomTypeModelList.set(i, roomTypeModel);
                        break;
                    }
                }
                roomManagerAdapter.notifyDataSetChanged();
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
                roomManagerAdapter.notifyDataSetChanged();
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