package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abc.R;
import com.example.abc.adapters.RoomManagerAdapter;
import com.example.abc.models.RoomTypeModel;
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
                openDialogUpdateItem(roomTypeModel);
            }

            @Override
            public void onClickDeleteItem(RoomTypeModel roomTypeModel) {
                onClickDeleteData(roomTypeModel);
            }
        });
        
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddRoom();
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

    private void openDialogAddRoom() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ntt_add_room_manager);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        ImageView btnCancel;
        EditText edtRoomName, edtPrice, edtDescription;
        AppCompatButton btnUpdateRoom;

        edtRoomName = dialog.findViewById(R.id.edtRoomName);
        edtPrice = dialog.findViewById(R.id.edtPrice);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnUpdateRoom = dialog.findViewById(R.id.btnUpdateRoom);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName = edtRoomName.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                RoomTypeModel roomTypeModel = new RoomTypeModel();
                roomTypeModel.setRoom(roomName);
                roomTypeModel.setPrice(Integer.parseInt(price));
                roomTypeModel.setDescription(description);
                databaseReference.child(roomTypeModel.getRoomId()).updateChildren(roomTypeModel.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ManagerListRoomActivity.this, "Add Room Successful!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void openDialogUpdateItem(RoomTypeModel roomTypeModel) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ntt_add_room_manager);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        ImageView btnCancel;
        EditText edtRoomName, edtPrice, edtDescription;
        AppCompatButton btnUpdateRoom;

        edtRoomName = dialog.findViewById(R.id.edtRoomName);
        edtPrice = dialog.findViewById(R.id.edtPrice);
        edtDescription = dialog.findViewById(R.id.edtDescription);
        btnUpdateRoom = dialog.findViewById(R.id.btnUpdateRoom);
        btnCancel = dialog.findViewById(R.id.btnCancel);

//        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageRoom);
        edtRoomName.setText(roomTypeModel.getRoom());
        edtPrice.setText(String.valueOf(roomTypeModel.getPrice()));
        edtDescription.setText(roomTypeModel.getDescription());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newRoomName = edtRoomName.getText().toString().trim();
                String newDescription = edtDescription.getText().toString().trim();
                String newPrice = edtPrice.getText().toString().trim();
                roomTypeModel.setRoom(newRoomName);
                roomTypeModel.setPrice(Integer.parseInt(newPrice));
                roomTypeModel.setDescription(newDescription);
                databaseReference.child(roomTypeModel.getRoomId()).updateChildren(roomTypeModel.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ManagerListRoomActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
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