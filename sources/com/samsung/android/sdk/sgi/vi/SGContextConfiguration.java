package com.samsung.android.sdk.sgi.vi;

public final class SGContextConfiguration {
    public int mAlphaSize;
    public int mBackgroundThreadCount;
    public int mBlueSize;
    public int mDepthSize;
    public boolean mDirtyBoxVisualization;
    public int mGreenSize;
    public boolean mRecreateSurfaceOnSizeChange;
    public int mRedSize;
    public int mSampleBuffers;
    public int mSamples;
    public boolean mSeparateThreads;
    public boolean mSimpleClipping;
    public int mStencilSize;
    public boolean mVSync;

    public SGContextConfiguration() {
        this.mRedSize = 8;
        this.mGreenSize = 8;
        this.mBlueSize = 8;
        this.mAlphaSize = 8;
        this.mDepthSize = 16;
        this.mStencilSize = 8;
        this.mSampleBuffers = 0;
        this.mSamples = 0;
        this.mBackgroundThreadCount = 0;
        this.mSeparateThreads = true;
        this.mDirtyBoxVisualization = false;
        this.mRecreateSurfaceOnSizeChange = true;
        this.mVSync = true;
        this.mSimpleClipping = false;
    }

    public SGContextConfiguration(int backgroundThreadCount) {
        this();
        this.mBackgroundThreadCount = backgroundThreadCount;
    }

    public SGContextConfiguration(boolean separateThreads) {
        this();
        this.mSeparateThreads = separateThreads;
    }

    public void setRGB565() {
        this.mRedSize = 5;
        this.mGreenSize = 6;
        this.mBlueSize = 5;
        this.mAlphaSize = 0;
    }
}
