package com.tallygo.tallygoexamples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tallygo.tallygoexamples.report_server.ReportCurrentLocServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportEtaServerActivity;
import com.tallygo.tallygoexamples.report_server.ReportRouteServerActivity;
import com.tallygo.tallygoexamples.display_driver_report.DisplayDriverReportActivity;

import timber.log.Timber;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plantDebugTree();
        setListAdapter(new MainAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ExampleType exampleType = ExampleType.values()[position];
        startExample(exampleType);
    }

    private void startExample(ExampleType exampleType) {
        Intent intent;
        switch (exampleType) {
            case REPORT_CURRENT_LOC_TO_SERVER:
                intent = new Intent(this, ReportCurrentLocServerActivity.class);
                break;
            case REPORT_ETA_SERVER:
                intent = new Intent(this, ReportEtaServerActivity.class);
                break;
            case REPORT_ROUTE_SERVER:
                intent = new Intent(this, ReportRouteServerActivity.class);
                break;
            case DISPLAY_DRIVER_REPORT:
                intent = new Intent(this, DisplayDriverReportActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown example type");
        }
        startActivity(intent);
    }

    private void plantDebugTree() {
        Timber.plant(new Timber.DebugTree());
    }
}
