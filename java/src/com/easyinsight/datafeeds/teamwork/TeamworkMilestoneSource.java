package com.easyinsight.datafeeds.teamwork;

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

    public TeamworkMilestoneSource() {
        setFeedName("Milestones");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(MILESTONE_NAME, new AnalysisDimension());
        fieldBuilder.addField(MILESTONE_ID, new AnalysisDimension());
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
