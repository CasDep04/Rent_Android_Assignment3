package com.example.assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.adapter.RentalRecordAdapter;
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

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();

        Button buttonView1 = findViewById(R.id.button1);
        Button buttonView2 = findViewById(R.id.button2);
        Button buttonView3 = findViewById(R.id.button3);
        viewAnimator = findViewById(R.id.viewAnimator);

        buttonView1.setOnClickListener(v -> showView(0));
        buttonView2.setOnClickListener(v -> showView(1));
        buttonView3.setOnClickListener(v -> showView(2));

        // Set default view
        showView(1);

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

        //View 3
        recordListView = view3.findViewById(R.id.recordListView);
        setupListView();

        // Display current user information
        displayCurrentUserInformation();
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
        // Sample data for the ListView
        rentalRecords.add(new RentalRecord(1, 1, 1, 1, "2023-01-01", "2023-01-10", 100.0, "Completed"));
        rentalRecords.add(new RentalRecord(2, 2, 2, 2, "2023-02-01", "2023-02-10", 200.0, "Pending"));

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
    }
}
