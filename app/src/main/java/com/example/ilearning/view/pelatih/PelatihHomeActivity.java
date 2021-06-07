package com.example.ilearning.view.pelatih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ilearning.R;
import com.example.ilearning.adapter.MenuAdminAdapter;
import com.example.ilearning.model.MenuAdmin;
import com.example.ilearning.model.User;
import com.example.ilearning.view.ProfileActivity;
import com.example.ilearning.view.admin.AdminHomeActivity;

import java.util.ArrayList;

public class PelatihHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelatih_home);

        setContentView(R.layout.activity_admin_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorDefault));
        }

        TextView welcome = findViewById(R.id.text_welcome);
        welcome.setText(String.format("Welcome\n%s", User.firstName()));

        ImageView profile = findViewById(R.id.menu_profile);
        profile.setImageDrawable(User.getImage());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(PelatihHomeActivity.this, ProfileActivity.class);
                startActivity(profilePage);
            }
        });

        ArrayList<MenuAdmin> menuAdmins = new ArrayList<MenuAdmin>();
        menuAdmins.add(new MenuAdmin(R.drawable.swingvy_all_illustrations, "Kursus", "Klik untuk mengelola kursus", "com.example.ilearning.view.course.CourseListPelatihActivity"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_recycler);
        MenuAdminAdapter adapter = new MenuAdminAdapter(menuAdmins);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}