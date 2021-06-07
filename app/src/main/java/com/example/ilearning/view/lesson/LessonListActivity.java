package com.example.ilearning.view.lesson;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilearning.R;
import com.example.ilearning.adapter.LessonAdapter;
import com.example.ilearning.model.Lesson;
import com.example.ilearning.model.User;
import com.example.ilearning.view.course.CourseManageActivity;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ilearning.utils.Helper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class LessonListActivity extends AppCompatActivity {

    String courseID;
    String courseTitle;
    LessonAdapter lessonAdapter;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    StorageReference storage;
    TextView title;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private String videoId= null;

    public LessonListActivity(){
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        changeColorStatusBar();
        goBack();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer2) {
                youTubePlayer = youTubePlayer2;
                setData();
            }
        });


    }

    protected void setData()
    {
        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseTitle = intent.getStringExtra("courseTitle");

        youTubePlayerView.setVisibility(View.GONE);
        title = findViewById(R.id.title);
        title.setText(courseTitle);
        title.setVisibility(View.VISIBLE);

        exoPlayerView = findViewById(R.id.idExoPlayerView);

        db = FirebaseFirestore.getInstance();
        ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonAdapter = new LessonAdapter(lessonList, null);
        db.collection("Pelajaran")
                .whereEqualTo("kursus_id", db.document("Kursus/"+courseID))
                .orderBy("created_at", Query.Direction.ASCENDING)
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
                                        showVideo(Helper.getYoutubeId(item.getVideo()), courseID);
                                    }
                                }));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LessonListActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showVideo(String video_id, String courseID){
        title.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        youTubePlayer.cueVideo(video_id, 0);

        db.collection("KursusKaryawan")
                .whereEqualTo("karyawan_id", User.getUid())
                .whereEqualTo("kursus_id",courseID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("karyawan_id", User.getUid());
                        data.put("kursus_id", courseID);
                        data.put("status",  "Progress");

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                db.collection("KursusKaryawan").document(d.getId()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LessonListActivity.this, "Berhasil update kursus",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            data.put("tanggal_mulai",  new Timestamp(new Date()));

                            db.collection("KursusKaryawan").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(LessonListActivity.this, "Berhasil menambahkan kursus",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LessonListActivity.this,"Gagal menambahkan kursus",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LessonListActivity.this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                });

//        String video_id = Helper.getYoutubeId(uri);
//        Log.d("Tag","video_id : "+video_id);
//        title.setVisibility(View.GONE);
//        YouTubePlayerView youtube_player_view = null;
//        youtube_player_view = findViewById(R.id.youtube_player_view);
//        youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                youTubePlayer.loadVideo(video_id, 0);
//            }
//
//
//        });
//        youtube_player_view.setVisibility(View.VISIBLE);
    }

    public void changeColorStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorDefault));
        }
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
}