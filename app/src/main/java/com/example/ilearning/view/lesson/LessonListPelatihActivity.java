package com.example.ilearning.view.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilearning.R;
import com.example.ilearning.adapter.CourseListAdapter;
import com.example.ilearning.adapter.LessonAdapter;
import com.example.ilearning.model.CourseList;
import com.example.ilearning.model.Lesson;
import com.example.ilearning.view.course.CourseListPelatihActivity;
import com.example.ilearning.view.course.CourseManageActivity;
import com.example.ilearning.view.course.CourseSheet;
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

public class LessonListPelatihActivity extends AppCompatActivity {

    String courseID;
    String courseTitle;
    LessonAdapter lessonAdapter;
    FirebaseFirestore db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list_pelatih);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseTitle = intent.getStringExtra("courseTitle");
        changeColorStatusBar();
        goBack();
        addData();
        initialize();
    }

    public void initialize(){
        Intent intent = getIntent();
        TextView title = findViewById(R.id.title);
        title.setText(courseTitle);

        db = FirebaseFirestore.getInstance();
        ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonAdapter = new LessonAdapter(lessonList, null);
        db.collection("Pelajaran")
//                .orderBy("created_at", Query.Direction.ASCENDING)
                .whereEqualTo("kursus_id", db.document("Kursus/"+courseID))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                lessonList.add(new Lesson(d.getId(), d.getDocumentReference("kursus_id"), d.getString("title"), d.getString("video"), d.getString("duration"), d.getTimestamp("created_at")));
                                recyclerView.setAdapter(new LessonAdapter(lessonList, new LessonAdapter.OnItemClickListener() {
                                    @Override public void onItemClick(Lesson item) {
                                        LessonSheet sheet = new LessonSheet(LessonListPelatihActivity.this, d.getId(), d.getString("title"), d.getString("video"), courseTitle);
                                        sheet.show(getSupportFragmentManager(), "TAG");
                                    }
                                }));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LessonListPelatihActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addData(){
        ImageButton add = findViewById(R.id.plus_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonListPelatihActivity.this, LessonManageActivity.class);
                intent.putExtra("courseID", courseID);
                intent.putExtra("courseTitle", courseTitle);
                startActivity(intent);
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorMidnightBlue));
        }
    }
}