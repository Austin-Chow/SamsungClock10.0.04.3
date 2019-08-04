package com.samsung.android.scloud.oem.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public final class FileTool {
    private static MessageDigest mMessageDigest = null;

    public interface PDMProgressListener {
        void transferred(long j, long j2);
    }

    public static void writeToFile(InputStream inputStream, long size, FileOutputStream fileOpStream, PDMProgressListener handler) throws IOException {
        try {
            LOG.m3d("FileTool", "writeToFile - start Write with stream : " + fileOpStream);
            byte[] buffer = new byte[131072];
            long sum = 0;
            while (true) {
                int len = inputStream.read(buffer);
                if (len <= 0) {
                    break;
                }
                sum += (long) len;
                if (handler != null) {
                    handler.transferred(sum, size);
                }
                fileOpStream.write(buffer, 0, len);
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOpStream != null) {
                fileOpStream.close();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable th) {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOpStream != null) {
                fileOpStream.close();
            }
        }
    }

    public static void writeToFile(InputStream inputStream, long size, String filepath, PDMProgressListener handler) throws IOException {
        LOG.m3d("FileTool", "writeToFile - start Write with stream : " + filepath);
        String[] split = filepath.split("/");
        String folderPath = filepath.substring(0, filepath.length() - split[split.length - 1].length());
        File file = new File(folderPath);
        if (!file.exists()) {
            LOG.m6i("FileTool", "Creating folder : " + folderPath);
            if (!file.mkdirs()) {
                LOG.m5f("FileTool", "ORSMetaResponse.fromBinaryFile(): Can not create directory. ");
                throw new IOException();
            }
        }
        writeToFile(inputStream, size, new FileOutputStream(filepath, false), handler);
    }

    public static void writeToFile(String inputFile, long size, FileOutputStream outputStream, PDMProgressListener handler) throws IOException {
        File file = new File(inputFile);
        if (file.exists()) {
            writeToFile(new FileInputStream(file), size, outputStream, handler);
            return;
        }
        throw new IOException();
    }
}
