package com.esli.fireturretlib.binder;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

/**
 * Created by lisic on 2019/4/21.
 */

public class ApplicationTurretWrapper implements Parcelable{

    private final int mPid;
    private final ITurret mAppTurret;

    public ApplicationTurretWrapper(int pid,IApplicationTurret turret)
    {
        this.mPid = pid;
        this.mAppTurret = turret;
    }

    protected ApplicationTurretWrapper(Parcel in) {
        mPid = in.readInt();
        mAppTurret = ApplicationTurretNative.asInterface(in.readStrongBinder());
    }

    public static final Creator<ApplicationTurretWrapper> CREATOR = new Creator<ApplicationTurretWrapper>() {
        @Override
        public ApplicationTurretWrapper createFromParcel(Parcel in) {
            return new ApplicationTurretWrapper(in);
        }

        @Override
        public ApplicationTurretWrapper[] newArray(int size) {
            return new ApplicationTurretWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mPid);
        parcel.writeStrongBinder(mAppTurret.asBinder());
    }

    public int getPid() {
        return mPid;
    }

    public ITurret getTurret()
    {
        return mAppTurret;
    }
}
