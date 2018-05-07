package com.tallygo.tallygoexamples.find_api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.tallygo.tallygoandroid.endpoint.search.TGSearchResult;
import com.tallygo.tallygoandroid.fragments.map.base.TGBaseSupportMapFragment;
import com.tallygo.tallygoandroid.sdk.TGMap;
import com.tallygo.tallygoandroid.sdk.TGSearchEndpoint;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.search.TGSearchRequest;
import com.tallygo.tallygoandroid.sdk.search.TGSearchResponse;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;
import com.tallygo.tallygoexamples.R;

import java.util.List;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class LatLonFromStreetAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_map_fragment_holder);

        //we use a button here to activate the functionality
        findViewById(R.id.fl_base_map_activate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress();
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

    private void searchAddress() {
        TGMap tgMap = getTGMap();
        if (tgMap == null) {
            TGToastHelper.showShort(this, "Map not yet available");
            return;
        }

        LatLng centerScreen = tgMap.getScreenCenterLatLng();
        LatLngBounds screenBounds = tgMap.getScreenBounds(); // Get bounds from map
        TGSearchRequest request = new TGSearchRequest("sushi", centerScreen)
                .setSearchBounds(screenBounds, centerScreen);

        TGSearchEndpoint.TGSearchCallback callback = new TGSearchEndpoint.TGSearchCallback() {
            @Override
            public void onSuccess(TGSearchResponse tgSearchResponse) {
                List<TGSearchResult> results = tgSearchResponse.getResults();
                if (results.isEmpty()) {
                    // Handle failure
                    return;
                }
                // Use the first result as it is likely the best (or filter based on your needs)
                LatLng latLng = results.get(0).getLocation();
                TGToastHelper.showShort(getBaseContext(), "Search successful");
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                TGToastHelper.showShort(getBaseContext(), "Search failed");
            }
        };

        TallyGo.getTGSearch().search(request, callback);
    }
}
