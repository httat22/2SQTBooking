package com.example.abc.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    private Button btnLogout, btnUpdate;
    private ImageView userImage;
    private TextView userName, userEmail;
    private Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName.getText().toString().trim())
                .setPhotoUri(imageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Update profile success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void initUI(View view) {
        btnLogout = view.findViewById(R.id.btn_logout);
        btnUpdate = view.findViewById(R.id.btn_update);
        userEmail = view.findViewById(R.id.userEmail);
        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null) {
            userName.setVisibility(View.GONE);
        } else {
            userName.setVisibility(View.VISIBLE);
            userName.setText(name);
        }

        userEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.ic_baseline_person).into(userImage);
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
                            userImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}