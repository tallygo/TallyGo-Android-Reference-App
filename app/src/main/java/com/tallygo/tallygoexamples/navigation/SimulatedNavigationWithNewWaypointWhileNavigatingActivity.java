package com.tallygo.tallygoexamples.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.endpoint.navigation.TGWaypoint;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGNavigationRepository;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;

import java.util.LinkedList;
import java.util.List;

public class SimulatedNavigationWithNewWaypointWhileNavigatingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();

        // Wait 30 seconds after starting navigation before adding a waypoint.
        // This simulates a server request that might automatically initiate this kind of action based on a realtime event.
        // Instead of a timer, you would use your own custom networking code to initiate this part.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            final TGWaypoint newWaypoint = new TGWaypoint(34.101558d, -118.340944d); // New Waypoint: Grauman's Chinese Theatre

            addNewWaypoint(newWaypoint);
            finish();
        }, 30 * 1000);
    }

    private void addNewWaypoint(TGWaypoint newWaypoint) {
        TGNavigationRepository.addWaypointToStartOfActiveRoute(new TGNavigationRepository.RerouteResultListener() {
            @Override
            public void onSuccess(List<TGWaypoint> waypointsInNewRoute) {
                TGToastHelper.showLong(SimulatedNavigationWithNewWaypointWhileNavigatingActivity.this, "A new delivery order has come in. You are being rerouted to a new destination...");
            }
        }, newWaypoint);
    }

    private void startDrivingSimulator() {
        //create the request
        final TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCoordinate(34.119190d, -118.300348d) //Griffith Observatory (origin)
                .addCoordinate(34.011441d, -118.494932d) //Santa Monica Pier (destination)
                .build();

        // The first waypoint is going to be simulated current location
        enableSimulation(routeRequest.getWayPoints().get(0).getCoordinate());

        // Start the turn-by-turn navigation
        TGLauncher.startTurnByTurn(this, routeRequest);
    }

    private void enableSimulation(@NonNull LatLng simulatedCurrentLocation) {
        //here we use a convenience method to achieve the same result more quickly
        final boolean simulate = true;
        final boolean clickToUpdate = false;

        TallyGo.getInstance().enableSimulation(simulate, clickToUpdate, simulatedCurrentLocation);
    }
}
