package com.esli.fireturret;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.esli.fireturretlib.FireTurret;

/**
 * Created by lisic on 2019/4/14.
 */

public class FireTurretApplication extends Application{
    private static final String TAG = "fireturret";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"FireTurretApplication-->onCreate(),pid:" + android.os.Process.myPid() + ",processName:" + getCurrentProcessName());
        FireTurret.getDefault().init(this,true);
    }

    private boolean isMainProcess() {
        String processName = getCurrentProcessName();
        if (processName != null && processName.equals(getPackageName())) {
            return true;
        }
        return false;
    }

    public String getCurrentProcessName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
