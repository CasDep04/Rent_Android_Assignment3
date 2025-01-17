package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.component.adapter.StatusAdapter;
import com.example.assignment3.component.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogout;
    private int currentView = 0;
    private DatabaseManager db;
    private List<RentalRecord> rentalRecords = new ArrayList<>();
    private GridLayout gridLayout;
    private RecyclerView recyclerView;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();
        gridLayout = findViewById(R.id.gridLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonLogout = findViewById(R.id.buttonLogout);

        ImageButton buttonUsers = findViewById(R.id.buttonUsers);
        ImageButton buttonLocations = findViewById(R.id.buttonLocations);
        ImageButton buttonReviews = findViewById(R.id.buttonReviews);
        ImageButton buttonRecords = findViewById(R.id.buttonRecords);

        buttonUsers.setOnClickListener(v -> showListView("users"));
        buttonLocations.setOnClickListener(v -> showListView("locations"));
        buttonReviews.setOnClickListener(v -> showListView("reviews"));
        buttonRecords.setOnClickListener(v -> showListView("records"));

        buttonCancel.setOnClickListener(v -> showGridLayout());
        buttonLogout.setOnClickListener(v -> signOut());

    }
    private void showListView(String type) {
        gridLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.VISIBLE);

        switch (type) {
            case "users":
                break;
            case "locations":
                //TODO
                break;
            case "reviews":
                //TODO: add stuff
                break;
            case "records":
                FirebaseAction.findAllRecords().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<RentalRecord> records = task.getResult();
                        
                        rentalRecords.clear();
                        rentalRecords.addAll(task.getResult());

                        for (RentalRecord record : records) {
                            StatusAdapter adapter = new StatusAdapter(rentalRecords,record.getGuestId());
                            recyclerView.setAdapter(adapter);
                        }

                    } else {

                    }
                });
                break;
        }
    }

    private void showGridLayout() {
        gridLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}