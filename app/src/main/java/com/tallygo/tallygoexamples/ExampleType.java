package com.tallygo.tallygoexamples;

import android.support.v7.app.AppCompatActivity;

import com.tallygo.tallygoexamples.map.AnnotateMapMarkerActivity;
import com.tallygo.tallygoexamples.display_driver_report.DisplayDriverReportActivity;
import com.tallygo.tallygoexamples.driver_motion.DisplayDriverMotionActivity;
import com.tallygo.tallygoexamples.driver_motion.ReportDriverMotionActivity;
import com.tallygo.tallygoexamples.find_api.LatLonFromStreetAddressActivity;
import com.tallygo.tallygoexamples.find_api.SearchForLocationActivity;
import com.tallygo.tallygoexamples.navigation.LiveNavigationFromCurrentLocationActivity;
import com.tallygo.tallygoexamples.navigation.LiveNavigationFromCurrentLocationWithPreviewActivity;
import com.tallygo.tallygoexamples.navigation.SimulatedNavigationActivity;
import com.tallygo.tallygoexamples.navigation.SimulatedNavigationFromCurrentLocationActivity;
import com.tallygo.tallygoexamples.navigation.SimulatedNavigationFromCurrentLocationWithPreviewActivity;
import com.tallygo.tallygoexamples.navigation.SimulatedNavigationWithNewWaypointWhileNavigatingActivity;
import com.tallygo.tallygoexamples.navigation.SimulatedNavigationWithPreviewActivity;
import com.tallygo.tallygoexamples.obtain_route.GetRouteRawDataActivity;
import com.tallygo.tallygoexamples.map.MapOverviewUiActivity;
import com.tallygo.tallygoexamples.report_server.ReportCurrentLocServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportEtaServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportRouteServerActivity;
import com.tallygo.tallygoexamples.map.MapSearchUiActivity;
import com.tallygo.tallygoexamples.turn_list_ui.TurnListUiActivity;
import com.tallygo.tallygoexamples.waypoint_list_ui.WaypointListUiActivity;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/2/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public enum ExampleType {
    SIMULATED_NAVIGATION_WITHOUT_PREVIEW(SimulatedNavigationActivity.class, "Simulated Navigation", "without preview"),
    SIMULATED_NAVIGATION_WITH_PREVIEW(SimulatedNavigationWithPreviewActivity.class, "Simulated Navigation", "with preview"),
    SIMULATED_NAVIGATION_FROM_CURRENT_LOCATION_WITHOUT_PREVIEW(SimulatedNavigationFromCurrentLocationActivity.class, "Simulated Navigation from current location", "without preview"),
    SIMULATED_NAVIGATION_FROM_CURRENT_LOCATION_WITH_PREVIEW(SimulatedNavigationFromCurrentLocationWithPreviewActivity.class, "Simulated Navigation from current location", "with preview"),
    SIMULATED_NAVIGATION_WITH_NEW_WAYPOINT_WHILE_NAVIGATING(SimulatedNavigationWithNewWaypointWhileNavigatingActivity.class, "Simulated Navigation", "with a new waypoint added while navigating"),
    LIVE_NAVIGATION_FROM_CURRENT_LOCATION_WITHOUT_PREVIEW(LiveNavigationFromCurrentLocationActivity.class, "Live Navigation from current location", "multiple waypoints - without Preview"),
    LIVE_NAVIGATION_FROM_CURRENT_LOCATION_WITH_PREVIEW(LiveNavigationFromCurrentLocationWithPreviewActivity.class, "Live Navigation from current location", "multiple waypoints - with Preview"),

    ANNOTATE_MAP_MARKER(AnnotateMapMarkerActivity.class, "Display markers on TGMapView"),
    MAP_SEARCH_UI(MapSearchUiActivity.class, "Map Search UI"),
    MAP_OVERVIEW_UI(MapOverviewUiActivity.class, "Map Overview UI"),
    TURN_LIST_UI(TurnListUiActivity.class, "Turn List UI"),
    WAYPOINT_LIST_UI(WaypointListUiActivity.class, "Waypoint List UI"),

    LAT_LON_FROM_STREET_ADDRESS(LatLonFromStreetAddressActivity.class, "Get lat/lon from street address"),
    SEARCH_FOR_LOCATION(SearchForLocationActivity.class, "Get detailed location information"),
    GET_ROUTE_RAW_DATA(GetRouteRawDataActivity.class, "Turn-by-Turn Navigation", "accessing raw route data"),

    REPORT_CURRENT_LOC_TO_SERVER(ReportCurrentLocServerActivity.class, "Report driver's location", "to any server"),
    REPORT_ETA_SERVER(ReportEtaServerActivity.class, "Report driver's ETA", "to any server"),
    REPORT_ROUTE_SERVER(ReportRouteServerActivity.class, "Report driver's route segment", "to any server"),

    DISPLAY_DRIVER_REPORT(DisplayDriverReportActivity.class, "Display driver report"),

    REPORT_DRIVER_MOTION(ReportDriverMotionActivity.class, "Report driver motion", "to any server"),
    DISPLAY_DRIVER_MOTION(DisplayDriverMotionActivity.class, "Display driver motion"),
    ;

    private final Class<? extends AppCompatActivity> activityClass;
    private final String title;
    private final String description;

    ExampleType(Class<? extends AppCompatActivity> activityClass, String title) {
        this(activityClass, title, null);
    }

    ExampleType(Class<? extends AppCompatActivity> activityClass, String title, String description) {
        this.activityClass = activityClass;
        this.title = title;
        this.description = description;
    }

    public Class<? extends AppCompatActivity> getActivityClass() {
        return activityClass;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
