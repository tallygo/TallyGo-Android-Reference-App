package com.tallygo.tallygoexamples.get_navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGUtils;

import java.util.List;

public class GetNavPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDrivingSimulator();
    }

    private void startDrivingSimulator() {
        List<LatLng> waypoints = TGUtils.simulatedWaypoints(2);

        simulateDriving(waypoints.get(0));

        //create the request
        TGRouteRequest routeRequest = new TGRouteRequest.Builder(waypoints).build();
        TGLauncher.startPreview(this, routeRequest);
    }

    private void simulateDriving(@NonNull LatLng simulatedCurrentLocation) {
        boolean simulate = true;
        boolean clickToUpdate = false;
        TallyGo.getInstance(this).enableSimulation(simulate, clickToUpdate, simulatedCurrentLocation);
    }

}
