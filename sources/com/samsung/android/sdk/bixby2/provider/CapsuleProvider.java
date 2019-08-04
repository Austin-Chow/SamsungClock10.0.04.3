package com.samsung.android.sdk.bixby2.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.samsung.android.sdk.bixby2.LogUtil;
import com.samsung.android.sdk.bixby2.Sbixby;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CapsuleProvider extends ContentProvider {
    private static final String TAG = (CapsuleProvider.class.getSimpleName() + "_" + "1.0.6");
    private static Map<String, ActionHandler> actionMap = new HashMap();
    private static String mActionId = null;
    private static Signature mBixbyAgentSignature = new Signature(Base64.decode("MIIE1DCCA7ygAwIBAgIJANIJlaecDarWMA0GCSqGSIb3DQEBBQUAMIGiMQswCQYDVQQGEwJLUjEUMBIGA1UECBMLU291dGggS29yZWExEzARBgNVBAcTClN1d29uIENpdHkxHDAaBgNVBAoTE1NhbXN1bmcgQ29ycG9yYXRpb24xDDAKBgNVBAsTA0RNQzEVMBMGA1UEAxMMU2Ftc3VuZyBDZXJ0MSUwIwYJKoZIhvcNAQkBFhZhbmRyb2lkLm9zQHNhbXN1bmcuY29tMB4XDTExMDYyMjEyMjUxMloXDTM4MTEwNzEyMjUxMlowgaIxCzAJBgNVBAYTAktSMRQwEgYDVQQIEwtTb3V0aCBLb3JlYTETMBEGA1UEBxMKU3V3b24gQ2l0eTEcMBoGA1UEChMTU2Ftc3VuZyBDb3Jwb3JhdGlvbjEMMAoGA1UECxMDRE1DMRUwEwYDVQQDEwxTYW1zdW5nIENlcnQxJTAjBgkqhkiG9w0BCQEWFmFuZHJvaWQub3NAc2Ftc3VuZy5jb20wggEgMA0GCSqGSIb3DQEBAQUAA4IBDQAwggEIAoIBAQDJhjhKPh8vsgZnDnjvIyIVwNJvRaInKNuZpE2hHDWsM6cf4HHEotaCWptMiLMz7ZbzxebGZtYPPulMSQiFq8+NxmD3B6q8d+rT4tDYrugQjBXNJg8uhQQsKNLyktqjxtoMe/I5HbeEGq3o/fDJ0N7893Ek5tLeCp4NLadGw2cOT/zchbcBu0dEhhuW/3MR2jYDxaEDNuVf+jS0NT7tyF9RAV4VGMZ+MJ45+HY5/xeBB/EJzRhBGmB38mlktuY/inC5YZ2wQwajI8Gh0jr4Z+GfFPVw/+Vz0OOgwrMGMqrsMXM4CZS+HjQeOpC9LkthVIH0bbOeqDgWRI7DX+sXNcHzAgEDo4IBCzCCAQcwHQYDVR0OBBYEFJMsOvcLYnoMdhC1oOdCfWz66j8eMIHXBgNVHSMEgc8wgcyAFJMsOvcLYnoMdhC1oOdCfWz66j8eoYGopIGlMIGiMQswCQYDVQQGEwJLUjEUMBIGA1UECBMLU291dGggS29yZWExEzARBgNVBAcTClN1d29uIENpdHkxHDAaBgNVBAoTE1NhbXN1bmcgQ29ycG9yYXRpb24xDDAKBgNVBAsTA0RNQzEVMBMGA1UEAxMMU2Ftc3VuZyBDZXJ0MSUwIwYJKoZIhvcNAQkBFhZhbmRyb2lkLm9zQHNhbXN1bmcuY29tggkA0gmVp5wNqtYwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAMpYB/kDgNqSobMXUndjBtUFZmOcmN1OLDUMDaaxRUw9jqs6MAZoaZmFqLxuyxfq9bzEyYfOA40cWI/BT2ePFP1/W0ZZdewAOTcJEwbJ+L+mjI/8Hf1LEZ16GJHqoARhxN+MMm78BxWekKZ20vwslt9cQenuB7hAvcv9HlQFk4mdS4RTEL4udKkLnMIiX7GQOoZJO0Tq76dEgkSti9JJkk6htuUwLRvRMYWHVjC9kgWSJDFEt+yjULIVb9HDb7i2raWDK0E6B9xUl3tRs3Q81n5nEYNufAH2WzoO0shisLYLEjxJgjUaXM/BaM3VZRmnMv4pJVUTWxXAek2nAjIEBWA==", 0));
    private static boolean mIsAppInitialized = false;
    private static final boolean mIsUserBuild = "user".equals(Build.TYPE);
    private static boolean mWaitForHandler = false;
    private static Object sWaitLock = new Object();
    private Object sActionExecutionLock = new Object();

    /* renamed from: com.samsung.android.sdk.bixby2.provider.CapsuleProvider$1 */
    static class C04391 extends TimerTask {
        C04391() {
        }

        public void run() {
            CapsuleProvider.mWaitForHandler = false;
        }
    }

    private class CapsuleResponseCallback implements ResponseCallback {
        private boolean actionExecuted;
        private boolean actionTimedOut;
        private Bundle resultBundle;

        public CapsuleResponseCallback() {
            this.resultBundle = new Bundle();
            this.actionExecuted = false;
            this.actionTimedOut = false;
            this.actionExecuted = false;
            this.actionTimedOut = false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete(java.lang.String r5) {
            /*
            r4 = this;
            r0 = com.samsung.android.sdk.bixby2.provider.CapsuleProvider.this;
            r1 = r0.sActionExecutionLock;
            monitor-enter(r1);
            r0 = r4.actionTimedOut;	 Catch:{ all -> 0x004a }
            if (r0 == 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r1);	 Catch:{ all -> 0x004a }
        L_0x000c:
            return;
        L_0x000d:
            r0 = r4.actionExecuted;	 Catch:{ all -> 0x004a }
            if (r0 != 0) goto L_0x0048;
        L_0x0011:
            r0 = r4.resultBundle;	 Catch:{ all -> 0x004a }
            r2 = "status_code";
            r3 = 0;
            r0.putInt(r2, r3);	 Catch:{ all -> 0x004a }
            r0 = r4.resultBundle;	 Catch:{ all -> 0x004a }
            r2 = "result";
            r0.putString(r2, r5);	 Catch:{ all -> 0x004a }
            r0 = com.samsung.android.sdk.bixby2.provider.CapsuleProvider.TAG;	 Catch:{ all -> 0x004a }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004a }
            r2.<init>();	 Catch:{ all -> 0x004a }
            r3 = "action result: ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x004a }
            if (r5 == 0) goto L_0x004d;
        L_0x0031:
            r2 = r2.append(r5);	 Catch:{ all -> 0x004a }
            r2 = r2.toString();	 Catch:{ all -> 0x004a }
            com.samsung.android.sdk.bixby2.LogUtil.m7d(r0, r2);	 Catch:{ all -> 0x004a }
            r0 = 1;
            r4.actionExecuted = r0;	 Catch:{ all -> 0x004a }
            r0 = com.samsung.android.sdk.bixby2.provider.CapsuleProvider.this;	 Catch:{ all -> 0x004a }
            r0 = r0.sActionExecutionLock;	 Catch:{ all -> 0x004a }
            r0.notify();	 Catch:{ all -> 0x004a }
        L_0x0048:
            monitor-exit(r1);	 Catch:{ all -> 0x004a }
            goto L_0x000c;
        L_0x004a:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x004a }
            throw r0;
        L_0x004d:
            r5 = 0;
            goto L_0x0031;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.sdk.bixby2.provider.CapsuleProvider.CapsuleResponseCallback.onComplete(java.lang.String):void");
        }

        public Bundle getResultBundle() {
            return this.resultBundle;
        }

        public void setActionTimedOut(boolean timedout) {
            this.actionTimedOut = timedout;
        }
    }

    public static void setAppInitialized(boolean appInitialized) {
        synchronized (sWaitLock) {
            if (!mIsAppInitialized && appInitialized) {
                mIsAppInitialized = appInitialized;
                LogUtil.m9i(TAG, "releasing initialize wait lock.");
                sWaitLock.notify();
            }
        }
        new Timer().schedule(new C04391(), 3000);
    }

    public static void addActionHandler(String actionId, ActionHandler actionHandler) {
        synchronized (sWaitLock) {
            if (((ActionHandler) actionMap.get(actionId)) == null) {
                actionMap.put(actionId, actionHandler);
                if (mActionId != null && mActionId.equals(actionId)) {
                    LogUtil.m9i(TAG, "handler added: " + actionId);
                    sWaitLock.notify();
                }
            }
        }
    }

    private boolean isCallerAllowed() {
        if (!mIsUserBuild) {
            return true;
        }
        int uid = Binder.getCallingUid();
        PackageManager pm = getContext().getPackageManager();
        String[] packages = pm.getPackagesForUid(uid);
        if (packages == null) {
            LogUtil.m8e(TAG, "packages is null");
            return false;
        }
        for (String pName : packages) {
            if ("com.samsung.android.bixby.agent".equals(pName)) {
                try {
                    Signature[] sigs = pm.getPackageInfo(pName, 64).signatures;
                    if (sigs != null && sigs.length > 0 && mBixbyAgentSignature.equals(sigs[0])) {
                        return true;
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.m8e(TAG, "Not allowed to access capsule provider. package (s): " + Arrays.toString(packages));
        return false;
    }

    public boolean onCreate() {
        LogUtil.m7d(TAG, "onCreate");
        mWaitForHandler = true;
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void waitForAppInitialization() {
        synchronized (sWaitLock) {
            if (!mIsAppInitialized) {
                try {
                    sWaitLock.wait(3000);
                } catch (InterruptedException e) {
                    LogUtil.m8e(TAG, "interrupted exception");
                    e.printStackTrace();
                }
            }
        }
    }

    public Bundle call(String method, String arg, Bundle extras) {
        LogUtil.m7d(TAG, "call(): method --> " + method + " args --> " + arg + " extras --> " + (extras != null ? extras.toString() : null));
        if (!isCallerAllowed()) {
            throw new SecurityException("not allowed to access capsule provider.");
        } else if (TextUtils.isEmpty(method)) {
            throw new IllegalArgumentException("method is null or empty. pass valid action name.");
        } else {
            waitForAppInitialization();
            if (!mIsAppInitialized) {
                LogUtil.m8e(TAG, "App initialization error.");
                return updateStatus(-1, "Initialization Failure..");
            } else if (method.equals("getAppContext")) {
                Sbixby.getInstance();
                String latestAppContext = Sbixby.getStateHandler().getAppState(getContext());
                if (latestAppContext == null) {
                    return null;
                }
                Bundle bundle = new Bundle();
                bundle.putString("appContext", latestAppContext);
                return bundle;
            } else if (extras != null) {
                return executeAction(method, extras);
            } else {
                throw new IllegalArgumentException("action params are EMPTY.");
            }
        }
    }

    private Bundle updateStatus(int statusCode, String statusMsg) {
        Bundle bundle = new Bundle();
        bundle.putInt("status_code", statusCode);
        if (TextUtils.isEmpty(statusMsg) && statusCode == -1) {
            statusMsg = "Failed to execute action.";
            LogUtil.m8e(TAG, statusMsg);
        }
        bundle.putString("status_message", statusMsg);
        return bundle;
    }

    private ActionHandler getActionHandler(String actionId) throws InterruptedException {
        ActionHandler handler = (ActionHandler) actionMap.get(actionId);
        synchronized (sWaitLock) {
            if (handler == null) {
                if (mWaitForHandler) {
                    mActionId = actionId;
                    sWaitLock.wait(3000);
                    handler = (ActionHandler) actionMap.get(actionId);
                }
            }
        }
        return handler;
    }

    private synchronized Bundle executeAction(String actionId, Bundle params) {
        Bundle updateStatus;
        try {
            final ActionHandler handler = getActionHandler(actionId);
            if (handler == null) {
                LogUtil.m8e(TAG, "Handler not found!!..");
                updateStatus = updateStatus(-2, "Action handler not found");
            } else {
                if (params != null) {
                    if (params.containsKey("actionType")) {
                        final CapsuleResponseCallback callback = new CapsuleResponseCallback();
                        final String str = actionId;
                        final Bundle bundle = params;
                        Thread handlerThread = new Thread(new Runnable() {
                            public void run() {
                                handler.executeAction(CapsuleProvider.this.getContext(), str, bundle, callback);
                            }
                        });
                        handlerThread.start();
                        synchronized (this.sActionExecutionLock) {
                            if (!callback.actionExecuted) {
                                this.sActionExecutionLock.wait(30000);
                            }
                            if (callback.actionExecuted) {
                                updateStatus = callback.getResultBundle();
                                if (updateStatus != null) {
                                }
                            } else {
                                LogUtil.m7d(TAG, "timeout occurred..");
                                callback.setActionTimedOut(true);
                                handlerThread.interrupt();
                            }
                            updateStatus = updateStatus(-1, "action execution timed out");
                        }
                    }
                }
                updateStatus = updateStatus(-1, "params missing..");
            }
        } catch (Exception e) {
            LogUtil.m8e(TAG, "Unable to execute action." + e.toString());
            e.printStackTrace();
            updateStatus = updateStatus(-1, e.toString());
        }
        return updateStatus;
    }

    public String getType(Uri uri) {
        return "actionUri";
    }
}
