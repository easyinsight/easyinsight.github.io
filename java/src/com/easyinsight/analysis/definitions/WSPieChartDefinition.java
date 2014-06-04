package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:44:12 PM
 */
public class WSPieChartDefinition extends WSXAxisDefinition {

    private String labelPosition;
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private int legendMaxWidth;

    public int getLegendMaxWidth() {
        return legendMaxWidth;
    }

    public void setLegendMaxWidth(int legendMaxWidth) {
        this.legendMaxWidth = legendMaxWidth;
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

    public int getChartType() {
        return ChartDefinitionState.PIE_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.PIE_FAMILY;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        labelPosition = findStringProperty(properties, "labelPosition", "outside");
        legendMaxWidth = (int) findNumberProperty(properties, "legendMaxWidth", 200);
        multiColors = multiColorProperty(properties, "multiColors");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportNumericProperty("legendMaxWidth", legendMaxWidth));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    protected List<MultiColor> configuredMultiColors() {
        return multiColors;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject pie = super.toJSON(htmlReportMetadata, parentDefinitions);
        pie.put("key", getUrlKey());
        pie.put("type", "pie");
        pie.put("styles", htmlReportMetadata.createStyleProperties());
        pie.put("url", "/app/pieChart");
        return pie;
    }
}
