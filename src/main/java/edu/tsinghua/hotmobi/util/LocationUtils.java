package edu.tsinghua.hotmobi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;

import edu.tsinghua.hotmobi.HotMobiConstants;
import edu.tsinghua.hotmobi.HotMobiLogger;
import edu.tsinghua.hotmobi.model.LatLng;

/**
 * Created by mariotaku on 16/1/29.
 */
public class LocationUtils implements HotMobiConstants {
    public static LatLng getCachedLatLng(Context context) {
        final Location location = getCachedLocation(context);
        if (location == null) {
            return getFallbackCachedLocation(context);
        }
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        try {
            prefs.edit().putString(HotMobiLogger.FALLBACK_CACHED_LOCATION, LoganSquare.serialize(latLng)).apply();
        } catch (IOException e) {
            // Ignore
        }
        return latLng;
    }

    private static LatLng getFallbackCachedLocation(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        try {
            return LoganSquare.parse(prefs.getString(HotMobiLogger.FALLBACK_CACHED_LOCATION, null), LatLng.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static Location getCachedLocation(Context context) {
        Location location = null;
        final LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) return null;
        try {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ignore) {

        }
        if (location != null) return location;
        try {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException ignore) {

        }
        return location;
    }

}
