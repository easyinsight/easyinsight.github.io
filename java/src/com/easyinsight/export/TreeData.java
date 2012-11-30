package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CalculationComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.PipelineData;

import java.util.*;

/**
 * User: jamesboe
 * Date: 7/6/12
 * Time: 9:50 AM
 */
public class TreeData {

    public static final String headerLabelStyle = "text-align:center;padding-top:15px;padding-bottom:15px;font-size:14px";
    public static final String tableStyle = "font-size:12px;border-collapse:collapse;border-style:solid;border-width:1px;border-spacing:0;border-color:#000000;width:100%";
    public static final String thStyle = "border-style:solid;padding:6px;border-width:1px;border-color:#000000";
    public static final String headerTRStyle = "background-color:#EEEEEE";
    public static final String trStyle = "padding:0px;margin:0px";
    public static final String summaryTRStyle = "padding:0px;margin:0px;background-color:#F4F4F4";
    public static final String tdStyle = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";
    public static final String tdStyle1 = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";
    public static final String tdStyle2 = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";

    private WSTreeDefinition treeDefinition;
    private AnalysisHierarchyItem hierarchy;
    private ExportMetadata exportMetadata;

    public TreeData(WSTreeDefinition treeDefinition, AnalysisHierarchyItem hierarchy, ExportMetadata exportMetadata) {
        this.treeDefinition = treeDefinition;
        this.hierarchy = hierarchy;
        this.exportMetadata = exportMetadata;
    }

    public List<TreeRow> toTreeRows(PipelineData pipelineData) {
        List<TreeRow> rows = new ArrayList<TreeRow>();

        for (Argh argh : map.values()) {
            TreeRow treeRow = argh.toTreeRow(pipelineData);
            rows.add(treeRow);

        }

        return rows;
    }

    private Map<Value, Argh> map = new LinkedHashMap<Value, Argh>();

    public void addRow(IRow row) {
        AnalysisItem analysisItem = hierarchy.getHierarchyLevels().get(0).getAnalysisItem();
        Value value = row.getValue(analysisItem);
        Argh argh = map.get(value);
        if (argh == null) {
            argh = new HigherLevel(analysisItem, hierarchy, 0);
            map.put(value, argh);
        }
        argh.addRow(row);
    }

