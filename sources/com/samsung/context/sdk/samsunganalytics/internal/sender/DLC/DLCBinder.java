package com.samsung.context.sdk.samsunganalytics.internal.sender.DLC;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.spp.push.dlc.api.IDlcService;
import com.sec.spp.push.dlc.api.IDlcService.Stub;

public class DLCBinder {
    private static String DLC_LOG_CLASS = "com.sec.spp.push.dlc.writer.WriterService";
    private static String DLC_LOG_PACKAGE = "com.sec.spp.push";
    private Callback callback;
    private Context context;
    private BroadcastReceiver dlcRegisterReplyReceiver;
    private IDlcService dlcService;
    private ServiceConnection dlcServiceConnection;
    private boolean isBindToDLC;
    private boolean onRegisterRequest;
    private String registerFilter;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.sender.DLC.DLCBinder$1 */
    class C04691 implements ServiceConnection {
        C04691() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            Debug.LogD("DLC Sender", "DLC Client ServiceConnected");
            DLCBinder.this.dlcService = Stub.asInterface(service);
            if (DLCBinder.this.dlcRegisterReplyReceiver != null) {
                DLCBinder.this.context.unregisterReceiver(DLCBinder.this.dlcRegisterReplyReceiver);
                DLCBinder.this.dlcRegisterReplyReceiver = null;
            }
            if (DLCBinder.this.callback != null) {
                DLCBinder.this.callback.onResult(null);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Debug.LogD("DLC Sender", "Client ServiceDisconnected");
            DLCBinder.this.dlcService = null;
            DLCBinder.this.isBindToDLC = false;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.sender.DLC.DLCBinder$2 */
    class C04702 extends BroadcastReceiver {
        C04702() {
        }

        public void onReceive(Context context, Intent intent) {
            DLCBinder.this.onRegisterRequest = false;
            if (intent == null) {
                Debug.LogD("DLC Sender", "dlc register reply fail");
                return;
            }
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action == null || bundle == null) {
                Debug.LogD("DLC Sender", "dlc register reply fail");
            } else if (action.equals(DLCBinder.this.registerFilter)) {
                String result = bundle.getString("EXTRA_STR");
                int resultCode = bundle.getInt("EXTRA_RESULT_CODE");
                Debug.LogD("DLC Sender", "register DLC result:" + result);
                if (resultCode < 0) {
                    Debug.LogD("DLC Sender", "register DLC result fail:" + result);
                    return;
                }
                DLCBinder.this.bindService(bundle.getString("EXTRA_STR_ACTION"));
            }
        }
    }

    public DLCBinder(Context context) {
        this.isBindToDLC = false;
        this.onRegisterRequest = false;
        this.dlcServiceConnection = new C04691();
        this.context = context;
        this.registerFilter = context.getPackageName();
        this.registerFilter += ".REGISTER_FILTER";
    }

    public DLCBinder(Context context, Callback callback) {
        this(context);
        this.callback = callback;
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.registerFilter);
        if (this.dlcRegisterReplyReceiver == null) {
            this.dlcRegisterReplyReceiver = new C04702();
        }
        this.context.registerReceiver(this.dlcRegisterReplyReceiver, intentFilter);
    }

    public void sendRegisterRequestToDLC() {
        if (this.dlcRegisterReplyReceiver == null) {
            registerReceiver();
        }
        if (this.onRegisterRequest) {
            Debug.LogD("DLCBinder", "already send register request");
            return;
        }
        Intent intent = new Intent("com.sec.spp.push.REQUEST_REGISTER");
        intent.putExtra("EXTRA_PACKAGENAME", this.context.getPackageName());
        intent.putExtra("EXTRA_INTENTFILTER", this.registerFilter);
        intent.setPackage("com.sec.spp.push");
        this.context.sendBroadcast(intent);
        this.onRegisterRequest = true;
        Debug.LogD("DLCBinder", "send register Request");
        Debug.LogENG("send register Request:" + this.context.getPackageName());
    }

    private void bindService(String action) {
        if (this.isBindToDLC) {
            unbindService();
        }
        try {
            Intent intent = new Intent(action);
            intent.setClassName(DLC_LOG_PACKAGE, DLC_LOG_CLASS);
            this.isBindToDLC = this.context.bindService(intent, this.dlcServiceConnection, 1);
            Debug.LogD("DLCBinder", "bind");
        } catch (Exception e) {
            Debug.LogException(getClass(), e);
        }
    }

    private void unbindService() {
        if (this.isBindToDLC) {
            try {
                Debug.LogD("DLCBinder", "unbind");
                this.context.unbindService(this.dlcServiceConnection);
                this.isBindToDLC = false;
            } catch (Exception e) {
                Debug.LogException(getClass(), e);
            }
        }
    }

    public boolean isBindToDLC() {
        return this.isBindToDLC;
    }

    public IDlcService getDlcService() {
        return this.dlcService;
    }
}
