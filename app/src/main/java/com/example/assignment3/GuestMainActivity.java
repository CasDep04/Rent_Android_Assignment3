package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Host;
import com.example.assignment3.EntityDetails.RecordDetailsActivity;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.adapter.RentalRecordAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

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

        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseManager(this);
        db.open();

        Button buttonView1 = findViewById(R.id.button1);
        Button buttonView2 = findViewById(R.id.button2);
        Button buttonView3 = findViewById(R.id.button3);
        viewAnimator = findViewById(R.id.viewAnimator);

        buttonView1.setOnClickListener(v -> showView(0));
        buttonView2.setOnClickListener(v -> showView(1));
        buttonView3.setOnClickListener(v -> showView(2));

        // Set default view
        showView(0);

        // Setup profile view
        View profileView = viewAnimator.getChildAt(0);
        setupProfileView(profileView);

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

        // Set up the Delete Account button
        Button deleteAccountButton = findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountDialog());


        // view 2 for map layout
        // Find the SupportMapFragment by its ID
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                // Customize the map
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // Add a marker (optional)
                LatLng sampleLocation = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sampleLocation).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10));
            });
        } else {
            Log.e("GuestMainActivity", "Map fragment not found!");
        }




        //View 3
        recordListView = view3.findViewById(R.id.recordListView);

        // Display current user information
        displayCurrentUserInformation();
    }

    private void setupProfileView(View profileView) {
        TextView nameTextView = profileView.findViewById(R.id.nameTextView);
        EditText nameEditText = profileView.findViewById(R.id.nameEditText);
        TextView birthdayTextView = profileView.findViewById(R.id.birthdayTextView);
        LinearLayout birthdayEditLayout = profileView.findViewById(R.id.birthdayEditLayout);
        Spinner daySpinner = profileView.findViewById(R.id.daySpinner);
        Spinner monthSpinner = profileView.findViewById(R.id.monthSpinner);
        Spinner yearSpinner = profileView.findViewById(R.id.yearSpinner);
        TextView roleLabel = profileView.findViewById(R.id.roleLabel);
        TextView roleTextView = profileView.findViewById(R.id.roleTextView);
        TextView balanceLabel = profileView.findViewById(R.id.balanceLabel);
        TextView balanceTextView = profileView.findViewById(R.id.balanceTextView);

        Button editProfileButton = profileView.findViewById(R.id.edit_profile_button);
        Button finishEditButton = profileView.findViewById(R.id.finish_edit_button);
        Button cancelButton = profileView.findViewById(R.id.cancel_button);
        Button addBalanceButton = profileView.findViewById(R.id.add_balance_button);
        Button logoutButton = profileView.findViewById(R.id.logout_button);
        Button deleteAccountButton = profileView.findViewById(R.id.delete_account_button);
        Button changeToHostButton = profileView.findViewById(R.id.change_to_host_button);

        // Assuming currentGuest is already initialized with the current user's data
        if (currentGuest != null) {
            nameTextView.setText(currentGuest.getName());
            nameEditText.setText(currentGuest.getName());

            birthdayTextView.setText(currentGuest.getDateOfBirth());
            // Set the spinners to the current date values
            String[] dateParts = currentGuest.getDateOfBirth().split("-");
            if (dateParts.length == 3) {
                setSpinnerValue(daySpinner, dateParts[0]);
                setSpinnerValue(monthSpinner, dateParts[1]);
                setSpinnerValue(yearSpinner, dateParts[2]);
            }

            roleTextView.setText(currentGuest.getRole());
            balanceTextView.setText(String.valueOf(currentGuest.getBalance()));
        }

        editProfileButton.setOnClickListener(v -> {
            nameEditText.setText(currentGuest.getName());

            String[] dateParts = currentGuest.getDateOfBirth().split("-");
            if (dateParts.length == 3) {
                setSpinnerValue(daySpinner, dateParts[0]);
                setSpinnerValue(monthSpinner, dateParts[1]);
                setSpinnerValue(yearSpinner, dateParts[2]);
            }

            nameTextView.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);

            birthdayTextView.setVisibility(View.GONE);
            birthdayEditLayout.setVisibility(View.VISIBLE);
            changeToHostButton.setVisibility(View.GONE);
            roleLabel.setVisibility(View.GONE);
            roleTextView.setVisibility(View.GONE);

            balanceLabel.setVisibility(View.GONE);
            balanceTextView.setVisibility(View.GONE);

            addBalanceButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            deleteAccountButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.GONE);
            finishEditButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        });

        finishEditButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString();
            String newDateOfBirth = daySpinner.getSelectedItem().toString() + "-" +
                                    monthSpinner.getSelectedItem().toString() + "-" +
                                    yearSpinner.getSelectedItem().toString();

            currentGuest.setName(newName);
            currentGuest.setDateOfBirth(newDateOfBirth);

            FirebaseAction.editUserInFirestore(currentGuest)
                .addOnSuccessListener(aVoid -> {
                    nameTextView.setText(newName);
                    birthdayTextView.setText(newDateOfBirth);

                    nameTextView.setVisibility(View.VISIBLE);
                    nameEditText.setVisibility(View.GONE);

                    birthdayTextView.setVisibility(View.VISIBLE);
                    birthdayEditLayout.setVisibility(View.GONE);

                    roleLabel.setVisibility(View.VISIBLE);
                    roleTextView.setVisibility(View.VISIBLE);

                    balanceLabel.setVisibility(View.VISIBLE);
                    balanceTextView.setVisibility(View.VISIBLE);
                    changeToHostButton.setVisibility(View.VISIBLE);

                    addBalanceButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    deleteAccountButton.setVisibility(View.VISIBLE);
                    editProfileButton.setVisibility(View.VISIBLE);
                    finishEditButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);

                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        });

        cancelButton.setOnClickListener(v -> {
            nameTextView.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.GONE);

            birthdayTextView.setVisibility(View.VISIBLE);
            birthdayEditLayout.setVisibility(View.GONE);

            roleLabel.setVisibility(View.VISIBLE);
            roleTextView.setVisibility(View.VISIBLE);

            balanceLabel.setVisibility(View.VISIBLE);
            balanceTextView.setVisibility(View.VISIBLE);
            changeToHostButton.setVisibility(View.VISIBLE);

            addBalanceButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            deleteAccountButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.VISIBLE);
            finishEditButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        });

        changeToHostButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Change to Host")
                .setMessage("Are you sure you want to change to host?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    FirebaseAction.specialDeleteUserFromFirestore(currentGuest.getId());
                    Host tempHost = new Host(
                            currentGuest.getId(),
                            currentGuest.getEmail(),
                            currentGuest.getBalance(),
                            currentGuest.getName(),
                            currentGuest.getDateOfBirth(), 0);
                    FirebaseAction.addUserToFirestore(tempHost)
                        .addOnSuccessListener(aVoid -> {
                            db.deleteUser();
                            db.updateUser(currentGuest.getEmail());
                            Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to change to host: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                })
                .setNegativeButton("No", null)
                .show();
        });
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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
