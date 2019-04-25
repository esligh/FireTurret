package com.esli.fireturretlib.binder;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.esli.fireturretlib.event.TurretEvent;

import java.util.List;

/**
 * Created by lisic on 2019/4/22.
 * Turret的IPC接口，一个Turret为一个独立的Binder，每个进程应该有且仅有一个
 */
public interface ITurret extends IInterface{

    String descriptor = "com.esli.fireturretlib.binder.IApplicationTurret";

    /**
     * @param event
     * @throws RemoteException
     */
    void publishEvent(TurretEvent event) throws RemoteException;

    /**
     * @param event
     * @throws RemoteException
     */
    void eventReceived(TurretEvent event) throws RemoteException;

    /**
     * @param pid
     * @throws RemoteException
     */
    void removeTurret(int pid) throws RemoteException;

    /**
     * @param turrets
     * @throws RemoteException
     */
    void copyTurrets(List<ApplicationTurretWrapper> turrets) throws RemoteException;

    /**
     * @param serviceClass
     * @return
     * @throws RemoteException
     */
    IBinder getRemoteService(String serviceClass) throws RemoteException;


//    /**
//     * @param serviceWrapper
//     * @throws RemoteException
//     */
//    void addService(TurretServiceWrapper serviceWrapper) throws RemoteException;
//
//    /**
//     * @param serviceCls
//     * @throws RemoteException
//     */
//    void removeService(String serviceCls)throws RemoteException;


    int PUBLISH_EVENT_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;

    int EVENT_RECEIVED_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 1;

    int ADD_TURRET_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 2;

    int REMOVE_TURRET_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 3;

    int COPY_TURRET_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 4;

    int GET_REMOTE_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 5;

//    int ADD_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 6;
//
//    int REMOVE_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 7;
}
