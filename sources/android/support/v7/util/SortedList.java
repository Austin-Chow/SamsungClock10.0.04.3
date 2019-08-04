package android.support.v7.util;

import java.util.Comparator;

public class SortedList<T> {

    public static abstract class Callback<T2> implements ListUpdateCallback, Comparator<T2> {
        public abstract boolean areContentsTheSame(T2 t2, T2 t22);

        public abstract boolean areItemsTheSame(T2 t2, T2 t22);

        public abstract int compare(T2 t2, T2 t22);

        public abstract void onChanged(int i, int i2);

        public void onChanged(int position, int count, Object payload) {
            onChanged(position, count);
        }

        public Object getChangePayload(T2 t2, T2 t22) {
            return null;
        }
    }
}
