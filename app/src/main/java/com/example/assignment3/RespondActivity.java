package com.example.assignment3;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.component.adapter.RespondAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class RespondActivity extends AppCompatActivity {
    private static final String TAG = "RespondActivity";
    private RecyclerView recyclerView;
    private RespondAdapter respondAdapter;
    private List<RentalRecord> rentalRecordList;
    private FirebaseFirestore db;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);

        recyclerView = findViewById(R.id.rental_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rentalRecordList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Retrieve userId from Intent
        userId = getIntent().getIntExtra("userId", -1);
        Log.d(TAG, "onCreate: Retrieved user ID: " + userId);

        fetchRentalRecords();
    }

    private void fetchRentalRecords() {
        db.collection("rentalRecords")
                .whereEqualTo("hostId", userId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            rentalRecordList.clear();
                            rentalRecordList.addAll(querySnapshot.toObjects(RentalRecord.class));
                            Log.d(TAG, "fetchRentalRecords: Fetched " + rentalRecordList.size() + " records");
                            respondAdapter = new RespondAdapter(this, rentalRecordList, userId);
                            recyclerView.setAdapter(respondAdapter);
                        } else {
                            Log.d(TAG, "fetchRentalRecords: No records found");
                        }
                    } else {
                        Log.e(TAG, "fetchRentalRecords: Error fetching records", task.getException());
                    }
                });
    }
}