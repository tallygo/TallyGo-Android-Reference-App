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
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;

import java.util.List;

public class SimulatedNavigationWithNewWaypointWhileNavigatingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();

        new Handler(Looper.getMainLooper()).postDelayed(this::addNewWaypoint, 30 * 1000);
        //finish();
    }

    private void addNewWaypoint() {
        TGNavigationRepository.getDefaultNavigationAdapter(this, this, new TGNavigationRepository.AdapterCallback() {
            @Override
            public void onReady(@NonNull TGNavigationRepository.Adapter navAdapter) {
                if( navAdapter.isNavRunning() ) {
                    final TGRoute currentRoute = navAdapter.getCurrentRoute();
                    final Integer currentRouteSegment = navAdapter.getCurrentSegmentIndex();
                    final List<TGWaypoint> currentWaypoints = currentRoute.getWaypoints();
                    final List<TGWaypoint> newWaypoints = currentWaypoints.subList(currentRouteSegment + 1, currentWaypoints.size());

                    newWaypoints.add(0, new TGWaypoint(navAdapter.getRouteLocation())); //Start from current location
                    newWaypoints.add(1, new TGWaypoint(34.101558d, -118.340944d)); //Grauman's Chinese Theatre (mid waypoint)

                    TGToastHelper.showLong(SimulatedNavigationWithNewWaypointWhileNavigatingActivity.this, "A new delivery order has come in. You are being rerouted to a new destination...");
                    navAdapter.rerouteRequired(navAdapter.getRouteLocation(), currentRoute, newWaypoints, false);
                }
            }
        });
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
