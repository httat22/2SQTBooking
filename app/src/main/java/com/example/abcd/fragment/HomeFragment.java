package com.example.abcd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.abcd.R;
import com.example.abcd.activities.ReserveFastBookingActivity;
import com.example.abcd.activities.ReserveGolfActivity;
import com.example.abcd.activities.ReserveGymActivity;
import com.example.abcd.activities.RoomTypeListActivity;
import com.example.abcd.activities.ReserveTennisActivity;

public class HomeFragment extends Fragment {
    private CardView cvFastBooking, cvGym, cvTennis, cvRoomReserve, cvGolf;
    private ImageView imgLogo;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cvFastBooking = root.findViewById(R.id.cv_fastBooking);
        cvGym = root.findViewById(R.id.cv_gym);
        cvTennis = root.findViewById(R.id.cv_tennis);
        cvRoomReserve = root.findViewById(R.id.cv_roomReserve);
        cvGolf = root.findViewById(R.id.cv_golf);
        imgLogo = root.findViewById(R.id.imgLogo);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new MapFragment());
        fragmentTransaction.commit();
        onClickListener();

        return root;
    }
    private void onClickListener() {
        cvRoomReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RoomTypeListActivity.class);
                startActivity(intent);
            }
        });

        cvGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReserveGymActivity.class);
                startActivity(intent);
            }
        });
        cvTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReserveTennisActivity.class);
                startActivity(intent);
            }
        });
        cvGolf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReserveGolfActivity.class);
                startActivity(intent);
            }
        });
        cvFastBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReserveFastBookingActivity.class);
                startActivity(intent);
            }
        });
    }
}