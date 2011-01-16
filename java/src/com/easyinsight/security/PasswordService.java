package com.easyinsight.security;

/**
 * User: James Boe
 * Date: May 21, 2008
 * Time: 2:17:05 AM
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;
import org.bouncycastle.util.encoders.Base64;

public final class PasswordService {
    private static PasswordService instance;

    private PasswordService() {
    }

    public synchronized String encrypt(String plaintext, String salt, String algorithm) {
        MessageDigest md;
        byte raw[] = null;
        try {
            md = MessageDigest.getInstance(algorithm); //step 2
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            if(salt != null) {
                md.update(Base64.decode(salt));
            }
            raw = md.digest(plaintext.getBytes("UTF-8")); //step 3
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return (new BASE64Encoder()).encode(raw); //step 5
    }

    public static synchronized PasswordService getInstance() //step 1
    {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
}
