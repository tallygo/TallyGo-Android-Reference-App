package com.tallygo.tallygoexamples;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExampleTypeAdapter extends RecyclerView.Adapter<ExampleTypeAdapter.ViewHolder> {
    private final ExampleType[] exampleTypes = ExampleType.values();
    private final Callback callback;
    private final LayoutInflater layoutInflater;

    @ColorInt
    private final int backgroundColorOdd;
    @ColorInt
    private final int backgroundColorEven;

    public ExampleTypeAdapter(Context context, Callback callback) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;

        backgroundColorEven = context.getResources().getColor(R.color.backgroundColorEven);
        backgroundColorOdd = context.getResources().getColor(R.color.backgroundColorOdd);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View row = layoutInflater.inflate(R.layout.row_example_type, parent, false);

        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(position, exampleTypes[position]);
    }

    @Override
    public int getItemCount() {
        return exampleTypes.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtDescription;
        private ExampleType exampleType;

        private ViewHolder(View row) {
            super(row);

            txtTitle = row.findViewById(R.id.title);
            txtDescription = row.findViewById(R.id.description);
            row.setOnClickListener(view -> {
                callback.onClicked(exampleType);
            });
        }

        private void bind(int position, ExampleType exampleType) {
            this.exampleType = exampleType;

            txtTitle.setText(exampleType.getTitle());
            if( exampleType.getDescription()!=null && !exampleType.getDescription().trim().equals("") ) {
                txtDescription.setVisibility(View.VISIBLE);
                txtDescription.setText("(" + exampleType.getDescription() + ")");
            } else {
                txtDescription.setVisibility(View.GONE);
                txtDescription.setText("");
            }

            if( position % 2 == 0 ) {
                itemView.setBackgroundColor(backgroundColorEven);
            } else {
                itemView.setBackgroundColor(backgroundColorOdd);
            }
        }
    }

    public interface Callback {
        void onClicked(ExampleType exampleType);
    }
}
