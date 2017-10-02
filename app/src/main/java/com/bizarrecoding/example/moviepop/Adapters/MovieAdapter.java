package com.bizarrecoding.example.moviepop.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizarrecoding.example.moviepop.Objects.Movie;
import com.bizarrecoding.example.moviepop.MovieDetailsActivity;
import com.bizarrecoding.example.moviepop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    public static final int MOVIE_REQUEST_CODE = 111;
    private List<Movie> movies;
    private Context ctx;

    public MovieAdapter(Context ctx, List<Movie> mList){
        this.movies = mList;
        this.ctx = ctx;
    }

    public void setMovies(boolean firstTime, List<Movie> mList){
        if (firstTime) {
            this.movies = mList;
        }else {
            this.movies.addAll(mList);
        }
        notifyDataSetChanged();
    }

    private Context getContext(){
        return ctx;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context cxt = parent.getContext();
        LayoutInflater linf = LayoutInflater.from(cxt);
        View view = linf.inflate(R.layout.grid_movie_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie m =  movies.get(position);
        holder.title.setText(m.getTitle());
        holder.rating.setText(m.getVotesavg()+"");
        holder.release.setText(m.getReleaseDate());

        String path = m.getImagePath();
        if(path.length()<40 ){
            String placeholder ="http://via.placeholder.com/100x150";
            Picasso.with(ctx).load(placeholder).into(holder.cover);
        }else {
            Picasso.with(ctx).load(path).into(holder.cover);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(),MovieDetailsActivity.class);
                in.putExtra("movie", m);
                ((Activity)ctx).startActivityForResult(in, MOVIE_REQUEST_CODE);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView title;
        private TextView rating;
        private TextView release;

        private ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            rating = (TextView) itemView.findViewById(R.id.rating);
            release = (TextView) itemView.findViewById(R.id.release);
        }
    }
}
