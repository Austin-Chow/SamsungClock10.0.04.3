package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAlarmCelebVoiceCallback extends IInterface {

    public static abstract class Stub extends Binder implements IAlarmCelebVoiceCallback {

        private static class Proxy implements IAlarmCelebVoiceCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void procedureResult(String packageName, int procedure, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback");
                    _data.writeString(packageName);
                    _data.writeInt(procedure);
                    _data.writeInt(result);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback");
        }

        public static IAlarmCelebVoiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback");
            if (iin == null || !(iin instanceof IAlarmCelebVoiceCallback)) {
                return new Proxy(obj);
            }
            return (IAlarmCelebVoiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback";
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    procedureResult(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void procedureResult(String str, int i, int i2) throws RemoteException;
}
