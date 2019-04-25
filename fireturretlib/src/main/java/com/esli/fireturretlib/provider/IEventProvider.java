package com.esli.fireturretlib.provider;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.event.TurretEvent;

/**
 * Created by lisic on 2019/4/22.
 */

public interface IEventProvider extends IProvider {

    void publish(TurretEvent event);

    void clearSubscribe(LifecycleOwner owner);

    void publishEvent(TurretEvent event);

    void subscribe(LifecycleOwner owner, String event,IEventListener callBack);

    void unSubscribe(LifecycleOwner owner);

    void addSubscriber(LifecycleOwner owner,String event,IEventListener l);

    boolean hasSubscriber(LifecycleOwner owner);

    void removeSubscriber(LifecycleOwner owner);

}
