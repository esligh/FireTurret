package com.esli.fireturretlib.provider;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.os.IBinder;
import android.util.ArrayMap;

import com.esli.fireturretlib.binder.IApplicationTurret;
import com.esli.fireturretlib.binder.TurretServiceWrapper;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;

/**
 * Created by lisic on 2019/4/21.
 */

public interface ITurretProvider extends IProvider{

    void subscribe(LifecycleOwner owner,String event, IEventListener callBack);
    void unSubscribe(LifecycleOwner owner);
    void clearSubscribe(LifecycleOwner owner);

    void publish(TurretEvent event);

    IApplicationTurret getIInterface();

    IBinder getService(String serviceName);

    void registerService(TurretServiceWrapper serviceWrapper);

    void unRegisterService(String serviceName);

    ArrayMap<String,TurretServiceWrapper> getPublishedServices();

    IServiceProvider getServiceProvider();

    IEventProvider getEventProvider();
}
