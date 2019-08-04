package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor fd) {
        try {
            String path = Os.readlink("/proc/self/fd/" + fd.getFd());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            return null;
        } catch (ErrnoException e) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r12, android.os.CancellationSignal r13, android.support.v4.provider.FontsContractCompat.FontInfo[] r14, int r15) {
        /*
        r11 = this;
        r6 = 0;
        r7 = r14.length;
        r8 = 1;
        if (r7 >= r8) goto L_0x0006;
    L_0x0005:
        return r6;
    L_0x0006:
        r0 = r11.findBestInfo(r14, r15);
        r5 = r12.getContentResolver();
        r7 = r0.getUri();	 Catch:{ IOException -> 0x0054 }
        r8 = "r";
        r4 = r5.openFileDescriptor(r7, r8, r13);	 Catch:{ IOException -> 0x0054 }
        r8 = 0;
        r2 = r11.getFile(r4);	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        if (r2 == 0) goto L_0x0025;
    L_0x001f:
        r7 = r2.canRead();	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        if (r7 != 0) goto L_0x007b;
    L_0x0025:
        r3 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        r7 = r4.getFileDescriptor();	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        r3.<init>(r7);	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        r9 = 0;
        r7 = super.createFromInputStream(r12, r3);	 Catch:{ Throwable -> 0x0067, all -> 0x009b }
        if (r3 == 0) goto L_0x003a;
    L_0x0035:
        if (r6 == 0) goto L_0x0056;
    L_0x0037:
        r3.close();	 Catch:{ Throwable -> 0x0043, all -> 0x005a }
    L_0x003a:
        if (r4 == 0) goto L_0x0041;
    L_0x003c:
        if (r6 == 0) goto L_0x0063;
    L_0x003e:
        r4.close();	 Catch:{ Throwable -> 0x005e }
    L_0x0041:
        r6 = r7;
        goto L_0x0005;
    L_0x0043:
        r10 = move-exception;
        r9.addSuppressed(r10);	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        goto L_0x003a;
    L_0x0048:
        r7 = move-exception;
        throw r7;	 Catch:{ all -> 0x004a }
    L_0x004a:
        r8 = move-exception;
        r9 = r7;
    L_0x004c:
        if (r4 == 0) goto L_0x0053;
    L_0x004e:
        if (r9 == 0) goto L_0x0097;
    L_0x0050:
        r4.close();	 Catch:{ Throwable -> 0x0092 }
    L_0x0053:
        throw r8;	 Catch:{ IOException -> 0x0054 }
    L_0x0054:
        r1 = move-exception;
        goto L_0x0005;
    L_0x0056:
        r3.close();	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        goto L_0x003a;
    L_0x005a:
        r7 = move-exception;
        r8 = r7;
        r9 = r6;
        goto L_0x004c;
    L_0x005e:
        r9 = move-exception;
        r8.addSuppressed(r9);	 Catch:{ IOException -> 0x0054 }
        goto L_0x0041;
    L_0x0063:
        r4.close();	 Catch:{ IOException -> 0x0054 }
        goto L_0x0041;
    L_0x0067:
        r8 = move-exception;
        throw r8;	 Catch:{ all -> 0x0069 }
    L_0x0069:
        r7 = move-exception;
    L_0x006a:
        if (r3 == 0) goto L_0x0071;
    L_0x006c:
        if (r8 == 0) goto L_0x0077;
    L_0x006e:
        r3.close();	 Catch:{ Throwable -> 0x0072, all -> 0x005a }
    L_0x0071:
        throw r7;	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
    L_0x0072:
        r9 = move-exception;
        r8.addSuppressed(r9);	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        goto L_0x0071;
    L_0x0077:
        r3.close();	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        goto L_0x0071;
    L_0x007b:
        r7 = android.graphics.Typeface.createFromFile(r2);	 Catch:{ Throwable -> 0x0048, all -> 0x005a }
        if (r4 == 0) goto L_0x0086;
    L_0x0081:
        if (r6 == 0) goto L_0x008e;
    L_0x0083:
        r4.close();	 Catch:{ Throwable -> 0x0089 }
    L_0x0086:
        r6 = r7;
        goto L_0x0005;
    L_0x0089:
        r9 = move-exception;
        r8.addSuppressed(r9);	 Catch:{ IOException -> 0x0054 }
        goto L_0x0086;
    L_0x008e:
        r4.close();	 Catch:{ IOException -> 0x0054 }
        goto L_0x0086;
    L_0x0092:
        r7 = move-exception;
        r9.addSuppressed(r7);	 Catch:{ IOException -> 0x0054 }
        goto L_0x0053;
    L_0x0097:
        r4.close();	 Catch:{ IOException -> 0x0054 }
        goto L_0x0053;
    L_0x009b:
        r7 = move-exception;
        r8 = r6;
        goto L_0x006a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
