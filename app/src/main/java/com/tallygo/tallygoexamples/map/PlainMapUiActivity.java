package com.tallygo.tallygoexamples.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoandroid.sdk.TallyGo;
import com.tallygo.tallygoandroid.utils.TGLauncher;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/7/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class PlainMapUiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making sure the simulation is OFF
        TallyGo.getInstance().enableSimulation(false);

        // Launch the map
        TGLauncher.startMap(this);
        finish();
    }
}
