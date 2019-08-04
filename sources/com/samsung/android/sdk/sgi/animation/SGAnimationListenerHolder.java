package com.samsung.android.sdk.sgi.animation;

final class SGAnimationListenerHolder extends SGAnimationListenerBase {
    SGAnimationListener mListener;
    SGVisualValueProvider mProvider = new SGVisualValueProvider();

    public SGAnimationListenerHolder(SGAnimationListener listener) {
        this.mListener = listener;
    }

    public SGAnimationListener getInterface() {
        return this.mListener;
    }

    public void onCancelled(int id) {
        this.mProvider.setAnimationListenerBase(this);
        try {
            this.mListener.onCancelled(id, this.mProvider);
        } catch (Exception e) {
            SGAnimationException.handle(e, "SGAnimationListener::onCancelled error: uncaught exception");
        }
        this.mProvider.setAnimationListenerBase(null);
    }

    public void onDiscarded(int id) {
        try {
            this.mListener.onDiscarded(id);
        } catch (Exception e) {
            SGAnimationException.handle(e, "SGAnimationListener::onDiscarded error: uncaught exception");
        }
    }

    public void onFinished(int id) {
        this.mProvider.setAnimationListenerBase(this);
        try {
            this.mListener.onFinished(id, this.mProvider);
        } catch (Exception e) {
            SGAnimationException.handle(e, "SGAnimationListener::onFinished error: uncaught exception");
        }
        this.mProvider.setAnimationListenerBase(null);
    }

    public void onRepeated(int id) {
        this.mProvider.setAnimationListenerBase(this);
        try {
            this.mListener.onRepeated(id, this.mProvider);
        } catch (Exception e) {
            SGAnimationException.handle(e, "SGAnimationListener::onRepeated error: uncaught exception");
        }
        this.mProvider.setAnimationListenerBase(null);
    }

    public void onStarted(int id) {
        this.mProvider.setAnimationListenerBase(this);
        try {
            this.mListener.onStarted(id, this.mProvider);
        } catch (Exception e) {
            SGAnimationException.handle(e, "SGAnimationListener::onStarted error: uncaught exception");
        }
        this.mProvider.setAnimationListenerBase(null);
    }

    public void setInterface(SGAnimationListener listener) {
        this.mListener = listener;
    }
}
