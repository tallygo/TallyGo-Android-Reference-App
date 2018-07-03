package com.tallygo.tallygoexamples.driver_motion;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.tallygo.tallygoandroid.fragments.map.base.TGBaseSupportMapFragment;
import com.tallygo.tallygoandroid.sdk.TGMap;
import com.tallygo.tallygoandroid.utils.TGIconFactory;
import com.tallygo.tallygoandroid.utils.TGLauncher;
import com.tallygo.tallygoandroid.utils.TGMarkerAnimator;
import com.tallygo.tallygoandroid.utils.TGUtils;
import com.tallygo.tallygoexamples.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import timber.log.Timber;

public class DisplayDriverMotionActivity extends AppCompatActivity {

    public enum EventType {
        MOTION;

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
        setContentView(R.layout.display_driver_motion_activity);
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
            new Handler(Looper.getMainLooper()).post(() -> onSocketReceiveMessage(text));
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
                case MOTION:
                    handleMotionPayload(payload);
                    break;
            }
        } catch (JSONException ignored) {}
    }

    private void handleMotionPayload(JSONObject payload) throws JSONException {
        int timeInterval = payload.getInt("timeInterval");

        List<LatLng> pointUpdates = new ArrayList<>();
        JSONArray pointUpdatesJson = new JSONArray(payload.getString("points"));
        for (int i = 0; i < pointUpdatesJson.length(); i++) {
            JSONArray pointUpdateJson = pointUpdatesJson.getJSONArray(i);
            double lat = pointUpdateJson.getDouble(0);
            double lon = pointUpdateJson.getDouble(1);
            pointUpdates.add(new LatLng(lat, lon));
        }

        updateDriverMotion(pointUpdates, timeInterval);
    }

    private void updateDriverMotion(List<LatLng> updates, int timeInterval) {
        if (getTGMap() == null || updates == null || updates.isEmpty()) {
            return;
        }

        if (updates.size() > 1) {
            LatLngBounds bounds = new LatLngBounds.Builder().includes(updates).build();
            getTGMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, TGUtils.dpToPx(20f)));
        } else {
            getTGMap().animateCamera(CameraUpdateFactory.newLatLng(updates.get(0)));
        }

        if (driverLocationMarker == null) {
            //create marker
            Icon icon = TGIconFactory.getInstance().getDefaultIcon(getBaseContext(), Color.BLUE);
            MarkerOptions options = new MarkerOptions().position(updates.get(0)).icon(icon).setTitle("Driver Location");
            driverLocationMarker = getTGMap().addMarker(options);
        }

        new TGMarkerAnimator(driverLocationMarker).animateMarker(timeInterval * 1000, updates.toArray(new LatLng[0]));
    }

}
