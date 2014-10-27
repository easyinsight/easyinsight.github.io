package com.easyinsight.datafeeds.internal;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/27/14
 * Time: 1:56 PM
 */
public class InternalDataSource extends ServerDataSourceDefinition {

    public static final String DATA_SOURCE_NAME = "Data Source Name";
    public static final String DATA_SOURCE_API_KEY = "Data Source API Key";
    public static final String LAST_REFRESH_TIME = "Last Refresh Time";

    public InternalDataSource() {
        setFeedName("Internal");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(DATA_SOURCE_NAME, new AnalysisDimension());
        fieldBuilder.addField(DATA_SOURCE_API_KEY, new AnalysisDimension());
        fieldBuilder.addField(LAST_REFRESH_TIME, new AnalysisDateDimension(AnalysisDateDimension.MINUTE_LEVEL));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage,
                              EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new InternalFeed();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INTERNAL_DATA_SOURCE;
    }
}
