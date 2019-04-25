package com.esli.fireturretlib.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.esli.fireturretlib.event.IEventListener;
import com.esli.fireturretlib.provider.EventProvider;
import com.esli.fireturretlib.utils.L;

/**
 * Created by lisic on 2019/4/25.
 */

public class EventLifeCycleObserver implements IEventLifeCycleObserver {
    private String mEvent;
    private IEventListener mListener;

    public EventLifeCycleObserver(String event ,IEventListener listener)
    {
        this.mEvent = event;
        this.mListener = listener;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        L.d("EventLifeCycleObserver:onCreate=>");
        EventProvider.get().addSubscriber(owner,mEvent,mListener);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        L.d("EventLifeCycleObserver:onDestroy=>");
        EventProvider.get().removeSubscriber(owner);
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {

    }
}
