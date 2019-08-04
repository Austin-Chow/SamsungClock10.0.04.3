package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClassesInfoCache {
    static ClassesInfoCache sInstance = new ClassesInfoCache();
    private final Map<Class, CallbackInfo> mCallbackMap = new HashMap();
    private final Map<Class, Boolean> mHasLifecycleMethods = new HashMap();

    static class CallbackInfo {
        final Map<Event, List<MethodReference>> mEventToHandlers;

        void invokeCallbacks(LifecycleOwner source, Event event, Object target) {
            invokeMethodsForEvent((List) this.mEventToHandlers.get(event), source, event, target);
            invokeMethodsForEvent((List) this.mEventToHandlers.get(Event.ON_ANY), source, event, target);
        }

        private static void invokeMethodsForEvent(List<MethodReference> handlers, LifecycleOwner source, Event event, Object mWrapped) {
            if (handlers != null) {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    ((MethodReference) handlers.get(i)).invokeCallback(source, event, mWrapped);
                }
            }
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        void invokeCallback(LifecycleOwner source, Event event, Object target) {
            try {
                switch (this.mCallType) {
                    case 0:
                        this.mMethod.invoke(target, new Object[0]);
                        return;
                    case 1:
                        this.mMethod.invoke(target, new Object[]{source});
                        return;
                    case 2:
                        this.mMethod.invoke(target, new Object[]{source, event});
                        return;
                    default:
                        return;
                }
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to call observer method", e.getCause());
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MethodReference that = (MethodReference) o;
            if (this.mCallType == that.mCallType && this.mMethod.getName().equals(that.mMethod.getName())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }
    }

    ClassesInfoCache() {
    }
}
