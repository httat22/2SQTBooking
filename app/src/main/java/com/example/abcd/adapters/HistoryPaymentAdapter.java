package com.example.abcd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abcd.R;
import com.example.abcd.models.TicketModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryPaymentAdapter extends RecyclerView.Adapter<HistoryPaymentAdapter.ViewHolder> {
    Context context;
    List<TicketModel> list;

    public HistoryPaymentAdapter() {
    }

    public HistoryPaymentAdapter(Context context,  List<TicketModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ntt_cardview_history_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticketModel = list.get(position);
        if (ticketModel == null) return;

        String nameRoom;
        if (ticketModel.getDescription().equals("")) {
            nameRoom = "Booking " + ticketModel.getNameType();
        } else {
            nameRoom = "Booking room " + ticketModel.getDescription();
        }
        long timeInMilliseconds = ticketModel.getDateBooked();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        String payment = "$" + ticketModel.getPrice();

        holder.tvNameRoom.setText(nameRoom);
        holder.tvPayment.setText(payment);
        holder.tvTime.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameRoom,tvTime,tvPayment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameRoom = itemView.findViewById(R.id.tvNameRoom);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPayment = itemView.findViewById(R.id.tvPayment);
        }
    }
}
