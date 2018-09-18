package com.tallygo.tallygoexamples.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;

public class SimulatedNavigationFromCurrentLocationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();
        finish();
    }

    private void startDrivingSimulator() {
        //create the request
        final TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCurrentLocationWaypoint() //Origin
                .addCoordinate(34.119190d, -118.300348d) //Griffith Observatory (mid)
                .addCoordinate(34.011441d, -118.494932d) //Santa Monica Pier (destination)
                .build();

        TallyGo.getInstance().enableSimulation(true);
        TGLauncher.startTurnByTurn(this, routeRequest);
    }
}
