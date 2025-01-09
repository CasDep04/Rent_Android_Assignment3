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
import com.example.assignment3.Entity.User;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;

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

        TextView locationIdTextView = convertView.findViewById(R.id.textViewLocationIdValue);
        TextView locationAddressTextView = convertView.findViewById(R.id.textViewLocationAddressValue);
        TextView locationPriceTextView = convertView.findViewById(R.id.textViewLocationPriceValue);
        TextView hostNameTextView = convertView.findViewById(R.id.textViewHostNameValue);

        locationIdTextView.setText(String.valueOf(location.getId()));
        locationAddressTextView.setText(location.getAddress());
        locationPriceTextView.setText(String.valueOf(location.getPrices()));

        FirebaseAction.findUserById(location.getHostId()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User host = task.getResult();
                hostNameTextView.setText(host.getName());
            } else {
                hostNameTextView.setText("Unknown Host");
            }
        });

        return convertView;
    }
}