package com.bizarrecoding.example.moviepop;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizarrecoding.example.moviepop.Utils.Network;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView movieListHolder;
    private TextView sortType;
    private ArrayList<Movie> movies;
    private MovieAdapter mAdapter;
    private int currentSort = 0;
    protected int currentPage = 1;
    private ProgressBar progress;
    private TextView errorTV;
    private GridLayoutManager glManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initGUI();
    }

    private void initGUI() {
        //Init UI components
        getWindow().setBackgroundDrawable(null);
        sortType = (TextView)findViewById(R.id.sort);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        errorTV = (TextView)findViewById(R.id.errorTV);
        movieListHolder = (RecyclerView) findViewById(R.id.movieList);

        movies = new ArrayList<>();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        glManager = new GridLayoutManager(this,2);
        movieListHolder.setLayoutManager(glManager);
        movieListHolder.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, movies);
        movieListHolder.setAdapter(mAdapter);

        if(isOnline()){
            loadMovies();
        }else{
            showError(R.string.networkerror);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ){
            glManager.setSpanCount(3);
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            glManager.setSpanCount(2);
        }
        movieListHolder.setLayoutManager(glManager);
        movieListHolder.invalidate();
    }


    private void showError(int errorRes) {
        movieListHolder.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.VISIBLE);
        errorTV.setText(errorRes);
    }

    private void showMovies(){
        movieListHolder.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
    }

    private void showProgress(){
        movieListHolder.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void loadMovies() {
        URL query = Network.buildURL(currentSort,currentPage);
        new moviesTask().execute(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Log.d("","");
        switch(id) {
            case R.id.spopularity:
                if(currentSort==1){
                    sortType.setText(R.string.sortpopularity);
                    currentSort = 0;
                    loadMovies();
                }
                break;
            case R.id.srating:
                if(currentSort==0){
                    sortType.setText(R.string.sortrating);
                    currentSort = 1;
                    loadMovies();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class moviesTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movies.clear();
            showProgress();
        }

        @Override
        protected String doInBackground(URL... params) {
            try {
                String json = Network.getResponseFromHttpUrl(params[0]);
                Log.d("JSON",json);
                JSONObject list = new JSONObject(json);
                JSONArray results = list.getJSONArray("results");
                for (int i = 0; i<results.length(); i++){
                    JSONObject movieObj  = results.getJSONObject(i);
                    int id = movieObj.getInt("id");
                    double avg = movieObj.getDouble("vote_average");
                    String title = movieObj.getString("title");
                    String orgTitle = movieObj.getString("original_title");
                    String release = movieObj.getString("release_date");
                    String overview = movieObj.getString("overview");
                    String imageCover = movieObj.getString("poster_path");
                    Movie movie = new Movie(id, avg, title, orgTitle, release, overview, imageCover);
                    movies.add(movie);
                }
                Log.d("asynctask",json);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                showError(R.string.task_error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAdapter.setMovies(movies);
            Log.d("ARRAY",""+movies.size());
            showMovies();
        }
    }


}
