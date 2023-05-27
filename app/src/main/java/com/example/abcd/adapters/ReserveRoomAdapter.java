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
import com.example.abcd.activities.ManagerDetailReservedRoomActivity;
import com.example.abcd.models.TicketModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReserveRoomAdapter extends RecyclerView.Adapter<ReserveRoomAdapter.ViewHolder> {

    Context context;
    List<TicketModel> list;

    public ReserveRoomAdapter() {
    }

    public ReserveRoomAdapter(Context context, List<TicketModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserving_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticketModel = list.get(position);
        if (ticketModel == null) return;

        String s1 = "Room " +  ticketModel.getDescription();
        String s2 = "Payment total: " + ticketModel.getPrice() + "$";
        String s3 = "Renter: " + ticketModel.getUserName();

        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = " " + sdf.format(date);

        holder.tvRoomTitle.setText(s1);
        holder.tvPrice.setText(s2);
        holder.tvNameUser.setText(s3);
        holder.tvTime.setText(formattedDate);
        Glide.with(context).load(ticketModel.getImageURL()).into(holder.imageRoom);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickGoToDetail(ticketModel);
            }
        });
    }

    private void onCLickGoToDetail(TicketModel ticketModel) {
        Intent intent = new Intent(context, ManagerDetailReservedRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_ticketModel", ticketModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRoom;
        TextView tvRoomTitle, tvPrice, tvNameUser, tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRoom = itemView.findViewById(R.id.imageRoom);
            tvRoomTitle = itemView.findViewById(R.id.tvRoomTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
