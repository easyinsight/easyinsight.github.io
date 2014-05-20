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
 * Date: 4/14/14
 * Time: 10:27 AM
 */
public class PivotalTrackerV5StoryOwnerService extends PivotalTrackerV5BaseSource {

    public static final String ID = "Story to Owner ID";
    public static final String NAME = "Owner Name";

    public PivotalTrackerV5StoryOwnerService() {
        setFeedName("Story to Owner");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_STORY_TO_OWNER;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        PivotalTrackerV5CompositeSource p = (PivotalTrackerV5CompositeSource) parentDefinition;
        for (Map.Entry<String, List<String>> entry : p.getStoryIDToUserMap().entrySet()) {
            for (String val : entry.getValue()) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), entry.getKey());
                String userName = p.getUserMap().get(val);
                row.addValue(keys.get(NAME), userName);
            }
        }
        return dataSet;
    }
}
