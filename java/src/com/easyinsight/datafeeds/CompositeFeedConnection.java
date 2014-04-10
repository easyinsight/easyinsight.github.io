package com.easyinsight.datafeeds;

import com.easyinsight.ReportQueryNodeKey;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.servlet.SystemSettings;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.Serializable;
import java.sql.Types;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:05 PM
 */
public class CompositeFeedConnection implements Serializable, IJoin {
    private Long sourceFeedID;
    private Long targetFeedID;
    private Long sourceReportID;
    private Long targetReportID;
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
    private boolean sourceJoinOnOriginal;
    private boolean targetJoinOnOriginal;
    private String sourceFeedName;
    private String targetFeedName;
    private String label = "";
    private boolean optimized;
    private String marmotScript;
    private int sourceCardinality;
    private int targetCardinality;
    private int forceOuterJoin;

    public CompositeFeedConnection() {
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceJoin = sourceJoin;
        sourceJoins = Arrays.asList(sourceJoin);
        this.targetJoin = targetJoin;
        targetJoins = Arrays.asList(targetJoin);
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceJoin = sourceJoin;
        sourceJoins = Arrays.asList(sourceJoin);
        this.targetJoin = targetJoin;
        targetJoins = Arrays.asList(targetJoin);
        this.sourceFeedName = sourceName;
        this.targetFeedName = targetName;
        this.sourceOuterJoin = sourceOuterJoin;
        this.targetOuterJoin = targetOuterJoin;
        this.sourceJoinOnOriginal = sourceJoinOnOriginal;
        this.targetJoinOnOriginal = targetJoinOnOriginal;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal, String marmotScript) {
        this(sourceFeedID, targetFeedID, sourceJoin, targetJoin, sourceName, targetName, sourceOuterJoin, targetOuterJoin, sourceJoinOnOriginal, targetJoinOnOriginal);
        this.marmotScript = marmotScript;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal, String marmotScript,
                                   int sourceCardinality, int targetCardinality, int forceOuterJoin) {
        this(sourceFeedID, targetFeedID, sourceJoin, targetJoin, sourceName, targetName, sourceOuterJoin, targetOuterJoin, sourceJoinOnOriginal, targetJoinOnOriginal);
        this.marmotScript = marmotScript;
        this.sourceCardinality = sourceCardinality;
        this.targetCardinality = targetCardinality;
        this.forceOuterJoin = forceOuterJoin;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, AnalysisItem sourceItem, AnalysisItem targetItem, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal, String marmotScript) {
        this(sourceFeedID, targetFeedID, sourceItem, targetItem, sourceName, targetName, sourceOuterJoin, targetOuterJoin, sourceJoinOnOriginal, targetJoinOnOriginal);
        this.marmotScript = marmotScript;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, AnalysisItem sourceItem, AnalysisItem targetItem, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal, String marmotScript,
                                   int sourceCardinality, int targetCardinality, int forceOuterJoin) {
        this(sourceFeedID, targetFeedID, sourceItem, targetItem, sourceName, targetName, sourceOuterJoin, targetOuterJoin, sourceJoinOnOriginal, targetJoinOnOriginal);
        this.marmotScript = marmotScript;
        this.sourceCardinality = sourceCardinality;
        this.targetCardinality = targetCardinality;
        this.forceOuterJoin = forceOuterJoin;
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, AnalysisItem sourceItem, AnalysisItem targetItem, String sourceName, String targetName,
                                   boolean sourceOuterJoin, boolean targetOuterJoin, boolean sourceJoinOnOriginal, boolean targetJoinOnOriginal) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceItem = sourceItem;
        sourceItems = Arrays.asList(sourceItem);
        this.targetItem = targetItem;
        targetItems = Arrays.asList(targetItem);
        this.sourceFeedName = sourceName;
        this.targetFeedName = targetName;
        this.sourceOuterJoin = sourceOuterJoin;
        this.targetOuterJoin = targetOuterJoin;
        this.sourceJoinOnOriginal = sourceJoinOnOriginal;
        this.targetJoinOnOriginal = targetJoinOnOriginal;
    }

    public int getSourceCardinality() {
        return sourceCardinality;
    }

    public void setSourceCardinality(int sourceCardinality) {
        this.sourceCardinality = sourceCardinality;
    }

    public int getTargetCardinality() {
        return targetCardinality;
    }

    public void setTargetCardinality(int targetCardinality) {
        this.targetCardinality = targetCardinality;
    }

    public int getForceOuterJoin() {
        return forceOuterJoin;
    }

    public void setForceOuterJoin(int forceOuterJoin) {
        this.forceOuterJoin = forceOuterJoin;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public QueryNodeKey sourceQueryNodeKey() {
        if (sourceReportID != null && sourceReportID > 0) {
            return new ReportQueryNodeKey(sourceReportID);
        } else {
            return new DataSourceQueryNodeKey(sourceFeedID);
        }
    }

    public QueryNodeKey targetQueryNodeKey() {
        if (targetReportID != null && targetReportID > 0) {
            return new ReportQueryNodeKey(targetReportID);
        } else {
            return new DataSourceQueryNodeKey(targetFeedID);
        }
    }

    public Long getSourceReportID() {
        return sourceReportID;
    }

    public void setSourceReportID(Long sourceReportID) {
        this.sourceReportID = sourceReportID;
    }

    public Long getTargetReportID() {
        return targetReportID;
    }

    public void setTargetReportID(Long targetReportID) {
        this.targetReportID = targetReportID;
    }

    public boolean isSourceJoinOnOriginal() {
        return sourceJoinOnOriginal;
    }

    public void setSourceJoinOnOriginal(boolean sourceJoinOnOriginal) {
        this.sourceJoinOnOriginal = sourceJoinOnOriginal;
    }

    public boolean isTargetJoinOnOriginal() {
        return targetJoinOnOriginal;
    }
    
    public void setBlockLabel(String label) {
        this.label = label;
    }

    public String getBlockLabel() {
        return label;
    }

    public boolean isPostJoin() {
        return false;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public void reconcile(List<CompositeFeedNode> compositeFeedNodes, List<AnalysisItem> fields) {

    }

    public void setTargetJoinOnOriginal(boolean targetJoinOnOriginal) {
        this.targetJoinOnOriginal = targetJoinOnOriginal;
    }

    public int dateLevelForJoin() {
        if (sourceItems != null && targetItems != null) {
            for (int i = 0; i < sourceItems.size(); i++) {
                AnalysisItem sourceItem = sourceItems.get(i);
                AnalysisItem targetItem = targetItems.get(i);
                if (sourceItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && targetItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    int sourceLevel = ((AnalysisDateDimension) sourceItem).getDateLevel();
                    int targetLevel = ((AnalysisDateDimension) targetItem).getDateLevel();
                    return sourceLevel;
                }
            }
        }
        return 0;
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

    public long store(Connection conn, long feedID) throws SQLException {
        if (sourceItem != null && targetItem != null) {
            PreparedStatement connInsertStmt = conn.prepareStatement("INSERT INTO COMPOSITE_CONNECTION (" +
                    "SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID, source_item_id, target_item_id, COMPOSITE_FEED_ID, " +
                    "left_join, right_join, left_join_on_original, right_join_on_original, marmot_script, source_report_id, target_report_id," +
                    "source_cardinality, target_cardinality, force_outer_join) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            if (sourceFeedID == null || sourceFeedID == 0) {
                connInsertStmt.setNull(1, Types.BIGINT);
            } else {
                connInsertStmt.setLong(1, sourceFeedID);
            }
            if (targetFeedID == null || targetFeedID == 0) {
                connInsertStmt.setNull(2, Types.BIGINT);
            } else {
                connInsertStmt.setLong(2, targetFeedID);
            }
            if (sourceItem.getAnalysisItemID() == 0) {
                Session session = Database.instance().createSession(conn);
                try {
                    sourceItem.reportSave(session);
                    session.save(sourceItem);
                } finally {
                    session.close();
                }
            }
            connInsertStmt.setLong(3, sourceItem.getAnalysisItemID());
            if (targetItem.getAnalysisItemID() == 0) {
                Session session = Database.instance().createSession(conn);
                try {
                    targetItem.reportSave(session);
                    session.save(targetItem);
                } finally {
                    session.close();
                }
            }
            connInsertStmt.setLong(4, targetItem.getAnalysisItemID());
            if (feedID > 0) {
                connInsertStmt.setLong(5, feedID);
            } else {
                connInsertStmt.setNull(5, Types.BIGINT);
            }
            connInsertStmt.setBoolean(6, sourceOuterJoin);
            connInsertStmt.setBoolean(7, targetOuterJoin);
            connInsertStmt.setBoolean(8, sourceJoinOnOriginal);
            connInsertStmt.setBoolean(9, targetJoinOnOriginal);
            connInsertStmt.setString(10, marmotScript);
            if (sourceReportID == null || sourceReportID == 0) {
                connInsertStmt.setNull(11, Types.BIGINT);
            } else {
                connInsertStmt.setLong(11, sourceReportID);
            }
            if (targetReportID == null || targetReportID == 0) {
                connInsertStmt.setNull(12, Types.BIGINT);
            } else {
                connInsertStmt.setLong(12, targetReportID);
            }
            connInsertStmt.setInt(13, sourceCardinality);
            connInsertStmt.setInt(14, targetCardinality);
            connInsertStmt.setInt(15, forceOuterJoin);
            connInsertStmt.execute();
            long id = Database.instance().getAutoGenKey(connInsertStmt);
            connInsertStmt.close();
            return id;
        } else {
            PreparedStatement connInsertStmt = conn.prepareStatement("INSERT INTO COMPOSITE_CONNECTION (" +
                    "SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID, SOURCE_JOIN, TARGET_JOIN, COMPOSITE_FEED_ID, " +
                    "left_join, right_join, left_join_on_original, right_join_on_original, marmot_script, source_report_id, target_report_id," +
                    "source_cardinality, target_cardinality, force_outer_join) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            if (sourceFeedID == null) {
                connInsertStmt.setNull(1, Types.BIGINT);
            } else {
                connInsertStmt.setLong(1, sourceFeedID);
            }
            if (targetFeedID == null) {
                connInsertStmt.setNull(2, Types.BIGINT);
            } else {
                connInsertStmt.setLong(2, targetFeedID);
            }
            connInsertStmt.setLong(3, sourceJoin.getKeyID());
            connInsertStmt.setLong(4, targetJoin.getKeyID());
            if (feedID > 0) {
                connInsertStmt.setLong(5, feedID);
            } else {
                connInsertStmt.setNull(5, Types.BIGINT);
            }

            connInsertStmt.setBoolean(6, sourceOuterJoin);
            connInsertStmt.setBoolean(7, targetOuterJoin);
            connInsertStmt.setBoolean(8, sourceJoinOnOriginal);
            connInsertStmt.setBoolean(9, targetJoinOnOriginal);
            connInsertStmt.setString(10, marmotScript);
            if (sourceReportID == null) {
                connInsertStmt.setNull(11, Types.BIGINT);
            } else {
                connInsertStmt.setLong(11, sourceReportID);
            }
            if (targetReportID == null) {
                connInsertStmt.setNull(12, Types.BIGINT);
            } else {
                connInsertStmt.setLong(12, targetReportID);
            }
            connInsertStmt.setInt(13, sourceCardinality);
            connInsertStmt.setInt(14, targetCardinality);
            connInsertStmt.setInt(15, forceOuterJoin);
            connInsertStmt.execute();
            long id = Database.instance().getAutoGenKey(connInsertStmt);
            connInsertStmt.close();
            return id;
        }
    }

    private boolean matches(AnalysisItem source, Key targetKey, long sourceID) {
        Key key = source.getKey();
        if (key.toKeyString().equals(targetKey.toKeyString())) {
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == sourceID) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(AnalysisItem source, Key targetKey) {
        Key key = source.getKey();
        return (key.toKeyString().equals(targetKey.toKeyString()));
    }

    public MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                            Set<AnalysisItem> targetFields, String sourceName, String targetName, EIConnection conn, long sourceID, long targetID, int operations) {
        Key myJoinDimension = null;
        if (sourceItem == null) {
            for (AnalysisItem item : sourceFields) {
                if (matches(item, getSourceJoin(), sourceID)) {
                    myJoinDimension = item.createAggregateKey();
                }
            }
            if (myJoinDimension == null) {
                for (AnalysisItem item : sourceFields) {
                    if (matches(item, getSourceJoin())) {
                        myJoinDimension = item.createAggregateKey();
                    }
                }
            }
        } else {
            for (AnalysisItem item : sourceFields) {
                if (matches(item, sourceItem.getKey(), sourceID)) {
                    myJoinDimension = item.createAggregateKey();
                }
            }
            if (myJoinDimension == null) {
                for (AnalysisItem item : sourceFields) {
                    if (matches(item, sourceItem.getKey())) {
                        myJoinDimension = item.createAggregateKey();
                    }
                }
            }
            if (myJoinDimension == null) {
                for (AnalysisItem item : sourceFields) {
                    if (sourceItem.toDisplay().equals(item.toDisplay())) {
                        myJoinDimension = item.createAggregateKey();
                    }
                }
            }
        }
        Key fromJoinDimension = null;
        if (targetItem == null) {
            for (AnalysisItem item : targetFields) {
                if (matches(item, getTargetJoin(), targetID)) {
                    fromJoinDimension = item.createAggregateKey();
                }
            }
            if (fromJoinDimension == null) {
                for (AnalysisItem item : targetFields) {
                    if (matches(item, getTargetJoin())) {
                        fromJoinDimension = item.createAggregateKey();
                    }
                }
            }
        } else {
            for (AnalysisItem item : targetFields) {
                if (matches(item, targetItem.getKey(), targetID)) {
                    fromJoinDimension = item.createAggregateKey();
                }
            }
            if (fromJoinDimension == null) {
                for (AnalysisItem item : targetFields) {
                    if (matches(item, targetItem.getKey())) {
                        fromJoinDimension = item.createAggregateKey();
                    }
                }
            }
            if (fromJoinDimension == null) {
                for (AnalysisItem item : targetFields) {
                    if (targetItem.toDisplay().equals(item.toDisplay())) {
                        fromJoinDimension = item.createAggregateKey();
                    }
                }
            }
        }
        if (myJoinDimension == null) {
            System.out.println("Couldn't find " + getSourceJoin().toKeyString() + " on " + sourceName);
        }
        if (fromJoinDimension == null) {
            if (getTargetItem() != null) {
                System.out.println("been looking for " + getTargetItem().toDisplay() + " with key " + getTargetItem().getKey().toKeyString() + " on " + targetName);
                for (AnalysisItem target : targetFields) {
                    System.out.println("\t" + target.toDisplay() + " with key " + target.getKey().toKeyString());
                }
            } else {
                System.out.println("Couldn't find " + getTargetJoin().toKeyString() + " on " + targetName);
            }

        }
        String mergeString = "Merging data set on " + sourceName + " : " + myJoinDimension.toKeyString() + " to " + targetName + " : " + fromJoinDimension.toKeyString();
        Map<Value, List<IRow>> index = new HashMap<Value, List<IRow>>();

        Collection<IRow> unjoinedRows = new ArrayList<IRow>();
        List<IRow> sourceSetRows = sourceSet.getRows();
        List<IRow> targetSetRows = dataSet.getRows();
        for (IRow row : sourceSetRows) {
            Value joinDimensionValue = row.getValue(myJoinDimension);
            if (joinDimensionValue == null || joinDimensionValue.type() == Value.EMPTY) {
                if (!sourceOuterJoin) {
                    operations++;
                    if (operations % 100000 == 0) {
                        validateOperation(operations);
                    }
                    unjoinedRows.add(row);
                }
            } else {
                List<IRow> rows = index.get(joinDimensionValue);
                if (rows == null) {
                    rows = new ArrayList<IRow>(1);
                    index.put(joinDimensionValue, rows);
                }
                operations++;
                if (operations % 100000 == 0) {
                    validateOperation(operations);
                }
                rows.add(row);
            }
        }
        DataSet result = new DataSet();
        result.getAudits().addAll(sourceSet.getAudits());
        result.getAudits().addAll(dataSet.getAudits());
        Map<Value, List<IRow>> indexCopy = new HashMap<Value, List<IRow>>(index);

        // for each row in target set...


        for (IRow row : targetSetRows) {
            // retrieve the join value from the target row

            Value joinDimensionValue = row.getValue(fromJoinDimension);
            if (joinDimensionValue == null || joinDimensionValue.type() == Value.EMPTY) {
                if (!targetOuterJoin) {
                    operations++;
                    if (operations % 100000 == 0) {
                        validateOperation(operations);
                    }
                    unjoinedRows.add(row);
                }
            } else {
                indexCopy.remove(joinDimensionValue);

                // find all rows in source indexed by the join value

                List<IRow> sourceRows = index.get(joinDimensionValue);
                if (sourceRows == null) {
                    if (!targetOuterJoin) {
                        operations++;
                        if (operations % 100000 == 0) {
                            validateOperation(operations);
                        }
                        unjoinedRows.add(row);
                    }
                } else {
                    for (IRow sourceRow : sourceRows) {
                        operations++;
                        if (operations % 100000 == 0) {
                            validateOperation(operations);
                        }

                        // for each project in source row, increment the count

                        sourceRow.incrementJoinCount();
                        row.incrementJoinCount();

                        sourceRow.merge(row, result);
                    }
                }
            }
        }

        int sourceTotal = 0;

        for (IRow row : sourceSet.getRows()) {
            sourceTotal += row.getJoinCount();
            row.resetJoinCount();
        }

        boolean hasSourceMeasure = false;
        for (AnalysisItem item : sourceFields) {
            if (item.hasType(AnalysisItemTypes.MEASURE)) {
                hasSourceMeasure = true;
                break;
            }
        }

        List<ReportAuditEvent> auditEvents = new ArrayList<ReportAuditEvent>();
        auditEvents.add(new ReportAuditEvent(ReportAuditEvent.JOIN, mergeString));

        if (hasSourceMeasure && sourceTotal > sourceSet.getRows().size() && getTargetCardinality() != IJoin.MANY) {
            StringBuilder sb = new StringBuilder();
            List<String> fields = new ArrayList<String>();
            for (AnalysisItem item : sourceFields) {
                if (item.hasType(AnalysisItemTypes.MEASURE)) {
                    fields.add(item.toOriginalDisplayName());
                }
            }
            for (int i = 0; i < fields.size(); i++) {
                String name = fields.get(i);
                sb.append("<b>").append(name).append("</b>");
                if (fields.size() == 1) {
                    // nothing else
                } else if (i == (fields.size() - 2)) {
                    sb.append(" and ");
                } else if (i < (fields.size() - 2)) {
                    sb.append(", ");
                }
            }
            auditEvents.add(new ReportAuditEvent(ReportAuditEvent.WARNING, "The join between <b>" + sourceName + "</b> and <b>" + targetName + "</b> " +
                    "may have incorrectly multiplied the values of " + sb.toString() + ". We suggest changing the target join cardinality to be <b>Many</b>."));
        }

        int targetTotal = 0;

        for (IRow row : dataSet.getRows()) {
            targetTotal += row.getJoinCount();
            row.resetJoinCount();
        }

        boolean hasTargetMeasure = false;
        for (AnalysisItem item : targetFields) {
            if (item.hasType(AnalysisItemTypes.MEASURE)) {
                hasTargetMeasure = true;
                break;
            }
        }
        if (hasTargetMeasure && targetTotal > dataSet.getRows().size() && getSourceCardinality() != IJoin.MANY) {
            StringBuilder sb = new StringBuilder();
            List<String> fields = new ArrayList<String>();
            for (AnalysisItem item : sourceFields) {
                if (item.hasType(AnalysisItemTypes.MEASURE)) {
                    fields.add(item.toOriginalDisplayName());
                }
            }
            for (int i = 0; i < fields.size(); i++) {
                String name = fields.get(i);
                sb.append("<b>").append(name).append("</b>");
                if (fields.size() == 1) {
                    // nothing else
                } else if (i == (fields.size() - 2)) {
                    sb.append(" and ");
                } else {
                    sb.append(", ");
                }
            }
            auditEvents.add(new ReportAuditEvent(ReportAuditEvent.WARNING, "The join between <b>" + sourceName + "</b> and <b>" + targetName + "</b> " +
                    "may have incorrectly multiplied the values of " + sb.toString() + ". We suggest changing the source join cardinality to be <b>Many</b>."));
        }

        for (List<IRow> rows : indexCopy.values()) {
            for (IRow row : rows) {
                result.createRow().addValues(row);
            }
        }
        for (IRow row : unjoinedRows) {
            result.createRow().addValues(row);
        }
        MergeAudit mergeAudit = new MergeAudit(mergeString, result, auditEvents);
        mergeAudit.setOperations(operations);
        return mergeAudit;
    }

    private void validateOperation(int operationCount) {
        if (operationCount > SystemSettings.instance().getMaxOperations()) {
            throw new ReportException(new GenericReportFault("The query requesting data was too complex."));
        }
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
