package edu.tsinghua.location.research;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import edu.tsinghua.hotmobi.HotMobiLogger;
import edu.tsinghua.hotmobi.UploadLogsTask;
import edu.tsinghua.location.research.util.Utils;

public class NetworkReceiver extends BroadcastReceiver {
    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) return;

        final int networkType = Utils.getActiveNetworkType(context.getApplicationContext());
        final boolean isWifi = networkType == ConnectivityManager.TYPE_WIFI;
        final boolean isCharging = Utils.isCharging(context.getApplicationContext());
        if (isWifi && isCharging) {
            final long currentTime = System.currentTimeMillis();
            final long lastSuccessfulTime = HotMobiLogger.getLastUploadTime(context);
            if ((currentTime - lastSuccessfulTime) > HotMobiLogger.UPLOAD_INTERVAL_MILLIS) {
                AsyncTask.execute(new UploadLogsTask(context.getApplicationContext()));
            }
        }
    }
}
