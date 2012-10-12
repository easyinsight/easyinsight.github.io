package com.easyinsight.export;

import com.easyinsight.api.v3.APIServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import nu.xom.Document;
import nu.xom.Element;

import javax.servlet.http.HttpServletRequest;

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

        if ("reportDelivery".equals(rootElement.getLocalName())) {
            xmlImportMetadata.setDataSource(new FeedStorage().getFeedDefinitionData(Long.parseLong(rootElement.getAttribute("dataSourceID").getValue())));
            ReportDelivery reportDelivery = new ReportDelivery();
            reportDelivery.fromXML(rootElement, xmlImportMetadata);
            reportDelivery.save(conn, reportDelivery.getTimezoneOffset());
            reportDelivery.setup(conn);
        } else if ("multipleDelivery".equals(rootElement.getLocalName())) {
            GeneralDelivery generalDelivery = new GeneralDelivery();
            generalDelivery.fromXML(rootElement, xmlImportMetadata);
            generalDelivery.save(conn, generalDelivery.getTimezoneOffset());
            generalDelivery.setup(conn);
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }
}
