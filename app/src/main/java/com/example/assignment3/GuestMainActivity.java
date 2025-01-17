package com.example.assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.Entity.MarkerData;
import com.example.assignment3.EntityDetails.RecordDetailsActivity;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.adapter.CustomInfoWindowAdapter;
import com.example.assignment3.component.adapter.LocationMapAdapter;
import com.example.assignment3.component.adapter.RentalRecordAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestMainActivity extends AppCompatActivity {

    private static final String TAG = "GuestMainActivity";
    private static final int ADD_BALANCE_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private DatabaseManager db;
    private ViewAnimator viewAnimator;
    private int currentView = -1; // Initialize to an invalid index
    private Guest currentGuest;
    private ListView recordListView;
    private List<RentalRecord> rentalRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();

        viewAnimator = findViewById(R.id.viewAnimator);


        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_profile) {
                    showView(0);
                } else if (itemId == R.id.nav_map) {
                    showView(1);
                } else if (itemId == R.id.nav_status) {
                    showView(2);
                }
                return true;
            }
        });

        // Set default view
        showView(0);

        // Set up the Delete Account button
        Button deleteAccountButton = findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountDialog());

        //miniview
        View view1 = viewAnimator.getChildAt(0);
        View view2 = viewAnimator.getChildAt(1);
        View view3 = viewAnimator.getChildAt(2);

        //view 1
        Button add_balance_button = view1.findViewById(R.id.add_balance_button);
        Button logout_button = view1.findViewById(R.id.logout_button);

        add_balance_button.setOnClickListener(v -> {
            Intent intent = new Intent(GuestMainActivity.this, AddBalanceActivity.class);
            intent.putExtra("id", currentGuest.getId());
            intent.putExtra("balance", currentGuest.getBalance());
            startActivityForResult(intent, ADD_BALANCE_REQUEST_CODE);
        });
        logout_button.setOnClickListener(v -> logOut());

        // view 2 for map layout


        RecyclerView locationList = findViewById(R.id.location_list);
        ImageButton toggleButton = findViewById(R.id.toggle_list_button);

        // Initialize RecyclerView (hidden by default)
        locationList.setLayoutManager(new LinearLayoutManager(this));

        // Toggle visibility of the list
        toggleButton.setOnClickListener(v -> {
            if (locationList.getVisibility() == View.GONE) {
                locationList.setVisibility(View.VISIBLE); // Show the list
            } else {
                locationList.setVisibility(View.GONE); // Hide the list
            }
        });

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        RecyclerView recyclerView = findViewById(R.id.location_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // Set the custom InfoWindowAdapter
                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

                // Move to RentalDetailActivity when click on the marker tag
                googleMap.setOnInfoWindowClickListener(marker -> {
                    MarkerData markerData = (MarkerData) marker.getTag();
                    if (markerData != null) {
                        // Navigate to RentalDetailActivity
                        Intent detailIntent = new Intent(this, RentalDetailActivity.class);
                        detailIntent.putExtra("markerData", markerData);
                        startActivity(detailIntent);
                    }
                });

                // Fetch rental houses and bind data to RecyclerView
                fetchRentalHousesFromFirestore(googleMap, recyclerView);
            });
        } else {
            Log.e("GuestMainActivity", "Map fragment not found!");
        }





        //View 3
        recordListView = view3.findViewById(R.id.recordListView);

        // Display current user information
        displayCurrentUserInformation();
    }


    private void fetchRentalHousesFromFirestore(GoogleMap googleMap, RecyclerView recyclerView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<MarkerData> locationList = new ArrayList<>();
        Map<String, Marker> markerMap = new HashMap<>();

        db.collection("rentals")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Extract data from Firestore
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String name = document.getString("name");
                        String address = document.getString("address");
                        String description = document.getString("description");
                        String propertyType = document.getString("propertyType");
                        int pricePerNight = document.getLong("pricePerNight").intValue();
                        String imageUrl = document.getString("imageUrl");
                        List<String> facilities = (List<String>) document.get("facilities");
                        int hostId = document.getLong("hostId").intValue();
                        String locationId = document.getString("id");

                        Bundle extras = getIntent().getExtras();
                        int guestId = 0;
                        if (extras != null) {
                            guestId = extras.getInt("userId");
                        }

                        // Create MarkerData object
                        MarkerData markerData = new MarkerData(
                                name,
                                address,
                                description,
                                imageUrl,
                                propertyType,
                                pricePerNight,
                                facilities,
                                hostId,
                                locationId,
                                guestId
                        );

                        // Add marker to the map
                        LatLng location = new LatLng(latitude, longitude);
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(name));
                        marker.setTag(markerData); // Attach MarkerData to the marker

                        // Add marker data to the list for the RecyclerView
                        locationList.add(markerData);

                        // Map the location name to the marker for quick access
                        markerMap.put(name, marker);
                    }

                    // Set RecyclerView Adapter
                    LocationMapAdapter adapter = new LocationMapAdapter(locationList, location -> {
                        // On click: Move the camera to the marker and show info window
                        Marker marker = markerMap.get(location.getName());
                        if (marker != null) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                            marker.showInfoWindow();
                        }
                    });
                    recyclerView.setAdapter(adapter);

                    // Optionally move the camera to the first rental house
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot firstDocument = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        double firstLatitude = firstDocument.getDouble("latitude");
                        double firstLongitude = firstDocument.getDouble("longitude");
                        LatLng firstLocation = new LatLng(firstLatitude, firstLongitude);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching rentals", e);
                    Toast.makeText(this, "Failed to load rental houses", Toast.LENGTH_SHORT).show();
                });
    }






    private void showView(int viewIndex) {
        if (viewIndex == currentView) return;

        viewAnimator.setInAnimation(this, viewIndex > viewAnimator.getDisplayedChild() ? R.anim.slide_in_right : R.anim.slide_in_left);
        viewAnimator.setOutAnimation(this, viewIndex > viewAnimator.getDisplayedChild() ? R.anim.slide_out_left : R.anim.slide_out_right);
        viewAnimator.setDisplayedChild(viewIndex);
        currentView = viewIndex;
    }

    private void displayCurrentUserInformation() {
        Intent intent = getIntent();
        double userIdDouble = intent.getDoubleExtra("id", 0);

        if (userIdDouble == 0) {
            Toast.makeText(this, "User ID is missing or invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = (int) userIdDouble;
        Log.d(TAG, "displayCurrentUserInformation: Finding user by ID: " + userId);
        FirebaseAction.findUserById(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User user = task.getResult();
                Log.d(TAG, "displayCurrentUserInformation: User found: " + user);
                if (user instanceof Guest) {
                    currentGuest = (Guest) user;
                    Log.d(TAG, "displayCurrentUserInformation: currentGuest assigned: " + currentGuest);
                    displayGuestInfo();
                    setupListView(); // Setup the ListView after currentGuest is assigned
                } else {
                    Toast.makeText(this, "User is not a guest", Toast.LENGTH_SHORT).show();
                }
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Log.e(TAG, "Failed to retrieve user", e);
                    Toast.makeText(this, "Failed to retrieve user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to retrieve user: Unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayGuestInfo() {
        Log.d(TAG, "displayGuestInfo: Displaying guest information");
        View view1 = viewAnimator.getChildAt(0);
        TextView nameTextView = view1.findViewById(R.id.nameTextView);
        TextView birthdayTextView = view1.findViewById(R.id.birthdayTextView);
        TextView roleTextView = view1.findViewById(R.id.roleTextView);
        TextView balanceTextView = view1.findViewById(R.id.balanceTextView);

        if (nameTextView == null || birthdayTextView == null || roleTextView == null || balanceTextView == null) {
            Log.e(TAG, "displayGuestInfo: One or more TextView elements are null");
            return;
        }
        if(currentGuest !=null) {
            nameTextView.setText(currentGuest.getName());
            birthdayTextView.setText(currentGuest.getDateOfBirth());
            roleTextView.setText(currentGuest.getRole());
            balanceTextView.setText(String.valueOf(currentGuest.getBalance()));
        }
        Log.d(TAG, "displayGuestInfo: Guest information displayed successfully");
    }

    private void addBalanceEvent() {
        if (currentGuest != null) {
            Intent intent = new Intent(GuestMainActivity.this, AddBalanceActivity.class);
            intent.putExtra("id", currentGuest.getId());
            intent.putExtra("balance", currentGuest.getBalance());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Guest information is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut() {
        mAuth.signOut();
        db.deleteUser();
        Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        //displayGuestInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BALANCE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                double newBalance = data.getDoubleExtra("selectedBalance", currentGuest.getBalance());
                currentGuest.setBalance(newBalance);
                FirebaseAction.editUserInFirestore(currentGuest)
                    .addOnSuccessListener(aVoid -> {
                        displayGuestInfo();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to update user in Firestore", e);
                        Toast.makeText(this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }
        }
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteAccount() {
        if (currentGuest != null) {
            FirebaseAction.deleteUserFromFirestore(currentGuest.getId())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User deleted successfully from Firestore");
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to login or main activity
                    Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete user from Firestore", e);
                    Toast.makeText(this, "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        } else {
            Log.e(TAG, "deleteAccount: currentGuest is null");
            Toast.makeText(this, "Failed to delete account: User not found", Toast.LENGTH_SHORT).show();
        }
        db.deleteUser();
        Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void setupListView() {
        if (currentGuest == null) {
            Log.e(TAG, "setupListView: currentGuest is null");
            return;
        }

        FirebaseAction.findRecordsByGuestId(currentGuest.getId()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                rentalRecords.clear();
                rentalRecords.addAll(task.getResult());

                // Create a custom adapter
                RentalRecordAdapter adapter = new RentalRecordAdapter(this, rentalRecords);

                // Set the adapter to the ListView
                recordListView.setAdapter(adapter);

                // Set an item click listener to navigate to RecordDetailsActivity
                recordListView.setOnItemClickListener((parent, view, position, id) -> {
                    RentalRecord selectedRecord = rentalRecords.get(position);
                    Intent intent = new Intent(GuestMainActivity.this, RecordDetailsActivity.class);
                    intent.putExtra("recordID", selectedRecord.getId());
                    startActivity(intent);
                });
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Log.e(TAG, "Failed to retrieve rental records", e);
                    Toast.makeText(this, "Failed to retrieve rental records: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to retrieve rental records: Unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
