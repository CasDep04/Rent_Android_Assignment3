package com.example.assignment3;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.User;
import com.example.assignment3.component.FirebaseAction;
import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;

public class GuestMainActivity extends AppCompatActivity {

    private static final String TAG = "GuestMainActivity";

    private FirebaseAuth mAuth;
    private DatabaseManager db;
    private TextView nameTextView, birthdayTextView, roleTextView, balanceTextView;
    private RelativeLayout view1, view2, view3;
    private Button buttonView1, buttonView2, buttonView3;
    private boolean showingView1 = true, showingView2 = false, showingView3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        db = new DatabaseManager(this);
        db.open();
        mAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.nameTextView);
        birthdayTextView = findViewById(R.id.birthdayTextView);
        roleTextView = findViewById(R.id.roleTextView);
        balanceTextView = findViewById(R.id.balanceTextView);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);

        buttonView1 = findViewById(R.id.button1);
        buttonView2 = findViewById(R.id.button2);
        buttonView3 = findViewById(R.id.button3);

        buttonView1.setOnClickListener(v -> showView(view1, "view1"));
        buttonView2.setOnClickListener(v -> showView(view2, "view2"));
        buttonView3.setOnClickListener(v -> showView(view3, "view3"));

        Intent intent = getIntent();
        double userIdDouble = intent.getDoubleExtra("id", 0);
        Log.d(TAG, "Received user ID: " + userIdDouble); // Log the received user ID
        if (userIdDouble != 0) {
            int userId = (int) userIdDouble;
            FirebaseAction.findUserById(userId).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = task.getResult();
                    if (user instanceof Guest) {
                        Guest guest = (Guest) user;
                        displayGuestInfo(guest);
                    } else {
                        Toast.makeText(this, "User is not a guest", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Exception e = task.getException();
                    if (e != null) {
                        Log.e(TAG, "Failed to retrieve user", e);
                        Toast.makeText(this, "Failed to retrieve user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to retrieve user: Unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "User ID is missing or invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayGuestInfo(Guest guest) {
        nameTextView.setText("Name: " + guest.getName());
        birthdayTextView.setText("Birthday: " + guest.getDateOfBirth());
        roleTextView.setText("Role: " + guest.getRole());
        balanceTextView.setText("Balance: " + guest.getBalance());
    }

    public void logOut(View view) {
        mAuth.signOut();
        db.deleteUser();
        Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showView(RelativeLayout viewToShow, String viewTag) {
        if (viewTag.equals("view1") && !showingView1) {
            animateViewOut(view2, -1000f);
            animateViewOut(view3, 1000f);
            animateViewIn(view1, 0f);
            showingView1 = true;
            showingView2 = false;
            showingView3 = false;
        } else if (viewTag.equals("view2") && !showingView2) {
            animateViewOut(view1, 1000f);
            animateViewOut(view3, -1000f);
            animateViewIn(view2, 0f);
            showingView1 = false;
            showingView2 = true;
            showingView3 = false;
        } else if (viewTag.equals("view3") && !showingView3) {
            animateViewOut(view1, -1000f);
            animateViewOut(view2, 1000f);
            animateViewIn(view3, 0f);
            showingView1 = false;
            showingView2 = false;
            showingView3 = true;
        }
    }

    private void animateViewOut(View view, float translationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animator.setDuration(300);
        animator.start();
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }

    private void animateViewIn(View view, float translationX) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animator.setDuration(300);
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
