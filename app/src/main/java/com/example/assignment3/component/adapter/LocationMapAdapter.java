package com.example.assignment3.component.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.Entity.MarkerData;
import com.example.assignment3.R;

import java.util.List;

public class LocationMapAdapter extends RecyclerView.Adapter<LocationMapAdapter.ViewHolder> {
    private final List<MarkerData> locations;
    private final OnLocationClickListener listener;

    // Constructor
    public LocationMapAdapter(List<MarkerData> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.location_name);
            addressTextView = itemView.findViewById(R.id.location_address);
        }

        public void bind(MarkerData location, OnLocationClickListener listener) {
            nameTextView.setText(location.getName());
            addressTextView.setText(location.getDetails());
            itemView.setOnClickListener(v -> listener.onLocationClick(location));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(locations.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    // Listener interface for item clicks
    public interface OnLocationClickListener {
        void onLocationClick(MarkerData location);
    }
}

