package org.jguitart.notification.push.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class TestUtils {

    public static final String loadP12CertificateBase64() throws IOException {
        InputStream is = TestUtils.class.getClassLoader().getResourceAsStream("/certificates/certificate_test.p12");
        byte[] certificateContent = new byte[is.available()];
        is.read(certificateContent);
        is.close();
        String result = Base64.getEncoder().encodeToString(certificateContent);
        return result;
    }

}
