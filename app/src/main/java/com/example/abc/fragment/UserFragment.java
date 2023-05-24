package com.example.abc.fragment;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.activities.LoginActivity;
import com.example.abc.activities.UserInfoActivity;
import com.example.abc.databinding.FragmentUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private TextView tvUserInfo, tvPayment, tvDeleteAccount, tvLogOut;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        initUI(root);

        onClickLogOut();
        onClickToUserInfoActivity();
        onClickDeleteAccount();

        return root;
    }

    private void onClickDeleteAccount() {
        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
                myDialog.setTitle("Delete account confirmation");
                myDialog.setMessage("Are you sure you want to delete account?");
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                        assert user != null;
                        String uid = user.getUid();
                        userRef.child(uid).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                                startActivity(intent);
                                                if (getActivity() != null) {
                                                    getActivity().finishAffinity();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });
    }

    private void onClickToUserInfoActivity() {
        tvUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserInfoActivity.class));
            }
        });
    }

    private void onClickLogOut() {
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
                myDialog.setTitle("Log out confirmation");
                myDialog.setMessage("Are you sure you want to log out?");
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        databaseReference.child(user.getUid()).child("isLogin").setValue(false);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finishAffinity();
                        }
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });
    }


    public void initUI(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        tvPayment = view.findViewById(R.id.tvPayment);
        tvDeleteAccount = view.findViewById(R.id.tvDeleteAccount);
        tvLogOut = view.findViewById(R.id.tvLogOut);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}