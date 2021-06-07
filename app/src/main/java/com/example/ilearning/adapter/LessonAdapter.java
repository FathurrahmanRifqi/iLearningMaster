package com.example.ilearning.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilearning.R;
import com.example.ilearning.model.Lesson;

import java.util.List;

import javax.annotation.Nullable;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder>{

    List<Lesson> models;
    private final LessonAdapter.OnItemClickListener listener;

    public LessonAdapter(List<Lesson> models, @Nullable LessonAdapter.OnItemClickListener listener){
        this.models = models;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Lesson item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView duration;
        public TextView number;
        public CardView card;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.title = itemView.findViewById(R.id.title);
            this.number = itemView.findViewById(R.id.number);
            this.duration = itemView.findViewById(R.id.duration);
            card = itemView.findViewById(R.id.card);
        }

        public void bind(final Lesson item, final LessonAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list, parent, false);
        LessonAdapter.ViewHolder viewHolder = new LessonAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LessonAdapter.ViewHolder holder, int position) {
        Log.d("Tag"," position < 9 : "+ (position < 9));
        String number = position < 9 ? String.format("%02d", (position+1)) : Integer.toString(position+1);
        holder.number.setText(number);
        holder.title.setText(models.get(position).getTitle());
        holder.duration.setText(models.get(position).getDuration());
        holder.bind(models.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
