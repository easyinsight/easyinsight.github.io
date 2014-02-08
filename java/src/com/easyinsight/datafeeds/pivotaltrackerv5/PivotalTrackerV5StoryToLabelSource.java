package com.easyinsight.datafeeds.pivotaltrackerv5;

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
 * Date: 2/6/14
 * Time: 4:48 PM
 */
public class PivotalTrackerV5StoryToLabelSource extends PivotalTrackerV5BaseSource {

    public static final String STORY_ID = "Story ID";
    public static final String LABEL_ID = "Label ID";

    public PivotalTrackerV5StoryToLabelSource() {
        setFeedName("Story to Label");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_STORY_TO_LABEL;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(STORY_ID, new AnalysisDimension());
        fieldBuilder.addField(LABEL_ID, new AnalysisDimension());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        PivotalTrackerV5CompositeSource p = (PivotalTrackerV5CompositeSource) parentDefinition;
        for (Map.Entry<String, List<String>> entry : p.getEpicIDToLabelMap().entrySet()) {
            for (String val : entry.getValue()) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(STORY_ID), entry.getKey());
                row.addValue(keys.get(LABEL_ID), val);
            }
        }
        return dataSet;
    }
}
