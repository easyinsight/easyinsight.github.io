package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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
                String clickEvent = "";
                Map<String, Object> j = null;
                if (l != null && l instanceof DrillThrough) {
                    j = new HashMap<String, Object>();
                    j.put("embedded", "false");
                    j.put("id", l.createID());
                    j.put("reportID", getUrlKey());
                    j.put("source", outcome.getMeasure().getAnalysisItemID());
                }

                //return old(fontColor, e, clickEvent, outcome, results, md, v);
                return improved(fontColor, e, j, outcome, results, md, v);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private String old(String fontColor, TextValueExtension e, String clickEvent, TrendOutcome outcome, TrendDataResults results, ExportMetadata md, double v) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='width: 245px;");
        if ("horizontal".equals(getDirection()))
            sb.append("display:inline-block;");
        sb.append("color:");
        if (e != null) {
            sb.append(String.format("#%06X", (0xFFFFFF & e.getColor())));
        } else {
            sb.append(fontColor);
        }
        sb.append(";'>");
        sb.append("<div style='padding: 0px;'>");
        if (results.getPreviousString() != null) {
            sb.append("<div style='float: right; width: 80px;font-size:");
            sb.append(getMinorFontSize());
            sb.append("px;'>");
            FormattingConfiguration c = new FormattingConfiguration();
            c.setFormattingType(FormattingConfiguration.PERCENTAGE);
            if (!clickEvent.isEmpty()) {
                sb.append("<a href='#' class='trendDrillthrough' onclick='");
                sb.append(clickEvent);
                sb.append("'>");
            }
            sb.append(FormattingConfiguration.createFormatter(c.getFormattingType()).format(v));
            if (!clickEvent.isEmpty()) {
                sb.append("</a>");
            }

            sb.append("</div>");
        }
        sb.append("<div style='font-size:");
        sb.append(getMajorFontSize());
        sb.append("px;text-align:center'> ");
        if (!clickEvent.isEmpty()) {
            sb.append("<a href='#' class='trendDrillthrough' onclick='");
            sb.append(clickEvent);
            sb.append("'>");
        }
        sb.append(ExportService.createValue(0, outcome.getMeasure(), outcome.getNow(), md.cal, md.currencySymbol, md.locale, false));

        if (!clickEvent.isEmpty()) {
            sb.append("</a>");
        }

        sb.append("</div></div>");
        sb.append("<div style='text-align:center;color:#000000;font-size:");

        sb.append(getMinorFontSize());
        sb.append("px;'>");
        if (!clickEvent.isEmpty()) {
            sb.append("<a href='#' class='trendDrillthrough' onclick='");
            sb.append(clickEvent);
            sb.append("'>");
        }
        sb.append(outcome.getMeasure().toDisplay());
        if (!clickEvent.isEmpty()) {
            sb.append("</a>");
        }

        sb.append("</div>");

        sb.append("</div>");
        return sb.toString();
    }

    private String improved(String fontColor, TextValueExtension e, Map<String, Object> clickEvent, TrendOutcome outcome, TrendDataResults results, ExportMetadata md, double v) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='text-align:center;");
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
        if (results.getPreviousString() != null) {
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
}
