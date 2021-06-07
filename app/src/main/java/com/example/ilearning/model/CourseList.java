package com.example.ilearning.model;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.RequestBuilder;

public class CourseList {

    private String courseID;
    private String courseTitle;
    private String courseSubtitle;
    private String courseImage;

    public CourseList(String courseID, String courseTitle, String courseSubtitle, String courseImage) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseSubtitle = courseSubtitle;
        this.courseImage = courseImage;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseSubtitle() {
        return courseSubtitle;
    }

    public void setCourseSubtitle(String courseSubtitle) {
        this.courseSubtitle = courseSubtitle;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }
}
