package com.example.ilearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ilearning.R;
import com.example.ilearning.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    List<User> models;

    public UserAdapter(List<User> models){
        this.models = models;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public TextView level;
        public TextView name;
        public ImageView image;
        public CardView card;
        public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.email = itemView.findViewById(R.id.email);
            this.level = itemView.findViewById(R.id.level);
            this.name = itemView.findViewById(R.id.name);
            this.image = itemView.findViewById(R.id.image);
            this.card = itemView.findViewById(R.id.card);
        }
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.email.setText(models.get(position).getEmail());
        holder.level.setText(models.get(position).getLevel());
        holder.name.setText(models.get(position).getName());
        Glide.with(holder.image)
                .asBitmap()
                .load("https://github.com/AmosKorir/AvatarImageGenerator/blob/master/art/Screen2.png")
                .placeholder(models.get(position).getImage())
                .into(holder.image);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    Class<?> c = Class.forName(String.valueOf(models.get(position).getActivity()));
//                    Intent intent = new Intent(holder.context, c);
//                    holder.context.startActivity(intent);
//                } catch (ClassNotFoundException ignored) {
//                    Log.e("ClassNotFoundException", "Error : "+ignored);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

