package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.User;
import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.component.adapter.UserAdapter;
import com.example.assignment3.component.adapter.LocationAdapter;
import com.example.assignment3.component.adapter.ReviewAdapter;
import com.example.assignment3.component.adapter.RentalRecordAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ListView listView;
    private Button buttonAdd, buttonBack, buttonLogout;
    private ImageButton buttonUsers, buttonLocations, buttonReviews, buttonRecords;
    private View gridLayout;
    private int currentView = 0;
    private DatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.listView);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonBack = findViewById(R.id.buttonBack);
        buttonLogout = findViewById(R.id.goBackButton);
        buttonUsers = findViewById(R.id.buttonUsers);
        buttonLocations = findViewById(R.id.buttonLocations);
        buttonReviews = findViewById(R.id.buttonReviews);
        buttonRecords = findViewById(R.id.buttonRecords);
        gridLayout = findViewById(R.id.gridLayout);

        buttonUsers.setOnClickListener(v -> loadUsers());
        buttonLocations.setOnClickListener(v -> loadLocations());
        buttonReviews.setOnClickListener(v -> loadReviews());
        buttonRecords.setOnClickListener(v -> loadRecords());
        buttonLogout.setOnClickListener(v -> signOut());
        buttonBack.setOnClickListener(v -> showGridLayout());
        buttonAdd.setOnClickListener(v -> addNewData());
    }

    private void loadUsers() {
        FirebaseAction.findAllUsers().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> users = task.getResult();
                UserAdapter adapter = new UserAdapter(this, users);
                listView.setAdapter(adapter);
                buttonAdd.setText("Add New User");
                currentView = 1;
                showListView();
            }
        }).addOnFailureListener(this::handleFailure);
    }

    private void loadLocations() {
        FirebaseAction.findAllLocations().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Location> locations = task.getResult();
                LocationAdapter adapter = new LocationAdapter(this, locations);
                listView.setAdapter(adapter);
                buttonAdd.setText("Add New Location");
                currentView = 2;
                showListView();
            }
        }).addOnFailureListener(this::handleFailure);
    }

    private void loadReviews() {
        FirebaseAction.findAllReviews().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Review> reviews = task.getResult();
                ReviewAdapter adapter = new ReviewAdapter(this, reviews);
                listView.setAdapter(adapter);
                buttonAdd.setText("Add New Review");
                currentView = 3;
                showListView();
            }
        }).addOnFailureListener(this::handleFailure);
    }

    private void loadRecords() {
        FirebaseAction.findAllRecords().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<RentalRecord> records = task.getResult();
                RentalRecordAdapter adapter = new RentalRecordAdapter(this, records);
                listView.setAdapter(adapter);
                buttonAdd.setText("Add New Records");
                currentView = 4;
                showListView();
            }
        }).addOnFailureListener(this::handleFailure);
    }

    private void showListView() {
        gridLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        buttonAdd.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
    }

    private void showGridLayout() {
        currentView = 0;
        gridLayout.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        buttonAdd.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);
    }

    private void addNewData(){
        switch(currentView){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
    }
    private void handleFailure(Exception exception) {
        String message = exception != null ? exception.getMessage() : "Failed to load data";
        Log.e("AdminMainActivity", message, exception);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void signOut() {
        mAuth.signOut();

        db.deleteUser();
        Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}