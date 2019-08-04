package com.samsung.android.sdk.sgi.vi;

public final class SGSceneInfo {
    private SGSceneNode mRootNode;

    SGSceneInfo() {
    }

    public SGSceneNode getRootNode() {
        return this.mRootNode;
    }

    void setRootNode(SGSceneNode node) {
        this.mRootNode = node;
    }
}
