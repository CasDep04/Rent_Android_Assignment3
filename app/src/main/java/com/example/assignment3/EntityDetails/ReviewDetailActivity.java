package com.example.assignment3.EntityDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView reviewIdTextView;
    private TextView locationNameTextView;
    private TextView ratingTextView;
    private TextView commentsTextView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        // Initialize TextViews
        reviewIdTextView = findViewById(R.id.textViewReviewIdValue);
        locationNameTextView = findViewById(R.id.textViewLocationNameValue);
        ratingTextView = findViewById(R.id.textViewRatingValue);
        commentsTextView = findViewById(R.id.textViewCommentsValue);
        goBackButton = findViewById(R.id.goBackButton);

        // Get the Review ID from the intent
        int reviewId = getIntent().getIntExtra("reviewID", 0);

        // Fetch the Review details
        FirebaseAction.findReviewById(reviewId).addOnCompleteListener(new OnCompleteListener<Review>() {
            @Override
            public void onComplete(@NonNull Task<Review> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Review review = task.getResult();
                    displayReviewDetails(review);
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

    private void displayReviewDetails(Review review) {
        reviewIdTextView.setText(String.valueOf(review.getId()));
        ratingTextView.setText(String.valueOf(review.getRating()));
        commentsTextView.setText(review.getComments());

        // Fetch the location name
        FirebaseAction.findLocationById(review.getLocationId()).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    locationNameTextView.setText(location.getAddress());
                } else {
                    locationNameTextView.setText("Unknown Location");
                }
            }
        });
    }
}