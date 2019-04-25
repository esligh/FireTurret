package com.esli.fireturretlib.service;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.SparseArray;

import com.esli.fireturretlib.binder.ApplicationTurretWrapper;
import com.esli.fireturretlib.binder.IApplicationTurret;
import com.esli.fireturretlib.binder.TurretServiceWrapper;
import com.esli.fireturretlib.utils.L;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisic on 2019/4/20.
 */
public class BinderServiceProvider extends BinderServiceProviderNative {

    private SparseArray<ApplicationTurretWrapper> mCachedTurrets;
    private List<ApplicationTurretWrapper> mCachedIBinder;
    private ArrayMap<String,TurretServiceWrapper> mCachedServices;

    public BinderServiceProvider() {
        mCachedTurrets = new SparseArray<>();
        mCachedIBinder = new ArrayList<>();
        mCachedServices = new ArrayMap<>();
    }

    @Override
    public void joinTurretGroup (ApplicationTurretWrapper wrapper) throws RemoteException {
        TurretDeathRecipient sdr = new TurretDeathRecipient(wrapper);
        wrapper.getTurret().asBinder().linkToDeath(sdr,0);
        synchronized (mCachedTurrets) {
            mCachedTurrets.put(wrapper.getPid(),wrapper);
        }
        convertToIBinders(mCachedTurrets);
        L.d("joinTurretGroup:turret="+wrapper.getTurret()+",mCachedTurret size="+mCachedTurrets.size()+
                ",mCachedIBinder="+mCachedIBinder.size());
        for (int i=0; i < mCachedTurrets.size();i++) {
            ApplicationTurretWrapper w = mCachedTurrets.valueAt(i);
            w.getTurret().copyTurrets(mCachedIBinder);
        }
    }

    @Override
    public void leaveTurretGroup(ApplicationTurretWrapper wrapper) throws RemoteException {
        for (int i=0; i < mCachedTurrets.size(); i++) {
            ApplicationTurretWrapper w = mCachedTurrets.valueAt(i);
            L.d("leaveTurretGroup:===turret=="+w.getTurret()+",remove pid="+w.getPid());
            w.getTurret().removeTurret(wrapper.getPid());
        }

        synchronized (mCachedTurrets) {
            mCachedTurrets.remove(wrapper.getPid());
        }

        convertToIBinders(mCachedTurrets);
        L.d("leaveTurretGroup:turret="+wrapper.getTurret()+",mCachedTurret size="+mCachedTurrets.size()+
                ",mCachedIBinder="+mCachedIBinder.size());
    }

    private void convertToIBinders(SparseArray<ApplicationTurretWrapper> turrets)
    {
        if (turrets != null) {
            synchronized (mCachedIBinder) {
                mCachedIBinder.clear();
                for(int i=0;i<mCachedTurrets.size();i++){
                    mCachedIBinder.add(mCachedTurrets.valueAt(i));
                }
            }
        }
    }

    class TurretDeathRecipient implements IBinder.DeathRecipient {
        private ApplicationTurretWrapper mWrapper;

        public TurretDeathRecipient(ApplicationTurretWrapper wrapper) {
            this.mWrapper = wrapper;
        }

        @Override
        public void binderDied() {
            synchronized (mCachedTurrets) {
                mCachedTurrets.remove(mWrapper.getPid());
            }
            convertToIBinders(mCachedTurrets);
            L.d("BinderServiceProvider: binderDied,mTurret="+mWrapper);
        }
    }
}
