package com.samsung.context.sdk.samsunganalytics.internal.sender.DLS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.policy.GetPolicyClient;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DLSLogSender extends BaseLogSender {
    public DLSLogSender(Context context, Configuration configuration) {
        super(context, configuration);
    }

    private int getNetworkType() {
        NetworkInfo networkInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return -4;
        }
        return networkInfo.getType();
    }

    private int checkAvailableLogging(int networkType) {
        if (networkType == -4) {
            Debug.LogD("DLS Sender", "Network unavailable.");
            return -4;
        } else if (PolicyUtils.isPolicyExpired(this.context)) {
            Debug.LogD("DLS Sender", "policy expired. request policy");
            return -6;
        } else if (this.configuration.getRestrictedNetworkType() != networkType) {
            return 0;
        } else {
            Debug.LogD("DLS Sender", "Network unavailable by restrict option:" + networkType);
            return -4;
        }
    }

    private void sendSum(int networkType, LogType logType, Queue<SimpleLog> logs, int size, AsyncTaskCallback callback) {
        PolicyUtils.useQuota(this.context, networkType, size);
        this.executor.execute(new DLSAPIClient(logType, logs, this.configuration.getTrackingId(), this.configuration.getNetworkTimeoutInMilliSeconds(), callback));
    }

    private int flushBufferedLogs(int networkType, LogType logType, Queue<SimpleLog> logs, AsyncTaskCallback callback) {
        List<String> delList = new ArrayList();
        Iterator<SimpleLog> iterator = logs.iterator();
        while (iterator.hasNext()) {
            Queue<SimpleLog> packet = new LinkedBlockingQueue();
            int sizeLimit = PolicyUtils.getRemainingQuota(this.context, networkType);
            if (51200 <= sizeLimit) {
                sizeLimit = 51200;
            }
            int size = 0;
            while (iterator.hasNext()) {
                SimpleLog log = (SimpleLog) iterator.next();
                if (log.getType() == logType) {
                    if (log.getData().getBytes().length + size > sizeLimit) {
                        break;
                    }
                    size += log.getData().getBytes().length;
                    packet.add(log);
                    iterator.remove();
                    delList.add(log.getId());
                    if (logs.isEmpty()) {
                        this.manager.remove(delList);
                        logs = this.manager.get(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                        iterator = logs.iterator();
                    }
                }
            }
            if (packet.isEmpty()) {
                return -1;
            }
            this.manager.remove(delList);
            sendSum(networkType, logType, packet, size, callback);
            Debug.LogD("DLSLogSender", "send packet : num(" + packet.size() + ") size(" + size + ")");
        }
        return 0;
    }

    private int sendOne(int networkType, SimpleLog simpleLog, AsyncTaskCallback callback, boolean isSync) {
        if (simpleLog == null) {
            return -100;
        }
        int size = simpleLog.getData().getBytes().length;
        int ret = PolicyUtils.hasQuota(this.context, networkType, size);
        if (ret != 0) {
            return ret;
        }
        PolicyUtils.useQuota(this.context, networkType, size);
        DLSAPIClient task = new DLSAPIClient(simpleLog, this.configuration.getTrackingId(), this.configuration.getNetworkTimeoutInMilliSeconds(), callback);
        if (isSync) {
            Debug.LogENG("sync send");
            task.run();
            return task.onFinish();
        }
        this.executor.execute(task);
        return 0;
    }

    public int send(Map<String, String> log) {
        final int networkType = getNetworkType();
        int ret = checkAvailableLogging(networkType);
        if (ret != 0) {
            insert(log);
            if (ret == -6) {
                PolicyUtils.getPolicy(this.context, this.configuration, this.executor, this.deviceInfo);
                this.manager.delete();
            }
            return ret;
        }
        AsyncTaskCallback callback = new AsyncTaskCallback() {
            public void onSuccess(int code, String param, String param2, String param3) {
            }

            public void onFail(int code, String param, String param2, String param3) {
                DLSLogSender.this.manager.insert(Long.valueOf(param).longValue(), param2, param3.equals(LogType.DEVICE.getAbbrev()) ? LogType.DEVICE : LogType.UIX);
                PolicyUtils.useQuota(DLSLogSender.this.context, networkType, param2.getBytes().length * -1);
            }
        };
        ret = sendOne(networkType, new SimpleLog(Long.valueOf((String) log.get("ts")).longValue(), makeBodyString(setCommonParamToLog(log)), getLogType(log)), callback, false);
        if (ret == -1) {
            int i = ret;
            return ret;
        }
        Queue<SimpleLog> queue = this.manager.get(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        if (this.manager.isEnabledDatabaseBuffering()) {
            flushBufferedLogs(networkType, LogType.UIX, queue, callback);
            flushBufferedLogs(networkType, LogType.DEVICE, queue, callback);
        } else {
            while (!queue.isEmpty()) {
                ret = sendOne(networkType, (SimpleLog) queue.poll(), callback, false);
                if (ret == -1) {
                    i = ret;
                    return ret;
                }
            }
        }
        i = ret;
        return ret;
    }

    public int sendSync(Map<String, String> log) {
        int networkType = getNetworkType();
        int ret = checkAvailableLogging(networkType);
        if (ret != 0) {
            if (ret == -6) {
                GetPolicyClient getPolicyClient = PolicyUtils.makeGetPolicyClient(this.context, this.configuration, this.deviceInfo, null);
                getPolicyClient.run();
                ret = getPolicyClient.onFinish();
                Debug.LogENG("get policy sync " + ret);
                if (ret != 0) {
                    return ret;
                }
            }
            return ret;
        }
        int i = ret;
        return sendOne(networkType, new SimpleLog(Long.valueOf((String) log.get("ts")).longValue(), makeBodyString(setCommonParamToLog(log)), getLogType(log)), null, true);
    }
}
