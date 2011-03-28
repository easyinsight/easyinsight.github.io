
package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 3/24/11
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestTaskSource extends HarvestBaseSource {

    public static final String BILLABLE_BY_DEFAULT = "Tasks - Billable By Default";
    public static final String DEACTIVATED = "Tasks - Deactivated";
    public static final String DEFAULT_HOURLY_RATE = "Tasks - Default Hourly Rate";
    public static final String ID = "Tasks - ID";
    public static final String DEFAULT = "Tasks - Default";
    public static final String NAME = "Tasks - Name";
    public static final String TASK_COUNT = "Tasks - Count";


    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem taskId = new AnalysisDimension(keys.get(ID), true);
        taskId.setHidden(true);
        analysisItems.add(taskId);
        analysisItems.add(new AnalysisDimension(keys.get(DEACTIVATED), true));
        analysisItems.add(new AnalysisDimension(keys.get(BILLABLE_BY_DEFAULT), true));
        analysisItems.add(new AnalysisMeasure(keys.get(DEFAULT_HOURLY_RATE), AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisDimension(keys.get(DEFAULT), true));
        analysisItems.add(new AnalysisDimension(keys.get(NAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(TASK_COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BILLABLE_BY_DEFAULT, DEACTIVATED, DEFAULT_HOURLY_RATE,  ID, DEFAULT, NAME, TASK_COUNT);
    }

    public HarvestTaskSource() {
        setFeedName("Tasks");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_TASKS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document tasks = runRestRequest("/tasks", client, builder, source.getUrl(), true, source, true);
            Nodes taskNodes = tasks.query("/tasks/task");
            for(int i = 0;i < taskNodes.size();i++) {
                Node curTask = taskNodes.get(i);
                String billableByDefault = queryField(curTask, "billable-by-default/text()");
                String deactivated = queryField(curTask, "deactivated/text()");
                String defaultHourlyRate = queryField(curTask, "default-hourly-rate/text()");
                String taskId = queryField(curTask, "id/text()");
                String isDefault = queryField(curTask, "is-default/text()");
                String name = queryField(curTask, "name/text()");
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), taskId);
                row.addValue(keys.get(BILLABLE_BY_DEFAULT), billableByDefault);
                if(defaultHourlyRate != null && defaultHourlyRate.length() > 0)
                    row.addValue(keys.get(DEFAULT_HOURLY_RATE), Double.parseDouble(defaultHourlyRate));
                row.addValue(keys.get(DEACTIVATED), deactivated);
                row.addValue(keys.get(DEFAULT), isDefault);
                row.addValue(keys.get(NAME), name);
                row.addValue(keys.get(TASK_COUNT), 1.0);
            }
        } catch (ParsingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }

        return dataSet;
    }
}
