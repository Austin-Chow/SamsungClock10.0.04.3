package android.support.v4.provider;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.SelfDestructiveThread.ReplyCallback;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontsContractCompat {
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    public static final String PARCEL_FONT_RESULTS = "font_results";
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS);
    private static final Comparator<byte[]> sByteArrayComparator = new C01575();
    private static final Object sLock = new Object();
    private static final SimpleArrayMap<String, ArrayList<ReplyCallback<TypefaceResult>>> sPendingReplies = new SimpleArrayMap();
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);

    /* renamed from: android.support.v4.provider.FontsContractCompat$5 */
    static class C01575 implements Comparator<byte[]> {
        C01575() {
        }

        public int compare(byte[] l, byte[] r) {
            if (l.length != r.length) {
                return l.length - r.length;
            }
            for (int i = 0; i < l.length; i++) {
                if (l[i] != r[i]) {
                    return l[i] - r[i];
                }
            }
            return 0;
        }
    }

    public static final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @Retention(RetentionPolicy.SOURCE)
        @interface FontResultStatus {
        }

        public FontFamilyResult(int statusCode, FontInfo[] fonts) {
            this.mStatusCode = statusCode;
            this.mFonts = fonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }
    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int ttcIndex, int weight, boolean italic, int resultCode) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = ttcIndex;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mResultCode = resultCode;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        public static final int RESULT_OK = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface FontRequestFailReason {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        public void onTypefaceRequestFailed(int reason) {
        }
    }

    private static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(Typeface typeface, int result) {
            this.mTypeface = typeface;
            this.mResult = result;
        }
    }

    private FontsContractCompat() {
    }

    private static TypefaceResult getFontInternal(Context context, FontRequest request, int style) {
        int i = -3;
        try {
            FontFamilyResult result = fetchFonts(context, null, request);
            if (result.getStatusCode() == 0) {
                Typeface typeface = TypefaceCompat.createFromFontInfo(context, null, result.getFonts(), style);
                if (typeface != null) {
                    i = 0;
                }
                return new TypefaceResult(typeface, i);
            }
            int resultCode;
            if (result.getStatusCode() == 1) {
                resultCode = -2;
            } else {
                resultCode = -3;
            }
            return new TypefaceResult(null, resultCode);
        } catch (NameNotFoundException e) {
            return new TypefaceResult(null, -1);
        }
    }

    public static final void resetCache() {
        sTypefaceCache.evictAll();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Typeface getFontSync(final android.content.Context r9, final android.support.v4.provider.FontRequest r10, final android.support.v4.content.res.ResourcesCompat.FontCallback r11, final android.os.Handler r12, boolean r13, int r14, final int r15) {
        /*
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r10.getIdentifier();
        r7 = r7.append(r8);
        r8 = "-";
        r7 = r7.append(r8);
        r7 = r7.append(r15);
        r3 = r7.toString();
        r7 = sTypefaceCache;
        r0 = r7.get(r3);
        r0 = (android.graphics.Typeface) r0;
        if (r0 == 0) goto L_0x002b;
    L_0x0025:
        if (r11 == 0) goto L_0x002a;
    L_0x0027:
        r11.onFontRetrieved(r0);
    L_0x002a:
        return r0;
    L_0x002b:
        if (r13 == 0) goto L_0x0048;
    L_0x002d:
        r7 = -1;
        if (r14 != r7) goto L_0x0048;
    L_0x0030:
        r6 = getFontInternal(r9, r10, r15);
        if (r11 == 0) goto L_0x003f;
    L_0x0036:
        r7 = r6.mResult;
        if (r7 != 0) goto L_0x0042;
    L_0x003a:
        r7 = r6.mTypeface;
        r11.callbackSuccessAsync(r7, r12);
    L_0x003f:
        r0 = r6.mTypeface;
        goto L_0x002a;
    L_0x0042:
        r7 = r6.mResult;
        r11.callbackFailAsync(r7, r12);
        goto L_0x003f;
    L_0x0048:
        r2 = new android.support.v4.provider.FontsContractCompat$1;
        r2.<init>(r9, r10, r15, r3);
        if (r13 == 0) goto L_0x005d;
    L_0x004f:
        r7 = sBackgroundThread;	 Catch:{ InterruptedException -> 0x005a }
        r7 = r7.postAndWait(r2, r14);	 Catch:{ InterruptedException -> 0x005a }
        r7 = (android.support.v4.provider.FontsContractCompat.TypefaceResult) r7;	 Catch:{ InterruptedException -> 0x005a }
        r0 = r7.mTypeface;	 Catch:{ InterruptedException -> 0x005a }
        goto L_0x002a;
    L_0x005a:
        r1 = move-exception;
        r0 = 0;
        goto L_0x002a;
    L_0x005d:
        if (r11 != 0) goto L_0x007e;
    L_0x005f:
        r5 = 0;
    L_0x0060:
        r8 = sLock;
        monitor-enter(r8);
        r7 = sPendingReplies;	 Catch:{ all -> 0x007b }
        r7 = r7.containsKey(r3);	 Catch:{ all -> 0x007b }
        if (r7 == 0) goto L_0x0084;
    L_0x006b:
        if (r5 == 0) goto L_0x0078;
    L_0x006d:
        r7 = sPendingReplies;	 Catch:{ all -> 0x007b }
        r7 = r7.get(r3);	 Catch:{ all -> 0x007b }
        r7 = (java.util.ArrayList) r7;	 Catch:{ all -> 0x007b }
        r7.add(r5);	 Catch:{ all -> 0x007b }
    L_0x0078:
        r0 = 0;
        monitor-exit(r8);	 Catch:{ all -> 0x007b }
        goto L_0x002a;
    L_0x007b:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x007b }
        throw r7;
    L_0x007e:
        r5 = new android.support.v4.provider.FontsContractCompat$2;
        r5.<init>(r11, r12);
        goto L_0x0060;
    L_0x0084:
        if (r5 == 0) goto L_0x0093;
    L_0x0086:
        r4 = new java.util.ArrayList;	 Catch:{ all -> 0x007b }
        r4.<init>();	 Catch:{ all -> 0x007b }
        r4.add(r5);	 Catch:{ all -> 0x007b }
        r7 = sPendingReplies;	 Catch:{ all -> 0x007b }
        r7.put(r3, r4);	 Catch:{ all -> 0x007b }
    L_0x0093:
        monitor-exit(r8);	 Catch:{ all -> 0x007b }
        r7 = sBackgroundThread;
        r8 = new android.support.v4.provider.FontsContractCompat$3;
        r8.<init>(r3);
        r7.postAndReply(r2, r8);
        r0 = 0;
        goto L_0x002a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.FontsContractCompat.getFontSync(android.content.Context, android.support.v4.provider.FontRequest, android.support.v4.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean, int, int):android.graphics.Typeface");
    }

    public static void requestFont(final Context context, final FontRequest request, final FontRequestCallback callback, Handler handler) {
        final Handler callerThreadHandler = new Handler();
        handler.post(new Runnable() {

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$1 */
            class C01471 implements Runnable {
                C01471() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-1);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$2 */
            class C01482 implements Runnable {
                C01482() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-2);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$3 */
            class C01493 implements Runnable {
                C01493() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-3);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$4 */
            class C01504 implements Runnable {
                C01504() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-3);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$5 */
            class C01515 implements Runnable {
                C01515() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(1);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$6 */
            class C01526 implements Runnable {
                C01526() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-3);
                }
            }

            /* renamed from: android.support.v4.provider.FontsContractCompat$4$8 */
            class C01548 implements Runnable {
                C01548() {
                }

                public void run() {
                    callback.onTypefaceRequestFailed(-3);
                }
            }

            public void run() {
                try {
                    FontFamilyResult result = FontsContractCompat.fetchFonts(context, null, request);
                    if (result.getStatusCode() != 0) {
                        switch (result.getStatusCode()) {
                            case 1:
                                callerThreadHandler.post(new C01482());
                                return;
                            case 2:
                                callerThreadHandler.post(new C01493());
                                return;
                            default:
                                callerThreadHandler.post(new C01504());
                                return;
                        }
                    }
                    FontInfo[] fonts = result.getFonts();
                    if (fonts == null || fonts.length == 0) {
                        callerThreadHandler.post(new C01515());
                        return;
                    }
                    for (FontInfo font : fonts) {
                        if (font.getResultCode() != 0) {
                            final int resultCode = font.getResultCode();
                            if (resultCode < 0) {
                                callerThreadHandler.post(new C01526());
                                return;
                            } else {
                                callerThreadHandler.post(new Runnable() {
                                    public void run() {
                                        callback.onTypefaceRequestFailed(resultCode);
                                    }
                                });
                                return;
                            }
                        }
                    }
                    final Typeface typeface = FontsContractCompat.buildTypeface(context, null, fonts);
                    if (typeface == null) {
                        callerThreadHandler.post(new C01548());
                    } else {
                        callerThreadHandler.post(new Runnable() {
                            public void run() {
                                callback.onTypefaceRetrieved(typeface);
                            }
                        });
                    }
                } catch (NameNotFoundException e) {
                    callerThreadHandler.post(new C01471());
                }
            }
        });
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fonts) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fonts, 0);
    }

    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] fonts, CancellationSignal cancellationSignal) {
        HashMap<Uri, ByteBuffer> out = new HashMap();
        for (FontInfo font : fonts) {
            if (font.getResultCode() == 0) {
                Uri uri = font.getUri();
                if (!out.containsKey(uri)) {
                    out.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return Collections.unmodifiableMap(out);
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest request) throws NameNotFoundException {
        ProviderInfo providerInfo = getProvider(context.getPackageManager(), request, context.getResources());
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, getFontFromProvider(context, request, providerInfo.authority, cancellationSignal));
    }

    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest request, Resources resources) throws NameNotFoundException {
        String providerAuthority = request.getProviderAuthority();
        ProviderInfo info = packageManager.resolveContentProvider(providerAuthority, 0);
        if (info == null) {
            throw new NameNotFoundException("No package found for authority: " + providerAuthority);
        } else if (info.packageName.equals(request.getProviderPackage())) {
            List<byte[]> signatures = convertToByteArrayList(packageManager.getPackageInfo(info.packageName, 64).signatures);
            Collections.sort(signatures, sByteArrayComparator);
            List<List<byte[]>> requestCertificatesList = getCertificates(request, resources);
            for (int i = 0; i < requestCertificatesList.size(); i++) {
                List<byte[]> requestSignatures = new ArrayList((Collection) requestCertificatesList.get(i));
                Collections.sort(requestSignatures, sByteArrayComparator);
                if (equalsByteArrayList(signatures, requestSignatures)) {
                    return info;
                }
            }
            return null;
        } else {
            throw new NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + request.getProviderPackage());
        }
    }

    private static List<List<byte[]>> getCertificates(FontRequest request, Resources resources) {
        if (request.getCertificates() != null) {
            return request.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, request.getCertificatesArrayResId());
    }

    private static boolean equalsByteArrayList(List<byte[]> signatures, List<byte[]> requestSignatures) {
        if (signatures.size() != requestSignatures.size()) {
            return false;
        }
        for (int i = 0; i < signatures.size(); i++) {
            if (!Arrays.equals((byte[]) signatures.get(i), (byte[]) requestSignatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatures) {
        List<byte[]> shas = new ArrayList();
        for (Signature toByteArray : signatures) {
            shas.add(toByteArray.toByteArray());
        }
        return shas;
    }

    static FontInfo[] getFontFromProvider(Context context, FontRequest request, String authority, CancellationSignal cancellationSignal) {
        Throwable th;
        ArrayList<FontInfo> result = new ArrayList();
        Uri uri = new Builder().scheme("content").authority(authority).build();
        Uri fileBaseUri = new Builder().scheme("content").authority(authority).appendPath("file").build();
        Cursor cursor = null;
        try {
            if (VERSION.SDK_INT > 16) {
                cursor = context.getContentResolver().query(uri, new String[]{"_id", Columns.FILE_ID, Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{request.getQuery()}, null, cancellationSignal);
            } else {
                cursor = context.getContentResolver().query(uri, new String[]{"_id", Columns.FILE_ID, Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{request.getQuery()}, null);
            }
            if (cursor != null && cursor.getCount() > 0) {
                int resultCodeColumnIndex = cursor.getColumnIndex(Columns.RESULT_CODE);
                ArrayList<FontInfo> result2 = new ArrayList();
                try {
                    int idColumnIndex = cursor.getColumnIndex("_id");
                    int fileIdColumnIndex = cursor.getColumnIndex(Columns.FILE_ID);
                    int ttcIndexColumnIndex = cursor.getColumnIndex(Columns.TTC_INDEX);
                    int weightColumnIndex = cursor.getColumnIndex(Columns.WEIGHT);
                    int italicColumnIndex = cursor.getColumnIndex(Columns.ITALIC);
                    while (cursor.moveToNext()) {
                        Uri fileUri;
                        int resultCode = resultCodeColumnIndex != -1 ? cursor.getInt(resultCodeColumnIndex) : 0;
                        int ttcIndex = ttcIndexColumnIndex != -1 ? cursor.getInt(ttcIndexColumnIndex) : 0;
                        if (fileIdColumnIndex == -1) {
                            fileUri = ContentUris.withAppendedId(uri, cursor.getLong(idColumnIndex));
                        } else {
                            fileUri = ContentUris.withAppendedId(fileBaseUri, cursor.getLong(fileIdColumnIndex));
                        }
                        int weight = weightColumnIndex != -1 ? cursor.getInt(weightColumnIndex) : 400;
                        boolean italic = italicColumnIndex != -1 && cursor.getInt(italicColumnIndex) == 1;
                        result2.add(new FontInfo(fileUri, ttcIndex, weight, italic, resultCode));
                    }
                    result = result2;
                } catch (Throwable th2) {
                    th = th2;
                    result = result2;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return (FontInfo[]) result.toArray(new FontInfo[0]);
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
