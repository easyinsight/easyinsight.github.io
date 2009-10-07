package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Oct 6, 2009
 * Time: 3:09:22 PM
 */
public class HierarchyOverride implements Serializable {
    private long analysisItemID;
    private int position;

    public long getAnalysisItemID() {
        return analysisItemID;
    }

    public void setAnalysisItemID(long analysisItemID) {
        this.analysisItemID = analysisItemID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HierarchyOverride that = (HierarchyOverride) o;

        if (analysisItemID != that.analysisItemID) return false;
        if (position != that.position) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (analysisItemID ^ (analysisItemID >>> 32));
        result = 31 * result + position;
        return result;
    }
}
