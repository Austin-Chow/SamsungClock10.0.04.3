package com.sec.android.diagmonagent.sa;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDMAInterface extends IInterface {

    public static abstract class Stub extends Binder implements IDMAInterface {

        private static class Proxy implements IDMAInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String checkToken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.diagmonagent.sa.IDMAInterface");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendCommon(int tcType, String tid, String data, String did) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.diagmonagent.sa.IDMAInterface");
                    _data.writeInt(tcType);
                    _data.writeString(tid);
                    _data.writeString(data);
                    _data.writeString(did);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendLog(int tcType, String tid, String logType, long timeStamp, String body) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.diagmonagent.sa.IDMAInterface");
                    _data.writeInt(tcType);
                    _data.writeString(tid);
                    _data.writeString(logType);
                    _data.writeLong(timeStamp);
                    _data.writeString(body);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static IDMAInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.sec.android.diagmonagent.sa.IDMAInterface");
            if (iin == null || !(iin instanceof IDMAInterface)) {
                return new Proxy(obj);
            }
            return (IDMAInterface) iin;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface("com.sec.android.diagmonagent.sa.IDMAInterface");
                    String _result2 = checkToken();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 2:
                    data.enforceInterface("com.sec.android.diagmonagent.sa.IDMAInterface");
                    _result = sendCommon(data.readInt(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface("com.sec.android.diagmonagent.sa.IDMAInterface");
                    _result = sendLog(data.readInt(), data.readString(), data.readString(), data.readLong(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString("com.sec.android.diagmonagent.sa.IDMAInterface");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    String checkToken() throws RemoteException;

    int sendCommon(int i, String str, String str2, String str3) throws RemoteException;

    int sendLog(int i, String str, String str2, long j, String str3) throws RemoteException;
}
