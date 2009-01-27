/*  This software code is made available "AS IS" without warranties of any 
	kind.  You may copy, display, modify and redistribute the software
	code either by itself or as incorporated into your code; provided that
	you do not remove any proprietary notices.  Your use of this software
	code is at your own risk and you waive any claim against Amazon
	Web Services LLC or its affiliates with respect to your use of this 
	software code. (c) Amazon Web Services LLC or its affiliates.*/

package com.easyinsight.billing;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.axis.encoding.Base64;
import com.easyinsight.logging.LogClass;

public class GetSignature {
	
	/**
	 * Computes RFC 2104-compliant HMAC signature.
	 * 
	 * @param data
	 *            The data to be signed.
	 * @param key
	 *            The signing key.
	 * @return The base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public static String calculateRFC2104HMAC(String data,String key)throws NoSuchAlgorithmException, InvalidKeyException {
		String result;
		// get an hmac_sha1 key from the raw key bytes
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
				Config.HMAC_SHA1_ALGORITHM);

		// get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance(Config.HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);

		// compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(data.getBytes());

		// base64-encode the hmac
		result = Base64.encode(rawHmac);

		LogClass.debug("String to sign:" + data);
		LogClass.debug("signature:" + result);
		
		return result;
	}
		
}
