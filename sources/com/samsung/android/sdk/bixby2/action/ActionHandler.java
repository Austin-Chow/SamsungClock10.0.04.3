package com.samsung.android.sdk.bixby2.action;

import android.content.Context;
import android.os.Bundle;

public abstract class ActionHandler {
    public abstract void executeAction(Context context, String str, Bundle bundle, ResponseCallback responseCallback);
}
