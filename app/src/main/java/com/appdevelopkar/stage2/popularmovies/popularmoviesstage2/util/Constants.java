package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.util;

/**
 * Created by kishor on 12/2/16.
 */
public class Constants {
    public static final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String TRAILER_BASE_URL_PREFIX = "http://img.youtube.com/vi/";
    public static final String TRAILER_SHARE_URL_PREFIX = "http://www.youtube.com/watch?v=";
    public static final String TRAILER_BASE_URL_POSTFIX = "/0.jpg";
    public static final String SORT_BY_MOST_POPULAR = "popularity.desc",
            SORT_BY_MOST_RATED = "vote_average.desc",
            SORT_BY_FAVOURITED = "sort_favourite.desc";
    public static final String FILTER_NAME_KEY = "filter_name_key";
    public static final int MOST_POPULAR = 0, MOST_RATED = 1, FAVOURITED = 2;
}
