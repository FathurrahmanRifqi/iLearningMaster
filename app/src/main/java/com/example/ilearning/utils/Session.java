package com.example.ilearning.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.ilearning.model.User;
import com.example.ilearning.view.admin.AdminHomeActivity;
import com.example.ilearning.view.karyawan.KaryawanHomeActivity;
import com.example.ilearning.view.pelatih.PelatihHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Session {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String name;
    String email;
    String level;
    int image;

    public Session(){
        this.fAuth = FirebaseAuth.getInstance();
        this.fStore = FirebaseFirestore.getInstance();
    }

    public boolean checkSession(){
        return fAuth.getCurrentUser() == null ? false : true;
    }

    public Task<DocumentSnapshot> getDetail(){
        DocumentReference df = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        return df.get();
    }

    public void setDetail(Context context){
        getDetail().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("name") != null){
                    User user = new User(
                            fAuth.getUid(),
                            fAuth.getCurrentUser().getEmail(),
                            documentSnapshot.getString("level"),
                            documentSnapshot.getString("name"),
                            AvatarGenerator.Companion.avatarImage(
                                    context,
                                    200,
                                    AvatarConstants.Companion.getCIRCLE(),
                                    documentSnapshot.getString("name")
                            )
                    );
                }

                switch (documentSnapshot.getString("level")) {
                    case "Admin": {
                        Intent intent = new Intent(context, AdminHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    }
                    case "Karyawan": {
                        Intent intent = new Intent(context, KaryawanHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    }
                    case "Pelatih": {
                        Intent intent = new Intent(context, PelatihHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    }
                }
            }
        });
    }
}
