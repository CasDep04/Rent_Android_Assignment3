package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.assignment3.Entity.Location;
import com.example.assignment3.R;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    private Context context;
    private List<Location> locations;

    public LocationAdapter(@NonNull Context context, @NonNull List<Location> locations) {
        super(context, 0, locations);
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.location_list_component, parent, false);
        }

        Location location = locations.get(position);

        TextView locationAddressTextView = convertView.findViewById(R.id.textViewLocationAddressValue);
        TextView locationCityTextView = convertView.findViewById(R.id.textViewLocationCityValue);
        TextView locationCountryTextView = convertView.findViewById(R.id.textViewLocationCountryValue);
        TextView locationDescriptionTextView = convertView.findViewById(R.id.textViewLocationDescriptionValue);
        TextView locationPricesTextView = convertView.findViewById(R.id.textViewLocationPricesValue);

        locationAddressTextView.setText(location.getAddress());
        locationCityTextView.setText(location.getCity());
        locationCountryTextView.setText(location.getCountry());
        locationDescriptionTextView.setText(location.getDescription());
        locationPricesTextView.setText(String.valueOf(location.getPrices()));

        return convertView;
    }
}