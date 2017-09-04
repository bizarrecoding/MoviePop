package com.bizarrecoding.example.moviepop.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Herik on 3/9/2017.
 */

public class Network {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String DISCOVER = "discover/movie";
    private static final String SORT_PARAM = "sort_by";
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";



    private static final String KEY = "da96d73bfe83cdc4d3787ed3f60dd830";
    private static final String[] sorts = {"popularity.desc","vote_average.desc"};


    public static URL buildURL(int sort, int page){
        Uri builtUri = Uri.parse(BASE_URL+DISCOVER).buildUpon()
                .appendQueryParameter(SORT_PARAM, sorts[sort])
                .appendQueryParameter(PAGE_PARAM, page+"")
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {

        }
        Log.d("URL",url.toString());
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
