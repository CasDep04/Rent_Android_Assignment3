package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.assignment3.R;
import com.example.assignment3.Entity.RentalRecord;
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
        guestNameTextView.setText(String.valueOf(record.getGuestId()));
        hostNameTextView.setText(String.valueOf(record.getHostId()));
        statusTextView.setText(String.valueOf(record.getStatus()));

        return convertView;
    }
}