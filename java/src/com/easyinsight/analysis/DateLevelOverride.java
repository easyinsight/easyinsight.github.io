package com.easyinsight.analysis;

import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/15/10
 * Time: 10:52 AM
 */
public class DateLevelOverride extends AnalysisItemOverride {
    private int dateLevel;

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public void apply(Set<AnalysisItem> allAnalysisItems) {
        for (AnalysisItem analysisItem : allAnalysisItems) {
            if (analysisItem.getAnalysisItemID() == getAnalysisItemID()) {
                AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                date.setDateLevel(dateLevel);
            }
        }
    }
}
