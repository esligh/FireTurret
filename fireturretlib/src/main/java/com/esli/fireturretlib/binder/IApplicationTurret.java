package com.esli.fireturretlib.binder;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * Created by lisic on 2019/4/20.
 * IApplicationTurret 提供了Turret一致的接口，它继承自ITurret
 */
public interface IApplicationTurret extends ITurret {

//    void publishService(TurretServiceWrapper wrapper);
//
//    void unPublishService(String serviceCls);

    IBinder getBinderOfService(String serviceName);
}
