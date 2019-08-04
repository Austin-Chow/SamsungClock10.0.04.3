package com.samsung.android.sdk.bixby2;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.provider.CapsuleProvider;
import com.samsung.android.sdk.bixby2.state.StateHandler;
import java.util.Map;

public class Sbixby {
    private static final String TAG = (Sbixby.class.getSimpleName() + "_" + "1.0.6");
    private static Map<String, AppMetaInfo> appMetaInfoMap;
    private static Context mContext;
    private static Sbixby mInstance;
    private static String mPackageName;

    private Sbixby(Context context) {
        mContext = context;
    }

    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("App Context is NULL. pass valid context.");
        }
        if (mInstance == null) {
            mInstance = new Sbixby(context);
        }
        mInstance.setPackageName(context.getPackageName());
        CapsuleProvider.setAppInitialized(true);
        LogUtil.m7d(TAG, "initialized in package " + mPackageName);
    }

    public static synchronized Sbixby getInstance() throws IllegalStateException {
        Sbixby sbixby;
        synchronized (Sbixby.class) {
            if (mInstance == null) {
                throw new IllegalStateException("The Sbixby instance is NULL. do initialize Sbixby before accessing instance.");
            }
            LogUtil.m7d(TAG, " getInstance()");
            sbixby = mInstance;
        }
        return sbixby;
    }

    public static StateHandler getStateHandler() {
        LogUtil.m7d(TAG, " getStateHandler()");
        return StateHandler.getInstance();
    }

    public void addActionHandler(String actionId, ActionHandler handler) {
        if (TextUtils.isEmpty(actionId) || handler == null) {
            throw new IllegalArgumentException("Action handler is NULL. pass valid app action handler implementation.");
        }
        LogUtil.m7d(TAG, " addActionHandler: action Id --> " + actionId);
        CapsuleProvider.addActionHandler(actionId, handler);
    }

    private void setPackageName(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("package name is null or empty.");
        }
        mPackageName = packageName;
    }

    public Map<String, AppMetaInfo> getAppMetaInfoMap() {
        return appMetaInfoMap;
    }
}
