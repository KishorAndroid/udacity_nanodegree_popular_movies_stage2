package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.MovieSelectedListener;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.R;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kishor on 3/2/16.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieGridViewHolder>{

    private Context context;
    private List<Movie> movieList;
    MovieSelectedListener mCallback;

    public MovieGridAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        mCallback = (MovieSelectedListener) context;
    }

    @Override
    public MovieGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        MovieGridViewHolder movieGridViewHolder = new MovieGridViewHolder(layoutView);
        return movieGridViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, final int position) {
        Picasso.with(context).load(Constants.THUMBNAIL_BASE_URL + movieList.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.movieThumbnail);
        holder.movieThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, movieList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                mCallback.onMovieSelected(movieList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieThumbnail;

        public MovieGridViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
