package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddBalanceActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button confirmButton;
    private double currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        // Get the current balance from the intent
        Intent intent = getIntent();
        currentBalance = intent.getDoubleExtra("balance", 0);

        // Initialize the RadioGroup and Confirm Button
        radioGroup = findViewById(R.id.radioGroup);
        confirmButton = findViewById(R.id.confirmButton);

        // Set up the Go Back button
        Button goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(v -> finish());

        // Set up the Confirm button
        confirmButton.setOnClickListener(v -> confirmSelection());

        // Change Confirm button color when any RadioButton is selected
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Change Confirm button to full green when any RadioButton is selected
            confirmButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_dark));
        });
    }

    private void confirmSelection() {
        // Get the selected RadioButton
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedValue = selectedRadioButton.getText().toString().replace("$", "");
            double selectedAmount = Double.parseDouble(selectedValue);

            // Calculate the new balance
            double newBalance = currentBalance + selectedAmount;

            // Create an Intent to hold the result
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedBalance", newBalance);

            // Set the result and finish the activity
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            // Handle the case where no option is selected
            // For example, show a Toast message
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }
}