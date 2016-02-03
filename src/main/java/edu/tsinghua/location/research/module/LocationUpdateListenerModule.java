package edu.tsinghua.location.research.module;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.location.LocationManager;
import android.util.SparseBooleanArray;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.tsinghua.location.research.Constants;
import edu.tsinghua.location.research.provider.LogProvider;

public class LocationUpdateListenerModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) return;
        for (Method method : LocationManager.class.getDeclaredMethods()) {
            // Only hook public methods
            if ((method.getModifiers() & Modifier.PUBLIC) == 0) break;
            if ("requestLocationUpdates".equals(method.getName())) {
                XposedBridge.hookMethod(method, new RequestLocationUpdatesMethodHook(loadPackageParam.packageName));
            }
        }
        final PostNotificationMethodHook hook = new PostNotificationMethodHook(loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod(NotificationManager.class, "notify", String.class,
                int.class, Notification.class, hook);
        XposedHelpers.findAndHookMethod(NotificationManager.class, "cancel", String.class,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        hook.cancelled((String) param.args[0], (Integer) param.args[1]);
                    }
                });
    }

    private static class PostNotificationMethodHook extends XC_MethodHook {

        private final SparseBooleanArray showingNotifications = new SparseBooleanArray();
        private final String hookingApplication;
        private int notificationHashCode;

        PostNotificationMethodHook(String hookingApplication) {
            this.hookingApplication = hookingApplication;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            final String tag = (String) param.args[0];
            final int id = (Integer) param.args[1];
            final Notification notification = (Notification) param.args[2];
            if (notification == null) return;
            // Use this to avoid duplicate method call
            final int hashCode = System.identityHashCode(notification);
            final int idHash = calculateHash(tag, id);
            if (notificationHashCode == hashCode || showingNotifications.get(idHash)) return;
            notificationHashCode = hashCode;
            synchronized (showingNotifications) {
                showingNotifications.append(idHash, true);
            }
            if (!hookingApplication.equals(AndroidAppHelper.currentPackageName())) {
                return;
            }
            try {
                final Application app = AndroidAppHelper.currentApplication();
                app.getContentResolver().call(Constants.CONTENT_URI, LogProvider.NOTIFICATION_POSTED,
                        hookingApplication, null);
            } catch (Throwable t) {
                // Swallow all exceptions
                XposedBridge.log(t);
            }
        }

        public void cancelled(String tag, int id) {
            if (!hookingApplication.equals(AndroidAppHelper.currentPackageName())) {
                return;
            }
            synchronized (showingNotifications) {
                showingNotifications.delete(calculateHash(tag, id));
            }
        }
    }

    static int calculateHash(String tag, int id) {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }

    private static class RequestLocationUpdatesMethodHook extends XC_MethodHook {
        private final String hookingApplication;

        public RequestLocationUpdatesMethodHook(String hookingApplication) {
            this.hookingApplication = hookingApplication;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (!hookingApplication.equals(AndroidAppHelper.currentPackageName())) {
                return;
            }
            try {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                boolean calledRequestLocationUpdates = false;
                for (int i = 2; i < stackTrace.length; i++) {
                    if ("requestLocationUpdates".equals(stackTrace[i].getMethodName()) &&
                            LocationManager.class.getName().equals(stackTrace[i].getClassName())) {
                        if (calledRequestLocationUpdates) {
                            return;
                        }
                        calledRequestLocationUpdates = true;
                    }
                }
                final Application app = AndroidAppHelper.currentApplication();
                app.getContentResolver().call(Constants.CONTENT_URI, LogProvider.REQUESTED_LOCATION_UPDATE,
                        hookingApplication, null);
            } catch (Throwable t) {
                // Swallow all exceptions
                XposedBridge.log(t);
            }
        }
    }
}
