package com.example.abcd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abcd.R;
import com.example.abcd.models.TicketModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    FragmentActivity context;
    List<TicketModel> list;

    public NotificationAdapter() {
    }
    public NotificationAdapter(FragmentActivity context, List<TicketModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticketModel = list.get(position);
        if (ticketModel == null) return;
        String s1;
        if (ticketModel.getRoomId() != null) {
            s1 = ticketModel.getUserName() + " has order room " + ticketModel.getRoomId() + " successfully";
        } else {
            s1 = ticketModel.getUserName() + " has order " + ticketModel.getNameType() + " successfully";
        }

        String s2 = "#ID: " + ticketModel.getUserId();

        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = " " + sdf.format(date);

        holder.notification.setText(s1);
        holder.idUser.setText(s2);
        holder.timeBooked.setText(formattedDate);
        Glide.with(context).load(ticketModel.getImageURL()).into(holder.imageNotification);
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification, idUser, timeBooked;
        ImageView imageNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
            idUser = itemView.findViewById(R.id.idUser);
            timeBooked = itemView.findViewById(R.id.timeBooked);
            imageNotification = itemView.findViewById(R.id.imageNotification);
        }
    }
}
