package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter.MovieReviewAdapter;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter.MovieTrailerAdapter;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.asynctask.FetchMovieReviews;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.asynctask.FetchMovieTrailers;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Review;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.Constants;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.SpacesItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    private TextView movieTitle;
    private ImageView moviePoster;
    private TextView movieOverview;
    private TextView releaseDate;
    private TextView vote;
    private RecyclerView moviesTrailers;
    private RecyclerView moviesReviews;

    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mTrailerListReceiver,
                new IntentFilter("trailers-fetched"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReviewListReceiver,
                new IntentFilter("reviews-fetched"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mTrailerListReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReviewListReceiver);
    }

    private BroadcastReceiver mTrailerListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            trailers = intent.getParcelableArrayListExtra("trailerList");
            if(trailers!=null && !trailers.isEmpty()) {
                movieTrailerAdapter = new MovieTrailerAdapter(getActivity(), trailers);
                moviesTrailers.setAdapter(movieTrailerAdapter);
                Log.d("Movie", "trailers " + trailers.size());

                //Share Intent
                /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TRAILER_SHARE_URL_PREFIX + trailers.get(0).getKey());
                setShareIntent(sharingIntent);*/
            }
        }
    };

    private BroadcastReceiver mReviewListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reviews = intent.getParcelableArrayListExtra("reviewList");
            if(reviews!=null && !reviews.isEmpty()) {
                movieReviewAdapter = new MovieReviewAdapter(getActivity(), reviews);
                moviesReviews.setAdapter(movieReviewAdapter);
                Log.d("Movie", "reviews " + reviews.size());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieTitle = (TextView) view.findViewById(R.id.movie_title);
        moviePoster= (ImageView) view.findViewById(R.id.movie_poster);
        movieOverview = (TextView) view.findViewById(R.id.overview);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        vote = (TextView) view.findViewById(R.id.vote_average);

        //Trailers RecyclerView
        moviesTrailers = (RecyclerView) view.findViewById(R.id.movies_trailer);
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        moviesTrailers.setHasFixedSize(true);
        moviesTrailers.addItemDecoration(new SpacesItemDecoration(2));
        moviesTrailers.setLayoutManager(trailersLayoutManager);
        Movie movie = getArguments().getParcelable("MOVIE_INTENT");
        setData(movie);

        //Reviews RecyclerView
        moviesReviews = (RecyclerView) view.findViewById(R.id.movies_reviews);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        moviesReviews.setHasFixedSize(true);
        moviesReviews.addItemDecoration(new SpacesItemDecoration(4));
        moviesReviews.setLayoutManager(reviewsLayoutManager);
    }

    public void setData(Movie movie){

        movieTitle.setText(movie.getTitle());
        Picasso.with(getActivity())
            .load(Constants.THUMBNAIL_BASE_URL + movie.getPosterPath())
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(moviePoster);
        movieOverview.setText(movie.getOverview());
        releaseDate.setText(movie.getReleaseDate());
        Log.d("Movie", "Movie " + movie.getVoteAverage() + " / " + movie.getVoteCount());
        vote.setText(movie.getVoteAverage() + " / " + movie.getVoteCount());
        if(trailers == null){
            new FetchMovieTrailers(getActivity()).execute(movie.getId() + "");
        }
        if(reviews == null){
            new FetchMovieReviews(getActivity()).execute(movie.getId() + "");
        }
    }
}