    public static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r));
        String gs = Integer.toHexString((int)(g));
        String bs = Integer.toHexString((int)(b));
        return rs + gs + bs;
    }

    private class HigherLevel extends Argh {
        private Map<Value, Argh> map = new LinkedHashMap<Value, Argh>();
        private AnalysisItem level;
        private AnalysisHierarchyItem hierarchy;
        private int index;
        private Value value;
        private String backgroundColor;
        private String textColor;

        private HigherLevel(AnalysisItem level, AnalysisHierarchyItem hierarchy, int index) {
            this.level = level;
            this.hierarchy = hierarchy;
            this.index = index;
            int value = 256 / (hierarchy.getHierarchyLevels().size() - index);
            backgroundColor = rgbToString(value, value, value);
            if (value < 148) {
                textColor = "#FFFFFF";
            } else {
                textColor = "#000000";
            }
        }

        public void addRow(IRow row) {
            this.value = row.getValue(level);
            AnalysisItem nextItem = hierarchy.getHierarchyLevels().get(index + 1).getAnalysisItem();
            Value value = row.getValue(nextItem);
            Argh argh = map.get(value);
            if (argh == null) {
                if ((index + 1) == (hierarchy.getHierarchyLevels().size() - 1)) {
                    argh = new LowerLevel(nextItem);
                } else {
                    argh = new HigherLevel(nextItem, hierarchy, index + 1);
                }
                map.put(value, argh);
            }
            argh.addRow(row);
        }

        public TreeRow toTreeRow(PipelineData pipelineData) {
            Map<AnalysisItem, Aggregation> sumMap = new HashMap<AnalysisItem, Aggregation>();

            Set<AnalysisItem> reportItems = new HashSet<AnalysisItem>(treeDefinition.getItems());
            List<IComponent> components = new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, null, new AnalysisItemRetrievalStructure(null));

            Iterator<IComponent> iter = components.iterator();
            while (iter.hasNext()) {
                IComponent component = iter.next();
                if (!(component instanceof CalculationComponent)) {
                    iter.remove();
                    continue;
                }
                CalculationComponent calculationComponent = (CalculationComponent) component;
                if (!calculationComponent.getAnalysisCalculation().isRecalculateSummary()) {
                    iter.remove();
                }
            }

            for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                    boolean recalculate = false;
                    if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                        AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                        if (analysisCalculation.isRecalculateSummary()) {
                            recalculate = true;
                        }
                    }
                    if (!recalculate) {
                        sumMap.put(analysisMeasure, new AggregationFactory(analysisMeasure, false).getAggregation());
                    }
                }
            }
            TreeRow treeRow = new TreeRow();
            treeRow.setBackgroundColor(backgroundColor);
            treeRow.setTextColor(textColor);
            treeRow.setGroupingField(level);
            treeRow.setGroupingColumn(this.value);

            DataSet tempSet = new DataSet();
            IRow tempRow = tempSet.createRow();

            for (Argh argh : map.values()) {
                TreeRow childRow = argh.toTreeRow(pipelineData);
                treeRow.getChildren().add(childRow);
                for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        boolean recalculate = false;
                        if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                            AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                            if (analysisCalculation.isRecalculateSummary()) {
                                recalculate = true;
                            }
                        }
                        if (!recalculate) {
                            Value value = (Value) childRow.getValues().get(analysisItem.qualifiedName());
                            if (value != null) {
                                Aggregation aggregation = sumMap.get(analysisItem);
                                aggregation.addValue(value);
                            }
                        }
                    }
                }
            }
            for (Map.Entry<AnalysisItem, Aggregation> entry : sumMap.entrySet()) {
                tempRow.addValue(entry.getKey().createAggregateKey(), entry.getValue().getValue());
            }
            for (IComponent component : components) {
                component.apply(tempSet, pipelineData);
            }

            TreeRow summaryRow = new TreeRow();

            for (AnalysisItem reportItem : reportItems) {
                if (reportItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    summaryRow.getValues().put(reportItem.qualifiedName(), tempSet.getRow(0).getValue(reportItem));
                }
            }

            summaryRow.setBackgroundColor("AAAAAA");
            summaryRow.setGroupingColumn(new StringValue(""));
            summaryRow.setSummaryColumn(true);
            for (Map.Entry<AnalysisItem, Aggregation> entry : sumMap.entrySet()) {
                summaryRow.getValues().put(entry.getKey().qualifiedName(), entry.getValue().getValue());
            }
            treeRow.getChildren().add(summaryRow);
            /*for (Map.Entry<AnalysisItem, Aggregation> entry : sumMap.entrySet()) {
                treeRow.getValues().put(entry.getKey().qualifiedName(), entry.getValue().getValue());
            }*/

            return treeRow;
        }

        public String toHTML() {
            /*List<Value> values = new ArrayList<Value>(map.keySet());
            Collections.sort(values, new Comparator<Value>() {

                public int compare(Value value, Value value1) {
                    return value.toString().compareTo(value1.toString());
                }
            });*/
            StringBuilder sb = new StringBuilder();
            sb.append("<tr style=\"").append(trStyle).append("\">");
            String tableStyle = "color:" + textColor + ";background-color:#" + backgroundColor + ";" + tdStyle;
            sb.append("<td style=\"").append(tableStyle).append("left\" colspan=\"").append(treeDefinition.getItems().size() + 1).append("\">");
            sb.append(this.value.toString());
            sb.append("</td>");
            sb.append("</tr>");
            for (Argh argh : map.values()) {
                sb.append(argh.toHTML());
            }

            return sb.toString();
        }
    }

    private class LowerLevel extends Argh {
        private IRow row;
        private AnalysisItem analysisItem;

        private LowerLevel(AnalysisItem analysisItem) {
            this.analysisItem = analysisItem;
        }

        @Override
        public void addRow(IRow row) {
            this.row = row;
        }

        public String toHTML() {
            StringBuilder sb = new StringBuilder();
            sb.append("<tr style=\"").append(trStyle).append("\">");
            sb.append("<td style=\"").append(tdStyle).append("left\">").append(row.getValue(analysisItem)).append("</td>");
            for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                StringBuilder styleString = new StringBuilder(tdStyle);
                String align = "left";
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                    TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                    if (textReportFieldExtension.getAlign() != null) {
                        if ("Left".equals(textReportFieldExtension.getAlign())) {
                            align = "left";
                        } else if ("Center".equals(textReportFieldExtension.getAlign())) {
                            align = "center";
                        } else if ("Right".equals(textReportFieldExtension.getAlign())) {
                            align = "right";
                        }
                    }
                    styleString.append(align);
                    if (textReportFieldExtension.getFixedWidth() > 0) {
                        styleString.append(";width:").append(textReportFieldExtension.getFixedWidth()).append("px");
                    }
                } else {
                    styleString.append(align);
                }
                com.easyinsight.core.Value value = row.getValue(analysisItem);
                if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                    TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                    if (textValueExtension.getColor() != 0) {
                        String hexString = "#" + Integer.toHexString(textValueExtension.getColor());
                        styleString.append(";color:").append(hexString);
                    }
                }
                sb.append("<td style=\"").append(styleString.toString()).append("\">");

                sb.append(com.easyinsight.export.ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol, false));

                sb.append("</td>");
            }
            sb.append("</tr>");
            return sb.toString();
        }

        @Override
        public TreeRow toTreeRow(PipelineData pipelineData) {
            TreeRow treeRow = new TreeRow();
            treeRow.setGroupingField(analysisItem);
            for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                treeRow.getValues().put(analysisItem.qualifiedName(), row.getValue(analysisItem));
            }
            treeRow.setGroupingColumn(row.getValue(analysisItem));
            return treeRow;
        }
    }

    private abstract class Argh {
        public void addRow(IRow row) {

        }

        public abstract String toHTML();

        public abstract TreeRow toTreeRow(PipelineData pipelineData);
    }
}
