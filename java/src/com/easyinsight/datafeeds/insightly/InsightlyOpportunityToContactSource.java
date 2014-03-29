package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/24/14
 * Time: 11:35 AM
 */
public class InsightlyOpportunityToContactSource extends InsightlyBaseSource {

    public static final String OPPORTUNITY_ID = "Link Opportunity ID";
    public static final String CONTACT_ID = "Link Contact ID";
    public static final String ROLE = "Link Role";

    public InsightlyOpportunityToContactSource() {
        setFeedName("Opportunity to Contact");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ID, OPPORTUNITY_ID, ROLE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(CONTACT_ID)));
        fields.add(new AnalysisDimension(keys.get(ROLE)));
        fields.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID)));
        return fields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
            Map<String, List<InsightlyLink>> orgMap = insightlyCompositeSource.getLinkedOrgMap();
            for (Map.Entry<String, List<InsightlyLink>> entry : orgMap.entrySet()) {
                String oppID = entry.getKey();
                for (InsightlyLink insightlyLink : entry.getValue()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(OPPORTUNITY_ID), oppID);
                    row.addValue(keys.get(CONTACT_ID), insightlyLink.getLinkedID());
                    row.addValue(keys.get(ROLE), insightlyLink.getRole());
                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_OPPORTUNITY_TO_ORGANIZATION;
    }
}
