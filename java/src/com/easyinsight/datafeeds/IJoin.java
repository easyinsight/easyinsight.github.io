package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 3/24/12
 * Time: 5:13 PM
 */
public interface IJoin {
    Long getSourceFeedID();

    Long getTargetFeedID();

    List<Key> getSourceJoins();

    List<AnalysisItem> getSourceItems();

    List<Key> getTargetJoins();

    List<AnalysisItem> getTargetItems();

    MergeAudit merge(DataSet sourceSet, DataSet dataSet, Set<AnalysisItem> sourceFields,
                     Set<AnalysisItem> targetFields, String sourceName, String targetName, EIConnection conn, long sourceID, long targetID, int operations);

    boolean isTargetJoinOnOriginal();

    String getBlockLabel();

    boolean isPostJoin();

    boolean isOptimized();

    void reconcile(List<CompositeFeedNode> compositeFeedNodes, List<AnalysisItem> fields);
}
