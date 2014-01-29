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
import org.jetbrains.annotations.NotNull;

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

    public Solve360OpportunitiesSource() {
        setFeedName("Opportunities");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_OPPORTUNITIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(OPPORTUNITY_ID, DESCRIPTION, DOLLARS, STATUS, RELATED_TO, CLOSING_DATE, PROBABILITY,
                CREATED, UPDATED, COUNT, RESPONSIBLE, STAGE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(DESCRIPTION)));
        analysisItems.add(new AnalysisDimension(keys.get(STAGE)));
        analysisItems.add(new AnalysisDimension(keys.get(STATUS)));
        analysisItems.add(new AnalysisDimension(keys.get(RELATED_TO)));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLE)));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(CLOSING_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(DOLLARS), DOLLARS, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(PROBABILITY), PROBABILITY, AggregationTypes.SUM, true, FormattingConfiguration.PERCENTAGE));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(solve360CompositeSource.getUserEmail(), solve360CompositeSource.getAuthKey());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Document doc = runRestRequest("https://secure.solve360.com/report/opportunities", httpClient, new Builder(), solve360CompositeSource);
            System.out.println(doc.toXML());
            DataSet dataSet = new DataSet();
            Nodes oppNodes = doc.query("/response/opportunities/opportunity");
            for (int i = 0; i < oppNodes.size(); i++) {
                Node dealNode = oppNodes.get(i);
                IRow row = dataSet.createRow();
                Nodes responsibleNodes = dealNode.query("/responsible");
                if (responsibleNodes.size() > 0) {
                    Element responsibleNode = (Element) responsibleNodes.get(0);
                    String responsibleParty = responsibleNode.getAttribute("cn").getValue();
                    row.addValue(keys.get(RESPONSIBLE), responsibleParty);
                }
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
                row.addValue(keys.get(COUNT), 1);
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
