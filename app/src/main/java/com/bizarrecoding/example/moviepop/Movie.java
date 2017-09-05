package com.bizarrecoding.example.moviepop;

import java.io.Serializable;

class Movie implements Serializable {

    private int id;
    private double votesavg;
    private String title;
    private String originalTitle;
    private String releaseDate;
    private String overview;
    private String imagePath;

    public int getId() {
        return id;
    }

    public double getVotesavg() {
        return votesavg;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Movie(int id, double votesavg, String title, String originalTitle,
                 String releaseDate,String overview, String imagePath){
        this.id = id;
        this.votesavg = votesavg;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        if (imagePath.equals("") || imagePath.equals("null")){
            this.imagePath="";
        }else {
            String BASE_IMG_PATH = "https://image.tmdb.org/t/p/";
            this.imagePath = BASE_IMG_PATH +"w185"+imagePath;
        }
    }
}
