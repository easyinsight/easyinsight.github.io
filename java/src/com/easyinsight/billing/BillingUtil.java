package com.easyinsight.billing;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 30, 2009
 * Time: 5:25:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class BillingUtil {
    public static String getKeyID() {
        return "776320";
    }

    public static String getKey() {
        return "UVgCejU48ANga4mKF77WFXfm2yUve76W";
    }

    public static String MD5Hash(String hashString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");

        digest.update((hashString).getBytes());
        byte[] hasher = digest.digest();


        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hasher.length; i++) {
        	int halfbyte = (hasher[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	        	if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	        	halfbyte = hasher[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        String hash = buf.toString();
        return hash;
    }
}
