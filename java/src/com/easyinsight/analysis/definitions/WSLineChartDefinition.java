package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinition;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:18 PM
 */
public class WSLineChartDefinition extends WSTwoAxisDefinition {
    public int getChartType() {
        return ChartDefinition.LINE_2D;
    }

    public int getChartFamily() {
        return ChartDefinition.LINE_FAMILY;
    }
}
