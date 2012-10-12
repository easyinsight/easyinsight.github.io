package com.easyinsight.export;

import com.easyinsight.api.v3.APIServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import nu.xom.Document;
import nu.xom.Element;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 10/10/12
 * Time: 2:15 PM
 */
public class ExportScheduledActivityServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        long activityID = Long.parseLong(request.getParameter("scheduleID"));
        int type = Integer.parseInt(request.getParameter("type"));
        ScheduledActivity scheduledActivity = ScheduledActivity.createActivity(type, activityID, conn);
        XMLMetadata xmlMetadata = new XMLMetadata();
        xmlMetadata.setConn(conn);
        String xml = scheduledActivity.toXML(xmlMetadata).toXML();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, xml);
    }
}
