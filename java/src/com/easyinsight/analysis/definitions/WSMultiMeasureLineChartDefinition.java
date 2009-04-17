package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;

/**
 * User: James Boe
 * Date: Apr 16, 2009
 * Time: 4:41:08 PM
 */
public class WSMultiMeasureLineChartDefinition extends WSMultiMeasureDefinition {
    public int getChartType() {
        return ChartDefinitionState.MULTI_MEASURE_LINE_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.LINE_FAMILY;
    }
}
