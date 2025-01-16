package com.example.assignment3.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignment3.CreateRentalActivity;
import com.example.assignment3.R;
import com.example.assignment3.RespondActivity;

public class HostHome extends Fragment {
    private static final String TAG = "HostHome";
    private Button navigateButton1;
    private Button navigateButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the user ID from the arguments
        Bundle bundle = getArguments();
        final int userId;
        if (bundle != null) {
            userId = bundle.getInt("userId", -1);
        } else {
            userId = -1;
        }
        Log.d(TAG, "onViewCreated: Retrieved user ID: " + userId);

        navigateButton1 = view.findViewById(R.id.create_btn);
        navigateButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateRentalActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        navigateButton2 = view.findViewById(R.id.respond_btn);
        navigateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RespondActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}