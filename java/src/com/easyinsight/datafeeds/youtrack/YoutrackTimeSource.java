package com.easyinsight.datafeeds.youtrack;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 5/27/14
 * Time: 8:35 AM
 */
public class YoutrackTimeSource extends YouTrackBaseSource {

    public static final String ISSUE_ID = "Time Entry Issue ID";
    public static final String DURATION = "Time Entry Duration";
    public static final String DATE = "Time Entry Date";
    public static final String WORK_AUTHOR = "Time Entry Work Author";

    public YoutrackTimeSource() {
        setFeedName("Time");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.YOUTRACK_TIME;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ISSUE_ID, new AnalysisDimension());
        fieldBuilder.addField(WORK_AUTHOR, new AnalysisDimension());
        fieldBuilder.addField(DURATION, new AnalysisMeasure());
        fieldBuilder.addField(DATE, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        YouTrackCompositeSource youTrackCompositeSource = (YouTrackCompositeSource) parentDefinition;
        List<YoutrackTimeEntry> time = youTrackCompositeSource.getTimeEntries();
        DataSet dataSet = new DataSet();
        for (YoutrackTimeEntry entry : time) {
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ISSUE_ID), entry.getIssueID());
            row.addValue(keys.get(WORK_AUTHOR), entry.getAuthor());
            row.addValue(keys.get(DATE), entry.getDate());
            row.addValue(keys.get(DURATION), entry.getDuration());
        }
        return dataSet;
    }
}
