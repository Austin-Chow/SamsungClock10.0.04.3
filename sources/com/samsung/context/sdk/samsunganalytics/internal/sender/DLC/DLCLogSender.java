package com.samsung.context.sdk.samsunganalytics.internal.sender.DLC;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.Map;
import java.util.Queue;

public class DLCLogSender extends BaseLogSender {
    private DLCBinder binder;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.sender.DLC.DLCLogSender$1 */
    class C04711 implements Callback<Void, Void> {
        C04711() {
        }

        public Void onResult(Void param) {
            DLCLogSender.this.sendAll();
            return null;
        }
    }

    public DLCLogSender(Context context, Configuration configuration) {
        super(context, configuration);
        this.binder = new DLCBinder(context, new C04711());
        this.binder.sendRegisterRequestToDLC();
    }

    private void sendAll() {
        Queue<SimpleLog> queue = this.manager.get();
        while (!queue.isEmpty()) {
            this.executor.execute(new SendLogTask(this.binder, this.configuration, (SimpleLog) queue.poll(), null));
        }
    }

    protected Map<String, String> setCommonParamToLog(Map<String, String> log) {
        Map<String, String> map = super.setCommonParamToLog(log);
        map.remove("do");
        map.remove("dm");
        map.remove("v");
        return map;
    }

    public int send(Map<String, String> log) {
        insert(log);
        if (!this.binder.isBindToDLC()) {
            this.binder.sendRegisterRequestToDLC();
        } else if (this.binder.getDlcService() != null) {
            sendAll();
        }
        return 0;
    }

    public int sendSync(Map<String, String> log) {
        Debug.LogD("DLCLogSender", "not support sync api");
        send(log);
        return -100;
    }
}
