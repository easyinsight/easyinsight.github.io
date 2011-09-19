package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Feb 8, 2010
 * Time: 11:05:02 AM
 */
public class NamedKeySpecification implements KeySpecification {
    private String key;

    public NamedKeySpecification(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Nullable
    public AnalysisItem findAnalysisItem(Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap) {
        List<AnalysisItem> analysisItems = keyMap.get(key);
        if (analysisItems == null) {
            analysisItems = displayMap.get(key);
        }
        if (analysisItems != null) {
            return analysisItems.get(0);
        }
        return null;
    }
}
