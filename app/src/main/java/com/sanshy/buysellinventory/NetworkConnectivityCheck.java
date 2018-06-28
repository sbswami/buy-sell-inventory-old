package com.sanshy.buysellinventory;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

public class NetworkConnectivityCheck {

    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void connectionCheck(final Context context)
    {

        if (!isDeviceOnline(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.connection_problem_)
                    .setMessage(context.getString(R.string.connect_request_))
                    .setPositiveButton(context.getString(R.string.ok_), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            connectionCheck(context);
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton(context.getString(R.string.close_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            builder.create().show();
            return;
        }
    }
}
