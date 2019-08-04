package com.samsung.android.sdk.sgi.vi;

import android.graphics.Color;
import com.samsung.android.sdk.sgi.animation.SGBone;
import com.samsung.android.sdk.sgi.animation.SGClipBoneParams;
import com.samsung.android.sdk.sgi.animation.SGPoseAnimationClip;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGVector4f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGMaterial;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGTextureProperty;
import com.samsung.android.sdk.sgi.render.SGVector4fProperty;
import com.samsung.android.sdk.sgi.vi.SGSceneNode.SGCameraInfo;
import java.util.ArrayList;
import java.util.List;

final class SGSceneBuilderListenerHolder extends SGSceneParserListenerBase {
    private String mMaterialName;
    private SGMaterial mNodeMaterial;
    private SGSceneInfo mSceneInfo;
    SGSceneNodeFactory mSceneNodeFactory;
    SGSceneResourceProvider mSceneResourceProvider;
    private List<SGSceneNode> mStack;

    public SGSceneBuilderListenerHolder(SGSceneResourceProvider sceneResourceProvider, SGSceneNodeFactory nodeFactory) {
        this.mNodeMaterial = null;
        this.mStack = null;
        this.mSceneInfo = null;
        this.mSceneNodeFactory = null;
        this.mSceneResourceProvider = null;
        this.mStack = new ArrayList();
        this.mSceneNodeFactory = nodeFactory;
        this.mSceneResourceProvider = sceneResourceProvider;
    }

    public void clear() {
        this.mStack.clear();
        this.mSceneInfo = null;
        this.mNodeMaterial = null;
    }

    public SGSceneInfo getSceneInfo() {
        return this.mSceneInfo;
    }

    public void onCamera(SGCameraInfo cameraInfo) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addCamera(cameraInfo);
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onCamera error: uncaught exception");
        }
    }

    public void onColor(SGSceneMaterialColorTypes colorType, int color) {
        try {
            if (colorType == SGSceneMaterialColorTypes.DIFFUSEC) {
                this.mNodeMaterial.setProperty(SGProperty.COLOR, new SGVector4fProperty(new SGVector4f((float) Color.alpha(color), (float) Color.red(color), (float) Color.green(color), (float) Color.blue(color))));
            }
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onColor error: uncaught exception");
        }
    }

    public void onGeometry(long cPtr) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).setGeometry((SGGeometry) SGJNI.createObjectFromNativePtr(SGGeometry.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onGeometry error: uncaught exception");
        }
    }

    public void onMaterialEnd() {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addMaterial(this.mMaterialName, this.mNodeMaterial);
            this.mNodeMaterial = null;
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onMaterialEnd error: uncaught exception");
        }
    }

    public boolean onMaterialStart(String name) {
        try {
            this.mNodeMaterial = new SGMaterial();
            this.mNodeMaterial.setTechnicName(name);
            this.mMaterialName = name;
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onMaterialStart error: uncaught exception");
        }
        return true;
    }

    public void onNodeEnd() {
        try {
            int lastElement = this.mStack.size() - 1;
            ((SGSceneNode) this.mStack.get(lastElement)).onCompleted();
            this.mStack.remove(lastElement);
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onNodeEnd error: uncaught exception");
        }
    }

    public boolean onNodeStart(String name, SGMatrix4f transform) {
        try {
            SGSceneNode currentNode = this.mSceneNodeFactory.createNode();
            currentNode.setName(name);
            currentNode.setLocalTransform(transform);
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addChild(currentNode);
            this.mStack.add(currentNode);
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onNodeStart error: uncaught exception");
        }
        return true;
    }

    public void onPoseAnimation(String target, long cPtr) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addPoseAnimation(target, (SGPoseAnimationClip) SGJNI.createObjectFromNativePtr(SGPoseAnimationClip.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onSkeletalAnimation error: uncaught exception");
        }
    }

    public void onPoseTarget(String target, long cPtr) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addPoseTarget(target, (SGGeometry) SGJNI.createObjectFromNativePtr(SGGeometry.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onSkeletalAnimation error: uncaught exception");
        }
    }

    public void onSkeletalAnimation(long cPtr) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).addSkeletonAnimationClip((SGClipBoneParams) SGJNI.createObjectFromNativePtr(SGClipBoneParams.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onSkeletalAnimation error: uncaught exception");
        }
    }

    public void onSkeleton(long cPtr) {
        try {
            ((SGSceneNode) this.mStack.get(this.mStack.size() - 1)).setSkeleton((SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onSkeleton error: uncaught exception");
        }
    }

    public void onTexture(SGSceneMaterialTextureTypes textureType, String name) {
        try {
            if (textureType == SGSceneMaterialTextureTypes.DIFFUSE) {
                String key = "texture:" + name;
                SGTextureProperty texture = (SGTextureProperty) this.mSceneResourceProvider.get(key);
                if (texture == null) {
                    texture = this.mSceneResourceProvider.getTexture(name);
                    if (texture != null) {
                        this.mSceneResourceProvider.put(key, texture);
                    }
                }
                if (texture != null) {
                    this.mNodeMaterial.setProperty(SGProperty.TEXTURE, texture);
                }
            }
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneBuilderListenerHolder::onTexture error: uncaught exception");
        }
    }

    public void prepare() {
        this.mSceneInfo = new SGSceneInfo();
        SGSceneNode rootNode = this.mSceneNodeFactory.createNode();
        this.mStack.add(rootNode);
        this.mSceneInfo.setRootNode(rootNode);
    }

    public void setNodeFactory(SGSceneNodeFactory nodeFactory) {
        this.mSceneNodeFactory = nodeFactory;
    }

    public void setResourceProvider(SGSceneResourceProvider sceneResourceProvider) {
        this.mSceneResourceProvider = sceneResourceProvider;
    }
}
