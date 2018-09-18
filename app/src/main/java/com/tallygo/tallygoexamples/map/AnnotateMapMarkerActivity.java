package com.tallygo.tallygoexamples.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.fragments.map.base.TGBaseSupportMapFragment;
import com.tallygo.tallygoandroid.sdk.TGMap;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;
import com.tallygo.tallygoexamples.R;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class AnnotateMapMarkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_map_fragment_holder);

        //we use a button here to activate the functionality
        findViewById(R.id.fl_base_map_activate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annotateMap();
            }
        });

        TGLauncher.startBaseSupportMapFragment(this, R.id.fl_base_map_fragment_holder);
    }

    @Nullable
    private TGMap getTGMap() {
        TGBaseSupportMapFragment frag = (TGBaseSupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_base_map_fragment_holder);
        if (frag == null) {
            return null;
        }
        return frag.getMap();
    }

    private void annotateMap() {
        TGMap tgMap = getTGMap();
        if (tgMap == null) {
            TGToastHelper.showShort(this, "Map not yet ready");
            return;
        }

        LatLng position = new LatLng(34.101558d, -118.340944d);

        MarkerOptions options = new MarkerOptions();
        options.setPosition(position);
        options.setTitle("Grauman's Chinese Theatre");
        options.setSnippet("6925 Hollywood Blvd, Hollywood, CA");

        Marker marker = tgMap.addMarker(options);

        tgMap.animateCamera(CameraUpdateFactory.newLatLng(position));
    }

}
