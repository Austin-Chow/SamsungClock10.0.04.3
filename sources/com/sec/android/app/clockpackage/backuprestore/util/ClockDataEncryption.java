package com.sec.android.app.clockpackage.backuprestore.util;

import com.sec.android.app.clockpackage.common.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ClockDataEncryption {
    private Cipher mDecryptCipher;
    private byte[] mDecryptSalt;
    private SecretKeySpec mDecryptSecretKey;
    private Cipher mEncryptCipher;
    private byte[] mEncryptSalt;
    private SecretKeySpec mEncryptSecretKey;

    private byte[] generateEncryptSalt() throws NoSuchAlgorithmException {
        Log.secD("BNR_CLOCK_ClockDataEncryption", "generateEncryptSalt()");
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private SecretKeySpec generateSHA256SecretKey(String password) throws Exception {
        Log.secD("BNR_CLOCK_ClockDataEncryption", "generateSHA256SecretKey()");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private SecretKeySpec generatePBKDF2SecretKey(String securityPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        Log.secD("BNR_CLOCK_ClockDataEncryption", "generatePBKDF2SecretKey()");
        return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(securityPassword.toCharArray(), salt, 1000, 256)).getEncoded(), "AES");
    }

    public OutputStream encryptStream(OutputStream out, String saveKey, int securityLevel) throws Exception {
        Log.secD("BNR_CLOCK_ClockDataEncryption", "encryptStream()");
        this.mEncryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[this.mEncryptCipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        AlgorithmParameterSpec spec = new IvParameterSpec(iv);
        out.write(iv);
        if (securityLevel == 1) {
            this.mEncryptSalt = generateEncryptSalt();
            out.write(this.mEncryptSalt);
            this.mEncryptSecretKey = generatePBKDF2SecretKey(saveKey, this.mEncryptSalt);
        } else {
            this.mEncryptSecretKey = generateSHA256SecretKey(saveKey);
        }
        this.mEncryptCipher.init(1, this.mEncryptSecretKey, spec);
        return new CipherOutputStream(out, this.mEncryptCipher);
    }

    public InputStream decryptStream(InputStream in, String saveKey, int securityLevel) throws Exception {
        Log.secD("BNR_CLOCK_ClockDataEncryption", "decryptStream()");
        if (securityLevel == -1) {
            return null;
        }
        this.mDecryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[this.mDecryptCipher.getBlockSize()];
        if (in.read(iv) < this.mDecryptCipher.getBlockSize()) {
            Log.m42e("BNR_CLOCK_ClockDataEncryption", "Unexpected end of stream");
        }
        AlgorithmParameterSpec spec = new IvParameterSpec(iv);
        if (securityLevel == 1) {
            this.mDecryptSalt = new byte[16];
            in.read(this.mDecryptSalt);
            this.mDecryptSecretKey = generatePBKDF2SecretKey(saveKey, this.mDecryptSalt);
        } else {
            this.mDecryptSecretKey = generateSHA256SecretKey(saveKey);
        }
        this.mDecryptCipher.init(2, this.mDecryptSecretKey, spec);
        return new CipherInputStream(in, this.mDecryptCipher);
    }
}
