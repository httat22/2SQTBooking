package com.example.abcd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abcd.R;
import com.example.abcd.models.RoomTypeModel;

import java.util.List;

public class RoomManagerAdapter extends RecyclerView.Adapter<RoomManagerAdapter.ViewHolder> {

    Context context;
    List<RoomTypeModel> list;
    private IClickListener mIClickListener;
    public interface IClickListener {
        void onClickUpdateItem(RoomTypeModel roomTypeModel);
        void onClickDeleteItem(RoomTypeModel roomTypeModel);
    }

    public RoomManagerAdapter() {}
    public RoomManagerAdapter(Context context, List<RoomTypeModel> list, IClickListener listener) {
        this.context = context;
        this.list = list;
        this.mIClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_room_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomTypeModel roomTypeModel = list.get(position);
        if (roomTypeModel == null) return;

        String price = roomTypeModel.getPrice() + "$/night";
        String room = "Room " + roomTypeModel.getRoom() + " - " + roomTypeModel.getRoomType();
        Glide.with(context).load(roomTypeModel.getImageURL()).into(holder.imageRoomItem);
        holder.tvTypeRoom.setText(room);
        holder.tvPrice.setText(price);
        holder.tvDescription.setText(roomTypeModel.getDescription());

        holder.btnEditRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListener.onClickUpdateItem(roomTypeModel);
            }
        });
        holder.btnDeleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListener.onClickDeleteItem(roomTypeModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRoomItem;
        TextView tvTypeRoom, tvPrice, tvDescription;
        AppCompatButton btnDeleteRoom, btnEditRoom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRoomItem = itemView.findViewById(R.id.imageRoomItem);
            tvTypeRoom = itemView.findViewById(R.id.tvTypeRoom);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDeleteRoom = itemView.findViewById(R.id.btnDeleteRoom);
            btnEditRoom = itemView.findViewById(R.id.btnEditRoom);
        }
    }
}
