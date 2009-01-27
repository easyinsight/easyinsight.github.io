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

public final class PasswordService {
    private static PasswordService instance;

    private PasswordService() {
    }

    public synchronized String encrypt(String plaintext) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA"); //step 2
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            md.update(plaintext.getBytes("UTF-8")); //step 3
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        byte raw[] = md.digest(); //step 4
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
