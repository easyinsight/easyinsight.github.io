package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftContactActionSource extends InfusionsoftTableSource {

    public static final String CONTACT_ACTION_ID = "Id";
    public static final String CONTACT_ID = "ContactId";
    public static final String OPPORTUNITY_ID = "OpportunityId";
    public static final String ACTION_TYPE = "ActionType";
    public static final String ACTION_DESCRIPTION = "ActionDescription";
    public static final String CREATION_DATE = "CreationDate";
    public static final String COMPLETION_DATE = "CompletionDate";
    public static final String ACTION_DATE = "ActionDate";
    public static final String END_DATE = "EndDate";


    public InfusionsoftContactActionSource() {
        setFeedName("Contact Actions");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CONTACT_ACTION;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ACTION_ID, CONTACT_ID, OPPORTUNITY_ID, ACTION_TYPE, ACTION_DESCRIPTION, CREATION_DATE, COMPLETION_DATE, ACTION_DATE, END_DATE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ACTION_ID), "Action ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Action Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID), "Action Opportunity ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ACTION_TYPE), "Action Type"));
        analysisitems.add(new AnalysisDimension(keys.get(ACTION_DESCRIPTION), "Action Description"));
        analysisitems.add(new AnalysisDateDimension(keys.get(CREATION_DATE), "Action Creation Date", AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(COMPLETION_DATE), "Action Completion Date", AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(ACTION_DATE), "Action Date", AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(END_DATE), "Action End Date", AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("ContactAction", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
