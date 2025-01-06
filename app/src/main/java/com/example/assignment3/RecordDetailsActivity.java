package com.example.assignment3;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.component.FirebaseAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class RecordDetailsActivity extends AppCompatActivity {

    private TextView guestIdTextView;
    private TextView hostIdTextView;
    private TextView locationIdTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView totalPriceTextView;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);

        // Initialize TextViews
        guestIdTextView = findViewById(R.id.textViewGuestIdValue);
        hostIdTextView = findViewById(R.id.textViewHostIdValue);
        locationIdTextView = findViewById(R.id.textViewLocationIdValue);
        startDateTextView = findViewById(R.id.textViewStartDateValue);
        endDateTextView = findViewById(R.id.textViewEndDateValue);
        totalPriceTextView = findViewById(R.id.textViewTotalPriceValue);
        statusTextView = findViewById(R.id.textViewStatusValue);

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
    }

    private void displayRecordDetails(RentalRecord record) {
        guestIdTextView.setText(String.valueOf(record.getGuestId()));
        hostIdTextView.setText(String.valueOf(record.getHostId()));
        locationIdTextView.setText(String.valueOf(record.getLocationId()));
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
    }
}