package com.easyinsight.datafeeds;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

import java.util.*;
import java.sql.SQLException;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Apr 26, 2008
 * Time: 8:07:57 PM
 */
public class StaticFeed extends Feed implements Serializable {

    public FeedType getDataFeedType() {
        return FeedType.STATIC;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        if (analysisItems.size() == 0) {
            return new DataSet();
        }
        DataSet dataSet;
        DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
        try {
            insightRequestMetadata.setGmtData(getDataSource().gmtTime());
            dataSet = source.retrieveData(analysisItems, filters, null, insightRequestMetadata);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            source.closeConnection();
        }
        return dataSet;
    }
}
