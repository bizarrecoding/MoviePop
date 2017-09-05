package com.bizarrecoding.example.moviepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getWindow().setBackgroundDrawable(null);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        initGUI();
    }

    private void initGUI() {
        ImageView cover = (ImageView) findViewById(R.id.poster);
        TextView title = (TextView) findViewById(R.id.title);
        TextView orgtitle = (TextView) findViewById(R.id.originalTitle);
        TextView overview = (TextView) findViewById(R.id.overview);
        TextView release = (TextView) findViewById(R.id.release);
        RatingBar ratebar = (RatingBar) findViewById(R.id.ratingBar);
        if(movie.getImagePath().length()<40 ){
            String placeholder ="http://via.placeholder.com/100x150";
            Picasso.with(this).load(placeholder).into(cover);
        }else {
            Picasso.with(this).load(movie.getImagePath()).into(cover);
        }
        title.setText(movie.getTitle());
        orgtitle.setText(movie.getOriginalTitle());
        String overviewStr = movie.getOverview();
        if(overviewStr.length()>0){
            overview.setText(movie.getOverview());
        }else{
            overview.setText(R.string.no_overview);
        }
        String releaseStr = movie.getOverview();
        if(releaseStr.length()>0){
            release.setText(movie.getReleaseDate());
        }else{
            release.setText(R.string.no_release);
        }
        ratebar.setMax(10);
        ratebar.setRating((float) movie.getVotesavg());
    }
}
