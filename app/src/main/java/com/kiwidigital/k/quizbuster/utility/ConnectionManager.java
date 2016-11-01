package com.kiwidigital.k.quizbuster.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by brian on 28/10/2016.
 */

public class ConnectionManager {

    public static boolean isConnectedToTheInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void showConnectionAlert(Context context){
        Toast.makeText(context, "Please connect to the internet", Toast.LENGTH_LONG).show();
    }

}
