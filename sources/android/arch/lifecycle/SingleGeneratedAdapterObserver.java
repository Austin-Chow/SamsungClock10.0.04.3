package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle.Event;

public class SingleGeneratedAdapterObserver implements GenericLifecycleObserver {
    private final GeneratedAdapter mGeneratedAdapter;

    public void onStateChanged(LifecycleOwner source, Event event) {
        this.mGeneratedAdapter.callMethods(source, event, false, null);
        this.mGeneratedAdapter.callMethods(source, event, true, null);
    }
}
