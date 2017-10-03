package com.bizarrecoding.example.moviepop;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizarrecoding.example.moviepop.Adapters.MovieAdapter;
import com.bizarrecoding.example.moviepop.Objects.Movie;
import com.bizarrecoding.example.moviepop.Utils.ApiMovieFetchLoader;
import com.bizarrecoding.example.moviepop.Utils.GlobalFunctions;
import com.bizarrecoding.example.moviepop.Utils.Network;
import com.bizarrecoding.example.moviepop.localData.DBLoader;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;

import static com.bizarrecoding.example.moviepop.Adapters.MovieAdapter.MOVIE_REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{ //<List<Object>>{

    private static final int MOVIELOADERID = 100;
    private static final int FAVSLOADERID = 200;
    public static final int POPULARITY = 0;
    public static final int RATING = 1;
    public static final int FAVORITES = 2;
    private int currentSort = 0;
    private static final int MAXITEMSPERREQUEST = 20;
    private int currentPage = 1;
    private boolean firstTime = true;

    private RecyclerView movieListHolder;
    private TextView sortType;
    private List<Movie> movies;
    private MovieAdapter mAdapter;
    private ProgressBar progress;
    private TextView errorTV;
    private Parcelable state;
    private GridLayoutManager glManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            currentSort = savedInstanceState.getInt("sort");
            if(savedInstanceState.containsKey("page")){
                currentPage = savedInstanceState.getInt("page");
            }
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initGUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sort",currentSort);
        if(firstTime) {
            outState.putInt("page", currentPage);
        }
    }

    private void initGUI() {
        //Init UI components
        sortType = (TextView) findViewById(R.id.sort);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        errorTV = (TextView) findViewById(R.id.errorTV);
        movieListHolder = (RecyclerView) findViewById(R.id.movieList);
        movies = new ArrayList<>();

        glManager = new GridLayoutManager(this, numberOfColumns());
        movieListHolder.setLayoutManager(glManager);
        movieListHolder.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, movies);
        movieListHolder.setAdapter(mAdapter);
        movieListHolder.addOnScrollListener(createInfiniteScrollListener());
        if (currentSort == FAVORITES){
            loadFavs();
        }else{
            loadMovies();
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(state!=null && movieListHolder != null) {
            movieListHolder.getLayoutManager().onRestoreInstanceState(state);
        }
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ){
            glManager.setSpanCount(3);
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            glManager.setSpanCount(2);
        }
        movieListHolder.setLayoutManager(glManager);
        movieListHolder.invalidate();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void loadMovies() {
        if(isOnline()){
            LoaderManager lm = getSupportLoaderManager();
            Loader movieLoader = lm.getLoader(MOVIELOADERID);
            Bundle b = new Bundle();
            b.putString("Action",getResources().getString(R.string.movies));
            String[] urls = new String[]{Network.buildURL(currentSort,currentPage).toString()};
            b.putStringArray("urls",urls);
            if (movieLoader == null) {
                lm.initLoader(MOVIELOADERID, b, this);
            }else{
                lm.restartLoader(MOVIELOADERID, b, this);
            }
        }else{
            GlobalFunctions.showError(R.string.networkerror,progress,movieListHolder,errorTV);
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (firstTime)
            GlobalFunctions.showProgress(true,progress,movieListHolder,errorTV);
        if(id == MOVIELOADERID){
            return new ApiMovieFetchLoader(this,args);
        }else if( id == FAVSLOADERID){
            return new DBLoader(this,args);
        }else{
            GlobalFunctions.showError(R.string.task_error,progress,movieListHolder,errorTV);
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data==null) {
            GlobalFunctions.showError(R.string.task_error,progress,movieListHolder,errorTV);
        }else{
            switch (loader.getId()){
                case MOVIELOADERID:
                    mAdapter.setMovies(
                            firstTime,
                            ((List<List<Movie>>) data).get(0)
                    );
                    if(firstTime)
                        GlobalFunctions.showProgress(false,progress,movieListHolder,errorTV);
                    break;
                case FAVSLOADERID:
                    ArrayList<Movie> favorites = new ArrayList<>();
                    Cursor favsCursor = (Cursor)  data;
                    while (favsCursor.moveToNext()){
                        Movie m = new Movie(favsCursor);
                        favorites.add(m);
                    }
                    favsCursor.close();
                    mAdapter.setMovies(firstTime,favorites);
                    GlobalFunctions.showProgress(false,progress,movieListHolder,errorTV);
                    break;
            }
        }
    }
    @Override
    public void onLoaderReset(Loader loader) {}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.spopularity:
                if(currentSort!=POPULARITY){
                    sortType.setText(R.string.sortpopularity);
                    currentSort = POPULARITY;
                    currentPage = 1;
                    firstTime = true;
                    loadMovies();
                }
                break;
            case R.id.srating:
                if(currentSort!= RATING){
                    sortType.setText(R.string.sortrating);
                    currentSort = RATING;
                    currentPage = 1;
                    firstTime = true;
                    loadMovies();
                }
                break;
            case R.id.sfavorites:
                currentSort = FAVORITES;
                sortType.setText(R.string.favorites);
                loadFavs();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavs() {
        LoaderManager lm = getSupportLoaderManager();
        Loader favsLoader = lm.getLoader(FAVSLOADERID);
        if (favsLoader == null) {
            Bundle b = new Bundle();
            b.putInt("Action",FAVSLOADERID);
            lm.initLoader(FAVSLOADERID, b, this);
        }else{
            Bundle b = new Bundle();
            b.putInt("Action",FAVSLOADERID);
            lm.restartLoader(FAVSLOADERID, b, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( currentSort == FAVORITES && requestCode == MOVIE_REQUEST_CODE && resultCode == RESULT_OK){
            if(data.getBooleanExtra("change",true)){
                loadFavs();
            }
        }
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(MAXITEMSPERREQUEST, glManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                if (!firstTime && currentPage > Math.ceil((float) mAdapter.getItemCount() / (float) MAXITEMSPERREQUEST)) {
                    return;
                }
                currentPage++;
                firstTime = false;
                loadMovies();
            }
        };
    }
}
