package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.dataset.DataSet;
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

    private Map<String, IRow> masterIndex = new HashMap<String, IRow>();

    public TreeData(WSTreeDefinition treeDefinition, AnalysisHierarchyItem hierarchy, ExportMetadata exportMetadata, DataSet dataSet) {
        this.treeDefinition = treeDefinition;
        this.hierarchy = hierarchy;
        this.exportMetadata = exportMetadata;

        for (int i = 0; i < dataSet.getAdditionalSets().size(); i++) {
            DataSet childSet = dataSet.getAdditionalSets().get(i);
            for (IRow row : childSet.getRows()) {
                String key = "|";
                for (int j = 0; j < hierarchy.getHierarchyLevels().size() - 1 - i; j++) {
                    HierarchyLevel level = hierarchy.getHierarchyLevels().get(j);
                    Value value = row.getValue(level.getAnalysisItem());
                    key += value.toString() + "|";
                }
                masterIndex.put(key, row);
            }

        }
    }

    private int getComparison(AnalysisItem field, Value value1, Value value2) {
        int comparison = 0;
        int ascending = field.getSort() == 2 ? -1 : 1;


        if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
            comparison = value1.toDouble().compareTo(value2.toDouble()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
            DateValue date1 = (DateValue) value1;
            DateValue date2 = (DateValue) value2;
            comparison = date1.getDate().compareTo(date2.getDate()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.EMPTY) {
            comparison = ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.DATE) {
            comparison = -ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
            StringValue stringValue1 = (StringValue) value1;
            StringValue stringValue2 = (StringValue) value2;
            comparison = stringValue1.getValue().compareTo(stringValue2.getValue()) * ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.EMPTY) {
            comparison = -ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.STRING) {
            comparison = ascending;
        }
        return comparison;
    }

    public List<TreeRow> toTreeRows(PipelineData pipelineData) {
        List<TreeRow> rows = new ArrayList<TreeRow>();

        List<Argh> children = new ArrayList<Argh>(map.values());

        Collections.sort(children, new Comparator<Argh>() {

            public int compare(Argh argh, Argh argh1) {
                if (argh instanceof HigherLevel) {
                    HigherLevel higherLevel = (HigherLevel) argh;
                    HigherLevel higherLevel1 = (HigherLevel) argh1;
                    return getComparison(higherLevel.level, higherLevel.value, higherLevel1.value);
                } else if (argh instanceof LowerLevel) {
                    LowerLevel lowerLevel = (LowerLevel) argh;
                    LowerLevel lowerLevel1 = (LowerLevel) argh1;
                    return getComparison(lowerLevel.analysisItem, lowerLevel.row.getValue(lowerLevel.analysisItem), lowerLevel1.row.getValue(lowerLevel1.analysisItem));
                }
                return 0;
            }
        });

        for (Argh argh : children) {
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
            argh = new HigherLevel(analysisItem, hierarchy, 0, null);
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
        private IRow aggregateLevel;
        private HigherLevel parent;

        private HigherLevel(AnalysisItem level, AnalysisHierarchyItem hierarchy, int index, HigherLevel parent) {
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
            this.parent = parent;
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
                    argh = new HigherLevel(nextItem, hierarchy, index + 1, this);
                }
                map.put(value, argh);
            }
            argh.addRow(row);
            String key = createKey();
            aggregateLevel = masterIndex.get(key);
        }

        private String createKey() {
            String key;
            if (parent != null) {
                key = parent.createKey();
            } else {
                key = "|";
            }
            key += value.toString() + "|";
            return key;
        }

        public TreeRow toTreeRow(PipelineData pipelineData) {

            TreeRow treeRow = new TreeRow();
            treeRow.setBackgroundColor(backgroundColor);
            treeRow.setTextColor(textColor);
            treeRow.setGroupingField(level);
            treeRow.setGroupingColumn(this.value);

            for (Argh argh : map.values()) {
                TreeRow childRow = argh.toTreeRow(pipelineData);
                treeRow.getChildren().add(childRow);
            }

            if (treeDefinition instanceof WSSummaryDefinition) {
                TreeRow summaryRow = new TreeRow();

                for (AnalysisItem reportItem : treeDefinition.getItems()) {
                    if (reportItem.hasType(AnalysisItemTypes.MEASURE)) {
                        summaryRow.getValues().put(reportItem.qualifiedName(), aggregateLevel.getValue(reportItem));
                    }
                }

                summaryRow.setBackgroundColor("AAAAAA");
                summaryRow.setGroupingColumn(new StringValue(""));
                summaryRow.setSummaryColumn(true);
                /*for (Map.Entry<AnalysisItem, Aggregation> entry : sumMap.entrySet()) {
                    summaryRow.getValues().put(entry.getKey().qualifiedName(), entry.getValue().getValue());
                }*/
                treeRow.getChildren().add(summaryRow);
            } else {
                for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        treeRow.getValues().put(analysisItem.qualifiedName(), aggregateLevel.getValue(analysisItem.createAggregateKey()));
                    }
                }
            }

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
