package com.tallygo.tallygoexamples.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;

public class SimulatedNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();
        finish();
    }

    private void startDrivingSimulator() {
        //create the request
        final TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCoordinate(34.101558d, -118.340944d) //Grauman's Chinese Theatre (origin)
                .addCoordinate(34.011441d, -118.494932d)//Santa Monica Pier (destination)
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
