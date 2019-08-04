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
import com.samsung.android.sdk.sgi.render.SGResourceShaderProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.render.SGShaderType;
import com.samsung.android.sdk.sgi.render.SGVectorfProperty;
import com.samsung.android.sdk.sgi.vi.SGSceneNode.SGCameraInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SGDefaultSceneNode implements SGSceneNode {
    protected List<SGClipBoneParams> mAnimationsList;
    protected List<SGCameraInfo> mCamerasList;
    protected List<SGDefaultSceneNode> mChildrenList;
    protected SGGeometry mGeometry;
    protected SGMatrix4f mLocalTransform;
    protected List<SGMaterial> mMaterialsList;
    protected String mNodeName = "";
    protected Map<String, Map<String, SGPoseAnimationClip>> mPoseAnimationsList;
    protected Map<String, SGGeometry> mPoseGeometry;
    protected SGSceneResourceProvider mResourceProvider;
    protected SGBone mSkeleton;

    public SGDefaultSceneNode(SGSceneResourceProvider resourceProvider) {
        this.mResourceProvider = resourceProvider;
        this.mLocalTransform = SGMatrix4f.getIdentity();
        this.mMaterialsList = new ArrayList();
        this.mChildrenList = new ArrayList();
        this.mAnimationsList = new ArrayList();
        this.mCamerasList = new ArrayList();
        this.mPoseGeometry = new HashMap();
        this.mPoseAnimationsList = new HashMap();
    }

    static final SGShaderProgramProperty createProgramProperty(String vertexShaderName, String fragmentShaderName) {
        return new SGShaderProgramProperty(new SGResourceShaderProperty(SGShaderType.VERTEX, vertexShaderName), new SGResourceShaderProperty(SGShaderType.FRAGMENT, fragmentShaderName));
    }

    public void addCamera(SGCameraInfo cameraInfo) {
        this.mCamerasList.add(cameraInfo);
    }

    public void addChild(SGSceneNode child) {
        this.mChildrenList.add((SGDefaultSceneNode) child);
    }

    public void addMaterial(String name, SGMaterial material) {
        this.mMaterialsList.add(material);
    }

    public void addPoseAnimation(String target, SGPoseAnimationClip clip) {
        String clipName = clip.getName();
        Map<String, SGPoseAnimationClip> mp = (Map) this.mPoseAnimationsList.get(clipName);
        if (mp == null) {
            mp = new HashMap();
            this.mPoseAnimationsList.put(clipName, mp);
        }
        mp.put(target, clip);
    }

    public void addPoseTarget(String target, SGGeometry geometry) {
        this.mPoseGeometry.put(target, geometry);
    }

    public void addSkeletonAnimationClip(SGClipBoneParams clip) {
        this.mAnimationsList.add(clip);
    }

    public SGDefaultSceneNode findByName(String name) {
        if (this.mNodeName.contentEquals(name)) {
            return this;
        }
        for (SGDefaultSceneNode node : this.mChildrenList) {
            SGDefaultSceneNode requiredNode = node.findByName(name);
            if (requiredNode != null) {
                return requiredNode;
            }
        }
        return null;
    }

    public final List<SGClipBoneParams> getAnimations() {
        return this.mAnimationsList;
    }

    public final List<SGCameraInfo> getCameras() {
        return this.mCamerasList;
    }

    public final List<SGDefaultSceneNode> getChildren() {
        return this.mChildrenList;
    }

    public final SGGeometry getGeometry() {
        return this.mGeometry;
    }

    public SGLayer getLayer() {
        SGLayer layer = getVisualContainer();
        if (this.mGeometry != null) {
            layer.setGeometryGenerator(SGGeometryGeneratorFactory.createStaticGeometryGenerator(this.mGeometry));
            if (this.mPoseGeometry != null) {
                layer.setGeometryGenerator(SGGeometryGeneratorFactory.createPoseGeometryGenerator(this.mGeometry, this.mPoseGeometry));
            }
        }
        for (SGDefaultSceneNode node : this.mChildrenList) {
            layer.addLayer(node.getLayer());
        }
        return layer;
    }

    public final SGMatrix4f getLocalTransform() {
        return this.mLocalTransform;
    }

    public final List<SGMaterial> getMaterials() {
        return this.mMaterialsList;
    }

    public final String getName() {
        return this.mNodeName;
    }

    public final Map<String, Map<String, SGPoseAnimationClip>> getPoseAnimations() {
        return this.mPoseAnimationsList;
    }

    protected SGShaderProgramProperty getSkeletalColorShader() {
        SGShaderProgramProperty property = (SGShaderProgramProperty) this.mResourceProvider.get("shader:SkeletalColor");
        if (property != null) {
            return property;
        }
        property = createProgramProperty("SkeletalAnimationColor.vert", "Color.frag");
        this.mResourceProvider.put("shader:SkeletalColor", property);
        return property;
    }

    protected SGShaderProgramProperty getSkeletalTextureShader() {
        SGShaderProgramProperty property = (SGShaderProgramProperty) this.mResourceProvider.get("shader:SkeletalTexture");
        if (property != null) {
            return property;
        }
        property = createProgramProperty("SkeletalAnimationTexture.vert", "TextureOpacity.frag");
        this.mResourceProvider.put("shader:SkeletalTexture", property);
        return property;
    }

    public final SGBone getSkeleton() {
        return this.mSkeleton;
    }

    protected SGLayer getVisualContainer() {
        SGLayer layer;
        int materialsCount = this.mMaterialsList.size();
        if (materialsCount > 0) {
            layer = new SGLayerImage();
            for (int i = 0; i < materialsCount; i++) {
                int j;
                SGMaterial mat = (SGMaterial) this.mMaterialsList.get(i);
                boolean haveBones = false;
                if (this.mGeometry != null) {
                    for (j = 0; j < this.mGeometry.getBuffersCount(); j++) {
                        if (this.mGeometry.getBufferName(j).contentEquals(SGProperty.BONES)) {
                            haveBones = true;
                            break;
                        }
                    }
                }
                boolean isTexture = false;
                for (j = 0; j < mat.getPropertyCount(); j++) {
                    String propName = mat.getPropertyName(j);
                    if (!propName.contentEquals(SGProperty.PROGRAM)) {
                        if (propName.contentEquals(SGProperty.TEXTURE)) {
                            isTexture = true;
                            ((SGLayerImage) layer).setTexture(mat.getProperty(j));
                        } else if (propName.contentEquals(SGProperty.COLOR)) {
                            SGVector4f p = new SGVector4f(((SGVectorfProperty) mat.getProperty(j)).toFloatArray());
                            ((SGLayerImage) layer).setColor(Color.argb((int) p.getX(), (int) p.getY(), (int) p.getZ(), (int) p.getW()));
                        } else {
                            layer.setProperty(propName, mat.getProperty(j));
                        }
                    }
                }
                if (haveBones) {
                    layer.setProgramProperty(isTexture ? getSkeletalTextureShader() : getSkeletalColorShader());
                }
            }
        } else if (this.mSkeleton != null) {
            layer = new SGLayerSkeleton();
            ((SGLayerSkeleton) layer).setSkeleton(this.mSkeleton);
        } else {
            layer = new SGLayer();
        }
        if (!this.mCamerasList.isEmpty()) {
            for (SGCameraInfo camera : this.mCamerasList) {
                SGLayerCamera cameraLayer = new SGLayerCamera();
                cameraLayer.setName(camera.mName);
                cameraLayer.setClearColor(camera.mColor);
                cameraLayer.setProjection(camera.mProjection);
                cameraLayer.setLocalTransform(camera.mWorld);
                layer.addLayer(cameraLayer);
            }
        }
        layer.setSize(1.0f, 1.0f);
        layer.setName(this.mNodeName);
        layer.setLocalTransform(this.mLocalTransform);
        return layer;
    }

    public void onCompleted() {
    }

    public void setGeometry(SGGeometry geometry) {
        this.mGeometry = geometry;
    }

    public void setLocalTransform(SGMatrix4f localTransform) {
        this.mLocalTransform = localTransform;
    }

    public void setName(String name) {
        this.mNodeName = name;
    }

    public void setSkeleton(SGBone skeleton) {
        this.mSkeleton = skeleton;
    }
}
