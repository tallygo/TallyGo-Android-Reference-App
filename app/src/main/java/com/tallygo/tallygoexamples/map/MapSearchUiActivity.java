package com.tallygo.tallygoexamples.map;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.tallygo.tallygoandroid.endpoint.search.TGSearchResult;
import com.tallygo.tallygoandroid.fragments.MapType;
import com.tallygo.tallygoandroid.fragments.map.base.TGBaseSupportMapFragment;
import com.tallygo.tallygoandroid.fragments.map.implementation.TGMapSearchSupportFragment;
import com.tallygo.tallygoandroid.fragments.map.implementation.model.TGMapViewModel;
import com.tallygo.tallygoandroid.interfaces.TGMapHolder;
import com.tallygo.tallygoandroid.lifecycle.LoadableData;
import com.tallygo.tallygoandroid.sdk.TGMap;
import com.tallygo.tallygoandroid.sdk.TGMapView;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;
import com.tallygo.tallygoandroid.utils.TGUtils;
import com.tallygo.tallygoexamples.R;

import java.util.Map;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//

/**
 * Note that internally {@link TGMapSearchSupportFragment} requires the parent activity or fragment
 * to implement {@link TGMapHolder}, so here we implement it and delegate the methods to the
 * base map fragment that we create
 */
public class MapSearchUiActivity extends AppCompatActivity implements TGMapHolder {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_map_fragment_holder);

        final TGMapViewModel mapViewModel = ViewModelProviders.of(this).get(TGMapViewModel.class);
        mapViewModel.getDataModel().observe(this, dataModel -> {
            if (dataModel != null) {
                //here is how we access the results
                updateSearchMarkers(dataModel.getSearchMarkers());
            }
        });

        //we use a button here to activate the functionality
        findViewById(R.id.fl_base_map_activate).setOnClickListener(v -> addSearchUi());

        TGLauncher.startBaseSupportMapFragment(this, R.id.fl_base_map_fragment_holder);
    }

    private void updateSearchMarkers(LoadableData<Map<TGSearchResult, MarkerOptions>> searchMarkers) {
        if (getMap() == null) {
            return;
        }
        getMap().clear();
        Map<TGSearchResult, MarkerOptions> markers = searchMarkers.getValue();
        if (markers == null || markers.isEmpty()) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions markerOptions: markers.values()) {
            getMap().addMarker(markerOptions);
            builder.include(markerOptions.getPosition());
        }
        getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), TGUtils.dpToPx(30)));
    }

    private void addSearchUi() {
        if (getSupportFragmentManager().findFragmentByTag(TGMapSearchSupportFragment.TAG) != null) {
            TGToastHelper.showShort(this, "Search fragment already shown");
            return;
        }

        //your parent fragment/activity must implement TGMapHolder
        final boolean focusSearchOnStart = false;
        final TGMapSearchSupportFragment fragment = TGMapSearchSupportFragment.newInstance(MapType.MAP, focusSearchOnStart);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_base_map_fragment_holder, fragment, TGMapSearchSupportFragment.TAG)
                .commit();
    }

    //
    // Here we delegate the map handling to the TGBaseMapFragment
    //

    @Nullable
    @Override
    public TGMapView getMapView() {
        TGBaseSupportMapFragment frag = (TGBaseSupportMapFragment) getSupportFragmentManager()
                .findFragmentByTag(TGBaseSupportMapFragment.TAG);
        if (frag == null) {
            return null;
        }

        return frag.getMapView();
    }

    @Nullable
    @Override
    public TGMap getMap() {
        TGBaseSupportMapFragment frag = (TGBaseSupportMapFragment) getSupportFragmentManager()
                .findFragmentByTag(TGBaseSupportMapFragment.TAG);
        if (frag == null) {
            return null;
        }
        return frag.getMap();
    }
}
