package com.sec.android.widgetapp.dualclockdigital;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.widgetapp.dualclockdigital.DualClockDigitalContract.View;
import com.sec.android.widgetapp.dualclockdigital.DualClockDigitalContract.ViewModel;
import java.util.Locale;
import java.util.TimeZone;

public class DualClockDigitalViewModel implements ViewModel {
    public static String sFirstCityName;
    public static int sFirstUniqueId;
    public static String sSecondCityName;
    public static int sSecondUniqueId = -1;
    private DualClockDigitalWidgetModel mModel;
    private View mView;

    public DualClockDigitalViewModel(DualClockDigitalWidgetModel model) {
        this.mModel = model;
        attachView(new DualClockDigitalView());
    }

    public void attachView(View view) {
        this.mView = view;
    }

    public void refresh(Context context, int appWidgetId, boolean isSetting) {
        refresh(context, appWidgetId, this.mModel, isSetting);
    }

    public void refresh(Context context, int appWidgetId, DualClockDigitalWidgetModel model, boolean isSetting) {
        this.mView.inflate(context, context.getPackageName(), isSetting, model.getSupportedWidget());
        Log.secD("DualClockDigitalViewModel", "refresh : appWidgetId = " + appWidgetId);
        updateDualClockView(context, getRemoteViews(), appWidgetId);
        this.mView.setBackgroundColorFilter(model.getBackgroundColor());
        this.mView.setBackgroundImageAlpha(model.getTransparency());
        this.mView.setTextColor(model.getTextColor());
        this.mView.setImageColorFilter(model.getImageColor());
        if (isSetting) {
            this.mView.setTextSize(context);
            return;
        }
        Intent intentFirst = new Intent("com.sec.android.app.clockpackage.dualclockdigital.CHANGE_CITY_FIRST");
        intentFirst.putExtra("id", 1);
        intentFirst.putExtra("widId", appWidgetId);
        intentFirst.putExtra("uniqueId", sFirstUniqueId);
        intentFirst.setPackage("com.sec.android.app.clockpackage");
        this.mView.setWidgetClickPendingIntent(R.id.first_layout, PendingIntent.getBroadcast(context, appWidgetId, intentFirst, 0));
        Intent intentSecond = new Intent("com.sec.android.app.clockpackage.dualclockdigital.CHANGE_CITY_SECOND");
        intentSecond.putExtra("id", 2);
        intentSecond.putExtra("widId", appWidgetId);
        if (sSecondUniqueId == -1) {
            intentSecond.putExtra("uniqueId", sFirstUniqueId);
        } else {
            intentSecond.putExtra("uniqueId", sSecondUniqueId);
        }
        intentSecond.setPackage("com.sec.android.app.clockpackage");
        this.mView.setWidgetClickPendingIntent(R.id.second_layout, PendingIntent.getBroadcast(context, appWidgetId, intentSecond, 0));
        if (AppWidgetUtils.canSetFlagForClockWidgetTextViewWidth()) {
            getRemoteViews().setBoolean(R.id.first_clock_text, "setFlagForClockWidgetTextViewWidth", true);
            getRemoteViews().setBoolean(R.id.second_clock_text, "setFlagForClockWidgetTextViewWidth", true);
        }
    }

    public RemoteViews getRemoteViews() {
        return this.mView.getRemoteViews();
    }

    public void setTransparency(Context context, int theme, int transparency) {
        DualClockDigitalDataManager.setTransparency(context, this.mModel, theme, transparency);
    }

    public static void updateDualClock(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        context.createDisplayContext(((DisplayManager) context.getSystemService("display")).getDisplay(0));
        RemoteViews views;
        if (widgetId != -1) {
            views = getView(context, widgetId);
            if (views != null && WidgetSettingUtils.isValidWidgetId(widgetId)) {
                appWidgetManager.updateAppWidget(widgetId, views);
                return;
            }
            return;
        }
        int[] widgetIds = SharedManager.getPrefIDs(context);
        Log.secD("DualClockDigitalViewModel", "updateDualClock widgetIds.length = " + widgetIds.length);
        int i = 0;
        while (i < widgetIds.length) {
            views = getView(context, widgetIds[i]);
            if (views != null && WidgetSettingUtils.isValidWidgetId(widgetIds[i])) {
                appWidgetManager.updateAppWidget(widgetIds[i], views);
            }
            i++;
        }
    }

