package com.example.abc.fragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.abc.R;
import com.example.abc.activities.ManagerListRoomActivity;
import com.example.abc.activities.ManagerListUserActivity;
import com.example.abc.activities.ManagerReservedRoomActivity;
import com.example.abc.databinding.FragmentHomeManagerBinding;

public class HomeManagerFragment extends Fragment {

    private FragmentHomeManagerBinding binding;
    private CardView cvReserveUserList, cvReserveRoomList, cvChangeRoomList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeManagerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        cvReserveUserList = view.findViewById(R.id.cvReserveUserList);
        cvReserveRoomList = view.findViewById(R.id.cvReserveRoomList);
        cvChangeRoomList = view.findViewById(R.id.cvChangeRoomList);
        
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

        return view;
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