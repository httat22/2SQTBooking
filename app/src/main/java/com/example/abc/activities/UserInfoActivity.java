package com.example.abc.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.RoomTypeModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserInfoActivity extends AppCompatActivity {
    private AppCompatButton btnUpdate;
    private EditText edtUserName, edtUserAddress, edtUserEmail, edtUserPhone;
    private ImageView userImage;
    private Uri imageUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("users");
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        initUI();
        showUserInformation();
        selectImageFromGallery();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        databaseReference.child(uid).child("userName").setValue(edtUserName.getText().toString().trim());
        databaseReference.child(uid).child("address").setValue(edtUserAddress.getText().toString().trim());
        databaseReference.child(uid).child("phone").setValue(edtUserPhone.getText().toString().trim());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(edtUserName.getText().toString().trim())
                .setPhotoUri(imageUri)
                .build();
        progressBar.setVisibility(View.VISIBLE);
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplication(), "Update profile success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (imageUri != null) {
            StorageReference fileRef = storageReference.child(uid).child(uid +
                    "." + getFileExtension(imageUri));

            UploadTask uploadTask = fileRef.putFile(imageUri);
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
                        databaseReference.child(uid).child("imageURL").setValue(downloadUri.toString());
                    }
                }
            });
        } else {
            Toast.makeText(UserInfoActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void selectImageFromGallery() {
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            userImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(getApplication(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        String uid = user.getUid();

        readUserInfo(uid);

        edtUserEmail.setText(email);

    }

    private void readUserInfo(String uid) {
        databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = String.valueOf(dataSnapshot.child("userName").getValue());
                        String address = String.valueOf(dataSnapshot.child("address").getValue());
                        String phone = String.valueOf(dataSnapshot.child("phone").getValue());
                        String imageURL = String.valueOf(dataSnapshot.child("imageURL").getValue());
                        edtUserName.setText(user.getDisplayName());
                        edtUserAddress.setText(address);
                        edtUserPhone.setText(phone);
                        Glide.with(UserInfoActivity.this).load(imageURL).error(R.drawable.user_default).into(userImage);
                    }
                }
            }
        });
    }

    private void initUI() {
        btnUpdate = findViewById(R.id.btnUpdate);
        userImage = findViewById(R.id.userImage);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserName = findViewById(R.id.edtUserName);
        edtUserAddress = findViewById(R.id.edtUserAddress);
        edtUserPhone = findViewById(R.id.edtUserPhone);
        progressBar = findViewById(R.id.progressBar);
    }
}