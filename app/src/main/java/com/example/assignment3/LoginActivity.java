package com.example.assignment3;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.component.Localdatabase.DatabaseHelper;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.component.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private EditText registerEmailEditText, registerPasswordEditText, registerConfirmPasswordEditText;
    private RelativeLayout loginView, registerView;
    private Spinner roleSpinner;
    private DatabaseManager LocalDB;
    private FirebaseFirestore firebaseDB;
    private Button loginButton, toRegisterViewButton, registerButton, goBackButton;

    private static final float ANIMATION_DISTANCE = 1000f; // Default animation distance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LocalDB = new DatabaseManager(this);
        LocalDB.open();
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        registerEmailEditText = findViewById(R.id.register_email);
        registerPasswordEditText = findViewById(R.id.register_password);
        registerConfirmPasswordEditText = findViewById(R.id.register_confirm_password);

        loginView = findViewById(R.id.loginView);
        registerView = findViewById(R.id.registerView);

        roleSpinner = findViewById(R.id.roleSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        try (Cursor cursor = LocalDB.selectAllUsers()) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                goToMainActivity(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_EMAIL)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginButton = findViewById(R.id.loginButton);
        toRegisterViewButton = findViewById(R.id.toRegisterViewButton);
        registerButton = findViewById(R.id.registerButton);
        goBackButton = findViewById(R.id.goBackButton);

        toRegisterViewButton.setOnClickListener(v -> toRegisterView());
        goBackButton.setOnClickListener(v -> toLoginView());

        Utils.animateViewOut(registerView, ANIMATION_DISTANCE);
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
                        LocalDB.updateUser(email);
                        goToMainActivity(email);
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void toRegisterView() {
        animateViewTransition(loginView, registerView, -ANIMATION_DISTANCE, ANIMATION_DISTANCE);
    }

    public void toLoginView() {
        animateViewTransition(registerView, loginView, ANIMATION_DISTANCE, -ANIMATION_DISTANCE);
    }

    private void animateViewTransition(View viewOut, View viewIn, float directionOut, float directionIn) {
        Utils.animateViewOut(viewOut, directionOut).addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewOut.setVisibility(View.GONE);
            }
        });
        Utils.animateViewIn(viewIn, directionIn);
    }

    public void registerUser(View view) {
        String email = registerEmailEditText.getText().toString();
        String password = registerPasswordEditText.getText().toString();
        String confirmPassword = registerConfirmPasswordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(LoginActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful
                        Toast.makeText(LoginActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        // Sign in the user
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, signInTask -> {
                                    if (signInTask.isSuccessful()) {
                                        // Sign-in successful
                                        FirebaseUser user = signInTask.getResult().getUser();

                                        Intent i1 = new Intent(LoginActivity.this, RegistrationInfoActivity.class);
                                        i1.putExtra("email", email);
                                        i1.putExtra("role", role);
                                        startActivity(i1);
                                        finish();
                                    } else {
                                        // Sign-in failed - handle errors
                                        String errorMessage = signInTask.getException() != null ? signInTask.getException().getMessage() : "Sign-in failed.";
                                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // Registration failed - handle errors
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToMainActivity(String emailIn) {
        firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("users")
                .whereEqualTo("email", emailIn)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String role = document.getString("role");
                                double id = document.getDouble("id");
                                Intent intent;
                                if ("admin".equals(role)) {
                                    intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                } else if ("guest".equals(role)) {
                                    intent = new Intent(LoginActivity.this, GuestMainActivity.class);
                                } else if ("host".equals(role)){
                                    intent = new Intent(LoginActivity.this, HostMainActivity.class);
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Unknown role.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                intent.putExtra("id", id);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "No user found with this email.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Exception e = task.getException();
                        Toast.makeText(LoginActivity.this, "Error getting documents: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
