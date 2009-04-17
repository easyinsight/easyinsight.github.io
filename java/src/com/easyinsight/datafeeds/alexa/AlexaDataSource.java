package com.easyinsight.datafeeds.alexa;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

/**
 * User: James Boe
 * Date: Apr 14, 2009
 * Time: 4:56:23 PM
 */
public class AlexaDataSource {
    private static final String ACTION_NAME = "UrlInfo";

	private static final String RESPONSE_GROUP_NAME = "TrafficData";

	private static final String AWS_BASE_URL = "http://awis.amazonaws.com?";

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private static final String DATEFORMAT_AWS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * Generate a timestamp for use with AWS request signing
	 *
	 * @param date
	 *            current date
	 * @return timestamp
	 */
	public static String getTimestampFromLocalTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT_AWS);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}

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
	 */
	public static String generateSignature(String data, String key) throws java.security.SignatureException {
		String result;
		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
					HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			// result = Encoding.EncodeBase64(rawHmac);
			result = new BASE64Encoder().encode(rawHmac);

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : "
					+ e.getMessage());
		}
		return result;
	}

	/**
	 * Make a request to the specified Url and return the results as a String
	 *
	 * @param urlBuffer
	 * @return the XML document as a String
	 * @throws java.net.MalformedURLException
	 * @throws IOException
	 */
	public static String makeRequest(String requestUrl) throws java.net.MalformedURLException, IOException {
		URL url = new URL(requestUrl);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();

		// Read the response

		StringBuffer sb = new StringBuffer();
		int c;
		int lastChar = 0;
		while ((c = in.read()) != -1) {
			if (c == '<' && (lastChar == '>'))
				sb.append('\n');
			sb.append((char) c);
			lastChar = c;
		}
		in.close();
		return sb.toString();
	}

	/**
	 * Make a test request to the AlexaWebInfo Service UrlInfo operation
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
			System.out.println("\nUsage: UrlInfo <Access Key ID> <Secret Key> <SiteUrl>\n");
			System.out.println("\nExample: UrlInfo AAAAAAAAAAAAAAAAAAAA SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS yahoo.com\n");
			return;
		}

		// Read command line parameters

		String accessKey = args[0];
		System.out.println("Using access key id: " + accessKey);

		String secretKey = args[1];
		System.out.println("Using secret key: " + secretKey);

		String siteUrl = args[2];
		System.out.println("For site: " + siteUrl);

		// Get current time

		String timestamp = AlexaDataSource.getTimestampFromLocalTime(Calendar
				.getInstance().getTime());

		// Generate request signature

		String signature = AlexaDataSource.generateSignature(ACTION_NAME + timestamp, secretKey);

		StringBuffer urlBuffer = new StringBuffer(AWS_BASE_URL);
		urlBuffer.append("&Action=");
		urlBuffer.append(ACTION_NAME);
		urlBuffer.append("&ResponseGroup=");
		urlBuffer.append(RESPONSE_GROUP_NAME);
		urlBuffer.append("&AWSAccessKeyId=");
		urlBuffer.append(accessKey);
		urlBuffer.append("&Signature=");
		urlBuffer.append(URLEncoder.encode(signature, "UTF-8"));
		urlBuffer.append("&Timestamp=");
		urlBuffer.append(URLEncoder.encode(timestamp, "UTF-8"));
		urlBuffer.append("&Url=");
		urlBuffer.append(URLEncoder.encode(siteUrl, "UTF-8"));

		System.out.println("Request:\n");
		System.err.println(urlBuffer);
		System.out.println();

		// Make the Request

		String xmlResponse = makeRequest(urlBuffer.toString());

		// Print out the XML Response

		System.out.println("Response:");
		System.out.println(xmlResponse);
	}
}
