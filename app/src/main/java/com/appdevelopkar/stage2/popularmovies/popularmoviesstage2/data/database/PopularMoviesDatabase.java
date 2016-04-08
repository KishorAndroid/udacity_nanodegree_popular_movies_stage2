package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table.MovieTable;

public class PopularMoviesDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "popularmovies_database.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDatabase(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(MovieTable.SQL_CREATE);

    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        upgrade(db, oldVersion, newVersion);
    }

    private final void dropTablesAndCreate(final SQLiteDatabase db) {
        db.execSQL(MovieTable.SQL_DROP);


        onCreate(db);
    }

    private void upgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        dropTablesAndCreate(db);
    }
}