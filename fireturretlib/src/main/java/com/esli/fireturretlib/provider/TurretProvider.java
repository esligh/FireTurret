package com.esli.fireturretlib.provider;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.os.IBinder;
import android.util.ArrayMap;

import com.esli.fireturretlib.binder.ApplicationTurret;
import com.esli.fireturretlib.binder.IApplicationTurret;
import com.esli.fireturretlib.binder.TurretServiceWrapper;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;

/**
 * Created by lisic on 2019/4/22.
 * 业务逻辑依赖于该Provider向上层提供服务，它内部依赖IApplicationTurret实现IPC调用
 */
public class TurretProvider implements ITurretProvider {

    private IApplicationTurret mTurret;
    private IServiceProvider mServiceProvider;
    private IEventProvider mEventProvider;

    private volatile static TurretProvider sInstance;

    public static TurretProvider getInstance()
    {
        if (sInstance == null) {
            synchronized (TurretProvider.class){
                if(sInstance == null){
                    sInstance = new TurretProvider();
                }
            }
        }
        return sInstance;
    }

    private TurretProvider(){
        this.mTurret = ApplicationTurret.getInstance();
        this.mEventProvider = EventProvider.get();
        this.mServiceProvider = ServiceProvider.get();
    }

    @Override
    public IBinder getService(String serviceName) {
        return mServiceProvider.getService(serviceName);
    }

    @Override
    public void subscribe(LifecycleOwner owner,String event, IEventListener callBack) {
        mEventProvider.subscribe(owner,event,callBack);
    }

    @Override
    public void unSubscribe(LifecycleOwner owner) {
        mEventProvider.unSubscribe(owner);
    }

    @Override
    public void clearSubscribe(LifecycleOwner owner) {
        mEventProvider.clearSubscribe(owner);
    }

    @Override
    public void publish(TurretEvent event) {
        mEventProvider.publish(event);
    }


    @Override
    public IApplicationTurret getIInterface() {
        return mTurret;
    }

    @Override
    public void registerService(TurretServiceWrapper serviceWrapper) {
        mServiceProvider.registerService(serviceWrapper);
    }

    @Override
    public void unRegisterService(String serviceName) {
        mServiceProvider.unRegisterService(serviceName);
    }

    @Override
    public ArrayMap<String, TurretServiceWrapper> getPublishedServices() {
        return mServiceProvider.getPublishedServices();
    }

    @Override
    public IServiceProvider getServiceProvider() {
        return mServiceProvider;
    }

    @Override
    public IEventProvider getEventProvider() {
        return mEventProvider;
    }

}
