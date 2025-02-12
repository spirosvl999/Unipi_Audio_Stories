package com.example.unipiaudioservices;

public class FavStoryModel {
    private String Tittle;
    private String Author;
    private String Inside;
    private String photo_url;
    private int listen_count; // New field for listen count

    public FavStoryModel() {
        // Default constructor for Firebase
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

    public String getInside() {
        return Inside;
    }

    public void setInside(String inside) {
        Inside = inside;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public int getListen_count() {
        return listen_count;
    }

    public void setListen_count(int listen_count) {
        this.listen_count = listen_count;
    }
}
