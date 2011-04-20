package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import java.io.ByteArrayInputStream;
import java.util.*;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 4:00:05 PM
 */
public class SalesforceFeed extends Feed {

    private String sobjectName;

    public SalesforceFeed(String sobjectName) {
        this.sobjectName = sobjectName;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        SalesforceBaseDataSource salesforceBaseDataSource = null;
        try {
            salesforceBaseDataSource = (SalesforceBaseDataSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT+");
            for (AnalysisItem analysisItem : analysisItems) {
                String keyString = analysisItem.getKey().toKeyString();
                queryBuilder.append(keyString);
                queryBuilder.append(",");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append("+from+");
            queryBuilder.append(sobjectName);
            String url = salesforceBaseDataSource.getInstanceName() + "/services/data/v20.0/query/?q=" + queryBuilder.toString();

            HttpGet httpRequest = new HttpGet(url);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("Authorization", "OAuth " + salesforceBaseDataSource.getAccessToken());


            org.apache.http.client.HttpClient cc = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = cc.execute(httpRequest, responseHandler);

            Builder builder = new Builder();
            Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
            DataSet dataSet = new DataSet();
            Nodes records = doc.query("/QueryResult/records");
            for (int i = 0; i < records.size(); i++) {
                IRow row = dataSet.createRow();
                Node record = records.get(i);
                for (AnalysisItem analysisItem : analysisItems) {
                    Nodes results = record.query(analysisItem.getKey().toKeyString() + "/text()");
                    if (results.size() > 0) {
                        String value = results.get(0).getValue();
                        row.addValue(analysisItem.createAggregateKey(), value);
                    }
                }
            }
            return dataSet;
        } catch (HttpResponseException hre) {
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Salesforce data.", salesforceBaseDataSource));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
