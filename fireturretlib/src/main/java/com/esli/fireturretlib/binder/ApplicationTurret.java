package com.esli.fireturretlib.binder;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.SparseArray;

import com.esli.fireturretlib.FireTurret;
import com.esli.fireturretlib.event.TurretEvent;
import com.esli.fireturretlib.provider.TurretProvider;
import com.esli.fireturretlib.service.BinderServiceProvider;
import com.esli.fireturretlib.utils.L;

import java.util.List;

/**
 * * Created by lisic on 2019/4/20.
 * ApplicationTurret为一个Application端的Binder Server,负责App间端对端的IPC通信
 */
public class ApplicationTurret extends ApplicationTurretNative {

    private static volatile ApplicationTurret sTurret;
    private SparseArray<ApplicationTurretWrapper> mTurretWrappers;

    public static ApplicationTurret getInstance()
    {
        if(sTurret == null){
            synchronized (ApplicationTurret.class){
                if(sTurret == null){
                    sTurret = new ApplicationTurret();
                }
            }
        }
        return sTurret;
    }

    private ApplicationTurret()
    {
        mTurretWrappers = new SparseArray<>();
    }

    @Override
    public void publishEvent(TurretEvent event) throws RemoteException {
        L.d(FireTurret.TAG,"publishEvent:mTurrets size="+mTurretWrappers.size()+",pid="+ Process.myPid());
        for(int i =0;i<mTurretWrappers.size();i++){
            ApplicationTurretWrapper wrapper = mTurretWrappers.valueAt(i);
            ITurret turret = wrapper.getTurret();
            turret.eventReceived(event);
        }
    }

    @Override
    public void eventReceived(TurretEvent event) throws RemoteException {
        L.d(FireTurret.TAG,"eventReceived:event="+event+
                ",data="+event+",pid="+ Process.myPid()+",thread="+Thread.currentThread().getName());
        TurretProvider.getInstance().getEventProvider().publishEvent(event);
        //
// IEventCallBack callBack = TurretProvider.getInstance().getEventCallBacks().get(event);
//        if(callBack != null){
//            callBack.onEvent(data);
//        }
    }

    @Override
    public void removeTurret(int pid) throws RemoteException {
        synchronized (mTurretWrappers) {
            mTurretWrappers.remove(pid);
        }
        L.d(FireTurret.TAG,"turretRemove:pid="+ Process.myPid());
    }

    @Override
    public void copyTurrets(List<ApplicationTurretWrapper> turrets) throws RemoteException {
        if(turrets != null && turrets.size() > 0) {
            synchronized (mTurretWrappers) {
                mTurretWrappers.clear();
                for (ApplicationTurretWrapper wrapper : turrets) {
                    ITurret turret = wrapper.getTurret();
                    L.d(FireTurret.TAG,"syncTurrets:turrets size="+turrets.size()
                            +",pid="+ Process.myPid()+",turret="+turret);

                    if (turret != null) {
                        TurretDeathRecipient tdr = new TurretDeathRecipient(wrapper);
                        turret.asBinder().linkToDeath(tdr, 0);
                        mTurretWrappers.put(wrapper.getPid(),wrapper);
                    }
                }
            }
        }
    }

    @Override
    public IBinder getRemoteService(String serviceName) throws RemoteException {
        L.d("getRemoteService:pid="+ Process.myPid()+",thread="+Thread.currentThread().getName());
        TurretServiceWrapper wrapper = TurretProvider.getInstance().getPublishedServices().get(serviceName);
        if (wrapper != null) {
            return wrapper.getBinder();
        }
        return null;
    }

    @Override
    public IBinder getBinderOfService(String serviceName) {
        for (int i = 0; i < mTurretWrappers.size(); i++) {
            ApplicationTurretWrapper wrapper = mTurretWrappers.valueAt(i);
            try {
                IBinder binder = wrapper.getTurret().getRemoteService(serviceName);
                if(binder != null){
                    return binder;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    class TurretDeathRecipient implements IBinder.DeathRecipient {

        private ApplicationTurretWrapper mWrapper;

        public TurretDeathRecipient (ApplicationTurretWrapper turret) {
            this.mWrapper = turret;
        }

        @Override
        public void binderDied() {
            synchronized (BinderServiceProvider.class) {
                synchronized (mTurretWrappers) {
                    mTurretWrappers.remove(mWrapper.getPid());
                }
                L.d(FireTurret.TAG,"ApplicationTurret:binderDied,mTurret="+
                        mWrapper.getTurret()+",pid="+ Process.myPid());
            }
        }
    }
}
