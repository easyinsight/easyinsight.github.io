package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
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
public class FixedConnection implements Serializable, IJoin {

    private String fixedSourceValue;
    private String targetField;
    private String sourceName;
    private String targetName;
    private AnalysisItem theTargetField;

    public FixedConnection(String sourceName, String targetName, String fixedSourceValue, String targetField) {
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.fixedSourceValue = fixedSourceValue;
        this.targetField = targetField;
    }

    public String getBlockLabel() {
        return "";
    }

    public boolean isPostJoin() {
        return false;
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

        for (AnalysisItem field : fields) {
            if (field.toDisplay().equals(targetField)) {
                theTargetField = field;
                targetItems.add(field);
            }
        }

    }

    private Long sourceFeedID;
    private Long targetFeedID;

    private List<AnalysisItem> sourceItems = new ArrayList<AnalysisItem>();
    private List<AnalysisItem> targetItems = new ArrayList<AnalysisItem>();
    private String sourceFeedName;
    private String targetFeedName;

    public FixedConnection() {
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

        List<IRow> sourceSetRows = sourceSet.getRows();
        List<IRow> targetSetRows = dataSet.getRows();

        Map<String, IRow> index = new HashMap<String, IRow>();
        for (IRow row : targetSetRows) {
            Value value = row.getValue(theTargetField);
            if (value.toString().equals(fixedSourceValue)) {
                index.put(fixedSourceValue, row);
            }
        }

        DataSet result = new DataSet();
        for (IRow row : sourceSetRows) {
            IRow targetRow = index.get(fixedSourceValue);
            row.merge(targetRow, result);
        }

        return new MergeAudit("", result);
    }

    public boolean isTargetJoinOnOriginal() {
        return false;
    }
}
