package com.example.ilearning.view.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilearning.R;
import com.example.ilearning.adapter.CourseListAdapter;
import com.example.ilearning.model.CourseList;
import com.example.ilearning.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CourseListPelatihActivity extends AppCompatActivity {

    CourseListAdapter courseAdapter;
    FirebaseFirestore db;
    StorageReference storage;
    RecyclerView recyclerView;

    public CourseListPelatihActivity(){
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_pelatih);

        goBack();
        changeColorStatusBar();
        getData();
        addData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    public void getData(){
        db = FirebaseFirestore.getInstance();
        ArrayList<CourseList> courseList = new ArrayList<CourseList>();
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseAdapter = new CourseListAdapter(courseList, null);

        db.collection("Kursus")
//                .orderBy("created_at", Query.Direction.ASCENDING)
                .whereEqualTo("pelatih_id", db.document("Users/"+User.getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                storage.child("course_image/"+d.getString("image")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        courseList.add(new CourseList(d.getId(), d.getString("title"), "10 video pelajaran", uri.toString()));
                                        recyclerView.setAdapter(new CourseListAdapter(courseList, new CourseListAdapter.OnItemClickListener() {
                                            @Override public void onItemClick(CourseList item) {
                                                CourseSheet sheet = new CourseSheet(CourseListPelatihActivity.this, d.getId(), d.getString("title"));
                                                sheet.show(getSupportFragmentManager(), "TAG");
                                            }
                                        }));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(CourseListPelatihActivity.this, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(CourseListPelatihActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseListPelatihActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
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

    public void addData(){
        ImageButton add = findViewById(R.id.plus_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListPelatihActivity.this, CourseManageActivity.class);
                startActivity(intent);
            }
        });
    }
}