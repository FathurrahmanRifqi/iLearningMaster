package com.example.ilearning.repositories;

import android.app.Activity;
import android.content.Context;

import com.example.ilearning.interfaces.BaseInterface;
import com.example.ilearning.interfaces.UserInterface;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserRepository implements BaseInterface, UserInterface {

    FirebaseRepository db;

    public UserRepository(){
        this.db = new FirebaseRepository();
    }

    @Override
    public void signInWithEmailAndPassword(Activity activity, Context ctx, String email, String password) {
        db.signInWithEmailAndPassword(activity, ctx, email, password);
    }

    @Override
    public Task<DocumentSnapshot> first(String uid){
        return db.firstData("Users", uid);
    }

    public void store(){

    }
}
