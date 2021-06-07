package com.example.ilearning.model;

import android.graphics.drawable.Drawable;

public class User {

    private static String uid;
    private String email;
    private String level;
    private static String name;
    private static Drawable image;

    public User(String uid, String email, String level, String name, Drawable image) {
        this.uid = uid;
        this.email = email;
        this.level = level;
        this.name = name;
        this.image = image;
    }


    public static String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        User.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Drawable getImage(){
        return image;
    }

    public void setImage(Drawable image){
        this.image = image;
    }

    public static String firstName() {
        return getName().split(" ")[0]; // Create array of words and return the 0th word
    }
}
