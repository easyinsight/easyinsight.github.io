package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:38 PM
 */
public class WS3DAreaChartDefinition extends WSTwoAxisDefinition {
    public int getChartType() {
        return ChartDefinitionState.AREA_3D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.AREA_FAMILY;
    }
}
