package com.literalcat.Developer4All;

import android.util.Log;
import android.content.Context;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "Developer4All";

    private static final Set<String> TARGET_PACKAGES = new HashSet<>(Arrays.asList(
            "com.oculus.systemux"
    ));

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        if (!TARGET_PACKAGES.contains(lpparam.packageName)) {
            Log.e(TAG,"no injection :(");
            return;
        }

        Log.i(TAG, "Loaded into " + lpparam.packageName + " (pid=" + android.os.Process.myPid() + ")");
        try {
            XposedHelpers.findAndHookMethod(
                    "com.oculus.panelapp.settings.sections.util.SettingsSectionUtil",
                    lpparam.classLoader,
                    "shouldShowSettingsDeveloperSection", Context.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                param.setResult(true);
                    }}
            );
        } catch (Throwable t) {
            Log.e(TAG, "Failure: ", t);
        }
    }
}