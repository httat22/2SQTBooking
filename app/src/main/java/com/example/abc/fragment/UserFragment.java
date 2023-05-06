package com.example.abc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.activities.LoginActivity;
import com.example.abc.databinding.FragmentUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    private Button btnLogout, btnUpdate;
    private ImageView imvUserImage;
    private EditText edtUserName, edtUserEmail, edtUserAddress, edtUserPhone;
    private Uri imageUri;
    private DatabaseReference databaseReference;

    ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        initUI(root);

        showUserInformation();

        selectImageFromGallery();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finishAffinity();
                }
            }
        });

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClickUpdateProfile();
           }
       });

        return root;
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        databaseReference.child(uid).child("userName").setValue(edtUserName.getText().toString().trim());
        databaseReference.child(uid).child("address").setValue(edtUserAddress.getText().toString().trim());
        databaseReference.child(uid).child("phone").setValue(edtUserPhone.getText().toString().trim());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtUserName.getText().toString().trim())
                .setPhotoUri(imageUri)
                .build();
        progressBar.setVisibility(View.VISIBLE);
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Update profile success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void initUI(View view) {
        btnLogout = view.findViewById(R.id.btn_logout);
        btnUpdate = view.findViewById(R.id.btn_update);
        imvUserImage = view.findViewById(R.id.userImage);

        edtUserEmail = view.findViewById(R.id.edtUserEmail);
        edtUserName = view.findViewById(R.id.edtUserName);
        edtUserAddress = view.findViewById(R.id.edtUserAddress);
        edtUserPhone = view.findViewById(R.id.edtUserPhone);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
//        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        String uid = user.getUid();

        readUserInfo(uid);

//        if (name == null) {
//            userName.setVisibility(View.GONE);
//        } else {
//            userName.setVisibility(View.VISIBLE);
//            userName.setText(name);
//        }

        edtUserEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.user_default).into(imvUserImage);
    }

    private void readUserInfo(String uid) {
        databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = String.valueOf(dataSnapshot.child("userName").getValue());
                        String address = String.valueOf(dataSnapshot.child("address").getValue());
                        String phone = String.valueOf(dataSnapshot.child("phone").getValue());
                        edtUserName.setText(name);
                        edtUserAddress.setText(address);
                        edtUserPhone.setText(phone);
                    }
                }
            }
        });
    }

    public void selectImageFromGallery() {
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imvUserImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imvUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}