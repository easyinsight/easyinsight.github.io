package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.TreeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:18 PM
 */
public class TreeRow {
    private Map<String, Object> values = new HashMap<String, Object>();
    private Value groupingColumn;
    private List<TreeRow> children = new ArrayList<TreeRow>();
    private boolean summaryColumn;
    private AnalysisItem groupingField;
    private String backgroundColor;
    private String textColor;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean isSummaryColumn() {
        return summaryColumn;
    }

    public void setSummaryColumn(boolean summaryColumn) {
        this.summaryColumn = summaryColumn;
    }

    public AnalysisItem getGroupingField() {
        return groupingField;
    }

    public void setGroupingField(AnalysisItem groupingField) {
        this.groupingField = groupingField;
    }

    public Value getGroupingColumn() {
        return groupingColumn;
    }

    public void setGroupingColumn(Value groupingColumn) {
        this.groupingColumn = groupingColumn;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public List<TreeRow> getChildren() {
        return children;
    }

    public void setChildren(List<TreeRow> children) {
        this.children = children;
    }

    public String toHTML(WSTreeDefinition treeDefinition, ExportMetadata exportMetadata) {
        StringBuilder sb = new StringBuilder();
        if (getChildren().size() == 0) {
            sb.append("<tr style=\"").append(TreeData.trStyle).append("\">");
            if (groupingField == null) {
                sb.append("<td style=\"background-color:#").append(backgroundColor).append("\"></td>");
            } else {
                sb.append("<td style=\"").append(TreeData.tdStyle).append("left\">").append(groupingColumn.toHTMLString()).append("</td>");
            }
            for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                StringBuilder styleString = new StringBuilder(TreeData.tdStyle);
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
                com.easyinsight.core.Value value =  (Value) values.get(analysisItem.qualifiedName());
                if (value == null) {
                    sb.append("<td></td>");
                } else {
                    if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                        TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                        if (textValueExtension.getColor() != 0) {
                            String hexString = "#" + Integer.toHexString(textValueExtension.getColor());
                            styleString.append(";color:").append(hexString);
                        }
                    }
                    if (backgroundColor != null) {
                        styleString.append(";background-color:#").append(backgroundColor);
                    }
                    sb.append("<td style=\"").append(styleString.toString()).append("\">");

                    sb.append(com.easyinsight.export.ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol, false));

                    sb.append("</td>");
                }
            }
            sb.append("</tr>");
        } else {
            sb.append("<tr style=\"").append(TreeData.trStyle).append("\">");
            String tableStyle = "color:#" + textColor + ";background-color:#" + backgroundColor + ";" + TreeData.tdStyle;
            sb.append("<td style=\"").append(tableStyle).append("left\" colspan=\"").append(treeDefinition.getItems().size() + 1).append("\">");
            sb.append(groupingColumn.toHTMLString());
            sb.append("</td>");
            sb.append("</tr>");
            for (TreeRow child : getChildren()) {
                sb.append(child.toHTML(treeDefinition, exportMetadata));
            }
        }

        return sb.toString();
    }
}
