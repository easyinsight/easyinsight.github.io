package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataService;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;


import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: May 17, 2010
 * Time: 3:34:50 PM
 */
public class JoinTester {
    private CompositeFeedConnection connection;

    public JoinTester(CompositeFeedConnection connection) {
        this.connection = connection;
    }

    public JoinAnalysis generateReport() throws SQLException {
        DataService dataService = new DataService();
        FeedDefinition sourceData = new FeedStorage().getFeedDefinitionData(connection.getSourceFeedID());
        AnalysisItem sourceItem = getAnalysisItem(sourceData, connection.getSourceJoin());
        AnalysisDimensionResultMetadata sourceMetadata = (AnalysisDimensionResultMetadata) dataService.getAnalysisItemMetadata(connection.getSourceFeedID(), sourceItem, 0);
        FeedDefinition targetData = new FeedStorage().getFeedDefinitionData(connection.getTargetFeedID());
        AnalysisItem targetItem = getAnalysisItem(targetData, connection.getTargetJoin());
        AnalysisDimensionResultMetadata targetMetadata = (AnalysisDimensionResultMetadata) dataService.getAnalysisItemMetadata(connection.getTargetFeedID(), targetItem, 0);

        // how many items matched, how many didn't match from each side

        Set<String> index = new HashSet<String>();
        List<String> unjoinedRows = new ArrayList<String>();
        for (Value joinDimensionValue : sourceMetadata.getValues()) {
            String string = joinDimensionValue.toString();
            index.add(string);
        }
        Set<String> indexCopy = new HashSet<String>(index);
        List<String> compositeRows = new LinkedList<String>();
        for (Value joinDimensionValue : targetMetadata.getValues()) {
            String string = joinDimensionValue.toString();
            indexCopy.remove(string);
            if (index.contains(string)) {
                compositeRows.add(string);
            } else {
                unjoinedRows.add(string);
            }
        }

        JoinAnalysis joinAnalysis = new JoinAnalysis();
        joinAnalysis.setJoinedValues(compositeRows);
        joinAnalysis.setSourceDataSourceName(sourceData.getFeedName());
        joinAnalysis.setUnjoinedSourceValues(new ArrayList<String>(indexCopy));
        joinAnalysis.setTargetDataSourceName(targetData.getFeedName());
        joinAnalysis.setUnjoinedTargetValues(unjoinedRows);
        return joinAnalysis;
    }

    private AnalysisItem getAnalysisItem(FeedDefinition dataSource, Key key) throws SQLException {
        return dataSource.findAnalysisItem(key.toKeyString());
    }
}
