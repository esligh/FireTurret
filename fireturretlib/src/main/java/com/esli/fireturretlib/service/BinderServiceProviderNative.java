package com.esli.fireturretlib.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.esli.fireturretlib.binder.ApplicationTurretNative;
import com.esli.fireturretlib.binder.ApplicationTurretWrapper;
import com.esli.fireturretlib.binder.IApplicationTurret;

/**
 * Created by lisic on 2019/4/20.
 */

public abstract class BinderServiceProviderNative extends Binder implements IBinderServiceProvider{

    static public IBinderServiceProvider asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IBinderServiceProvider in =
                (IBinderServiceProvider)obj.queryLocalInterface(descriptor);
        if (in != null) {
            return in;
        }

        return new BinderServiceProviderProxy(obj);
    }

    public BinderServiceProviderNative() {
        attachInterface(this, descriptor);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case JOIN_TURRET_GROUP_TRANSACTION:
            {
                data.enforceInterface(IBinderServiceProvider.descriptor);
                ApplicationTurretWrapper wrapper = ApplicationTurretWrapper.CREATOR
                        .createFromParcel(data);
                joinTurretGroup(wrapper);
                return true;
            }

            case LEAVE_TURRET_GROUP_TRANSACTION:
            {
                data.enforceInterface(IBinderServiceProvider.descriptor);
                ApplicationTurretWrapper wrapper = ApplicationTurretWrapper.CREATOR
                        .createFromParcel(data);
                leaveTurretGroup(wrapper);
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }
}

class BinderServiceProviderProxy implements IBinderServiceProvider {

    private final IBinder mRemote;

    public BinderServiceProviderProxy(IBinder remote) {
        mRemote = remote;
    }

    public final IBinder asBinder() {
        return mRemote;
    }

    @Override
    public void joinTurretGroup(ApplicationTurretWrapper wrapper) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IBinderServiceProvider.descriptor);
        wrapper.writeToParcel(data,0);
        mRemote.transact(JOIN_TURRET_GROUP_TRANSACTION, data, null, 0);//同步方式
        data.recycle();
    }

    @Override
    public void leaveTurretGroup(ApplicationTurretWrapper wrapper) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IBinderServiceProvider.descriptor);
        wrapper.writeToParcel(data,0);
        mRemote.transact(LEAVE_TURRET_GROUP_TRANSACTION, data, null,0);
        data.recycle();
    }

}
