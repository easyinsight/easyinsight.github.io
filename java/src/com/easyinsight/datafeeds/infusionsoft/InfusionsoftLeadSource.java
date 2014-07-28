package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
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
    public static final String USER_NAME = "LeadUserName";
    public static final String AFFILIATE_ID = "AffiliateId";
    public static final String STAGE_ID = "StageID";
    public static final String STAGE_NAME = "StageName";
    public static final String STATUS_ID = "StatusID";
    public static final String STATUS_NAME = "StatusName";
    public static final String LEAD_SOURCE = "Leadsource";
    public static final String PROJECTED_REVENUE_LOW = "ProjectedRevenueLow";
    public static final String PROJECTED_REVENUE_HIGH = "ProjectedRevenueHigh";
    public static final String DATE_CREATED = "DateCreated";
    public static final String OPPORTUNITY_COUNT = "OpportunityCount";
    public static final String OPPORTUNITY_URL = "Opportunity URL";
    public static final String OPPORTUNITY_AUTHOR = "CreatedBy";
    public static final String EXPECTED_CLOSE_DATE = "EstimatedCloseDate";
    public static final String OPPORTUNITY_NOTES = "OpportunityNotes";
    public static final String OBJECTION = "Objection";

    public InfusionsoftLeadSource() {
        setFeedName("Opportunities");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_LEAD;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(LEAD_ID, new AnalysisDimension("Opportunity ID"));
        fieldBuilder.addField(OPPORTUNITY_TITLE, new AnalysisDimension("Opportunity Title"));
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension("Opportunity Contact ID"));
        fieldBuilder.addField(USER_ID, new AnalysisDimension("Opportunity User ID"));
        fieldBuilder.addField(USER_NAME, new AnalysisDimension("Opportunity User Name"));
        fieldBuilder.addField(AFFILIATE_ID, new AnalysisDimension("Opportunity Affiliate ID"));
        fieldBuilder.addField(STAGE_ID, new AnalysisDimension("Opportunity Stage ID"));
        fieldBuilder.addField(STAGE_NAME, new AnalysisDimension("Opportunity Stage Name"));
        fieldBuilder.addField(STATUS_ID, new AnalysisDimension("Opportunity Status ID"));
        fieldBuilder.addField(LEAD_SOURCE, new AnalysisDimension("Opportunity Source"));
        fieldBuilder.addField(OBJECTION, new AnalysisDimension("Objection"));
        fieldBuilder.addField(PROJECTED_REVENUE_HIGH, new AnalysisDimension("Projected Revenue High"));
        fieldBuilder.addField(PROJECTED_REVENUE_LOW, new AnalysisDimension("Projected Revenue Low"));
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension("Opportunity Created At"));
        fieldBuilder.addField(EXPECTED_CLOSE_DATE, new AnalysisDateDimension("Estimated Close Date"));
        fieldBuilder.addField(OPPORTUNITY_AUTHOR, new AnalysisDimension("Opportunity Author"));
        fieldBuilder.addField(OPPORTUNITY_NOTES, new AnalysisDimension("Opportunity Notes"));
        fieldBuilder.addField(OPPORTUNITY_COUNT, new AnalysisMeasure("Number of Opportunities"));
        fieldBuilder.addField(OPPORTUNITY_URL, new AnalysisDimension("Opportunity URL"));
        InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
        List<CustomField> customFields = infusionsoftCompositeSource.getCache().getCustomFieldMap().get(-4);
        if (customFields != null) {
            for (CustomField customField : customFields) {
                fieldBuilder.addField(customField.key(), customField.createAnalysisItem());
            }
        }
    }

    public List<AnalysisItem> createStageItems() {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(new NamedKey(InfusionsoftStageSource.STAGE_NAME), "Stage Name"));
        analysisitems.add(new AnalysisDimension(new NamedKey(InfusionsoftStageSource.STAGE_ID), "Stage ID"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
            Map<String, String> stages = infusionsoftCompositeSource.getLeadStageCache();
            Map<String, String> users = infusionsoftCompositeSource.getUserCache();
            DataSet leads = query("Lead", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(STAGE_NAME, USER_NAME,
                    STATUS_NAME, OPPORTUNITY_COUNT, OPPORTUNITY_URL));
            for (IRow row : leads.getRows()) {
                row.addValue(keys.get(STAGE_NAME), stages.get(row.getValue(keys.get(STAGE_ID)).toString()));
                row.addValue(keys.get(USER_NAME), users.get(row.getValue(keys.get(USER_ID)).toString()));
                row.addValue(keys.get(OPPORTUNITY_COUNT), 1);
                row.addValue(keys.get(OPPORTUNITY_URL), infusionsoftCompositeSource.getUrl() + "/Opportunity/manageOpportunity.jsp?view=edit&id=" + row.getValue(keys.get(LEAD_ID)));
                row.addValue(keys.get(OPPORTUNITY_AUTHOR), users.get(row.getValue(keys.get(OPPORTUNITY_AUTHOR)).toString()));
            }
            return leads;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
