package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.xmlrpc.XmlRpcException;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftLeadSource extends InfusionsoftTableSource {

    public static final String LEAD_ID = "Id";
    public static final String OPPORTUNITY_TITLE = "OpportunityTitle";
    public static final String CONTACT_ID = "ContactID";
    public static final String USER_ID = "UserID";
    public static final String AFFILIATE_ID = "AffiliateId";
    public static final String STAGE_ID = "StageID";
    public static final String STATUS_ID = "StatusID";
    public static final String LEAD_SOURCE = "Leadsource";
    public static final String PROJECTED_REVENUE_LOW = "ProjectedRevenueLow";
    public static final String PROJECTED_REVENUE_HIGH = "ProjectedRevenueHigh";
    public static final String DATE_CREATED = "DateCreated";

    public InfusionsoftLeadSource() {
        setFeedName("Leads");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_LEAD;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(LEAD_ID, OPPORTUNITY_TITLE, CONTACT_ID, USER_ID, AFFILIATE_ID, STAGE_ID, STATUS_ID, LEAD_SOURCE,
                PROJECTED_REVENUE_HIGH, PROJECTED_REVENUE_LOW, DATE_CREATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(LEAD_ID), "Lead ID"));
        analysisitems.add(new AnalysisDimension(keys.get(OPPORTUNITY_TITLE), "Opportunity Title"));
        analysisitems.add(new AnalysisDimension(keys.get(USER_ID), "Lead User ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Lead Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(AFFILIATE_ID), "Lead Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(STAGE_ID), "Lead Stage ID"));
        analysisitems.add(new AnalysisDimension(keys.get(STATUS_ID), "Lead Status ID"));
        analysisitems.add(new AnalysisDimension(keys.get(LEAD_SOURCE), "Lead Source"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Lead Created At", AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisMeasure(keys.get(PROJECTED_REVENUE_HIGH), "Project Revenue High", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisitems.add(new AnalysisMeasure(keys.get(PROJECTED_REVENUE_LOW), "Project Revenue Low", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Lead", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
