package com.bizarrecoding.example.moviepop.Objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.repacked.kotlin.collections.LongIterator;

import com.bizarrecoding.example.moviepop.localData.MovieContract;

import java.io.Serializable;

public class Movie implements Serializable {

    private int _id;
    private int id;
    private double votesavg;
    private String title;
    private String originalTitle;
    private String releaseDate;
    private String overview;
    private String imagePath;

    public int get_Id() {
        return _id;
    }

    public void set_Id(int _id){
        this._id = _id;
    }

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

    public Movie(Cursor c){
        this._id = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));
        this.id = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        this.votesavg = c.getDouble(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_VOTES_AVG));
        this.title = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));
        this.originalTitle = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_ORG_TITLE));
        this.overview = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        this.releaseDate = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASE));
        this.imagePath = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_IMAGE_PATH));
    }

    public ContentValues getContentValues(){
        ContentValues movie= new ContentValues();
        movie.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,id);
        movie.put(MovieContract.MovieEntry.COLUMN_VOTES_AVG,votesavg);
        movie.put(MovieContract.MovieEntry.COLUMN_TITLE,title);
        movie.put(MovieContract.MovieEntry.COLUMN_ORG_TITLE,originalTitle);
        movie.put(MovieContract.MovieEntry.COLUMN_RELEASE,releaseDate);
        movie.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,overview);
        movie.put(MovieContract.MovieEntry.COLUMN_IMAGE_PATH,imagePath);
        return movie;
    }
}
