package com.esli.fireturretlib.service;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.esli.fireturretlib.binder.ApplicationTurretWrapper;
import com.esli.fireturretlib.binder.IApplicationTurret;

/**
 * Created by lisic on 2019/4/14.
 */
public interface IBinderServiceProvider extends IInterface
{
    String descriptor = "com.esli.fireturretlib.service.IBinderServiceProvider";
    void joinTurretGroup(ApplicationTurretWrapper wrapper) throws RemoteException;
    void leaveTurretGroup(ApplicationTurretWrapper wrapper) throws RemoteException;

    int JOIN_TURRET_GROUP_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;
    int LEAVE_TURRET_GROUP_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+2;
}
