package com.samsung.context.sdk.samsunganalytics.internal.sender.buffering;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.DBOpenHelper;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.database.DbManager;
import com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.queue.QueueManager;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.List;
import java.util.Queue;

public class Manager {
    private static Manager instance;
    private DbManager dbManager;
    private QueueManager queueManager;
    private boolean useDatabase;

    private Manager(Context context, boolean useDatabase, int queueSize) {
        if (useDatabase) {
            this.dbManager = new DbManager(context);
        }
        this.queueManager = new QueueManager(queueSize);
        this.useDatabase = useDatabase;
    }

    private Manager(DBOpenHelper dbOpenHelper, int queueSize) {
        this.dbManager = new DbManager(dbOpenHelper);
        this.queueManager = new QueueManager(queueSize);
        this.useDatabase = true;
    }

    public static Manager getInstance(Context context, Configuration configuration) {
        if (instance == null) {
            synchronized (Manager.class) {
                int queueSize = configuration.getQueueSize();
                if (!Tracker.sdkPolicy.enableUseDBQueue()) {
                    instance = new Manager(context, false, queueSize);
                } else if (Preferences.getPreferences(context).getString("lgt", "").equals("rtb")) {
                    DBOpenHelper dbOpenHelper = configuration.getDbOpenHelper();
                    if (dbOpenHelper != null) {
                        instance = new Manager(dbOpenHelper, queueSize);
                    } else {
                        instance = new Manager(context, true, queueSize);
                    }
                } else {
                    instance = new Manager(context, false, queueSize);
                }
            }
        }
        return instance;
    }

    public void enableDatabaseBuffering(Context context) {
        enableDatabaseBuffering(new DbManager(context));
    }

    public void enableDatabaseBuffering(DBOpenHelper dbOpenHelper) {
        enableDatabaseBuffering(new DbManager(dbOpenHelper));
    }

    public void enableDatabaseBuffering(DbManager dbManager) {
        this.useDatabase = true;
        this.dbManager = dbManager;
        mergeQueueToDb();
    }

    private void mergeQueueToDb() {
        if (!this.queueManager.getAll().isEmpty()) {
            for (SimpleLog log : this.queueManager.getAll()) {
                this.dbManager.insert(log);
            }
            this.queueManager.getAll().clear();
        }
    }

    public boolean isEnabledDatabaseBuffering() {
        return this.useDatabase;
    }

    public void insert(SimpleLog simpleLog) {
        if (this.useDatabase) {
            this.dbManager.insert(simpleLog);
        } else {
            this.queueManager.insert(simpleLog);
        }
    }

    public void insert(long timestamp, String log, LogType logType) {
        insert(new SimpleLog(timestamp, log, logType));
    }

    public void delete() {
        if (this.useDatabase) {
            this.dbManager.delete(Utils.getDaysAgo(5));
        }
    }

    public Queue<SimpleLog> get() {
        return get(0);
    }

    public Queue<SimpleLog> get(int limit) {
        Queue<SimpleLog> queue;
        if (this.useDatabase) {
            delete();
            if (limit <= 0) {
                queue = this.dbManager.selectAll();
            } else {
                queue = this.dbManager.selectSome(limit);
            }
        } else {
            queue = this.queueManager.getAll();
        }
        if (!queue.isEmpty()) {
            Debug.LogENG("get log from " + (this.useDatabase ? "Database " : "Queue ") + "(" + queue.size() + ")");
        }
        return queue;
    }

    public void remove(List<String> ids) {
        if (!ids.isEmpty() && this.useDatabase) {
            this.dbManager.delete((List) ids);
        }
    }
}
