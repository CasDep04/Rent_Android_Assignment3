package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.assignment3.R;
import com.example.assignment3.Entity.Rental;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class RentalDetailPageActivity extends AppCompatActivity {

    private ImageView rentalImage;
    private TextView rentalName, rentalPrice, rentalDescription, rentalAddress, rentalType;
    private Button updateButton;
    private LinearLayout facilitiesContainer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_detail_page);

        rentalImage = findViewById(R.id.homestay_image);
        rentalName = findViewById(R.id.homestay_name);
        rentalPrice = findViewById(R.id.homestay_price);
        rentalDescription = findViewById(R.id.homestay_description);
        rentalAddress = findViewById(R.id.homestay_address);
        rentalType = findViewById(R.id.property_type);
        facilitiesContainer = findViewById(R.id.facilities_container);
        updateButton = findViewById(R.id.update_button);

        db = FirebaseFirestore.getInstance();

        String rentalId = getIntent().getStringExtra("RENTAL_ID");
        if (rentalId != null) {
            fetchRentalDetails(rentalId);
        } else {
            Toast.makeText(this, "Rental ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentalDetailPageActivity.this, UpdateRentalActivity.class);
                intent.putExtra("RENTAL_ID", rentalId);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void fetchRentalDetails(String rentalId) {
        db.collection("rentals").document(rentalId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Rental rental = document.toObject(Rental.class);
                    if (rental != null) {
                        displayRentalDetails(rental);
                    }
                } else {
                    Toast.makeText(this, "Rental not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Error fetching rental details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRentalDetails(Rental rental) {
        Glide.with(this).load(rental.getImageUrl()).into(rentalImage);
        rentalName.setText(rental.getName());
        rentalPrice.setText(String.format("$%s per night", rental.getPricePerNight()));
        rentalDescription.setText(rental.getDescription());
        rentalAddress.setText(rental.getAddress());
        rentalType.setText(rental.getPropertyType());

        facilitiesContainer.removeAllViews();
        if (rental.getFacilities() != null) {
            for (String facility : rental.getFacilities()) {
                LinearLayout facilityLayout = new LinearLayout(this);
                facilityLayout.setOrientation(LinearLayout.HORIZONTAL);
                facilityLayout.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 23, 0, 0);
                facilityLayout.setLayoutParams(layoutParams);

                ImageView facilityIcon = new ImageView(this);
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                        72, 72
                );
                iconParams.setMarginEnd(13); // margin between icon and text
                facilityIcon.setLayoutParams(iconParams);
                facilityIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                facilityIcon.setImageResource(getFacilityIcon(facility));

                TextView facilityText = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                facilityText.setLayoutParams(textParams);
                facilityText.setTextSize(20); // text size
                facilityText.setText(facility);

                facilityLayout.addView(facilityIcon);
                facilityLayout.addView(facilityText);

                facilitiesContainer.addView(facilityLayout);
            }
        }
    }

    private int getFacilityIcon(String facility) {
        switch (facility) {
            case "Free Wifi":
                return R.drawable.baseline_wifi_24;
            case "Airport Shuttle":
                return R.drawable.baseline_airport_shuttle_24;
            case "Laundry Services":
                return R.drawable.baseline_local_laundry_service_24;
            case "Shared Kitchen":
                return R.drawable.baseline_kitchen_24;
            default:
                return R.drawable.baseline_umbrella_24;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Handle the result, e.g., refresh the details
            String rentalId = getIntent().getStringExtra("RENTAL_ID");
            if (rentalId != null) {
                fetchRentalDetails(rentalId);
            }
        }
    }
}