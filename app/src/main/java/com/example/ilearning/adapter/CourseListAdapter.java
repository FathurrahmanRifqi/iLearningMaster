package com.example.ilearning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ilearning.R;
import com.example.ilearning.model.CourseList;

import java.util.List;

import javax.annotation.Nullable;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder>{

    List<CourseList> models;
    private final OnItemClickListener listener;

    public CourseListAdapter(List<CourseList> models, @Nullable OnItemClickListener listener){
        this.models = models;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CourseList item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView courseImage;
        public TextView courseTitle;
        public TextView courseSubtitle;
        public String courseID;
        public CardView card;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.courseImage = itemView.findViewById(R.id.image);
            this.courseTitle = itemView.findViewById(R.id.title);
            this.courseSubtitle = itemView.findViewById(R.id.subtitle);
            card = itemView.findViewById(R.id.card);
        }

        public void bind(final CourseList item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_pelatih, parent, false);
        CourseListAdapter.ViewHolder viewHolder = new CourseListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseListAdapter.ViewHolder holder, int position) {
        Glide.with(holder.context).load(models.get(position).getCourseImage()).into(holder.courseImage);
        holder.courseTitle.setText(models.get(position).getCourseTitle());
        holder.courseSubtitle.setText(models.get(position).getCourseSubtitle());
        holder.bind(models.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
