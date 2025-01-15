package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.assignment3.Entity.MarkerData;
import com.example.assignment3.Entity.RentalRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RentalDetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_detail);

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentUserId = currentUser.getUid().hashCode(); // Convert UID to int.



        // Get MarkerData from Intent
        MarkerData markerData = (MarkerData) getIntent().getSerializableExtra("markerData");

        if (markerData != null) {
            // Populate UI with MarkerData details
            ImageView rentalImage = findViewById(R.id.rental_image);
            TextView rentalName = findViewById(R.id.rental_name);
            TextView rentalAddress = findViewById(R.id.rental_address);
            TextView rentalDescription = findViewById(R.id.rental_description);
            TextView rentalFacilities = findViewById(R.id.rental_facilities);
            TextView rentalPropertyType = findViewById(R.id.rental_property_type);
            TextView rentalPrice = findViewById(R.id.rental_price);
            EditText startDateField = findViewById(R.id.start_date);
            EditText endDateField = findViewById(R.id.end_date);
            TextView totalPriceField = findViewById(R.id.total_price);
            Button requestPaymentButton = findViewById(R.id.request_payment_button);

            rentalName.setText(markerData.getName());
            rentalAddress.setText(markerData.getDetails());
            rentalDescription.setText(markerData.getDescription());
            rentalFacilities.setText(String.join(", ", markerData.getFacilities()));
            rentalPropertyType.setText("Property Type: " + markerData.getPropertyType());
            rentalPrice.setText("$" + markerData.getPricePerNight() + " / night");

            Glide.with(this)
                    .load(markerData.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(rentalImage);

            // Add Text Watchers for real-time calculation
            TextWatcher dateWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateTotalPrice(startDateField.getText().toString(), endDateField.getText().toString(), markerData.getPricePerNight(), totalPriceField);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            };

            startDateField.addTextChangedListener(dateWatcher);
            endDateField.addTextChangedListener(dateWatcher);

            // Handle "Request Payment" button click
            requestPaymentButton.setOnClickListener(v -> {
                String startDate = startDateField.getText().toString();
                String endDate = endDateField.getText().toString();

                // Validate dates
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    Toast.makeText(this, "Please enter valid dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calculate the total price
                long numberOfNights = calculateNights(startDate, endDate);
                double totalPrice = numberOfNights * markerData.getPricePerNight();



                // Create a rental record
                RentalRecord rentalRecord = new RentalRecord(
                        0, // auto-generated ID
                        currentUserId, // guestId
                        2, // hostId
                        1, // locationId
                        startDate,
                        endDate,
                        totalPrice,
                        "pending"
                );

                // Save to Firestore
                saveRentalRecordToFirestore(rentalRecord);
            });
        }
    }

    private long calculateNights(String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            if (start != null && end != null) {
                return TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateTotalPrice(String startDate, String endDate, double pricePerNight, TextView totalPriceField) {
        long numberOfNights = calculateNights(startDate, endDate);
        if (numberOfNights > 0) {
            double totalPrice = numberOfNights * pricePerNight;
            totalPriceField.setText("$" + totalPrice);
        } else {
            totalPriceField.setText("Calculating...");
        }
    }

    private void saveRentalRecordToFirestore(RentalRecord rentalRecord) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rentalRecords")
                .add(rentalRecord)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Request sent successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }
}



