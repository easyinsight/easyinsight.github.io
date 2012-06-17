package com.easyinsight.html;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 6/16/12
 * Time: 7:53 PM
 */
public class RedirectUtil {
    public static String getURL(HttpServletRequest request, String endURL) {
        return "https://" + (request.getParameter("subdomain") == null ? "www" : request.getParameter("subdomain")) + ".easy-insight.com" + endURL;
    }
}
