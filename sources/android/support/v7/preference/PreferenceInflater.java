package android.support.v7.preference;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PreferenceInflater {
    private static final HashMap<String, Constructor> CONSTRUCTOR_MAP = new HashMap();
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
    private final Object[] mConstructorArgs = new Object[2];
    private final Context mContext;
    private String[] mDefaultPackages;
    private PreferenceManager mPreferenceManager;

    public PreferenceInflater(Context context, PreferenceManager preferenceManager) {
        this.mContext = context;
        init(preferenceManager);
    }

    private void init(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        setDefaultPackages(new String[]{"android.support.v14.preference.", "android.support.v7.preference."});
    }

    public void setDefaultPackages(String[] defaultPackage) {
        this.mDefaultPackages = defaultPackage;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Preference inflate(int resource, PreferenceGroup root) {
        XmlPullParser parser = getContext().getResources().getXml(resource);
        try {
            Preference inflate = inflate(parser, root);
            return inflate;
        } finally {
            parser.close();
        }
    }

    public Preference inflate(XmlPullParser parser, PreferenceGroup root) {
        Preference result;
        synchronized (this.mConstructorArgs) {
            AttributeSet attrs = Xml.asAttributeSet(parser);
            this.mConstructorArgs[0] = this.mContext;
            int type;
            do {
                try {
                    type = parser.next();
                    if (type == 2) {
                        break;
                    }
                } catch (InflateException e) {
                    throw e;
                } catch (XmlPullParserException e2) {
                    InflateException ex = new InflateException(e2.getMessage());
                    ex.initCause(e2);
                    throw ex;
                } catch (IOException e3) {
                    ex = new InflateException(parser.getPositionDescription() + ": " + e3.getMessage());
                    ex.initCause(e3);
                    throw ex;
                }
            } while (type != 1);
            if (type != 2) {
                throw new InflateException(parser.getPositionDescription() + ": No start tag found!");
            }
            result = onMergeRoots(root, (PreferenceGroup) createItemFromTag(parser.getName(), attrs));
            rInflate(parser, result, attrs);
        }
        return result;
    }

    private PreferenceGroup onMergeRoots(PreferenceGroup givenRoot, PreferenceGroup xmlRoot) {
        if (givenRoot != null) {
            return givenRoot;
        }
        xmlRoot.onAttachedToHierarchy(this.mPreferenceManager);
        return xmlRoot;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.support.v7.preference.Preference createItem(java.lang.String r12, java.lang.String[] r13, android.util.AttributeSet r14) throws java.lang.ClassNotFoundException, android.view.InflateException {
        /*
        r11 = this;
        r8 = CONSTRUCTOR_MAP;
        r3 = r8.get(r12);
        r3 = (java.lang.reflect.Constructor) r3;
        if (r3 != 0) goto L_0x0029;
    L_0x000a:
        r8 = r11.mContext;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r1 = r8.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r2 = 0;
        if (r13 == 0) goto L_0x0016;
    L_0x0013:
        r8 = r13.length;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        if (r8 != 0) goto L_0x0035;
    L_0x0016:
        r2 = r1.loadClass(r12);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
    L_0x001a:
        r8 = CONSTRUCTOR_SIGNATURE;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r3 = r2.getConstructor(r8);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = 1;
        r3.setAccessible(r8);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = CONSTRUCTOR_MAP;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8.put(r12, r3);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
    L_0x0029:
        r0 = r11.mConstructorArgs;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = 1;
        r0[r8] = r14;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = r3.newInstance(r0);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = (android.support.v7.preference.Preference) r8;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        return r8;
    L_0x0035:
        r6 = 0;
        r9 = r13.length;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8 = 0;
    L_0x0038:
        if (r8 >= r9) goto L_0x0051;
    L_0x003a:
        r7 = r13[r8];	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r10 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
        r10.<init>();	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
        r10 = r10.append(r7);	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
        r10 = r10.append(r12);	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
        r10 = r10.toString();	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
        r2 = r1.loadClass(r10);	 Catch:{ ClassNotFoundException -> 0x0078, Exception -> 0x007e }
    L_0x0051:
        if (r2 != 0) goto L_0x001a;
    L_0x0053:
        if (r6 != 0) goto L_0x007d;
    L_0x0055:
        r8 = new android.view.InflateException;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r9.<init>();	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r10 = r14.getPositionDescription();	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r10 = ": Error inflating class ";
        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r9 = r9.append(r12);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        r8.<init>(r9);	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
        throw r8;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
    L_0x0076:
        r4 = move-exception;
        throw r4;
    L_0x0078:
        r4 = move-exception;
        r6 = r4;
        r8 = r8 + 1;
        goto L_0x0038;
    L_0x007d:
        throw r6;	 Catch:{ ClassNotFoundException -> 0x0076, Exception -> 0x007e }
    L_0x007e:
        r4 = move-exception;
        r5 = new android.view.InflateException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r14.getPositionDescription();
        r8 = r8.append(r9);
        r9 = ": Error inflating class ";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r8 = r8.toString();
        r5.<init>(r8);
        r5.initCause(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.preference.PreferenceInflater.createItem(java.lang.String, java.lang.String[], android.util.AttributeSet):android.support.v7.preference.Preference");
    }

    protected Preference onCreateItem(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createItem(name, this.mDefaultPackages, attrs);
    }

    private Preference createItemFromTag(String name, AttributeSet attrs) {
        InflateException ie;
        try {
            if (-1 == name.indexOf(46)) {
                return onCreateItem(name, attrs);
            }
            return createItem(name, null, attrs);
        } catch (InflateException e) {
            throw e;
        } catch (ClassNotFoundException e2) {
            ie = new InflateException(attrs.getPositionDescription() + ": Error inflating class (not found)" + name);
            ie.initCause(e2);
            throw ie;
        } catch (Exception e3) {
            ie = new InflateException(attrs.getPositionDescription() + ": Error inflating class " + name);
            ie.initCause(e3);
            throw ie;
        }
    }

    private void rInflate(XmlPullParser parser, Preference parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        XmlPullParserException ex;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                return;
            }
            if (type == 2) {
                String name = parser.getName();
                if ("intent".equals(name)) {
                    try {
                        parent.setIntent(Intent.parseIntent(getContext().getResources(), parser, attrs));
                    } catch (IOException e) {
                        ex = new XmlPullParserException("Error parsing preference");
                        ex.initCause(e);
                        throw ex;
                    }
                } else if ("extra".equals(name)) {
                    getContext().getResources().parseBundleExtra("extra", attrs, parent.getExtras());
                    try {
                        skipCurrentTag(parser);
                    } catch (IOException e2) {
                        ex = new XmlPullParserException("Error parsing preference");
                        ex.initCause(e2);
                        throw ex;
                    }
                } else {
                    Preference item = createItemFromTag(name, attrs);
                    ((PreferenceGroup) parent).addItemFromInflater(item);
                    rInflate(parser, item, attrs);
                }
            }
        }
    }

    private static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
        }
    }
}
