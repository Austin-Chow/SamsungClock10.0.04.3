package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle.Event;

public class CompositeGeneratedAdaptersObserver implements GenericLifecycleObserver {
    private final GeneratedAdapter[] mGeneratedAdapters;

    public void onStateChanged(LifecycleOwner source, Event event) {
        int i = 0;
        MethodCallsLogger logger = new MethodCallsLogger();
        for (GeneratedAdapter mGenerated : this.mGeneratedAdapters) {
            mGenerated.callMethods(source, event, false, logger);
        }
        GeneratedAdapter[] generatedAdapterArr = this.mGeneratedAdapters;
        int length = generatedAdapterArr.length;
        while (i < length) {
            generatedAdapterArr[i].callMethods(source, event, true, logger);
            i++;
        }
    }
}
