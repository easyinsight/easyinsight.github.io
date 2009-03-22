package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinition;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:43:52 PM
 */
public class WS3DBarChartDefinition extends WSYAxisDefinition {
    public int getChartType() {
        return ChartDefinition.BAR_3D;
    }

    public int getChartFamily() {
        return ChartDefinition.BAR_FAMILY;
    }
}
