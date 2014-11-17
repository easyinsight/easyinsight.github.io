package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.preferences.ApplicationSkin;
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
    private String labelType;
    private double donutRatio;

    public double getDonutRatio() {
        return donutRatio;
    }

    public void setDonutRatio(double donutRatio) {
        this.donutRatio = donutRatio;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

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
        labelType = findStringProperty(properties, "labelType", "Value");
        legendMaxWidth = (int) findNumberProperty(properties, "legendMaxWidth", 200);
        multiColors = multiColorProperty(properties, "multiColors");
        donutRatio = findNumberProperty(properties, "donutRatio", 0);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportStringProperty("labelType", labelType));
        properties.add(new ReportNumericProperty("legendMaxWidth", legendMaxWidth));
        properties.add(new ReportNumericProperty("donutRatio", donutRatio));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme()) && applicationSkin.getMultiColors() != null && applicationSkin.getMultiColors().size() > 0 &&
                applicationSkin.getMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getMultiColors());
        } else if ("Secondary".equals(getColorScheme()) && applicationSkin.getSecondaryMultiColors() != null && applicationSkin.getSecondaryMultiColors().size() > 0 &&
                applicationSkin.getSecondaryMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getSecondaryMultiColors());
        }
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
