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

import com.example.assignment3.Entity.Rental;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateRentalActivity extends AppCompatActivity {

    private static final String TAG = "CreateRentalActivity";

    private EditText homestayName, homestayAddress, homestayPrice, homestayDescription;
    private RadioGroup propertyTypeGroup;
    private CheckBox facilityWifi, facilityAirportShuttle, facilityLaundry, facilityKitchen;
    private ImageView selectedImage;
    private Uri imageUri;
    private double latitude, longitude;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rental);

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
        Button publishButton = findViewById(R.id.publish_button);
        ImageButton mapIcon = findViewById(R.id.map_icon);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("rental_images");
        auth = FirebaseAuth.getInstance();

        selectImageButton.setOnClickListener(v -> selectImage());
        mapIcon.setOnClickListener(v -> openMapsActivity());
        publishButton.setOnClickListener(v -> publishRental());

        Log.d(TAG, "onCreate: Activity created");

        // Retrieve the user ID from the Intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        Log.d(TAG, "onCreate: Retrieved user ID: " + userId);
    }

    private void selectImage() {
        Log.d(TAG, "selectImage: Selecting image");
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
            Log.d(TAG, "onActivityResult: Image selected: " + imageUri);
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
                        Log.d(TAG, "onActivityResult: Address set: " + addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onActivityResult: Geocoder exception", e);
                }
            }
        }
    }

    private void openMapsActivity() {
        Log.d(TAG, "openMapsActivity: Opening maps activity");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 2);
    }

    private void publishRental() {
        Log.d(TAG, "publishRental: Publishing rental");
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

        // Use the retrieved user ID as hostId
        Intent intent = getIntent();
        int hostId = intent.getIntExtra("userId", -1);
        Log.d(TAG, "publishRental: Host ID: " + hostId);

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Rental rental = new Rental(null, hostId, name, address, price, description, propertyType, facilities, imageUrl, latitude, longitude);
                        db.collection("rentals").add(rental).addOnSuccessListener(documentReference -> {
                            rental.setId(documentReference.getId());
                            db.collection("rentals").document(documentReference.getId()).set(rental).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "publishRental: Rental published successfully");
                                    Toast.makeText(CreateRentalActivity.this, "Rental published successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Log.e(TAG, "publishRental: Failed to publish rental");
                                    Toast.makeText(CreateRentalActivity.this, "Failed to publish rental", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "publishRental: Error adding rental to Firestore", e);
                            Toast.makeText(CreateRentalActivity.this, "Error adding rental to Firestore", Toast.LENGTH_SHORT).show();
                        });
                    })).addOnFailureListener(e -> {
                        Log.e(TAG, "publishRental: Error uploading image to Firebase Storage", e);
                        Toast.makeText(CreateRentalActivity.this, "Error uploading image to Firebase Storage", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.w(TAG, "publishRental: No image selected");
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}