package com.esli.fireturretlib;

import android.arch.lifecycle.LifecycleOwner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Process;
import android.os.RemoteException;

import com.esli.fireturretlib.binder.ApplicationTurretWrapper;
import com.esli.fireturretlib.binder.TurretServiceWrapper;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;
import com.esli.fireturretlib.provider.ITurretProvider;
import com.esli.fireturretlib.provider.TurretProvider;
import com.esli.fireturretlib.service.BinderService;
import com.esli.fireturretlib.service.BinderServiceProvider;
import com.esli.fireturretlib.service.IBinderServiceProvider;
import com.esli.fireturretlib.utils.L;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lisic on 2019/4/14.
 */

public final class FireTurret {

    public static final String TAG = "fireturret";
    private ITurretProvider mTurretProvider;
    private volatile static FireTurret sInstance;
    private IBinderServiceProvider mBinderProvider;
    private ApplicationTurretWrapper mAppTurretWrapper;
    private AtomicBoolean bTurretReg = new AtomicBoolean(false);
    private AtomicBoolean bAutoReg = new AtomicBoolean(false);
    private AtomicBoolean bBoundService = new AtomicBoolean(false);

    private Context appContext;

    public static FireTurret getDefault()
    {
        if (sInstance == null) {
            synchronized (FireTurret.class) {
                if(sInstance == null) {
                    sInstance = new FireTurret();
                    L.setTag(TAG,BuildConfig.DEBUG);
                }
            }
        }
        return sInstance;
    }

    /**
     * initialize FireTurret
     * @param context
     * @param autoReg if true,will register a turret for application automatically.
     *               or you need to call {@registerTurret} for application manually later.
     */
    public void init(Context context,boolean autoReg)
    {
        this.appContext = context;
        bAutoReg.set(autoReg);
        if(mTurretProvider == null){
            mTurretProvider = TurretProvider.getInstance();
        }
        Intent intent = new Intent(context, BinderService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Register a Turret for application manually , Generally you should
     * call this method when need the IPC mechanism.
     */
    public void registerTurret()
    {
        if(!bTurretReg.get()) {
            regTurret();
            L.d("registerTurret:mTurret=" + mTurretProvider);
        }
    }

    public boolean isTurretRegister()
    {
        return bTurretReg.get();
    }

    public void unregisterTurret()
    {
        if(mBinderProvider != null
                && mBinderProvider.asBinder().isBinderAlive()){
            if (mAppTurretWrapper != null) {
                try {
                    mBinderProvider.leaveTurretGroup(mAppTurretWrapper);
                    if(bBoundService.get()) {
                        appContext.unbindService(mConnection);
                        bBoundService.set(false);
                    }
                    bTurretReg.set(false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinderProvider = BinderServiceProvider.asInterface(iBinder);
            bBoundService.set(true);
            if(bAutoReg.get() && !bTurretReg.get()){
                regTurret();
            }
            L.d("onServiceConnected,pid="+ Process.myPid());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            L.d("onServiceDisconnected,pid="+ Process.myPid());
            handleServiceDied();
        }
    };

    private void handleServiceDied()
    {
        if(appContext == null){
            return ;
        }
        bTurretReg.set(false);
        Intent intent = new Intent(appContext, BinderService.class);
        appContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Register the turret
     */
    private void regTurret()
    {
        if(mBinderProvider != null && mBinderProvider.asBinder().isBinderAlive()) {

            if(mAppTurretWrapper == null){
                mAppTurretWrapper = new ApplicationTurretWrapper(Process.myPid(), mTurretProvider.getIInterface());
            }
            try {
                mBinderProvider.joinTurretGroup(mAppTurretWrapper);
                bTurretReg.set(true);
            } catch (RemoteException e) {
                e.printStackTrace();
                L.d("joinTurretGroup exception=" + e.getMessage());
            }
        }
    }

    public void publish(TurretEvent event)
    {
        L.d("FireTurret:publish event");
        if(mTurretProvider != null) {
            mTurretProvider.publish(event);
        }
    }

    public void subscribe(LifecycleOwner owner,String event,IEventListener listener)
    {
        L.d("FireTurret:subscribe event");
        if(mTurretProvider != null){
            L.d("FireTurret:===========");
            mTurretProvider.subscribe(owner,event,listener);
        }
    }

    public void unSubscribe(LifecycleOwner owner)
    {
        if(mTurretProvider != null) {
            mTurretProvider.unSubscribe(owner);
        }
    }

    public void clear(LifecycleOwner owner)
    {
        if(mTurretProvider != null) {
            mTurretProvider.clearSubscribe(owner);
        }
    }

    public <T extends IBinder> void registerService(Class<? extends IInterface> serviceClass,T binder)
    {

        if(serviceClass == null || binder == null || mTurretProvider == null){
            return;
        }
        TurretServiceWrapper wrapper = new TurretServiceWrapper(Process.myPid(),
                serviceClass.getCanonicalName(),binder);
        mTurretProvider.registerService(wrapper);
    }

    public void unRegisterService(Class<? extends IInterface> serviceClass)
    {
        if(serviceClass != null && mTurretProvider != null) {
            mTurretProvider.unRegisterService(serviceClass.getCanonicalName());
        }
    }

    public IBinder getService(Class<? extends IInterface> serviceClass)
    {
        if(serviceClass != null && mTurretProvider != null) {
            return mTurretProvider.getService(serviceClass.getCanonicalName());
        }
        return null;
    }

}
