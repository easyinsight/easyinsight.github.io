package com.easyinsight.datafeeds.internal;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 10/27/14
 * Time: 2:02 PM
 */
public class InternalFeed extends Feed {
    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        Map<String, Key> keys = new HashMap<>();
        for (AnalysisItem analysisItem : analysisItems) {
            keys.put(analysisItem.getKey().toKeyString(), analysisItem.createAggregateKey());
        }
        List<DataSourceDescriptor> dataSources = new FeedService().searchForSubscribedFeeds();
        DataSet dataSet = new DataSet();
        for (DataSourceDescriptor dataSourceDescriptor : dataSources) {
            IRow row = dataSet.createRow();
            row.addValue(keys.get(InternalDataSource.DATA_SOURCE_NAME), dataSourceDescriptor.getName());
            row.addValue(keys.get(InternalDataSource.DATA_SOURCE_API_KEY), dataSourceDescriptor.getUrlKey());
            row.addValue(keys.get(InternalDataSource.LAST_REFRESH_TIME), dataSourceDescriptor.getLastDataTime());
        }
        return dataSet;
    }
}
