package com.example.unipiaudioservices;

public class MainModel
{

    String name, tittle, image;
    int year;

    public MainModel(int year, String tittle, String name) {
        this.year = year;
        this.tittle = tittle;
        this.name = name;
    }

    public MainModel()
    {

    }

    public String getName() {
        return name;
    }

    public String getTittle() {
        return tittle;
    }

    public int getYear() {
        return year;
    }

    public String getImage() {
        return image;
    }
}
