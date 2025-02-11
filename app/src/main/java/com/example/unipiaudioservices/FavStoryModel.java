package com.example.unipiaudioservices;

public class FavStoryModel {
    private String Tittle;
    private String Author;
    private String Inside; // The main content of the story

    private long Year_Created; // Change from String to long
    private String photo_url;

    public FavStoryModel()
    {
        // Default constructor required for Firebase deserialization
    }

    public String getTittle()
    {
        return Tittle;
    }

    public void setTittle(String tittle)
    {
        Tittle = tittle;
    }

    public String getAuthor()
    {
        return Author;
    }

    public void setAuthor(String author)
    {
        Author = author;
    }
    public String getInside() {
        return Inside;
    }

    public void setInside(String inside) {
        this.Inside = inside;
    }

    public long getYear_Created()
    {
        return Year_Created;
    }

    public void setYear_Created(long year_Created)
    {
        Year_Created = year_Created;
    }

    public String getPhoto_url()
    {
        return photo_url;
    }

    public void setPhoto_url(String photo_url)
    {
        this.photo_url = photo_url;
    }
}
