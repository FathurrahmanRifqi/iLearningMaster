package com.example.ilearning.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ilearning.MainActivity;
import com.example.ilearning.R;
import com.example.ilearning.component.ProgressButton;
import com.example.ilearning.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorDefault));
        }

        TextView welcome = findViewById(R.id.name);
        welcome.setText(User.getName());

        ImageView profile = findViewById(R.id.image);
        profile.setImageDrawable(User.getImage());

        TextView textProgress = findViewById(R.id.textProgress);
        textProgress.setText("Logout");

        CardView btnLogout = findViewById(R.id.button_loading);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressButton progressButton = new ProgressButton(ProfileActivity.this, view);
                progressButton.start();

                FirebaseAuth.getInstance().signOut();

                progressButton.stop();

                Intent login = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(login);
            }
        });
    }
}