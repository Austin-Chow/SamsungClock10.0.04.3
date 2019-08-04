package com.samsung.android.sdk.sgi.vi;

import android.content.Context;

public final class SGSceneBuilder {
    SGSceneBuilderListenerHolder mBuilderListenerHolder;
    private Context mContext;
    SGSceneResourceProviderHolder mResProviderHolder;
    SGSceneNodeFactory mSceneNodeFactory;
    SGSceneResourceProvider mSceneResourceProvider;

    public SGSceneBuilder(Context context) {
        this.mContext = context;
    }

    private void initSceneListeners() {
        if (this.mSceneResourceProvider == null) {
            this.mSceneResourceProvider = new SGDefaultSceneResourceProvider(this.mContext.getResources(), this.mContext.getPackageName());
        }
        if (this.mResProviderHolder == null) {
            this.mResProviderHolder = new SGSceneResourceProviderHolder(this.mSceneResourceProvider);
        } else {
            this.mResProviderHolder.setInterface(this.mSceneResourceProvider);
        }
        if (this.mSceneNodeFactory == null) {
            this.mSceneNodeFactory = new SGDefaultSceneNodeFactory(this.mSceneResourceProvider);
        }
        if (this.mBuilderListenerHolder == null) {
            this.mBuilderListenerHolder = new SGSceneBuilderListenerHolder(this.mSceneResourceProvider, this.mSceneNodeFactory);
            return;
        }
        this.mBuilderListenerHolder.setNodeFactory(this.mSceneNodeFactory);
        this.mBuilderListenerHolder.setResourceProvider(this.mSceneResourceProvider);
    }

    public SGSceneInfo getScene(int resId) {
        initSceneListeners();
        SGSceneImporter sceneImporter = new SGSceneImporter(this.mResProviderHolder, this.mBuilderListenerHolder);
        this.mBuilderListenerHolder.prepare();
        sceneImporter.load("res:" + resId);
        SGSceneInfo sceneInfo = this.mBuilderListenerHolder.getSceneInfo();
        this.mResProviderHolder.clear();
        this.mBuilderListenerHolder.clear();
        return sceneInfo;
    }

    public SGSceneInfo getScene(String path) {
        initSceneListeners();
        SGSceneImporter sceneImporter = new SGSceneImporter(this.mResProviderHolder, this.mBuilderListenerHolder);
        this.mBuilderListenerHolder.prepare();
        sceneImporter.load(path);
        SGSceneInfo sceneInfo = this.mBuilderListenerHolder.getSceneInfo();
        this.mResProviderHolder.clear();
        this.mBuilderListenerHolder.clear();
        return sceneInfo;
    }

    public void setNodeFactory(SGSceneNodeFactory factory) {
        this.mSceneNodeFactory = factory;
    }

    public void setResourceProvider(SGSceneResourceProvider resourceProvider) {
        this.mSceneResourceProvider = resourceProvider;
    }
}
