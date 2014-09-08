package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/7/14
 * Time: 4:56 PM
 */
public class SampleTaskSource extends ServerDataSourceDefinition {
    public static final String TASK_ID = "Task ID";
    public static final String TASK_CREATED_AT = "Task Created At";
    public static final String TASK_COMPLETED_AT = "Task Completed At";
    public static final String TASK_DUE = "Task Due At";

    public SampleTaskSource() {
        setFeedName("Tasks");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_TASKS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TASK_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(TASK_COMPLETED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(TASK_DUE, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            for (int i = -500; i < 500; i++) {
                ZonedDateTime zdt = ZonedDateTime.now();
                zdt = zdt.plusDays(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(TASK_ID), String.valueOf(i));
                row.addValue(keys.get(TASK_CREATED_AT), new DateValue(Date.from(zdt.toInstant())));
            }
            IDataStorage.insertData(dataSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
