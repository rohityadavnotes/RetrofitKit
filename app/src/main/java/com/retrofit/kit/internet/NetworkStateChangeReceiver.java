package com.retrofit.kit.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.retrofit.kit.utilities.LogcatUtils;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkStateChangeReceiver.class.getSimpleName();
    public static NetworkStateChangeListener networkStateChangeListener;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final String action = intent.getAction();

        if (action != null) {
            if (networkStateChangeListener != null) {

                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (NetworkConnectivityUtil.isConnectedAll(context))
                    {
                        LogcatUtils.errorMessage(TAG,"ConnectivityManager.CONNECTIVITY_ACTION - 1");
                        networkStateChangeListener.networkAvailable();
                    }
                    else
                    {
                        LogcatUtils.errorMessage(TAG,"ConnectivityManager.CONNECTIVITY_ACTION - 2");
                        networkStateChangeListener.networkUnavailable();
                    }
                }

                if (!intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false))
                {
                    LogcatUtils.errorMessage(TAG,"RegisterAndUnregisterNetworkReceiver.NETWORK_AVAILABILITY_ACTION - 1");
                    networkStateChangeListener.networkAvailable();
                }
                else
                {
                    LogcatUtils.errorMessage(TAG,"RegisterAndUnregisterNetworkReceiver.NETWORK_AVAILABILITY_ACTION - 2");
                    networkStateChangeListener.networkUnavailable();
                }
            }
        }
    }

    public static void setNetworkStateChangeListener(NetworkStateChangeListener listener) {
        NetworkStateChangeReceiver.networkStateChangeListener = listener;
    }
}