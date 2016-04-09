package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.database.table.MovieTable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private String mPosterPath;
    private boolean mAdult;
    private String mOverview;
    private String mReleaseDate;
    private String mOriginalTitle;
    private Integer mId;
    private String mOriginalLanguage;
    private String mTitle;
    private String mBackdropPath;
    private double mPopularity;
    private double mVoteCount;
    private double mVoteAverage;
    private boolean mIsFavourite;
    private String mDateFavourited;


    private ContentValues mValues = new ContentValues();

    public Movie() {}

    public Movie(final Cursor cursor) {
        this(cursor, false);
    }

    public Movie(final Cursor cursor, boolean prependTableName) {
        String prefix = prependTableName ? MovieTable.TABLE_NAME + "_" : "";
        setPosterPath(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.POSTER_PATH)));
        setAdult(!cursor.isNull(cursor.getColumnIndex(prefix + MovieTable.ADULT)) &&
                cursor.getInt(cursor.getColumnIndex(prefix + MovieTable.ADULT)) == 1);
        setOverview(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.OVERVIEW)));
        setReleaseDate(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.RELEASE_DATE)));
        setOriginalTitle(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.ORIGINAL_TITLE)));
        setId(cursor.getInt(cursor.getColumnIndex(prefix + MovieTable.ID)));
        setOriginalLanguage(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.ORIGINAL_LANGUAGE)));
        setTitle(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.TITLE)));
        setBackdropPath(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.BACKDROP_PATH)));
        setPopularity(cursor.getDouble(cursor.getColumnIndex(prefix + MovieTable.POPULARITY)));
        setVoteCount(cursor.getDouble(cursor.getColumnIndex(prefix + MovieTable.VOTE_COUNT)));
        setVoteAverage(cursor.getDouble(cursor.getColumnIndex(prefix + MovieTable.VOTE_AVERAGE)));
        setIsFavourite(!cursor.isNull(cursor.getColumnIndex(prefix + MovieTable.IS_FAVOURITE)) &&
                cursor.getInt(cursor.getColumnIndex(prefix + MovieTable.IS_FAVOURITE)) == 1);
        setDateFavourited(cursor.getString(cursor.getColumnIndex(prefix + MovieTable.DATE_FAVOURITED)));

    }

    public ContentValues getContentValues() {
        return mValues;
    }

    public void setPosterPath(String poster_path) {
        mPosterPath = poster_path;
        mValues.put(MovieTable.POSTER_PATH, poster_path);
    }

    public String getPosterPath() {
            return mPosterPath;
    }


    public void setAdult(boolean adult) {
        mAdult = adult;
        mValues.put(MovieTable.ADULT, adult);
    }

    public boolean getAdult() {
            return mAdult;
    }


    public void setOverview(String overview) {
        mOverview = overview;
        mValues.put(MovieTable.OVERVIEW, overview);
    }

    public String getOverview() {
            return mOverview;
    }


    public void setReleaseDate(String release_date) {
        mReleaseDate = release_date;
        mValues.put(MovieTable.RELEASE_DATE, release_date);
    }

    public String getReleaseDate() {
            return mReleaseDate;
    }


    public void setOriginalTitle(String original_title) {
        mOriginalTitle = original_title;
        mValues.put(MovieTable.ORIGINAL_TITLE, original_title);
    }

    public String getOriginalTitle() {
            return mOriginalTitle;
    }


    public void setId(Integer id) {
        mId = id;
        mValues.put(MovieTable.ID, id);
    }

    public Integer getId() {
            return mId;
    }


    public void setOriginalLanguage(String original_language) {
        mOriginalLanguage = original_language;
        mValues.put(MovieTable.ORIGINAL_LANGUAGE, original_language);
    }

    public String getOriginalLanguage() {
            return mOriginalLanguage;
    }


    public void setTitle(String title) {
        mTitle = title;
        mValues.put(MovieTable.TITLE, title);
    }

    public String getTitle() {
            return mTitle;
    }


    public void setBackdropPath(String backdrop_path) {
        mBackdropPath = backdrop_path;
        mValues.put(MovieTable.BACKDROP_PATH, backdrop_path);
    }

    public String getBackdropPath() {
            return mBackdropPath;
    }


    public void setPopularity(double popularity) {
        mPopularity = popularity;
        mValues.put(MovieTable.POPULARITY, popularity);
    }

    public double getPopularity() {
            return mPopularity;
    }


    public void setVoteCount(double vote_count) {
        mVoteCount = vote_count;
        mValues.put(MovieTable.VOTE_COUNT, vote_count);
    }

    public double getVoteCount() {
            return mVoteCount;
    }


    public void setVoteAverage(double vote_average) {
        mVoteAverage = vote_average;
        mValues.put(MovieTable.VOTE_AVERAGE, vote_average);
    }

    public double getVoteAverage() {
            return mVoteAverage;
    }


    public void setIsFavourite(boolean is_favourite) {
        mIsFavourite = is_favourite;
        mValues.put(MovieTable.IS_FAVOURITE, is_favourite);
    }

    public boolean getIsFavourite() {
            return mIsFavourite;
    }


    public void setDateFavourited(String date_favourited) {
        mDateFavourited = date_favourited;
        mValues.put(MovieTable.DATE_FAVOURITED, date_favourited);
    }

    public String getDateFavourited() {
            return mDateFavourited;
    }

    public static List<Movie> listFromCursor(Cursor cursor) {
        List<Movie> list = new ArrayList<Movie>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Movie(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    protected Movie(Parcel in) {
        mPosterPath = in.readString();
        setPosterPath(mPosterPath);
        mAdult = in.readByte() != 0x00;
        setAdult(mAdult);
        mOverview = in.readString();
        setOverview(mOverview);
        mReleaseDate = in.readString();
        setReleaseDate(mReleaseDate);
        mId = in.readInt();
        setId(mId);
        mOriginalTitle = in.readString();
        setOriginalTitle(mOriginalTitle);
        mOriginalLanguage = in.readString();
        setOriginalLanguage(mOriginalLanguage);
        mTitle = in.readString();
        setTitle(mTitle);
        mBackdropPath = in.readString();
        setBackdropPath(mBackdropPath);
        mPopularity = in.readDouble();
        setPopularity(mPopularity);
        mVoteCount = in.readDouble();
        setVoteCount(mVoteCount);
        mVoteAverage = in.readDouble();
        setVoteAverage(mVoteAverage);
        mIsFavourite = in.readByte() != 0;
        setIsFavourite(mIsFavourite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeByte((byte) (mAdult ? 0x01 : 0x00));
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mTitle);
        dest.writeString(mBackdropPath);
        dest.writeDouble(mPopularity);
        dest.writeDouble(mVoteCount);
        dest.writeDouble(mVoteAverage);
        dest.writeByte((byte) (mIsFavourite ? 1 : 0));
    }

    @SuppressWarnings("unused")
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}