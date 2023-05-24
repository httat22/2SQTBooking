package com.example.abc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.adapters.HistoryPaymentAdapter;
import com.example.abc.adapters.ReserveRoomAdapter;
import com.example.abc.models.ReserveUserModel;
import com.example.abc.models.TicketModel;
import com.example.abc.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManagerDetailUserActivity extends AppCompatActivity {
    private UserModel userModel;
    private ImageView imageUser, btnBack;
    private TextView tvNameUser, tvIdUser, tvEmail, tvPhone, tvAddress, tvTotalPayment;
    private RecyclerView recyclerView;
    private List<TicketModel> ticketModelList;
    private HistoryPaymentAdapter historyPaymentAdapter;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_staying");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_user_information_manager);
        getSupportActionBar().hide();

        imageUser = findViewById(R.id.imageUser);
        tvNameUser = findViewById(R.id.tvNameUser);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerDetailUserActivity.super.onBackPressed();
            }
        });
        getData();

        getHistoryPayment();
    }

    private void getHistoryPayment() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ticketModelList = new ArrayList<>();
        getDataFromRealTimeDatabase();
        historyPaymentAdapter = new HistoryPaymentAdapter(this, ticketModelList);
        recyclerView.setAdapter(historyPaymentAdapter);
    }

    private void getDataFromRealTimeDatabase() {
        databaseReference.child(userModel.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPayment = 0;
                if (ticketModelList != null) {
                    ticketModelList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TicketModel ticketModel = dataSnapshot.getValue(TicketModel.class);
                    assert ticketModel != null;
                    totalPayment += ticketModel.getPrice();
                    ticketModelList.add(ticketModel);
                }
                tvTotalPayment.setText("$" + totalPayment);
                historyPaymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        userModel = (UserModel) bundle.get("object_userModel");
        if (!userModel.getImageURL().equals("")) {
            Glide.with(this).load(userModel.getImageURL()).into(imageUser);
        }
        else {
            imageUser.setImageResource(R.drawable.user_default);
        }
        tvNameUser.setText(userModel.getUserName());
        tvEmail.setText(userModel.getEmail());
        tvIdUser.setText(userModel.getUserId());
        tvPhone.setText(userModel.getPhone());
        tvAddress.setText(userModel.getAddress());
    }
}