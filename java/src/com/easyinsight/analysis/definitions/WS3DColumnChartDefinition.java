package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:59:19 PM
 */
public class WS3DColumnChartDefinition extends WSXAxisDefinition {
    public int getChartType() {
        return ChartDefinitionState.COLUMN_3D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.COLUMN_FAMILY;
    }
}
