package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.asynctask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.R;
import com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.data.model.Trailer;

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
 * Created by kishor on 9/3/16.
 */
public class FetchMovieTrailers extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final String TAG = "FetchMovieTrailers";
    private Context context;
    private ArrayList<Trailer> trailers;

    public FetchMovieTrailers(Context context) {
        this.context = context;
        trailers = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... id) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String trailersJson = null;

        try {
            // Construct the URL
            //http://api.themoviedb.org/3/sort_by=popularity.desc&api_key=XXX
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority("api.themoviedb.org");
            builder.appendPath("3");
            builder.appendPath("movie");
            builder.appendPath(id[0]);
            builder.appendPath("videos");
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
            trailersJson = buffer.toString();

            Log.d(TAG, trailersJson);

            JSONObject jsonObject = new JSONObject(trailersJson);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setId(object.getString("id"));
                trailer.setKey(object.getString("key"));
                trailer.setName(object.getString("name"));
                trailer.setSite(object.getString("site"));
                trailer.setType(object.getString("type"));
                trailers.add(trailer);
            }

            return trailers;
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
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        Intent intent = new Intent("trailers-fetched");
        intent.putParcelableArrayListExtra("trailerList", trailers);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
