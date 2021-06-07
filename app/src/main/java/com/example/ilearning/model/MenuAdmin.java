package com.example.ilearning.model;

public class MenuAdmin<T> {
    private int icon;
    private String title;
    private String subtitle;
    private String activity;

    public MenuAdmin(int icon, String title, String subtitle,String activity) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.activity = activity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
