package com.bizarrecoding.example.moviepop.localData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Herik on 30/9/2017.
 */

class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Favorites.db";

    public static final int DB_VERSION = 4;

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"+
                MovieContract.MovieEntry.COLUMN_VOTES_AVG + " REAL," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_ORG_TITLE + " TEXT," +
                MovieContract.MovieEntry.COLUMN_RELEASE + " TEXT," +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT," +
                MovieContract.MovieEntry.COLUMN_IMAGE_PATH + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
