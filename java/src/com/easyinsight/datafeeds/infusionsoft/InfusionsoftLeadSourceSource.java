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
 * Date: 12/30/13
 * Time: 5:08 PM
 */
public class InfusionsoftLeadSourceSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String START_DATE = "StartDate";
    public static final String END_DATE = "EndDate";
    public static final String COST_PER_LEAD = "CostPerLead";
    public static final String VENDOR = "Vendor";
    public static final String MEDIUM = "Medium";
    public static final String MESSAGE = "Message";
    public static final String LEAD_SOURCE_CATEGORY_ID = "LeadSourceCategoryId";
    public static final String STATUS = "Status";


    public InfusionsoftLeadSourceSource() {
        setFeedName("Lead Source");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_LEAD_SOURCE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, NAME, DESCRIPTION, START_DATE, END_DATE, COST_PER_LEAD, VENDOR, MEDIUM, MESSAGE, LEAD_SOURCE_CATEGORY_ID, STATUS);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(NAME), "Name"));
        analysisitems.add(new AnalysisDimension(keys.get(COST_PER_LEAD), "Cost per Lead"));
        analysisitems.add(new AnalysisDimension(keys.get(VENDOR), "Vendor"));
        analysisitems.add(new AnalysisDimension(keys.get(MEDIUM), "Medium"));
        analysisitems.add(new AnalysisDimension(keys.get(MESSAGE), "Message"));
        analysisitems.add(new AnalysisDimension(keys.get(STATUS), "Status"));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), "Description"));
        analysisitems.add(new AnalysisDimension(keys.get(LEAD_SOURCE_CATEGORY_ID), "Lead Source Category ID"));
        analysisitems.add(new AnalysisDateDimension(keys.get(START_DATE), "Start Date", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(END_DATE), "End Date", AnalysisDateDimension.DAY_LEVEL, true));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("LeadSource", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
