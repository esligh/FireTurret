package com.esli.fireturretlib.provider;

import android.os.IBinder;
import android.util.ArrayMap;

import com.esli.fireturretlib.binder.ApplicationTurret;
import com.esli.fireturretlib.binder.IApplicationTurret;
import com.esli.fireturretlib.binder.TurretServiceWrapper;

/**
 * Created by lisic on 2019/4/22.
 */

public class ServiceProvider implements IServiceProvider{
    private IApplicationTurret mTurret;//Turret
    private ArrayMap<String,TurretServiceWrapper> mServices;//发布的服务
    private ArrayMap<String,IBinder> mCachedService; //缓存的服务Binder

    private static volatile ServiceProvider sInstance;

    public static ServiceProvider get()
    {
        if (sInstance == null) {
            synchronized (ServiceProvider.class) {
                if (sInstance == null) {
                    sInstance = new ServiceProvider();
                }
            }
        }
        return sInstance;
    }

    private ServiceProvider()
    {
        this.mTurret = ApplicationTurret.getInstance();
        mServices = new ArrayMap<>();
        mCachedService = new ArrayMap<>();
    }

    @Override
    public IBinder getService (String serviceClass) {
        IBinder result = mCachedService.get(serviceClass);
        if (result != null) {
            return result;
        } else {
            result = mTurret.getBinderOfService(serviceClass);
            if (result != null) {
                synchronized (ServiceProvider.class) {
                    mCachedService.put(serviceClass, result);
                }
            }
        }
        return result;
    }

    @Override
    public boolean isRegister(String serviceCls) {
        return mServices.get(serviceCls) != null ;
    }

    @Override
    public void registerService(TurretServiceWrapper wrapper)
    {
        if(!isRegister(wrapper.getServiceClassName())) {
            synchronized (ServiceProvider.class) {
                mServices.put(wrapper.getServiceClassName(), wrapper);
            }
        }
    }

    @Override
    public void unRegisterService(String serviceName) {
        synchronized (ServiceProvider.class){
            mServices.remove(serviceName);
            mCachedService.remove(serviceName);
        }
    }

    @Override
    public ArrayMap<String, TurretServiceWrapper> getPublishedServices() {
        return mServices;
    }
}
