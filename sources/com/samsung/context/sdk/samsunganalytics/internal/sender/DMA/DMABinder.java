package com.samsung.context.sdk.samsunganalytics.internal.sender.DMA;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.android.diagmonagent.sa.IDMAInterface;
import com.sec.android.diagmonagent.sa.IDMAInterface.Stub;

public class DMABinder {
    private Context context;
    private IDMAInterface dmaInterface;
    private boolean isTokenFail = false;
    private ServiceConnection serviceConnection;

    public DMABinder(Context context, final Callback<Void, String> callback) {
        this.context = context;
        this.serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    DMABinder.this.dmaInterface = Stub.asInterface(service);
                    String dmaDid = DMABinder.this.dmaInterface.checkToken();
                    if (dmaDid == null) {
                        DMABinder.this.unBind();
                        DMABinder.this.isTokenFail = true;
                        Debug.LogD("DMABinder", "Token failed");
                        return;
                    }
                    DMABinder.this.isTokenFail = false;
                    callback.onResult(dmaDid);
                    Debug.LogD("DMABinder", "DMA connected");
                } catch (Exception e) {
                    DMABinder.this.unBind();
                    DMABinder.this.isTokenFail = true;
                    Debug.LogException(e.getClass(), e);
                    e.printStackTrace();
                }
            }

            public void onServiceDisconnected(ComponentName name) {
                DMABinder.this.dmaInterface = null;
            }
        };
    }

    public boolean bind() {
        if (this.dmaInterface == null && !this.isTokenFail) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.sec.android.diagmonagent", "com.sec.android.diagmonagent.sa.receiver.SALogReceiverService");
                this.context.bindService(intent, this.serviceConnection, 1);
            } catch (Exception e) {
                Debug.LogException(e.getClass(), e);
            }
        }
        return this.isTokenFail;
    }

    public boolean isTokenfail() {
        return this.isTokenFail;
    }

    public void unBind() {
        if (this.dmaInterface != null) {
            this.context.unbindService(this.serviceConnection);
        }
    }

    public IDMAInterface getDmaInterface() {
        return this.dmaInterface;
    }
}
