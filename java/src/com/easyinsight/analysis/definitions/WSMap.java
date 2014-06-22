package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: jamesboe
 * Date: 6/5/14
 * Time: 2:09 PM
 */
public class WSMap extends WSAnalysisDefinition {

    private AnalysisItem region;
    private AnalysisItem measure;
    private AnalysisItem latitude;
    private AnalysisItem longitude;
    private AnalysisItem pointMeasure;
    private AnalysisItem pointGrouping;
    private int regionFillStart;
    private int regionFillEnd;
    private int noDataFill = 0xCCCCCC;
    private String map = "US States";
    private List<MultiColor> pointColors = new ArrayList<>();
    private long mapID;

    public int getNoDataFill() {
        return noDataFill;
    }

    public void setNoDataFill(int noDataFill) {
        this.noDataFill = noDataFill;
    }

    public List<MultiColor> getPointColors() {
        return pointColors;
    }

    public void setPointColors(List<MultiColor> pointColors) {
        this.pointColors = pointColors;
    }

    public AnalysisItem getPointGrouping() {
        return pointGrouping;
    }

    public void setPointGrouping(AnalysisItem pointGrouping) {
        this.pointGrouping = pointGrouping;
    }

    public int getRegionFillStart() {
        return regionFillStart;
    }

    public void setRegionFillStart(int regionFillStart) {
        this.regionFillStart = regionFillStart;
    }

    public int getRegionFillEnd() {
        return regionFillEnd;
    }

    public void setRegionFillEnd(int regionFillEnd) {
        this.regionFillEnd = regionFillEnd;
    }

    public AnalysisItem getLatitude() {
        return latitude;
    }

    public void setLatitude(AnalysisItem latitude) {
        this.latitude = latitude;
    }

    public AnalysisItem getLongitude() {
        return longitude;
    }

    public void setLongitude(AnalysisItem longitude) {
        this.longitude = longitude;
    }

    public AnalysisItem getPointMeasure() {
        return pointMeasure;
    }

    public void setPointMeasure(AnalysisItem pointMeasure) {
        this.pointMeasure = pointMeasure;
    }

    public long getMapID() {
        return mapID;
    }

    public void setMapID(long mapID) {
        this.mapID = mapID;
    }

    public AnalysisItem getRegion() {
        return region;
    }

    public void setRegion(AnalysisItem region) {
        this.region = region;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.TOPO;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> sets = new HashSet<AnalysisItem>();
        sets.add(region);
        sets.add(measure);
        if (latitude != null && longitude != null && pointMeasure != null) {
            sets.add(latitude);
            sets.add(longitude);
            sets.add(pointMeasure);
            if (pointGrouping != null) {
                sets.add(pointGrouping);
            }
        }
        return sets;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("region", Arrays.asList(region), structure);
        addItems("measure", Arrays.asList(measure), structure);
        if (latitude != null) {
            addItems("latitude", Arrays.asList(latitude), structure);
        }
        if (longitude != null) {
            addItems("longitude", Arrays.asList(longitude), structure);
        }
        if (pointMeasure != null) {
            addItems("pointMeasure", Arrays.asList(pointMeasure), structure);
        }
        if (pointGrouping != null) {
            addItems("pointGrouping", Arrays.asList(pointGrouping), structure);
        }
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        region = firstItem("region", structure);
        measure = firstItem("measure", structure);
        latitude = firstItem("latitude", structure);
        longitude = firstItem("longitude", structure);
        pointMeasure = firstItem("pointMeasure", structure);
        pointGrouping = firstItem("pointGrouping", structure);
    }

    public List<String> createMultiColors() {
        List<MultiColor> multiColors = configuredMultiColors();
        List<String> resultColors = new ArrayList<String>();
        if (multiColors != null && !multiColors.isEmpty()) {
            MultiColor testColor = multiColors.get(0);
            if (testColor.isColor1StartEnabled()) {
                for (MultiColor color : multiColors) {
                    if (color.isColor1StartEnabled()) {
                        resultColors.add(String.format("#%06X", (0xFFFFFF & color.getColor1Start())));
                    }
                }
                return resultColors;
            }
        }
        return Arrays.asList("#a6bc59", "#597197", "#d6ab2a", "#d86068", "#5d9942",
                "#7a4c6c", "#F0B400", "#1E6C0B", "#00488C", "#332600", "#D84000");
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        map = findStringProperty(properties, "map", "US States");
        regionFillStart = (int) findNumberProperty(properties, "regionFillStart", 0x0);
        regionFillEnd = (int) findNumberProperty(properties, "regionFillEnd", 0x0);
        pointColors = multiColorProperty(properties, "pointColors");
        noDataFill = (int) findNumberProperty(properties, "noDataFill", 0xCCCCCC);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("map", map));
        properties.add(new ReportNumericProperty("regionFillStart", regionFillStart));
        properties.add(new ReportNumericProperty("regionFillEnd", regionFillEnd));
        properties.add(new ReportNumericProperty("noDataFill", noDataFill));
        properties.add(ReportMultiColorProperty.fromColors(pointColors, "pointColors"));
        return properties;
    }

    protected List<MultiColor> configuredMultiColors() {
        return pointColors;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {

        JSONObject areaChart = super.toJSON(htmlReportMetadata, parentDefinitions);
        areaChart.put("type", "topomap");
        areaChart.put("key", getUrlKey());
        areaChart.put("url", "/app/topoMap");
        areaChart.put("parameters", new JSONObject());
        areaChart.put("styles", htmlReportMetadata.createStyleProperties());
        return areaChart;
    }
}
