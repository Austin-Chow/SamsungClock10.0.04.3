package com.samsung.android.sdk.sgi.base;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class SGMemoryRegistrator extends SGRegistrator {
    private static final String LOG_TAG = "SGMemoryRegistrator";
    private ConcurrentHashMap<Long, ReferenceHolder> mRegistrionMap;

    private static class MemoryRegistratorHolder {
        private static final SGMemoryRegistrator HOLDER_INSTANCE = new SGMemoryRegistrator();

        private MemoryRegistratorHolder() {
        }
    }

    private static class ReferenceHolder {
        public int counter = 0;
        public Object strong = null;
        public WeakReference<Object> weak = null;
    }

    private SGMemoryRegistrator() {
        this.mRegistrionMap = new ConcurrentHashMap();
    }

    public static SGMemoryRegistrator getInstance() {
        return MemoryRegistratorHolder.HOLDER_INSTANCE;
    }

    public static void printMap(boolean printAll) {
        int len;
        StringBuilder sb = new StringBuilder(4096);
        sb.append("MemoryRegistrator total entries count: ");
        sb.append(getInstance().mRegistrionMap.size());
        sb.append('\n');
        boolean noStrong = !printAll;
        for (Entry<Long, ReferenceHolder> e : getInstance().mRegistrionMap.entrySet()) {
            ReferenceHolder h = (ReferenceHolder) e.getValue();
            long key = ((Long) e.getKey()).longValue();
            len = sb.length();
            sb.append("Key: ");
            sb.append(key);
            sb.append(" count: ");
            sb.append(h.counter);
            sb.append(' ');
            if (h.strong != null) {
                sb.append("strong ref: ");
                sb.append(h.strong.getClass().getName());
                noStrong = false;
            } else if (printAll) {
                Object obj = h.weak.get();
                if (obj != null) {
                    sb.append("weak ref: ");
                    sb.append(obj.getClass().getName());
                } else {
                    sb.append("lost entry");
                }
            } else {
                sb.setLength(len);
            }
            sb.append('\n');
        }
        if (noStrong) {
            sb.append("no strong refs");
        }
        len = sb.length();
        int chunkCount = (len + 3999) / 4000;
        for (int i = 0; i < chunkCount; i++) {
            int max = (i + 1) * 4000;
            if (max > len) {
                max = len;
            }
            Log.d(LOG_TAG, sb.substring(i * 4000, max));
        }
    }

    public static void printReferenceTables() {
        try {
            Class.forName("dalvik.system.VMDebug").getDeclaredMethod("dumpReferenceTables", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean AddToManagementList(long pointer) {
        ReferenceHolder holder = (ReferenceHolder) this.mRegistrionMap.get(Long.valueOf(pointer));
        if (holder != null) {
            Object obj = holder.weak.get();
            if (obj != null) {
                holder.counter++;
                if (holder.counter != 1) {
                    return true;
                }
                holder.strong = obj;
                return true;
            }
            Log.e(LOG_TAG, "Trying to add collected object");
        } else {
            Log.e(LOG_TAG, "Trying to add unregistered object");
        }
        return false;
    }

    public boolean Deregister(long pointer) {
        Long tmpPointer = Long.valueOf(pointer);
        if (this.mRegistrionMap.get(tmpPointer) == null) {
            return false;
        }
        this.mRegistrionMap.remove(tmpPointer);
        return true;
    }

    public Object GetObjectByPointer(long pointer) {
        if (pointer == 0) {
            return null;
        }
        ReferenceHolder holder = (ReferenceHolder) this.mRegistrionMap.get(Long.valueOf(pointer));
        return (holder == null || holder.weak == null) ? null : holder.weak.get();
    }

    public boolean Register(Object obj, long pointer) {
        if (obj == null || pointer == 0) {
            return false;
        }
        ReferenceHolder holder = new ReferenceHolder();
        holder.weak = new WeakReference(obj);
        if (this.mRegistrionMap.put(Long.valueOf(pointer), holder) == null) {
            return true;
        }
        Log.e(LOG_TAG, "Duplicate key when registering " + obj.getClass().getName());
        return false;
    }

    public boolean RemoveFromManagementList(long pointer) {
        ReferenceHolder holder = (ReferenceHolder) this.mRegistrionMap.get(Long.valueOf(pointer));
        if (holder != null) {
            if (holder.counter == 1) {
                holder.counter = 0;
                holder.strong = null;
                return true;
            } else if (holder.counter > 0) {
                holder.counter--;
            }
        }
        return false;
    }
}
