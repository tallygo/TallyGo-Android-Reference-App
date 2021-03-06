package com.tallygo.tallygoexamples.report_server;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 4/19/18
//  Copyright © 2017 TallyGo. All rights reserved.
//

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.sdk.TGMapUtils;
import com.tallygo.tallygoandroid.sdk.navigation.TGNavigationRepository;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportCurrentLocServerActivity extends AppCompatActivity {
    private final UUID DRIVER_ID = UUID.randomUUID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TGNavigationRepository.NavigationListener listener = new TGNavigationRepository.NavigationListener() {
            @Override
            public void onRouteLocationUpdated(Location location) {
                reportLocation(location);
            }
        };

        TGNavigationRepository.getDefaultNavigationAdapter(getApplication(),
                new TGNavigationRepository.AdapterCallback() {
                    @Override
                    public void onReady(@NonNull TGNavigationRepository.Adapter adapter) {
                        adapter.setNavigationListener(listener);
                    }
                });

        //launch simulated navigation
        TGLauncher.launchSimulatedNavigation(this, 2);
        finish();
    }


    private static final double DISTANCE_BETWEEN_REPORTS = 100d; //meters
    private static final long DURATION_BETWEEN_REPORTS = 60000L; //ms
    private static final String REPORT_URL = "http://localhost:3200/drivers/current_location";

    private LatLng lastReportedPoint;
    private long lastReportedTime;

    private void reportLocation(Location location) {
        final LatLng coordinate = TGUtils.locToCoor(location);
        if (coordinate == null) {
            return;
        }

        if (lastReportedPoint != null
                && TGMapUtils.metersBetweenPoints(lastReportedPoint, coordinate) < DISTANCE_BETWEEN_REPORTS
                && System.currentTimeMillis() < lastReportedTime + DURATION_BETWEEN_REPORTS) {
            //do not report unless one is satisfied
            return;
        }
        lastReportedTime = System.currentTimeMillis();
        lastReportedPoint = coordinate;

        //we use Volley but you may use whatever library you prefer
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest putRequest = new StringRequest(Request.Method.PUT, REPORT_URL,
                response -> {
                    //success
                },
                error -> {
                    //failure to upload
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                params.put("latitude", String.valueOf(coordinate.getLatitude()));
                params.put("longitude", String.valueOf(coordinate.getLongitude()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();

                headers.put("X-Temporary-ID", DRIVER_ID.toString());
                return headers;
            }
        };

        queue.add(putRequest);
    }
}
