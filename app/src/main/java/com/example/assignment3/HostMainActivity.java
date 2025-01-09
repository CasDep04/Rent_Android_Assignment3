package com.example.assignment3;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.fragments.HostHome;
import com.example.assignment3.fragments.HostHomestay;
import com.example.assignment3.fragments.HostNotification;
import com.example.assignment3.fragments.HostProfile;
import com.example.assignment3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        frameLayout = findViewById(R.id.frame_layout);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.host_home) {
                    loadFragment(new HostHome(), false);
                } else if (itemId == R.id.host_homestay) {
                    loadFragment(new HostHomestay(), false);
                } else if (itemId == R.id.host_notification) {
                    loadFragment(new HostNotification(), false);
                } else { // Host Profile
                    loadFragment(new HostProfile(), false);
                }
                return true;
            }
        });
        loadFragment(new HostHome(), true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}