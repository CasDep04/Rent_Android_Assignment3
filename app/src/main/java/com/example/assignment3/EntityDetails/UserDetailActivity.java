package com.example.assignment3.EntityDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.User;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class UserDetailActivity extends AppCompatActivity {

    private TextView userIdTextView;
    private TextView userEmailTextView;
    private TextView userRoleTextView;
    private TextView userBalanceTextView;
    private TextView userNameTextView;
    private TextView userDateOfBirthTextView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Initialize TextViews
        userIdTextView = findViewById(R.id.textViewUserIdValue);
        userEmailTextView = findViewById(R.id.textViewUserEmailValue);
        userRoleTextView = findViewById(R.id.textViewUserRoleValue);
        userBalanceTextView = findViewById(R.id.textViewUserBalanceValue);
        userNameTextView = findViewById(R.id.textViewUserNameValue);
        userDateOfBirthTextView = findViewById(R.id.textViewUserDateOfBirthValue);
        goBackButton = findViewById(R.id.goBackButton);

        // Get the User ID from the intent
        int userId = getIntent().getIntExtra("userID", 0);

        // Fetch the User details
        FirebaseAction.findUserById(userId).addOnCompleteListener(new OnCompleteListener<User>() {
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    User user = task.getResult();
                    displayUserDetails(user);
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set go back button click listener
        goBackButton.setOnClickListener(v -> finish());
    }

    private void displayUserDetails(User user) {
        userIdTextView.setText(String.valueOf(user.getId()));
        userEmailTextView.setText(user.getEmail());
        userRoleTextView.setText(user.getRole());
        userBalanceTextView.setText(String.valueOf(user.getBalance()));
        userNameTextView.setText(user.getName());
        userDateOfBirthTextView.setText(user.getDateOfBirth());
    }
}