package android.arch.core.internal;

import java.util.HashMap;

public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V> {
    private HashMap<K, Entry<K, V>> mHashMap = new HashMap();

    protected Entry<K, V> get(K k) {
        return (Entry) this.mHashMap.get(k);
    }

    public V remove(K key) {
        V removed = super.remove(key);
        this.mHashMap.remove(key);
        return removed;
    }

    public boolean contains(K key) {
        return this.mHashMap.containsKey(key);
    }
}
