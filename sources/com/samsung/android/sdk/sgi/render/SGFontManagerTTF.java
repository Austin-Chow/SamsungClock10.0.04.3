package com.samsung.android.sdk.sgi.render;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;

public final class SGFontManagerTTF {
    public static void loadFontData(AssetManager assetManager, String assetName, String fontName) {
        SGJNI.SGFontManagerTTF_loadFontData__SWIG_0(assetManager, assetName, fontName);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void loadFontData(android.content.res.Resources r4, int r5, java.lang.String r6) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0011 in list [B:4:0x000e]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        r2 = 0;
        r3 = r4.openRawResource(r5);	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        r0 = r3;	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        r0 = (android.content.res.AssetManager.AssetInputStream) r0;	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        r2 = r0;	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        loadFontDataAsset(r2, r6);	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        if (r2 == 0) goto L_0x0011;
    L_0x000e:
        r2.close();
    L_0x0011:
        return;
    L_0x0012:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x0012, all -> 0x001c }
        if (r2 == 0) goto L_0x0011;
    L_0x0018:
        r2.close();
        goto L_0x0011;
    L_0x001c:
        r3 = move-exception;
        if (r2 == 0) goto L_0x0022;
    L_0x001f:
        r2.close();
    L_0x0022:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.sdk.sgi.render.SGFontManagerTTF.loadFontData(android.content.res.Resources, int, java.lang.String):void");
    }

    public static void loadFontData(FileDescriptor fd, String name) throws Exception {
        Exception e;
        Throwable th;
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream(fd);
            try {
                loadFontData(in2, name);
                if (in2 != null) {
                    in2.close();
                    in = in2;
                    return;
                }
            } catch (Exception e2) {
                e = e2;
                in = in2;
                try {
                    e.printStackTrace();
                    if (in != null) {
                        in.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (in != null) {
                in.close();
            }
        }
    }

    public static void loadFontData(FileInputStream fileInputStream, String fontName) {
        SGJNI.SGFontManagerTTF_loadFontData__SWIG_1(fileInputStream, fontName);
    }

    private static void loadFontDataAsset(AssetInputStream assetInputStream, String assetName) {
        SGJNI.SGFontManagerTTF_loadFontDataAsset(assetInputStream, assetName);
    }
}
