package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:43:52 PM
 */
public class WS3DBarChartDefinition extends WSYAxisDefinition {
    public int getChartType() {
        return ChartDefinitionState.BAR_3D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
    }
}
