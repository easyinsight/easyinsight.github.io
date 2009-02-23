package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 4:52:21 PM
 */
public class AnalysisText extends AnalysisItem {
    public int getType() {
        return AnalysisItemTypes.TEXT;
    }
}
