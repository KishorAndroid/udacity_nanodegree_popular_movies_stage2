package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.asynctask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.R;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Movie;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.provider.PopularMoviesProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kishor on 4/2/16.
 */
public class FetchMovieList extends AsyncTask<String, Void, ArrayList<Movie>> {

    private ArrayList<Movie> movies;
    private Context context;
    private String sortOrder;

    public FetchMovieList(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        movies = new ArrayList<>();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... sortBy) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJson = null;

        try {
            sortOrder = sortBy[0];
            // Construct the URL
            //http://api.themoviedb.org/3/sort_by=popularity.desc&api_key=XXX
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority("api.themoviedb.org");
            builder.appendPath("3");
            builder.appendPath("discover");
            builder.appendPath("movie");
            builder.appendQueryParameter("sort_by", sortOrder);
            builder.appendQueryParameter("api_key", context.getResources().getString(R.string.API_KEY));
            String myUrl = builder.build().toString();

            URL url = new URL(myUrl);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJson = buffer.toString();
            JSONObject jsonObject = new JSONObject(moviesJson);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setPosterPath(object.getString("poster_path"));
                movie.setAdult(object.getBoolean("adult"));
                movie.setOverview(object.getString("overview"));
                movie.setReleaseDate(object.getString("release_date"));
                movie.setId(object.getInt("id"));
                movie.setOriginalTitle(object.getString("original_title"));
                movie.setTitle(object.getString("title"));
                movie.setPopularity(object.getDouble("popularity"));
                movie.setVoteCount(object.getDouble("vote_count"));
                movie.setVoteAverage(object.getDouble("vote_average"));
                movies.add(movie);
            }

            return movies;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> fetchedMovies) {
        super.onPostExecute(movies);
        Intent intent = new Intent("movies-fetched");
        intent.putExtra("FILTER_NAME",sortOrder);
        intent.putParcelableArrayListExtra("movieList", fetchedMovies);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
