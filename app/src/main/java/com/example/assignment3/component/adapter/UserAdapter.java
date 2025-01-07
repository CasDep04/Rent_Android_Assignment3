package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.assignment3.Entity.Guest;
import com.example.assignment3.Entity.Host;
import com.example.assignment3.Entity.User;
import com.example.assignment3.R;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;

    public UserAdapter(@NonNull Context context, @NonNull List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_component, parent, false);
        }

        User user = users.get(position);

        TextView userNameTextView = convertView.findViewById(R.id.textViewUserNameValue);
        TextView userEmailTextView = convertView.findViewById(R.id.textViewUserEmailValue);
        TextView userRoleTextView = convertView.findViewById(R.id.textViewUserRoleValue);
        TextView specificInfoTextView = convertView.findViewById(R.id.textViewSpecificInfoValue);

        userNameTextView.setText(user.getName());
        userEmailTextView.setText(user.getEmail());
        userRoleTextView.setText(user.getRole());

        if (user instanceof Guest) {
            Guest guest = (Guest) user;
            specificInfoTextView.setText(guest.getPreferences());
        } else if (user instanceof Host) {
            Host host = (Host) user;
            specificInfoTextView.setText(String.valueOf(host.getTotalProperties()));
        }

        return convertView;
    }
}