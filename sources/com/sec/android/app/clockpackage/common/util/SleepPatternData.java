package com.sec.android.app.clockpackage.common.util;

import android.util.SparseArray;

public class SleepPatternData {
    private final SparseArray<Record> mSleepPatternRecords = new SparseArray();

    public static final class Record {
        public final long analyzedAt;
        public final long bedTime;
        public final float confidence;
        public final boolean isConfident;
        public final int type;
        public final long wakeupTime;

        Record(int type, long wakeupTime, long bedTime, boolean isConfident, float confidence, long analyzedAt) {
            this.type = type;
            this.wakeupTime = wakeupTime;
            this.bedTime = bedTime;
            this.isConfident = isConfident;
            this.confidence = confidence;
            this.analyzedAt = analyzedAt;
        }
    }

    SleepPatternData() {
    }

    Record getData(int type) {
        return (Record) this.mSleepPatternRecords.get(type);
    }

    void putData(int type, Record record) {
        this.mSleepPatternRecords.put(type, record);
    }

    void clearData() {
        this.mSleepPatternRecords.clear();
    }
}
