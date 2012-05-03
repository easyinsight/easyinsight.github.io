package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.Row;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import java.io.Serializable;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:05 PM
 */
public class AltFallthroughConnection implements Serializable, IJoin {
    
    private List<CompositePair> pairs;
    private String sourceName;
    private String targetName;
    private String label = "";

    public AltFallthroughConnection(List<CompositePair> pairs, String sourceName, String targetName) {
        this.pairs = pairs;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public String getBlockLabel() {
        return label;
    }

    public boolean isPostJoin() {
        return true;
    }

    public boolean isOptimized() {
        return false;
    }

    public void reconcile(List<CompositeFeedNode> nodes, List<AnalysisItem> fields) {
        
        for (CompositeFeedNode node : nodes) {
            if (sourceName.equals(node.getDataSourceName())) {
                sourceFeedID = node.getDataFeedID();
            }
            if (targetName.equals(node.getDataSourceName())) {
                targetFeedID = node.getDataFeedID();
            }
        }
        for (CompositePair compositePair : pairs) {
            for (AnalysisItem field : fields) {
                if (field.toDisplay().equals(compositePair.getField1())) {
                    sourceItems.add(field);
                }
                if (field.toDisplay().equals(compositePair.getField2())) {
                    targetItems.add(field);
                }
            }
        }
    }
    
    private Long sourceFeedID;
    private Long targetFeedID;
    
    private List<AnalysisItem> sourceItems = new ArrayList<AnalysisItem>();
    private List<AnalysisItem> targetItems = new ArrayList<AnalysisItem>();
    private String sourceFeedName;
    private String targetFeedName;

    public AltFallthroughConnection() {
    }

    public List<AnalysisItem> getSourceItems() {
        return sourceItems;
    }

    public List<Key> getTargetJoins() {
        return new ArrayList<Key>();
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

    public Long getSourceFeedID() {
        return sourceFeedID;
    }

    public void setSourceFeedID(Long sourceFeedID) {
        this.sourceFeedID = sourceFeedID;
    }

    public Long getTargetFeedID() {
        return targetFeedID;
    }

    public List<Key> getSourceJoins() {
        return new ArrayList<Key>();
    }

    public void setTargetFeedID(Long targetFeedID) {
        this.targetFeedID = targetFeedID;
    }

    public MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                            Set<AnalysisItem> targetFields, String sourceName, String targetName, EIConnection conn, long sourceID, long targetID, int operations) {

        Map<Value, List<IRow>> index = new HashMap<Value, List<IRow>>();
        /*Collection<AnalysisItem> sourceItems = new ArrayList<AnalysisItem>();
        Collection<AnalysisItem> targetItems = new ArrayList<AnalysisItem>();*/
        /*Collection<IRow> unjoinedRows = new ArrayList<IRow>();
        List<IRow> sourceSetRows = sourceSet.getRows();
        List<IRow> targetSetRows = dataSet.getRows();*/
        DataSet result = new DataSet();
        for (int z = 0; z < sourceItems.size(); z ++) {
            AnalysisItem myJoinDimension = sourceItems.get(z);
            AnalysisItem fromJoinDimension = targetItems.get(z);
            Collection<IRow> unjoinedRows = new ArrayList<IRow>();
            List<IRow> sourceSetRows = sourceSet.getRows();
            List<IRow> targetSetRows = dataSet.getRows();
            for (IRow row : sourceSetRows) {
                Value joinDimensionValue = row.getValue(myJoinDimension);
                if (joinDimensionValue == null || joinDimensionValue.type() == Value.EMPTY) {
                } else {
                    List<IRow> rows = index.get(joinDimensionValue);
                    if (rows == null) {
                        rows = new ArrayList<IRow>(1);
                        index.put(joinDimensionValue, rows);
                    }
                    rows.add(row);
                }
            }
            
            Map<Value, List<IRow>> indexCopy = new HashMap<Value, List<IRow>>(index);
            Iterator<IRow> targetIter = targetSetRows.iterator();
            while (targetIter.hasNext()) {
                IRow row = targetIter.next();
                Value joinDimensionValue = row.getValue(fromJoinDimension);
                if (joinDimensionValue == null || joinDimensionValue.type() == Value.EMPTY) {
                } else {
                    indexCopy.remove(joinDimensionValue);
                    List<IRow> sourceRows = index.get(joinDimensionValue);
                    if (sourceRows == null) {
                    } else {
                        targetIter.remove();
                        for (IRow sourceRow : sourceRows) {
                            sourceRow.setMarked(true);
                            sourceRow.merge(row, result);
                        }
                    }
                }
            }


            /*for (List<IRow> rows : indexCopy.values()) {
                for (IRow row : rows) {
                    result.createRow().addValues(row);
                }
            }
            for (IRow row : unjoinedRows) {
                result.createRow().addValues(row);
            }*/
        }
        
        for (IRow row : sourceSet.getRows()) {
            if (!row.isMarked()) {
                result.addRow(row);
            }
        }

        return new MergeAudit("", result);
    }

    public boolean isTargetJoinOnOriginal() {
        return false;
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
