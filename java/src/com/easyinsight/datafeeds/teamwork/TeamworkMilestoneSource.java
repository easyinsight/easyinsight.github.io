package com.easyinsight.datafeeds.teamwork;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 11/2/14
 * Time: 5:50 PM
 */
public class TeamworkMilestoneSource extends TeamworkBaseSource {

    public static final String MILESTONE_NAME = "Milestone Name";
    public static final String MILESTONE_ID = "Milestone ID";
    public static final String MILESTONE_PROJECT_ID = "Milestone Project ID";
    public static final String MILESTONE_RESPONSIBLE_PARTY = "Milestone Responsible Party";
    public static final String MILESTONE_STATUS = "Milestone Status";
    public static final String MILESTONE_CREATED_ON = "Milestone Created On";
    public static final String MILESTONE_COMPLETED_ON = "Milestone Completed On";
    public static final String MILESTONE_DEADLINE = "Milestone Deadline";
    public static final String MILESTONE_COMPLETER = "Milestone Completer";

    public TeamworkMilestoneSource() {
        setFeedName("Milestones");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(MILESTONE_NAME, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_ID, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_RESPONSIBLE_PARTY, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_STATUS, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_CREATED_ON, new AnalysisDateDimension());
        fieldBuilder.addField(MILESTONE_COMPLETED_ON, new AnalysisDateDimension());
        fieldBuilder.addField(MILESTONE_DEADLINE, new AnalysisDateDimension(true));
        fieldBuilder.addField(MILESTONE_COMPLETER, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            TeamworkCompositeSource teamworkCompositeSource = (TeamworkCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(teamworkCompositeSource.getTeamworkApiKey());
            Map results = runRestRequestForMap("milestones.json?find=all", httpClient, teamworkCompositeSource);
            List<Map> milestones = (List<Map>) results.get("milestones");
            for (Map milestone : milestones) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(MILESTONE_ID), getValue(milestone, "id"));
                row.addValue(keys.get(MILESTONE_NAME), getValue(milestone, "title"));
                row.addValue(keys.get(MILESTONE_PROJECT_ID), getValue(milestone, "project-id"));
                if (getValue(milestone, "responsible-party-lastname") != null) {
                    row.addValue(keys.get(MILESTONE_RESPONSIBLE_PARTY), getValue(milestone, "responsible-party-firstname") + " " + getValue(milestone, "responsible-party-lastname"));
                }
                row.addValue(keys.get(MILESTONE_STATUS), getValue(milestone, "status"));
                row.addValue(keys.get(MILESTONE_CREATED_ON), getDate(milestone, "created-on"));
                row.addValue(keys.get(MILESTONE_COMPLETED_ON), getDate(milestone, "completed-on"));
                row.addValue(keys.get(MILESTONE_DEADLINE), getDeadlineDate(milestone, "deadline"));
                if (getValue(milestone, "completer-lastname") != null) {
                    row.addValue(keys.get(MILESTONE_COMPLETER), getValue(milestone, "completer-firstname") + " " + getValue(milestone, "completer-lastname"));
                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_MILESTONE;
    }
}
