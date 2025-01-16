package com.example.assignment3.BroadcastReceiver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetworkMonitorManager {

    private final ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private boolean isConnected;

    // The callback interface:
    private NetworkListener listener;

    // Provide a setter so the Activity or Fragment can implement this interface:
    public void setNetworkListener(NetworkListener listener) {
        this.listener = listener;
    }

    public NetworkMonitorManager(Context context) {
        connectivityManager = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void registerNetworkCallback() {
        if (connectivityManager == null) return;

        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        // Define the callback
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                isConnected = true;
                // Forward the event to the interface
                if (listener != null) {
                    listener.onNetworkAvailable();
                }
            }

            @Override
            public void onLost(Network network) {
                isConnected = false;
                // Forward the event to the interface
                if (listener != null) {
                    listener.onNetworkLost();
                }
            }
        };

        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    public void unregisterNetworkCallback() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
