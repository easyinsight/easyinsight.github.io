package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/15/10
 * Time: 10:52 AM
 */
public abstract class AnalysisItemOverride implements Serializable {
    private long analysisItemID;

    public long getAnalysisItemID() {
        return analysisItemID;
    }

    public void setAnalysisItemID(long analysisItemID) {
        this.analysisItemID = analysisItemID;
    }

    public abstract void apply(Set<AnalysisItem> allAnalysisItems);
}
