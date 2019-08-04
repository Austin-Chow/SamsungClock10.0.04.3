package com.sec.android.app.clockpackage.stopwatch.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ListItem implements Parcelable {
    public static final Creator<ListItem> CREATOR = new C07071();
    private long mElapsedTime;
    private String mIndex;
    private String mMilliSecond;
    private String mMilliSecondDiff;
    private String mTime;
    private String mTimeDescription;
    private String mTimeDiff;
    private String mTimeDiffDescription;

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.model.ListItem$1 */
    static class C07071 implements Creator<ListItem> {
        C07071() {
        }

        public ListItem createFromParcel(Parcel source) {
            return new ListItem(source.readString(), source.readLong(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString());
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    }

    public ListItem(String index, long elapsedTime, String time, String millisecond, String timeDiff, String millisecondDiff, String timeDescription, String timeDiffDescription) {
        this.mIndex = index;
        this.mElapsedTime = elapsedTime;
        this.mTime = time;
        this.mMilliSecond = millisecond;
        this.mTimeDiff = timeDiff;
        this.mMilliSecondDiff = millisecondDiff;
        this.mTimeDescription = timeDescription;
        this.mTimeDiffDescription = timeDiffDescription;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIndex);
        dest.writeLong(this.mElapsedTime);
        dest.writeString(this.mTime);
        dest.writeString(this.mMilliSecond);
        dest.writeString(this.mTimeDiff);
        dest.writeString(this.mMilliSecondDiff);
        dest.writeString(this.mTimeDescription);
        dest.writeString(this.mTimeDiffDescription);
    }

    public String getIndex() {
        return this.mIndex;
    }

    public String getTime() {
        return this.mTime;
    }

    public long getElapsedTime() {
        return this.mElapsedTime;
    }

    public String getMillisecond() {
        return this.mMilliSecond;
    }

    public String getTimeDiff() {
        return this.mTimeDiff;
    }

    public String getMillisecondDiff() {
        return this.mMilliSecondDiff;
    }

    public String getTimeDescription() {
        return this.mTimeDescription;
    }

    public String getTimeDiffDescription() {
        return this.mTimeDiffDescription;
    }
}
