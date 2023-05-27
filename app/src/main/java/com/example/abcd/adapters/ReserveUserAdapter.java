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
import com.example.abcd.activities.ManagerDetailUserActivity;
import com.example.abcd.models.UserModel;

import java.util.List;

public class ReserveUserAdapter extends RecyclerView.Adapter<ReserveUserAdapter.ViewHolder> {
    Context context;
    List<UserModel> list;

    public ReserveUserAdapter() {
    }

    public ReserveUserAdapter(Context context, List<UserModel> list) {
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
        UserModel userModel = list.get(position);
        if (userModel == null) return;
        holder.tvName.setText(userModel.getUserName());
        holder.tvEmail.setText(userModel.getEmail());
        if (!userModel.getImageURL().equals("")) {
            Glide.with(context).load(userModel.getImageURL()).into(holder.imageUser);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(userModel);
            }
        });
    }

    private void onClickGoToDetail(UserModel userModel) {
        Intent intent = new Intent(context, ManagerDetailUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_userModel", userModel);
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
