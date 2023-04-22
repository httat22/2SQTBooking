package com.example.abc.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.adapters.RoomAdapter;
import com.example.abc.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class ListRoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<RoomModel> roomModelList;
    RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);

        recyclerView = findViewById(R.id.roomRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomModelList = new ArrayList<>();
        roomModelList.add(new RoomModel(R.drawable.gym1, "F", "1000"));
        roomModelList.add(new RoomModel(R.drawable.gym2, "F", "1000"));
        roomModelList.add(new RoomModel(R.drawable.gym3, "F", "1000"));
        roomModelList.add(new RoomModel(R.drawable.gym4, "F", "1000"));

        roomAdapter = new RoomAdapter(this, roomModelList);
        recyclerView.setAdapter(roomAdapter);
    }
}