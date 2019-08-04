package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender.OnHighlightResultListener;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder.HighlightButtonClickListener;

public class TimerToneViewModel {
    private Activity mActivity = null;
    private AlertDialog mHighlightDialog;
    private HighlightDialogBuilder mHighlightDialogBuilder;
    private int mOffset = 0;
    private RingtoneRecommender mRecommender;
    private OnHighlightResultListener mResultListener = new C08352();

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerToneViewModel$2 */
    class C08352 implements OnHighlightResultListener {
        C08352() {
        }

        public void onResult(int status, int offset) {
            Log.secD("TimerToneViewModel", "mResultListener() - status: " + status + " offset: " + offset);
            if (status == 1) {
                TimerToneViewModel.this.mOffset = offset;
            } else {
                TimerToneViewModel.this.mOffset = 0;
            }
            Log.secD("TimerToneViewModel", "mResultListener() - status: " + status + " mOffset: " + TimerToneViewModel.this.mOffset);
            TimerToneViewModel.this.mRecommender.close();
        }
    }

    private boolean isExistRingtoneHighlight(android.net.Uri r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Incorrect nodes count for selectOther: B:21:0x00a7 in [B:20:0x00a5, B:21:0x00a7, B:30:0x005f, B:29:0x005f]
	at jadx.core.utils.BlockUtils.selectOther(BlockUtils.java:53)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:64)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        r7 = this;
        r2 = 0;
        r3 = 1;
        if (r8 == 0) goto L_0x005f;
    L_0x0004:
        r4 = "TimerToneViewModel";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "pickedUri = ";
        r5 = r5.append(r6);
        r5 = r5.append(r8);
        r5 = r5.toString();
        com.sec.android.app.clockpackage.common.util.Log.secI(r4, r5);
        r4 = com.sec.android.app.clockpackage.common.feature.Feature.isMusicAutoRecommendationSupported();
        if (r4 == 0) goto L_0x005f;
    L_0x0022:
        r1 = r7.getSongInfo(r8);
        r4 = new com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender;
        r4.<init>();
        r7.mRecommender = r4;
        r4 = "TimerToneViewModel";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "filePath = ";
        r5 = r5.append(r6);
        r5 = r5.append(r1);
        r5 = r5.toString();
        com.sec.android.app.clockpackage.common.util.Log.secI(r4, r5);
        if (r1 != 0) goto L_0x0060;
    L_0x0047:
        r3 = "TimerToneViewModel";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "There is no URI!! Set default URI pickedUri was = ";
        r4 = r4.append(r5);
        r4 = r4.append(r8);
        r4 = r4.toString();
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r4);
    L_0x005f:
        return r2;
    L_0x0060:
        r4 = r7.mRecommender;
        r4 = r4.checkFile(r1);
        if (r4 == 0) goto L_0x005f;
    L_0x0068:
        r4 = r7.mRecommender;
        r5 = r7.mResultListener;
        r4.doExtract(r1, r5);
    L_0x006f:
        r4 = r7.mRecommender;	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r4 = r4.isOpen();	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        if (r4 != 0) goto L_0x006f;
    L_0x0077:
        r4 = r7.mOffset;
        if (r4 == 0) goto L_0x005f;
    L_0x007b:
        r2 = r3;
        goto L_0x005f;
    L_0x007d:
        r0 = move-exception;
        r4 = "TimerToneViewModel";	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r5 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r5.<init>();	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r6 = "RuntimeException : ";	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r5 = r5.append(r6);	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r6 = r0.toString();	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r5 = r5.append(r6);	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r5 = r5.toString();	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        com.sec.android.app.clockpackage.common.util.Log.secE(r4, r5);	 Catch:{ RuntimeException -> 0x007d, all -> 0x00a0 }
        r4 = r7.mOffset;
        if (r4 == 0) goto L_0x005f;
    L_0x009e:
        r2 = r3;
        goto L_0x005f;
    L_0x00a0:
        r2 = move-exception;
        r4 = r7.mOffset;
        if (r4 == 0) goto L_0x00a7;
    L_0x00a5:
        r2 = r3;
        goto L_0x005f;
    L_0x00a7:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.timer.viewmodel.TimerToneViewModel.isExistRingtoneHighlight(android.net.Uri):boolean");
    }

    public HighlightDialogBuilder getHighlightDialogBuilder() {
        return this.mHighlightDialogBuilder;
    }

    public TimerToneViewModel(Activity activity) {
        this.mActivity = activity;
    }

    public void onActivityResult(int resultCode, Intent data, TimerManager timerManager) {
        if (resultCode != -1) {
            Log.secD("TimerToneViewModel", "resultCode != RESULT_OK");
            return;
        }
        Uri pickedUri = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
        boolean bNeedHighlightPopUp = data.getBooleanExtra("ringtone_highlight_popup", false);
        Log.secD("TimerToneViewModel", "pickedUri = " + pickedUri + " bNeedHighlightPopUp = " + bNeedHighlightPopUp);
        int volume = data.getIntExtra("ringtone_volume_value", 11);
        boolean bIsVibOn = data.getBooleanExtra("ringtone_vibration_on", false);
        timerManager.setTimerSoundValue(pickedUri, volume, bIsVibOn);
        if (bNeedHighlightPopUp && isExistRingtoneHighlight(pickedUri)) {
            showHighlightDialog(pickedUri, volume, bIsVibOn, timerManager);
        }
    }

    private void showHighlightDialog(Uri pickedUri, final int volume, final boolean bIsVibOn, final TimerManager timerManager) {
        Log.secD("TimerToneViewModel", "showHighlightDialog() pickedUri : " + pickedUri);
        this.mHighlightDialogBuilder = new HighlightDialogBuilder(this.mActivity, pickedUri, this.mOffset, 1, timerManager.getTimerVolume());
        this.mHighlightDialogBuilder.setOnHighlightButtonClickListener(new HighlightButtonClickListener() {
            public void onPositiveButtonClick(Uri uri) {
                Log.secD("TimerToneViewModel", "onPositiveButtonClick uri : " + uri);
                timerManager.setTimerSoundValue(uri, volume, bIsVibOn);
            }

            public void onDismiss(int volume) {
                Log.secD("TimerToneViewModel", "onDismiss");
            }
        });
        this.mHighlightDialog = this.mHighlightDialogBuilder.create();
        this.mHighlightDialog.setVolumeControlStream(4);
        this.mHighlightDialog.show();
    }

    private String getSongInfo(Uri uri) {
        Cursor c = null;
        String filePath = null;
        if (uri != null) {
            try {
                c = this.mActivity.getContentResolver().query(uri, new String[]{"_data", "title"}, null, null, null);
            } catch (RuntimeException e) {
                Log.secE("TimerToneViewModel", "RuntimeException : " + e.toString());
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (c != null && c.moveToFirst()) {
            filePath = c.getString(c.getColumnIndex("_data"));
        }
        if (c != null) {
            c.close();
        }
        return filePath;
    }

    public void releaseInstance() {
        if (this.mHighlightDialog != null) {
            this.mHighlightDialog.cancel();
            this.mHighlightDialog = null;
        }
        if (this.mHighlightDialogBuilder != null) {
            this.mHighlightDialogBuilder.removeInstance();
            this.mHighlightDialogBuilder = null;
        }
        this.mRecommender = null;
    }

    public Intent getTimerToneIntent(TimerManager timerManager) {
        Context context = this.mActivity.getApplicationContext();
        Intent intent = new Intent();
        if (context != null) {
            Log.secD("TimerToneViewModel", "getTimerToneIntent");
            intent.setClassName(context, "com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity");
            Uri uri = RingtoneManager.getDefaultUri(1);
            Uri savedTimerToneUri = timerManager.getTimerSoundUri();
            if (Uri.EMPTY.equals(savedTimerToneUri)) {
                uri = savedTimerToneUri;
            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(context, savedTimerToneUri);
                if (ringtone != null && ringtone.semIsUriValid()) {
                    uri = savedTimerToneUri;
                }
            }
            intent.putExtra("ringtone_mode", 1);
            intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
            intent.putExtra("ringtone_volume_value", timerManager.getTimerVolume());
            intent.putExtra("ringtone_vibration_on", timerManager.isTimerVibOn());
            intent.setFlags(393216);
        }
        return intent;
    }
}
