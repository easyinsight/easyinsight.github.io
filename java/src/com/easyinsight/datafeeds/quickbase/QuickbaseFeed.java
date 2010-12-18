package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/16/10
 * Time: 12:44 PM
 */
public class QuickbaseFeed extends Feed {

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt></qdbapi>";

    private String databaseID;
    private String applicationToken;
    private String sessionTicket;
    private String host;
    private FeedDefinition parent;

    public QuickbaseFeed(String databaseID, String applicationToken, String sessionTicket, String host, FeedDefinition parent) {
        this.databaseID = databaseID;
        this.applicationToken = applicationToken;
        this.sessionTicket = sessionTicket;
        this.host = host;
        this.parent = parent;
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) throws ReportException {
        return null;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws ReportException {
        try {
            String fullPath = "https://" + host + "/db/" + databaseID;
            HttpPost httpRequest = new HttpPost(fullPath);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("QUICKBASE-ACTION", "API_DoQuery");
            BasicHttpEntity entity = new BasicHttpEntity();
            StringBuilder columnBuilder = new StringBuilder();
            Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                String fieldID = analysisItem.getKey().toBaseKey().toKeyString().split("\\.")[1];
                map.put(fieldID, analysisItem);
                columnBuilder.append(fieldID).append(".");
            }
            columnBuilder.deleteCharAt(columnBuilder.length() - 1);
            String requestBody = MessageFormat.format(REQUEST, sessionTicket, applicationToken, columnBuilder.toString());
            byte[] contentBytes = requestBody.getBytes();
            entity.setContent(new ByteArrayInputStream(contentBytes));
            entity.setContentLength(contentBytes.length);
            httpRequest.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
            DataSet dataSet = new DataSet();
            Nodes errors = doc.query("/qdbapi/errcode/text()");
            if (errors.size() > 0) {
                Node error = errors.get(0);
                if (!"0".equals(error.getValue())) {
                    String errorDetail = doc.query("/qdbapi/errdetail/text()").get(0).getValue();
                    throw new ReportException(new DataSourceConnectivityReportFault(errorDetail, parent));
                }
            }
            Nodes records = doc.query("/qdbapi/table/records/record");
            for (int i = 0; i < records.size(); i++) {
                Element record = (Element) records.get(i);
                IRow row = dataSet.createRow();
                Elements childElements = record.getChildElements();
                for (int j = 0; j < childElements.size(); j++) {
                    Element childElement = childElements.get(j);
                    if (childElement.getLocalName().equals("f")) {
                        String fieldID = childElement.getAttribute("id").getValue();
                        AnalysisItem analysisItem = map.get(fieldID);
                        String value = childElement.getValue();
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && !"".equals(value)) {
                            row.addValue(analysisItem.createAggregateKey(), new Date(Long.parseLong(value)));
                        } else {
                            row.addValue(analysisItem.createAggregateKey(), value);
                        }
                    }
                }
            }

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
