package com.bizarrecoding.example.moviepop;

import java.io.Serializable;

class Movie implements Serializable {

    private int id;
    private int voteCount;
    private double votesavg;
    private double popularity;
    private String title;
    private String originalTitle;
    private String originalLang;
    private String releaseDate;
    private String overview;
    private String imagePath;

    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVotesavg() {
        return votesavg;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLang() {
        return originalLang;
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

    public Movie(int id, int votes, double votesavg, double popularity, String title, String originalTitle,
                 String originalLang,String releaseDate,String overview, String imagePath){
        this.id = id;
        this.voteCount = votes;
        this.votesavg = votesavg;
        this.popularity = popularity;
        this.title = title;
        this.originalTitle = originalTitle;
        this.originalLang = originalLang;
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
