package com.esli.fireturretlib.provider;

import android.arch.lifecycle.LifecycleOwner;
import android.os.RemoteException;
import android.util.ArrayMap;

import com.esli.fireturretlib.binder.ApplicationTurret;
import com.esli.fireturretlib.binder.IApplicationTurret;
import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;
import com.esli.fireturretlib.lifecycle.EventLifeCycleObserver;
import com.esli.fireturretlib.utils.L;

/**
 * Created by lisic on 2019/4/22.
 */
public class EventProvider implements IEventProvider{

    private IApplicationTurret mTurret;
    private ArrayMap<LifecycleOwner,ArrayMap<String,IEventListener>> mSubscriber;

    private static volatile EventProvider sInstance;

    public static EventProvider get()
    {
        if(sInstance == null){
            synchronized (EventProvider.class){
                if(sInstance == null){
                    sInstance = new EventProvider();
                }
            }
        }
        return sInstance;
    }

    private EventProvider()
    {
        this.mTurret = ApplicationTurret.getInstance();
        this.mSubscriber = new ArrayMap<>();
    }

    public void subscribe(LifecycleOwner owner,String event ,IEventListener callBack)
    {
        if(owner == null || callBack == null){
            return ;
        }
        L.d("EventProvider:subscribe=>");
        owner.getLifecycle().addObserver(new EventLifeCycleObserver(event,callBack));
    }

    @Override
    public void unSubscribe(LifecycleOwner owner) {
        if(owner != null){
            synchronized (EventProvider.class) {
                mSubscriber.remove(owner);
            }
        }
    }

    @Override
    public void addSubscriber(LifecycleOwner owner, String event,IEventListener listener) {

        if(owner == null || event == null || listener == null){
            return ;
        }
        synchronized (EventProvider.class) {
            ArrayMap<String,IEventListener> eventMap = mSubscriber.get(owner);
            if(eventMap == null){
                eventMap = new ArrayMap<>();
            }
            eventMap.put(event,listener);
            mSubscriber.put(owner, eventMap);
            L.d("EventProvider:addSubscriber=>owner="+owner);
            L.d("EventProvider:addSubscriber=>size="+mSubscriber.size());
        }
    }

    @Override
    public boolean hasSubscriber(LifecycleOwner owner) {
        return mSubscriber.containsKey(owner);
    }

    @Override
    public void removeSubscriber(LifecycleOwner owner) {
        if(owner != null){
            synchronized (EventProvider.class){
                mSubscriber.remove(owner);
            }
        }
    }

    @Override
    public void publish(TurretEvent event) {
        try {
            mTurret.publishEvent(event);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSubscribe(LifecycleOwner owner) {
        synchronized (EventProvider.class) {
            mSubscriber.remove(owner);
        }
    }

    @Override
    public void publishEvent(TurretEvent event) {
        if(event == null){
            return ;
        }
        L.d("EventProvider:publishEvent=>size="+mSubscriber.size());
        for(int i=0;i<mSubscriber.size();i++){
            ArrayMap<String,IEventListener> map = mSubscriber.valueAt(i);
            if(map != null){
               IEventListener listener = map.get(event.getWhat());
               if(listener != null){
                   listener.onEvent(event);
               }
            }
        }
    }

}
