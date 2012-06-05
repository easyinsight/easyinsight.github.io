package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 4:52:27 PM
 */
public class AnalysisImage extends AnalysisItem {
    public int getType() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int actualType() {
        return AnalysisItemTypes.IMAGE;
    }
}
