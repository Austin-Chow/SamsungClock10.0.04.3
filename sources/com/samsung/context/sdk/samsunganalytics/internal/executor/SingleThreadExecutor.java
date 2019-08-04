package com.samsung.context.sdk.samsunganalytics.internal.executor;

import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SingleThreadExecutor implements Executor {
    private static ExecutorService executorService;
    private static SingleThreadExecutor singleThreadExecutor;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.executor.SingleThreadExecutor$1 */
    class C04651 implements ThreadFactory {
        C04651() {
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setPriority(1);
            t.setDaemon(true);
            Debug.LogD("newThread on Executor");
            return t;
        }
    }

    public SingleThreadExecutor() {
        executorService = Executors.newSingleThreadExecutor(new C04651());
    }

    public void execute(final AsyncTaskClient api) {
        executorService.submit(new Runnable() {
            public void run() {
                api.run();
                api.onFinish();
            }
        });
    }

    public static Executor getInstance() {
        if (singleThreadExecutor == null) {
            singleThreadExecutor = new SingleThreadExecutor();
        }
        return singleThreadExecutor;
    }
}
