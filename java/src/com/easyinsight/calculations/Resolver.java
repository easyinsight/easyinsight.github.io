package com.easyinsight.calculations;

import com.easyinsight.analysis.AggregateMeasureKey;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jul 11, 2008
 * Time: 4:37:21 PM
 */
public class Resolver {

    private Map<String, List<Key>> keyMap = new HashMap<String, List<Key>>();

    public Resolver(FeedDefinition feedDefinition) {
        this(feedDefinition.getFields());
    }

    public Resolver(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            List<Key> keys = keyMap.get(analysisItem.getKey().toKeyString());
            if (keys == null) {
                keys = new ArrayList<Key>();
                keyMap.put(analysisItem.getKey().toKeyString(), keys);
            }
            keys.add(analysisItem.getKey());
        }
    }

    public Resolver() {
        
    }

    @Nullable
    public Key getKey(String name) {
        List<Key> keys = keyMap.get(name);
        if (keys != null) {
            return keyMap.get(name).get(0);
        } else {
            return null;
        }
    }

    public Key getKey(String name, int aggregation) {
        List<Key> keys = keyMap.get(name);
        for (Key key : keys) {
            if (key instanceof AggregateMeasureKey) {
                AggregateMeasureKey aggregateMeasureKey = (AggregateMeasureKey) key;
                if (aggregateMeasureKey.getAggregation() == aggregation) {
                    return aggregateMeasureKey;
                }
            }
        }
        return null;
    }

    public void addKey(Key key) {
        if (key == null) {
            return;
        }
        List<Key> keys = keyMap.get(key.toKeyString());
        if (keys == null) {
            keys = new ArrayList<Key>();
            keyMap.put(key.toKeyString(), keys);
        }
        keys.add(key);
    }
}
