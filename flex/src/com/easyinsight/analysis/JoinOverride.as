/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/1/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.feedassembly.CompositeFeedConnection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.JoinOverride")]
public class JoinOverride {

    public var sourceItem:AnalysisItem;
    public var targetItem:AnalysisItem;
    public var sourceName:String;
    public var targetName:String;
    public var dataSourceID:int;
    public var sourceOuterJoin:Boolean;
    public var targetOuterJoin:Boolean;
    public var sourceJoinOriginal:Boolean;
    public var targetJoinOriginal:Boolean;

    public function JoinOverride() {
    }

    public function toConnection():CompositeFeedConnection {
        var connection:CompositeFeedConnection = new CompositeFeedConnection();
        connection.sourceFeedID = DerivedKey(sourceItem.key).feedID;
        connection.targetFeedID = DerivedKey(targetItem.key).feedID;
        connection.sourceFeedName = sourceName;
        connection.targetFeedName = targetName;
        connection.sourceItem = sourceItem;
        connection.targetItem = targetItem;
        connection.sourceOuterJoin = sourceOuterJoin;
        connection.targetOuterJoin = targetOuterJoin;
        connection.sourceJoinOnOriginal = sourceJoinOriginal;
        connection.targetJoinOnOriginal = targetJoinOriginal;
        return connection;
    }

    public function matches(join:JoinOverride):Boolean {
        return sourceItem.matches(join.sourceItem) &&
                targetItem.matches(join.targetItem) &&
                dataSourceID == join.dataSourceID;
    }

    public function updateFromSaved(join:JoinOverride):void {
        sourceItem.updateFromSaved(join.sourceItem);
        targetItem.updateFromSaved(join.targetItem);
    }
}
}
