package com.example.abc.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.abc.R;
import com.example.abc.models.RoomTypeModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ManagerAddRoomActivity extends AppCompatActivity {
    private ImageView btnCancelAdd;
    private EditText edtRoomName, edtPrice, edtDescription;
    private AppCompatButton btnAddRoom, btnSingleRoom, btnDoubleRoom;
    private ImageView imageViewAdd;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("hotels");
    private Uri mImageUri;
    private boolean isSingleRoom = true, isDoubleRoom = false;
    private TextView tvChooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_add_room_manager);
        getSupportActionBar().hide();

        edtRoomName = findViewById(R.id.edtRoomName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);
        btnSingleRoom = findViewById(R.id.btnSingleRoom);
        btnDoubleRoom = findViewById(R.id.btnDoubleRoom);
        imageViewAdd = findViewById(R.id.imageView);
        tvChooseImage = findViewById(R.id.tvChooseImage);

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
        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerAddRoomActivity.super.onBackPressed();
            }
        });
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

        selectImageFromGallery();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addRoom() {
        long currentTime = System.currentTimeMillis();
        if (mImageUri != null) {
            StorageReference fileRef = storageReference.child(String.valueOf(currentTime)).child(currentTime +
                    "." + getFileExtension(mImageUri));

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
                        Toast.makeText(ManagerAddRoomActivity.this, "Add successful!", Toast.LENGTH_SHORT).show();
                        String roomName = edtRoomName.getText().toString().trim();
                        String description = edtDescription.getText().toString().trim();
                        String price = edtPrice.getText().toString().trim();
                        String roomType;
                        if (isSingleRoom) {
                            roomType = "single";
                        } else {
                            roomType = "double";
                        }
                        RoomTypeModel roomTypeModel = new RoomTypeModel();

                        roomTypeModel.setRoom(roomName);
                        roomTypeModel.setPrice(Integer.parseInt(price));
                        roomTypeModel.setDescription(description);
                        roomTypeModel.setRoomType(roomType);
                        roomTypeModel.setRoomId(String.valueOf(currentTime));
                        roomTypeModel.setImageURL(downloadUri.toString());

                        databaseReference.child(roomTypeModel.getRoomId()).setValue(roomTypeModel);
                    }
                }
            });
        } else {
            Toast.makeText(ManagerAddRoomActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
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
                            imageViewAdd.setImageURI(mImageUri);
                        } else {
                            Toast.makeText(getApplication(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
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