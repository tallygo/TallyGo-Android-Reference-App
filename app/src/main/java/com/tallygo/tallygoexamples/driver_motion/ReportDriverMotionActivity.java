package com.tallygo.tallygoexamples.driver_motion;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tallygo.tallygoandroid.sdk.navigation.TGNavigationRepository;
import com.tallygo.tallygoandroid.utils.TGLauncher;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ReportDriverMotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TGNavigationRepository.NavigationListener listener = new TGNavigationRepository.NavigationListener() {
            @Override
            public void onRouteLocationUpdated(Location location) {
                if (location == null) {
                    return;
                }
                locationUpdated(location);
            }
        };

        TGNavigationRepository.getDefaultNavigationAdapter(getApplication(),
                adapter -> adapter.setNavigationListener(listener));

        //launch simulated navigation
        TGLauncher.launchSimulatedNavigation(this, 2);
    }


    private static final String URL = "http://localhost:3200/drivers/motion";

    private static final int COLLECTION_INTERVAL = 30 * 1000; //millis
    private Timer collectionTimer;

    private List<Location> collectedLocations = new LinkedList<>();

    private void locationUpdated(@NonNull Location location) {
        if (collectionTimer == null) {
            scheduleTimer();
        }
        collectedLocations.add(location);
    }

    private void scheduleTimer() {
        if (collectionTimer != null) {
            collectionTimer.cancel();
        }
        collectionTimer = new Timer();
        collectionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    sendUpdates();
                });
            }
        }, 0, COLLECTION_INTERVAL);
    }

    private void sendUpdates() {
        List<Location> listToSend = collectedLocations;
        collectedLocations = new LinkedList<>();

        if (listToSend == null || listToSend.isEmpty()) {
            return;
        }

        //we use Volley but you may use whatever library you prefer
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest putRequest = new StringRequest(Request.Method.POST, URL,
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
                params.put("id", UUID.randomUUID().toString());
                params.put("timeInterval", String.valueOf(COLLECTION_INTERVAL / 1000));

                JSONArray locations = new JSONArray();
                try {
                    for (Location location: listToSend) {
                        JSONArray locationJson = new JSONArray();
                        locationJson.put((float) location.getLatitude());
                        locationJson.put((float) location.getLongitude());
                        locations.put(locationJson);
                    }
                } catch (JSONException ignored) {}
                params.put("points", locations.toString());

                return params;
            }
        };
        queue.add(putRequest);
    }
}
