package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:44:19 PM
 */
public class WS3DPieChartDefinition extends WSXAxisDefinition {

    private String labelPosition;
    private String colorScheme;

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public int getChartType() {
        return ChartDefinitionState.PIE_3D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.PIE_FAMILY;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        labelPosition = findStringProperty(properties, "labelPosition", "outside");
        colorScheme = findStringProperty(properties, "colorScheme", "Bright Gradients");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportStringProperty("colorScheme", colorScheme));
        return properties;
    }
}
