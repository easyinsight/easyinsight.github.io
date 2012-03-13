package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Document;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 3/13/12
 * Time: 10:46 AM
 */
public class MeServlet extends APIServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<account>\r\n");
        result.append("<name>");
        result.append(SecurityUtil.getUserName());
        result.append("</name>\r\n");
        result.append("</account>\r\n");
        return new ResponseInfo(ResponseInfo.ALL_GOOD, result.toString());
    }
}
