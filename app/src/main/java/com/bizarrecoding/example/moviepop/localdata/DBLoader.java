package com.bizarrecoding.example.moviepop.localdata;

import android.net.Uri;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;

/**
 * Created by Herik on 1/10/2017.
 */

public class DBLoader extends CursorLoader {
    private static final int IS_FAV = 400;
    private static final int ALL_FAVS = 200;

    private final ContentResolver contentResolver;
    private int action;
    private int movie_id;



    public DBLoader(Context context, Bundle args) {
        super(context);
        this.contentResolver = context.getContentResolver();
        this.action = args.getInt("Action");
        if(action==IS_FAV){
            movie_id = args.getInt("movie_id");
        }
    }


    @Override
    public Cursor loadInBackground() {
        Cursor results = null;
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        try{
            switch (action){
                case ALL_FAVS:
                    String sort = MovieContract.MovieEntry.COLUMN_MOVIE_ID+" DESC";
                    //Log.d("LOADER ALL_FAVS","SELECT * FROM MOVIES ORDER BY "+sort);
                    results = contentResolver.query(uri,null,null,null,sort);
                    break;
                case IS_FAV:
                    String[] projection = new String[]{MovieContract.MovieEntry._ID};
                    String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                    String[] selectionVal = new String[]{""+movie_id};
                    //Log.d("LOADER IS_FAV","SELECT _ID FROM MOVIES WHERE movie_id = "+selectionVal[0]+" ORDER BY movie_id");
                    results = contentResolver.query(uri,projection,selection,selectionVal,"movie_id");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }
}
