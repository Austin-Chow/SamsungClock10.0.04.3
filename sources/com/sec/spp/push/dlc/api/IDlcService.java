package com.sec.spp.push.dlc.api;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDlcService extends IInterface {

    public static abstract class Stub extends Binder implements IDlcService {

        private static class Proxy implements IDlcService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int requestSend(String category, String svcCode, long timeStamp, String activity, String deviceId, String userId, String appVersion, String body) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.spp.push.dlc.api.IDlcService");
                    _data.writeString(category);
                    _data.writeString(svcCode);
                    _data.writeLong(timeStamp);
                    _data.writeString(activity);
                    _data.writeString(deviceId);
                    _data.writeString(userId);
                    _data.writeString(appVersion);
                    _data.writeString(body);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestSendUrgent(String category, String svcCode, long timeStamp, String activity, String deviceId, String userId, String appVersion, String body) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.spp.push.dlc.api.IDlcService");
                    _data.writeString(category);
                    _data.writeString(svcCode);
                    _data.writeLong(timeStamp);
                    _data.writeString(activity);
                    _data.writeString(deviceId);
                    _data.writeString(userId);
                    _data.writeString(appVersion);
                    _data.writeString(body);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestSendAggregation(String category, String svcCode, long timeStamp, String activity, String deviceId, String userId, String appVersion, String body, int index, long sum1, long sum2, long sum3, long sum4, long sum5) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.spp.push.dlc.api.IDlcService");
                    _data.writeString(category);
                    _data.writeString(svcCode);
                    _data.writeLong(timeStamp);
                    _data.writeString(activity);
                    _data.writeString(deviceId);
                    _data.writeString(userId);
                    _data.writeString(appVersion);
                    _data.writeString(body);
                    _data.writeInt(index);
                    _data.writeLong(sum1);
                    _data.writeLong(sum2);
                    _data.writeLong(sum3);
                    _data.writeLong(sum4);
                    _data.writeLong(sum5);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestSendSummary(String category, String svcCode, long timeStamp, String activity, String deviceId, String userId, String appVersion, String body, String index, long sum1, long sum2, long sum3, long sum4, long sum5) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.spp.push.dlc.api.IDlcService");
                    _data.writeString(category);
                    _data.writeString(svcCode);
                    _data.writeLong(timeStamp);
                    _data.writeString(activity);
                    _data.writeString(deviceId);
                    _data.writeString(userId);
                    _data.writeString(appVersion);
                    _data.writeString(body);
                    _data.writeString(index);
                    _data.writeLong(sum1);
                    _data.writeLong(sum2);
                    _data.writeLong(sum3);
                    _data.writeLong(sum4);
                    _data.writeLong(sum5);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static IDlcService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.sec.spp.push.dlc.api.IDlcService");
            if (iin == null || !(iin instanceof IDlcService)) {
                return new Proxy(obj);
            }
            return (IDlcService) iin;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface("com.sec.spp.push.dlc.api.IDlcService");
                    _result = requestSend(data.readString(), data.readString(), data.readLong(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface("com.sec.spp.push.dlc.api.IDlcService");
                    _result = requestSendUrgent(data.readString(), data.readString(), data.readLong(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface("com.sec.spp.push.dlc.api.IDlcService");
                    _result = requestSendAggregation(data.readString(), data.readString(), data.readLong(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readInt(), data.readLong(), data.readLong(), data.readLong(), data.readLong(), data.readLong());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface("com.sec.spp.push.dlc.api.IDlcService");
                    _result = requestSendSummary(data.readString(), data.readString(), data.readLong(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readLong(), data.readLong(), data.readLong(), data.readLong(), data.readLong());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString("com.sec.spp.push.dlc.api.IDlcService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int requestSend(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    int requestSendAggregation(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, int i, long j2, long j3, long j4, long j5, long j6) throws RemoteException;

    int requestSendSummary(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, long j2, long j3, long j4, long j5, long j6) throws RemoteException;

    int requestSendUrgent(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7) throws RemoteException;
}
