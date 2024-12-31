package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.Host;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegistrationInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String role;
    private EditText nameEditText;
    private Spinner dateSpinner, monthSpinner, yearSpinner, preferenceSpinner;
    private Button submitButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);

        mAuth = FirebaseAuth.getInstance();

        // Retrieve the email and role from the Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        role = intent.getStringExtra("role").toLowerCase();

        nameEditText = findViewById(R.id.nameEditText);
        dateSpinner = findViewById(R.id.dateSpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        preferenceSpinner = findViewById(R.id.preferenceSpinner);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Set up the form based on the role
        if ("guest".equals(role)) {
            preferenceSpinner.setVisibility(View.VISIBLE);
        } else if ("host".equals(role)) {
            preferenceSpinner.setVisibility(View.GONE);
        }

        cancelButton.setOnClickListener(v -> cancelRegistration());
        submitButton.setOnClickListener(v -> submitForm());
    }

    private void cancelRegistration() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail().equals(email)) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationInfoActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegistrationInfoActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(RegistrationInfoActivity.this, "No user is currently signed in with the provided email.", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitForm() {
        String name = nameEditText.getText().toString().trim();
        String date = dateSpinner.getSelectedItem().toString();
        String month = monthSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();
        String dateOfBirth = year + "-" + month + "-" + date;

        if (name.isEmpty() || date.isEmpty() || month.isEmpty() || year.isEmpty()) {
            Toast.makeText(RegistrationInfoActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the highest current ID from the database
        db.collection("users").orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(1).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double newId = 1; // Default ID if the collection is empty
                        if (!task.getResult().isEmpty()) {
                            newId = task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        }

                        // Create the user instance
                        User user;
                        Intent intent;
                        Log.d("RegistrationInfoActivity", "Role: " + role); // Log the role value
                        if ("guest".equals(role)) {
                            String preference = preferenceSpinner.getSelectedItem().toString();
                            if (preference.isEmpty()) {
                                Toast.makeText(RegistrationInfoActivity.this, "Please select a preference.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            user = new Guest((int) newId, email, 0.0, name, dateOfBirth, preference);
                            intent = new Intent(RegistrationInfoActivity.this, GuestMainActivity.class);
                        } else {
                            user = new Host((int) newId, email, 0.0, name, dateOfBirth, 0);
                            intent = new Intent(RegistrationInfoActivity.this, HostMainActivity.class);
                        }

                        // Add the user ID to the Intent
                        intent.putExtra("id", newId);

                        // Save user to Firestore and navigate to the appropriate activity
                        FirebaseAction.addUserToFirestore(user).addOnSuccessListener(aVoid -> {
                            DatabaseManager localdb = new DatabaseManager(this);
                            localdb.open();
                            localdb.updateUser(user.getEmail());
                            localdb.close();

                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(RegistrationInfoActivity.this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                    } else {
                        Toast.makeText(RegistrationInfoActivity.this, "Failed to retrieve user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(RegistrationInfoActivity.this, "Failed to retrieve user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
