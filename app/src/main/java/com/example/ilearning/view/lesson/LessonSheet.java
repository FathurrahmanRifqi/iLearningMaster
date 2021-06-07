package com.example.ilearning.view.lesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.ilearning.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.annotation.Nullable;

public class LessonSheet extends BottomSheetDialogFragment {

    private Context context;
    private String lessonID;
    private String lessonTitle;
    private String lessonVideo;
    private String courseTitle;

    public LessonSheet(Context context, String lessonID, String lessonTitle, String lessonVideo, String courseTitle){
        this.context = context;
        this.lessonID = lessonID;
        this.lessonTitle = lessonTitle;
        this.lessonVideo = lessonVideo;
        this.courseTitle = courseTitle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sheet_lesson, container, false);

        RelativeLayout btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LessonManageActivity.class);
                intent.putExtra("lessonID", lessonID);
                intent.putExtra("lessonTitle", lessonTitle);
                intent.putExtra("lessonVideo", lessonVideo);
                intent.putExtra("courseTitle", courseTitle);
                startActivity(intent);
            }
        });

        return view;
    }
}
