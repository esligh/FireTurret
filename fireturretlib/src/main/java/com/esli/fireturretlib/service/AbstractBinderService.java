package com.esli.fireturretlib.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import com.esli.fireturretlib.utils.L;

public abstract class AbstractBinderService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("Binder service == > onCreate pid= "+ Process.myPid()
                +",Process name="+ getCurrentProcessName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        L.d("Binder service == > onBind pid= "+ Process.myPid());
        return createBinderService();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.d("Binder service == > onUnbind pid= "+ Process.myPid());
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        L.d("Binder service == > onDestroy pid= "+ Process.myPid());
        super.onDestroy();
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

    public abstract IBinder createBinderService();
}
