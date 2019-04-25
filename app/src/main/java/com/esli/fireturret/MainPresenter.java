package com.esli.fireturret;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by lisic on 2019/4/25.
 */

public class MainPresenter implements MyLifeCycleObserver{
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d("sss","onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d("sss","onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d("sss","onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d("sss","onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d("sss","onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d("sss","onDestroy");
        owner.getLifecycle().removeObserver(this);
    }


}
