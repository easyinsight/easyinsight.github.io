package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:44:19 PM
 */
public class WS3DPieChartDefinition extends WSXAxisDefinition {
    public int getChartType() {
        return ChartDefinitionState.PIE_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.PIE_FAMILY;
    }
}
