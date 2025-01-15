package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(@NonNull Context context, @NonNull List<Review> reviews) {
        super(context, 0, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.review_list_component, parent, false);
        }

        Review review = reviews.get(position);

        TextView reviewIdTextView = convertView.findViewById(R.id.textViewReviewIdValue);
        TextView locationNameTextView = convertView.findViewById(R.id.textViewLocationNameValue);
        TextView ratingTextView = convertView.findViewById(R.id.textViewRatingValue);

        reviewIdTextView.setText(String.valueOf(review.getId()));
        ratingTextView.setText(String.valueOf(review.getRating()));

        FirebaseAction.findLocationById(review.getLocationId()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                locationNameTextView.setText(location.getAddress());
            } else {
                locationNameTextView.setText("Unknown Location");
            }
        });

        return convertView;
    }
}