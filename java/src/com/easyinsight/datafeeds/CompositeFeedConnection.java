package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.Row;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
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
    private AnalysisItem sourceItem;
    private AnalysisItem targetItem;
    private List<Key> sourceJoins = new ArrayList<Key>();
    private List<Key> targetJoins = new ArrayList<Key>();
    private List<AnalysisItem> sourceItems = new ArrayList<AnalysisItem>();
    private List<AnalysisItem> targetItems = new ArrayList<AnalysisItem>();
    private boolean sourceOuterJoin;
    private boolean targetOuterJoin;
    private String sourceFeedName;
    private String targetFeedName;

    public CompositeFeedConnection() {
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin, String sourceName, String targetName) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceJoin = sourceJoin;
        sourceJoins = Arrays.asList(sourceJoin);
        this.targetJoin = targetJoin;
        targetJoins = Arrays.asList(targetJoin);
        this.sourceFeedName = sourceName;
        this.targetFeedName = targetName;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, AnalysisItem sourceItem, AnalysisItem targetItem, String sourceName, String targetName) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceItem = sourceItem;
        sourceItems = Arrays.asList(sourceItem);
        this.targetItem = targetItem;
        targetItems = Arrays.asList(targetItem);
        this.sourceFeedName = sourceName;
        this.targetFeedName = targetName;
    }

    public List<AnalysisItem> getSourceItems() {
        return sourceItems;
    }

    public void setSourceItems(List<AnalysisItem> sourceItems) {
        this.sourceItems = sourceItems;
    }

    public List<AnalysisItem> getTargetItems() {
        return targetItems;
    }

    public void setTargetItems(List<AnalysisItem> targetItems) {
        this.targetItems = targetItems;
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

    public String getSourceFeedName() {
        return sourceFeedName;
    }

    public void setSourceFeedName(String sourceFeedName) {
        this.sourceFeedName = sourceFeedName;
    }

    public String getTargetFeedName() {
        return targetFeedName;
    }

    public void setTargetFeedName(String targetFeedName) {
        this.targetFeedName = targetFeedName;
    }

    public AnalysisItem getSourceItem() {
        return sourceItem;
    }

    public void setSourceItem(AnalysisItem sourceItem) {
        this.sourceItem = sourceItem;
    }

    public AnalysisItem getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(AnalysisItem targetItem) {
        this.targetItem = targetItem;
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
        if (sourceItem != null && targetItem != null) {
            PreparedStatement connInsertStmt = conn.prepareStatement("INSERT INTO COMPOSITE_CONNECTION (" +
                    "SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID, source_item_id, target_item_id, COMPOSITE_FEED_ID) VALUES (?, ?, ?, ?, ?)");
            connInsertStmt.setLong(1, sourceFeedID);
            connInsertStmt.setLong(2, targetFeedID);
            connInsertStmt.setLong(3, sourceItem.getAnalysisItemID());
            connInsertStmt.setLong(4, targetItem.getAnalysisItemID());
            connInsertStmt.setLong(5, feedID);
            connInsertStmt.execute();
            connInsertStmt.close();
        } else {
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
    }

    public MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                            Set<AnalysisItem> targetFields, String sourceName, String targetName, EIConnection conn) {
        Key myJoinDimension = null;
        System.out.println(sourceSet.getRows().size() + " - " + dataSet.getRows().size());
        System.out.println(Runtime.getRuntime().totalMemory() + " and " + Runtime.getRuntime().freeMemory());
        System.out.println(sourceName + " to " + targetName);
        if (sourceItem == null) {
            for (AnalysisItem item : sourceFields) {
                if (item.getKey().toKeyString().equals(getSourceJoin().toKeyString())) {
                    myJoinDimension = item.createAggregateKey();
                }
            }
        } else {
            for (AnalysisItem item : sourceFields) {
                if (item.getKey().toKeyString().equals(sourceItem.getKey().toKeyString())) {
                    myJoinDimension = item.createAggregateKey();
                }
            }
            //myJoinDimension = sourceItem.createAggregateKey();
        }
        Key fromJoinDimension = null;
        if (targetItem == null) {
            for (AnalysisItem item : targetFields) {
                if (item.getKey().toKeyString().equals(getTargetJoin().toKeyString())) {
                    fromJoinDimension = item.createAggregateKey();
                }
            }
        } else {
            for (AnalysisItem item : targetFields) {
                if (item.getKey().toKeyString().equals(targetItem.getKey().toKeyString())) {
                    fromJoinDimension = item.createAggregateKey();
                }
            }
        }
        if (myJoinDimension == null) {
            System.out.println("Couldn't find " + getSourceJoin().toKeyString() + " on " + sourceName);
        }
        if (fromJoinDimension == null) {
            System.out.println("Couldn't find " + getTargetJoin().toKeyString() + " on " + targetName);
        }
        String mergeString = "Merging data set on " + sourceName + " : " + myJoinDimension.toKeyString() + " to " + targetName + " : " + fromJoinDimension.toKeyString();

        Map<Value, List<IRow>> index = new HashMap<Value, List<IRow>>();
        int size = Math.max(sourceSet.getRows().size(), dataSet.getRows().size());
        Collection<IRow> unjoinedRows = new ArrayList<IRow>();
        List<IRow> sourceSetRows = sourceSet.getRows();
        List<IRow> targetSetRows = dataSet.getRows();
        Iterator<IRow> sourceIter = sourceSetRows.iterator();
        while (sourceIter.hasNext()) {
            IRow row = sourceIter.next();
            Value joinDimensionValue = row.getValue(myJoinDimension);
            if (joinDimensionValue == null) {
                LogClass.debug("bad bad bad");
            } else {
                List<IRow> rows = index.get(joinDimensionValue);
                if (rows == null){
                    rows = new ArrayList<IRow>(1);
                    index.put(joinDimensionValue, rows);
                }
                rows.add(row);
            }
            sourceIter.remove();
        }
        System.out.println("index size = " + index.size());
        Map<Value, List<IRow>> indexCopy = new HashMap<Value, List<IRow>>(index);
        List<IRow> compositeRows = new ArrayList<IRow>(size);
        Iterator<IRow> targetIter = targetSetRows.iterator();
        int mergeCount = 0;
        int rowCount = 0;
        while (targetIter.hasNext()) {
            IRow row = targetIter.next();
            rowCount++;
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
                        mergeCount++;
                        if (mergeCount % 1000 == 0) {
                            System.out.println(mergeCount + "and " + rowCount + " on joining " + joinDimensionValue + " and " + sourceRows.size());
                        }
                        compositeRows.add(sourceRow.merge(row));
                    }
                }
            }
            targetIter.remove();
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
        IRow outerRow = new Row(row.getDataSetKeys());
        outerRow.addValues(row);
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
        IRow outerRow = new Row(row.getDataSetKeys());
        outerRow.addValues(row);
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
