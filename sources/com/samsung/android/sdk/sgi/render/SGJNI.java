package com.samsung.android.sdk.sgi.render;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGMatrix2f;
import com.samsung.android.sdk.sgi.base.SGMatrix3f;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector3i;
import com.samsung.android.sdk.sgi.base.SGVector4f;
import com.samsung.android.sdk.sgi.base.SGVector4i;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

class SGJNI {
    private static Map<Class, Constructor> mCache = new HashMap();

    static {
        SGConfiguration.initLibrary();
        swig_module_init();
    }

    SGJNI() {
    }

    public static final native String PROPERTY_NAME_ALPHA_BLEND_get();

    public static final native String PROPERTY_NAME_BITANGENTS_get();

    public static final native String PROPERTY_NAME_BONES_get();

    public static final native String PROPERTY_NAME_CAMERA_WORLD_POSITION_get();

    public static final native String PROPERTY_NAME_CENTER_get();

    public static final native String PROPERTY_NAME_COLORS_get();

    public static final native String PROPERTY_NAME_COLOR_get();

    public static final native String PROPERTY_NAME_DEPTH_TEXTURE_get();

    public static final native String PROPERTY_NAME_FILTER_get();

    public static final native String PROPERTY_NAME_LAYER_get();

    public static final native String PROPERTY_NAME_MATERIAL_SPECULAR_INTENSITY_get();

    public static final native String PROPERTY_NAME_MATERIAL_SPECULAR_POWER_get();

    public static final native String PROPERTY_NAME_NORMALS_get();

    public static final native String PROPERTY_NAME_OPACITY_get();

    public static final native String PROPERTY_NAME_POSITIONS_get();

    public static final native String PROPERTY_NAME_PROGRAM_get();

    public static final native String PROPERTY_NAME_RATIO_get();

    public static final native String PROPERTY_NAME_SHADOW_WIDTH_get();

    public static final native String PROPERTY_NAME_SHININESS_get();

    public static final native String PROPERTY_NAME_SWIPE_get();

    public static final native String PROPERTY_NAME_TANGENTS_get();

    public static final native String PROPERTY_NAME_TEXTURE_COORDS_get();

    public static final native String PROPERTY_NAME_TEXTURE_RECT_get();

    public static final native String PROPERTY_NAME_TEXTURE_get();

    public static final native String PROPERTY_NAME_TIME_get();

    public static final native String PROPERTY_NAME_VIEW_PORT_get();

    public static final native String PROPERTY_NAME_VIEW_get();

    public static final native String PROPERTY_NAME_WEIGHTS_get();

    public static final native String PROPERTY_NAME_WIDGET_RATIO_get();

    public static final native String PROPERTY_NAME_WIZZLE_get();

    public static final native String PROPERTY_NAME_WORLD_INVERSE_TRANSPOSE_get();

    public static final native String PROPERTY_NAME_WORLD_SCALE_get();

    public static final native String PROPERTY_NAME_WORLD_VIEW_INVERSE_get();

    public static final native String PROPERTY_NAME_WORLD_VIEW_PROJECTION_get();

    public static final native String PROPERTY_NAME_WORLD_get();

    public static final native long SGAlphaBlendProperty_SWIGUpcast(long j);

