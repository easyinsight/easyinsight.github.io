package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSMultiSummaryDefinition;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.PipelineData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: 7/6/12
 * Time: 9:50 AM
 */
public class MultiSummaryData {

    public static final String headerLabelStyle = "text-align:center;padding-top:15px;padding-bottom:15px;font-size:14px";
    public static final String tableStyle = "font-size:12px;border-collapse:collapse;border-style:solid;border-width:1px;border-spacing:0;border-color:#000000;width:100%";
    public static final String thStyle = "border-style:solid;padding:6px;border-width:1px;border-color:#000000";
    public static final String headerTRStyle = "background-color:#EEEEEE";
    public static final String trStyle = "padding:0px;margin:0px";
    public static final String summaryTRStyle = "padding:0px;margin:0px;background-color:#F4F4F4";
    public static final String tdStyle = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";
    public static final String tdStyle1 = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";
    public static final String tdStyle2 = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";

    private WSMultiSummaryDefinition report;
    private ExportMetadata exportMetadata;
    private DataSet dataSet;
    private Map<InsightDescriptor, WSListDefinition> reportMap = new HashMap<>();

    private Map<InsightDescriptor, DataSet> childSets = new HashMap<>();

    private List<MultiSummaryRow> rows;

    private List<HigherLevel> higherLevels;

    public MultiSummaryData(WSMultiSummaryDefinition report, ExportMetadata exportMetadata, DataSet dataSet, Map<InsightDescriptor, DataSet> childSets,
                            Map<InsightDescriptor, WSListDefinition> reportMap) {
        this.report = report;
        this.exportMetadata = exportMetadata;
        this.dataSet = dataSet;
        this.childSets = childSets;
        this.reportMap = reportMap;

        List<MultiSummaryRow> rows = new ArrayList<>();

        List<HigherLevel> higherLevels = new ArrayList<>();

        int rowID = 0;
        for (IRow row : dataSet.getRows()) {
            // create the high level tree node
            HigherLevel higherLevel = new HigherLevel(row, rowID++);
            higherLevels.add(higherLevel);
            Value keyValue = row.getValue(report.getKey());
            for (Map.Entry<InsightDescriptor, DataSet> entry : childSets.entrySet()) {
                DataSet childSet = entry.getValue();
                WSListDefinition childReport = reportMap.get(entry.getKey());
                AnalysisItem childKeyItem = null;
                for (AnalysisItem item : childReport.getColumns()) {
                    if (item.toDisplay().equals(report.getKey().toDisplay())) {
                        childKeyItem = item;
                    }
                }
                for (IRow childRow : childSet.getRows()) {
                    Value childKeyValue = childRow.getValue(childKeyItem);
                    if (childKeyValue.equals(keyValue)) {
                        higherLevel.addChildRow(entry.getKey(), childRow);
                    }
                }
            }
        }
        this.higherLevels = higherLevels;
        this.rows = rows;
    }

    public Element toPDF(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws DocumentException, SQLException {
        PdfPTable table = new PdfPTable(report.getCoreItems().size());
        table.setSplitLate(false);
        table.setSplitRows(true);
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(3);
        int fontSize = report.getFontSize();
        BaseColor color = DashboardPDF.fromColor(report.getHeaderColor1());
        BaseColor headerTextColor = DashboardPDF.fromColor(report.getHeaderTextColor());
        if (fontSize < 8) {
            fontSize = 6;
        } else {
            fontSize -= 2;
        }
        int minimumSize = fontSize + 5;
        int totalWidth = 0;
        for (AnalysisItem analysisItem : report.getCoreItems()) {
            int widthToUse = analysisItem.getWidth();
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                TextReportFieldExtension ext = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                if (ext.getFixedWidth() > 0) {
                    widthToUse = ext.getFixedWidth();
                }
            }
            totalWidth += widthToUse;
        }

        if (totalWidth > 0) {
            float[] widths = new float[report.getCoreItems().size()];
            int wCtr = 0;
            for (AnalysisItem analysisItem : report.getCoreItems()) {
                int widthToUse = analysisItem.getWidth();
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                    TextReportFieldExtension ext = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                    if (ext.getFixedWidth() > 0) {
                        widthToUse = ext.getFixedWidth();
                    }
                }
                float percent = (float) widthToUse / (float) totalWidth;
                if (percent < .01f) {
                    percent = .1f;
                }
                widths[wCtr++] = percent;
            }
            table.setWidths(widths);
        }

