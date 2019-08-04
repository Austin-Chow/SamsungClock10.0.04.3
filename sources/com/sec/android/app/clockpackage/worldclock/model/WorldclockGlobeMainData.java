package com.sec.android.app.clockpackage.worldclock.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WorldclockGlobeMainData implements Parcelable {
    public static final Creator<WorldclockGlobeMainData> CREATOR = new C08401();
    private String mFromWhere;
    private int mHomeZone;
    private int mIndex;
    private int mListPosition;
    private int mUniqueId;
    private int mWidgetId;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.model.WorldclockGlobeMainData$1 */
    static class C08401 implements Creator<WorldclockGlobeMainData> {
        C08401() {
        }

        public WorldclockGlobeMainData createFromParcel(Parcel p) {
            return new WorldclockGlobeMainData(p);
        }

        public WorldclockGlobeMainData[] newArray(int size) {
            return new WorldclockGlobeMainData[size];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mHomeZone);
        dest.writeInt(this.mWidgetId);
        dest.writeInt(this.mIndex);
        dest.writeInt(this.mUniqueId);
        dest.writeInt(this.mListPosition);
        dest.writeString(this.mFromWhere);
    }

    private WorldclockGlobeMainData(Parcel p) {
        this.mHomeZone = p.readInt();
        this.mWidgetId = p.readInt();
        this.mIndex = p.readInt();
        this.mUniqueId = p.readInt();
        this.mListPosition = p.readInt();
        this.mFromWhere = p.readString();
    }

    public int getHomeZone() {
        return this.mHomeZone;
    }

    public void setHomeZone(int homeZone) {
        this.mHomeZone = homeZone;
    }

    public int getWidgetId() {
        return this.mWidgetId;
    }

    public void setWidgetId(int widgetId) {
        this.mWidgetId = widgetId;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public int getUniqueId() {
        return this.mUniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.mUniqueId = uniqueId;
    }

    public int getListPosition() {
        return this.mListPosition;
    }

    public void setListPosition(int listPosition) {
        this.mListPosition = listPosition;
    }

    public String getFromWhere() {
        return this.mFromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.mFromWhere = fromWhere;
    }
}
