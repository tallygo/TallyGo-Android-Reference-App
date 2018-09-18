package com.tallygo.tallygoexamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final ExampleTypeAdapter exampleTypeAdapter = new ExampleTypeAdapter(this, this::startExample);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(exampleTypeAdapter);
    }

    private void startExample(ExampleType exampleType) {
        Intent intent = new Intent(this, exampleType.getActivityClass());

        startActivity(intent);
    }
}
