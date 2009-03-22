package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinition;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:59:19 PM
 */
public class WS3DColumnChartDefinition extends WSXAxisDefinition {
    public int getChartType() {
        return ChartDefinition.COLUMN_3D;
    }

    public int getChartFamily() {
        return ChartDefinition.COLUMN_FAMILY;
    }
}
