package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.support.v4.provider.FontsContractCompat.FontInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class TypefaceCompatBaseImpl implements TypefaceCompatImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    private interface StyleExtractor<T> {
        int getWeight(T t);

        boolean isItalic(T t);
    }

    /* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl$1 */
    class C01271 implements StyleExtractor<FontInfo> {
        C01271() {
        }

        public int getWeight(FontInfo info) {
            return info.getWeight();
        }

        public boolean isItalic(FontInfo info) {
            return info.isItalic();
        }
    }

    /* renamed from: android.support.v4.graphics.TypefaceCompatBaseImpl$2 */
    class C01282 implements StyleExtractor<FontFileResourceEntry> {
        C01282() {
        }

        public int getWeight(FontFileResourceEntry entry) {
            return entry.getWeight();
        }

        public boolean isItalic(FontFileResourceEntry entry) {
            return entry.isItalic();
        }
    }

    TypefaceCompatBaseImpl() {
    }

    private static <T> T findBestFont(T[] fonts, int style, StyleExtractor<T> extractor) {
        boolean isTargetItalic;
        int targetWeight = (style & 1) == 0 ? 400 : 700;
        if ((style & 2) != 0) {
            isTargetItalic = true;
        } else {
            isTargetItalic = false;
        }
        T best = null;
        int bestScore = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        for (T font : fonts) {
            int i;
            int abs = Math.abs(extractor.getWeight(font) - targetWeight) * 2;
            if (extractor.isItalic(font) == isTargetItalic) {
                i = 0;
            } else {
                i = 1;
            }
            int score = abs + i;
            if (best == null || bestScore > score) {
                best = font;
                bestScore = score;
            }
        }
        return best;
    }

    protected FontInfo findBestInfo(FontInfo[] fonts, int style) {
        return (FontInfo) findBestFont(fonts, style, new C01271());
    }

    protected Typeface createFromInputStream(Context context, InputStream is) {
        Typeface typeface = null;
        File tmpFile = TypefaceCompatUtil.getTempFile(context);
        if (tmpFile != null) {
            try {
                if (TypefaceCompatUtil.copyToFile(tmpFile, is)) {
                    typeface = Typeface.createFromFile(tmpFile.getPath());
                    tmpFile.delete();
                }
            } catch (RuntimeException e) {
            } finally {
                tmpFile.delete();
            }
        }
        return typeface;
    }

    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontInfo[] fonts, int style) {
        Typeface typeface = null;
        if (fonts.length >= 1) {
            InputStream is = null;
            try {
                is = context.getContentResolver().openInputStream(findBestInfo(fonts, style).getUri());
                typeface = createFromInputStream(context, is);
            } catch (IOException e) {
            } finally {
                TypefaceCompatUtil.closeQuietly(is);
            }
        }
        return typeface;
    }

    private FontFileResourceEntry findBestEntry(FontFamilyFilesResourceEntry entry, int style) {
        return (FontFileResourceEntry) findBestFont(entry.getEntries(), style, new C01282());
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry entry, Resources resources, int style) {
        FontFileResourceEntry best = findBestEntry(entry, style);
        if (best == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, best.getResourceId(), best.getFileName(), style);
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        Typeface typeface = null;
        File tmpFile = TypefaceCompatUtil.getTempFile(context);
        if (tmpFile != null) {
            try {
                if (TypefaceCompatUtil.copyToFile(tmpFile, resources, id)) {
                    typeface = Typeface.createFromFile(tmpFile.getPath());
                    tmpFile.delete();
                }
            } catch (RuntimeException e) {
            } finally {
                tmpFile.delete();
            }
        }
        return typeface;
    }
}
