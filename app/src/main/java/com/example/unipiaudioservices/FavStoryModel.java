package com.example.unipiaudioservices;

public class FavStoryModel {
    private String Tittle;
    private String Author;
    private String Inside;
    private String photo_url;
    private String user_id;
    private String story_id;
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

    public String getuser_id() {
        return user_id;
    }

    public void setuser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getstory_id() {
        return story_id;
    }

    public void setstory_id(String story_id) {
        this.story_id = story_id;
    }
}
