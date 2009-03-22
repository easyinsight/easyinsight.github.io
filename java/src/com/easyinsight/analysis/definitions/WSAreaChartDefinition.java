package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinition;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:30 PM
 */
public class WSAreaChartDefinition extends WSTwoAxisDefinition {
    public int getChartType() {
        return ChartDefinition.AREA_2D;
    }

    public int getChartFamily() {
        return ChartDefinition.AREA_FAMILY;
    }
}
