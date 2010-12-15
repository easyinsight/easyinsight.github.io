package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Oct 6, 2009
 * Time: 3:09:22 PM
 */
public class HierarchyOverride extends AnalysisItemOverride {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void apply(Set<AnalysisItem> allAnalysisItems) {
        for (AnalysisItem analysisItem : allAnalysisItems) {
            if (analysisItem.getAnalysisItemID() == getAnalysisItemID()) {
                AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) analysisItem;
                hierarchy.setHierarchyLevel(hierarchy.getHierarchyLevels().get(getPosition()));
            }
        }
    }
}
