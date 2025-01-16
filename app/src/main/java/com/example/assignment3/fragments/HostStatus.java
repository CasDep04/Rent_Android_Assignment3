package com.example.assignment3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment3.R;
import com.example.assignment3.component.adapter.StatusAdapter;
import com.example.assignment3.Entity.RentalRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HostStatus extends Fragment {

    private RecyclerView statusRecyclerView;
    private StatusAdapter statusAdapter;
    private List<RentalRecord> rentalRecords;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rentalRecords = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusRecyclerView = view.findViewById(R.id.status_recycler_view);
        statusRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        statusAdapter = new StatusAdapter(rentalRecords, userId);
        statusRecyclerView.setAdapter(statusAdapter);

        // Retrieve the user ID from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userId", -1);
        } else {
            userId = -1;
        }

        fetchRentalRecords();
    }

    private void fetchRentalRecords() {
        if (userId != -1) {
            db.collection("rentalRecords")
                    .whereEqualTo("hostId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                rentalRecords.clear();
                                rentalRecords.addAll(querySnapshot.toObjects(RentalRecord.class));
                                statusAdapter.updateData(rentalRecords);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to fetch rental records", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}