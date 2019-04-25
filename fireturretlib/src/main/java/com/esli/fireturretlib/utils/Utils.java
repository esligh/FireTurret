package com.esli.fireturretlib.utils;

import android.os.Looper;

/**
 * Created by lisic on 2019/4/25.
 */

public final class Utils {
    private Utils(){}

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
