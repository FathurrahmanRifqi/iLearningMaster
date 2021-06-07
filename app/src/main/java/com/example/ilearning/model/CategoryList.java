package com.example.ilearning.model;

public class CategoryList {

    private String id;
    private String image;
    private String title;
    private String desc;

    public CategoryList(String id, String image, String title, String desc) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public CategoryList(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString(){
        return title;
    }
}
