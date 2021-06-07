package com.example.ilearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ilearning.component.LoadingDialog;
import com.example.ilearning.repositories.UserRepository;
import com.example.ilearning.utils.Session;
import com.example.ilearning.view.admin.AdminHomeActivity;
import com.example.ilearning.view.karyawan.KaryawanHomeActivity;
import com.example.ilearning.view.pelatih.PelatihHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    ImageButton login;
    boolean valid;
    Session session;

    public MainActivity(){
        this.session = new Session();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkField(email);
                checkField(password);

                if(valid){
                    UserRepository repository = new UserRepository();
                    repository.signInWithEmailAndPassword(MainActivity.this, getApplicationContext(), email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    public void checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Wajib Diisi !");
            valid = false;
        }else{
            valid = true;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            final LoadingDialog loading = new LoadingDialog(MainActivity.this);
            loading.start();
            session.setDetail(this);
        }
    }
}