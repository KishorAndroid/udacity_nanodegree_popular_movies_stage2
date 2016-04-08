package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.R;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kishor on 22/3/16.
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>{

    private Context context;
    private List<Trailer> trailerList;

    public MovieTrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer, null);
        MovieTrailerViewHolder movieGridViewHolder = new MovieTrailerViewHolder(layoutView);
        return movieGridViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, final int position) {
        Log.d("MovieTrailers", "MovieTrailers url : " + Constants.TRAILER_BASE_URL_PREFIX + trailerList.get(position).getKey() + Constants.TRAILER_BASE_URL_POSTFIX);
        Picasso.with(context)
                .load(Constants.TRAILER_BASE_URL_PREFIX + trailerList.get(position).getKey() + Constants.TRAILER_BASE_URL_POSTFIX)
                .placeholder(R.drawable.youtube)
                .error(R.drawable.youtube)
                .into(holder.trailerThumbnail);
        holder.trailerThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TRAILER_SHARE_URL_PREFIX + trailerList.get(position).getKey())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView trailerThumbnail;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            trailerThumbnail = (ImageView) itemView.findViewById(R.id.movie_trailer_thumbnail);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
