package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 11/1/11
 * Time: 4:26 PM
 */
public class Solve360OpportunitiesSource extends Solve360BaseSource {

    public static final String OPPORTUNITY_ID = "Opportunity ID";
    public static final String DESCRIPTION = "Opportunity Description";
    public static final String DOLLARS = "Opportunity Value";
    public static final String STATUS = "Opportunity Status";
    public static final String RELATED_TO = "Opportunity Related To";
    public static final String CLOSING_DATE = "Opportunity Closing Date";
    public static final String PROBABILITY = "Opportunity Probability";
    public static final String STAGE = "Opportunity Probability";
    public static final String CREATED = "Opportunity Created On";
    public static final String UPDATED = "Opportunity Updated On";
    public static final String COUNT = "Opportunity Count";
    public static final String RESPONSIBLE = "Opportunity Responsible Party";
    public static final String RELATED_COMPANY = "Related Company";
    public static final String RELATED_CONTACT = "Related Contact";

    public Solve360OpportunitiesSource() {
        setFeedName("Opportunities");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_OPPORTUNITIES;
    }


    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        try {
            HttpClient client = getHttpClient(solve360CompositeSource.getUserEmail(), solve360CompositeSource.getAuthKey());
            Document doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/fields/opportunity", client, new Builder(), this);
            Nodes responseNode = doc.query("/response/fields");

            for (int i = 0; i < responseNode.size(); i++) {
                Element screwYou = (Element) responseNode.get(i);
                for (int j = 0 ; j < screwYou.getChildCount(); j++) {
                    Element customFieldNode = (Element) screwYou.getChild(j);
                    String customFieldID = Solve360BaseSource.queryField(customFieldNode, "name/text()");
                    if (customFieldID.startsWith("custom")) {
                        String customFieldName = Solve360BaseSource.queryField(customFieldNode, "label/text()");
                        String type = Solve360BaseSource.queryField(customFieldNode, "type/text()");

                        if ("date".equals(type)) {
                            fieldBuilder.addField(customFieldID, new AnalysisDateDimension(customFieldName));
                        } else if ("number".equals(type)) {
                            fieldBuilder.addField(customFieldID, new AnalysisMeasure(customFieldName));
                        } else {
                            fieldBuilder.addField(customFieldID, new AnalysisDimension(customFieldName));
                        }
                    }
                }

            }
            fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
            fieldBuilder.addField(DESCRIPTION, new AnalysisDimension());
            fieldBuilder.addField(STAGE, new AnalysisDimension());
            fieldBuilder.addField(STATUS, new AnalysisDimension());
            fieldBuilder.addField(RELATED_TO, new AnalysisDimension());
            fieldBuilder.addField(RESPONSIBLE, new AnalysisDimension());
            fieldBuilder.addField(RELATED_COMPANY, new AnalysisDimension());
            fieldBuilder.addField(RELATED_CONTACT, new AnalysisDimension());
            fieldBuilder.addField(CREATED, new AnalysisDateDimension());
            fieldBuilder.addField(UPDATED, new AnalysisDateDimension());
            fieldBuilder.addField(CLOSING_DATE, new AnalysisDateDimension());
            fieldBuilder.addField(DOLLARS, new AnalysisMeasure());
            fieldBuilder.addField(PROBABILITY, new AnalysisMeasure());
            fieldBuilder.addField(COUNT, new AnalysisMeasure());
        } catch (Exception e) {
            throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), parentDefinition));
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(solve360CompositeSource.getUserEmail(), solve360CompositeSource.getAuthKey());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Document doc = runRestRequest("https://secure.solve360.com/report/opportunities", httpClient, new Builder(), solve360CompositeSource);
            DataSet dataSet = new DataSet();
            Nodes oppNodes = doc.query("/response/opportunities/opportunity");
            for (int i = 0; i < oppNodes.size(); i++) {
                Node dealNode = oppNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(RESPONSIBLE), queryField(dealNode, "responsible/@cn"));
                row.addValue(keys.get(OPPORTUNITY_ID), queryField(dealNode, "id/text()"));
                row.addValue(keys.get(DESCRIPTION), queryField(dealNode, "description/text()"));
                row.addValue(keys.get(STATUS), queryField(dealNode, "status/text()"));
                row.addValue(keys.get(STAGE), queryField(dealNode, "stage/text()"));
                row.addValue(keys.get(RELATED_TO), queryField(dealNode, "owner/text()"));
                row.addValue(keys.get(DOLLARS), queryField(dealNode, "valueunit/text()"));
                row.addValue(keys.get(PROBABILITY), queryField(dealNode, "probability/text()"));
                row.addValue(keys.get(CREATED), df.parse(queryField(dealNode, "created/text()")));
                row.addValue(keys.get(UPDATED), df.parse(queryField(dealNode, "modified/text()")));
                String closedDate = queryField(dealNode, "closingdate/text()");
                if (closedDate != null) {
                    row.addValue(keys.get(CLOSING_DATE), df2.parse(closedDate));
                }
                String relatedType = queryField(dealNode, "itemtype/text()");
                if("40".equals(relatedType)) {
                    row.addValue(keys.get(RELATED_COMPANY), queryField(dealNode, "itemid/text()"));
                } else if("1".equals(relatedType)) {
                    row.addValue(keys.get(RELATED_CONTACT), queryField(dealNode, "itemid/text()"));
                }
                row.addValue(keys.get(COUNT), 1);
                for (Key key : keys.values()) {
                    if (key.toKeyString().startsWith("custom")) {
                        row.addValue(key, queryField(dealNode, key.toKeyString() + "/text()"));
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
