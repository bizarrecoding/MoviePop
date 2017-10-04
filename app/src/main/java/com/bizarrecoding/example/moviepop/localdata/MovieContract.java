package com.bizarrecoding.example.moviepop.localdata;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Herik on 30/9/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.bizarrecoding.example.moviepop";
    //The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";


    private MovieContract(){}

    public static class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "MOVIES";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VOTES_AVG= "avg_votes";
        public static final String COLUMN_TITLE     = "title";
        public static final String COLUMN_ORG_TITLE = "original_title";
        public static final String COLUMN_RELEASE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_IMAGE_PATH = "image_path";
    }
}
