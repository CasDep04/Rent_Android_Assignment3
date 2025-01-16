package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.fragments.HostHome;
import com.example.assignment3.fragments.HostHomestay;
import com.example.assignment3.fragments.HostProfile;
import com.example.assignment3.fragments.HostStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostMainActivity extends AppCompatActivity {
    private static final String TAG = "HostMainActivity";
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        frameLayout = findViewById(R.id.frame_layout);

        // Retrieve the user ID from the Intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        Log.d(TAG, "onCreate: Retrieved user ID: " + userId);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment fragment;
                if (itemId == R.id.host_home) {
                    fragment = new HostHome();
                } else if (itemId == R.id.host_homestay) {
                    fragment = new HostHomestay();
                } else if (itemId == R.id.host_status) {
                    fragment = new HostStatus();
                } else { // Host Profile
                    fragment = new HostProfile();
                }
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                fragment.setArguments(bundle);
                loadFragment(fragment, false);
                return true;
            }
        });

        Fragment fragment = new HostHome();
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        fragment.setArguments(bundle);
        loadFragment(fragment, true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }
        fragmentTransaction.commit();
    }
}