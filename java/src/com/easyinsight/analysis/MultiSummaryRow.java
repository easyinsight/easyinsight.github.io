package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSMultiSummaryDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.export.TreeData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:18 PM
 */
public class MultiSummaryRow {
    private Map<String, Object> values = new HashMap<String, Object>();
    private Value groupingColumn;
    private Value sortColumn;
    private List<MultiSummaryRow> children = new ArrayList<MultiSummaryRow>();
    private boolean summaryColumn;
    private AnalysisItem groupingField;
    private Integer backgroundColor;
    private Integer textColor;
    private int depth;

    public Value sortValue() {
        if (sortColumn != null) {
            return sortColumn;
        }
        return groupingColumn;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Value getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Value sortColumn) {
        this.sortColumn = sortColumn;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
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

    public List<MultiSummaryRow> getChildren() {
        return children;
    }

    public void setChildren(List<MultiSummaryRow> children) {
        this.children = children;
    }

    public void toPDF(WSTreeDefinition treeDefinition, ExportMetadata exportMetadata, PdfPTable table) {
        if (getChildren().size() == 0) {
            if (groupingField == null) {
                PdfPCell valueCell = new PdfPCell(new Phrase(""));
                valueCell.setBackgroundColor(new BaseColor(backgroundColor));
                valueCell.setMinimumHeight(20f);
                table.addCell(valueCell);
            } else {
                Font font = new Font(Font.FontFamily.HELVETICA, treeDefinition.getFontSize());
                font.setColor(new BaseColor(textColor));
                Phrase phrase = new Phrase(ExportService.createValue(exportMetadata.dateFormat, groupingField, groupingColumn, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false), font);
                PdfPCell valueCell = new PdfPCell(phrase);
                valueCell.setMinimumHeight(20f);
                table.addCell(valueCell);
            }
            for (AnalysisItem analysisItem : treeDefinition.getItems()) {
                Value value =  (Value) values.get(analysisItem.qualifiedName());
                if (value == null) {
                    Font font = new Font(Font.FontFamily.HELVETICA, treeDefinition.getFontSize());
                    font.setColor(new BaseColor(textColor));
                    Phrase phrase = new Phrase("", font);
                    PdfPCell valueCell = new PdfPCell(phrase);
                    if (backgroundColor != null) {
                        valueCell.setBackgroundColor(new BaseColor(backgroundColor));
                    }
                    valueCell.setMinimumHeight(20f);
                    table.addCell(valueCell);
                } else {
                    Font font = new Font(Font.FontFamily.HELVETICA, treeDefinition.getFontSize());
                    font.setColor(new BaseColor(textColor));
                    Phrase phrase = new Phrase(ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, true), font);
                    PdfPCell valueCell = new PdfPCell(phrase);
                    if (backgroundColor != null) {
                        valueCell.setBackgroundColor(new BaseColor(backgroundColor));
                    }
                    valueCell.setMinimumHeight(20f);
                    table.addCell(valueCell);
                }
            }
        } else {
            Font font = new Font(Font.FontFamily.HELVETICA, treeDefinition.getFontSize());
            font.setColor(new BaseColor(textColor));
            Phrase phrase = new Phrase(ExportService.createValue(exportMetadata.dateFormat, groupingField, groupingColumn, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false), font);
            PdfPCell valueCell = new PdfPCell(phrase);
            //valueCell.setColspan(treeDefinition.getItems().size() + 1);
            valueCell.setBackgroundColor(new BaseColor(backgroundColor));
            valueCell.setMinimumHeight(20f);
            table.addCell(valueCell);

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
                Value value =  (Value) values.get(analysisItem.qualifiedName());
                if (value == null) {
                    valueCell = new PdfPCell(new Phrase(""));
                    valueCell.setBackgroundColor(new BaseColor(backgroundColor));
                    valueCell.setMinimumHeight(20f);
                    table.addCell(valueCell);
                } else {
                    /*if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                        TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                        if (textValueExtension.getColor() != 0) {
                            String hexString = ExportService.createHexString(textValueExtension.getColor());
                            styleString.append(";color:").append(hexString);
                        }
                    }*/
                    /*if (backgroundColor != null) {
                        styleString.append(";background-color:").append(ExportService.createHexString(backgroundColor));
                    }
                    if (textColor != null) {
                        styleString.append(";color:").append(ExportService.createHexString(textColor));
                    }*/
                    font = new Font(Font.FontFamily.HELVETICA, treeDefinition.getFontSize());
                    font.setColor(new BaseColor(textColor));
                    valueCell = new PdfPCell(new Phrase(ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false), font));
                    valueCell.setBackgroundColor(new BaseColor(backgroundColor));
                    valueCell.setMinimumHeight(20f);
                    table.addCell(valueCell);
                }
            }

            for (MultiSummaryRow child : getChildren()) {
                child.toPDF(treeDefinition, exportMetadata, table);
            }
        }
    }


}