package com.example.abc.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.RoomTypeModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerUpdateRoomActivity extends AppCompatActivity {

    private ImageView btnCancel;
    private EditText edtRoomName, edtPrice, edtDescription;
    private AppCompatButton btnUpdateRoom;
    private ImageView imageViewUpdate;
    private Uri mImageUri;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    private RoomTypeModel roomTypeModel;
    private TextView tvChooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_update_room_manager);
        getSupportActionBar().hide();

        edtRoomName = findViewById(R.id.edtRoomName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnUpdateRoom = findViewById(R.id.btnUpdateRoom);
        btnCancel = findViewById(R.id.btnCancel);
        tvChooseImage = findViewById(R.id.tvChooseImage);
        imageViewUpdate = findViewById(R.id.imageRoom);
        getObjectRoomModel();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        Toast.makeText(ManagerUpdateRoomActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        selectImageFromGallery();
    }
    private void getObjectRoomModel() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomTypeModel");
        tvChooseImage.setVisibility(View.GONE);
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageViewUpdate);
        edtRoomName.setText(roomTypeModel.getRoom());
        edtPrice.setText(String.valueOf(roomTypeModel.getPrice()));
        edtDescription.setText(roomTypeModel.getDescription());
    }
    private void selectImageFromGallery() {
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            mImageUri = data.getData();
                            tvChooseImage.setVisibility(View.GONE);
                            imageViewUpdate.setImageURI(mImageUri);
                        } else {
                            Toast.makeText(getApplication(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        imageViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }
}