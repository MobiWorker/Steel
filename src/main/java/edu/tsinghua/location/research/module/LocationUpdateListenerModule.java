package edu.tsinghua.location.research.module;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.ContentResolver;
import android.location.LocationManager;
import android.net.Uri;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LocationUpdateListenerModule implements IXposedHookLoadPackage {

    private static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.APPLICATION_ID).build();

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) return;
        for (Method method : LocationManager.class.getMethods()) {
            if ("requestLocationUpdates".equals(method.getName())) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            final Application app = AndroidAppHelper.currentApplication();
                            app.getContentResolver().call(CONTENT_URI, "requested_location_update", null, null);
                        } catch (Throwable t) {
                            // Swallow all exceptions
                            XposedBridge.log(t);
                        }
                    }
                });
            }
        }
    }
}
