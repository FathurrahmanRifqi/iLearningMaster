package com.example.ilearning.component;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ilearning.R;

public class ProgressButton {

    private CardView button_loading;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private TextView textProgress;

    Animation fade_in;

    public ProgressButton(Context context, View view){
        button_loading = view.findViewById(R.id.button_loading);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        progressBar = view.findViewById(R.id.progressBar);
        textProgress = view.findViewById(R.id.textProgress);
    }

    public void start(){
        progressBar.setVisibility(View.VISIBLE);
        textProgress.setVisibility(View.GONE);
    }

    public void stop(){
        progressBar.setVisibility(View.GONE);
        textProgress.setVisibility(View.VISIBLE);
    }
}
