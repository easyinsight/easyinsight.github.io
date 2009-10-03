package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 10:19:29 AM
 */
import com.easyinsight.security.PasswordService;
import com.easyinsight.PasswordStorage;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * A class with a set of utility functions.
 *
 *
 */
public class Utility {

  public static final String LOGIN_COOKIE_NAME = "AuthSubSampleCookie";

  private static PrivateKey privateKey;

    static {
        privateKey = PasswordStorage.getGooglePrivateKey();
    }

  // Make class uninstantiable
  private Utility() {
  }

  /**
   * Retrieves the value of the cookie with the corresponding name.
   */
  public static String getCookieValueWithName(Cookie[] cookies, String name) {
    for (int cookie_i = 0; cookie_i < cookies.length; cookie_i++) {
      if (cookies[cookie_i].getName().equals(name)) {
        return cookies[cookie_i].getValue();
      }
    }
    return null;
  }

  /**
   * Parse a query string using the '&' character as the delimiter.  Handles
   * quoted values and is thus different than HttpUtils.parseQueryString.
   */
  public static Map<String, String>  parseQueryString(String queryString) {
    StringBuilder query = new StringBuilder(queryString);
    Map<String,String> hash = new HashMap<String,String>();

    while (query.length() > 0) {
      char c = query.charAt(0);
      if (c == '&') {
        query.deleteCharAt(0);
        continue;
      }
      int equals = query.indexOf("=");
      String key = query.substring(0, equals);
      String value;
      query.delete(0, equals + 1);
      if (query.charAt(0) == '\"') {
        int nextQuote = query.indexOf("\"", 1);
        if (nextQuote == -1) {
          // mismatched quotes -- try to be accommodating
          value = query.toString();
          query.delete(0, query.length());
        } else {
          value = query.substring(1, nextQuote);
          query.delete(0, nextQuote + 1);
        }
      } else {
        int ampIndex = query.indexOf("&");
        if (ampIndex == -1) {
          ampIndex = query.length();
        }
        value = query.substring(0, ampIndex);
        query.delete(0, ampIndex);
      }
      hash.put(key, value);
    }
    return hash;
  }

  /**
   * Return the private key to use to sign the AuthSub request.
   *
   * Note: Currently configured to use AuthSub without signatures. Uncomment
   *       the function below if AuthSub requests need to be signed.
   */
  public static PrivateKey getPrivateKey() {
    /**
     *  if (privateKey == null) {
     *    try {
     *      privateKey = AuthSubUtil.
     *                   getPrivateKeyFromKeystore("/usr/etc/gdata_keys/AuthSub.jks",
     *                                             "passwd",
     *                                             "AuthSubAliasName",
     *                                             "passwd");
     *    } catch (Exception e) {
     *      throw new RuntimeException("Error reading from keystore file - ", e);
     *    }
     *  }
     */
    return privateKey;
  }
}

