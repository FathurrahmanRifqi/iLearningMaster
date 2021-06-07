package com.example.ilearning.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface BaseInterface {

    public Task<DocumentSnapshot> first(String uid);
}
