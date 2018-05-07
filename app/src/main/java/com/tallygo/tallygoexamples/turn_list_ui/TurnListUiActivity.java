package com.tallygo.tallygoexamples.turn_list_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.sdk.TGNavigationEndpoint;
import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteRequest;
import com.tallygo.tallygoandroid.sdk.navigation.TGRouteResponse;
import com.tallygo.tallygoandroid.sdk.route.TGRoute;
import com.tallygo.tallygoandroid.sdk.route.TGRouteRepository;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGToastHelper;
import com.tallygo.tallygoandroid.utils.TGUtils;

import java.util.List;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class TurnListUiActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTallyGo();
    }

    private void initTallyGo() {
        TallyGo.initializeFromMetaData(this, new TallyGo.InitializeCallback() {
            @Override
            public void onSuccess() {
                startTurnList();
            }
            @Override
            public void onFailure(Exception e) { }
            @Override
            public void onRetryInit(long l) { }
        });
    }

    private void startTurnList() {
        List<LatLng> waypoints = TGUtils.simulatedWaypoints(2);

        //create the request
        TGRouteRequest routeRequest = new TGRouteRequest.Builder(waypoints).build();

        TallyGo.getTGNavigation().route(TGRouteRepository.getInstance(this), routeRequest, new TGNavigationEndpoint.TGRouteCallback() {
            @Override
            public void onSuccess(TGRouteResponse tgRouteResponse) {
                TGRoute route = tgRouteResponse.getRoute();
                TGLauncher.startTurnList(TurnListUiActivity.this, route.getRouteId());
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                TGToastHelper.showShort(getBaseContext(), "Failed to get route");
            }
        });
    }

}
