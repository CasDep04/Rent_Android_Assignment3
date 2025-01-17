package com.example.assignment3.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.assignment3.R;
import com.example.assignment3.LoginActivity;
import com.example.assignment3.Entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostProfile extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore db;
    Button logoutButton, withdrawButton;
    TextView idText, emailText, roleText, balanceText, nameText, dateOfBirthText;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        idText = view.findViewById(R.id.user_id);
        emailText = view.findViewById(R.id.user_email);
        roleText = view.findViewById(R.id.user_role);
        balanceText = view.findViewById(R.id.user_balance);
        nameText = view.findViewById(R.id.user_name);
        dateOfBirthText = view.findViewById(R.id.user_date_of_birth);
        logoutButton = view.findViewById(R.id.logout_btn);
        withdrawButton = view.findViewById(R.id.withdraw_btn);

        // Retrieve userId from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userId", -1);
        } else {
            userId = -1;
        }

        if (userId == -1) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            fetchUserProfile();
        }

        logoutButton.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        withdrawButton.setOnClickListener(view2 -> {
            db.collection("users").document(String.valueOf(userId)).update("balance", 0)
                    .addOnSuccessListener(aVoid -> {
                        fetchUserProfile();
                        showTransactionCompleteDialog();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error processing withdrawal", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void fetchUserProfile() {
        db.collection("users").document(String.valueOf(userId)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User userProfile = document.toObject(User.class);
                            if (userProfile != null) {
                                idText.setText("ID: " + userProfile.getId());
                                emailText.setText("Email: " + userProfile.getEmail());
                                roleText.setText("Role: " + userProfile.getRole());
                                balanceText.setText("Balance: $" + userProfile.getBalance());
                                nameText.setText("Name: " + userProfile.getName());
                                dateOfBirthText.setText("Date of Birth: " + userProfile.getDateOfBirth());
                            }
                        } else {
                            emailText.setText("Welcome, " + userId);
                        }
                    } else {
                        emailText.setText("Welcome, " + userId);
                    }
                });
    }

    private void showTransactionCompleteDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Transaction Complete")
                .setMessage("Balance transferred to your bank account")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}