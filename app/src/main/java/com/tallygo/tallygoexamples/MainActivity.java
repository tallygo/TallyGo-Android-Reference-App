package com.tallygo.tallygoexamples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import timber.log.Timber;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plantDebugTree();
        setListAdapter(new MainAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ExampleType exampleType = ExampleType.values()[position];
        startExample(exampleType);
    }

    private void startExample(ExampleType exampleType) {
        Intent intent = new Intent(this, exampleType.getActivityClass());
        startActivity(intent);
    }

    private void plantDebugTree() {
        Timber.plant(new Timber.DebugTree());
    }
}