    public static final native float[] SGAlphaBlendProperty_getBlendColor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getBlendEquationAlpha(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getBlendEquationColor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getDestinationFactor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getDestinationFactorAlpha(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getDestinationFactorColor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getSourceFactor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getSourceFactorAlpha(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native int SGAlphaBlendProperty_getSourceFactorColor(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native boolean SGAlphaBlendProperty_isAlphaBlendingEnabled(long j, SGAlphaBlendProperty sGAlphaBlendProperty);

    public static final native void SGAlphaBlendProperty_setAlphaBlendingEnabled(long j, SGAlphaBlendProperty sGAlphaBlendProperty, boolean z);

    public static final native void SGAlphaBlendProperty_setBlendColor(long j, SGAlphaBlendProperty sGAlphaBlendProperty, float[] fArr);

    public static final native void SGAlphaBlendProperty_setBlendEquation(long j, SGAlphaBlendProperty sGAlphaBlendProperty, int i, int i2);

    public static final native void SGAlphaBlendProperty_setFactors(long j, SGAlphaBlendProperty sGAlphaBlendProperty, int i, int i2);

    public static final native void SGAlphaBlendProperty_setFactorsSeparate(long j, SGAlphaBlendProperty sGAlphaBlendProperty, int i, int i2, int i3, int i4);

    public static final native long SGArrayProperty_SWIGUpcast(long j);

    public static final native int SGArrayProperty_getNumElements(long j, SGArrayProperty sGArrayProperty);

    public static final native void SGArrayProperty_invalidate(long j, SGArrayProperty sGArrayProperty);

    public static final native long SGBatchingGroupProperty_SWIGUpcast(long j);

    public static final native int SGBatchingGroupProperty_getId(long j, SGBatchingGroupProperty sGBatchingGroupProperty);

    public static final native boolean SGBatchingGroupProperty_isBatchingGroupEnabled(long j, SGBatchingGroupProperty sGBatchingGroupProperty);

    public static final native void SGBatchingGroupProperty_setBatchingGroupEnabled(long j, SGBatchingGroupProperty sGBatchingGroupProperty, boolean z);

    public static final native void SGBatchingGroupProperty_setId(long j, SGBatchingGroupProperty sGBatchingGroupProperty, int i);

    public static final native long SGBitmapTexture2DProperty_SWIGUpcast(long j);

    public static final native void SGBitmapTexture2DProperty_addPatch(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty, Bitmap bitmap, int[] iArr, int[] iArr2, boolean z);

    public static final native int SGBitmapTexture2DProperty_getHeight(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty);

    public static final native int SGBitmapTexture2DProperty_getWidth(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty);

    public static final native boolean SGBitmapTexture2DProperty_isGenerateMipmapsEnabled(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty);

    public static final native void SGBitmapTexture2DProperty_setBitmap(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty, Bitmap bitmap, boolean z);

    public static final native void SGBitmapTexture2DProperty_setGenerateMipmapsEnabled(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty, boolean z);

    public static final native void SGBitmapTexture2DProperty_setWrapType(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty, int i, int i2);

    public static final native long SGBoolProperty_SWIGUpcast(long j);

    public static final native boolean SGBoolProperty_get(long j, SGBoolProperty sGBoolProperty);

    public static final native void SGBoolProperty_set(long j, SGBoolProperty sGBoolProperty, boolean z);

    public static final native Object SGBuffer_getBuffer(long j, SGBuffer sGBuffer);

    public static final native int SGBuffer_getDataType(long j, SGBuffer sGBuffer);

    public static final native long SGBuffer_getHandle(long j, SGBuffer sGBuffer);

    public static final native int SGBuffer_getUsageType(long j, SGBuffer sGBuffer);

    public static final native void SGBuffer_invalidate__SWIG_0(long j, SGBuffer sGBuffer);

    public static final native void SGBuffer_invalidate__SWIG_1(long j, SGBuffer sGBuffer, int i);

    public static final native void SGBuffer_swap(long j, SGBuffer sGBuffer, long j2, SGBuffer sGBuffer2);

    public static final native float[] SGCamera_getBoundingBox(long j, SGCamera sGCamera);

    public static final native int SGCamera_getClearColor(long j, SGCamera sGCamera);

    public static final native float SGCamera_getDistance(long j, SGCamera sGCamera);

    public static final native long SGCamera_getHandle(long j, SGCamera sGCamera);

    public static final native long SGCamera_getObjectVisibilityMask(long j, SGCamera sGCamera);

    public static final native int SGCamera_getPriority(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getProjection(long j, SGCamera sGCamera);

    public static final native long SGCamera_getRenderTarget(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getScissorsPosition(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getScissorsSize(long j, SGCamera sGCamera);

    public static final native String SGCamera_getTechnicName(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getViewportPosition(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getViewportSize(long j, SGCamera sGCamera);

    public static final native float[] SGCamera_getWorldTransformation(long j, SGCamera sGCamera);

    public static final native boolean SGCamera_isClearColorEnabled(long j, SGCamera sGCamera);

    public static final native boolean SGCamera_isClearDepthEnabled(long j, SGCamera sGCamera);

    public static final native boolean SGCamera_isClearStencilEnabled(long j, SGCamera sGCamera);

    public static final native boolean SGCamera_isFrustumCullingEnabled(long j, SGCamera sGCamera);

    public static final native boolean SGCamera_isScissorsEnabled(long j, SGCamera sGCamera);

    public static final native void SGCamera_setClearColor(long j, SGCamera sGCamera, int i);

    public static final native void SGCamera_setClearColorEnabled(long j, SGCamera sGCamera, boolean z);

    public static final native void SGCamera_setClearDepth(long j, SGCamera sGCamera, boolean z);

    public static final native void SGCamera_setClearStencil(long j, SGCamera sGCamera, boolean z);

    public static final native void SGCamera_setFrustumCulling(long j, SGCamera sGCamera, boolean z);

    public static final native void SGCamera_setObjectVisibilityMask(long j, SGCamera sGCamera, long j2);

    public static final native void SGCamera_setPriority(long j, SGCamera sGCamera, int i);

    public static final native void SGCamera_setProjection(long j, SGCamera sGCamera, float[] fArr);

    public static final native void SGCamera_setRenderTarget(long j, SGCamera sGCamera, long j2, SGRenderTarget sGRenderTarget);

    public static final native void SGCamera_setScissors(long j, SGCamera sGCamera, boolean z);

    public static final native void SGCamera_setScissorsRect(long j, SGCamera sGCamera, float[] fArr, float[] fArr2);

    public static final native void SGCamera_setTechnicName(long j, SGCamera sGCamera, String str);

    public static final native void SGCamera_setViewport(long j, SGCamera sGCamera, float[] fArr, float[] fArr2);

    public static final native void SGCamera_setWorldTransformation(long j, SGCamera sGCamera, float[] fArr);

    public static final native long SGColorMaskProperty_SWIGUpcast(long j);

    public static final native boolean SGColorMaskProperty_getWriteStateA(long j, SGColorMaskProperty sGColorMaskProperty);

    public static final native boolean SGColorMaskProperty_getWriteStateB(long j, SGColorMaskProperty sGColorMaskProperty);

    public static final native boolean SGColorMaskProperty_getWriteStateG(long j, SGColorMaskProperty sGColorMaskProperty);

    public static final native boolean SGColorMaskProperty_getWriteStateR(long j, SGColorMaskProperty sGColorMaskProperty);

    public static final native boolean SGColorMaskProperty_isColorMaskingEnabled(long j, SGColorMaskProperty sGColorMaskProperty);

    public static final native void SGColorMaskProperty_setColorMaskingEnabled(long j, SGColorMaskProperty sGColorMaskProperty, boolean z);

    public static final native void SGColorMaskProperty_setWriteState(long j, SGColorMaskProperty sGColorMaskProperty, boolean z, boolean z2, boolean z3, boolean z4);

    public static final native void SGCompositeVertexBufferBuilder_exportBuffers(long j, SGCompositeVertexBufferBuilder sGCompositeVertexBufferBuilder, long j2, SGGeometry sGGeometry);

    public static final native long SGCompositeVertexBufferBuilder_getBuffer(long j, SGCompositeVertexBufferBuilder sGCompositeVertexBufferBuilder, int i);

    public static final native String SGCompositeVertexBufferBuilder_getBufferName(long j, SGCompositeVertexBufferBuilder sGCompositeVertexBufferBuilder, int i);

    public static final native int SGCompositeVertexBufferBuilder_getBuffersCount(long j, SGCompositeVertexBufferBuilder sGCompositeVertexBufferBuilder);

    public static final native long SGCompositeVertexBufferBuilder_getCompositeVertexBuffer(long j, SGCompositeVertexBufferBuilder sGCompositeVertexBufferBuilder);

    public static final native long SGCompositeVertexBuffer_SWIGUpcast(long j);

    public static final native long SGCompressedTextureFactory_createTextureAsset(AssetInputStream assetInputStream);

    public static final native long SGCompressedTextureFactory_createTexture__SWIG_0(AssetManager assetManager, String str);

    public static final native long SGCompressedTextureFactory_createTexture__SWIG_1(FileInputStream fileInputStream);

    public static final native long SGCubeMapTextureProperty_SWIGUpcast(long j);

    public static final native int SGCubeMapTextureProperty_getDataFormat(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, int i);

    public static final native int SGCubeMapTextureProperty_getDataType(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, int i);

    public static final native int SGCubeMapTextureProperty_getInternalFormat(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, int i);

    public static final native int SGCubeMapTextureProperty_getNumMipmaps(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty);

    public static final native int SGCubeMapTextureProperty_getSize(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, int i);

    public static final native boolean SGCubeMapTextureProperty_isGenerateMipmapsEnabled(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty);

    public static final native void SGCubeMapTextureProperty_setBitmaps(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, Bitmap bitmap5, Bitmap bitmap6, boolean z);

    public static final native void SGCubeMapTextureProperty_setGenerateMipmapsEnabled(long j, SGCubeMapTextureProperty sGCubeMapTextureProperty, boolean z);

    public static final native long SGCullFaceProperty_SWIGUpcast(long j);

    public static final native int SGCullFaceProperty_getCullType(long j, SGCullFaceProperty sGCullFaceProperty);

    public static final native int SGCullFaceProperty_getFrontType(long j, SGCullFaceProperty sGCullFaceProperty);

    public static final native boolean SGCullFaceProperty_isFaceCullingEnabled(long j, SGCullFaceProperty sGCullFaceProperty);

    public static final native void SGCullFaceProperty_setCullType(long j, SGCullFaceProperty sGCullFaceProperty, int i);

    public static final native void SGCullFaceProperty_setFaceCullingEnabled(long j, SGCullFaceProperty sGCullFaceProperty, boolean z);

    public static final native void SGCullFaceProperty_setFrontType(long j, SGCullFaceProperty sGCullFaceProperty, int i);

    public static final native long SGDepthProperty_SWIGUpcast(long j);

    public static final native boolean SGDepthProperty_isDepthTestEnabled(long j, SGDepthProperty sGDepthProperty);

    public static final native boolean SGDepthProperty_isWriteEnabled(long j, SGDepthProperty sGDepthProperty);

    public static final native void SGDepthProperty_setDepthTestEnabled(long j, SGDepthProperty sGDepthProperty, boolean z);

    public static final native void SGDepthProperty_setWriteEnabled(long j, SGDepthProperty sGDepthProperty, boolean z);

    public static final native long SGDiscardRasterizerProperty_SWIGUpcast(long j);

    public static final native boolean SGDiscardRasterizerProperty_isDiscardRasterizerEnabled(long j, SGDiscardRasterizerProperty sGDiscardRasterizerProperty);

    public static final native void SGDiscardRasterizerProperty_setDiscardRasterizerEnabled(long j, SGDiscardRasterizerProperty sGDiscardRasterizerProperty, boolean z);

    public static final native long SGDitherProperty_SWIGUpcast(long j);

    public static final native boolean SGDitherProperty_isDitherEnabled(long j, SGDitherProperty sGDitherProperty);

    public static final native void SGDitherProperty_setDitherEnabled(long j, SGDitherProperty sGDitherProperty, boolean z);

    public static final native long SGFloatArrayProperty_SWIGUpcast(long j);

    public static final native float[] SGFloatArrayProperty_get(long j, SGFloatArrayProperty sGFloatArrayProperty);

    public static final native Object SGFloatArrayProperty_getBuffer(long j, SGFloatArrayProperty sGFloatArrayProperty);

    public static final native void SGFloatArrayProperty_set(long j, SGFloatArrayProperty sGFloatArrayProperty, float[] fArr);

    public static final native void SGFloatArrayProperty_setSize(long j, SGFloatArrayProperty sGFloatArrayProperty, int i);

    public static final native long SGFloatProperty_SWIGUpcast(long j);

    public static final native float SGFloatProperty_get(long j, SGFloatProperty sGFloatProperty);

    public static final native void SGFloatProperty_set(long j, SGFloatProperty sGFloatProperty, float f);

    public static final native void SGFontManagerTTF_loadFontDataAsset(AssetInputStream assetInputStream, String str);

    public static final native void SGFontManagerTTF_loadFontData__SWIG_0(AssetManager assetManager, String str, String str2);

    public static final native void SGFontManagerTTF_loadFontData__SWIG_1(FileInputStream fileInputStream, String str);

    public static final native void SGGeometry_addBuffer(long j, SGGeometry sGGeometry, String str, long j2, SGBuffer sGBuffer);

    public static final native float[] SGGeometry_getBoundingBox(long j, SGGeometry sGGeometry);

    public static final native String SGGeometry_getBufferName(long j, SGGeometry sGGeometry, int i);

    public static final native int SGGeometry_getBuffersCount(long j, SGGeometry sGGeometry);

    public static final native long SGGeometry_getHandle(long j, SGGeometry sGGeometry);

    public static final native long SGGeometry_getIndexBuffer(long j, SGGeometry sGGeometry);

    public static final native int SGGeometry_getPrimitiveType(long j, SGGeometry sGGeometry);

    public static final native long SGGeometry_getVertexBuffer__SWIG_0(long j, SGGeometry sGGeometry, String str);

    public static final native long SGGeometry_getVertexBuffer__SWIG_1(long j, SGGeometry sGGeometry, int i);

    public static final native void SGGeometry_invalidate__SWIG_0(long j, SGGeometry sGGeometry);

    public static final native void SGGeometry_invalidate__SWIG_1(long j, SGGeometry sGGeometry, int i);

    public static final native void SGGeometry_setBoundingBox(long j, SGGeometry sGGeometry, float[] fArr);

    public static final native long SGIndexBuffer_SWIGUpcast(long j);

    public static final native int SGIndexBuffer_getPrimitiveType(long j, SGIndexBuffer sGIndexBuffer);

    public static final native void SGIndexBuffer_setSize(long j, SGIndexBuffer sGIndexBuffer, int i);

    public static final native long SGIntArrayProperty_SWIGUpcast(long j);

    public static final native int[] SGIntArrayProperty_get(long j, SGIntArrayProperty sGIntArrayProperty);

    public static final native ByteBuffer SGIntArrayProperty_getIntBuffer(long j, SGIntArrayProperty sGIntArrayProperty);

    public static final native void SGIntArrayProperty_set(long j, SGIntArrayProperty sGIntArrayProperty, int[] iArr);

    public static final native void SGIntArrayProperty_setSize(long j, SGIntArrayProperty sGIntArrayProperty, int i);

    public static final native long SGIntProperty_SWIGUpcast(long j);

    public static final native int SGIntProperty_get(long j, SGIntProperty sGIntProperty);

    public static final native void SGIntProperty_set(long j, SGIntProperty sGIntProperty, int i);

    public static final native long SGLineWidthProperty_SWIGUpcast(long j);

    public static final native float SGLineWidthProperty_getWidth(long j, SGLineWidthProperty sGLineWidthProperty);

    public static final native boolean SGLineWidthProperty_isLineWidthPropertyEnabled(long j, SGLineWidthProperty sGLineWidthProperty);

    public static final native void SGLineWidthProperty_setLineWidthPropertyEnabled(long j, SGLineWidthProperty sGLineWidthProperty, boolean z);

    public static final native void SGLineWidthProperty_setWidth(long j, SGLineWidthProperty sGLineWidthProperty, float f);

    public static final native long SGMaterial_getHandle(long j, SGMaterial sGMaterial);

    public static final native int SGMaterial_getId(long j, SGMaterial sGMaterial);

    public static final native SGShaderProgramProperty SGMaterial_getProgramProperty(long j, SGMaterial sGMaterial);

    public static final native int SGMaterial_getPropertyCount(long j, SGMaterial sGMaterial);

    public static final native String SGMaterial_getPropertyName(long j, SGMaterial sGMaterial, int i);

    public static final native SGProperty SGMaterial_getProperty__SWIG_0(long j, SGMaterial sGMaterial, String str);

    public static final native SGProperty SGMaterial_getProperty__SWIG_1(long j, SGMaterial sGMaterial, int i);

    public static final native String SGMaterial_getTechnicName(long j, SGMaterial sGMaterial);

    public static final native void SGMaterial_removeProperty__SWIG_0(long j, SGMaterial sGMaterial, String str);

    public static final native void SGMaterial_removeProperty__SWIG_1(long j, SGMaterial sGMaterial, int i);

    public static final native void SGMaterial_setProgramProperty(long j, SGMaterial sGMaterial, long j2, SGShaderProgramProperty sGShaderProgramProperty);

    public static final native void SGMaterial_setProperty(long j, SGMaterial sGMaterial, String str, long j2, SGProperty sGProperty);

    public static final native void SGMaterial_setTechnicName(long j, SGMaterial sGMaterial, String str);

    public static final native float[] SGMatrix4fProperty_get(long j, SGMatrix4fProperty sGMatrix4fProperty);

    public static final native long SGMatrixfArrayProperty_SWIGUpcast(long j);

    public static final native float[] SGMatrixfArrayProperty_get(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty);

    public static final native int SGMatrixfArrayProperty_getDimension(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty);

    public static final native void SGMatrixfArrayProperty_set__SWIG_0(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty, float[] fArr);

    public static final native void SGMatrixfArrayProperty_set__SWIG_1(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty, SGMatrix4f[] sGMatrix4fArr);

    public static final native void SGMatrixfArrayProperty_set__SWIG_2(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty, SGMatrix3f[] sGMatrix3fArr);

    public static final native void SGMatrixfArrayProperty_set__SWIG_3(long j, SGMatrixfArrayProperty sGMatrixfArrayProperty, SGMatrix2f[] sGMatrix2fArr);

    public static final native long SGMatrixfProperty_SWIGUpcast(long j);

    public static final native int SGMatrixfProperty_getDimension(long j, SGMatrixfProperty sGMatrixfProperty);

    public static final native void SGMatrixfProperty_set(long j, SGMatrixfProperty sGMatrixfProperty, float[] fArr);

    public static final native float[] SGMatrixfProperty_toFloatArray(long j, SGMatrixfProperty sGMatrixfProperty);

    public static final native long SGPartVertexBuffer_SWIGUpcast(long j);

    public static final native int SGPartVertexBuffer_getComponentsPerElement(long j, SGPartVertexBuffer sGPartVertexBuffer);

    public static final native long SGPartVertexBuffer_getCompositeBuffer(long j, SGPartVertexBuffer sGPartVertexBuffer);

    public static final native int SGPartVertexBuffer_getOffset(long j, SGPartVertexBuffer sGPartVertexBuffer);

    public static final native int SGPartVertexBuffer_getStride(long j, SGPartVertexBuffer sGPartVertexBuffer);

    public static final native long SGPreMultiplyProperty_SWIGUpcast(long j);

    public static final native boolean SGPreMultiplyProperty_isPremultiplyEnabled(long j, SGPreMultiplyProperty sGPreMultiplyProperty);

    public static final native void SGPreMultiplyProperty_setPremultiplyEnabled(long j, SGPreMultiplyProperty sGPreMultiplyProperty, boolean z);

    public static final native long SGProperty_getHandle(long j, SGProperty sGProperty);

    public static final native long SGQuaternionArrayProperty_SWIGUpcast(long j);

    public static final native SGQuaternion[] SGQuaternionArrayProperty_get(long j, SGQuaternionArrayProperty sGQuaternionArrayProperty);

    public static final native void SGQuaternionArrayProperty_set(long j, SGQuaternionArrayProperty sGQuaternionArrayProperty, SGQuaternion[] sGQuaternionArr);

    public static final native long SGQuaternionProperty_SWIGUpcast(long j);

    public static final native float[] SGQuaternionProperty_get(long j, SGQuaternionProperty sGQuaternionProperty);

    public static final native void SGQuaternionProperty_set__SWIG_0(long j, SGQuaternionProperty sGQuaternionProperty, float[] fArr);

    public static final native void SGQuaternionProperty_set__SWIG_1(long j, SGQuaternionProperty sGQuaternionProperty, float f, float f2, float f3, float f4);

    public static final native int SGRenderBuffer_getInternalFormat(long j, SGRenderBuffer sGRenderBuffer);

    public static final native void SGRenderDataProvider_change_ownership(SGRenderDataProvider sGRenderDataProvider, long j, boolean z);

    public static final native void SGRenderDataProvider_director_connect(SGRenderDataProvider sGRenderDataProvider, long j, boolean z, boolean z2);

    public static final native byte[] SGRenderDataProvider_loadBuiltinShaderData(String str);

    public static final native byte[] SGRenderDataProvider_loadProgram(long j, SGRenderDataProvider sGRenderDataProvider, String str, String str2);

    public static final native byte[] SGRenderDataProvider_loadShaderData(long j, SGRenderDataProvider sGRenderDataProvider, String str);

    public static final native void SGRenderDataProvider_saveProgram(long j, SGRenderDataProvider sGRenderDataProvider, String str, String str2, byte[] bArr);

    public static final native void SGRenderInterface_change_ownership(SGRenderInterface sGRenderInterface, long j, boolean z);

    public static final native void SGRenderInterface_director_connect(SGRenderInterface sGRenderInterface, long j, boolean z, boolean z2);

    public static final native void SGRenderInterface_draw(long j, SGRenderInterface sGRenderInterface);

    public static final native void SGRenderInterface_drawSwigExplicitSGRenderInterface(long j, SGRenderInterface sGRenderInterface);

    public static final native void SGRenderInterface_init(long j, SGRenderInterface sGRenderInterface);

    public static final native void SGRenderInterface_initSwigExplicitSGRenderInterface(long j, SGRenderInterface sGRenderInterface);

    public static final native boolean SGRenderInterface_needRedraw(long j, SGRenderInterface sGRenderInterface);

    public static final native boolean SGRenderInterface_needRedrawSwigExplicitSGRenderInterface(long j, SGRenderInterface sGRenderInterface);

    public static final native void SGRenderInterface_release(long j, SGRenderInterface sGRenderInterface);

    public static final native void SGRenderInterface_releaseSwigExplicitSGRenderInterface(long j, SGRenderInterface sGRenderInterface);

    public static final native long SGRenderTargetToTexture_SWIGUpcast(long j);

    public static final native void SGRenderTarget_attachRenderBuffer(long j, SGRenderTarget sGRenderTarget, int i, long j2, SGRenderBuffer sGRenderBuffer);

    public static final native void SGRenderTarget_attachTexture2D(long j, SGRenderTarget sGRenderTarget, int i, long j2, SGTexture2DAttachmentProperty sGTexture2DAttachmentProperty, int i2);

    public static final native void SGRenderTarget_detach(long j, SGRenderTarget sGRenderTarget, int i);

    public static final native int SGRenderTarget_getAntiAliasingType(long j, SGRenderTarget sGRenderTarget);

    public static final native long SGRenderTarget_getHandle(long j, SGRenderTarget sGRenderTarget);

    public static final native int SGRenderTarget_getHeight(long j, SGRenderTarget sGRenderTarget);

    public static final native int SGRenderTarget_getWidth(long j, SGRenderTarget sGRenderTarget);

    public static final native void SGRenderTarget_setAntiAliasingType(long j, SGRenderTarget sGRenderTarget, int i);

    public static final native void SGRenderTarget_setSize(long j, SGRenderTarget sGRenderTarget, int i, int i2);

    public static final native long SGResourceShaderProperty_SWIGUpcast(long j);

    public static final native long SGShaderProgramProperty_SWIGUpcast(long j);

    public static final native SGShaderProperty SGShaderProgramProperty_getShader(long j, SGShaderProgramProperty sGShaderProgramProperty, int i);

    public static final native long SGShaderProperty_SWIGUpcast(long j);

    public static final native int SGShaderProperty_getDataType(long j, SGShaderProperty sGShaderProperty);

    public static final native int SGShaderProperty_getShaderType(long j, SGShaderProperty sGShaderProperty);

    public static final native long SGSharedTextureProperty_SWIGUpcast(long j);

    public static final native void SGSharedTextureProperty_addPatch(long j, SGSharedTextureProperty sGSharedTextureProperty, Bitmap bitmap, int[] iArr, int[] iArr2, boolean z);

    public static final native long SGSharedTextureProperty_getBuffer(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native ByteBuffer SGSharedTextureProperty_getBufferAndLock(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getFormat(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getHeight(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getSize(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getSpace(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getStride(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native int SGSharedTextureProperty_getWidth(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native long SGSharedTextureProperty_lockNative(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native void SGSharedTextureProperty_textureUpdated(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native void SGSharedTextureProperty_unlockNative(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native long SGStencilProperty_SWIGUpcast(long j);

    public static final native int SGStencilProperty_getFunction(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getGlobalMask(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getMask(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getOperationDepthFail(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getOperationDepthPass(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getOperationStencilFail(long j, SGStencilProperty sGStencilProperty);

    public static final native int SGStencilProperty_getReference(long j, SGStencilProperty sGStencilProperty);

    public static final native boolean SGStencilProperty_isStencilPropertyEnabled(long j, SGStencilProperty sGStencilProperty);

    public static final native void SGStencilProperty_setFunction(long j, SGStencilProperty sGStencilProperty, int i, int i2, int i3);

    public static final native void SGStencilProperty_setGlobalMask(long j, SGStencilProperty sGStencilProperty, int i);

    public static final native void SGStencilProperty_setStencilOperation(long j, SGStencilProperty sGStencilProperty, int i, int i2, int i3);

    public static final native void SGStencilProperty_setStencilPropertyEnabled(long j, SGStencilProperty sGStencilProperty, boolean z);

    public static final native long SGStringShaderProperty_SWIGUpcast(long j);

    public static final native void SGSurfaceRendererBase_change_ownership(SGSurfaceRendererBase sGSurfaceRendererBase, long j, boolean z);

    public static final native void SGSurfaceRendererBase_director_connect(SGSurfaceRendererBase sGSurfaceRendererBase, long j, boolean z, boolean z2);

    public static final native void SGSurfaceRendererBase_onCreateTexture(long j, SGSurfaceRendererBase sGSurfaceRendererBase, int i);

    public static final native void SGSurfaceRendererBase_onDestroyTexture(long j, SGSurfaceRendererBase sGSurfaceRendererBase);

    public static final native void SGSurfaceRendererBase_onDraw(long j, SGSurfaceRendererBase sGSurfaceRendererBase, int i);

    public static final native long SGTexture2DAttachmentProperty_SWIGUpcast(long j);

    public static final native long SGTexture2DAttachmentProperty_getExternalId(long j, SGTexture2DAttachmentProperty sGTexture2DAttachmentProperty);

    public static final native boolean SGTexture2DAttachmentProperty_isGenerateMipmapsEnabled(long j, SGTexture2DAttachmentProperty sGTexture2DAttachmentProperty);

    public static final native void SGTexture2DAttachmentProperty_setExternalId(long j, SGTexture2DAttachmentProperty sGTexture2DAttachmentProperty, long j2);

    public static final native void SGTexture2DAttachmentProperty_setGenerateMipmapsEnabled(long j, SGTexture2DAttachmentProperty sGTexture2DAttachmentProperty, boolean z);

    public static final native boolean SGTexture2DVideoPropertyWeakRef_textureUpdated(long j, SGTexture2DVideoPropertyWeakRef sGTexture2DVideoPropertyWeakRef);

    public static final native boolean SGTexture2DVideoPropertyWeakRef_updateTextureMatrix(long j, SGTexture2DVideoPropertyWeakRef sGTexture2DVideoPropertyWeakRef, float[] fArr);

    public static final native long SGTexture2DVideoProperty_SWIGUpcast(long j);

    public static final native void SGTexture2DVideoProperty_setUpdater(long j, SGTexture2DVideoProperty sGTexture2DVideoProperty, long j2);

    public static final native void SGTexture2DVideoProperty_textureUpdated(long j, SGTexture2DVideoProperty sGTexture2DVideoProperty);

    public static final native void SGTexture2DVideoProperty_updateTextureMatrix(long j, SGTexture2DVideoProperty sGTexture2DVideoProperty, float[] fArr);

    public static final native boolean SGTextureAtlas_addPatch(long j, SGTextureAtlas sGTextureAtlas, long j2, Bitmap bitmap, int[] iArr, boolean z);

    public static final native long SGTextureAtlas_addRect__SWIG_0(long j, SGTextureAtlas sGTextureAtlas, int i, int i2);

    public static final native long SGTextureAtlas_addRect__SWIG_1(long j, SGTextureAtlas sGTextureAtlas, Bitmap bitmap, boolean z);

    public static final native void SGTextureAtlas_clear(long j, SGTextureAtlas sGTextureAtlas);

    public static final native int SGTextureAtlas_getHeight(long j, SGTextureAtlas sGTextureAtlas);

    public static final native int[] SGTextureAtlas_getRect(long j, SGTextureAtlas sGTextureAtlas, long j2);

    public static final native SGTextureProperty SGTextureAtlas_getTexture(long j, SGTextureAtlas sGTextureAtlas);

    public static final native float[] SGTextureAtlas_getTextureCoords(long j, SGTextureAtlas sGTextureAtlas, long j2);

    public static final native int SGTextureAtlas_getWidth(long j, SGTextureAtlas sGTextureAtlas);

    public static final native boolean SGTextureAtlas_isEmpty(long j, SGTextureAtlas sGTextureAtlas);

    public static final native boolean SGTextureAtlas_isFilled(long j, SGTextureAtlas sGTextureAtlas);

    public static final native boolean SGTextureAtlas_removeRect(long j, SGTextureAtlas sGTextureAtlas, long j2);

    public static final native long SGTextureProperty_SWIGUpcast(long j);

    public static final native int SGTextureProperty_getDataFormat(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getDataType(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getInternalFormat(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getMagFilter(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getMinFilter(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getWrapR(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getWrapS(long j, SGTextureProperty sGTextureProperty);

    public static final native int SGTextureProperty_getWrapT(long j, SGTextureProperty sGTextureProperty);

    public static final native void SGTextureProperty_setMagFilter(long j, SGTextureProperty sGTextureProperty, int i);

    public static final native void SGTextureProperty_setMinFilter(long j, SGTextureProperty sGTextureProperty, int i);

    public static final native long SGTransformFeedbackProgramProperty_SWIGUpcast(long j);

    public static final native String[] SGTransformFeedbackProgramProperty_getOutputAttrsList(long j, SGTransformFeedbackProgramProperty sGTransformFeedbackProgramProperty);

    public static final native SGShaderProperty SGTransformFeedbackProgramProperty_getShader(long j, SGTransformFeedbackProgramProperty sGTransformFeedbackProgramProperty, int i);

    public static final native long SGULongProperty_SWIGUpcast(long j);

    public static final native long SGULongProperty_get(long j, SGULongProperty sGULongProperty);

    public static final native void SGULongProperty_set(long j, SGULongProperty sGULongProperty, long j2);

    public static final native float[] SGVector2fProperty_get(long j, SGVector2fProperty sGVector2fProperty);

    public static final native long SGVector3fArrayProperty_SWIGUpcast(long j);

    public static final native SGVector3f[] SGVector3fArrayProperty_get(long j, SGVector3fArrayProperty sGVector3fArrayProperty);

    public static final native void SGVector3fArrayProperty_set(long j, SGVector3fArrayProperty sGVector3fArrayProperty, SGVector3f[] sGVector3fArr);

    public static final native float[] SGVector3fProperty_get(long j, SGVector3fProperty sGVector3fProperty);

    public static final native float[] SGVector4fProperty_get(long j, SGVector4fProperty sGVector4fProperty);

    public static final native long SGVectorfArrayProperty_SWIGUpcast(long j);

    public static final native float[] SGVectorfArrayProperty_get(long j, SGVectorfArrayProperty sGVectorfArrayProperty);

    public static final native int SGVectorfArrayProperty_getDimension(long j, SGVectorfArrayProperty sGVectorfArrayProperty);

    public static final native void SGVectorfArrayProperty_set__SWIG_0(long j, SGVectorfArrayProperty sGVectorfArrayProperty, float[] fArr);

    public static final native void SGVectorfArrayProperty_set__SWIG_1(long j, SGVectorfArrayProperty sGVectorfArrayProperty, SGVector4f[] sGVector4fArr);

    public static final native void SGVectorfArrayProperty_set__SWIG_2(long j, SGVectorfArrayProperty sGVectorfArrayProperty, SGVector3f[] sGVector3fArr);

    public static final native void SGVectorfArrayProperty_set__SWIG_3(long j, SGVectorfArrayProperty sGVectorfArrayProperty, SGVector2f[] sGVector2fArr);

    public static final native long SGVectorfProperty_SWIGUpcast(long j);

    public static final native int SGVectorfProperty_getDimension(long j, SGVectorfProperty sGVectorfProperty);

    public static final native void SGVectorfProperty_set__SWIG_0(long j, SGVectorfProperty sGVectorfProperty, float[] fArr);

    public static final native void SGVectorfProperty_set__SWIG_1(long j, SGVectorfProperty sGVectorfProperty, float f, float f2, float f3, float f4);

    public static final native void SGVectorfProperty_set__SWIG_2(long j, SGVectorfProperty sGVectorfProperty, float[] fArr);

    public static final native void SGVectorfProperty_set__SWIG_3(long j, SGVectorfProperty sGVectorfProperty, float f, float f2, float f3);

    public static final native void SGVectorfProperty_set__SWIG_4(long j, SGVectorfProperty sGVectorfProperty, float[] fArr);

    public static final native void SGVectorfProperty_set__SWIG_5(long j, SGVectorfProperty sGVectorfProperty, float f, float f2);

    public static final native void SGVectorfProperty_set__SWIG_6(long j, SGVectorfProperty sGVectorfProperty, float[] fArr);

    public static final native float[] SGVectorfProperty_toFloatArray(long j, SGVectorfProperty sGVectorfProperty);

    public static final native long SGVectoriArrayProperty_SWIGUpcast(long j);

    public static final native int[] SGVectoriArrayProperty_get(long j, SGVectoriArrayProperty sGVectoriArrayProperty);

    public static final native int SGVectoriArrayProperty_getDimension(long j, SGVectoriArrayProperty sGVectoriArrayProperty);

    public static final native void SGVectoriArrayProperty_set__SWIG_0(long j, SGVectoriArrayProperty sGVectoriArrayProperty, int[] iArr);

    public static final native void SGVectoriArrayProperty_set__SWIG_1(long j, SGVectoriArrayProperty sGVectoriArrayProperty, SGVector4i[] sGVector4iArr);

    public static final native void SGVectoriArrayProperty_set__SWIG_2(long j, SGVectoriArrayProperty sGVectoriArrayProperty, SGVector3i[] sGVector3iArr);

    public static final native void SGVectoriArrayProperty_set__SWIG_3(long j, SGVectoriArrayProperty sGVectoriArrayProperty, SGVector2i[] sGVector2iArr);

    public static final native long SGVectoriProperty_SWIGUpcast(long j);

    public static final native int SGVectoriProperty_getDimension(long j, SGVectoriProperty sGVectoriProperty);

    public static final native void SGVectoriProperty_set__SWIG_0(long j, SGVectoriProperty sGVectoriProperty, int[] iArr);

    public static final native void SGVectoriProperty_set__SWIG_1(long j, SGVectoriProperty sGVectoriProperty, int i, int i2, int i3, int i4);

    public static final native void SGVectoriProperty_set__SWIG_2(long j, SGVectoriProperty sGVectoriProperty, int[] iArr);

    public static final native void SGVectoriProperty_set__SWIG_3(long j, SGVectoriProperty sGVectoriProperty, int i, int i2, int i3);

    public static final native void SGVectoriProperty_set__SWIG_4(long j, SGVectoriProperty sGVectoriProperty, int[] iArr);

    public static final native void SGVectoriProperty_set__SWIG_5(long j, SGVectoriProperty sGVectoriProperty, int i, int i2);

    public static final native void SGVectoriProperty_set__SWIG_6(long j, SGVectoriProperty sGVectoriProperty, int[] iArr);

    public static final native int[] SGVectoriProperty_toIntArray(long j, SGVectoriProperty sGVectoriProperty);

    public static final native long SGVertexBuffer_SWIGUpcast(long j);

    public static final native int SGVertexBuffer_getComponentsPerElement(long j, SGVertexBuffer sGVertexBuffer);

    public static final native int SGVertexBuffer_getVertexCount(long j, SGVertexBuffer sGVertexBuffer);

    public static final native void SGVertexBuffer_setVertexCount(long j, SGVertexBuffer sGVertexBuffer, int i);

    public static byte[] SwigDirector_SGRenderDataProvider_loadProgram(SGRenderDataProvider jself, String vertexShaderName, String fragmentShaderName) {
        return jself.loadProgram(vertexShaderName, fragmentShaderName);
    }

    public static byte[] SwigDirector_SGRenderDataProvider_loadShaderData(SGRenderDataProvider jself, String name) {
        return jself.loadShaderData(name);
    }

    public static void SwigDirector_SGRenderDataProvider_saveProgram(SGRenderDataProvider jself, String vertexShaderName, String fragmentShaderName, byte[] program) {
        jself.saveProgram(vertexShaderName, fragmentShaderName, program);
    }

    public static void SwigDirector_SGRenderInterface_draw(SGRenderInterface jself) {
        jself.draw();
    }

    public static void SwigDirector_SGRenderInterface_init(SGRenderInterface jself) {
        jself.init();
    }

    public static boolean SwigDirector_SGRenderInterface_needRedraw(SGRenderInterface jself) {
        return jself.needRedraw();
    }

    public static void SwigDirector_SGRenderInterface_release(SGRenderInterface jself) {
        jself.release();
    }

    public static void SwigDirector_SGSurfaceRendererBase_onCreateTexture(SGSurfaceRendererBase jself, int id) {
        jself.onCreateTexture(id);
    }

    public static void SwigDirector_SGSurfaceRendererBase_onDestroyTexture(SGSurfaceRendererBase jself) {
        jself.onDestroyTexture();
    }

    public static void SwigDirector_SGSurfaceRendererBase_onDraw(SGSurfaceRendererBase jself, int id) {
        jself.onDraw(id);
    }

    static synchronized Object createObjectFromNativePtr(Class objClass, long cPtr, boolean cMemoryOwn) {
        Object ret;
        synchronized (SGJNI.class) {
            ret = null;
            Constructor<?> swigCtr = (Constructor) mCache.get(objClass);
            if (swigCtr == null) {
                try {
                    swigCtr = objClass.getDeclaredConstructor(new Class[]{Long.TYPE, Boolean.TYPE});
                    if (swigCtr != null) {
                        swigCtr.setAccessible(true);
                        mCache.put(objClass, swigCtr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (swigCtr != null) {
                ret = swigCtr.newInstance(new Object[]{Long.valueOf(cPtr), Boolean.valueOf(cMemoryOwn)});
            }
        }
        return ret;
    }

    public static final native void delete_SGAlphaBlendProperty(long j);

    public static final native void delete_SGArrayProperty(long j);

    public static final native void delete_SGBatchingGroupProperty(long j);

    public static final native void delete_SGBitmapTexture2DProperty(long j);

    public static final native void delete_SGBoolProperty(long j);

    public static final native void delete_SGBuffer(long j);

    public static final native void delete_SGCamera(long j);

    public static final native void delete_SGColorMaskProperty(long j);

    public static final native void delete_SGCompositeVertexBuffer(long j);

    public static final native void delete_SGCompositeVertexBufferBuilder(long j);

    public static final native void delete_SGCompressedTextureFactory(long j);

    public static final native void delete_SGCubeMapTextureProperty(long j);

    public static final native void delete_SGCullFaceProperty(long j);

    public static final native void delete_SGDepthProperty(long j);

    public static final native void delete_SGDiscardRasterizerProperty(long j);

    public static final native void delete_SGDitherProperty(long j);

    public static final native void delete_SGFloatArrayProperty(long j);

    public static final native void delete_SGFloatProperty(long j);

    public static final native void delete_SGFontManagerTTF(long j);

    public static final native void delete_SGGeometry(long j);

    public static final native void delete_SGIndexBuffer(long j);

    public static final native void delete_SGIntArrayProperty(long j);

    public static final native void delete_SGIntProperty(long j);

    public static final native void delete_SGLineWidthProperty(long j);

    public static final native void delete_SGMaterial(long j);

    public static final native void delete_SGMatrix4fProperty(long j);

    public static final native void delete_SGMatrixfArrayProperty(long j);

    public static final native void delete_SGMatrixfProperty(long j);

    public static final native void delete_SGPartVertexBuffer(long j);

    public static final native void delete_SGPreMultiplyProperty(long j);

    public static final native void delete_SGProperty(long j);

    public static final native void delete_SGQuaternionArrayProperty(long j);

    public static final native void delete_SGQuaternionProperty(long j);

    public static final native void delete_SGRenderBuffer(long j);

    public static final native void delete_SGRenderDataProvider(long j);

    public static final native void delete_SGRenderInterface(long j);

    public static final native void delete_SGRenderTarget(long j);

    public static final native void delete_SGRenderTargetToTexture(long j);

    public static final native void delete_SGResourceShaderProperty(long j);

    public static final native void delete_SGShaderProgramProperty(long j);

    public static final native void delete_SGShaderProperty(long j);

    public static final native void delete_SGSharedTextureProperty(long j);

    public static final native void delete_SGStencilProperty(long j);

    public static final native void delete_SGStringShaderProperty(long j);

    public static final native void delete_SGSurfaceRendererBase(long j);

    public static final native void delete_SGTexture2DAttachmentProperty(long j);

    public static final native void delete_SGTexture2DVideoProperty(long j);

    public static final native void delete_SGTexture2DVideoPropertyWeakRef(long j);

    public static final native void delete_SGTextureAtlas(long j);

    public static final native void delete_SGTextureProperty(long j);

    public static final native void delete_SGTransformFeedbackProgramProperty(long j);

    public static final native void delete_SGULongProperty(long j);

    public static final native void delete_SGVector2fProperty(long j);

    public static final native void delete_SGVector3fArrayProperty(long j);

    public static final native void delete_SGVector3fProperty(long j);

    public static final native void delete_SGVector4fProperty(long j);

    public static final native void delete_SGVectorfArrayProperty(long j);

    public static final native void delete_SGVectorfProperty(long j);

    public static final native void delete_SGVectoriArrayProperty(long j);

    public static final native void delete_SGVectoriProperty(long j);

    public static final native void delete_SGVertexBuffer(long j);

    static int getData(Enum param) {
        if (param != null) {
            return param.ordinal();
        }
        throw new NullPointerException();
    }

    public static final native long new_SGAlphaBlendProperty();

    public static final native long new_SGArrayProperty();

    public static final native long new_SGBatchingGroupProperty(int i);

    public static final native long new_SGBitmapTexture2DProperty(int i, int i2);

    public static final native long new_SGBoolProperty__SWIG_0();

    public static final native long new_SGBoolProperty__SWIG_1(boolean z);

    public static final native long new_SGBuffer();

    public static final native long new_SGCamera();

    public static final native long new_SGColorMaskProperty();

    public static final native long new_SGCompositeVertexBufferBuilder(String[] strArr, SGVertexBuffer[] sGVertexBufferArr, int i);

    public static final native long new_SGCubeMapTextureProperty__SWIG_0();

    public static final native long new_SGCubeMapTextureProperty__SWIG_1(int i, int i2, int i3, int i4, int i5);

    public static final native long new_SGCullFaceProperty();

    public static final native long new_SGDepthProperty();

    public static final native long new_SGDiscardRasterizerProperty();

    public static final native long new_SGDitherProperty();

    public static final native long new_SGFloatArrayProperty(int i);

    public static final native long new_SGFloatProperty__SWIG_0();

    public static final native long new_SGFloatProperty__SWIG_1(float f);

    public static final native long new_SGGeometry__SWIG_0(long j, SGIndexBuffer sGIndexBuffer);

    public static final native long new_SGGeometry__SWIG_1(int i);

    public static final native long new_SGIndexBuffer(int i, int i2, int i3);

    public static final native long new_SGIntArrayProperty(int[] iArr);

    public static final native long new_SGIntProperty__SWIG_0();

    public static final native long new_SGIntProperty__SWIG_1(int i);

    public static final native long new_SGLineWidthProperty__SWIG_0();

    public static final native long new_SGLineWidthProperty__SWIG_1(float f);

    public static final native long new_SGMaterial();

    public static final native long new_SGMatrixfArrayProperty__SWIG_0(SGMatrix2f[] sGMatrix2fArr);

    public static final native long new_SGMatrixfArrayProperty__SWIG_1(SGMatrix3f[] sGMatrix3fArr);

    public static final native long new_SGMatrixfArrayProperty__SWIG_2(SGMatrix4f[] sGMatrix4fArr);

    public static final native long new_SGMatrixfArrayProperty__SWIG_3(float[] fArr, int i);

    public static final native long new_SGMatrixfProperty__SWIG_0(int i);

    public static final native long new_SGMatrixfProperty__SWIG_1(float[] fArr);

    public static final native long new_SGPreMultiplyProperty__SWIG_0();

    public static final native long new_SGPreMultiplyProperty__SWIG_1(boolean z);

    public static final native long new_SGProperty();

    public static final native long new_SGQuaternionArrayProperty(SGQuaternion[] sGQuaternionArr);

    public static final native long new_SGQuaternionProperty__SWIG_0();

    public static final native long new_SGQuaternionProperty__SWIG_1(float[] fArr);

    public static final native long new_SGRenderBuffer__SWIG_0();

    public static final native long new_SGRenderBuffer__SWIG_1(int i);

    public static final native long new_SGRenderDataProvider();

    public static final native long new_SGRenderInterface();

    public static final native long new_SGRenderTarget();

    public static final native long new_SGRenderTargetToTexture();

    public static final native long new_SGResourceShaderProperty(int i, String str);

    public static final native long new_SGShaderProgramProperty(long j, SGShaderProperty sGShaderProperty, long j2, SGShaderProperty sGShaderProperty2);

    public static final native long new_SGSharedTextureProperty__SWIG_0(int i, int i2, String str);

    public static final native long new_SGSharedTextureProperty__SWIG_1(int i, int i2, String str, int i3, int i4, int i5, int i6);

    public static final native long new_SGSharedTextureProperty__SWIG_2(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z);

    public static final native long new_SGStencilProperty();

    public static final native long new_SGStringShaderProperty(int i, String str);

    public static final native long new_SGSurfaceRendererBase();

    public static final native long new_SGTexture2DAttachmentProperty(boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public static final native long new_SGTexture2DVideoProperty(String str);

    public static final native long new_SGTexture2DVideoPropertyWeakRef(long j, SGTexture2DVideoProperty sGTexture2DVideoProperty);

    public static final native long new_SGTextureAtlas__SWIG_0(long j, SGBitmapTexture2DProperty sGBitmapTexture2DProperty);

    public static final native long new_SGTextureAtlas__SWIG_1(long j, SGSharedTextureProperty sGSharedTextureProperty);

    public static final native long new_SGTextureAtlas__SWIG_2(int i, int i2);

    public static final native long new_SGTransformFeedbackProgramProperty(String[] strArr, long j, SGShaderProperty sGShaderProperty, long j2, SGShaderProperty sGShaderProperty2);

    public static final native long new_SGULongProperty__SWIG_0();

    public static final native long new_SGULongProperty__SWIG_1(long j);

    public static final native long new_SGVector3fArrayProperty(SGVector3f[] sGVector3fArr);

    public static final native long new_SGVectorfArrayProperty__SWIG_0(SGVector2f[] sGVector2fArr);

    public static final native long new_SGVectorfArrayProperty__SWIG_1(SGVector3f[] sGVector3fArr);

    public static final native long new_SGVectorfArrayProperty__SWIG_2(SGVector4f[] sGVector4fArr);

    public static final native long new_SGVectorfArrayProperty__SWIG_3(float[] fArr, int i);

    public static final native long new_SGVectorfProperty__SWIG_0(int i);

    public static final native long new_SGVectorfProperty__SWIG_1(float[] fArr);

    public static final native long new_SGVectorfProperty__SWIG_2(float[] fArr);

    public static final native long new_SGVectorfProperty__SWIG_3(float[] fArr);

    public static final native long new_SGVectorfProperty__SWIG_4(float[] fArr);

    public static final native long new_SGVectoriArrayProperty__SWIG_0(SGVector2i[] sGVector2iArr);

    public static final native long new_SGVectoriArrayProperty__SWIG_1(SGVector3i[] sGVector3iArr);

    public static final native long new_SGVectoriArrayProperty__SWIG_2(SGVector4i[] sGVector4iArr);

    public static final native long new_SGVectoriArrayProperty__SWIG_3(int[] iArr, int i);

    public static final native long new_SGVectoriProperty__SWIG_0(int i);

    public static final native long new_SGVectoriProperty__SWIG_1(int[] iArr);

    public static final native long new_SGVertexBuffer(int i, int i2, int i3, int i4);

    private static final native void swig_module_init();
}
