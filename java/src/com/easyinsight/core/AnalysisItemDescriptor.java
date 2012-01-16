package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 1/16/12
 * Time: 10:15 AM
 */
public class AnalysisItemDescriptor extends EIDescriptor {
    @Override
    public int getType() {
        return EIDescriptor.ANALYSIS_ITEM;
    }

    public AnalysisItemDescriptor(AnalysisItem analysisItem) {
        super(analysisItem.toDisplay(), analysisItem.getAnalysisItemID(), false);
    }
}
