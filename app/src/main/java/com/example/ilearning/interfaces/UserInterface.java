package com.example.ilearning.interfaces;

import android.app.Activity;
import android.content.Context;

public interface UserInterface
{
    public void signInWithEmailAndPassword(Activity activity, Context ctx, String email, String password);

}
