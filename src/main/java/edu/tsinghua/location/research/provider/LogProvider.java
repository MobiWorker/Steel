package edu.tsinghua.location.research.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import edu.tsinghua.hotmobi.HotMobiLogger;
import edu.tsinghua.hotmobi.model.BatteryState;
import edu.tsinghua.hotmobi.model.PostedNotificationEvent;
import edu.tsinghua.hotmobi.model.RequestLocationUpdateEvent;
import edu.tsinghua.location.research.Constants;

public class LogProvider extends ContentProvider implements Constants {

    public static final String NOTIFICATION_POSTED = "notification_posted";
    public static final String REQUESTED_LOCATION_UPDATE = "requested_location_update";

    public LogProvider() {
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        try {
            final Context context = getContext();
            if (context == null) return null;
            final PackageManager pm = context.getPackageManager();
            final int uid = Binder.getCallingUid();
            final String[] packagesForUid = pm.getPackagesForUid(uid);
            switch (method) {
                case NOTIFICATION_POSTED: {
                    final PostedNotificationEvent event = new PostedNotificationEvent();
                    event.markStart(context);
                    if (contains(packagesForUid, arg)) {
                        event.setCallingPackages(new String[]{arg});
                    } else {
                        event.setCallingPackages(packagesForUid);
                    }
                    event.setBatteryState(BatteryState.create(context));
                    event.markEnd();
                    HotMobiLogger.getInstance(context).log(event);
                    break;
                }
                case REQUESTED_LOCATION_UPDATE: {
                    final RequestLocationUpdateEvent event = new RequestLocationUpdateEvent();
                    event.markStart(context);
                    if (contains(packagesForUid, arg)) {
                        event.setCallingPackages(new String[]{arg});
                    } else {
                        event.setCallingPackages(packagesForUid);
                    }
                    event.setBatteryState(BatteryState.create(context));
                    event.markEnd();
                    HotMobiLogger.getInstance(context).log(event);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException(method);
                }
            }
        } catch (Throwable e) {
            Log.w(LOGTAG, e);
        }
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static boolean contains(String[] array, String item) {
        for (String s : array) {
            if (item != null && item.equals(s)) {
                return true;
            } else if (s == null) {
                return true;
            }
        }
        return false;
    }
}
