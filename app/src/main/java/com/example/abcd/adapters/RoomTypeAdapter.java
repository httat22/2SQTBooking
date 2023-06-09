package com.example.abcd.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abcd.R;
import com.example.abcd.activities.BookRoomDetailActivity;
import com.example.abcd.models.RoomTypeModel;

import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.ViewHolder> {
    Context context;
    List<RoomTypeModel> list;

    public RoomTypeAdapter() {}
    public RoomTypeAdapter(Context context, List<RoomTypeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RoomTypeModel roomTypeModel = list.get(position);
        if (roomTypeModel == null) return;

        String price = roomTypeModel.getPrice() + "$/night";
        Glide.with(context).load(roomTypeModel.getImageURL()).into(holder.imageView);
        String nameRoom = "Room " + roomTypeModel.getRoom() + " - " + roomTypeModel.getRoomType();
        holder.tvTypeRoom.setText(nameRoom);
        holder.tvPrice.setText(price);
        holder.tvDescription.setText(roomTypeModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickGoToDetail(roomTypeModel);
            }
        });
    }

    private void onCLickGoToDetail(RoomTypeModel roomTypeModel) {
        Intent intent = new Intent(context, BookRoomDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_roomTypeModel", roomTypeModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void release() {
        context = null;
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTypeRoom, tvPrice, tvDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageRoomItem);
            tvTypeRoom = itemView.findViewById(R.id.tvTypeRoom);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
