package com.bizarrecoding.example.moviepop.localdata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.bizarrecoding.example.moviepop.localdata.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by Herik on 30/9/2017.
 */

public class MovieProvider extends ContentProvider {

    private DBHelper dbhelper;
    public UriMatcher uriMatcher = buildURIMatcher();

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
       dbhelper = new DBHelper(ctx);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbhelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor returnCursor;
        switch (match){
            case MOVIES:
                returnCursor =  db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String sel = "_id=?";
                String[] mSelVal = new String[]{id};
                returnCursor =  db.query(TABLE_NAME,projection,sel,mSelVal,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                long id = db.insert(TABLE_NAME,null,values);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int deleted = 0;
        switch (match){
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String sel = "_id=?";
                String[] selVal = new String[]{id};
                deleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,sel,selVal);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //no implementation needed
        return 0;
    }

    public static UriMatcher buildURIMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        matcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/#",MOVIE_WITH_ID);
        return matcher;
    }
}
