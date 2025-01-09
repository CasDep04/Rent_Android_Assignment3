package com.example.assignment3.EntityDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.User;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class LocationDetailActivity extends AppCompatActivity {

    private TextView locationIdTextView;
    private TextView locationAddressTextView;
    private TextView locationCityTextView;
    private TextView locationCountryTextView;
    private TextView locationDescriptionTextView;
    private TextView locationPricesTextView;
    private TextView hostNameTextView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        // Initialize TextViews
        locationIdTextView = findViewById(R.id.textViewLocationIdValue);
        locationAddressTextView = findViewById(R.id.textViewLocationAddressValue);
        locationCityTextView = findViewById(R.id.textViewLocationCityValue);
        locationCountryTextView = findViewById(R.id.textViewLocationCountryValue);
        locationDescriptionTextView = findViewById(R.id.textViewLocationDescriptionValue);
        locationPricesTextView = findViewById(R.id.textViewLocationPricesValue);
        hostNameTextView = findViewById(R.id.textViewHostNameValue);
        goBackButton = findViewById(R.id.goBackButton);

        // Get the Location ID from the intent
        int locationId = getIntent().getIntExtra("locationID", 0);

        // Fetch the Location details
        FirebaseAction.findLocationById(locationId).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    displayLocationDetails(location);
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set go back button click listener
        goBackButton.setOnClickListener(v -> finish());
    }

    private void displayLocationDetails(Location location) {
        locationIdTextView.setText(String.valueOf(location.getId()));
        locationAddressTextView.setText(location.getAddress());
        locationCityTextView.setText(location.getCity());
        locationCountryTextView.setText(location.getCountry());
        locationDescriptionTextView.setText(location.getDescription());
        locationPricesTextView.setText(String.valueOf(location.getPrices()));

        // Fetch the host name
        FirebaseAction.findUserById(location.getHostId()).addOnCompleteListener(new OnCompleteListener<User>() {
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    User host = task.getResult();
                    hostNameTextView.setText(host.getName());
                } else {
                    hostNameTextView.setText("Unknown Host");
                }
            }
        });
    }
}