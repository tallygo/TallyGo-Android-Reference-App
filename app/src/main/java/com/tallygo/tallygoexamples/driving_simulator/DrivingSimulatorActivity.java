package com.tallygo.tallygoexamples.driving_simulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.sdk.TGMutableConfiguration;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGUtils;

import java.util.List;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class DrivingSimulatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();
    }

    private void startDrivingSimulator() {
        List<LatLng> waypoints = TGUtils.simulatedWaypoints(2);

        simulateDrivingV1(waypoints.get(0));

        //create the request
        TGRouteRequest routeRequest = new TGRouteRequest.Builder(waypoints).build();
        TGLauncher.startTurnByTurn(this, routeRequest);
    }

    private void simulateDrivingV1(@NonNull LatLng simulatedCurrentLocation) {
        //here we edit the global TallyGo configuration directly
        TGMutableConfiguration configuration = TallyGo.getInstance(this).getMutableConfiguration();
        configuration.setSimulatedLocation(simulatedCurrentLocation);
        configuration.getSimulateNavigationSettings().setSimulated(true);
        TallyGo.getInstance(this).updateConfiguration(configuration);
    }

    private void simulateDrivingV2(@NonNull LatLng simulatedCurrentLocation) {
        //here we use a convenience method to achieve the same result more quickly
        boolean simulate = true;
        boolean clickToUpdate = false;
        TallyGo.getInstance(this).enableSimulation(simulate, clickToUpdate, simulatedCurrentLocation);
    }
}
