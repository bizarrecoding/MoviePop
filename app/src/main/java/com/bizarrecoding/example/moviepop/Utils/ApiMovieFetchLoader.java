package com.bizarrecoding.example.moviepop.Utils;

import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.bizarrecoding.example.moviepop.Objects.Movie;
import com.bizarrecoding.example.moviepop.Objects.Review;
import com.bizarrecoding.example.moviepop.Objects.Trailer;
import com.bizarrecoding.example.moviepop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herik on 30/9/2017.
 */

public class ApiMovieFetchLoader extends AsyncTaskLoader<List<Object>> {

    private final String action;
    private final String[] urls;
    private final String MovieAction;
    private List<Object> objects;
    public Context ctx;

    public ApiMovieFetchLoader(Context context, Bundle args){
        super(context);
        this.ctx = context;
        this.urls = args.getStringArray("urls");
        this.action = args.getString("Action");
        this.MovieAction = getContext().getResources().getString(R.string.movies);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(objects==null){
            forceLoad();
        }else {
            deliverResult(objects);
        }
    }

    @Override
    public void deliverResult(List<Object> data) {
        objects = data;
        super.deliverResult(data);
    }

    @Override
    public List<Object> loadInBackground() {
        List<Object> objList = new ArrayList<>();
        try {
            for (int i = 0;i<urls.length;i++) {
                URL url = new URL(urls[i]);
                String json = Network.getResponseFromHttpUrl(url);
                JSONObject list = new JSONObject(json);
                JSONArray results = list.getJSONArray("results");
                objList.add(parseJSON(results,i));
            }
        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return objList;
    }
    private List<Object> parseJSON(JSONArray results,int type) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i<results.length(); i++){
            JSONObject obj  = results.getJSONObject(i);
            if(action.equals(MovieAction)) {
                list.add(parseMovieJSON(obj));
            }else if (type == 0){
                list.add(parseReviewJSON(obj));
            }else {
                list.add(parseTrailerJSON(obj));
            }
        }
        return list;
    }
    private Movie parseMovieJSON(JSONObject movieObj) throws JSONException {
        int id = movieObj.getInt("id");
        double avg = movieObj.getDouble("vote_average");
        String title = movieObj.getString("title");
        String orgTitle = movieObj.getString("original_title");
        String release = movieObj.getString("release_date");
        String overview = movieObj.getString("overview");
        String imageCover = movieObj.getString("poster_path");
        return new Movie(id, avg, title, orgTitle, release, overview, imageCover);
    }
    private Review parseReviewJSON(JSONObject reviewObj) throws JSONException {
        String author = reviewObj.getString("author");
        String content = reviewObj.getString("content");
        String url = reviewObj.getString("url");
        return new Review(author,content,url);
    }
    private Trailer parseTrailerJSON(JSONObject trailerObj) throws JSONException {
        String key = trailerObj.getString("key");
        String site = trailerObj.getString("site");
        String type = trailerObj.getString("type");
        String name = trailerObj.getString("name");
        return new Trailer(key,site,type,name);
    }
}
