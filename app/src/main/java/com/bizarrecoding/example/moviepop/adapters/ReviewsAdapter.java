package com.bizarrecoding.example.moviepop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bizarrecoding.example.moviepop.objects.Review;
import com.bizarrecoding.example.moviepop.R;

import java.util.ArrayList;

/**
 * Created by Herik on 30/9/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private ArrayList<Review> reviews;
    private int SHORTCOMMENT = 4;
    private int FULLCOMMENT = 99;
    private int LINESTHRESHOLD = 10;


    public ReviewsAdapter(ArrayList<Review> mList){
        this.reviews = mList;
    }

    public void setReviews(ArrayList<Review> mList){
        this.reviews = mList;
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return reviews;
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
                if(lines<LINESTHRESHOLD){
                    tv.setMaxLines(FULLCOMMENT);
                }else {
                    tv.setMaxLines(SHORTCOMMENT);
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
