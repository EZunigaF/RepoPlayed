package com.example.loginapp;

public class Blog {

    private String title;
    private String Desc;
    private String imageUrl;
    private String conso;
    private String categ;
    private String creator;


    public Blog() {

    }

    public Blog(String title, String Desc, String imageUrl, String conso,String categ,String creator) {
        this.title = title;
        this.Desc = Desc;
        this.imageUrl = imageUrl;
        this.conso = conso;
        this.categ = categ;
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConso() {
        return conso;
    }

    public void setConso(String conso) {
        this.conso = conso;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        this.Desc = desc;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String image) {
        this.imageUrl = image;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
