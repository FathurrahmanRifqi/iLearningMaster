package com.example.ilearning.view.karyawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ilearning.R;
import com.example.ilearning.adapter.CategoryListAdapter;
import com.example.ilearning.adapter.CourseListAdapter;
import com.example.ilearning.adapter.LessonAdapter;
import com.example.ilearning.model.CategoryList;
import com.example.ilearning.model.CourseList;
import com.example.ilearning.model.Lesson;
import com.example.ilearning.model.User;
import com.example.ilearning.view.course.CourseListActivity;
import com.example.ilearning.view.course.CourseListPelatihActivity;
import com.example.ilearning.view.course.CourseSheet;
import com.example.ilearning.view.lesson.LessonListActivity;
import com.example.ilearning.view.lesson.LessonListPelatihActivity;
import com.example.ilearning.view.lesson.LessonSheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class KaryawanHomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    CategoryListAdapter adapter;
    List<CategoryList> models;
    CourseListAdapter courseAdapter;
    FirebaseFirestore db;
    StorageReference storage;

    public KaryawanHomeActivity(){
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_home);

        models = new ArrayList<>();
        adapter = new CategoryListAdapter(models, this);

        db = FirebaseFirestore.getInstance();
        db.collection("Kategori")
//                .orderBy("created_at", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                storage.child("category_image/"+d.getString("image")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        models.add(new CategoryList(d.getId(), uri.toString(), d.getString("nama_kategori"), d.getString("deskripsi")));
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(KaryawanHomeActivity.this, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KaryawanHomeActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
            }
        });

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);


        ArrayList<CourseList> courseList = new ArrayList<CourseList>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseListAdapter(courseList, null);

        db.collection("KursusKaryawan")
                .whereEqualTo("karyawan_id", User.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("TAG", "queryDocumentSnapshots : " + queryDocumentSnapshots.isEmpty());
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                db.collection("Kursus").document(d.getString("kursus_id"))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if (documentSnapshot.exists()) {

                                            DocumentSnapshot item = documentSnapshot;
                                            storage.child("course_image/" + item.getString("image")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    db.collection("Pelajaran")
                                                            .whereEqualTo("kursus_id", db.document("Kursus/"+d.getString("kursus_id")))
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    List<DocumentSnapshot> pelajaran = queryDocumentSnapshots.getDocuments();
                                                                    Log.d("TAG", "pelajaran : "+pelajaran.size());
                                                                    courseList.add(new CourseList(item.getId(), item.getString("title"), pelajaran.size()+" video pelajaran", uri.toString()));

                                                                    recyclerView.setAdapter(new CourseListAdapter(courseList, new CourseListAdapter.OnItemClickListener() {
                                                                        @Override public void onItemClick(CourseList course) {
                                                                            Intent intent = new Intent(getApplicationContext(), LessonListActivity.class);
                                                                            intent.putExtra("courseID", item.getId());
                                                                            intent.putExtra("courseTitle", item.getString("title"));
                                                                            startActivity(intent);
                                                                        }
                                                                    }));
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Log.d("TAG", "Gagal mengambil gambar : " + item.getId());
                                                    Toast.makeText(KaryawanHomeActivity.this, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                                    }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(KaryawanHomeActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(KaryawanHomeActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
        });

    }
}