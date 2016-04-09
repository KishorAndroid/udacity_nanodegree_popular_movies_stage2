package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.adapter.MovieTrailerAdapter;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table.MovieTable;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.provider.PopularMoviesProvider;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    private FloatingActionButton fab;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        movie = getIntent().getExtras().getParcelable("MOVIE_INTENT");

        fab = (FloatingActionButton) findViewById(R.id.favourite_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movie.getIsFavourite()){
                    movie.setIsFavourite(false);
                    fab.setImageResource(android.R.drawable.btn_star_big_off);
                    MovieDetailActivity.this.getContentResolver().delete(PopularMoviesProvider.MOVIE_CONTENT_URI, "id = ?", new String[]{movie.getId() + ""});
                }else {
                    movie.setIsFavourite(true);
                    fab.setImageResource(android.R.drawable.btn_star_big_on);
                    Uri uri = MovieDetailActivity.this.getContentResolver().insert(PopularMoviesProvider.MOVIE_CONTENT_URI, movie.getContentValues());
                    Snackbar.make(view, "Movie " + movie.getTitle() + " is favourited", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            appBarLayout.setTitle(movie.getOriginalTitle());
        } catch(Exception e){
            e.printStackTrace();
        }

        if(movie.getIsFavourite()){
            fab.setImageResource(android.R.drawable.btn_star_big_on);
        }

        Bundle arguments = new Bundle();
        arguments.putParcelable("MOVIE_INTENT", movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mShareIntentReceiver,
                new IntentFilter("trailers-fetched"));
    }

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

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mShareIntentReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure.
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
