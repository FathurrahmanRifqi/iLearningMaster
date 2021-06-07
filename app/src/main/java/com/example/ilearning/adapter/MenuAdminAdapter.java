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

import com.example.ilearning.R;
import com.example.ilearning.model.MenuAdmin;
import java.util.List;

public class MenuAdminAdapter extends RecyclerView.Adapter<MenuAdminAdapter.ViewHolder>{

    List<MenuAdmin> models;

    public MenuAdminAdapter(List<MenuAdmin> models){
        this.models = models;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView subtitle;
        public CardView card;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.icon = itemView.findViewById(R.id.icon);
            this.title = itemView.findViewById(R.id.title);
            this.subtitle = itemView.findViewById(R.id.subtitle);
            card = itemView.findViewById(R.id.card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageResource(models.get(position).getIcon());
        holder.title.setText(models.get(position).getTitle());
        holder.subtitle.setText(models.get(position).getSubtitle());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class<?> c = Class.forName(String.valueOf(models.get(position).getActivity()));
                    Intent intent = new Intent(holder.context, c);
                    holder.context.startActivity(intent);
                } catch (ClassNotFoundException ignored) {
                    Log.e("ClassNotFoundException", "Error : "+ignored);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
