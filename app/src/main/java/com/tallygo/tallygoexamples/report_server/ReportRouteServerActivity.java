package com.tallygo.tallygoexamples.report_server;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 4/19/18
//  Copyright © 2017 TallyGo. All rights reserved.
//

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tallygo.tallygoandroid.endpoint.navigation.TGArrivalInfo;
import com.tallygo.tallygoandroid.endpoint.navigation.TGNotificationInfo;
import com.tallygo.tallygoandroid.endpoint.navigation.TGTurnInfo;
import com.tallygo.tallygoandroid.sdk.TGNavigationState;
import com.tallygo.tallygoandroid.sdk.navigation.TGNavigationRepository;
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.utils.TGLauncher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReportRouteServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TGNavigationRepository.NavigationListener listener = new TGNavigationRepository.NavigationListener() {
            @Override
            public void onNavigationStateChange(@Nullable TGNavigationState tgNavigationState) {}

            @Override
            public void onNotificationInfoUpdated(@Nullable TGNotificationInfo tgNotificationInfo) {}

            @Override
            public void onTurnInfoUpdated(@Nullable TGTurnInfo[] tgTurnInfos) {}

            @Override
            public void onArrivalInfoUpdated(@Nullable TGArrivalInfo tgArrivalInfo) {}

            @Override
            public void onRouteUpdated(TGRoute tgRoute) {
                if (tgRoute == null) {
                    return;
                }
                reportRoute(tgRoute);
            }

            @Override
            public void onRouteLocationUpdated(Location location) {}

            @Override
            public void onTurnPercentUpdated(double v) {}

            @Override
            public void onTripPercentUpdated(double v) {}

            @Override
            public void onTurnPassed() {}
        };

        TGNavigationRepository.getInstance().getDefaultNavigationAdapter(getApplication())
                .setNavigationListener(listener);

        //launch simulated navigation
        TGLauncher.launchSimulatedNavigation(this, 2);
    }

    private static final String REPORT_URL = "http://192.168.86.69:3200/drivers/route_segment";

    private TGRoute lastRoute;

    private void reportRoute(final TGRoute route) {
        if (route == lastRoute) {
            //no assurances that you will never receive the same route through this interface
            return;
        }
        lastRoute = route;

        //we use Volley but you may use whatever library you prefer
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //success
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //failure to upload
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                try {
                    JSONObject json = route.getSegments().get(0).getJson();
                    for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                        String key = it.next();
                        params.put(key, json.get(key).toString());
                    }
                } catch (JSONException ignored) {}
                return params;
            }
        };
        queue.add(putRequest);
    }
}
