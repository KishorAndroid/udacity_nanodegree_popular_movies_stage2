package com.appdevelopkar.stage2.popularmovies.popularmoviesstage2.application;

import android.app.Application;
import android.content.ContextWrapper;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kishor on 27/3/16.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initializr the Crashalytics
        Fabric.with(this, new Crashlytics());
    }
}
