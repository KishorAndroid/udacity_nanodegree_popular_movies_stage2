package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table;

public interface MovieTable {
    String TABLE_NAME = "movie";

    String POSTER_PATH = "poster_path";
    String ADULT = "adult";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "release_date";
    String ORIGINAL_TITLE = "original_title";
    String ID = "id";
    String ORIGINAL_LANGUAGE = "original_language";
    String TITLE = "title";
    String BACKDROP_PATH = "backdrop_path";
    String POPULARITY = "popularity";
    String VOTE_COUNT = "vote_count";
    String VOTE_AVERAGE = "vote_average";
    String IS_FAVOURITE = "is_favourite";
    String DATE_FAVOURITED = "date_favourited";
    String[] ALL_COLUMNS = new String[] {ID, POSTER_PATH, ADULT, OVERVIEW, RELEASE_DATE, ORIGINAL_TITLE, ORIGINAL_LANGUAGE, TITLE, BACKDROP_PATH, POPULARITY, VOTE_COUNT, VOTE_AVERAGE, IS_FAVOURITE, DATE_FAVOURITED};

    String SQL_CREATE = "CREATE TABLE movie ( id INTEGER PRIMARY KEY AUTOINCREMENT, poster_path TEXT, adult INTEGER, overview TEXT, release_date TEXT, original_title TEXT, original_language TEXT, title TEXT, backdrop_path TEXT, popularity DOUBLE, vote_count INTEGER, vote_average DOUBLE, is_favourite INTEGER, date_favourited TEXT )";

    String SQL_INSERT = "INSERT INTO movie ( id, poster_path, adult, overview, release_date, original_title, original_language, title, backdrop_path, popularity, vote_count, vote_average, is_favourite, date_favourited ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    String SQL_DROP = "DROP TABLE IF EXISTS movie";

    String WHERE_ID_EQUALS = ID + "=?";

}