    private static RemoteViews getView(Context context, int appWidgetId) {
        ViewModel viewModel = new DualClockDigitalViewModel(DualClockDigitalDataManager.loadModel(context, appWidgetId));
        viewModel.refresh(context, appWidgetId, false);
        return viewModel.getRemoteViews();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateDualClockView(android.content.Context r25, android.widget.RemoteViews r26, int r27) {
        /*
        r24 = this;
        r0 = r24;
        r1 = r25;
        r2 = r27;
        r3 = r0.defaultUpdateViews(r1, r2);
        if (r3 == 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r3 = com.sec.android.app.clockpackage.worldclock.model.CityManager.sCities;
        if (r3 == 0) goto L_0x0015;
    L_0x0011:
        r3 = com.sec.android.app.clockpackage.worldclock.model.CityManager.sCitiesId;
        if (r3 != 0) goto L_0x0018;
    L_0x0015:
        com.sec.android.app.clockpackage.worldclock.model.CityManager.initCity(r25);
    L_0x0018:
        r8 = "homezone asc";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "homezone > 0 and wid = ";
        r3 = r3.append(r4);
        r0 = r27;
        r3 = r3.append(r0);
        r6 = r3.toString();
        r3 = r25.getContentResolver();
        r4 = com.sec.android.widgetapp.dualclockdigital.DualClockDigital.DATA_URI;
        r5 = 0;
        r7 = 0;
        r17 = r3.query(r4, r5, r6, r7, r8);
        if (r17 == 0) goto L_0x000c;
    L_0x003d:
        r17.moveToFirst();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = 5;
        r0 = r17;
        r12 = r0.getInt(r3);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = java.lang.Integer.valueOf(r12);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r20 = com.sec.android.app.clockpackage.worldclock.model.CityManager.findCityByUniqueId(r3);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        if (r20 == 0) goto L_0x00d5;
    L_0x0051:
        r13 = r20.getTimeZone();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r11 = r20.getName();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = "DualClockDigitalViewModel";
        r4 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4.<init>();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r5 = "updateDualClockView firstCityName = ";
        r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.append(r11);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.toString();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r4);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r18 = r17.getCount();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = "DualClockDigitalViewModel";
        r4 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4.<init>();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r5 = "count:  ";
        r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r5 = java.lang.String.valueOf(r18);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.toString();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r4);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r15 = -1;
        r14 = 0;
        r16 = 0;
        r3 = 1;
        r0 = r18;
        if (r0 <= r3) goto L_0x00ce;
    L_0x009a:
        r17.moveToNext();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = 5;
        r0 = r17;
        r15 = r0.getInt(r3);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = java.lang.Integer.valueOf(r15);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r23 = com.sec.android.app.clockpackage.worldclock.model.CityManager.findCityByUniqueId(r3);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        if (r23 == 0) goto L_0x00ce;
    L_0x00ae:
        r16 = r23.getTimeZone();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r14 = r23.getName();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r3 = "DualClockDigitalViewModel";
        r4 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4.<init>();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r5 = "updateDualClockView secondCityName = ";
        r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.append(r14);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r4 = r4.toString();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r4);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
    L_0x00ce:
        r9 = r24;
        r10 = r26;
        r9.updateDualClockData(r10, r11, r12, r13, r14, r15, r16);	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
    L_0x00d5:
        r17.close();	 Catch:{ NullPointerException -> 0x00dd, SQLiteFullException -> 0x00ff, Exception -> 0x011a }
        r17.close();
        goto L_0x000c;
    L_0x00dd:
        r21 = move-exception;
        r3 = "DualClockDigitalViewModel";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013c }
        r4.<init>();	 Catch:{ all -> 0x013c }
        r5 = "NullPointerException : ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013c }
        r5 = r21.toString();	 Catch:{ all -> 0x013c }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013c }
        r4 = r4.toString();	 Catch:{ all -> 0x013c }
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r4);	 Catch:{ all -> 0x013c }
        r17.close();
        goto L_0x000c;
    L_0x00ff:
        r22 = move-exception;
        r3 = r25.getResources();	 Catch:{ all -> 0x013c }
        r4 = 2131361993; // 0x7f0a00c9 float:1.8343754E38 double:1.0530327396E-314;
        r3 = r3.getString(r4);	 Catch:{ all -> 0x013c }
        r4 = 0;
        r0 = r25;
        r3 = android.widget.Toast.makeText(r0, r3, r4);	 Catch:{ all -> 0x013c }
        r3.show();	 Catch:{ all -> 0x013c }
        r17.close();
        goto L_0x000c;
    L_0x011a:
        r19 = move-exception;
        r3 = "DualClockDigitalViewModel";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013c }
        r4.<init>();	 Catch:{ all -> 0x013c }
        r5 = "Exception : ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013c }
        r5 = r19.toString();	 Catch:{ all -> 0x013c }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013c }
        r4 = r4.toString();	 Catch:{ all -> 0x013c }
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r4);	 Catch:{ all -> 0x013c }
        r17.close();
        goto L_0x000c;
    L_0x013c:
        r3 = move-exception;
        r17.close();
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalViewModel.updateDualClockView(android.content.Context, android.widget.RemoteViews, int):void");
    }

    private void updateDualClockData(RemoteViews v, String firstCityName, int firstUniqueId, TimeZone firstTimeZone, String secondCityName, int secondUniqueId, TimeZone secondTimeZone) {
        Log.secD("DualClockDigitalViewModel", "firstCityName : " + firstCityName);
        Log.secD("DualClockDigitalViewModel", "firstTimeZone : " + firstTimeZone);
        Log.secD("DualClockDigitalViewModel", "secondCityName : " + secondCityName);
        Log.secD("DualClockDigitalViewModel", "secondUniqueId : " + secondUniqueId);
        Log.secD("DualClockDigitalViewModel", "secondTimeZone : " + secondTimeZone);
        v.setViewVisibility(R.id.first_city_layout, 0);
        Log.secD("DualClockDigitalViewModel", "firstTimeZone.getID() : " + firstTimeZone.getID());
        v.setString(R.id.first_clock_text, "setTimeZone", firstTimeZone.getID());
        v.setString(R.id.first_ampm, "setTimeZone", firstTimeZone.getID());
        v.setString(R.id.first_ampm_left, "setTimeZone", firstTimeZone.getID());
        drawTime(v, true);
        drawAmPm(v, true);
        drawCityText(v, R.id.first_city_text, firstCityName);
        sFirstCityName = firstCityName;
        drawDate(v, firstTimeZone.getID(), true);
        sFirstUniqueId = firstUniqueId;
        sSecondUniqueId = secondUniqueId;
        if (secondUniqueId != -1) {
            v.setViewVisibility(R.id.second_city_layout, 0);
            v.setString(R.id.second_clock_text, "setTimeZone", secondTimeZone.getID());
            v.setString(R.id.second_ampm, "setTimeZone", secondTimeZone.getID());
            v.setString(R.id.second_ampm_left, "setTimeZone", secondTimeZone.getID());
            drawTime(v, false);
            drawAmPm(v, false);
            drawCityText(v, R.id.second_city_text, secondCityName);
            sSecondCityName = secondCityName;
            drawDate(v, secondTimeZone.getID(), false);
            v.setViewVisibility(R.id.second_set_city_layout, 8);
            v.setViewVisibility(R.id.second_city_and_clock_layout, 0);
            return;
        }
        v.setViewVisibility(R.id.second_set_city_layout, 0);
        v.setViewVisibility(R.id.second_city_and_clock_layout, 8);
    }

    private void drawTime(RemoteViews v, boolean isFirst) {
        v.setViewVisibility(isFirst ? R.id.first_clock_text : R.id.second_clock_text, 0);
    }

    private void drawAmPm(RemoteViews v, boolean isFirst) {
        int viewId = isFirst ? R.id.first_ampm : R.id.second_ampm;
        int viewIdLeft = isFirst ? R.id.first_ampm_left : R.id.second_ampm_left;
        if (("HK".equalsIgnoreCase(Locale.getDefault().getCountry()) && !"zh-Hans-HK".equalsIgnoreCase(Locale.getDefault().toLanguageTag())) || "SG".equalsIgnoreCase(Locale.getDefault().getCountry()) || "TW".equalsIgnoreCase(Locale.getDefault().getCountry())) {
            v.setTextViewTextSize(viewId, 1, 11.0f);
        } else if ("ES".equalsIgnoreCase(Locale.getDefault().getCountry())) {
            v.setTextViewTextSize(viewId, 1, 12.5f);
        }
        if (StateUtils.isLeftAmPm()) {
            v.setViewVisibility(viewId, 8);
            v.setViewVisibility(viewIdLeft, 0);
            return;
        }
        v.setViewVisibility(viewIdLeft, 8);
        v.setViewVisibility(viewId, 0);
    }

    private void drawCityText(RemoteViews v, int cityTextViewId, String zoneName) {
        Log.secD("DualClockDigitalViewModel", "drawCityText : zoneName = " + zoneName);
        v.setTextViewText(cityTextViewId, zoneName);
    }

    private void drawDate(RemoteViews v, String timeZoneId, boolean isFirst) {
        int viewId = isFirst ? R.id.date_text_first : R.id.date_text_second;
        Log.secD("DualClockDigitalViewModel", "drawDate viewId : " + viewId);
        Log.secD("DualClockDigitalViewModel", "drawDate timeZoneId : " + timeZoneId);
        v.setString(viewId, "setTimeZone", timeZoneId);
        if (Locale.getDefault().getLanguage().equals(Locale.KOREA.getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMMM"));
        } else if ("de".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "my".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "bo".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
        } else if ("lt".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM EEE"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM-d-EEE"));
        } else {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMM"));
        }
        v.setViewVisibility(viewId, 0);
    }

    private boolean defaultUpdateViews(Context context, int widgetId) {
        boolean needDefaultUpdate = false;
        Cursor cursor = context.getContentResolver().query(DualClockDigital.DATA_URI, null, "wid = " + widgetId, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                City city = CityManager.getCityOfDefaultTime(context);
                if (city != null) {
                    Log.secD("DualClockDigitalViewModel", "defaultUpdateViews count = 0 city.getUniqueId() = " + city.getUniqueId());
                    DualClockUtils.saveDBCityCountry(context, 1, widgetId, city.getUniqueId());
                }
                needDefaultUpdate = true;
            } else {
                needDefaultUpdate = false;
            }
            cursor.close();
        }
        return needDefaultUpdate;
    }
}
