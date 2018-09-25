package com.tallygo.tallygoexamples.report_server;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 4/19/18
//  Copyright © 2017 TallyGo. All rights reserved.
//

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

        final TGNavigationRepository.NavigationListener listener = new TGNavigationRepository.NavigationListener() {
            @Override
            public void onRouteUpdated(TGRoute tgRoute) {
                if (tgRoute == null) {
                    return;
                }
                reportRoute(tgRoute);
            }
        };

        TGNavigationRepository.getDefaultNavigationAdapter(getApplication(),
                new TGNavigationRepository.AdapterCallback() {
                    @Override
                    public void onReady(@NonNull TGNavigationRepository.Adapter adapter) {
                        adapter.setNavigationListener(listener);
                    }

                    @Override
                    public void onDisconnected() { }
                });

        //launch simulated navigation
        TGLauncher.launchSimulatedNavigation(this, 2);
        finish();
    }

    private static final String REPORT_URL = "http://localhost:3200/drivers/route_segment";

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
