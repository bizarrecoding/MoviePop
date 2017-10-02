package com.bizarrecoding.example.moviepop.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizarrecoding.example.moviepop.MovieDetailsActivity;
import com.bizarrecoding.example.moviepop.Objects.Movie;
import com.bizarrecoding.example.moviepop.Objects.Review;
import com.bizarrecoding.example.moviepop.R;

import java.util.ArrayList;

/**
 * Created by Herik on 30/9/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private ArrayList<Review> reviews;
    private Context ctx;

    public ReviewsAdapter(Context ctx, ArrayList<Review> mList){
        this.reviews = mList;
        this.ctx = ctx;
    }

    public void setReviews(ArrayList<Review> mList){
        this.reviews = mList;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context cxt = parent.getContext();
        LayoutInflater linf = LayoutInflater.from(cxt);
        View view = linf.inflate(R.layout.review_entry, parent, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review r =  reviews.get(position);
        holder.author.setText(r.getAuthor());
        holder.content.setText(r.getContent());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                int lines = tv.getMaxLines();
                if(lines<10){
                    tv.setMaxLines(99);
                }else {
                    tv.setMaxLines(4);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView author;
        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
