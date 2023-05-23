package com.example.abc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abc.R;
import com.example.abc.models.ReserveUserModel;
import com.example.abc.models.UserModel;

import java.util.List;

public class ReserveUserAdapter extends RecyclerView.Adapter<ReserveUserAdapter.ViewHolder> {
    Context context;
    List<ReserveUserModel> list;

    public ReserveUserAdapter() {
    }

    public ReserveUserAdapter(Context context, List<ReserveUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserving_user_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReserveUserModel userModel = list.get(position);
        if (userModel == null) return;
        holder.tvName.setText(userModel.getName());
        holder.tvEmail.setText(userModel.getEmail());
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser;
        TextView tvName, tvEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.imageUser);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
