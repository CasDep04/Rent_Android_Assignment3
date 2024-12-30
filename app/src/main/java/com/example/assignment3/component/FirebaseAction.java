package com.example.assignment3.component;

import android.util.Log;
import android.widget.Toast;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.Host;
import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.RegistrationInfoActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseAction {

    public static Task<Void> addLocationToFirestore(Location location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").document(String.valueOf(location.getId())).set(location);
    }
//    sample
//      FirebaseAction.addUserToFirestore(user)
//            .addOnSuccessListener(aVoid -> Toast.makeText(RegistrationInfoActivity .this, "User information saved successfully.", Toast.LENGTH_SHORT).show())
//            .addOnFailureListener(e -> Toast.makeText(RegistrationInfoActivity.this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    public static Task<Void> addReviewToFirestore(Review review) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews").document(String.valueOf(review.getId())).set(review);
    }

    public static Task<Void> addRecordToFirestore(RentalRecord record) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").document(String.valueOf(record.getId())).set(record);
    }

    public static Task<Void> addUserToFirestore(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(String.valueOf(user.getId())).set(user);
    }

    public static Task<Void> editLocationInFirestore(Location location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").document(String.valueOf(location.getId())).set(location);
    }

    public static Task<Void> editReviewInFirestore(Review review) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews").document(String.valueOf(review.getId())).set(review);
    }

    public static Task<Void> editRecordInFirestore(RentalRecord record) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").document(String.valueOf(record.getId())).set(record);
    }

    public static Task<Void> editUserInFirestore(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(String.valueOf(user.getId())).set(user);
    }

    public static Task<Void> deleteLocationFromFirestore(int locationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").document(String.valueOf(locationId)).delete();
    }

    public static Task<Void> deleteReviewFromFirestore(int reviewId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews").document(String.valueOf(reviewId)).delete();
    }

    public static Task<Void> deleteRecordFromFirestore(int recordId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").document(String.valueOf(recordId)).delete();
    }

    public static Task<Void> deleteUserFromFirestore(int userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(String.valueOf(userId)).delete();
    }

    public static Task<User> findUserById(int userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(String.valueOf(userId)).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Determine the type based on a field, e.g., "role"
                    String role = snapshot.getString("role");
                    if ("guest".equals(role)) {
                        return snapshot.toObject(Guest.class);
                    } else if ("host".equals(role)) {
                        return snapshot.toObject(Host.class);
                    } else {
                        return snapshot.toObject(User.class);
                    }
                } else {
                    throw new Exception("User not found");
                }
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find user");
            }
        });
    }
//   sample
//      double userIdDouble = intent.getDoubleExtra("id", 0);
//        if (userIdDouble != 0) {
//        int userId = (int) userIdDouble;
//        FirebaseAction.findUserById(userId).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                User user = task.getResult();
//                if (user instanceof Guest) {
//                    Guest guest = (Guest) user;
//                    displayGuestInfo(guest);
//                } else {
//                    Toast.makeText(this, "User is not a guest", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Exception e = task.getException();
//                if (e != null) {
//                    Log.e(TAG, "Failed to retrieve user", e);
//                    Toast.makeText(this, "Failed to retrieve user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Failed to retrieve user: Unknown error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    } else {
//        Toast.makeText(this, "User ID is missing or invalid", Toast.LENGTH_SHORT).show();
//    }

    public static Task<Location> findLocationById(int locationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").document(String.valueOf(locationId)).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return task.getResult().toObject(Location.class);
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find location");
            }
        });
    }

    public static Task<Review> findReviewById(int reviewId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews").document(String.valueOf(reviewId)).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return task.getResult().toObject(Review.class);
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find review");
            }
        });
    }

    public static Task<RentalRecord> findRecordById(int recordId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").document(String.valueOf(recordId)).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return task.getResult().toObject(RentalRecord.class);
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find rental record");
            }
        });
    }
}
