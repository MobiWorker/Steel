package edu.tsinghua.location.research;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.net.ConnectivityManagerCompat;

import edu.tsinghua.hotmobi.HotMobiLogger;
import edu.tsinghua.hotmobi.UploadLogsTask;
import edu.tsinghua.location.research.util.Utils;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) return;
        final Context appContext = context.getApplicationContext();
        final ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final boolean isMetered = ConnectivityManagerCompat.isActiveNetworkMetered(cm);
        final boolean isCharging = Utils.isCharging(appContext);
        if (!isMetered && isCharging) {
            final long currentTime = System.currentTimeMillis();
            final long lastSuccessfulTime = HotMobiLogger.getLastUploadTime(context);
            if ((currentTime - lastSuccessfulTime) > HotMobiLogger.UPLOAD_INTERVAL_MILLIS) {
                AsyncTask.execute(new UploadLogsTask(appContext));
            }
        }
    }
}
