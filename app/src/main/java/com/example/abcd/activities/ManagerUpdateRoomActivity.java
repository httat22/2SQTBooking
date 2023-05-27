package com.example.abcd.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abcd.R;
import com.example.abcd.models.RoomTypeModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ManagerUpdateRoomActivity extends AppCompatActivity {

    private ImageView btnCancel;
    private EditText edtRoomName, edtPrice, edtDescription;
    private AppCompatButton btnUpdateRoom, btnSingleRoom, btnDoubleRoom;
    private ImageView imageViewUpdate;
    private Uri mImageUri;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("hotels");
    private boolean isSingleRoom = false, isDoubleRoom = false;
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
        btnSingleRoom = findViewById(R.id.btnSingleRoom);
        btnDoubleRoom = findViewById(R.id.btnDoubleRoom);
        getObjectRoomModel();
        btnSingleRoom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                isSingleRoom = true;
                isDoubleRoom = false;
                btnSingleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                btnDoubleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
            }
        });
        btnDoubleRoom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                isSingleRoom = false;
                isDoubleRoom = true;
                btnSingleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
                btnDoubleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerUpdateRoomActivity.super.onBackPressed();
            }
        });
        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRoom();
            }
        });
        selectImageFromGallery();
    }

    private void updateRoom() {
        if (mImageUri != null) {
            StorageReference fileRef = storageReference.child(roomTypeModel.getRoomId())
                    .child(roomTypeModel.getRoomId() + "." + getFileExtension(mImageUri));

            UploadTask uploadTask = fileRef.putFile(mImageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(ManagerUpdateRoomActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();
                        String roomName = edtRoomName.getText().toString().trim();
                        String description = edtDescription.getText().toString().trim();
                        String price = edtPrice.getText().toString().trim();
                        String roomType;
                        if (isSingleRoom) {
                            roomType = "single";
                        } else {
                            roomType = "double";
                        }
                        roomTypeModel.setRoom(roomName);
                        roomTypeModel.setPrice(Integer.parseInt(price));
                        roomTypeModel.setDescription(description);
                        roomTypeModel.setRoomType(roomType);
                        roomTypeModel.setImageURL(downloadUri.toString());

                        databaseReference.child(roomTypeModel.getRoomId()).updateChildren(roomTypeModel.toMap());
                    }
                }
            });
        } else {
            Toast.makeText(ManagerUpdateRoomActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();
            String roomName = edtRoomName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String price = edtPrice.getText().toString().trim();
            String roomType;
            if (isSingleRoom) {
                roomType = "single";
            } else {
                roomType = "double";
            }
            roomTypeModel.setRoom(roomName);
            roomTypeModel.setPrice(Integer.parseInt(price));
            roomTypeModel.setDescription(description);
            roomTypeModel.setRoomType(roomType);
            roomTypeModel.setImageURL(roomTypeModel.getImageURL());

            databaseReference.child(roomTypeModel.getRoomId()).updateChildren(roomTypeModel.toMap());
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @SuppressLint("ResourceType")
    private void getObjectRoomModel() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        roomTypeModel = (RoomTypeModel) bundle.get("object_roomTypeModel");
        tvChooseImage.setVisibility(View.GONE);
        Glide.with(this).load(roomTypeModel.getImageURL()).into(imageViewUpdate);
        edtRoomName.setText(roomTypeModel.getRoom());
        edtPrice.setText(String.valueOf(roomTypeModel.getPrice()));
        edtDescription.setText(roomTypeModel.getDescription());
        if (roomTypeModel.getRoomType().equals("single")) {
            isSingleRoom = true;
            btnSingleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            btnDoubleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        } else {
            isDoubleRoom = true;
            btnSingleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
            btnDoubleRoom.setBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
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