package com.tallygo.tallygoexamples;

import android.app.Application;

import timber.log.Timber;

public class ExamplesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        plantDebugTree();
    }

    private void plantDebugTree() {
        Timber.plant(new Timber.DebugTree());
    }
}
