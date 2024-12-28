package com.example.assignment3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Localdatabase.DatabaseHelper;
import com.example.assignment3.Localdatabase.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseManager(this);
        db.open();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        try (Cursor cursor = db.selectAllUsers()) {
            if (cursor != null || cursor.getCount() > 0) {
                goToActivity(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_EMAIL)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                    db.updateUser(email);
                    goToActivity(email);
                    // Navigate to another activity or update UI
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void registerUser(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                    // Navigate to another activity or update UI
                } else {
                    Toast.makeText(LoginActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void goToActivity(String emailIn) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .whereEqualTo("email", emailIn)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String role = document.getString("role");
                            Intent intent;
                            if ("admin".equals(role)) {
                                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            } else if ("guest".equals(role)) {
                                intent = new Intent(LoginActivity.this, GuestMainActivity.class);
                            }else if("host".equals(role)){
                                //TODO: add host activity
                                Toast.makeText(LoginActivity.this, "Unknown role.", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                Toast.makeText(LoginActivity.this, "Unknown role.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            intent.putExtra("email", emailIn);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No user found with this email.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}