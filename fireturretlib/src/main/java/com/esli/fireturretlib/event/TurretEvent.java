package com.esli.fireturretlib.event;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

import com.esli.fireturretlib.common.StaticValue;

/**
 * Created by lisic on 2019/4/25.
 */

public class TurretEvent implements Parcelable{

    private int mSenderPid;
    private int mType;
    private String mWhat;
    private Bundle mData;
    private int mThreadMode;

    public TurretEvent(String what)
    {
        this(what,null);
    }

    public TurretEvent(String what,Bundle data)
    {
        this.mSenderPid = Process.myPid();
        this.mType = StaticValue.EventType.DEFAULT;
        this.mThreadMode = StaticValue.ThredMode.DEFAULT;
        this.mWhat = what;
        this.mData = data;
    }

    protected TurretEvent(Parcel in) {
        mSenderPid = in.readInt();
        mType = in.readInt();
        mWhat = in.readString();
        mData = in.readBundle();
        mThreadMode = in.readInt();
    }

    public static final Creator<TurretEvent> CREATOR = new Creator<TurretEvent>() {
        @Override
        public TurretEvent createFromParcel(Parcel in) {
            return new TurretEvent(in);
        }

        @Override
        public TurretEvent[] newArray(int size) {
            return new TurretEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mSenderPid);
        parcel.writeInt(mType);
        parcel.writeString(mWhat);
        parcel.writeBundle(mData);
        parcel.writeInt(mThreadMode);
    }

    public int getSenderPid() {
        return mSenderPid;
    }

    public int getType() {
        return mType;
    }

    public String getWhat() {
        return mWhat;
    }

    public Bundle getData() {
        return mData;
    }

    public int getThreadMode() {
        return mThreadMode;
    }

    @Override
    public String toString() {
        return "TurretEvent{" +
                "mSenderPid=" + mSenderPid +
                ", mType=" + mType +
                ", mWhat='" + mWhat + '\'' +
                ", mData=" + mData +
                ", mThreadMode=" + mThreadMode +
                '}';
    }
}
