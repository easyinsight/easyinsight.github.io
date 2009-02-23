package com.easyinsight.calculations;

import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.analysis.AnalysisItem;

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

    private Map<String, Key> keyMap = new HashMap<String, Key>();

    public Resolver(FeedDefinition feedDefinition) {
        this(feedDefinition.getFields());
    }

    public Resolver(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            keyMap.put(analysisItem.getKey().toKeyString(), analysisItem.getKey());
        }
    }

    public Resolver() {
        
    }

    @Nullable
    public Key getKey(String name) {
        return keyMap.get(name);
    }

    public void addKey(Key key) {
        keyMap.put(key.toKeyString(), key);
    }
}
