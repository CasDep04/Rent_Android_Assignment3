package com.example.assignment3.component.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.assignment3.R;
import com.example.assignment3.Entity.Rental;
import com.example.assignment3.RentalDetailPageActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.RentalViewHolder> {

    private final Context context;
    private final List<Rental> rentalList;
    private final FirebaseFirestore db;

    public RentalAdapter(Context context, List<Rental> rentalList) {
        this.context = context;
        this.rentalList = rentalList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rental, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental rental = rentalList.get(position);
        holder.homestayName.setText(rental.getName());
        holder.homestayAddress.setText(rental.getAddress());
        holder.homestayPrice.setText("$" + rental.getPricePerNight() + " per night");
        holder.propertyType.setText(rental.getPropertyType());
        Glide.with(context).load(rental.getImageUrl()).into(holder.selectedImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RentalDetailPageActivity.class);
            intent.putExtra("RENTAL_ID", rental.getId());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(rental, position));
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    private void showDeleteConfirmationDialog(Rental rental, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Homestay")
                .setMessage("Are you sure you want to delete this homestay?")
                .setPositiveButton("Yes", (dialog, which) -> deleteRental(rental, position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRental(Rental rental, int position) {
        // Get a reference to the image in Firebase Storage
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(rental.getImageUrl());

        // Delete the image from Firebase Storage
        imageRef.delete().addOnSuccessListener(aVoid -> {
            // Image deleted successfully, now delete the rental from Firestore
            db.collection("rentals").document(rental.getId()).delete()
                    .addOnSuccessListener(aVoid1 -> {
                        rentalList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, rentalList.size());
                        Toast.makeText(context, "Rental deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to delete rental", Toast.LENGTH_SHORT).show();
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show();
        });
    }

    static class RentalViewHolder extends RecyclerView.ViewHolder {
        TextView homestayName, homestayAddress, homestayPrice, propertyType;
        ImageView selectedImage;
        ImageButton deleteButton;

        public RentalViewHolder(@NonNull View itemView) {
            super(itemView);
            homestayName = itemView.findViewById(R.id.homestay_name);
            homestayAddress = itemView.findViewById(R.id.homestay_address);
            homestayPrice = itemView.findViewById(R.id.homestay_price);
            propertyType = itemView.findViewById(R.id.property_type);
            selectedImage = itemView.findViewById(R.id.selected_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
    public void setFilteredList(List<Rental> filteredList) {
        rentalList.clear();  // Clear the existing list
        rentalList.addAll(filteredList);  // Add filtered items
        notifyDataSetChanged();
    }
}