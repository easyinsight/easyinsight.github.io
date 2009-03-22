package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ChartDefinition;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSBarChartDefinition extends WSYAxisDefinition {

    public int getChartType() {
        return ChartDefinition.BAR_2D;
    }

    public int getChartFamily() {
        return ChartDefinition.BAR_FAMILY;
    }
}