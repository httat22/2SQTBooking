package com.example.abc.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abc.R;
import com.example.abc.models.TicketModel;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<TicketModel> list;
    FragmentActivity context;

    public TicketAdapter(List<TicketModel> list, FragmentActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staying, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticketModel = list.get(position);
        if (ticketModel == null) return;

        String s = ticketModel.getNumberPerson() + " Adult  -  " + ticketModel.getPrice() + "$";
        String time = ticketModel.getDateArrive() + " - " + ticketModel.getDateLeave();

        holder.tvNumberAndPrice.setText(s);
        holder.tvTime.setText(time);
//        if (ticketModel.getStatus().equals("checkIn")) {
//            holder.imageStatus.setVisibility(View.VISIBLE);
//        }
        if (!ticketModel.getNameType().equals("Booking Room")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E5EFCB"));
            String s2 = "Ticket: " + ticketModel.getNameType();
            holder.tvDescription.setText(s2);
        } else {
            String s2 = ticketModel.getNameType() + " " + ticketModel.getDescription();
            holder.tvDescription.setText(s2);
        }
        Glide.with(context).load(list.get(position).getImageURL()).into(holder.imageItem);
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageItem, imageStatus;
        private TextView tvNumberAndPrice, tvTime, tvDescription;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.imageItem);
            imageStatus = itemView.findViewById(R.id.imageStatus);
            tvNumberAndPrice = itemView.findViewById(R.id.tvNumberAndPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
