package com.easyinsight.api.v3;


import com.easyinsight.database.EIConnection;
import nu.xom.Document;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class RollbackServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }
}
