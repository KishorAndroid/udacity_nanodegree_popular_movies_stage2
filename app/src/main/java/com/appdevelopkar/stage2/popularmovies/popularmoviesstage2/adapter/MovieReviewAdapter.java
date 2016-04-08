package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.R;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Review;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;

import java.util.List;

/**
 * Created by kishor on 26/3/16.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>{

    private Context context;
    private List<Review> reviewList;

    public MovieReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, null);
        MovieReviewViewHolder movieGridViewHolder = new MovieReviewViewHolder(layoutView);
        return movieGridViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.content.setText(reviewList.get(position).getContent());
        holder.author.setText(reviewList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView content;
        TextView author;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
