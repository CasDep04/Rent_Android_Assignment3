package com.example.assignment3.component.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment3.R;
import com.example.assignment3.Entity.RentalRecord;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class RespondAdapter extends RecyclerView.Adapter<RespondAdapter.RespondViewHolder> {
    private static final String TAG = "RespondAdapter";
    private final Context context;
    private final List<RentalRecord> rentalRecordList;
    private final FirebaseFirestore db;
    private final int userId;

    public RespondAdapter(Context context, List<RentalRecord> rentalRecordList, int userId) {
        this.context = context;
        this.rentalRecordList = rentalRecordList;
        this.db = FirebaseFirestore.getInstance();
        this.userId = userId;
    }

    @NonNull
    @Override
    public RespondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.respond_rental, parent, false);
        return new RespondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespondViewHolder holder, int position) {
        RentalRecord rentalRecord = rentalRecordList.get(position);
        Log.d(TAG, "onBindViewHolder: Binding record " + rentalRecord.getId());
        holder.nameTextView.setText(rentalRecord.getName());
        holder.startDateTextView.setText("Start Date: " + rentalRecord.getStartDate());
        holder.endDateTextView.setText("End Date: " + rentalRecord.getEndDate());
        holder.statusTextView.setText("Status: " + rentalRecord.getStatus());
        holder.totalPriceTextView.setText("Total Price: $" + rentalRecord.getTotalPrice());

        holder.approveButton.setOnClickListener(v -> updateRentalStatus(rentalRecord, "approved"));
        holder.declineButton.setOnClickListener(v -> updateRentalStatus(rentalRecord, "declined"));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + rentalRecordList.size() + " items");
        return rentalRecordList.size();
    }

    private void updateRentalStatus(RentalRecord rentalRecord, String status) {
        db.collection("rentalRecords").document(rentalRecord.getId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    rentalRecord.setStatus(status);
                    if ("approved".equals(status)) {
                        chargeTotalPrice(rentalRecord);
                    } else {
                        refreshList();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateRentalStatus: Error updating status", e);
                    Toast.makeText(context, "Error updating status", Toast.LENGTH_SHORT).show();
                });
    }

    private void chargeTotalPrice(RentalRecord rentalRecord) {
        double totalPrice = rentalRecord.getTotalPrice();
        int guestId = rentalRecord.getGuestId();
        int hostId = rentalRecord.getHostId();

        db.runTransaction(transaction -> {
                    // Get guest balance
                    DocumentSnapshot guestSnapshot = transaction.get(db.collection("users").document(String.valueOf(guestId)));
                    double guestBalance = guestSnapshot.getDouble("balance");

                    // Get host balance
                    DocumentSnapshot hostSnapshot = transaction.get(db.collection("users").document(String.valueOf(hostId)));
                    double hostBalance = hostSnapshot.getDouble("balance");

                    // Update balances
                    transaction.update(db.collection("users").document(String.valueOf(guestId)), "balance", guestBalance - totalPrice);
                    transaction.update(db.collection("users").document(String.valueOf(hostId)), "balance", hostBalance + totalPrice);

                    return null;
                }).addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "chargeTotalPrice: Transaction successful");
                    refreshList();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "chargeTotalPrice: Error processing transaction", e);
                    Toast.makeText(context, "Error processing transaction", Toast.LENGTH_SHORT).show();
                });
    }

    private void refreshList() {
        rentalRecordList.removeIf(record -> !"pending".equals(record.getStatus()));
        notifyDataSetChanged();
    }

    static class RespondViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, startDateTextView, endDateTextView, statusTextView, totalPriceTextView;
        Button approveButton, declineButton;

        public RespondViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.rental_name);
            startDateTextView = itemView.findViewById(R.id.rental_start_date);
            endDateTextView = itemView.findViewById(R.id.rental_end_date);
            statusTextView = itemView.findViewById(R.id.rental_status);
            totalPriceTextView = itemView.findViewById(R.id.rental_total_price);
            approveButton = itemView.findViewById(R.id.approve_button);
            declineButton = itemView.findViewById(R.id.decline_button);
        }
    }
}