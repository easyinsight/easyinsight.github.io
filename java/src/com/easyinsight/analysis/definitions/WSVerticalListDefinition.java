package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:51:51 PM
 */
public class WSVerticalListDefinition extends WSAnalysisDefinition {

    private List<AnalysisItem> measures;
    private AnalysisItem column;

    private long verticalListID;

    private int headerWidth;
    private int columnWidth;

    private String patternName;

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public int getHeaderWidth() {
        return headerWidth;
    }

    public void setHeaderWidth(int headerWidth) {
        this.headerWidth = headerWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public long getVerticalListID() {
        return verticalListID;
    }

    public void setVerticalListID(long verticalListID) {
        this.verticalListID = verticalListID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.VERTICAL_LIST;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        if (measures != null) {
            items.addAll(measures);
        }
        if (column != null) {
            items.add(column);
        }
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measures", measures, structure);
        if (column != null) {
            addItems("grouping", Arrays.asList(column), structure);
        }
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measures = items("measures", structure);
        List<AnalysisItem> columns = items("grouping", structure);
        if (columns.size() > 0) {
            column = columns.get(0);
        }
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public AnalysisItem getColumn() {
        return column;
    }

    public void setColumn(AnalysisItem column) {
        this.column = column;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        headerWidth = (int) findNumberProperty(properties, "headerWidth", 140);
        columnWidth = (int) findNumberProperty(properties, "columnWidth", 73);
        patternName = findStringProperty(properties, "patternName", "");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("headerWidth", headerWidth));
        properties.add(new ReportNumericProperty("columnWidth", columnWidth));
        properties.add(new ReportStringProperty("patternName", patternName));
        return properties;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "trend_definition");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }

    protected boolean supportsMultiField() {
        return true;
    }

    protected List<AnalysisItem> reportFieldsForMultiField() {
        return measures;
    }

    public void argh() {
        Map<String, List<AnalysisItem>> map = new LinkedHashMap<String, List<AnalysisItem>>();
        boolean firstPH = true;
        for (AnalysisItem field : measures) {
            boolean defined = false;
            if (field.getReportFieldExtension() != null && field.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension verticalListExtension = (VerticalListReportExtension) field.getReportFieldExtension();
                if (verticalListExtension.getSection() != null && !"".equals(verticalListExtension.getSection().trim())) {
                    defined = true;
                    String section = verticalListExtension.getSection();
                    List<AnalysisItem> items = map.get(section);
                    if (items == null) {
                        items = new ArrayList<AnalysisItem>();
                        AnalysisMeasure placeHolder = new AnalysisMeasure(new NamedKey(section), AggregationTypes.SUM);
                        VerticalListReportExtension ext = new VerticalListReportExtension();
                        ext.setAlwaysShow(true);
                        placeHolder.setReportFieldExtension(ext);
                        items.add(placeHolder);
                        map.put(section, items);
                        if (firstPH) {
                            firstPH = false;
                        } else {
                            ext.setLineAbove(true);
                        }
                    }
                    items.add(field);
                }
            }
            if (!defined) {
                List<AnalysisItem> items = map.get(null);
                if (items == null) {
                    items = new ArrayList<AnalysisItem>();
                    map.put(null, items);
                }
                items.add(field);
            }
        }
        List<AnalysisItem> result = new ArrayList<AnalysisItem>();
        for (List<AnalysisItem> list : map.values()) {
            result.addAll(list);
        }
        setMeasures(result);
    }

    protected void assignResults(List<AnalysisItem> fields) {
        Map<String, List<AnalysisItem>> map = new LinkedHashMap<String, List<AnalysisItem>>();
        boolean firstPH = true;
        for (AnalysisItem field : fields) {
            boolean defined = false;
            if (field.getReportFieldExtension() != null && field.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension verticalListExtension = (VerticalListReportExtension) field.getReportFieldExtension();
                if (verticalListExtension.getSection() != null && !"".equals(verticalListExtension.getSection().trim())) {
                    defined = true;
                    String section = verticalListExtension.getSection();
                    List<AnalysisItem> items = map.get(section);
                    if (items == null) {
                        items = new ArrayList<AnalysisItem>();
                        AnalysisMeasure placeHolder = new AnalysisMeasure(new NamedKey(section), AggregationTypes.SUM);
                        VerticalListReportExtension ext = new VerticalListReportExtension();
                        ext.setAlwaysShow(true);
                        placeHolder.setReportFieldExtension(ext);
                        items.add(placeHolder);
                        map.put(section, items);
                        if (firstPH) {
                            firstPH = false;
                        } else {
                            ext.setLineAbove(true);
                        }
                    }
                    items.add(field);
                }
            }
            if (!defined) {
                List<AnalysisItem> items = map.get(null);
                if (items == null) {
                    items = new ArrayList<AnalysisItem>();
                    map.put(null, items);
                }
                items.add(field);
            }
        }
        List<AnalysisItem> result = new ArrayList<AnalysisItem>();
        for (List<AnalysisItem> list : map.values()) {
            result.addAll(list);
        }
        setMeasures(result);
    }

    public int extensionType() {
        return ReportFieldExtension.VERTICAL_LIST;
    }

    protected boolean accepts(AnalysisItem analysisItem) {
        return analysisItem.hasType(AnalysisItemTypes.MEASURE);
    }
}
