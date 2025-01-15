package com.example.assignment3.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.assignment3.R;
import com.example.assignment3.component.adapter.RentalAdapter;
import com.example.assignment3.Entity.Rental;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class HostHomestay extends Fragment {

    private RecyclerView recyclerView;
    private RentalAdapter rentalAdapter;
    private List<Rental> rentalList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private SearchView searchView;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_homestay, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rental_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize rental list
        rentalList = new ArrayList<>();

        // Set up the adapter
        rentalAdapter = new RentalAdapter(getContext(), rentalList);
        recyclerView.setAdapter(rentalAdapter);

        // Initialize SearchView and handle text changes
        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);  // Apply both search and spinner filters when text changes
                return false;
            }
        });

        // Initialize Spinner and set up the adapter
        spinner = view.findViewById(R.id.filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.property_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Spinner item selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList(searchView.getQuery().toString());  // Apply both search and spinner filters when the spinner changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });

        // Fetch data from Firestore initially
        fetchRentalsFromFirestore();

        return view;
    }

    // Method to filter the list based on search query and selected spinner type
    private void filterList(String newText) {
        List<Rental> filteredList = new ArrayList<>();
        String selectedType = (String) spinner.getSelectedItem();  // Get selected property type from the spinner

        // Check if we should fetch everything
        if (newText.isEmpty() && selectedType.equals("All")) {
            fetchRentalsFromFirestore();
            return;
        }

        // Apply filter based on search text and spinner type
        for (Rental rental : rentalList) {
            boolean matchesSearch = newText.isEmpty() || rental.getName().toLowerCase().contains(newText.toLowerCase());
            boolean matchesType = selectedType.equals("All") || rental.getPropertyType().equalsIgnoreCase(selectedType);

            if (matchesSearch && matchesType) {
                filteredList.add(rental);
            }
        }

        // If no rentals match the filters, show a toast
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No rentals found", Toast.LENGTH_SHORT).show();
        }

        // Update the adapter with the new filtered list
        rentalAdapter.setFilteredList(filteredList);
    }

    // Method to fetch rentals from Firestore
    private void fetchRentalsFromFirestore() {
        String hostId = auth.getCurrentUser().getUid();
        db.collection("rentals")
                .whereEqualTo("hostId", hostId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        rentalList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Rental rental = document.toObject(Rental.class);
                            rental.setId(document.getId());
                            rentalList.add(rental);
                        }
                        // Apply filtering after data fetch
                        filterList(searchView.getQuery().toString());  // Ensure that the list is filtered according to the search
                        rentalAdapter.notifyDataSetChanged();  // Notify adapter to refresh the UI
                    } else {
                        Toast.makeText(getContext(), "Error getting rentals.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

