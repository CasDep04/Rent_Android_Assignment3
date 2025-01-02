package com.example.assignment3;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.component.Localdatabase.DatabaseManager;
import com.example.assignment3.component.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class GuestMainActivity extends AppCompatActivity {

    private static final String TAG = "GuestMainActivity";

    private FirebaseAuth mAuth;
    private DatabaseManager db;
    private TextView nameTextView, birthdayTextView, roleTextView, balanceTextView;
    private RelativeLayout view1, view2, view3;
    private Button buttonView1, buttonView2, buttonView3;
    private int currentView = 2; // Start with view2 visible

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

        buttonView1.setOnClickListener(v -> handleButtonClick(1));
        buttonView2.setOnClickListener(v -> handleButtonClick(2));
        buttonView3.setOnClickListener(v -> handleButtonClick(3));
    }

    private void handleButtonClick(int targetView) {
        if (currentView == targetView) {
            return; // No action needed if the target view is already visible
        }

        RelativeLayout currentViewLayout = getViewLayout(currentView);
        RelativeLayout targetViewLayout = getViewLayout(targetView);

        if (currentViewLayout != null && targetViewLayout != null) {
            float directionOut = (targetView > currentView) ? -1000f : 1000f;
            float directionIn = (targetView > currentView) ? 1000f : -1000f;

            Utils.animateViewOut(currentViewLayout, directionOut).addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentViewLayout.setVisibility(View.GONE);
                }
            });

            Utils.animateViewIn(targetViewLayout, directionIn).addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentView = targetView;
                }
            });;


        }
    }

    private RelativeLayout getViewLayout(int viewNumber) {
        switch (viewNumber) {
            case 1:
                return view1;
            case 2:
                return view2;
            case 3:
                return view3;
            default:
                return null;
        }
    }

    public void logOut(View view) {
        mAuth.signOut();
        db.deleteUser();
        Intent intent = new Intent(GuestMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
