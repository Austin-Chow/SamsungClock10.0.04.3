package com.samsung.android.sdk.sgi.ui;

import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public final class SGTouchEvent {
    static final int DEFAULT_POINTER = 0;
    private static MotionEvent lastMotionEvent = null;
    private static SGTouchEvent[] mObjectPool = new SGTouchEvent[mPoolSize];
    private static int mPoolIndex = -1;
    private static int mPoolSize = 100;
    private SGAction mAction;
    private Date mDownTime;
    private ArrayList<SGHistoryEntry> mHistoryList;
    private int mMaxPointersCount;
    private SGPointer[] mPointerList;
    private int mPointersCount;
    private Date mTouchTime;

    public enum SGAction {
        NOTHING,
        DOWN,
        MOVE,
        UP,
        CANCEL,
        OUTSIDE,
        HOVER_ENTER,
        HOVER_MOVE,
        HOVER_EXIT,
        HOVER_BUTTON_DOWN,
        HOVER_BUTTON_UP,
        TOUCH_BUTTON_DOWN,
        TOUCH_BUTTON_UP
    }

    private final class SGHistoryEntry {
        public Date mEventTime;
        public ArrayList<SGPointer> mPointerList;

        public SGHistoryEntry() {
            this.mEventTime = null;
            this.mPointerList = new ArrayList();
            this.mEventTime = new Date();
        }

        public SGHistoryEntry(Date eventTime) {
            this.mEventTime = null;
            this.mPointerList = new ArrayList();
            this.mEventTime = (Date) eventTime.clone();
        }

        private void copyFrom(SGHistoryEntry other) {
            int size = this.mPointerList.size();
            int sizeDiff = size - other.mPointerList.size();
            if (sizeDiff > 0) {
                this.mPointerList.subList(size - sizeDiff, size).clear();
            }
            this.mPointerList.ensureCapacity(size - sizeDiff);
            for (int i = 0; i < size; i++) {
                if (i < size) {
                    SGPointer ptr = (SGPointer) this.mPointerList.get(i);
                    ptr.copyFrom((SGPointer) other.mPointerList.get(i));
                    this.mPointerList.set(i, ptr);
                } else {
                    this.mPointerList.add(i, (SGPointer) ((SGPointer) other.mPointerList.get(i)).clone());
                }
            }
            this.mEventTime = (Date) other.mEventTime.clone();
        }

        protected Object clone() {
            SGHistoryEntry historyEntry = new SGHistoryEntry();
            historyEntry.mPointerList = new ArrayList(this.mPointerList.size());
            Iterator it = this.mPointerList.iterator();
            while (it.hasNext()) {
                historyEntry.mPointerList.add((SGPointer) ((SGPointer) it.next()).clone());
            }
            historyEntry.mEventTime = (Date) historyEntry.mEventTime.clone();
            return historyEntry;
        }
    }

    private final class SGPointer {
        public SGAction mAction;
        public int mId;
        public SGPointerType mPointerType;
        public float mPressure;
        public float mRawX;
        public float mRawY;
        public float mX;
        public float mY;

        public SGPointer() {
            this.mId = 0;
            this.mX = 0.0f;
            this.mY = 0.0f;
            this.mRawX = 0.0f;
            this.mRawY = 0.0f;
            this.mPressure = 0.0f;
            this.mAction = SGAction.NOTHING;
            this.mPointerType = SGPointerType.FINGER;
        }

        public SGPointer(int id, float x, float y, float rawX, float rawY, SGAction action, float pressure, SGPointerType pointerType) {
            this.mId = id;
            this.mX = x;
            this.mY = y;
            this.mRawX = rawX;
            this.mRawY = rawY;
            this.mAction = action;
            this.mPressure = pressure;
            this.mPointerType = pointerType;
        }

        protected Object clone() {
            SGPointer pointer = new SGPointer();
            pointer.mId = this.mId;
            pointer.mX = this.mX;
            pointer.mY = this.mY;
            pointer.mRawX = this.mRawX;
            pointer.mRawY = this.mRawY;
            pointer.mPressure = this.mPressure;
            pointer.mAction = this.mAction;
            pointer.mPointerType = this.mPointerType;
            return pointer;
        }

        void copyFrom(SGPointer other) {
            this.mId = other.mId;
            this.mX = other.mX;
            this.mY = other.mY;
            this.mRawX = other.mRawX;
            this.mRawY = other.mRawY;
            this.mPressure = other.mPressure;
            this.mAction = other.mAction;
            this.mPointerType = other.mPointerType;
        }

        void set(int id, float x, float y, float rawX, float rawY, SGAction action, float pressure, SGPointerType pointerType) {
            this.mId = id;
            this.mX = x;
            this.mY = y;
            this.mRawX = rawX;
            this.mRawY = rawY;
            this.mAction = action;
            this.mPressure = pressure;
            this.mPointerType = pointerType;
        }
    }

    public enum SGPointerType {
        UNKNOWN,
        FINGER,
        ERASER,
        MOUSE,
        STYLUS
    }

    public SGTouchEvent() {
        this.mTouchTime = null;
        this.mDownTime = null;
        this.mAction = null;
        this.mPointersCount = 0;
        this.mMaxPointersCount = 10;
        this.mTouchTime = new Date();
        this.mDownTime = new Date();
        this.mPointerList = new SGPointer[this.mMaxPointersCount];
        this.mHistoryList = new ArrayList();
    }

    public SGTouchEvent(SGTouchEvent other) {
        this();
        if (other != null) {
            copyFrom(other);
        }
    }

    private void copyFrom(SGTouchEvent other) {
        int i;
        this.mTouchTime = other.mTouchTime;
        this.mDownTime = other.mDownTime;
        this.mAction = other.mAction;
        if (other.mMaxPointersCount > this.mMaxPointersCount) {
            this.mMaxPointersCount = other.mMaxPointersCount;
            this.mPointerList = new SGPointer[this.mMaxPointersCount];
        }
        this.mPointersCount = other.mPointersCount;
        for (i = 0; i < this.mPointersCount; i++) {
            if (this.mPointerList[i] != null) {
                this.mPointerList[i].copyFrom(other.mPointerList[i]);
            } else {
                this.mPointerList[i] = (SGPointer) other.mPointerList[i].clone();
            }
        }
        int size = this.mHistoryList.size();
        int sizeDiff = size - other.mHistoryList.size();
        if (sizeDiff > 0) {
            this.mHistoryList.subList(size - sizeDiff, size).clear();
        } else {
            this.mHistoryList.ensureCapacity(size - sizeDiff);
        }
        for (i = 0; i < size; i++) {
            if (i < size) {
                SGHistoryEntry his = (SGHistoryEntry) this.mHistoryList.get(i);
                his.copyFrom((SGHistoryEntry) other.mHistoryList.get(i));
                this.mHistoryList.set(i, his);
            } else {
                this.mHistoryList.add(i, (SGHistoryEntry) ((SGHistoryEntry) other.mHistoryList.get(i)).clone());
            }
        }
    }

    public static SGTouchEvent create(SGTouchEvent event) {
        if (mPoolIndex == -1) {
            return new SGTouchEvent(event);
        }
        SGTouchEvent[] sGTouchEventArr = mObjectPool;
        int i = mPoolIndex;
        mPoolIndex = i - 1;
        SGTouchEvent ev = sGTouchEventArr[i];
        if (event == null) {
            return ev;
        }
        ev.copyFrom(event);
        return ev;
    }

    public static final MotionEvent createMotionEvent(SGTouchEvent event, SGWidget widget) {
        return lastMotionEvent != null ? MotionEvent.obtain(lastMotionEvent) : null;
    }

    private SGPointer pointerByIndex(int index) {
        if (index < this.mPointersCount) {
            return this.mPointerList[index];
        }
        throw new IndexOutOfBoundsException();
    }

    public static void recycle(SGTouchEvent event) {
        if (event != null) {
            event.mAction = null;
            event.mPointersCount = 0;
            event.mHistoryList.clear();
            if (mPoolIndex < mPoolSize - 1) {
                SGTouchEvent[] sGTouchEventArr = mObjectPool;
                int i = mPoolIndex + 1;
                mPoolIndex = i;
                sGTouchEventArr[i] = event;
            }
        }
    }

    private void resizePointers(int count) {
        if (count > this.mMaxPointersCount) {
            this.mMaxPointersCount = (count % 4) + count;
            System.arraycopy(this.mPointerList, 0, new SGPointer[this.mMaxPointersCount], 0, this.mPointersCount);
        }
        this.mPointersCount = count;
    }

    public void addPointer(int id, float x, float y, float rawX, float rawY, SGAction action, float pressure, SGPointerType pointerType) {
        if (action == null || pointerType == null) {
            throw new NullPointerException();
        }
        if (this.mPointersCount == this.mMaxPointersCount) {
            this.mMaxPointersCount += 4;
            SGPointer[] pointers = new SGPointer[this.mMaxPointersCount];
            System.arraycopy(this.mPointerList, 0, pointers, 0, this.mPointersCount);
            this.mPointerList = pointers;
        }
        this.mPointerList[this.mPointersCount] = new SGPointer(id, x, y, rawX, rawY, action, pressure, pointerType);
        this.mPointersCount++;
    }

    public void appendHistoryEntry(Date eventTime) {
        if (eventTime == null) {
            throw new NullPointerException();
        }
        int size = this.mHistoryList.size();
        if (size == 0 || (this.mPointerList != null && this.mPointersCount == ((SGHistoryEntry) this.mHistoryList.get(size - 1)).mPointerList.size())) {
            this.mHistoryList.add(new SGHistoryEntry(eventTime));
            return;
        }
        throw new RuntimeException("Last history entry is incomplete");
    }

    public void appendPointerToHistory(float rawX, float rawY, float pressure) {
        int size = this.mHistoryList.size();
        if (size == 0) {
            throw new RuntimeException("Append history entry first");
        }
        SGPointer currPointer = new SGPointer();
        currPointer.mX = rawX;
        currPointer.mY = rawY;
        currPointer.mRawX = rawX;
        currPointer.mRawY = rawY;
        currPointer.mPressure = pressure;
        ((SGHistoryEntry) this.mHistoryList.get(size - 1)).mPointerList.add(currPointer);
    }

    public int findPointerIndex(int pointerId) {
        for (int i = 0; i < this.mPointersCount; i++) {
            if (pointerByIndex(i).mId == pointerId) {
                return i;
            }
        }
        return -1;
    }

    public SGAction getAction() {
        if (this.mAction != null) {
            return this.mAction;
        }
        this.mAction = getAction(0);
        return this.mAction;
    }

    public SGAction getAction(int index) {
        return pointerByIndex(index).mAction;
    }

    public Date getDownTime() {
        return this.mDownTime;
    }

    public Date getHistoricalEventTime(int pos) {
        return ((SGHistoryEntry) this.mHistoryList.get(pos)).mEventTime;
    }

    public float getHistoricalPressure(int pointerIndex, int pos) {
        return ((SGPointer) ((SGHistoryEntry) this.mHistoryList.get(pos)).mPointerList.get(pointerIndex)).mPressure;
    }

    public float getHistoricalX(int pointerIndex, int pos) {
        return ((SGPointer) ((SGHistoryEntry) this.mHistoryList.get(pos)).mPointerList.get(pointerIndex)).mRawX;
    }

    public float getHistoricalY(int pointerIndex, int pos) {
        return ((SGPointer) ((SGHistoryEntry) this.mHistoryList.get(pos)).mPointerList.get(pointerIndex)).mRawY;
    }

    public int getHistorySize() {
        return this.mHistoryList.size();
    }

    public int getPointerCount() {
        return this.mPointersCount;
    }

    public int getPointerId(int index) {
        return pointerByIndex(index).mId;
    }

    public float getPressure() {
        return pointerByIndex(0).mPressure;
    }

    public float getPressure(int index) {
        return pointerByIndex(index).mPressure;
    }

    public float getRawX() {
        return pointerByIndex(0).mRawX;
    }

    public float getRawX(int index) {
        return pointerByIndex(index).mRawX;
    }

    public float getRawY() {
        return pointerByIndex(0).mRawY;
    }

    public float getRawY(int index) {
        return pointerByIndex(index).mRawY;
    }

    public Date getTouchTime() {
        return this.mTouchTime;
    }

    public float getX() {
        return pointerByIndex(0).mX;
    }

    public float getX(int index) {
        return pointerByIndex(index).mX;
    }

    public float getY() {
        return pointerByIndex(0).mY;
    }

    public float getY(int index) {
        return pointerByIndex(index).mY;
    }

    public void setAction(int index, SGAction action) {
        if (action == null) {
            throw new NullPointerException();
        }
        if (index == 0) {
            this.mAction = action;
        }
        pointerByIndex(index).mAction = action;
    }

    public void setDownTime(Date downTime) {
        if (downTime == null) {
            throw new NullPointerException();
        }
        this.mDownTime = downTime;
    }

    public void setPointer(int index, int id, float x, float y, float rawX, float rawY, SGAction action, float pressure, SGPointerType pointerType) {
        if (action == null || pointerType == null) {
            throw new NullPointerException();
        }
        if (index >= this.mMaxPointersCount) {
            resizePointers(index + 1);
        }
        SGPointer ptr = this.mPointerList[index];
        if (index + 1 > this.mPointersCount) {
            this.mPointersCount = index + 1;
        }
        if (ptr != null) {
            ptr.set(id, x, y, rawX, rawY, action, pressure, pointerType);
            return;
        }
        this.mPointerList[index] = new SGPointer(id, x, y, rawX, rawY, action, pressure, pointerType);
    }

    public void setTouchTime(Date touchTime) {
        if (touchTime == null) {
            throw new NullPointerException();
        }
        this.mTouchTime = touchTime;
    }

    public void setX(int index, float value) {
        pointerByIndex(index).mX = value;
    }

    public void setY(int index, float value) {
        pointerByIndex(index).mY = value;
    }
}
