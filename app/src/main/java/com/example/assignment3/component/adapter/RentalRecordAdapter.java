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
import com.example.assignment3.R;

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

        TextView guestIdTextView = convertView.findViewById(R.id.textViewGuestIdValue);
        TextView startDateTextView = convertView.findViewById(R.id.textViewStartDateValue);
        TextView statusTextView = convertView.findViewById(R.id.textViewStatusValue);

        guestIdTextView.setText(String.valueOf(record.getGuestId()));
        startDateTextView.setText(record.getStartDate());
        statusTextView.setText(record.getStatus());

        // Set the status text color based on the status
        switch (record.getStatus().toLowerCase()) {
            case "completed":
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

        return convertView;
    }
}