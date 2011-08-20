package com.easyinsight.datafeeds.composite;

import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 14, 2010
 * Time: 9:13:23 PM
 */
public class MultiChildConnection extends ChildConnection {

    private List<String> sourceKeys = new ArrayList<String>();
    private List<String> targetKeys = new ArrayList<String>();
    private List<List<String>> removeSourceKeys = new ArrayList<List<String>>();
    private List<List<String>> removeTargetKeys = new ArrayList<List<String>>();
    private boolean sourceOuterJoin;
    private boolean targetOuterJoin;

    public MultiChildConnection(FeedType sourceFeedType, FeedType targetFeedType, List<String> sourceKeys,
                                List<String> targetKeys, List<List<String>> removeSourceKeys, List<List<String>> removeTargetKeys,
                                boolean sourceOuterJoin, boolean targetOuterJoin) {
        super(sourceFeedType, targetFeedType, sourceKeys.get(0), targetKeys.get(0));
        this.sourceKeys = sourceKeys;
        this.targetKeys = targetKeys;
        this.removeSourceKeys = removeSourceKeys;
        this.removeTargetKeys = removeTargetKeys;
        this.sourceOuterJoin = sourceOuterJoin;
        this.targetOuterJoin = targetOuterJoin;
    }

    @Override
    public CompositeFeedConnection createConnection(IServerDataSourceDefinition sourceDef, IServerDataSourceDefinition targetDef) {
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        for (int i = 0; i < sourceKeys.size(); i++) {
            String sourceKeyString = sourceKeys.get(i);
            String targetKeyString = targetKeys.get(i);
            Key sourceKey = sourceDef.getField(sourceKeyString);
            Key targetKey = targetDef.getField(targetKeyString);
            CompositeFeedConnection conn = new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                    sourceKey, targetKey, sourceDef.getFeedName(), targetDef.getFeedName(), false);
            conn.setSourceOuterJoin(sourceOuterJoin);
            List<Key> sourceKeyList = new ArrayList<Key>();
            List<String> removeKeyStrings = removeSourceKeys.get(i);
            for (String removeKey : removeKeyStrings) {
                sourceKeyList.add(sourceDef.getField(removeKey));
            }
            conn.setRemoveSourceKeys(sourceKeyList);
            List<Key> targetKeyList = new ArrayList<Key>();
            List<String> removeTargetKeys = this.removeTargetKeys.get(i);
            for (String removeKey : removeTargetKeys) {
                targetKeyList.add(targetDef.getField(removeKey));
            }
            conn.setRemoveTargetKeys(targetKeyList);
            conn.setTargetOuterJoin(targetOuterJoin);
            connections.add(conn);
        }
        return new FallthroughConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(), connections);
    }
}
