package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        String prefix = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        int i = 0;
        while (i < 100) {
            File file = new File(context.getCacheDir(), prefix + i);
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException e) {
            }
        }
        return null;
    }

    private static ByteBuffer mmap(File file) {
        Throwable th;
        Throwable th2;
        try {
            FileInputStream fis = new FileInputStream(file);
            Throwable th3 = null;
            try {
                FileChannel channel = fis.getChannel();
                ByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                if (fis == null) {
                    return map;
                }
                if (null != null) {
                    try {
                        fis.close();
                        return map;
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                        return map;
                    }
                }
                fis.close();
                return map;
            } catch (Throwable th5) {
                th4 = th5;
                th2 = th;
            }
            throw th4;
            if (fis != null) {
                if (th2 != null) {
                    try {
                        fis.close();
                    } catch (Throwable th6) {
                        th2.addSuppressed(th6);
                    }
                } else {
                    fis.close();
                }
            }
            throw th4;
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r13, android.os.CancellationSignal r14, android.net.Uri r15) {
        /*
        r10 = 0;
        r9 = r13.getContentResolver();
        r1 = "r";
        r8 = r9.openFileDescriptor(r15, r1, r14);	 Catch:{ IOException -> 0x0046 }
        r11 = 0;
        r7 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        r1 = r8.getFileDescriptor();	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        r7.<init>(r1);	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        r12 = 0;
        r0 = r7.getChannel();	 Catch:{ Throwable -> 0x005a, all -> 0x0078 }
        r4 = r0.size();	 Catch:{ Throwable -> 0x005a, all -> 0x0078 }
        r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x005a, all -> 0x0078 }
        r2 = 0;
        r1 = r0.map(r1, r2, r4);	 Catch:{ Throwable -> 0x005a, all -> 0x0078 }
        if (r7 == 0) goto L_0x002d;
    L_0x0028:
        if (r10 == 0) goto L_0x0049;
    L_0x002a:
        r7.close();	 Catch:{ Throwable -> 0x0035, all -> 0x004d }
    L_0x002d:
        if (r8 == 0) goto L_0x0034;
    L_0x002f:
        if (r10 == 0) goto L_0x0056;
    L_0x0031:
        r8.close();	 Catch:{ Throwable -> 0x0051 }
    L_0x0034:
        return r1;
    L_0x0035:
        r2 = move-exception;
        r12.addSuppressed(r2);	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        goto L_0x002d;
    L_0x003a:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x003c }
    L_0x003c:
        r2 = move-exception;
        r3 = r1;
    L_0x003e:
        if (r8 == 0) goto L_0x0045;
    L_0x0040:
        if (r3 == 0) goto L_0x0074;
    L_0x0042:
        r8.close();	 Catch:{ Throwable -> 0x006f }
    L_0x0045:
        throw r2;	 Catch:{ IOException -> 0x0046 }
    L_0x0046:
        r6 = move-exception;
        r1 = r10;
        goto L_0x0034;
    L_0x0049:
        r7.close();	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        goto L_0x002d;
    L_0x004d:
        r1 = move-exception;
        r2 = r1;
        r3 = r10;
        goto L_0x003e;
    L_0x0051:
        r2 = move-exception;
        r11.addSuppressed(r2);	 Catch:{ IOException -> 0x0046 }
        goto L_0x0034;
    L_0x0056:
        r8.close();	 Catch:{ IOException -> 0x0046 }
        goto L_0x0034;
    L_0x005a:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x005c }
    L_0x005c:
        r2 = move-exception;
        r3 = r1;
    L_0x005e:
        if (r7 == 0) goto L_0x0065;
    L_0x0060:
        if (r3 == 0) goto L_0x006b;
    L_0x0062:
        r7.close();	 Catch:{ Throwable -> 0x0066, all -> 0x004d }
    L_0x0065:
        throw r2;	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
    L_0x0066:
        r1 = move-exception;
        r3.addSuppressed(r1);	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        goto L_0x0065;
    L_0x006b:
        r7.close();	 Catch:{ Throwable -> 0x003a, all -> 0x004d }
        goto L_0x0065;
    L_0x006f:
        r1 = move-exception;
        r3.addSuppressed(r1);	 Catch:{ IOException -> 0x0046 }
        goto L_0x0045;
    L_0x0074:
        r8.close();	 Catch:{ IOException -> 0x0046 }
        goto L_0x0045;
    L_0x0078:
        r1 = move-exception;
        r2 = r1;
        r3 = r10;
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    public static ByteBuffer copyToDirectBuffer(Context context, Resources res, int id) {
        ByteBuffer byteBuffer = null;
        File tmpFile = getTempFile(context);
        if (tmpFile != null) {
            try {
                if (copyToFile(tmpFile, res, id)) {
                    byteBuffer = mmap(tmpFile);
                    tmpFile.delete();
                }
            } finally {
                tmpFile.delete();
            }
        }
        return byteBuffer;
    }

    public static boolean copyToFile(File file, InputStream is) {
        IOException e;
        Throwable th;
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream os = new FileOutputStream(file, false);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int readLen = is.read(buffer);
                    if (readLen != -1) {
                        os.write(buffer, 0, readLen);
                    } else {
                        closeQuietly(os);
                        fileOutputStream = os;
                        return true;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = os;
                try {
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(fileOutputStream);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = os;
                closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
            closeQuietly(fileOutputStream);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream inputStream = null;
        try {
            inputStream = res.openRawResource(id);
            boolean copyToFile = copyToFile(file, inputStream);
            return copyToFile;
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
