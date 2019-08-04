package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAlarmCelebVoice extends IInterface {

    public static abstract class Stub extends Binder implements IAlarmCelebVoice {

        private static class Proxy implements IAlarmCelebVoice {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void installContent(String validDate, String cost, String type, String title, String packageName, Uri path, IAlarmCelebVoiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice");
                    _data.writeString(validDate);
                    _data.writeString(cost);
                    _data.writeString(type);
                    _data.writeString(title);
                    _data.writeString(packageName);
                    if (path != null) {
                        _data.writeInt(1);
                        path.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteContent(String type, String packageName, IAlarmCelebVoiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice");
                    _data.writeString(type);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice");
        }

        public static IAlarmCelebVoice asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice");
            if (iin == null || !(iin instanceof IAlarmCelebVoice)) {
                return new Proxy(obj);
            }
            return (IAlarmCelebVoice) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice";
            switch (code) {
                case 1:
                    Uri _arg5;
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    if (data.readInt() != 0) {
                        _arg5 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    installContent(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    deleteContent(data.readString(), data.readString(), com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback.Stub.asInterface(data.readStrongBinder()));
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

    void deleteContent(String str, String str2, IAlarmCelebVoiceCallback iAlarmCelebVoiceCallback) throws RemoteException;

    void installContent(String str, String str2, String str3, String str4, String str5, Uri uri, IAlarmCelebVoiceCallback iAlarmCelebVoiceCallback) throws RemoteException;
}
