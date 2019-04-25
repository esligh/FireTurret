package com.esli.fireturret.service;

import android.os.RemoteException;

import com.esli.fireturret.aidl.ITestService;

/**
 * Created by lisic on 2019/4/21.
 */

public class TestService extends ITestService.Stub {
    private String mName = "lisc";

    @Override
    public String getName() throws RemoteException {
        return mName;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.mName = name;
    }
}
