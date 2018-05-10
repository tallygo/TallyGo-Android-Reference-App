package com.tallygo.tallygoexamples;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//
//  TallyGoKit
//
//  Created by haydenchristensen on 5/2/18
//  Copyright © 2017 TallyGo. All rights reserved.
//
public class MainAdapter extends ArrayAdapter<ExampleType> {

    public MainAdapter(@NonNull Context context) {
        super(context, R.layout.row_example_type, ExampleType.values());
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_example_type, null);
        }

        ExampleType exampleType = getItem(position);
        final TextView exampleTypeTextView = v.findViewById(R.id.tv_example_type);
        exampleTypeTextView.setText(exampleType.getLabel());

        return v;
    }

}
