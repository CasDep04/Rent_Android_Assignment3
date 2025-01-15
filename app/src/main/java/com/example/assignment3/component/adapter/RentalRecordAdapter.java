package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.example.assignment3.Entity.RentalRecord;
import com.example.assignment3.Entity.User;
import com.example.assignment3.R;
import com.example.assignment3.component.FirebaseAction;

import java.util.List;

public class RentalRecordAdapter extends ArrayAdapter<RentalRecord> {

    private Context context;
    private List<RentalRecord> records;

    public RentalRecordAdapter(@NonNull Context context, @NonNull List<RentalRecord> records) {
        super(context, 0, records);
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.record_list_component, parent, false);
        }

        RentalRecord record = records.get(position);

        TextView recordIdTextView = convertView.findViewById(R.id.textViewRecordIdValue);
        TextView guestNameTextView = convertView.findViewById(R.id.textViewGuestNameValue);
        TextView hostNameTextView = convertView.findViewById(R.id.textViewHostNameValue);
        TextView statusTextView = convertView.findViewById(R.id.textViewStatusValue);

        recordIdTextView.setText(String.valueOf(record.getId()));
        statusTextView.setText(record.getStatus());

        // Set the status text color based on the status
        switch (record.getStatus().toLowerCase()) {
            case "accepted":
                statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
                break;
            case "pending":
                statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
                break;
            case "rejected":
                statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
                break;
            default:
                statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                break;
        }

        FirebaseAction.findUserById(record.getGuestId()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User guest = task.getResult();
                guestNameTextView.setText(guest.getName());
            } else {
                guestNameTextView.setText("Unknown Guest");
            }
        });

        FirebaseAction.findUserById(record.getHostId()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User host = task.getResult();
                hostNameTextView.setText(host.getName());
            } else {
                hostNameTextView.setText("Unknown Host");
            }
        });

        return convertView;
    }
}