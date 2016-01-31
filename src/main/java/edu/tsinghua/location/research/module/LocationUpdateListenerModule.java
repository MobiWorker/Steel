package edu.tsinghua.location.research.module;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.NotificationManager;
import android.location.LocationManager;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.tsinghua.location.research.Constants;
import edu.tsinghua.location.research.provider.LogProvider;

public class LocationUpdateListenerModule implements IXposedHookLoadPackage {

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
                            app.getContentResolver().call(Constants.CONTENT_URI, LogProvider.REQUESTED_LOCATION_UPDATE, null, null);
                        } catch (Throwable t) {
                            // Swallow all exceptions
                            XposedBridge.log(t);
                        }
                    }
                });
            }
        }
        for (Method method : NotificationManager.class.getMethods()) {
            if (method.getDeclaringClass() != NotificationManager.class) break;
            if ("notify".equals(method.getName())) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            final Application app = AndroidAppHelper.currentApplication();
                            app.getContentResolver().call(Constants.CONTENT_URI, LogProvider.NOTIFICATION_POSTED, null, null);
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
