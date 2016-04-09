package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;

/**
 * Created by kishor on 9/4/16.
 */
// Container Activity must implement this interface
public interface MovieSelectedListener {
    public void onMovieSelected(Movie movie);
}
