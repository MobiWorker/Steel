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
import edu.tsinghua.hotmobi.model.RequestLocationUpdateEvent;
import edu.tsinghua.location.research.Constants;

public class LogProvider extends ContentProvider implements Constants {

    public LogProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        try {
            final Context context = getContext();
            if (context == null) return null;
            final PackageManager pm = context.getPackageManager();
            final int uid = Binder.getCallingUid();
            RequestLocationUpdateEvent event = new RequestLocationUpdateEvent();
            event.markStart(context);
            event.setCallingPackages(pm.getPackagesForUid(uid));
            event.setBatteryState(BatteryState.create(context));
            event.markEnd();
            HotMobiLogger.getInstance(context).log(event);
        } catch (Throwable e) {
            Log.w(LOGTAG, e);
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
