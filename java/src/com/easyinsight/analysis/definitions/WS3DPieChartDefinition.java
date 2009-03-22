package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinition;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:44:19 PM
 */
public class WS3DPieChartDefinition extends WSXAxisDefinition {
    public int getChartType() {
        return ChartDefinition.PIE_2D;
    }

    public int getChartFamily() {
        return ChartDefinition.PIE_FAMILY;
    }
}
