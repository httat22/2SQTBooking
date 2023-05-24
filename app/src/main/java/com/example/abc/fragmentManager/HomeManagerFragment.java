package com.example.abc.fragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.abc.R;
import com.example.abc.activities.ManagerListRoomActivity;
import com.example.abc.activities.ManagerListUserActivity;
import com.example.abc.activities.ManagerReservedRoomActivity;
import com.example.abc.databinding.FragmentHomeManagerBinding;
import com.example.abc.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeManagerFragment extends Fragment {

    private FragmentHomeManagerBinding binding;
    private CardView cvReserveUserList, cvReserveRoomList, cvChangeRoomList;
    private TextView tvCurrentSignIn;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeManagerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        cvReserveUserList = view.findViewById(R.id.cvReserveUserList);
        cvReserveRoomList = view.findViewById(R.id.cvReserveRoomList);
        cvChangeRoomList = view.findViewById(R.id.cvChangeRoomList);
        tvCurrentSignIn = view.findViewById(R.id.tvCurrentSignIn);

        cvChangeRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangeRoomList();
            }
        });
        cvReserveRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickReserveRoomList();
            }
        });
        cvReserveUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickReserveUserList();
            }
        });

        currentSignIn();

        return view;
    }

    private void currentSignIn() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalUser = 0;
                int totalUserIsLogin = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        if (userModel.getIsLogin()) {
                            totalUserIsLogin++;
                        }
                        totalUser++;
                    }
                }
                String s = totalUserIsLogin + "/" + totalUser;
                tvCurrentSignIn.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickReserveUserList() {
        Intent intent = new Intent(getContext(), ManagerListUserActivity.class);
        startActivity(intent);
    }

    private void onClickReserveRoomList() {
        Intent intent = new Intent(getContext(), ManagerReservedRoomActivity.class);
        startActivity(intent);
    }

    private void onClickChangeRoomList() {
        Intent intent = new Intent(getContext(), ManagerListRoomActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}