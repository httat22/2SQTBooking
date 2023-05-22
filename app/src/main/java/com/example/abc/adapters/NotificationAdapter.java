package com.example.abc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.models.TicketModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<TicketModel> list;

    public NotificationAdapter() {
    }

    public NotificationAdapter(Context context, List<TicketModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticketModel = list.get(position);
        if (ticketModel == null) return;
//        holder.notification.setText();
//        holder.idUser.setText();
//        holder.timeBooked.setText();
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification, idUser, timeBooked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
            idUser = itemView.findViewById(R.id.idUser);
            timeBooked = itemView.findViewById(R.id.timeBooked);
        }
    }
}
