package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;
import com.easyinsight.analysis.ReportBooleanProperty;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:30 PM
 */
public class WSAreaChartDefinition extends WSTwoAxisDefinition {

    private String stackingType = "stacked";

    public String getStackingType() {
        return stackingType;
    }

    public void setStackingType(String stackingType) {
        this.stackingType = stackingType;
    }

    public int getChartType() {
        return ChartDefinitionState.AREA_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.AREA_FAMILY;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        stackingType = findStringProperty(properties, "stackingType", "stacked");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("stackingType", stackingType));
        return properties;
    }
}
