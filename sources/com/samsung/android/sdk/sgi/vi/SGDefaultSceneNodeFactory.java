package com.samsung.android.sdk.sgi.vi;

final class SGDefaultSceneNodeFactory implements SGSceneNodeFactory {
    private SGSceneResourceProvider mResourceProvider = null;

    SGDefaultSceneNodeFactory(SGSceneResourceProvider resourceProvider) {
        this.mResourceProvider = resourceProvider;
    }

    public SGDefaultSceneNode createNode() {
        return new SGDefaultSceneNode(this.mResourceProvider);
    }
}
