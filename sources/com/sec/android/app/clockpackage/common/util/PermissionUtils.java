package com.sec.android.app.clockpackage.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.C0645R;

public class PermissionUtils {
    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String[] PERMISSION_MULTI = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_CALENDAR"};
    private static final String[] PERMISSION_READ_CALENDAR = new String[]{"android.permission.READ_CALENDAR"};

    /* renamed from: com.sec.android.app.clockpackage.common.util.PermissionUtils$1 */
    static class C06481 implements OnClickListener {
        C06481() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Log.secD("PermissionUtils", "cancel");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.common.util.PermissionUtils$2 */
    static class C06492 implements OnCancelListener {
        C06492() {
        }

        public void onCancel(DialogInterface dialog) {
            Log.secD("PermissionUtils", "cancel");
        }
    }

    private static Drawable getPermissionIcon(Context context, String permission) {
        Drawable iconId = null;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                iconId = context.getDrawable(pm.getPermissionGroupInfo(pm.getPermissionInfo(permission, 128).group, 128).icon);
            }
        } catch (NameNotFoundException e) {
            Log.secE("PermissionUtils", "PackageManager.NameNotFoundException " + e.toString());
        }
        return iconId;
    }

    private static String getPermissionGroupName(Context context, String permission) {
        String groupName = null;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                groupName = context.getResources().getString(pm.getPermissionGroupInfo(pm.getPermissionInfo(permission, 128).group, 128).labelRes);
            }
        } catch (NameNotFoundException e) {
            Log.secE("PermissionUtils", "PackageManager.NameNotFoundException : " + e.toString());
        }
        return groupName;
    }

    private static void addPermissionItem(Context context, LinearLayout parent, String perm) {
        View listItem = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0645R.layout.permission_list_item, null);
        LayoutParams p = new LayoutParams(-2, -2);
        TextView permLabel = (TextView) listItem.findViewById(C0645R.id.name);
        ((ImageView) listItem.findViewById(C0645R.id.icon)).setImageDrawable(getPermissionIcon(context, perm));
        permLabel.setText(getPermissionGroupName(context, perm));
        parent.addView(listItem, p);
    }

    public static AlertDialog showPermissionPopup(final Context context, String permissionItem, int descriptResourceId, String perms) {
        ScrollView scrollView = (ScrollView) View.inflate(context, C0645R.layout.permission_popup, null);
        LinearLayout customLayout = (LinearLayout) scrollView.findViewById(C0645R.id.permission);
        Builder alertDialogBuilder = new Builder(context, C0645R.style.MyCustomThemeForDialog);
        ((TextView) customLayout.findViewById(C0645R.id.description)).setText(Html.fromHtml(context.getString(descriptResourceId, new Object[]{"<b>" + permissionItem + "</b>"}), 0));
        addPermissionItem(context, customLayout, perms);
        alertDialogBuilder.setView(scrollView);
        alertDialogBuilder.setNegativeButton(17039360, new C06481());
        alertDialogBuilder.setOnCancelListener(new C06492());
        alertDialogBuilder.setPositiveButton(C0645R.string.settings_caps, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.secD("PermissionUtils", "setting");
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:com.sec.android.app.clockpackage"));
                intent.setFlags(276824064);
                intent.putExtra("hideInfoButton", true);
                context.startActivity(intent);
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        return dialog;
    }

    private static boolean hasPermissions(Context context, String[] permissions) {
        Log.secD("PermissionUtils", "hasPermissions");
        if (context == null) {
            Log.secD("PermissionUtils", "context is null");
            return false;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                Log.secD("PermissionUtils", "PERMISSION_DENIED : " + permission);
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissionExternalStorage(Context context) {
        return hasPermissions(context, PERMISSION_EXTERNAL_STORAGE);
    }

    public static void requestPermissions(Activity activity, int requestCode) {
        if (activity == null) {
            Log.secD("PermissionUtils", "activity is null");
        } else {
            ActivityCompat.requestPermissions(activity, getPermissionString(requestCode), requestCode);
        }
    }

    public static void requestPermissions(Fragment fragment, int requestCode) {
        if (fragment == null) {
            Log.secD("PermissionUtils", "fragment is null");
        } else {
            fragment.requestPermissions(getPermissionString(requestCode), requestCode);
        }
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    private static String[] getPermissionString(int requestCode) {
        if (requestCode == 1) {
            return PERMISSION_EXTERNAL_STORAGE;
        }
        if (requestCode == 2) {
            return PERMISSION_READ_CALENDAR;
        }
        if (requestCode == 3) {
            return PERMISSION_MULTI;
        }
        return null;
    }
}
