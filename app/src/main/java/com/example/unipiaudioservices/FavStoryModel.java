package com.example.unipiaudioservices;

public class FavStoryModel {
    private String title;
    private String author;
    private String yearCreated;
    private String imageUrl; // Optional, if you have an image URL

    public FavStoryModel() {
        // Firestore needs an empty constructor
    }

    public FavStoryModel(String title, String author, String yearCreated, String imageUrl) {
        this.title = title;
        this.author = author;
        this.yearCreated = yearCreated;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYearCreated() {
        return yearCreated;
    }

    public void setYearCreated(String yearCreated) {
        this.yearCreated = yearCreated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
