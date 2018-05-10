package com.tallygo.tallygoexamples;

import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoexamples.annotate_map.AnnotateMapMarkerActivity;
import com.tallygo.tallygoexamples.display_driver_report.DisplayDriverReportActivity;
import com.tallygo.tallygoexamples.driving_simulator.DrivingSimulatorActivity;
import com.tallygo.tallygoexamples.find_api.LatLonFromStreetAddressActivity;
import com.tallygo.tallygoexamples.find_api.SearchForLocationActivity;
import com.tallygo.tallygoexamples.obtain_route.GetRouteActivity;
import com.tallygo.tallygoexamples.overview_ui.OverviewUiActivity;
import com.tallygo.tallygoexamples.report_server.ReportCurrentLocServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportEtaServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportRouteServerActivity;
import com.tallygo.tallygoexamples.search_ui.SearchUiActivity;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/2/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public enum ExampleType {
    ANNOTATE_MAP_MARKER(AnnotateMapMarkerActivity.class, "Annotate the map with a marker"),
    DISPLAY_DRIVER_REPORT(DisplayDriverReportActivity.class, "Display driver report"),
    DRIVING_SIMULATOR(DrivingSimulatorActivity.class, "Use the driving simulator"),
    LAT_LON_FROM_STREET_ADDRESS(LatLonFromStreetAddressActivity.class, "Get lat/lon from street address"),
    SEARCH_FOR_LOCATION(SearchForLocationActivity.class, "Search for location"),
    GET_ROUTE(GetRouteActivity.class, "Get a route"),
    OVERVIEW_UI(OverviewUiActivity.class, "Show a route overview"),
    REPORT_CURRENT_LOC_TO_SERVER(ReportCurrentLocServerActivity.class, "Report location to server"),
    REPORT_ETA_SERVER(ReportEtaServerActivity.class, "Report ETA to server"),
    REPORT_ROUTE_SERVER(ReportRouteServerActivity.class, "Report route to server"),
    SEARCH_UI(SearchUiActivity.class, "Display search UI on a map"),
    TURN_LIST_UI(SearchUiActivity.class, "Display turn list UI"),

    ;

    private final Class<? extends AppCompatActivity> activityClass;
    private final String label;

    ExampleType(Class<? extends AppCompatActivity> activityClass, String label) {
        this.activityClass = activityClass;
        this.label = label;
    }

    public Class<? extends AppCompatActivity> getActivityClass() {
        return activityClass;
    }

    public String getLabel() {
        return label;
    }
}
