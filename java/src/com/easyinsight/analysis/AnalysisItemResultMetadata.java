package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.AnalysisItem;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 4:34:21 PM
 */
public class AnalysisItemResultMetadata implements Serializable {

    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addValue(AnalysisItem analysisItem, Value value) {
    }
}
