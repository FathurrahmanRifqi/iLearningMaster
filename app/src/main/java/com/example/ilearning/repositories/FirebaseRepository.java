package com.example.ilearning.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ilearning.MainActivity;
import com.example.ilearning.component.LoadingDialog;
import com.example.ilearning.utils.Session;
import com.example.ilearning.view.admin.AdminHomeActivity;
import com.example.ilearning.view.karyawan.KaryawanHomeActivity;
import com.example.ilearning.view.pelatih.PelatihHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository{

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Session session;

    public FirebaseRepository(){
        this.fAuth = FirebaseAuth.getInstance();
        this.fStore = FirebaseFirestore.getInstance();
        this.session = new Session();
    }

    public void signInWithEmailAndPassword(Activity activity, Context ctx, String email, String password){

        final LoadingDialog loading = new LoadingDialog(activity);
        loading.start();

        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkLevel(loading, ctx, authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.stop();
                Toast toast = Toast.makeText(ctx, "Username atau password salah", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void checkLevel(LoadingDialog loading, Context ctx, String uid) {
        session.getDetail().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                session.setDetail(ctx);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.stop();
                Toast toast = Toast.makeText(ctx, "Terjadi kesalahan", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public Task<DocumentSnapshot> firstData(String collection, String uid){
        DocumentReference df = fStore.collection(collection).document(uid);
        return df.get();
    }
}
