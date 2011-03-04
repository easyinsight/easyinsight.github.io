package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.MergeAudit;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CompositeReportPipeline;
import com.easyinsight.pipeline.Pipeline;

import java.util.*;

/**
 * User: jamesboe
 * Date: Jun 14, 2010
 * Time: 9:00:29 PM
 */
public class FallthroughConnection extends CompositeFeedConnection {

    private List<CompositeFeedConnection> connections;

    public FallthroughConnection(Long sourceFeedID, Long targetFeedID, List<CompositeFeedConnection> connections) {
        super(sourceFeedID, targetFeedID);
        List<Key> sourceKeys = new ArrayList<Key>();
        List<Key> targetKeys = new ArrayList<Key>();
        for (CompositeFeedConnection connection : connections) {
            sourceKeys.add(connection.getSourceJoin());
            targetKeys.add(connection.getTargetJoin());
        }
        setSourceJoins(sourceKeys);
        setTargetJoins(targetKeys);
        this.connections = connections;
    }

    private static class MergeResults {
        private List<IRow> mergedRows;
        private List<IRow> unmergedSourceRows;
        private List<IRow> unmergedTargetRows;
        private String audit;

        private MergeResults(List<IRow> mergedRows, List<IRow> unmergedSourceRows, List<IRow> unmergedTargetRows, String audit) {
            this.mergedRows = mergedRows;
            this.unmergedSourceRows = unmergedSourceRows;
            this.unmergedTargetRows = unmergedTargetRows;
            this.audit = audit;
        }
    }

    private MergeResults merge(List<IRow> sourceRows, List<IRow> targetRows, Set<AnalysisItem> sourceFields, Set<AnalysisItem> targetFields,
                               List<CompositeFeedConnection> connections) {
        List<Key> sourceKeys = new ArrayList<Key>();
        List<Key> targetKeys = new ArrayList<Key>();
        //Key sourceJoin = null;
        for (CompositeFeedConnection connection : connections) {
            Key sourceJoin = null;
            Key targetJoin = null;
            for (AnalysisItem item : sourceFields) {
                if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getSourceJoin().toKeyString())) {
                    sourceJoin = item.createAggregateKey();
                }
            }
            //Key targetJoin = null;
            for (AnalysisItem item : targetFields) {
                if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getTargetJoin().toKeyString())) {
                    targetJoin = item.createAggregateKey();
                }
            }
            sourceKeys.add(sourceJoin);
            targetKeys.add(targetJoin);
        }

        String audit = "Merging data set on " + sourceKeys + " to " + targetKeys;
        Map<Set<Value>, List<IRow>> index = new HashMap<Set<Value>, List<IRow>>();
        List<IRow> unjoinedSourceRows = new ArrayList<IRow>();
        for (IRow row : sourceRows) {
            Set<Value> values = new HashSet<Value>();
            boolean valid = true;
            for (Key sourceKey : sourceKeys) {
                Value value = row.getValue(sourceKey);
                if (value == null || value.type() == Value.EMPTY) {
                    valid = false;
                }
                values.add(value);
            }
            if (valid) {
                List<IRow> rows = index.get(values);
                if (rows == null){
                    rows = new ArrayList<IRow>();
                    index.put(values, rows);
                }
                rows.add(row);
            } else {
                unjoinedSourceRows.add(row);
            }
        }
        Map<Set<Value>, List<IRow>> indexCopy = new HashMap<Set<Value>, List<IRow>>(index);
        List<IRow> unjoinedTargetRows = new ArrayList<IRow>();
        List<IRow> compositeRows = new LinkedList<IRow>();
        for (IRow row : targetRows) {
            Set<Value> values = new HashSet<Value>();
            boolean valid = true;
            for (Key targetKey : targetKeys) {
                Value value = row.getValue(targetKey);
                if (value == null || value.type() == Value.EMPTY) {
                    valid = false;
                }
                values.add(value);
            }
            if (valid) {
                indexCopy.remove(values);
                List<IRow> blahRows = index.get(values);
                if (blahRows == null) {
                    unjoinedSourceRows.add(row);
                } else {
                    for (IRow sourceRow : blahRows) {
                        compositeRows.add(sourceRow.merge(row));
                    }
                }
            } else {
                unjoinedTargetRows.add(row);
            }
        }

        for (List<IRow> rows : indexCopy.values()) {
            for (IRow row : rows) {
                unjoinedSourceRows.add(row);
            }
        }
        return new MergeResults(compositeRows, unjoinedSourceRows, unjoinedTargetRows, audit);
    }

    @Override
    public MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                            Set<AnalysisItem> targetFields, String sourceName, String targetName, EIConnection conn) {

        // start by merging items by to-do to-do ID -> time tracking to-do ID
        // for those items which can't be merged by IDs, merge by project name
        List<IRow> compositeRows = new ArrayList<IRow>();
        List<IRow> sourceRows = sourceSet.getRows();
        List<IRow> targetRows = dataSet.getRows();
        List<IRow> outerJoinRows = new ArrayList<IRow>();
        List<String> mergeStrings = new ArrayList<String>();
        for (int i = 0; i < connections.size(); i++) {
                                                 
            List<CompositeFeedConnection> conns = new ArrayList<CompositeFeedConnection>();
            for (int j = i; j < connections.size(); j++) {
                conns.add(connections.get(j));
            }
            for (int j = 0; j < i; j++) {
                CompositeFeedConnection connection = connections.get(j);
                for (IRow row : sourceRows) {
                    IRow unjoined = connection.removeSourceValues(row, sourceFields);
                    if (connection.isSourceOuterJoin()) {
                        outerJoinRows.add(unjoined);
                    }
                }
                DataSet sourceCopySet = new DataSet(sourceRows);
                sourceRows = combinedData(sourceFields, getSourceFeedID(), sourceCopySet, conn).getRows();
                for (IRow row : targetRows) {
                    IRow unjoined = connection.removeTargetValues(row, targetFields);
                    if (connection.isTargetOuterJoin()) {
                        outerJoinRows.add(unjoined);
                    }
                }
                DataSet targetCopySet = new DataSet(targetRows);
                targetRows = combinedData(targetFields, getTargetFeedID(), targetCopySet, conn).getRows();
            }
            MergeResults mergeResults = merge(sourceRows, targetRows, sourceFields, targetFields, conns);
            compositeRows.addAll(mergeResults.mergedRows);
            sourceRows = mergeResults.unmergedSourceRows;
            targetRows = mergeResults.unmergedTargetRows;
            sourceRows = combinedData(sourceFields, getSourceFeedID(), new DataSet(sourceRows), conn).getRows();
            targetRows = combinedData(targetFields, getTargetFeedID(), new DataSet(targetRows), conn).getRows();
            mergeStrings.add(mergeResults.audit);
        }
        //compositeRows.addAll(sourceRows);
        //compositeRows.addAll(targetRows);
        compositeRows.addAll(outerJoinRows);
        DataSet result = new DataSet(compositeRows);
        result.normalize();
        return new MergeAudit(mergeStrings, result);
    }

    private DataSet combinedData(Set<AnalysisItem> neededItems, long sourceID, DataSet dataSet, EIConnection conn) {
        Feed feed = FeedRegistry.instance().getFeed(sourceID, conn);
        Pipeline pipeline = new CompositeReportPipeline();
        WSListDefinition analysisDefinition = new WSListDefinition();
        analysisDefinition.setColumns(new ArrayList<AnalysisItem>(neededItems));
        pipeline.setup(analysisDefinition, feed, new InsightRequestMetadata());
        return pipeline.toDataSet(dataSet);
    }
}
