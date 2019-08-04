package com.samsung.android.sdk.sgi.render;

public class SGProperty {
    public static final String ALPHA_BLEND = "SGAlphaBlend";
    public static final String BITANGENTS = "SGBitangents";
    public static final String BONES = "SGBones";
    public static final String CAMERA_WORLD_POSITION = "SGCameraWorldPosition";
    public static final String CENTER = "SGCenter";
    public static final String COLOR = "SGColor";
    public static final String COLORS = "SGColors";
    public static final String DEPTH_TEXTURE = "SGDepthTexture";
    public static final String FILTER = "SGFilter";
    public static final String LAYER = "SGLayer";
    public static final String MATERIAL_SPECULAR_INTENSITY = "SGMaterialSpecularIntensity";
    public static final String MATERIAL_SPECULAR_POWER = "SGMaterialSpecularPower";
    public static final String NORMALS = "SGNormals";
    public static final String OPACITY = "SGOpacity";
    public static final String POSITIONS = "SGPositions";
    public static final String PROGRAM = "SGProgram";
    public static final String RATIO = "SGRatio";
    public static final String SHADOW_WIDTH = "SGShadowWidth";
    public static final String SHININESS = "SGShininess";
    public static final String SWIPE = "SGSwipe";
    public static final String TANGENTS = "SGTangents";
    public static final String TEXTURE = "SGTexture";
    public static final String TEXTURE_COORDS = "SGTextureCoords";
    public static final String TEXTURE_RECT = "SGTextureRect";
    public static final String TIME = "SGTime";
    public static final String VIEW = "SGView";
    public static final String VIEWPORT = "SGViewport";
    public static final String WEIGHTS = "SGWeights";
    public static final String WIDGET_RATIO = "SGWidgetRatio";
    public static final String WIZZLE = "SGWizzle";
    public static final String WORLD = "SGWorld";
    public static final String WORLD_INVERSE_TRANSPOSE = "SGWorldInverseTranspose";
    public static final String WORLD_SCALE = "SGWorldScale";
    public static final String WORLD_VIEW_INVERSE = "SGWorldViewInverse";
    public static final String WORLD_VIEW_PROJECTION = "SGWorldViewProjection";
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGProperty() {
        this(SGJNI.new_SGProperty(), true);
    }

    protected SGProperty(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGProperty obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGProperty) && ((SGProperty) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGProperty(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    protected long getHandle() {
        return SGJNI.SGProperty_getHandle(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }
}
