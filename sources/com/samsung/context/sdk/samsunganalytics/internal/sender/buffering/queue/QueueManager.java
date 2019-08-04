package com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.queue;

import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager {
    protected LinkedBlockingQueue<SimpleLog> logQueue;

    public QueueManager() {
        this.logQueue = new LinkedBlockingQueue(25);
    }

    public QueueManager(int queueSize) {
        if (queueSize < 25) {
            this.logQueue = new LinkedBlockingQueue(25);
        } else if (queueSize > 100) {
            this.logQueue = new LinkedBlockingQueue(100);
        } else {
            this.logQueue = new LinkedBlockingQueue(queueSize);
        }
    }

    public void insert(SimpleLog log) {
        if (!this.logQueue.offer(log)) {
            Debug.LogD("QueueManager", "queue size over. remove oldest log");
            this.logQueue.poll();
            this.logQueue.offer(log);
        }
    }

    public Queue<SimpleLog> getAll() {
        return this.logQueue;
    }
}
