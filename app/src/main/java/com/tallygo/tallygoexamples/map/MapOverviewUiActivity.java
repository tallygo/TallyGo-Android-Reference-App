package com.tallygo.tallygoexamples.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoandroid.sdk.TGNavigationEndpoint;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteResponse;
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.sdk.route.TGRouteRepository;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class MapOverviewUiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTallyGo();
    }

    private void initTallyGo() {
        TallyGo.initializeFromMetaData(new TallyGo.InitializeCallback() {
            @Override
            public void onSuccess() {
                startOverview();
                finish();
            }
            @Override
            public void onFailure(Exception e) { }
            @Override
            public void onRetryInit(long l) { }
        });
    }

    private void startOverview() {
        //create the request
        TGRouteRequest routeRequest = new TGRouteRequest.Builder()
                .addCoordinate(34.101558d, -118.340944d) //Grauman's Chinese Theatre (origin)
                .addCoordinate(34.011441d, -118.494932d)//Santa Monica Pier (destination)
                .build();

        TallyGo.getTGNavigation().route(TGRouteRepository.getInstance(this), routeRequest, new TGNavigationEndpoint.TGRouteCallback() {
            @Override
            public void onSuccess(TGRouteResponse tgRouteResponse) {
                TGRoute route = tgRouteResponse.getRoute();
                TGLauncher.startMapOverview(MapOverviewUiActivity.this, route.getRouteId());
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                TGToastHelper.showShort(getBaseContext(), "Failed to get route");
            }
        });
    }
}
