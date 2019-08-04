package android.arch.core.internal;

import java.util.Iterator;
import java.util.WeakHashMap;

public class SafeIterableMap<K, V> implements Iterable<java.util.Map.Entry<K, V>> {
    private Entry<K, V> mEnd;
    private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators = new WeakHashMap();
    private int mSize = 0;
    private Entry<K, V> mStart;

    interface SupportRemove<K, V> {
        void supportRemove(Entry<K, V> entry);
    }

    private static abstract class ListIterator<K, V> implements SupportRemove<K, V>, Iterator<java.util.Map.Entry<K, V>> {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;

        abstract Entry<K, V> backward(Entry<K, V> entry);

        abstract Entry<K, V> forward(Entry<K, V> entry);

        ListIterator(Entry<K, V> start, Entry<K, V> expectedEnd) {
            this.mExpectedEnd = expectedEnd;
            this.mNext = start;
        }

        public boolean hasNext() {
            return this.mNext != null;
        }

        public void supportRemove(Entry<K, V> entry) {
            if (this.mExpectedEnd == entry && entry == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            if (this.mExpectedEnd == entry) {
                this.mExpectedEnd = backward(this.mExpectedEnd);
            }
            if (this.mNext == entry) {
                this.mNext = nextNode();
            }
        }

        private Entry<K, V> nextNode() {
            if (this.mNext == this.mExpectedEnd || this.mExpectedEnd == null) {
                return null;
            }
            return forward(this.mNext);
        }

        public java.util.Map.Entry<K, V> next() {
            java.util.Map.Entry<K, V> result = this.mNext;
            this.mNext = nextNode();
            return result;
        }
    }

    static class AscendingIterator<K, V> extends ListIterator<K, V> {
        AscendingIterator(Entry<K, V> start, Entry<K, V> expectedEnd) {
            super(start, expectedEnd);
        }

        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mNext;
        }

        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mPrevious;
        }
    }

    private static class DescendingIterator<K, V> extends ListIterator<K, V> {
        DescendingIterator(Entry<K, V> start, Entry<K, V> expectedEnd) {
            super(start, expectedEnd);
        }

        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mPrevious;
        }

        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mNext;
        }
    }

    static class Entry<K, V> implements java.util.Map.Entry<K, V> {
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        final V mValue;

        public K getKey() {
            return this.mKey;
        }

        public V getValue() {
            return this.mValue;
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.mKey + "=" + this.mValue;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (this.mKey.equals(entry.mKey) && this.mValue.equals(entry.mValue)) {
                return true;
            }
            return false;
        }
    }

    private class IteratorWithAdditions implements SupportRemove<K, V>, Iterator<java.util.Map.Entry<K, V>> {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;

        private IteratorWithAdditions() {
            this.mBeforeStart = true;
        }

        public void supportRemove(Entry<K, V> entry) {
            if (entry == this.mCurrent) {
                this.mCurrent = this.mCurrent.mPrevious;
                this.mBeforeStart = this.mCurrent == null;
            }
        }

        public boolean hasNext() {
            if (this.mBeforeStart) {
                if (SafeIterableMap.this.mStart != null) {
                    return true;
                }
                return false;
            } else if (this.mCurrent == null || this.mCurrent.mNext == null) {
                return false;
            } else {
                return true;
            }
        }

        public java.util.Map.Entry<K, V> next() {
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                this.mCurrent = SafeIterableMap.this.mStart;
            } else {
                this.mCurrent = this.mCurrent != null ? this.mCurrent.mNext : null;
            }
            return this.mCurrent;
        }
    }

    protected Entry<K, V> get(K k) {
        Entry<K, V> currentNode = this.mStart;
        while (currentNode != null && !currentNode.mKey.equals(k)) {
            currentNode = currentNode.mNext;
        }
        return currentNode;
    }

    public V remove(K key) {
        Entry<K, V> toRemove = get(key);
        if (toRemove == null) {
            return null;
        }
        this.mSize--;
        if (!this.mIterators.isEmpty()) {
            for (SupportRemove<K, V> iter : this.mIterators.keySet()) {
                iter.supportRemove(toRemove);
            }
        }
        if (toRemove.mPrevious != null) {
            toRemove.mPrevious.mNext = toRemove.mNext;
        } else {
            this.mStart = toRemove.mNext;
        }
        if (toRemove.mNext != null) {
            toRemove.mNext.mPrevious = toRemove.mPrevious;
        } else {
            this.mEnd = toRemove.mPrevious;
        }
        toRemove.mNext = null;
        toRemove.mPrevious = null;
        return toRemove.mValue;
    }

    public int size() {
        return this.mSize;
    }

    public Iterator<java.util.Map.Entry<K, V>> iterator() {
        ListIterator<K, V> iterator = new AscendingIterator(this.mStart, this.mEnd);
        this.mIterators.put(iterator, Boolean.valueOf(false));
        return iterator;
    }

    public Iterator<java.util.Map.Entry<K, V>> descendingIterator() {
        DescendingIterator<K, V> iterator = new DescendingIterator(this.mEnd, this.mStart);
        this.mIterators.put(iterator, Boolean.valueOf(false));
        return iterator;
    }

    public IteratorWithAdditions iteratorWithAdditions() {
        IteratorWithAdditions iterator = new IteratorWithAdditions();
        this.mIterators.put(iterator, Boolean.valueOf(false));
        return iterator;
    }

    public java.util.Map.Entry<K, V> eldest() {
        return this.mStart;
    }

    public java.util.Map.Entry<K, V> newest() {
        return this.mEnd;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SafeIterableMap)) {
            return false;
        }
        SafeIterableMap map = (SafeIterableMap) obj;
        if (size() != map.size()) {
            return false;
        }
        Iterator<java.util.Map.Entry<K, V>> iterator1 = iterator();
        Iterator iterator2 = map.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            java.util.Map.Entry<K, V> next1 = (java.util.Map.Entry) iterator1.next();
            Object next2 = iterator2.next();
            if (next1 == null && next2 != null) {
                return false;
            }
            if (next1 != null && !next1.equals(next2)) {
                return false;
            }
        }
        if (iterator1.hasNext() || iterator2.hasNext()) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator<java.util.Map.Entry<K, V>> iterator = iterator();
        while (iterator.hasNext()) {
            builder.append(((java.util.Map.Entry) iterator.next()).toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
