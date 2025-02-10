package com.example.unipiaudioservices;

public class MainModel {
    private String Tittle;
    private String Author;
    private String Year_Created;
    private String photo_url;

    public MainModel() {
        // Default constructor required for Firebase deserialization
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getYear_Created() {
        return Year_Created;
    }

    public void setYear_Created(String year_Created) {
        Year_Created = year_Created;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}