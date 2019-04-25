package com.esli.fireturretlib.binder;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

/**
 * Created by lisic on 2019/4/22.
 */

public class TurretServiceWrapper implements Parcelable {
    private int mOwnerPid;
    private String mServiceClass;
    private IBinder mBinder;

    public TurretServiceWrapper(int pid,String clsName,IBinder binder)
    {
        this.mOwnerPid = pid;
        this.mServiceClass = clsName;
        this.mBinder = binder;
    }

    protected TurretServiceWrapper(Parcel in) {
        mOwnerPid = in.readInt();
        mServiceClass = in.readString();
        mBinder = in.readStrongBinder();
    }

    public static final Creator<TurretServiceWrapper> CREATOR = new Creator<TurretServiceWrapper>() {
        @Override
        public TurretServiceWrapper createFromParcel(Parcel in) {
            return new TurretServiceWrapper(in);
        }

        @Override
        public TurretServiceWrapper[] newArray(int size) {
            return new TurretServiceWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mOwnerPid);
        parcel.writeString(mServiceClass);
        parcel.writeStrongBinder(mBinder);
    }

    public String getServiceClassName() {
        return mServiceClass;
    }

    public IBinder getBinder() {
        return mBinder;
    }

    public int getOwnerPid(){return mOwnerPid;}
}
