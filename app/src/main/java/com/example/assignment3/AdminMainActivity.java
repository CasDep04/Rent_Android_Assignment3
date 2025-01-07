package com.example.assignment3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewAnimator;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.User;
import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.adapter.UserAdapter;
import com.example.assignment3.component.adapter.LocationAdapter;
import com.example.assignment3.component.adapter.ReviewAdapter;
import com.example.assignment3.component.adapter.RentalRecordAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ViewAnimator viewAnimator;
    private Button buttonUsers, buttonLocations, buttonReviews, buttonRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mAuth = FirebaseAuth.getInstance();

        viewAnimator = findViewById(R.id.viewAnimator);
        buttonUsers = findViewById(R.id.buttonUsers);
        buttonLocations = findViewById(R.id.buttonLocations);
        buttonReviews = findViewById(R.id.buttonReviews);
        buttonRecords = findViewById(R.id.buttonRecords);

        buttonUsers.setOnClickListener(v -> loadUsers());
        buttonLocations.setOnClickListener(v -> loadLocations());
        buttonReviews.setOnClickListener(v -> loadReviews());
        buttonRecords.setOnClickListener(v -> loadRecords());

        // Load users by default
        loadUsers();
    }

    private void loadUsers() {
        FirebaseAction.findAllUsers().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> users = task.getResult();
                UserAdapter adapter = new UserAdapter(this, users);
                ListView listView = findViewById(R.id.listViewUsers);
                listView.setAdapter(adapter);
                viewAnimator.setDisplayedChild(0);
            }
        });
    }

    private void loadLocations() {
        FirebaseAction.findAllLocations().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Location> locations = task.getResult();
                LocationAdapter adapter = new LocationAdapter(this, locations);
                ListView listView = findViewById(R.id.listViewLocations);
                listView.setAdapter(adapter);
                viewAnimator.setDisplayedChild(1);
            }
        });
    }

    private void loadReviews() {
        FirebaseAction.findAllReviews().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Review> reviews = task.getResult();
                ReviewAdapter adapter = new ReviewAdapter(this, reviews);
                ListView listView = findViewById(R.id.listViewReviews);
                listView.setAdapter(adapter);
                viewAnimator.setDisplayedChild(2);
            }
        });
    }

    private void loadRecords() {
        FirebaseAction.findAllRecords().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<RentalRecord> records = task.getResult();
                RentalRecordAdapter adapter = new RentalRecordAdapter(this, records);
                ListView listView = findViewById(R.id.listViewRecords);
                listView.setAdapter(adapter);
                viewAnimator.setDisplayedChild(3);
            }
        });
    }
}