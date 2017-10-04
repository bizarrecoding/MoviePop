package com.bizarrecoding.example.moviepop;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizarrecoding.example.moviepop.adapters.ReviewsAdapter;
import com.bizarrecoding.example.moviepop.adapters.TrailersAdapter;
import com.bizarrecoding.example.moviepop.objects.Movie;
import com.bizarrecoding.example.moviepop.objects.Review;
import com.bizarrecoding.example.moviepop.objects.Trailer;
import com.bizarrecoding.example.moviepop.utils.ApiMovieFetchLoader;
import com.bizarrecoding.example.moviepop.utils.GlobalFunctions;
import com.bizarrecoding.example.moviepop.utils.Network;
import com.bizarrecoding.example.moviepop.localdata.DBLoader;
import com.bizarrecoding.example.moviepop.localdata.MovieContract;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int MOVIE_EXTRAS_LOADER_ID = 300;
    private static final int IS_FAV_LOADER_ID = 400;
    private boolean isFavorite = false;
    private Movie movie;
    private RecyclerView reviewsList;
    private RecyclerView trailersList;
    private ReviewsAdapter rAdapter;
    private TrailersAdapter tAdapter;
    private boolean change;
    private MenuItem star;
    private ScrollView mScrollView;
    private int[] scrollState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        rAdapter = new ReviewsAdapter(new ArrayList<Review>());
        tAdapter = new TrailersAdapter(this, new ArrayList<Trailer>());
        if (getIntent().hasExtra("movie")){
            Log.d("MOVIE","from intent");
            movie = getIntent().getParcelableExtra("movie");
        }
        if(savedInstanceState!= null){
            if(movie==null) {
                movie = savedInstanceState.getParcelable("movie");
            }
            if(savedInstanceState.containsKey("reviews") && savedInstanceState.containsKey("trailers")){
                ArrayList<Review> rList = savedInstanceState.getParcelableArrayList("reviews");
                rAdapter.setReviews(rList);
                ArrayList<Trailer> tList = savedInstanceState.getParcelableArrayList("trailers");
                tAdapter.setTrailers(tList);
            }
            if(rAdapter.getItemCount()<1){
                GlobalFunctions.showError(R.string.reviewserror,findViewById(R.id.reviews),findViewById(R.id.noreview));
            }
            if(tAdapter.getItemCount()<1){
                GlobalFunctions.showError(R.string.trailerserror,findViewById(R.id.trailers),findViewById(R.id.notrailer));
            }
            scrollState = savedInstanceState.getIntArray("extras_position");
            initGUI();
        }else {
            initGUI();
            Log.d("RESTORATION","false");
            if(isOnline()){
                fetchData();
            }
        }
        change = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie",movie);
        outState.putParcelableArrayList("reviews",rAdapter.getReviews());
        outState.putParcelableArrayList("trailers",tAdapter.getTrailers());
        outState.putIntArray("extras_position",
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
        tAdapter.getTrailers();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void initGUI() {
        ImageView cover = (ImageView) findViewById(R.id.poster);
        TextView title = (TextView) findViewById(R.id.title);
        TextView orgtitle = (TextView) findViewById(R.id.originalTitle);
        TextView overview = (TextView) findViewById(R.id.overview);
        TextView release = (TextView) findViewById(R.id.release);
        TextView numRate = (TextView) findViewById(R.id.numRate);
        RatingBar ratebar = (RatingBar) findViewById(R.id.ratingBar);

        mScrollView = (ScrollView)findViewById(R.id.extraScroll);
        reviewsList = (RecyclerView)findViewById(R.id.reviews);
        reviewsList.setLayoutManager(new LinearLayoutManager(this));
        reviewsList.setHasFixedSize(true);
        reviewsList.setAdapter(rAdapter);
        trailersList = (RecyclerView)findViewById(R.id.trailers);
        trailersList.setLayoutManager(new LinearLayoutManager(this));
        trailersList.setHasFixedSize(true);
        trailersList.setAdapter(tAdapter);

        if(movie.getImagePath().length()<40 ){
            String placeholder ="http://via.placeholder.com/100x150";
            Picasso.with(this)
                    .load(placeholder)
                    .into(cover);
        }else {
            Picasso.with(this)
                    .load(movie.getImagePath())
                    .into(cover);
        }
        title.setText(movie.getTitle());
        orgtitle.setText(movie.getOriginalTitle());
        String releaseStr = movie.getOverview();
        if(releaseStr.length()>0){
            release.setText(movie.getReleaseDate());
        }else{
            release.setText(R.string.no_release);
        }
        float rate = (float) movie.getVotesavg();
        numRate.setText("("+rate+")");
        ratebar.setNumStars(5);
        ratebar.setStepSize(0.5f);
        ratebar.setRating(rate/2);
        ratebar.invalidate();

        String overviewStr = movie.getOverview();
        if(overviewStr.length()>0){
            overview.setText(movie.getOverview());
        }else{
            overview.setText(R.string.no_overview);
        }

        if(!isOnline()){
            GlobalFunctions.showError(trailersList,findViewById(R.id.errorTV1));
            GlobalFunctions.showError(reviewsList,findViewById(R.id.errorTV2));
        }

        if(scrollState!=null){
            mScrollView.scrollTo(scrollState[0],scrollState[1]);
        }
    }

    private void fetchData() {
        URL reviewsURL = Network.buildURL(movie.getId(),"reviews");
        URL trailersURL = Network.buildURL(movie.getId(),"videos");
        LoaderManager lm = getSupportLoaderManager();
        Loader extrasLoader = lm.getLoader(MOVIE_EXTRAS_LOADER_ID);
        Bundle b = new Bundle();
        b.putString("Action",getResources().getString(R.string.extras));
        String[] urls = new String[]{reviewsURL.toString(),trailersURL.toString()};
        b.putStringArray("urls",urls);
        if (extrasLoader == null) {
            lm.initLoader(MOVIE_EXTRAS_LOADER_ID, b, this);
        }else{
            lm.restartLoader(MOVIE_EXTRAS_LOADER_ID, b, this);
        }
    }


    private void insertFavorite(){
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI,movie.getContentValues());
        if (uri!=null){
            try{
                movie.set_Id(Integer.parseInt(uri.getLastPathSegment()));
                Toast.makeText(this, R.string.add_favorite,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                //Log.e("PARSE ERROR",uri.getLastPathSegment()+" not a number");
                e.printStackTrace();
            }
        }
    }

    private void deleteFavorite(){
        ContentResolver contentResolver = this.getContentResolver();
        Uri targetUri = MovieContract.MovieEntry.CONTENT_URI;
        targetUri = targetUri.buildUpon().appendPath(movie.get_Id()+"").build();
        int deleted = contentResolver.delete(targetUri,"",new String[]{"id"});
        if (deleted>0){
            Toast.makeText(this, R.string.delete_favorite,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case MOVIE_EXTRAS_LOADER_ID:
                return new ApiMovieFetchLoader(this,args);
            case IS_FAV_LOADER_ID:
                return new DBLoader(this,args);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data==null) {

        }else{
            switch (loader.getId()){
                case MOVIE_EXTRAS_LOADER_ID:
                    List<Object> list = (List<Object>) data;
                    rAdapter.setReviews((ArrayList<Review>) list.get(0));
                    tAdapter.setTrailers((ArrayList<Trailer>) list.get(1));
                    if(rAdapter.getItemCount()<1){
                        GlobalFunctions.showError(R.string.reviewserror,reviewsList,findViewById(R.id.noreview));
                    }
                    if(tAdapter.getItemCount()<1){
                        GlobalFunctions.showError(R.string.trailerserror,trailersList,findViewById(R.id.notrailer));
                    }
                    break;
                case IS_FAV_LOADER_ID:
                    Cursor favCursor = (Cursor) data;
                    int count = favCursor.getCount();
                    boolean check = count>0 && favCursor.moveToFirst();
                    star.setChecked(check);
                    isFavorite = check;
                    if(check) {
                        movie.set_Id(favCursor.getInt(0));
                        star.setIcon(R.drawable.ic_star);
                    }else{
                        star.setIcon(R.drawable.ic_star_outline);
                    }
                    favCursor.close();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    private void isFavorite() {
        LoaderManager lm = getSupportLoaderManager();
        Loader isFavLoader = lm.getLoader(IS_FAV_LOADER_ID);
        Bundle b = new Bundle();
        b.putInt("Action",IS_FAV_LOADER_ID);
        b.putInt("movie_id",movie.getId());
        if (isFavLoader == null) {
            lm.initLoader(IS_FAV_LOADER_ID, b, this);
        }else{
            lm.restartLoader(IS_FAV_LOADER_ID, b, this);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        star = menu.findItem(R.id.favorite);
        isFavorite();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        change = true;
        switch (id){
            case R.id.favorite:
                if(!isFavorite) {
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_star);
                    insertFavorite();
                    isFavorite=true;
                }else{
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_star_outline);
                    deleteFavorite();
                    isFavorite = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("change",change);
        setResult(RESULT_OK,i);
        finish();
        super.onBackPressed();
    }
}
