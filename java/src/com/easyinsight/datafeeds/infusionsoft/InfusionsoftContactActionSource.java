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
    public static final String OWNER_ID = "UserID";
    public static final String OWNER_NAME = "Action Owner Name";
    public static final String PRIORITY = "Priority";
    public static final String APPOINTMENT = "IsAppointment";
    public static final String ACTION_COUNT = "Action Count";
    public static final String CREATION_NOTES = "CreationNotes";

    public InfusionsoftContactActionSource() {
        setFeedName("Contact Actions");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CONTACT_ACTION;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CONTACT_ACTION_ID, new AnalysisDimension("Action ID"));
        fieldBuilder.addField(OWNER_ID, new AnalysisDimension("Action Owner ID"));
        fieldBuilder.addField(PRIORITY, new AnalysisDimension("Action Priority"));
        fieldBuilder.addField(APPOINTMENT, new AnalysisDimension("Action is Appointment"));
        fieldBuilder.addField(OWNER_NAME, new AnalysisDimension("Action Owner Name"));
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension("Action Contact ID"));
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension("Action Opportunity ID"));
        fieldBuilder.addField(ACTION_TYPE, new AnalysisDimension("Action Type"));
        fieldBuilder.addField(ACTION_DESCRIPTION, new AnalysisDimension("Action Description"));
        fieldBuilder.addField(CREATION_DATE, new AnalysisDateDimension("Action Creation Date"));
        fieldBuilder.addField(COMPLETION_DATE, new AnalysisDateDimension("Action Completion Date"));
        fieldBuilder.addField(ACTION_DATE, new AnalysisDateDimension("Action Date"));
        fieldBuilder.addField(END_DATE, new AnalysisDateDimension("Action End Date"));
        fieldBuilder.addField(ACTION_COUNT, new AnalysisMeasure("Number of Actions"));
        fieldBuilder.addField(CREATION_NOTES, new AnalysisDimension("Creation Notes"));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
            DataSet dataSet = query("ContactAction", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(ACTION_COUNT, OWNER_NAME));
            for (IRow row : dataSet.getRows()) {
                String ownerID = row.getValue(keys.get(OWNER_ID)).toString();
                String ownerName = infusionsoftCompositeSource.getUserCache().get(ownerID);
                if (ownerName != null) {
                    row.addValue(keys.get(OWNER_NAME), ownerName);
                }
                row.addValue(keys.get(ACTION_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
