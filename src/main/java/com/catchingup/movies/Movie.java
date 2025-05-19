package com.catchingup.movies;

public class Movie {

    private String id;
    private String title;
    private String originalTitle;
    private int releaseYear;
    private double rating;
    private String country;
    // @NotEmpty
    private String originalFileName;
    private byte[] data;

    public Movie(){};

    public Movie(String id, String title, String originalTitle, int releaseYear, double rating, String country, String originalFileName, byte[] data){
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.country = country;
        this.originalFileName = originalFileName;
        this.data = data;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getOriginalFileName() {
        return originalFileName;
    }
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    
}
