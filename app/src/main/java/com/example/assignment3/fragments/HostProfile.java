package com.example.assignment3.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment3.R;
import com.example.assignment3.LoginActivity;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostProfile extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    Button logoutButton;
    TextView greetingsText;
    DatabaseManager localDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        greetingsText = view.findViewById(R.id.user_details);
        logoutButton = view.findViewById(R.id.logout_btn);
        user = auth.getCurrentUser();

        localDB = new DatabaseManager(view.getContext());
        localDB.open();
        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            db.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");
                                greetingsText.setText("Welcome, " + user.getEmail() + " (" + role + ")");
                            } else {
                                greetingsText.setText("Welcome, " + user.getEmail());
                            }
                        } else {
                            greetingsText.setText("Welcome, " + user.getEmail());
                        }
                    });
        }

        logoutButton.setOnClickListener(view1 -> {
            auth.signOut();

            localDB.deleteUser();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();

        });
    }

    @Override
    public void onDestroyView() {
        localDB.close();
        super.onDestroyView();
    }
}