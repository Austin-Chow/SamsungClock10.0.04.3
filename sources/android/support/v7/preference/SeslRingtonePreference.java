package android.support.v7.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.UserHandle;
import android.support.v4.content.SeslContextReflector;
import android.support.v4.os.SeslUserHandleReflector;
import android.text.TextUtils;
import android.util.AttributeSet;

public class SeslRingtonePreference extends Preference {
    private int mRingtoneType;
    private boolean mShowDefault;
    private boolean mShowSilent;
    protected Context mUserContext;
    protected int mUserId;

    public SeslRingtonePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, C0263R.styleable.RingtonePreference, defStyleAttr, defStyleRes);
        this.mRingtoneType = a.getInt(C0263R.styleable.RingtonePreference_android_ringtoneType, 1);
        this.mShowDefault = a.getBoolean(C0263R.styleable.RingtonePreference_android_showDefault, true);
        this.mShowSilent = a.getBoolean(C0263R.styleable.RingtonePreference_android_showSilent, true);
        setIntent(new Intent("android.intent.action.RINGTONE_PICKER"));
        setUserId(SeslUserHandleReflector.myUserId());
        a.recycle();
    }

    public SeslRingtonePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslRingtonePreference(Context context, AttributeSet attrs) {
        this(context, attrs, C0263R.attr.ringtonePreferenceStyle);
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
        this.mUserContext = createPackageContextAsUser(getContext(), this.mUserId);
    }

    private Context createPackageContextAsUser(Context context, int userId) {
        return SeslContextReflector.createPackageContextAsUser(context, context.getPackageName(), 0, UserHandle.getUserHandleForUid(userId));
    }

    protected void onSaveRingtone(Uri ringtoneUri) {
        persistString(ringtoneUri != null ? ringtoneUri.toString() : "");
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValueObj) {
        String defaultValue = (String) defaultValueObj;
        if (!restorePersistedValue && !TextUtils.isEmpty(defaultValue)) {
            onSaveRingtone(Uri.parse(defaultValue));
        }
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
    }
}
