package com.example.assignment3.component;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.Host;
import com.example.assignment3.Entity.Location;
import com.example.assignment3.Entity.Rental;
import com.example.assignment3.Entity.Review;
import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
//import com.example.assignment3.GuestMainActivity;
import com.example.assignment3.HostMainActivity;
import com.example.assignment3.RegistrationInfoActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAction {

    private static final String TAG = "FirebaseAction";

    public static Task<Void> addLocationToFirestore(Location location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        int newId = task.getResult().isEmpty() ? 1 : task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        location.setId(newId);
                        return db.collection("locations").document(String.valueOf(location.getId())).set(location);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }
//    sample
//      FirebaseAction.addUserToFirestore(user)
//            .addOnSuccessListener(aVoid -> Toast.makeText(RegistrationInfoActivity .this, "User information saved successfully.", Toast.LENGTH_SHORT).show())
//            .addOnFailureListener(e -> Toast.makeText(RegistrationInfoActivity.this, "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    public static Task<Void> addReviewToFirestore(Review review) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        int newId = task.getResult().isEmpty() ? 1 : task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        review.setId(newId);
                        return db.collection("reviews").document(String.valueOf(review.getId())).set(review);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    public static Task<Void> addRecordToFirestore(RentalRecord record) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        int newId = task.getResult().isEmpty() ? 1 : task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        record.setId(newId);
                        return db.collection("rentalRecords").document(String.valueOf(record.getId())).set(record);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    public static Task<Void> addUserToFirestore(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        int newId = task.getResult().isEmpty() ? 1 : task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        user.setId(newId);
                        return db.collection("users").document(String.valueOf(user.getId())).set(user);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
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
        //Authentication: delete user only
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().delete()
                    .addOnSuccessListener(aVoid -> Log.d("DeleteUser", "Firebase Authentication account deleted successfully."))
                    .addOnFailureListener(e -> Log.e("DeleteUser", "Failed to delete Firebase Authentication account: " + e.getMessage()));
        } else {
            Log.e("DeleteUser", "No current user logged in. Unable to delete Firebase Authentication account.");
        }
        return db.collection("users").document(String.valueOf(userId)).delete();
    }
    public static Task<Void> specialDeleteUserFromFirestore(int userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(String.valueOf(userId)).delete();
    }
    public static Task<User> findUserById(int userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("FirebaseAction", "Finding user by ID: " + userId);
        return db.collection("users").document(String.valueOf(userId)).get().continueWith(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find user");
            }
    
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                String role = snapshot.getString("role");
                Log.d("FirebaseAction", "User found with role: " + role);
                if ("guest".equals(role)) {
                    return snapshot.toObject(Guest.class);
                } else if ("host".equals(role)) {
                    return snapshot.toObject(Host.class);
                } else {
                    return snapshot.toObject(User.class);
                }
            } else {
                Log.e("FirebaseAction", "User not found");
                throw new Exception("User not found");
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

    public static Task<List<RentalRecord>> findRecordsByGuestId(int guestId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").whereEqualTo("guestId", guestId).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<RentalRecord> records = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    records.add(document.toObject(RentalRecord.class));
                }
                return records;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find rental records by guestId");
            }
        });
    }

    public static Task<List<RentalRecord>> findRecordsByHostId(int hostId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").whereEqualTo("hostId", hostId).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<RentalRecord> records = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    records.add(document.toObject(RentalRecord.class));
                }
                return records;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find rental records by hostId");
            }
        });
    }

    public static Task<List<User>> findAllUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> users = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    users.add(document.toObject(User.class));
                }
                return users;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find users");
            }
        });
    }

    public static Task<List<RentalRecord>> findAllRecords() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentalRecords").get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<RentalRecord> records = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    records.add(document.toObject(RentalRecord.class));
                }
                return records;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find rental records");
            }
        });
    }

    public static Task<List<Location>> findAllLocations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Location> locations = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    locations.add(document.toObject(Location.class));
                }
                return locations;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find locations");
            }
        });
    }

    public static Task<List<Review>> findAllReviews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("reviews").get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Review> reviews = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    reviews.add(document.toObject(Review.class));
                }
                return reviews;
            } else {
                throw task.getException() != null ? task.getException() : new Exception("Failed to find reviews");
            }
        });
    }

    // Create a new rental record in Firestore
    public static Task<Void> addRentalRecordToFirestore(RentalRecord rentalRecord) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        int newId = task.getResult().isEmpty() ? 1 : task.getResult().getDocuments().get(0).getLong("id").intValue() + 1;
                        rentalRecord.setId(newId);
                        return db.collection("rentals").document(String.valueOf(rentalRecord.getId())).set(rentalRecord);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    // Read all rental records from Firestore
    public static Task<List<RentalRecord>> getAllRentalRecordsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObjects(RentalRecord.class);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    // Read a specific rental record by ID from Firestore
    public static Task<RentalRecord> getRentalRecordByIdFromFirestore(int id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals").document(String.valueOf(id))
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObject(RentalRecord.class);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    // Read rental records by hostId from Firestore
    public static Task<List<RentalRecord>> getRentalRecordsByHostIdFromFirestore(int hostId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals")
                .whereEqualTo("hostId", hostId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObjects(RentalRecord.class);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    // Read rental records by guestId from Firestore
    public static Task<List<RentalRecord>> getRentalRecordsByGuestIdFromFirestore(int guestId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals")
                .whereEqualTo("guestId", guestId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObjects(RentalRecord.class);
                    } else {
                        throw task.getException(); // Re-throw the exception if the query fails
                    }
                });
    }

    // Update an existing rental record in Firestore
    public static Task<Void> updateRentalRecordInFirestore(RentalRecord rentalRecord) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals").document(String.valueOf(rentalRecord.getId())).set(rentalRecord);
    }

    // Delete a rental record from Firestore
    public static Task<Void> deleteRentalRecordFromFirestore(int id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("rentals").document(String.valueOf(id)).delete();
    }
}
