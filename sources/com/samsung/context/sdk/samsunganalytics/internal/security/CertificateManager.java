package com.samsung.context.sdk.samsunganalytics.internal.security;

import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CertificateManager {
    private SSLContext sslContext;

    private static class Singleton {
        private static final CertificateManager instance = new CertificateManager();
    }

    private CertificateManager() {
        pinningSystemCA();
    }

    public static CertificateManager getInstance() {
        return Singleton.instance;
    }

    private void pinningSystemCA() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            KeyStore androidCAStore = KeyStore.getInstance("AndroidCAStore");
            androidCAStore.load(null, null);
            Enumeration aliases = androidCAStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();
                X509Certificate cert = (X509Certificate) androidCAStore.getCertificate(alias);
                if (alias.startsWith("system:")) {
                    keyStore.setCertificateEntry(alias, cert);
                }
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            this.sslContext = SSLContext.getInstance("TLS");
            this.sslContext.init(null, tmf.getTrustManagers(), null);
            Debug.LogENG("pinning success");
        } catch (Exception e) {
            Debug.LogENG("pinning fail : " + e.getMessage());
        }
    }

    public SSLContext getSSLContext() {
        return this.sslContext;
    }
}
