package com.example.ilearning.view.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.ilearning.R;
import com.example.ilearning.component.ProgressButton;
import com.example.ilearning.model.CategoryList;
import com.example.ilearning.model.CourseList;
import com.example.ilearning.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CourseManageActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageButton image;
    Spinner kategori;
    TextView title;

    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;

    public CourseManageActivity(){
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manage);
        changeColorStatusBar();
        goBack();
        setData();
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        filePath
                );
                image.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setData(){

        List<CategoryList> kategoriArray = new ArrayList<>();
        kategori = findViewById(R.id.kategori);

        TextView progress = findViewById(R.id.textProgress);
        progress.setText("Simpan");

        db.collection("Kategori").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                kategoriArray.add(new CategoryList(d.getId(), d.getString("nama_kategori")));
                            }

                            ArrayAdapter<CategoryList> kategoriAdapter = new ArrayAdapter<CategoryList>(CourseManageActivity.this,R.layout.support_simple_spinner_dropdown_item,kategoriArray);
                            kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            kategori.setAdapter(kategoriAdapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseManageActivity.this, "Gagal mengambil data kategori.", Toast.LENGTH_SHORT).show();
            }
        });

        image = findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        CardView button_loading = findViewById(R.id.button_loading);
        button_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(CourseManageActivity.this, view);
            }
        });
    }

    public boolean validate(){
        title = findViewById(R.id.title);
        if(filePath == null){
            Toast.makeText(CourseManageActivity.this, "Gambar belum dipilih.", Toast.LENGTH_SHORT).show();
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
            String imageName = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("course_image/"+ imageName);
            ref.putFile(filePath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            addData(imageName);
                            progressButton.stop();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(CourseManageActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void addData(String image){

        title = findViewById(R.id.title);
        kategori = findViewById(R.id.kategori);
        CategoryList list_kategori = (CategoryList) kategori.getSelectedItem();

        Map data = new HashMap<>();
        data.put("pelatih_id", db.document("Users/" + User.getUid()));
        data.put("title", title.getText().toString());
        data.put("image", image);
        data.put("kategori_id", list_kategori.getId());
        data.put("created_at", new Timestamp(new Date()));

        db.collection("Kursus").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CourseManageActivity.this, "Berhasil menambahkan kursus",Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseManageActivity.this,"Gagal menambahkan kursus",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
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