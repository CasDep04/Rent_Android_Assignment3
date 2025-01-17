package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.assignment3.Entity.MarkerData;
import com.example.assignment3.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View infoWindow;
    private final Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        infoWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null; // Use default window frame
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Populate the custom layout with marker data
        TextView titleTextView = infoWindow.findViewById(R.id.titleTextView);
        TextView detailsTextView = infoWindow.findViewById(R.id.detailsTextView);
        TextView priceTextView = infoWindow.findViewById(R.id.priceTextView);

        MarkerData markerData = (MarkerData) marker.getTag();
        if (markerData != null) {
            titleTextView.setText(markerData.getName());
            detailsTextView.setText(markerData.getDetails());
            priceTextView.setText("$" + markerData.getPricePerNight() + " / night");
        }

        return infoWindow;
    }
}
