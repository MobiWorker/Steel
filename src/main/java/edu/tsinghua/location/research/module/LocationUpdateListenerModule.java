package edu.tsinghua.location.research.module;

import android.location.LocationManager;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LocationUpdateListenerModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final String packageName = loadPackageParam.packageName;
        for (Method method : LocationManager.class.getMethods()) {
            if ("requestLocationUpdates".equals(method.getName())) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(packageName + " requested location updates");
                    }
                });
            }
        }
    }
}
