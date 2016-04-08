package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.PopularMoviesDatabase;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table.MovieTable;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesProvider extends ContentProvider {

    public static final String AUTHORITY = "com.appdevelopkar.stage2.movies.popularmoviesstage2.data.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri MOVIE_CONTENT_URI = Uri.withAppendedPath(PopularMoviesProvider.AUTHORITY_URI, MovieContent.CONTENT_PATH);


    private static final UriMatcher URI_MATCHER;
    private PopularMoviesDatabase mDatabase;

    private static final int MOVIE_DIR = 0;
    private static final int MOVIE_ID = 1;


    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, MovieContent.CONTENT_PATH, MOVIE_DIR);
        URI_MATCHER.addURI(AUTHORITY, MovieContent.CONTENT_PATH + "/#",    MOVIE_ID);

     }

    public static final class MovieContent implements BaseColumns {
        public static final String CONTENT_PATH = "movie";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.popularmovies_database.movie";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.popularmovies_database.movie";
    }


    @Override
    public final boolean onCreate() {
        mDatabase = new PopularMoviesDatabase(getContext());
        return true;
    }

    @Override
    public final String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_DIR:
                return MovieContent.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContent.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public final Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = mDatabase.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case MOVIE_ID:
                queryBuilder.appendWhere(MovieTable.ID + "=" + uri.getLastPathSegment());
            case MOVIE_DIR:
                queryBuilder.setTables(MovieTable.TABLE_NAME);
                break;

            default :
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case MOVIE_DIR:
                case MOVIE_ID:
                    final long movieId = dbConnection.insertWithOnConflict(MovieTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                    final Uri newMovieUri = ContentUris.withAppendedId(MOVIE_CONTENT_URI, movieId);
                    getContext().getContentResolver().notifyChange(newMovieUri, null);

                    dbConnection.setTransactionSuccessful();
                    return newMovieUri;
                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int updateCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case MOVIE_DIR:
                    updateCount = dbConnection.update(MovieTable.TABLE_NAME, values, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case MOVIE_ID:
                   final long movieId = ContentUris.parseId(uri);
                   updateCount = dbConnection.update(MovieTable.TABLE_NAME, values,
                       MovieTable.ID + "=" + movieId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                   dbConnection.setTransactionSuccessful();
                   break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return updateCount;

    }

    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int deleteCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case MOVIE_DIR:
                    deleteCount = dbConnection.delete(MovieTable.TABLE_NAME, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case MOVIE_ID:
                    deleteCount = dbConnection.delete(MovieTable.TABLE_NAME, MovieTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    dbConnection.setTransactionSuccessful();
                    break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return deleteCount;
    }
}