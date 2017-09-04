package com.bizarrecoding.example.moviepop;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Herik on 3/9/2017.
 */

public class Movie implements Serializable {

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

    private final String BASE_IMG_PATH = "https://image.tmdb.org/t/p/";

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
        if (imagePath.equals("")){
            this.imagePath="";
        }else {
            this.imagePath = BASE_IMG_PATH+"w185"+imagePath;
        }
    }
}
