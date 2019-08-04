package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.sec.android.app.clockpackage.common.util.UserProfileContract.UserProfile;

public class UserProfileDataBroker {
    public static int getGenderTypeValue(Context context) {
        String gender = queryGenderTypeData(context);
        if (gender.equals("M")) {
            return 1;
        }
        if (gender.equals("F")) {
            return 2;
        }
        return 3;
    }

    private static String queryGenderTypeData(Context context) {
        String gender = "N";
        try {
            Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(UserProfile.PERIOD_CATEGORY_CONTENT_URI, "last7days/demo.genderType"), null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                gender = cursor.getString(cursor.getColumnIndex("value"));
                cursor.close();
            }
        } catch (Exception e) {
            Log.secD("UserProfileDataBroker", "error for query data ");
        }
        if (!(gender.equals("F") && gender.equals("M"))) {
            float isMale = 0.0f;
            float isFemale = 0.0f;
            Uri uri1 = Uri.withAppendedPath(UserProfile.PERIOD_CATEGORY_CONTENT_URI, "last7days/demo.genderType.male");
            Uri uri2 = Uri.withAppendedPath(UserProfile.PERIOD_CATEGORY_CONTENT_URI, "last7days/demo.genderType.female");
            try {
                Cursor c1 = context.getContentResolver().query(uri1, null, null, null, null);
                Cursor c2 = context.getContentResolver().query(uri2, null, null, null, null);
                if (c1 != null) {
                    c1.moveToFirst();
                    isMale = c1.getFloat(c1.getColumnIndex("value"));
                    c1.close();
                }
                if (c2 != null) {
                    c2.moveToFirst();
                    isFemale = c2.getFloat(c2.getColumnIndex("value"));
                    c2.close();
                }
                if (isMale > isFemale) {
                    gender = "M";
                } else if (isMale < isFemale) {
                    gender = "F";
                }
            } catch (Exception e2) {
                Log.secD("UserProfileDataBroker", "error for query data ");
            }
        }
        Log.secD("UserProfileDataBroker", "genderType " + gender);
        return gender;
    }
}
