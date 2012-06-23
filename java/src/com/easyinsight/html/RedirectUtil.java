package com.easyinsight.html;

import com.easyinsight.config.ConfigLoader;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 6/16/12
 * Time: 7:53 PM
 */
public class RedirectUtil {
    public static String getURL(HttpServletRequest request, String endURL) {
        if (ConfigLoader.instance().getLocalURL() != null) {
            return "https://" + ConfigLoader.instance().getLocalURL() + endURL;
        }
        return "https://" + (request.getParameter("subdomain") == null ? "www" : request.getParameter("subdomain")) + ".easy-insight.com" + endURL;
    }
}
