package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter.MovieGridAdapter;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter.MovieGridCursorAdapter;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.asynctask.FetchMovieList;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table.MovieTable;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.provider.PopularMoviesProvider;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.Constants;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.SpacesItemDecoration;
import java.util.ArrayList;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements MovieSelectedListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private MovieGridAdapter movieGridAdapter;
    private MovieGridCursorAdapter movieGridCursorAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView movieGridView;
    private Spinner spinner;
    private ProgressBar progressBar;
    private FloatingActionButton favouriteMovieFab;
    private ArrayList<Movie> movies;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        setUpViews();
        setUpListeners();
    }

    private void setUpViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = (ProgressBar) findViewById(R.id.content_loading);

        movieGridView = (RecyclerView) findViewById(R.id.movies_grid);
        movieGridView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 2);
        movieGridView.addItemDecoration(new SpacesItemDecoration(0));

        spinner = (Spinner) findViewById(R.id.sort_order);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_order_array, android.R.layout.simple_dropdown_item_1line);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp)
            // If this view is present, then the
            // activity should be in two-pane mode
            gridLayoutManager = new GridLayoutManager(this, 3);
            mTwoPane = true;

            favouriteMovieFab = (FloatingActionButton) findViewById(R.id.favourite_movie);
        }
        movieGridView.setLayoutManager(gridLayoutManager);

        changeSortOrder(Constants.MOST_POPULAR);
    }

    private void setUpListeners(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                changeSortOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(mTwoPane){
            favouriteMovieFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movie.getIsFavourite()) {
                        movie.setIsFavourite(false);
                        favouriteMovieFab.setImageResource(android.R.drawable.btn_star_big_off);
                        MovieListActivity.this.getContentResolver().delete(PopularMoviesProvider.MOVIE_CONTENT_URI, "id = ?", new String[]{movie.getId()+""});
                    } else {
                        movie.setIsFavourite(true);
                        favouriteMovieFab.setImageResource(android.R.drawable.btn_star_big_on);
                        Uri uri = MovieListActivity.this.getContentResolver().insert(PopularMoviesProvider.MOVIE_CONTENT_URI, movie.getContentValues());
                        Snackbar.make(view, "Movie " + movie.getTitle() + " is favourited", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }

    private void changeSortOrder(int filter){
        progressBar.setVisibility(View.VISIBLE);
        movieGridView.setAdapter(null);
        switch (filter){
            case Constants.MOST_POPULAR:
                new FetchMovieList(this).execute(Constants.SORT_BY_MOST_POPULAR);
                break;
            case Constants.MOST_RATED:
                new FetchMovieList(this).execute(Constants.SORT_BY_MOST_RATED);
                break;
            case Constants.FAVOURITED:
                Cursor cursor = getContentResolver().query(PopularMoviesProvider.MOVIE_CONTENT_URI, null, MovieTable.IS_FAVOURITE + " = ?", new String[]{"1"}, null);
                showFavouriteMovies(cursor);
                break;
        }
    }

    private void showFavouriteMovies(Cursor cursor){
        if(cursor != null && cursor.getCount()>0) {
            progressBar.setVisibility(View.GONE);
            movieGridCursorAdapter = new MovieGridCursorAdapter(this, cursor);
            movieGridView.setAdapter(movieGridCursorAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMovieListReceiver,
                new IntentFilter("movies-fetched"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mShareIntentReceiver,
                new IntentFilter("trailers-fetched"));
    }

    private BroadcastReceiver mMovieListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            movies = intent.getParcelableArrayListExtra("movieList");
            if(movies != null && !movies.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                movieGridAdapter = new MovieGridAdapter(MovieListActivity.this, movies);
                movieGridView.setAdapter(movieGridAdapter);
            }
        }
    };

    private BroadcastReceiver mShareIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Trailer> trailers = intent.getParcelableArrayListExtra("trailerList");
            if(trailers!=null && !trailers.isEmpty()) {
                //Share Intent
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TRAILER_SHARE_URL_PREFIX + trailers.get(0).getKey());
                setShareIntent(sharingIntent);
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMovieListReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mShareIntentReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mTwoPane) {
            // Inflate menu resource file.
            getMenuInflater().inflate(R.menu.share_menu, menu);
            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);
            // Fetch and store ShareActionProvider
            mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTwoPane) {
            this.movie = movie;
            Bundle arguments = new Bundle();
            arguments.putParcelable("MOVIE_INTENT", movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
            if(movie.getIsFavourite()){
                favouriteMovieFab.setImageResource(android.R.drawable.btn_star_big_on);
            }else{
                favouriteMovieFab.setImageResource(android.R.drawable.btn_star_big_off);
            }
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MOVIE_INTENT", movie);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}