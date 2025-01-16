package com.example.assignment3;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment3.Entity.Rental;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateRentalActivity extends AppCompatActivity {

    private EditText homestayName, homestayAddress, homestayPrice, homestayDescription;
    private RadioGroup propertyTypeGroup;
    private CheckBox facilityWifi, facilityAirportShuttle, facilityLaundry, facilityKitchen;
    private ImageView selectedImage;
    private Uri imageUri;
    private String rentalId;
    private double latitude, longitude;
    private String oldImageUrl;
    private int userId;

    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        homestayName = findViewById(R.id.homestay_name);
        homestayAddress = findViewById(R.id.homestay_address);
        homestayPrice = findViewById(R.id.homestay_price);
        homestayDescription = findViewById(R.id.homestay_description);
        propertyTypeGroup = findViewById(R.id.property_type_group);
        facilityWifi = findViewById(R.id.facility_wifi);
        facilityAirportShuttle = findViewById(R.id.facility_airport_shuttle);
        facilityLaundry = findViewById(R.id.facility_laundry);
        facilityKitchen = findViewById(R.id.facility_kitchen);
        selectedImage = findViewById(R.id.selected_image);
        Button selectImageButton = findViewById(R.id.select_image_button);
        Button saveButton = findViewById(R.id.save_button);
        ImageButton mapIcon = findViewById(R.id.map_icon);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("rental_images");

        rentalId = getIntent().getStringExtra("RENTAL_ID");
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (rentalId != null) {
            loadRentalDetails(rentalId);
        } else {
            Toast.makeText(this, "Rental ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        selectImageButton.setOnClickListener(v -> selectImage());
        mapIcon.setOnClickListener(v -> openMapsActivity());
        saveButton.setOnClickListener(v -> saveRental());
    }

    private void loadRentalDetails(String rentalId) {
        db.collection("rentals").document(rentalId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Rental rental = documentSnapshot.toObject(Rental.class);
                if (rental != null) {
                    homestayName.setText(rental.getName());
                    homestayAddress.setText(rental.getAddress());
                    homestayPrice.setText(String.valueOf(rental.getPricePerNight()));
                    homestayDescription.setText(rental.getDescription());
                    latitude = rental.getLatitude();
                    longitude = rental.getLongitude();
                    oldImageUrl = rental.getImageUrl();
                    // Set property type
                    for (int i = 0; i < propertyTypeGroup.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) propertyTypeGroup.getChildAt(i);
                        if (radioButton.getText().toString().equals(rental.getPropertyType())) {
                            radioButton.setChecked(true);
                            break;
                        }
                    }
                    // Set facilities
                    facilityWifi.setChecked(rental.getFacilities().contains("Free Wifi"));
                    facilityAirportShuttle.setChecked(rental.getFacilities().contains("Airport Shuttle"));
                    facilityLaundry.setChecked(rental.getFacilities().contains("Laundry Services"));
                    facilityKitchen.setChecked(rental.getFacilities().contains("Shared Kitchen"));
                    // Load image
                    Glide.with(this).load(oldImageUrl).into(selectedImage);
                    selectedImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            selectedImage.setImageURI(imageUri);
            selectedImage.setVisibility(View.VISIBLE);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            LatLng selectedLocation = data.getParcelableExtra("selected_location");
            if (selectedLocation != null) {
                latitude = selectedLocation.latitude;
                longitude = selectedLocation.longitude;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        homestayAddress.setText(addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 2);
    }

    private void saveRental() {
        String name = homestayName.getText().toString().trim();
        String address = homestayAddress.getText().toString().trim();
        double price = Double.parseDouble(homestayPrice.getText().toString().trim());
        String description = homestayDescription.getText().toString().trim();
        String propertyType = ((RadioButton) findViewById(propertyTypeGroup.getCheckedRadioButtonId())).getText().toString();
        List<String> facilities = new ArrayList<>();
        if (facilityWifi.isChecked()) facilities.add("Free Wifi");
        if (facilityAirportShuttle.isChecked()) facilities.add("Airport Shuttle");
        if (facilityLaundry.isChecked()) facilities.add("Laundry Services");
        if (facilityKitchen.isChecked()) facilities.add("Shared Kitchen");

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateRental(name, address, price, description, propertyType, facilities, imageUrl);
                        deleteOldImage();
                    })).addOnFailureListener(e -> {
                        Toast.makeText(UpdateRentalActivity.this, "Error uploading image to Firebase Storage", Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateRental(name, address, price, description, propertyType, facilities, null);
        }
    }

    private void updateRental(String name, String address, double price, String description, String propertyType, List<String> facilities, @Nullable String imageUrl) {
        DocumentReference rentalRef = db.collection("rentals").document(rentalId);
        rentalRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String currentImageUrl = task.getResult().getString("imageUrl");
                rentalRef.update("name", name,
                                "address", address,
                                "pricePerNight", price,
                                "description", description,
                                "propertyType", propertyType,
                                "facilities", facilities,
                                "latitude", latitude,
                                "longitude", longitude,
                                "imageUrl", imageUrl != null ? imageUrl : currentImageUrl)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(UpdateRentalActivity.this, "Rental updated successfully", Toast.LENGTH_SHORT).show();
                                Intent resultIntent = new Intent();
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                Toast.makeText(UpdateRentalActivity.this, "Failed to update rental", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(UpdateRentalActivity.this, "Failed to get current rental data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteOldImage() {
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
            oldImageRef.delete().addOnSuccessListener(aVoid -> {
                // Old image deleted successfully
            }).addOnFailureListener(e -> {
                Toast.makeText(UpdateRentalActivity.this, "Failed to delete old image", Toast.LENGTH_SHORT).show();
            });
        }
    }
}