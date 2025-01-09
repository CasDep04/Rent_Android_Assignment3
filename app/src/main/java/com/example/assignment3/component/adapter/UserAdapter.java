package com.example.assignment3.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

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

        TextView userIdTextView = convertView.findViewById(R.id.textViewUserIdValue);
        TextView userEmailTextView = convertView.findViewById(R.id.textViewUserEmailValue);
        TextView userNameTextView = convertView.findViewById(R.id.textViewUserNameValue);
        TextView userRoleTextView = convertView.findViewById(R.id.textViewUserRoleValue);

        userIdTextView.setText(String.valueOf(user.getId()));
        userEmailTextView.setText(user.getEmail());
        userNameTextView.setText(user.getName());
        userRoleTextView.setText(user.getRole());

        return convertView;
    }
}