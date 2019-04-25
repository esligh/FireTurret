package com.esli.fireturretlib.service;


import android.os.IBinder;

/**
 * Created by lisic on 2019/4/14.
 */

public class BinderService extends AbstractBinderService {

    @Override
    public IBinder createBinderService() {
        return new BinderServiceProvider();
    }
}
