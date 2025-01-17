package com.example.assignment3.EntityDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.Entity.Location;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class RecordDetailsActivity extends AppCompatActivity {

    private TextView guestNameTextView;
    private TextView hostNameTextView;
    private TextView locationNameTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView totalPriceTextView;
    private TextView statusTextView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);

        // Initialize TextViews
        guestNameTextView = findViewById(R.id.textViewGuestNameValue);
        hostNameTextView = findViewById(R.id.textViewHostNameValue);
        locationNameTextView = findViewById(R.id.textViewLocationNameValue);
        startDateTextView = findViewById(R.id.textViewStartDateValue);
        endDateTextView = findViewById(R.id.textViewEndDateValue);
        totalPriceTextView = findViewById(R.id.textViewTotalPriceValue);
        statusTextView = findViewById(R.id.textViewStatusValue);
        goBackButton = findViewById(R.id.goBackButton);

        // Get the RentalRecord ID from the intent
        int recordId = getIntent().getIntExtra("recordID", 0);

        // Fetch the RentalRecord details
        FirebaseAction.findRecordById(recordId).addOnCompleteListener(new OnCompleteListener<RentalRecord>() {
            @Override
            public void onComplete(@NonNull Task<RentalRecord> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    RentalRecord record = task.getResult();
                    displayRecordDetails(record);
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set logout button click listener
        goBackButton.setOnClickListener(v -> finish());
    }

    private void displayRecordDetails(RentalRecord record) {
        startDateTextView.setText(record.getStartDate());
        endDateTextView.setText(record.getEndDate());
        totalPriceTextView.setText(String.valueOf(record.getTotalPrice()));
        statusTextView.setText(record.getStatus());

        // Set the status text color based on the status
        switch (record.getStatus().toLowerCase()) {
            case "completed":
                statusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "pending":
                statusTextView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "rejected":
                statusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                statusTextView.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }

//        // Fetch and display guest name
//        FirebaseAction.findUserById(record.getGuestId()).addOnCompleteListener(new OnCompleteListener<User>() {
//            @Override
//            public void onComplete(@NonNull Task<User> task) {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    User guest = task.getResult();
//                    guestNameTextView.setText(guest.getName());
//                } else {
//                    guestNameTextView.setText("Unknown Guest");
//                }
//            }
//        });
//
//        // Fetch and display host name
//        FirebaseAction.findUserById(record.getHostId()).addOnCompleteListener(new OnCompleteListener<User>() {
//            @Override
//            public void onComplete(@NonNull Task<User> task) {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    User host = task.getResult();
//                    hostNameTextView.setText(host.getName());
//                } else {
//                    hostNameTextView.setText("Unknown Host");
//                }
//            }
//        });
//
//        // Fetch and display location name
//        FirebaseAction.findLocationById(record.getLocationId()).addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    Location location = task.getResult();
//                    locationNameTextView.setText(location.getAddress());
//                } else {
//                    locationNameTextView.setText("Unknown Location");
//                }
//            }
//        });
    }
}