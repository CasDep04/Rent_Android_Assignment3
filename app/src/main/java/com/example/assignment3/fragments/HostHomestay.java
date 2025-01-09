package com.example.assignment3.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.assignment3.R;
import com.example.assignment3.component.adapter.RentalAdapter;
import com.example.assignment3.Entity.Rental;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class HostHomestay extends Fragment {

    private RecyclerView recyclerView;
    private RentalAdapter rentalAdapter;
    private List<Rental> rentalList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_homestay, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rental_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize rental list
        rentalList = new ArrayList<>();

        // Set up the adapter
        rentalAdapter = new RentalAdapter(getContext(), rentalList);
        recyclerView.setAdapter(rentalAdapter);

        // Fetch data from Firestore
        fetchRentalsFromFirestore();

        return view;
    }

    private void fetchRentalsFromFirestore() {
        db.collection("rentals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Rental rental = document.toObject(Rental.class);
                    rental.setId(document.getId());
                    rentalList.add(rental);
                }
                rentalAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error getting rentals.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}