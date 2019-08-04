package com.samsung.android.sdk.sgi.ui;

import com.samsung.android.sdk.sgi.render.SGMaterial;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.vi.SGFilter;
import com.samsung.android.sdk.sgi.vi.SGGeometryGenerator;
import com.samsung.android.sdk.sgi.vi.SGLayer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public final class SGWidgetDecorator {
    private SGWidget mWidget;
    private boolean swigCMemOwn;
    private long swigCPtr;

    private SGWidgetDecorator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGWidgetDecorator(SGWidget widget) {
        this(SGJNI.new_SGWidgetDecorator(SGWidget.getCPtr(widget), widget), true);
        this.mWidget = widget;
    }

    private int addL(SGLayer layer) {
        return SGJNI.SGWidgetDecorator_addL__SWIG_0(this.swigCPtr, this, SGLayer.getCPtr(layer), layer);
    }

    private int addL(SGLayer layer, int index) {
        return SGJNI.SGWidgetDecorator_addL__SWIG_1(this.swigCPtr, this, SGLayer.getCPtr(layer), layer, index);
    }

    private void bringLToF(int index) {
        SGJNI.SGWidgetDecorator_bringLToF__SWIG_0(this.swigCPtr, this, index);
    }

    private void bringLToF(SGLayer layer) {
        SGJNI.SGWidgetDecorator_bringLToF__SWIG_1(this.swigCPtr, this, SGLayer.getCPtr(layer), layer);
    }

    public static long getCPtr(SGWidgetDecorator obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGWidgetDecorator_getHandle(this.swigCPtr, this);
    }

    private Class<?> getSuperclass(Class<?> cls) {
        while (true) {
            String name = cls.getCanonicalName();
            if (name != null && name.compareTo("com.samsung.android.sdk.sgi.vi.SGLayer") == 0) {
                return cls;
            }
            cls = cls.getSuperclass();
        }
    }

    private void removeAllL() {
        SGJNI.SGWidgetDecorator_removeAllL(this.swigCPtr, this);
    }

    private void removeL(int index) {
        SGJNI.SGWidgetDecorator_removeL__SWIG_1(this.swigCPtr, this, index);
    }

    private void removeL(SGLayer layer) {
        SGJNI.SGWidgetDecorator_removeL__SWIG_0(this.swigCPtr, this, SGLayer.getCPtr(layer), layer);
    }

    private void sendLToB(int index) {
        SGJNI.SGWidgetDecorator_sendLToB__SWIG_0(this.swigCPtr, this, index);
    }

    private void sendLToB(SGLayer layer) {
        SGJNI.SGWidgetDecorator_sendLToB__SWIG_1(this.swigCPtr, this, SGLayer.getCPtr(layer), layer);
    }

    private void setGeometryGeneratorNative(SGGeometryGenerator generator) {
        SGJNI.SGWidgetDecorator_setGeometryGeneratorNative(this.swigCPtr, this, SGGeometryGenerator.getCPtr(generator), generator);
    }

    private void swapL(SGLayer firstLayer, SGLayer secondLayer) {
        SGJNI.SGWidgetDecorator_swapL(this.swigCPtr, this, SGLayer.getCPtr(firstLayer), firstLayer, SGLayer.getCPtr(secondLayer), secondLayer);
    }

    public void addFilter(SGFilter filter) {
        SGJNI.SGWidgetDecorator_addFilter(this.swigCPtr, this, SGFilter.getCPtr(filter), filter);
    }

    public int addLayer(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        SGLayer parentLayer = layer.getParent();
        if (parentLayer != null) {
            try {
                Field field = getSuperclass(parentLayer.getClass()).getDeclaredField("mChildArray");
                field.setAccessible(true);
                ((ArrayList) field.get(parentLayer)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
        SGWidget parentWidget = layer.getParentWidget();
        if (parentWidget != null) {
            parentWidget.mChildArrayLayer.remove(layer);
        }
        int Id = addL(layer);
        this.mWidget.mChildArrayLayer.add(layer);
        return Id;
    }

    public int addLayer(SGLayer layer, int index) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        SGLayer parentLayer = layer.getParent();
        if (parentLayer != null) {
            try {
                Field field = getSuperclass(parentLayer.getClass()).getDeclaredField("mChildArray");
                field.setAccessible(true);
                ((ArrayList) field.get(parentLayer)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
        SGWidget parentWidget = layer.getParentWidget();
        if (parentWidget != null) {
            parentWidget.mChildArrayLayer.remove(layer);
        }
        int Id = addL(layer, index);
        this.mWidget.mChildArrayLayer.add(index, layer);
        return Id;
    }

    public void addMaterial(SGMaterial material) {
        SGJNI.SGWidgetDecorator_addMaterial(this.swigCPtr, this, SGMaterial.getCPtr(material), material);
    }

    public void bringLayerToFront(int index) {
        SGLayer layer = (SGLayer) this.mWidget.mChildArrayLayer.get(index);
        if (layer == null) {
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        bringLToF(index);
        this.mWidget.mChildArrayLayer.remove(index);
        this.mWidget.mChildArrayLayer.add(layer);
    }

    public void bringLayerToFront(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        SGLayer parentLayer = layer.getParent();
        SGWidget parentWidget = layer.getParentWidget();
        bringLToF(layer);
        if (parentLayer != null) {
            try {
                Field field = parentLayer.getClass().getDeclaredField("mChildArray");
                field.setAccessible(true);
                ((ArrayList) field.get(parentLayer)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        } else if (parentWidget != null) {
            parentWidget.mChildArrayLayer.remove(layer);
        }
        this.mWidget.mChildArrayLayer.add(layer);
    }

    public boolean equals(Object other) {
        return (other == null || !(other instanceof SGWidgetDecorator)) ? false : ((SGWidgetDecorator) other).getWidget().equals(getWidget());
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGWidgetDecorator(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGLayer findLayerById(int id) {
        return SGJNI.SGWidgetDecorator_findLayerById(this.swigCPtr, this, id);
    }

    public SGLayer findLayerByName(String name) {
        return SGJNI.SGWidgetDecorator_findLayerByName(this.swigCPtr, this, name);
    }

    public SGFilter getFilter(int index) {
        return (SGFilter) SGJNI.createObjectFromNativePtr(SGFilter.class, SGJNI.SGWidgetDecorator_getFilter(this.swigCPtr, this, index), true);
    }

    public int getFilterCount() {
        return SGJNI.SGWidgetDecorator_getFilterCount(this.swigCPtr, this);
    }

    public SGGeometryGenerator getGeometryGenerator() {
        return this.mWidget.mGeometryGenerator;
    }

    public float getGeometryGeneratorParam() {
        return SGJNI.SGWidgetDecorator_getGeometryGeneratorParam(this.swigCPtr, this);
    }

    public SGLayer getLayer(int index) {
        return (SGLayer) this.mWidget.mChildArrayLayer.get(index);
    }

    public SGLayer getLayer(String name) {
        int count = this.mWidget.mChildArrayLayer.size();
        for (int i = 0; i < count; i++) {
            SGLayer layer = (SGLayer) this.mWidget.mChildArrayLayer.get(i);
            if (layer.getName().contentEquals(name)) {
                return layer;
            }
        }
        return null;
    }

    public int getLayerIndex(SGLayer layer) {
        return this.mWidget.mChildArrayLayer.indexOf(layer);
    }

    public int getLayersCount() {
        return this.mWidget.mChildArrayLayer.size();
    }

    public SGMaterial getMaterial(int index) {
        return (SGMaterial) SGJNI.createObjectFromNativePtr(SGMaterial.class, SGJNI.SGWidgetDecorator_getMaterial(this.swigCPtr, this, index), true);
    }

    public int getMaterialsCount() {
        return SGJNI.SGWidgetDecorator_getMaterialsCount(this.swigCPtr, this);
    }

    public SGShaderProgramProperty getProgramProperty() {
        return SGJNI.SGWidgetDecorator_getProgramProperty(this.swigCPtr, this);
    }

    public SGProperty getProperty(int index) {
        return SGJNI.SGWidgetDecorator_getProperty__SWIG_0(this.swigCPtr, this, index);
    }

    public SGProperty getProperty(String name) {
        return SGJNI.SGWidgetDecorator_getProperty__SWIG_1(this.swigCPtr, this, name);
    }

    public int getPropertyCount() {
        return SGJNI.SGWidgetDecorator_getPropertyCount(this.swigCPtr, this);
    }

    public String getPropertyName(int index) {
        return SGJNI.SGWidgetDecorator_getPropertyName(this.swigCPtr, this, index);
    }

    public SGWidget getWidget() {
        return this.mWidget;
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void removeAllFilters() {
        SGJNI.SGWidgetDecorator_removeAllFilters(this.swigCPtr, this);
    }

    public void removeAllLayers() {
        if (!this.mWidget.mChildArrayLayer.isEmpty()) {
            removeAllL();
            this.mWidget.mChildArrayLayer.clear();
        }
    }

    public void removeAllMaterials() {
        SGJNI.SGWidgetDecorator_removeAllMaterials(this.swigCPtr, this);
    }

    public void removeFilter(int index) {
        SGJNI.SGWidgetDecorator_removeFilter__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeFilter(SGFilter filter) {
        SGJNI.SGWidgetDecorator_removeFilter__SWIG_0(this.swigCPtr, this, SGFilter.getCPtr(filter), filter);
    }

    public void removeLayer(int index) {
        removeL(index);
        this.mWidget.mChildArrayLayer.remove(index);
    }

    public void removeLayer(SGLayer layer) {
        removeL(layer);
        this.mWidget.mChildArrayLayer.remove(layer);
    }

    public void removeMaterial(int index) {
        SGJNI.SGWidgetDecorator_removeMaterial__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeMaterial(SGMaterial material) {
        SGJNI.SGWidgetDecorator_removeMaterial__SWIG_0(this.swigCPtr, this, SGMaterial.getCPtr(material), material);
    }

    public void removeProperty(int index) {
        SGJNI.SGWidgetDecorator_removeProperty__SWIG_0(this.swigCPtr, this, index);
    }

    public void removeProperty(String name) {
        SGJNI.SGWidgetDecorator_removeProperty__SWIG_1(this.swigCPtr, this, name);
    }

    public void resetFilterFrameBufferSize() {
        SGJNI.SGWidgetDecorator_resetFilterFrameBufferSize(this.swigCPtr, this);
    }

    public void sendLayerToBack(int index) {
        SGLayer layer = (SGLayer) this.mWidget.mChildArrayLayer.get(index);
        if (layer == null) {
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        sendLToB(index);
        this.mWidget.mChildArrayLayer.remove(index);
        this.mWidget.mChildArrayLayer.add(0, layer);
    }

    public void sendLayerToBack(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        SGLayer parentLayer = layer.getParent();
        SGWidget parentWidget = layer.getParentWidget();
        sendLToB(layer);
        if (parentLayer != null) {
            try {
                Field field = parentLayer.getClass().getDeclaredField("mChildArray");
                field.setAccessible(true);
                ((ArrayList) field.get(parentLayer)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        } else if (parentWidget != null) {
            parentWidget.mChildArrayLayer.remove(layer);
        }
        this.mWidget.mChildArrayLayer.add(0, layer);
    }

    public void setFilterFrameBufferSize(float width, float height) {
        SGJNI.SGWidgetDecorator_setFilterFrameBufferSize(this.swigCPtr, this, width, height);
    }

    public void setGeometryGenerator(SGGeometryGenerator generator) {
        setGeometryGeneratorNative(generator);
        this.mWidget.mGeometryGenerator = generator;
    }

    public void setGeometryGeneratorParam(float param) {
        SGJNI.SGWidgetDecorator_setGeometryGeneratorParam(this.swigCPtr, this, param);
    }

    public void setProgramProperty(SGShaderProgramProperty property) {
        SGJNI.SGWidgetDecorator_setProgramProperty(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void setProperty(String name, SGProperty property) {
        SGJNI.SGWidgetDecorator_setProperty(this.swigCPtr, this, name, SGProperty.getCPtr(property), property);
    }

    public void swapLayers(SGLayer firstLayer, SGLayer secondLayer) {
        swapL(firstLayer, secondLayer);
        Collections.swap(this.mWidget.mChildArrayLayer, this.mWidget.mChildArrayLayer.indexOf(firstLayer), this.mWidget.mChildArrayLayer.indexOf(secondLayer));
    }
}
