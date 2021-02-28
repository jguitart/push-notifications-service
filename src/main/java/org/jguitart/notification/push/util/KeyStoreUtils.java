package org.jguitart.notification.push.util;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

public class KeyStoreUtils {

    public static KeyStore getKeyStore(String keyStoreBase64, String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        byte[] keyStoreBytes = Base64.getDecoder().decode(keyStoreBase64);
        return getKeyStore(keyStoreBytes, password);
    }

    public static KeyStore getKeyStore(byte[] keyStoreContent, String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore result = KeyStore.getInstance("pkcs12");
        ByteArrayInputStream certificateInputStream = new ByteArrayInputStream(keyStoreContent);

        // EOFException is not launches as IOException and not recovered as a valid exception
        // to recover a wrong format certificate error.
        // We need to check it explicitly.
        try {
            result.load(certificateInputStream, password.toCharArray());
        } catch (EOFException e) {
            e.printStackTrace();
            throw new CertificateException("Error loading certificate");
        }
        return result;
    }

}
