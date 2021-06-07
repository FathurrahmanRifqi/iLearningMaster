package com.example.ilearning.view.users;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.ilearning.R;
import com.example.ilearning.component.ProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class UsersSheet extends BottomSheetDialogFragment {

    private Context context;
    private String title;
    private String button;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;

    public UsersSheet(Context context, String title, String button){
        this.context = context;
        this.title = title;
        this.button = button;
        this.db = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sheet_user, container, false);

        TextView titleSheet = view.findViewById(R.id.titleSheet);
        titleSheet.setText(title);

        TextView progress = view.findViewById(R.id.textProgress);
        progress.setText(button);

        List<String> levelArray = new ArrayList<>();
        levelArray.add("Admin");
        levelArray.add("Pelatih");
        levelArray.add("Karyawan");

        Spinner level = view.findViewById(R.id.level);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,levelArray);
        level.setAdapter(levelAdapter);

        CardView button_loading = view.findViewById(R.id.button_loading);
        button_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(context, view);
            }
        });

        return view;
    }

    public void saveData(Context context, View view){
        final ProgressButton progressButton = new ProgressButton(context, view);
        progressButton.start();

        EditText name = view.findViewById(R.id.name);
        EditText email = view.findViewById(R.id.email);
        EditText password = view.findViewById(R.id.password);
        Spinner level = view.findViewById(R.id.level);

        fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String id = authResult.getUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name.getText().toString());
                        user.put("email", email.getText().toString());
                        user.put("level",  level.getSelectedItem().toString());

                        db.collection("Users").document(id)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressButton.stop();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast toast = Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                    }
                });
    }
}
