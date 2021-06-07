package com.example.ilearning.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Lesson {
    private String id;
    private DocumentReference kursus_id;
    private String title;
    private String video;
    private String duration;
    private Timestamp created_at;

    public Lesson(String id, DocumentReference kursus_id, String title, String video, String duration, Timestamp created_at) {
        this.id = id;
        this.kursus_id = kursus_id;
        this.title = title;
        this.video = video;
        this.duration = duration;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getKursusId() {
        return kursus_id;
    }

    public void setKursusId(DocumentReference kursus_id) {
        this.kursus_id = kursus_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }


}
