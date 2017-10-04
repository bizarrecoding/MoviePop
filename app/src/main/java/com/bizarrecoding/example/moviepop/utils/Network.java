package com.bizarrecoding.example.moviepop.utils;

import android.net.Uri;
import android.util.Log;

import com.bizarrecoding.example.moviepop.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private static final String BASE = "http://api.themoviedb.org/3/movie/";
    private static final String TOPRATED = "top_rated";
    private static final String POPULAR = "popular";
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final int SORT_POPULAR = 0;

    public static URL buildURL(int sort, int page){
        String base;
        if(sort==SORT_POPULAR){
            base = BASE+POPULAR;
        }else{
            base = BASE+TOPRATED;
        }

        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(PAGE_PARAM, page+"")
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("ERROR",e.getMessage());
        }
        return url;
    }

    public static URL buildURL(int id,String type){
        String base = BASE+id+"/"+type;

        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("ERROR",e.getMessage());
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
