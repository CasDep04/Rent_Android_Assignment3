package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;

public class GuestMainActivity extends AppCompatActivity {

    private static final String TAG = "GuestMainActivity";

    private FirebaseAuth mAuth;
    private DatabaseManager db;
    private TextView nameTextView, birthdayTextView, roleTextView, balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.nameTextView);
        birthdayTextView = findViewById(R.id.birthdayTextView);
        roleTextView = findViewById(R.id.roleTextView);
        balanceTextView = findViewById(R.id.balanceTextView);

        Intent intent = getIntent();
        double userIdDouble = intent.getDoubleExtra("id", 0);
        if (userIdDouble != 0) {
            int userId = (int) userIdDouble;
            FirebaseAction.findUserById(userId).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = task.getResult();
                    if (user instanceof Guest) {
                        Guest guest = (Guest) user;
                        displayGuestInfo(guest);
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
        } else {
            Toast.makeText(this, "User ID is missing or invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayGuestInfo(Guest guest) {
        nameTextView.setText("Name: " + guest.getName());
        birthdayTextView.setText("Birthday: " + guest.getDateOfBirth());
        roleTextView.setText("Role: " + guest.getRole());
        balanceTextView.setText("Balance: " + guest.getBalance());
    }

    public void logOut(View view) {
        mAuth.signOut();
        db.deleteUser();
        Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}