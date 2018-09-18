package com.tallygo.tallygoexamples.obtain_route;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoandroid.interfaces.TGLocationListener;
import com.tallygo.tallygoandroid.sdk.TGNavigationEndpoint;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteResponse;
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.sdk.route.TGRouteRepository;
import com.tallygo.tallygoandroid.utils.TGUtils;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/2/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class GetRouteRawDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        obtainLocation();
    }

    private void obtainLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        TGUtils.getRealCurrentLocation(getBaseContext(), new TGLocationListener() {
            @Override
            public void onLocationUpdated(@Nullable Location currentLocation) {
                obtainRoute(currentLocation);
            }
        });
    }

    private void obtainRoute(Location currentLocation) {
        // You can create any two points and see different routes

        // Create the request
        TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCoordinate(currentLocation)
                .addCoordinate(34.101558d, -118.340944d) // Grauman's Chinese Theatre
                .build();

        TallyGo.getTGNavigation().route(TGRouteRepository.getInstance(this),
                routeRequest, new TGNavigationEndpoint.TGRouteCallback() {
            @Override
            public void onSuccess(TGRouteResponse tgRouteResponse) {
                TGRoute route = tgRouteResponse.getRoute();
            }

            @Override
            public void onFailure(Exception e) {
                // Something failed, this could be a no-network error -- handle as you please
            }
        });
    }
}
