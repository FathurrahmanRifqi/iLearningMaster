package com.example.ilearning.view.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.ilearning.R;
import com.example.ilearning.adapter.UserAdapter;
import com.example.ilearning.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        goBack();
        changeColorStatusBar();
        bottomSheet();

        ArrayList<User> users = new ArrayList<User>();
        users.add(new User("1","sigit1418@gmail.com", "Karyawan", "Sigit Ari Prasetyo", AvatarGenerator.Companion.avatarImage(this,200,AvatarConstants.Companion.getCIRCLE(),"Sigit")));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        UserAdapter adapter = new UserAdapter(users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void bottomSheet(){
        ImageButton add = findViewById(R.id.plus_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersSheet sheet = new UsersSheet(UsersListActivity.this, "Tambah Pengguna", "Simpan");
                sheet.show(getSupportFragmentManager(), "TAG");
            }
        });
    }

    public void goBack(){
        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void changeColorStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorDefault));
        }
    }
}