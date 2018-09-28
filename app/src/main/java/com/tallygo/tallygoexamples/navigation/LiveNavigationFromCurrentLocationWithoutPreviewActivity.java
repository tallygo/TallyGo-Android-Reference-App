package com.tallygo.tallygoexamples.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;

public class LiveNavigationFromCurrentLocationWithoutPreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLiveNavigation();
        finish();
    }

    private void startLiveNavigation() {
        //create the request
        final TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCurrentLocationWaypoint() //Origin
                .addCoordinate(34.119190d, -118.300348d) //Griffith Observatory (mid)
                .addCoordinate(34.101558d, -118.340944d) //Grauman's Chinese Theatre (mid)
                .addCoordinate(34.102078d, -118.334236d) //Hollywood Walk of Fame (mid)
                .addCoordinate(34.011441d, -118.494932d) //Santa Monica Pier (destination)
                .build();

        // Making sure the simulation is OFF
        TallyGo.getInstance().enableSimulation(false);

        // Start the Turn-by-Turn navigation
        TGLauncher.startTurnByTurn(this, routeRequest);
    }
}
