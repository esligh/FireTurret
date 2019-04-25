package com.esli.fireturretlib.binder;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.esli.fireturretlib.event.TurretEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisic on 2019/4/20.
 */

public abstract class ApplicationTurretNative extends Binder implements IApplicationTurret{

    static public ITurret asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        ITurret in = (ITurret)obj.queryLocalInterface(descriptor);
        if (in != null) {
            return in;
        }
        return new ApplicationTurretProxy(obj);
    }

    public ApplicationTurretNative() {
        attachInterface(this, descriptor);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code){
            case PUBLISH_EVENT_TRANSACTION:
            {
                data.enforceInterface(IApplicationTurret.descriptor);
                TurretEvent event;
                if(data.readInt()!=0){
                    event = TurretEvent.CREATOR.createFromParcel(data);
                }else{
                    event = null;
                }
                publishEvent(event);
                return true;
            }
            case EVENT_RECEIVED_TRANSACTION:
            {
                data.enforceInterface(IApplicationTurret.descriptor);
                TurretEvent event;
                if(data.readInt()!=0){
                    event = TurretEvent.CREATOR.createFromParcel(data);
                }else{
                    event = null;
                }
                eventReceived(event);
                return true;
            }
            case REMOVE_TURRET_TRANSACTION:
            {
                data.enforceInterface(IApplicationTurret.descriptor);
                int pid = data.readInt();
                removeTurret(pid);
                return true;
            }
            case COPY_TURRET_TRANSACTION:
            {
                data.enforceInterface(IApplicationTurret.descriptor);
                List<ApplicationTurretWrapper> wrappers = new ArrayList<>();
                data.readTypedList(wrappers,ApplicationTurretWrapper.CREATOR);
                copyTurrets(wrappers);
                return true;
            }
            case GET_REMOTE_SERVICE_TRANSACTION:
            {
                data.enforceInterface(IApplicationTurret.descriptor);
                String serviceName = data.readString();
                if(serviceName != null){
                    IBinder binder = getRemoteService(serviceName);
                    reply.writeStrongBinder(binder);
                }
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }
}

class ApplicationTurretProxy implements ITurret {

    private final IBinder mRemote;

    public ApplicationTurretProxy(IBinder remote) {
        mRemote = remote;
    }

    public final IBinder asBinder() {
        return mRemote;
    }

    @Override
    public void publishEvent(TurretEvent event) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationTurret.descriptor);
        if(event != null){
            data.writeInt(1);
            event.writeToParcel(data,0);
        }else{
            data.writeInt(0);
        }
        mRemote.transact(PUBLISH_EVENT_TRANSACTION, data, null,
                IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @Override
    public void eventReceived(TurretEvent event) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationTurret.descriptor);
        if(event != null){
            data.writeInt(1);
            event.writeToParcel(data,0);
        }else{
            data.writeInt(0);
        }
        mRemote.transact(EVENT_RECEIVED_TRANSACTION, data, null,
                IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @Override
    public void removeTurret(int pid) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationTurret.descriptor);
        data.writeInt(pid);
        mRemote.transact(REMOVE_TURRET_TRANSACTION, data, null,
                IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @Override
    public void copyTurrets(List<ApplicationTurretWrapper> turrets) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationTurret.descriptor);
        data.writeTypedList(turrets);
        mRemote.transact(COPY_TURRET_TRANSACTION, data, null,
                IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @Override
    public IBinder getRemoteService(String serviceName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IApplicationTurret.descriptor);
        data.writeString(serviceName);
        mRemote.transact(GET_REMOTE_SERVICE_TRANSACTION, data, reply, 0);
        IBinder res = reply.readStrongBinder();
        data.recycle();
        reply.recycle();
        return res;
    }
}

