package com.easyinsight.datafeeds.teamwork;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
public class TeamworkProjectSource extends TeamworkBaseSource {

    public static final String PROJECT_NAME = "Project Name";
    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_CATEGORY = "Project Category";
    public static final String PROJECT_DESCRIPTION = "Project Description";
    public static final String PROJECT_LAST_CHANGED_ON = "Project Last Changed On";
    public static final String PROJECT_STARRED = "Project Starred";
    public static final String PROJECT_COMPANY_NAME = "Project Company Name";
    public static final String PROJECT_COMPANY_ID = "Project Company ID";
    public static final String PROJECT_START_DATE = "Project Start Date";
    public static final String PROJECT_STATUS = "Project Status";
    public static final String PROJECT_ANNOUNCEMENT = "Project Announcement";

    public TeamworkProjectSource() {
        setFeedName("Projects");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PROJECT_NAME, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_STARRED, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_COMPANY_NAME, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_STATUS, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ANNOUNCEMENT, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_START_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(PROJECT_LAST_CHANGED_ON, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            TeamworkCompositeSource teamworkCompositeSource = (TeamworkCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(teamworkCompositeSource.getTeamworkApiKey());
            Map results = runRestRequestForMap("projects.json", httpClient, teamworkCompositeSource);
            List<Map> projects = (List<Map>) results.get("projects");
            for (Map project : projects) {
                System.out.println(project);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PROJECT_NAME), getValue(project, "name"));
                row.addValue(keys.get(PROJECT_ID), getValue(project, "id"));
                row.addValue(keys.get(PROJECT_DESCRIPTION), getValue(project, "description"));
                row.addValue(keys.get(PROJECT_STARRED), getValue(project, "starred"));
                row.addValue(keys.get(PROJECT_STATUS), getValue(project, "status"));
                row.addValue(keys.get(PROJECT_ANNOUNCEMENT), getValue(project, "announcement"));
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_PROJECT;
    }
}
