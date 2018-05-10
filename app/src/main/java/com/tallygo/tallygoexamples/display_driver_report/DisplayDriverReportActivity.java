package com.tallygo.tallygoexamples.display_driver_report;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tallygo.tallygoandroid.fragments.map.base.TGBaseSupportMapFragment;
import com.tallygo.tallygoandroid.sdk.TGMap;
import com.tallygo.tallygoandroid.utils.TGIconFactory;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoexamples.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import timber.log.Timber;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/2/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class DisplayDriverReportActivity extends AppCompatActivity {

    public enum EventType {
        ETA,
        CURRENT_LOCATION;

        public static EventType fromJson(String json) {
            for (EventType eventType: values()) {
                if (eventType.name().toLowerCase().equals(json)) {
                    return eventType;
                }
            }
            return null;
        }
    }

    private WebSocket websocket;

    private Marker driverLocationMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_driver_report_activity);
        TGLauncher.startBaseSupportMapFragment(this, R.id.fl_map_fragment_holder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWebSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopWebSocket();
    }

    @Nullable
    private TGMap getTGMap() {
        TGBaseSupportMapFragment frag = (TGBaseSupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_map_fragment_holder);
        if (frag == null) {
            return null;
        }
        return frag.getMap();
    }

    private void startWebSocket() {
        OkHttpClient socketClient = new OkHttpClient();
        Request request = new Request.Builder().url("ws://localhost:3200/").build();
        websocket = socketClient.newWebSocket(request, socketListener);
        socketClient.dispatcher().executorService().shutdown();
    }

    private void stopWebSocket() {
        websocket.close(4000, null);
    }

    private final WebSocketListener socketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
           Timber.i("WebSocket opened");
        }
        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            //handle our messages on the main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onSocketReceiveMessage(text);
                }
            });
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            Timber.i("WebSocket closing");
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Timber.e("WebSocket failure");
        }
    };

    private void onSocketReceiveMessage(String text) {
        if (text == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject(text);
            EventType eventType = EventType.fromJson(json.getString("event_type"));
            JSONObject payload = json.getJSONObject("payload");
            if (eventType == null || payload == null) {
                return;
            }

            switch (eventType) {
                case ETA:
                    handleEtaPayload(payload);
                    break;
                case CURRENT_LOCATION:
                    handleLocationPayload(payload);
                    break;
            }
        } catch (JSONException ignored) {}
    }

    private void handleEtaPayload(JSONObject payload) throws JSONException {
        String eta = payload.getString("ETA");
        updateEtaText(eta);
    }

    private void updateEtaText(String etaText) {
        TextView etaView = findViewById(R.id.tv_eta_text);
        etaView.setText(etaText);
        etaView.setVisibility(View.VISIBLE);
    }

    private void handleLocationPayload(JSONObject payload) throws JSONException {
        double lat = payload.getDouble("latitude");
        double lon = payload.getDouble("longitude");
        LatLng location = new LatLng(lat, lon);
        updateDriverLocation(location);
    }

    private void updateDriverLocation(LatLng location) {
        if (getTGMap() == null) {
            return;
        }
        if (driverLocationMarker == null) {
            //create marker
            Icon icon = TGIconFactory.getInstance().getDefaultIcon(getBaseContext(), Color.BLUE);
            MarkerOptions options = new MarkerOptions().position(location).icon(icon).setTitle("Driver Location");
            driverLocationMarker = getTGMap().addMarker(options);
        } else {
            //update position
            driverLocationMarker.setPosition(location);
        }
        getTGMap().animateCamera(CameraUpdateFactory.newLatLng(location));
    }
}
