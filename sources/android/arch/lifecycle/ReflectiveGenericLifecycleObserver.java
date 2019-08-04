package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle.Event;

class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
    private final CallbackInfo mInfo;
    private final Object mWrapped;

    public void onStateChanged(LifecycleOwner source, Event event) {
        this.mInfo.invokeCallbacks(source, event, this.mWrapped);
    }
}
