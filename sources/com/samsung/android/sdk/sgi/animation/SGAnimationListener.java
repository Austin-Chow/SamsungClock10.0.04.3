package com.samsung.android.sdk.sgi.animation;

public interface SGAnimationListener {
    void onCancelled(int i, SGVisualValueProvider sGVisualValueProvider);

    void onDiscarded(int i);

    void onFinished(int i, SGVisualValueProvider sGVisualValueProvider);

    void onRepeated(int i, SGVisualValueProvider sGVisualValueProvider);

    void onStarted(int i, SGVisualValueProvider sGVisualValueProvider);
}
