package com.samsung.android.scloud.oem.lib;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemSavedList extends ArrayList<String> {
    private FileWriter fw;
    private String mTAG = "ItemSavedList_";
    private File processedKeyFile;

    public static ItemSavedList load(Context context, String name) {
        return new ItemSavedList(context, name);
    }

    private ItemSavedList(Context context, String name) {
        IOException e;
        Throwable th;
        this.mTAG += name;
        File folder = new File(context.getFilesDir() + "/scloud/");
        if (!folder.exists()) {
            LOG.m6i(this.mTAG, "isDirectoryMade : " + folder.mkdir());
        }
        this.processedKeyFile = new File(context.getFilesDir() + "/scloud/" + name + "_processedItem");
        LOG.m6i(this.mTAG, "create : " + context.getFilesDir() + "/scloud/" + name + "_processedItem" + ", " + this.processedKeyFile.exists() + ", " + this.processedKeyFile.length());
        BufferedReader br = null;
        try {
            if (this.processedKeyFile.exists() && this.processedKeyFile.length() > 0) {
                List<String> prev = new ArrayList();
                BufferedReader br2 = new BufferedReader(new FileReader(this.processedKeyFile));
                while (true) {
                    try {
                        String id = br2.readLine();
                        if (id == null) {
                            break;
                        }
                        id = id.trim();
                        if (!id.isEmpty()) {
                            LOG.m6i(this.mTAG, "read : " + id);
                            prev.add(id);
                        }
                    } catch (IOException e2) {
                        e = e2;
                        br = br2;
                    } catch (Throwable th2) {
                        th = th2;
                        br = br2;
                    }
                }
                addAll(prev);
                LOG.m6i(this.mTAG, "Add prev data : " + prev.size());
                br = br2;
            }
            this.fw = new FileWriter(this.processedKeyFile, true);
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (IOException e4) {
            e3 = e4;
            try {
                e3.printStackTrace();
                LOG.m4e(this.mTAG, "ItemSavedList err", e3);
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    public boolean add(String object) {
        try {
            this.fw.write(new StringBuilder(String.valueOf(object)).append("\n").toString());
            this.fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.m4e(this.mTAG, "ItemSavedList err", e);
        }
        return super.add(object);
    }

    public void clear() {
        try {
            this.fw.close();
            LOG.m6i(this.mTAG, "clear this : " + this.processedKeyFile.exists() + ", " + this.processedKeyFile.delete());
            this.fw = new FileWriter(this.processedKeyFile, true);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.m4e(this.mTAG, "ItemSavedList err", e);
        }
        super.clear();
    }
}
