package com.easyinsight.export;

import com.easyinsight.api.v3.APIServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.email.UserStub;
import nu.xom.Document;
import nu.xom.Element;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 10/10/12
 * Time: 2:15 PM
 */
public class ImportScheduledActivityServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        Element rootElement = document.getRootElement();
        XMLImportMetadata xmlImportMetadata = new XMLImportMetadata();
        xmlImportMetadata.setConn(conn);

        PreparedStatement userExists = conn.prepareStatement("SELECT EMAIL FROM USER WHERE USER_ID = ?");
        if ("reportDelivery".equals(rootElement.getLocalName())) {
            xmlImportMetadata.setDataSource(new FeedStorage().getFeedDefinitionData(Long.parseLong(rootElement.getAttribute("dataSourceID").getValue())));
            ReportDelivery reportDelivery = new ReportDelivery();
            reportDelivery.fromXML(rootElement, xmlImportMetadata);
            Iterator<UserStub> iter = reportDelivery.getUsers().iterator();
            while (iter.hasNext()) {
                UserStub userStub = iter.next();
                userExists.setLong(1, userStub.getUserID());
                ResultSet existsRS = userExists.executeQuery();
                if (!existsRS.next()) {
                    iter.remove();
                }
            }
            reportDelivery.save(conn, reportDelivery.getTimezoneOffset());
            reportDelivery.setup(conn);
        } else if ("multipleDelivery".equals(rootElement.getLocalName())) {
            GeneralDelivery generalDelivery = new GeneralDelivery();
            generalDelivery.fromXML(rootElement, xmlImportMetadata);
            Iterator<UserStub> iter = generalDelivery.getUsers().iterator();
            while (iter.hasNext()) {
                UserStub userStub = iter.next();
                userExists.setLong(1, userStub.getUserID());
                ResultSet existsRS = userExists.executeQuery();
                if (!existsRS.next()) {
                    iter.remove();
                }
            }
            generalDelivery.save(conn, generalDelivery.getTimezoneOffset());
            generalDelivery.setup(conn);
        }
        userExists.close();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }
}
