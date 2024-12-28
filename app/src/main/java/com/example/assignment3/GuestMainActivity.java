package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Localdatabase.DatabaseManager;

public class GuestMainActivity extends AppCompatActivity {

    DatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
        db = new DatabaseManager(this);
        db.open();
    }

    public void logOut(View view){
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