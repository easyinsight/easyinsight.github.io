package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.Row;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.Serializable;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:05 PM
 */
public class CompositeFeedConnection implements Serializable {
    private Long sourceFeedID;
    private Long targetFeedID;
    private Key sourceJoin;
    private Key targetJoin;
    private List<Key> sourceJoins;
    private List<Key> targetJoins;
    private boolean sourceOuterJoin;
    private boolean targetOuterJoin;

    public CompositeFeedConnection() {
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceJoin = sourceJoin;
        sourceJoins = Arrays.asList(sourceJoin);
        this.targetJoin = targetJoin;
        targetJoins = Arrays.asList(targetJoin);
    }

    public boolean isSourceOuterJoin() {
        return sourceOuterJoin;
    }

    public void setSourceOuterJoin(boolean sourceOuterJoin) {
        this.sourceOuterJoin = sourceOuterJoin;
    }

    public boolean isTargetOuterJoin() {
        return targetOuterJoin;
    }

    public void setTargetOuterJoin(boolean targetOuterJoin) {
        this.targetOuterJoin = targetOuterJoin;
    }

    public void setSourceJoins(List<Key> sourceJoins) {
        this.sourceJoins = sourceJoins;
    }

    public void setTargetJoins(List<Key> targetJoins) {
        this.targetJoins = targetJoins;
    }

    public List<Key> getSourceJoins() {
        return sourceJoins;
    }

    public List<Key> getTargetJoins() {
        return targetJoins;
    }

    public Long getSourceFeedID() {
        return sourceFeedID;
    }

    public void setSourceFeedID(Long sourceFeedID) {
        this.sourceFeedID = sourceFeedID;
    }

    public Long getTargetFeedID() {
        return targetFeedID;
    }

    public void setTargetFeedID(Long targetFeedID) {
        this.targetFeedID = targetFeedID;
    }

    public Key getSourceJoin() {
        return sourceJoin;
    }

    public void setSourceJoin(Key sourceJoin) {
        this.sourceJoin = sourceJoin;
        sourceJoins = Arrays.asList(sourceJoin);
    }

    public Key getTargetJoin() {
        return targetJoin;
    }

    public void setTargetJoin(Key targetJoin) {
        this.targetJoin = targetJoin;
        targetJoins = Arrays.asList(targetJoin);
    }

    public void store(Connection conn, long feedID) throws SQLException {
        PreparedStatement connInsertStmt = conn.prepareStatement("INSERT INTO COMPOSITE_CONNECTION (" +
                "SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID, SOURCE_JOIN, TARGET_JOIN, COMPOSITE_FEED_ID) VALUES (?, ?, ?, ?, ?)");
        connInsertStmt.setLong(1, sourceFeedID);
        connInsertStmt.setLong(2, targetFeedID);
        connInsertStmt.setLong(3, sourceJoin.getKeyID());
        connInsertStmt.setLong(4, targetJoin.getKeyID());
        connInsertStmt.setLong(5, feedID);
        connInsertStmt.execute();
        connInsertStmt.close();
    }

    public MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                            Set<AnalysisItem> targetFields, String sourceName, String targetName, Set<AnalysisItem> masterItems) {
        Key myJoinDimension = null;
        for (AnalysisItem item : sourceFields) {
            if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(getSourceJoin().toKeyString())) {
                myJoinDimension = item.createAggregateKey();
            }
        }
        Key fromJoinDimension = null;
        for (AnalysisItem item : targetFields) {
            if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(getTargetJoin().toKeyString())) {
                fromJoinDimension = item.createAggregateKey();
            }
        }
        String mergeString = "Merging data set on " + sourceName + " : " + myJoinDimension.toKeyString() + " to " + targetName + " : " + fromJoinDimension.toKeyString();

        Map<Value, List<IRow>> index = new HashMap<Value, List<IRow>>();
        Collection<IRow> unjoinedRows = new ArrayList<IRow>();
        for (IRow row : sourceSet.getRows()) {
            Value joinDimensionValue = row.getValue(myJoinDimension);
            if (joinDimensionValue == null) {
                LogClass.debug("bad bad bad");
            } else {
                List<IRow> rows = index.get(joinDimensionValue);
                if (rows == null){
                    rows = new ArrayList<IRow>();
                    index.put(joinDimensionValue, rows);
                }
                rows.add(row);
            }
        }
        Map<Value, List<IRow>> indexCopy = new HashMap<Value, List<IRow>>(index);
        List<IRow> compositeRows = new LinkedList<IRow>();
        for (IRow row : dataSet.getRows()) {
            Value joinDimensionValue = row.getValue(fromJoinDimension);
            if (joinDimensionValue == null) {
                LogClass.debug("bad bad bad");
            } else {
                indexCopy.remove(joinDimensionValue);
                List<IRow> sourceRows = index.get(joinDimensionValue);
                if (sourceRows == null) {
                    unjoinedRows.add(row);
                } else {
                    for (IRow sourceRow : sourceRows) {
                        compositeRows.add(sourceRow.merge(row));
                    }
                }
            }
        }
        for (List<IRow> rows : indexCopy.values()) {
            compositeRows.addAll(rows);
        }
        compositeRows.addAll(unjoinedRows);
        return new MergeAudit(mergeString, new DataSet(compositeRows));
    }

    private List<Key> removeSourceKeys;

    public void setRemoveSourceKeys(List<Key> removeSourceKeys) {
        this.removeSourceKeys = removeSourceKeys;
    }

    private List<Key> removeTargetKeys;

    public void setRemoveTargetKeys(List<Key> removeTargetKeys) {
        this.removeTargetKeys = removeTargetKeys;
    }

    public boolean hasRemoveSourceKeys() {
        return removeSourceKeys != null && removeSourceKeys.size() > 0;
    }

    public boolean hasRemoveTargetKeys() {
        return removeTargetKeys != null && removeTargetKeys.size() > 0;
    }

    public IRow removeSourceValues(IRow row, Set<AnalysisItem> sourceFields) {
        IRow outerRow = new Row();
        outerRow.addValues(new HashMap<Key, Value>(row.getValues()));
        for (Key key : removeSourceKeys) {
            Key matchedKey = null;
            for (AnalysisItem item : sourceFields) {
                if (item.getKey().toBaseKey().getKeyID() == (key.toBaseKey().getKeyID())) {
                    matchedKey = item.createAggregateKey();
                }
            }
            row.removeValue(matchedKey);
        }
        return outerRow;
    }

    public IRow removeTargetValues(IRow row, Set<AnalysisItem> targetFields) {
        IRow outerRow = new Row();
        outerRow.addValues(row.getValues());
        for (Key key : removeTargetKeys) {
            Key matchedKey = null;
            for (AnalysisItem item : targetFields) {
                if (item.getKey().toBaseKey().getKeyID() == (key.toBaseKey().getKeyID())) {
                    matchedKey = item.createAggregateKey();
                }
            }
            row.removeValue(matchedKey);
        }
        return outerRow;
    }
}
