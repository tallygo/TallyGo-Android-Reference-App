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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tallygo.tallygoandroid.endpoint.navigation.TGArrivalInfo;
import com.tallygo.tallygoandroid.endpoint.navigation.TGNotificationInfo;
import com.tallygo.tallygoandroid.endpoint.navigation.TGTurnInfo;
import com.tallygo.tallygoandroid.sdk.TGNavigationState;
import com.tallygo.tallygoandroid.sdk.navigation.TGNavigationRepository;
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportEtaServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TGNavigationRepository.NavigationListener listener = new TGNavigationRepository.NavigationListener() {
            @Override
            public void onNavigationStateChange(@Nullable TGNavigationState tgNavigationState) {}

            @Override
            public void onNotificationInfoUpdated(@Nullable TGNotificationInfo tgNotificationInfo) {}

            @Override
            public void onTurnInfoUpdated(@Nullable TGTurnInfo[] tgTurnInfos) {}

            @Override
            public void onArrivalInfoUpdated(@Nullable TGArrivalInfo tgArrivalInfo) {
                if (tgArrivalInfo == null) {
                    return;
                }
                reportEta(tgArrivalInfo.getArrivalDate());
            }

            @Override
            public void onRouteUpdated(@NonNull TGRoute tgRoute) {}

            @Override
            public void onRouteLocationUpdated(Location location) {}

            @Override
            public void onTurnPercentUpdated(double v) {}

            @Override
            public void onTripPercentUpdated(double v) {}

            @Override
            public void onTurnPassed() {}
        };

        TGNavigationRepository.getDefaultNavigationAdapter(getApplication(),
                adapter -> adapter.setNavigationListener(listener));

        //launch simulated navigation
        TGLauncher.launchSimulatedNavigation(this, 2);
    }

    private static final long DURATION_BETWEEN_REPORTS = 60000L; //ms
    private static final String REPORT_URL = "http://localhost:3200/drivers/eta";

    private long lastReportedTime;

    private void reportEta(final Date arrivalDate) {
        if (System.currentTimeMillis() < lastReportedTime + DURATION_BETWEEN_REPORTS) {
            //do not report unless 60 sec have passed since last report
            return;
        }
        lastReportedTime = System.currentTimeMillis();

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
                params.put("ETA", TGUtils.getUtcTime(arrivalDate));
                return params;
            }

        };
        queue.add(putRequest);
    }
}
