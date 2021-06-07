package com.example.ilearning.view.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ilearning.R;
import com.example.ilearning.component.ProgressButton;
import com.example.ilearning.model.CategoryList;
import com.example.ilearning.utils.Helper;
import com.example.ilearning.view.course.CourseManageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class LessonManageActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    CardView btnUpload;
    VideoView video;

    private final int PICK_VIDEO_REQUEST = 100;
    private Uri filePath;
    private String courseID;
    private String courseTitle;
    private String durationVideo;
    private String videoName;
    private String lessonID;

    public LessonManageActivity(){
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_manage);
        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseTitle = intent.getStringExtra("courseTitle");

        TextView subtitle = findViewById(R.id.subtitle);
        subtitle.setText(courseTitle);

        goBack();
        changeColorStatusBar();
        setData();
    }

    public void setData(){
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectVideo();
            }
        });

        TextView progress = findViewById(R.id.textProgress);
        progress.setText("Simpan");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && intent.getStringExtra("lessonTitle") != null) {
            TextView title = findViewById(R.id.title);
            title.setText(intent.getStringExtra("lessonTitle"));
            lessonID = intent.getStringExtra("lessonID");

            ImageButton back_button = findViewById(R.id.back_button);
            back_button.setVisibility(View.GONE);

            TextView textTitle = findViewById(R.id.textTitle);
            textTitle.setVisibility(View.GONE);

            TextView subtitle = findViewById(R.id.subtitle);
            subtitle.setVisibility(View.GONE);

            if(!intent.getStringExtra("lessonVideo").equals(null)){
                MxVideoPlayerWidget videoPlayerWidget = (MxVideoPlayerWidget) findViewById(R.id.mp_video);
                videoPlayerWidget.startPlay(intent.getStringExtra("lessonVideo"), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "Pelajaran");
            }

        }

        CardView button_loading = findViewById(R.id.button_loading);
        button_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(LessonManageActivity.this, view);
            }
        });
    }

    private void selectVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select video from here..."),PICK_VIDEO_REQUEST);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
//            setVideo();
        }
    }

    public void setVideo(){
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setVisibility(View.GONE);

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setVisibility(View.GONE);

        TextView subtitle = findViewById(R.id.subtitle);
        subtitle.setVisibility(View.GONE);

//            final MediaController controller = new MediaController(LessonManageActivity.this);

        video = findViewById(R.id.video);
        video.setVisibility(View.VISIBLE);
        video.setVideoURI(filePath);
//            video.setMediaController(controller);
//            controller.setMediaPlayer(video);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                int duration = video.getDuration();
                durationVideo = Helper.convertMillieToHMmSs(duration);
                video.requestFocus();
                video.start();
            }
        });
    }

    public boolean validate(){
        TextView title = findViewById(R.id.title);
        if(filePath == null){
            Toast.makeText(LessonManageActivity.this, "Video belum dipilih.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(title.getText().toString().isEmpty()){
            title.setError("Judul belum diisi");
            return false;
        }

        return true;
    }

    public void saveData(Context context, View view){
        final ProgressButton progressButton = new ProgressButton(context, view);
        progressButton.start();

        if(validate()){
            videoName = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("course_video/"+ videoName);
            ref.putFile(filePath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            addData(progressButton);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(LessonManageActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            progressButton.stop();
        }
    }

    public void addData(ProgressButton progressButton){

        TextView title = findViewById(R.id.title);

        Map data = new HashMap<>();
        data.put("title", title.getText().toString());
        data.put("video", videoName);
        data.put("kursus_id", db.document("Kursus/"+courseID));
        data.put("duration", durationVideo);
        data.put("created_at", new Timestamp(new Date()));

        db.collection("Pelajaran").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressButton.stop();
                Toast.makeText(LessonManageActivity.this, "Berhasil menambahkan pelajaran",Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressButton.stop();
                Toast.makeText(LessonManageActivity.this,"Gagal menambahkan pelajaran",Toast.LENGTH_SHORT).show();
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