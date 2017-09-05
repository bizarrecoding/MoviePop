package com.bizarrecoding.example.moviepop.Utils;

import android.net.Uri;

import com.bizarrecoding.example.moviepop.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private static final String TOPRATED = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String POPULAR = "http://api.themoviedb.org/3/movie/popular";
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    private static final String API_KEY = BuildConfig.API_KEY;


    public static URL buildURL(int sort, int page){
        String base;
        if(sort==0){
            base = POPULAR;
        }else{
            base = TOPRATED;
        }

        Uri builtUri = Uri.parse(base).buildUpon()
                .appendQueryParameter(PAGE_PARAM, page+"")
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
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
