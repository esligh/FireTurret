package com.esli.fireturretlib.provider;

import android.os.IBinder;
import android.util.ArrayMap;

import com.esli.fireturretlib.binder.TurretServiceWrapper;

/**
 * Created by lisic on 2019/4/22.
 */

public interface IServiceProvider extends IProvider{

    IBinder getService(String serviceCls);
    boolean isRegister(String serviceCls);
    void registerService(TurretServiceWrapper wrapper);

    void unRegisterService(String serviceCls);

    ArrayMap<String,TurretServiceWrapper> getPublishedServices();

}