        for (AnalysisItem analysisItem : report.getCoreItems()) {
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, fontSize, com.itextpdf.text.Font.BOLD,
                    new BaseColor(headerTextColor.getRed(), headerTextColor.getGreen(), headerTextColor.getBlue()));
            PdfPCell cell = new PdfPCell(new Phrase(analysisItem.toUnqualifiedDisplay(), boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setMinimumHeight(minimumSize);
            cell.setBackgroundColor(new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
            table.addCell(cell);
        }
        table.setHeaderRows(1);

        for (HigherLevel higherLevel : higherLevels) {
            higherLevel.toElement(insightRequestMetadata, conn, table);
        }

        return table;
    }

    public String toHTMLForEmail(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<thead>");
        sb.append("<tr style=\"font-size:").append(report.getFontSize()).append("px;background-color:").append(ExportService.createHexString(report.getHeaderColor1())).append("\">");
        sb.append("<th style=\"text-align:left;width:100px\"></th>");
        for (AnalysisItem item : report.getCoreItems()) {
            sb.append("<th style=\"text-align:center;color:").append(ExportService.createHexString(report.getHeaderTextColor())).append("\">").append(item.toUnqualifiedDisplay()).append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        for (HigherLevel higherLevel : higherLevels) {
            sb.append(higherLevel.toHTML(insightRequestMetadata, conn));
        }
        sb.append("</table>");
        return sb.toString();
    }

    public String toHTML(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table table-condensed table-bordered\">");
        sb.append("<thead>");
        sb.append("<tr style=\"background-color:").append(ExportService.createHexString(report.getHeaderColor1())).append("\">");
        sb.append("<th style=\"text-align:left;width:100px\"></th>");
        for (AnalysisItem item : report.getCoreItems()) {
            sb.append("<th style=\"text-align:center;color:").append(ExportService.createHexString(report.getHeaderTextColor())).append("\">").append(item.toUnqualifiedDisplay()).append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        for (HigherLevel higherLevel : higherLevels) {
            sb.append(higherLevel.toHTML(insightRequestMetadata, conn));
        }
        sb.append("</table>");
        return sb.toString();
    }

    public List<MultiSummaryRow> toTreeRows(PipelineData pipelineData) {
        return rows;
    }


    public static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int) (r));
        String gs = Integer.toHexString((int) (g));
        String bs = Integer.toHexString((int) (b));
        return rs + gs + bs;
    }

    private class HigherLevel extends AbstractTreeRow {
        private Map<InsightDescriptor, List<IRow>> reportToChildRows = new LinkedHashMap<>();
        private int backgroundColor;
        private int textColor;

        private IRow row;
        private int rowID;


        private HigherLevel(IRow row, int rowID) {
            this.row = row;
            this.rowID = rowID;
            backgroundColor = report.getSummaryBackgroundColor();
            textColor = report.getSummaryTextColor();
        }

        public MultiSummaryRow toTreeRow(PipelineData pipelineData) {

            MultiSummaryRow treeRow = new MultiSummaryRow();
            treeRow.setBackgroundColor(backgroundColor);
            treeRow.setTextColor(textColor);

            return treeRow;
        }

        @Override
        public String toHTMLForEmail(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
            StringBuilder sb = new StringBuilder();
            sb.append("<tr>");

            for (AnalysisItem analysisItem : report.getCoreItems()) {
                StringBuilder styleString = new StringBuilder("text-align:");
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
                Value value = row.getValue(analysisItem);
                if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                    TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                    if (textValueExtension.getColor() != 0) {
                        String hexString = "#" + Integer.toHexString(textValueExtension.getColor());
                        styleString.append(";color:").append(hexString);
                    }
                }
                sb.append("<td style=\"").append(styleString.toString()).append("\">");

                sb.append(ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol,
                        exportMetadata.locale, false));

                sb.append("</td>");
            }
            sb.append("</tr>");


            for (Map.Entry<InsightDescriptor, List<IRow>> entry : map.entrySet()) {
                if (report.isNestedReportTitles()) {
                    sb.append("<tr><td style=\"text-align:center;font-size:10px;background-color:#555555;color:#FFFFFF\" colspan=\"").append(report.getCoreItems().size()).append("\">").append(entry.getKey().getName()).append("</td></tr>");
                }
                sb.append("<tr style=\"min-height:0\"><td style=\"min-height:0; background-color:#DDDDDD\" colspan=\"").append(report.getCoreItems().size() + 1).append("\">");
                WSListDefinition childReport = reportMap.get(entry.getKey());
                LowerLevel lowerLevel = new LowerLevel(entry.getValue(), childReport);
                sb.append(lowerLevel.toHTMLForEmail(insightRequestMetadata, conn));
                sb.append("</td></tr>");
            }

            return sb.toString();
        }

        public void toElement(InsightRequestMetadata insightRequestMetadata, EIConnection conn, Element element) throws SQLException, DocumentException {
            PdfPTable table = (PdfPTable) element;
            for (AnalysisItem analysisItem : report.getCoreItems()) {
                Value value = row.getValue(analysisItem);
                table.addCell(new PdfPCell(new Phrase(ExportService.createValue(exportMetadata, analysisItem, value, true))));
            }
            for (Map.Entry<InsightDescriptor, List<IRow>> entry : map.entrySet()) {
                PdfPCell spanCell = new PdfPCell();
                spanCell.setColspan(report.getCoreItems().size());
                WSListDefinition childReport = reportMap.get(entry.getKey());
                LowerLevel lowerLevel = new LowerLevel(entry.getValue(), childReport);
                lowerLevel.toElement(insightRequestMetadata, conn, spanCell);
                table.addCell(spanCell);
            }
        }

        public String toHTML(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {

            StringBuilder sb = new StringBuilder();
            sb.append("<tr>");
            if (map.size() > 0) {
                sb.append("<td><button type=\"button\" style=\"padding: 2px 4px;font-size:10px\" class=\"btn btn-info\" data-toggle=\"collapse\" data-target=\"#collapse" + rowID + "\">Details</button>");
            } else {
                sb.append("<td></td>");
            }

            for (AnalysisItem analysisItem : report.getCoreItems()) {
                StringBuilder styleString = new StringBuilder("text-align:");
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
                Value value = row.getValue(analysisItem);
                if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                    TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                    if (textValueExtension.getColor() != 0) {
                        String hexString = "#" + Integer.toHexString(textValueExtension.getColor());
                        styleString.append(";color:").append(hexString);
                    }
                }
                sb.append("<td style=\"").append(styleString.toString()).append("\">");

                sb.append(ExportService.createValue(exportMetadata.dateFormat, analysisItem, value, exportMetadata.cal, exportMetadata.currencySymbol,
                        exportMetadata.locale, false));

                sb.append("</td>");
            }
            sb.append("</tr>");


            for (Map.Entry<InsightDescriptor, List<IRow>> entry : map.entrySet()) {
                if (report.isNestedReportTitles()) {
                    sb.append("<tr><td style=\"text-align:center;font-size:10px;background-color:#555555;color:#FFFFFF\" colspan=\"").append(report.getCoreItems().size()).append("\">").append(entry.getKey().getName()).append("</td></tr>");
                }
                sb.append("<tr style=\"min-height:0\"><td style=\"min-height:0; background-color:#DDDDDD\" colspan=\"").append(report.getCoreItems().size() + 1).append("\">");

                sb.append("<div id=\"collapse" + rowID + "\" class=\"panel-collapse collapse\">");
                WSListDefinition childReport = reportMap.get(entry.getKey());
                LowerLevel lowerLevel = new LowerLevel(entry.getValue(), childReport);
                sb.append(lowerLevel.toHTML(insightRequestMetadata, conn));
                sb.append("</div>");
                sb.append("</td></tr>");
            }

            return sb.toString();
        }

        private Map<InsightDescriptor, List<IRow>> map = new LinkedHashMap<>();

        public void addChildRow(InsightDescriptor key, IRow childRow) {
            List<IRow> rows = map.get(key);
            if (rows == null) {
                rows = new ArrayList<>();
                map.put(key, rows);
            }
            rows.add(childRow);
        }
    }

    private class LowerLevel extends AbstractTreeRow {
        private List<IRow> rows;
        private WSListDefinition childDefinition;

        private LowerLevel(List<IRow> rows, WSListDefinition childDefinition) {
            this.rows = rows;
            this.childDefinition = childDefinition;
        }

        public void toElement(InsightRequestMetadata insightRequestMetadata, EIConnection conn, Element parent) throws SQLException, DocumentException {
            DataSet pseudoSet = new DataSet();
            for (IRow row : rows) {
                pseudoSet.addRow(row);
            }
            ListDataResults results = (ListDataResults) pseudoSet.toListDataResults(childDefinition.getColumns(), new HashMap<>(), childDefinition);
            PdfPCell spanCell = (PdfPCell) parent;
            Element element = new ExportService().listReportToPDFTable(childDefinition, conn, insightRequestMetadata, exportMetadata, results);
            spanCell.addElement(element);
        }

        public String toHTML(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
            DataSet pseudoSet = new DataSet();
            for (IRow row : rows) {
                pseudoSet.addRow(row);
            }
            List<AnalysisItem> items = new ArrayList<>();
            for (AnalysisItem item : childDefinition.getColumns()) {
                if (!item.toDisplay().equals(report.getKey().toDisplay())) {
                    items.add(item);
                }
            }
            ListDataResults results = (ListDataResults) pseudoSet.toListDataResults(items, new HashMap<>(), childDefinition);
            childDefinition.setFontSize(10);
            ExportProperties exportProperties = new ExportProperties();
            exportProperties.setIncludeHeaders(report.isNestedReportHeaders());
            return ExportService.listReportToHTMLTableWithActualCSS(childDefinition, results, conn, insightRequestMetadata, false, exportProperties);
        }

        @Override
        public String toHTMLForEmail(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
            DataSet pseudoSet = new DataSet();
            for (IRow row : rows) {
                pseudoSet.addRow(row);
            }
            List<AnalysisItem> items = new ArrayList<>();
            for (AnalysisItem item : childDefinition.getColumns()) {
                if (!item.toDisplay().equals(report.getKey().toDisplay())) {
                    items.add(item);
                }
            }
            ListDataResults results = (ListDataResults) pseudoSet.toListDataResults(items, new HashMap<>(), childDefinition);
            childDefinition.setFontSize(report.getNestedFontSize());
            ExportProperties exportProperties = new ExportProperties();
            exportProperties.setIncludeHeaders(report.isNestedReportHeaders());
            return ExportService.listReportToHTMLTable(childDefinition, results, conn, insightRequestMetadata, false, exportProperties);
        }

        @Override
        public MultiSummaryRow toTreeRow(PipelineData pipelineData) {
            MultiSummaryRow treeRow = new MultiSummaryRow();
            treeRow.setDepth(1);
            treeRow.setBackgroundColor(0xFFFFFF);
            treeRow.setTextColor(0x0);
            for (AnalysisItem analysisItem : childDefinition.getColumns()) {
                boolean remove = false;
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                    TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                    remove = textReportFieldExtension.isForceToSummary();
                }
                if (remove) {
                    continue;
                }
                //treeRow.getValues().put(analysisItem.qualifiedName(), row.getValue(analysisItem));
            }
            return treeRow;
        }
    }

    private abstract class AbstractTreeRow {

        public abstract void toElement(InsightRequestMetadata insightRequestMetadata, EIConnection conn, Element parent) throws SQLException, DocumentException;

        public abstract String toHTML(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException;

        public abstract String toHTMLForEmail(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException;

        public abstract MultiSummaryRow toTreeRow(PipelineData pipelineData);
    }
}
