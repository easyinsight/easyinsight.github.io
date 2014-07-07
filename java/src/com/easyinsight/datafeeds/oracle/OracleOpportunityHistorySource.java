package com.easyinsight.datafeeds.oracle;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.oracle.history.ExternalReportWSSService;
import com.easyinsight.datafeeds.oracle.history.ExternalReportWSSService_Service;
import com.easyinsight.datafeeds.oracle.history.ReportRequest;
import com.easyinsight.datafeeds.oracle.history.ReportResponse;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import javax.xml.ws.BindingProvider;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 6/18/14
 * Time: 9:54 AM
 */
public class OracleOpportunityHistorySource extends ServerDataSourceDefinition {

    public static final String OPPORTUNITY_ID = "History Opportunity ID";
    public static final String STAGE_NAME = "History Stage Name";
    public static final String STAGE_ID = "History Stage ID";
    public static final String STAGE_ENTER_DATE = "History Stage Date";

    public OracleOpportunityHistorySource() {
        setFeedName("Opportunity History");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
        fieldBuilder.addField(STAGE_NAME, new AnalysisDimension());
        fieldBuilder.addField(STAGE_ID, new AnalysisDimension());
        fieldBuilder.addField(STAGE_ENTER_DATE, new AnalysisDateDimension(true));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
        if (oracleDataSource.getHistoryUserName() == null || "".equals(oracleDataSource.getHistoryUserName())) {
            return dataSet;
        }
        try {
            ExternalReportWSSService opportunityService = new ExternalReportWSSService_Service(new URL(oracleDataSource.getUrl() + "//xmlpserver/services/ExternalReportWSSService?wsdl")).getExternalReportWSSService();
            BindingProvider prov = (BindingProvider) opportunityService;
            prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, oracleDataSource.getHistoryUserName());
            prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, oracleDataSource.getHistoryPassword());
            ReportRequest reportRequest = new ReportRequest();
            reportRequest.setReportAbsolutePath(oracleDataSource.getHistoryReport());
            reportRequest.setSizeOfDataChunkDownload(-1);
            ReportResponse response = opportunityService.runReport(reportRequest, "");
            byte[] bytes = response.getReportBytes();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            //byte[] decodedBytes = Base64.getDecoder().decode(bytes);
            Document document = new Builder().build(new ByteArrayInputStream(bytes));
            Nodes nodes = document.query("/DATA_DS/G_1");
            for (int i = 0; i < nodes.size(); i++) {
                IRow row = dataSet.createRow();
                Node node = nodes.get(i);
                String optyID = node.query("OPTY_ID/text()").get(0).getValue();
                row.addValue(keys.get(OPPORTUNITY_ID), optyID);
                String stageName = node.query("NAME_1/text()").get(0).getValue();
                row.addValue(keys.get(STAGE_NAME), stageName);
                String stageID = node.query("STG_ID/text()").get(0).getValue();
                row.addValue(keys.get(STAGE_ID), stageID);
                String stageEnterDate = node.query("STG_ENTER_DATE/text()").get(0).getValue();
                Date date = sdf.parse(stageEnterDate);
                row.addValue(keys.get(STAGE_ENTER_DATE), date);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITY_HISTORY;
    }
}
