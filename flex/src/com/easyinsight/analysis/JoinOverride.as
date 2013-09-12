/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/1/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.feedassembly.CompositeConnectionPair;
import com.easyinsight.feedassembly.CompositeFeedCompositeConnection;
import com.easyinsight.feedassembly.CompositeFeedConnection;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.JoinOverride")]
public class JoinOverride {

    public static const NORMAL:int = 1;
    public static const COMPOSITE:int = 2;
    public static const DATE:int = 3;

    public var joinType:int = JoinOverride.NORMAL;

    // default join conditions

    public var sourceItem:AnalysisItem;
    public var targetItem:AnalysisItem;
    public var sourceName:String;
    public var targetName:String;
    public var dataSourceID:int;
    public var sourceOuterJoin:Boolean;
    public var targetOuterJoin:Boolean;
    public var sourceJoinOriginal:Boolean;
    public var targetJoinOriginal:Boolean;

    public var sourceCardinality:int;
    public var targetCardinality:int;
    public var forceOuterJoin:int;

    // composite join conditions

    public var sourceItems:ArrayCollection;
    public var targetItems:ArrayCollection;

    // date join conditions

    public var dates:ArrayCollection;

    public var marmotScript:String;

    public function JoinOverride() {
    }

    public function toConnection():CompositeFeedConnection {
        var connection:CompositeFeedConnection = new CompositeFeedConnection();
        if (sourceItem.key is DerivedKey) {
            connection.sourceFeedID = DerivedKey(sourceItem.key).feedID;
        } else {
            connection.sourceReportID = ReportKey(sourceItem.key).reportID;
        }
        if (targetItem.key is DerivedKey) {
            connection.targetFeedID = DerivedKey(targetItem.key).feedID;
        } else {
            connection.targetReportID = ReportKey(targetItem.key).reportID;
        }
        connection.sourceCardinality = sourceCardinality;
        connection.targetCardinality = targetCardinality;
        connection.forceOuterJoin = forceOuterJoin;
        connection.sourceFeedName = sourceName;
        connection.targetFeedName = targetName;
        connection.sourceItem = sourceItem;
        connection.targetItem = targetItem;
        connection.sourceOuterJoin = sourceOuterJoin;
        connection.targetOuterJoin = targetOuterJoin;
        connection.sourceJoinOnOriginal = sourceJoinOriginal;
        connection.targetJoinOnOriginal = targetJoinOriginal;
        connection.marmotScript = marmotScript;
        return connection;
    }

    public function matches(join:JoinOverride):Boolean {
        if (sourceItem != null && targetItem != null) {
            return sourceItem.matches(join.sourceItem) &&
                targetItem.matches(join.targetItem) &&
                dataSourceID == join.dataSourceID;
        }
        return false;
    }

    public function updateFromSaved(join:JoinOverride):void {
        sourceItem.updateFromSaved(join.sourceItem);
        targetItem.updateFromSaved(join.targetItem);
    }

    public function toCompositeConnection():CompositeFeedCompositeConnection {
        var compositeFeedCompositeConnection:CompositeFeedCompositeConnection = new CompositeFeedCompositeConnection();
        var pairs:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < sourceItems.length; i++) {
            var sourceField:AnalysisItem = sourceItems.getItemAt(i) as AnalysisItem;
            var targetField:AnalysisItem = targetItems.getItemAt(i) as AnalysisItem;
            var pair:CompositeConnectionPair = new CompositeConnectionPair();
            pair.sourceField = sourceField;
            pair.targetField = targetField;
            pairs.addItem(pair);
        }
        compositeFeedCompositeConnection.pairs = pairs;
        return compositeFeedCompositeConnection;
    }
}
}
