package com.example.assignment3.component.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment3.R;
import com.example.assignment3.Entity.RentalRecord;
import java.util.List;
import java.util.stream.Collectors;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<RentalRecord> rentalRecords;
    private int userId;

    public StatusAdapter(List<RentalRecord> rentalRecords, int userId) {
        this.rentalRecords = rentalRecords.stream()
                .filter(record -> !record.getStatus().equalsIgnoreCase("pending"))
                .collect(Collectors.toList());
        this.userId = userId;
    }

    public void updateData(List<RentalRecord> newRecords) {
        this.rentalRecords = newRecords.stream()
                .filter(record -> !record.getStatus().equalsIgnoreCase("pending"))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_rental, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        RentalRecord record = rentalRecords.get(position);
        holder.nameTextView.setText(record.getName());
        holder.startDateTextView.setText(record.getStartDate());
        holder.endDateTextView.setText(record.getEndDate());
        holder.statusTextView.setText(record.getStatus());
        holder.totalPriceTextView.setText(String.valueOf(record.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return rentalRecords.size();
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView statusTextView;
        TextView totalPriceTextView;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.rental_name);
            startDateTextView = itemView.findViewById(R.id.rental_start_date);
            endDateTextView = itemView.findViewById(R.id.rental_end_date);
            statusTextView = itemView.findViewById(R.id.rental_status);
            totalPriceTextView = itemView.findViewById(R.id.rental_total_price);
        }
    }
}