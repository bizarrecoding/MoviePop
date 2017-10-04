package com.bizarrecoding.example.moviepop.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizarrecoding.example.moviepop.objects.Trailer;
import com.bizarrecoding.example.moviepop.R;

import java.util.ArrayList;

/**
 * Created by Herik on 30/9/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private ArrayList<Trailer> trailers;
    private Context ctx;

    public TrailersAdapter (Context ctx, ArrayList<Trailer> mList){
        this.trailers = mList;
        this.ctx = ctx;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context cxt = parent.getContext();
        LayoutInflater linf = LayoutInflater.from(cxt);
        View view = linf.inflate(R.layout.trailer_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trailer tr = trailers.get(position);
        holder.title.setText(tr.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tr.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
