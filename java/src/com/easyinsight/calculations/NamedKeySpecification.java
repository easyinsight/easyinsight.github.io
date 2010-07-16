package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Nullable
    public AnalysisItem findAnalysisItem(List<AnalysisItem> currentItems) {
        for (AnalysisItem item : currentItems) {
            if ((item.getKey().toKeyString().equals(key) || (item.getDisplayName() != null && item.getDisplayName().equals(key)))) {
                return item;
            }
        }
        return null;
    }
}
