package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.kpi.KPIOutcome;
import com.easyinsight.security.SecurityUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:44 AM
 */
public class WSTrendDefinition extends WSKPIDefinition {

    private long trendReportID;
    private int majorFontSize;
    private int minorFontSize;
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.TREND;
    }

    public int getMajorFontSize() {
        return majorFontSize;
    }

    public void setMajorFontSize(int majorFontSize) {
        this.majorFontSize = majorFontSize;
    }

    public int getMinorFontSize() {
        return minorFontSize;
    }

    public void setMinorFontSize(int minorFontSize) {
        this.minorFontSize = minorFontSize;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        majorFontSize = (int) findNumberProperty(properties, "majorFontSize", 32);
        minorFontSize = (int) findNumberProperty(properties, "minorFontSize", 16);
        direction = findStringProperty(properties, "direction", "horizontal");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("majorFontSize", majorFontSize));
        properties.add(new ReportNumericProperty("minorFontSize", minorFontSize));
        properties.add(new ReportStringProperty("direction", direction));
        return properties;
    }

    @Override
    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata) {
        StringBuilder sb = new StringBuilder();

        try {
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
            TrendDataResults results = DataService.getTrendDataResults(this, insightRequestMetadata, conn);
            for (TrendOutcome outcome : results.getTrendOutcomes()) {
                TextValueExtension e = (TextValueExtension) outcome.getNow().getValueExtension();
                String fontColor = ExportService.getFontColor(outcome);
                double v = ((outcome.getNow().toDouble() / outcome.getHistorical().toDouble()) - 1.0) * 100.0;
                Link l = outcome.getMeasure().defaultLink();
                Map<String, Object> j = null;
                if (l != null && l instanceof DrillThrough) {
                    j = new HashMap<String, Object>();
                    j.put("embedded", "false");
                    j.put("id", l.createID());
                    j.put("reportID", getUrlKey());
                    j.put("source", outcome.getMeasure().getAnalysisItemID());
                }

                //return old(fontColor, e, clickEvent, outcome, results, md, v);
                sb.append(improved(fontColor, e, j, outcome, results, md, v));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private String improved(String fontColor, TextValueExtension e, Map<String, Object> clickEvent, TrendOutcome outcome, TrendDataResults results, ExportMetadata md, double v) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='text-align:center;margin-left:30px;margin-right:30px;");
        sb.append("display:inline-block;");
        sb.append("color:");
        if (e != null) {
            sb.append(String.format("#%06X", (0xFFFFFF & e.getColor())));
        } else {
            sb.append(fontColor);
        }
        sb.append(";'>");
        sb.append("<div>");

        StringBuilder drillthroughBuilder = new StringBuilder();
        if(clickEvent != null) {
            drillthroughBuilder.append("data-source=\"");
            drillthroughBuilder.append(clickEvent.get("source"));
            drillthroughBuilder.append("\" data-reportid=\"");
            drillthroughBuilder.append(clickEvent.get("reportID"));
            drillthroughBuilder.append("\" data-drillthroughid=\"");
            drillthroughBuilder.append(clickEvent.get("id"));
            drillthroughBuilder.append("\" data-reportid=\"");
            drillthroughBuilder.append(clickEvent.get("reportID"));
            drillthroughBuilder.append("\"");
        }

        sb.append("<div style='font-size:");
        sb.append(getMajorFontSize());
        sb.append("px;display: inline-block;'> ");
        if (clickEvent != null) {
            sb.append("<a href='#' class='trendDrillthrough list_drillthrough' ");
            sb.append(drillthroughBuilder);
            sb.append(">");

        }
        sb.append(ExportService.createValue(0, outcome.getMeasure(), outcome.getNow(), md.cal, md.currencySymbol, md.locale, false));

        if (clickEvent != null) {
            sb.append("</a>");
        }

        sb.append("</div>");
        if (results.getPreviousString() != null && outcome.getHistorical() != null && outcome.getHistorical().type() != Value.EMPTY) {
            sb.append("<div style='margin-left:10px;font-size:");
            sb.append(getMinorFontSize());
            sb.append("px;display: inline-block;'>");
            FormattingConfiguration c = new FormattingConfiguration();
            c.setFormattingType(FormattingConfiguration.PERCENTAGE);
            if (clickEvent != null) {
                sb.append("<a href='#' class='trendDrillthrough list_drillthrough' ");
                sb.append(drillthroughBuilder);
                sb.append(">");
            }
            sb.append(FormattingConfiguration.createFormatter(c.getFormattingType()).format(v));
            if (clickEvent != null) {
                sb.append("</a>");
            }

            sb.append("</div>");
        }
        sb.append("</div>");
        sb.append("<div style=\"clear:both\"><div style='text-align:center;color:#333333;font-weight:bold;font-size:");

        sb.append(getMinorFontSize());
        sb.append("px;'>");
        if (clickEvent != null) {
            sb.append("<a href='#' class='trendDrillthrough list_drillthrough' ");
            sb.append(drillthroughBuilder);
            sb.append(">");
        }
        sb.append(outcome.getMeasure().toDisplay());
        if (clickEvent != null) {
            sb.append("</a>");
        }

        sb.append("</div>");
        sb.append("</div>");

        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        return super.toHTML(targetDiv, htmlReportMetadata);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "trend_definition");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }

    public Element kpiReportToPDFTable(EIConnection conn, InsightRequestMetadata insightRequestMetadata,
                                       ExportMetadata exportMetadata) throws SQLException, DocumentException {

        TrendDataResults trendDataResults = DataService.getTrendDataResults(this, insightRequestMetadata, conn);
        PdfPTable table = new PdfPTable(getMeasures().size());
        for (TrendOutcome outcome : trendDataResults.getTrendOutcomes()) {
            AnalysisMeasure analysisMeasure = outcome.getMeasure();
            PdfPTable cellTable = new PdfPTable(1);
            PdfPCell baseCell = new PdfPCell();
            baseCell.setBorder(0);

            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12);
            TextValueExtension e = (TextValueExtension) outcome.getNow().getValueExtension();
            BaseColor color;
            if (e != null) {
                color = new BaseColor(e.getColor());
            } else {
                switch (outcome.getOutcome()) {
                    case KPIOutcome.EXCEEDING_GOAL:
                    case KPIOutcome.POSITIVE:
                        color = new BaseColor(0x009900);
                        break;
                    case KPIOutcome.NEGATIVE:
                        color = new BaseColor(0x990000);
                        break;
                    default:
                        color = new BaseColor(0x0);
                }
            }
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 24, Font.NORMAL, color);
            PdfPCell valueCell = new PdfPCell(new Phrase(ExportService.createValue(exportMetadata, analysisMeasure, outcome.getNow(), false), headerFont));
            valueCell.setBorder(0);
            valueCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            if (outcome.getHistorical() != null && outcome.getHistorical().type() != Value.EMPTY) {
                double v = ((outcome.getNow().toDouble() / outcome.getHistorical().toDouble()) - 1.0) * 100.0;
                FormattingConfiguration c = new FormattingConfiguration();
                c.setFormattingType(FormattingConfiguration.PERCENTAGE);
                String trend = FormattingConfiguration.createFormatter(c.getFormattingType()).format(v);
                Font trendFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, color);
                PdfPCell trendCell = new PdfPCell(new Phrase(trend, trendFont));
                trendCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                trendCell.setBorder(0);
                PdfPTable topTable = new PdfPTable(2);
                topTable.addCell(valueCell);
                topTable.addCell(trendCell);
                PdfPCell ph = new PdfPCell(topTable);
                ph.setBorder(0);
                cellTable.addCell(ph);
            } else {
                cellTable.addCell(valueCell);
            }

            PdfPCell labelCell = new PdfPCell(new Phrase(analysisMeasure.toUnqualifiedDisplay(), labelFont));
            labelCell.setBorder(0);
            labelCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            cellTable.addCell(labelCell);
            baseCell.addElement(cellTable);
            table.addCell(baseCell);
        }
        return table;
    }
}
