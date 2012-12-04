package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
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
                if (l != null && l instanceof DrillThrough) {
                    JSONObject j = new JSONObject();
                    j.put("embedded", "false");
                    j.put("id", l.getLinkID());
                    j.put("reportID", getUrlKey());
                    j.put("source", outcome.getMeasure().getAnalysisItemID());
                    clickEvent = "drillThroughParameterized(" + j.toString() + ")";
                }
                sb.append("<div style='width: 245px;height:100px;");
                if ("horizontal".equals(getDirection()))
                    sb.append("display:inline-block;");
                sb.append("color:");
                if (e != null) {
                    sb.append(String.format("#%06X", (0xFFFFFF & e.getColor())));
                } else {
                    sb.append(fontColor);
                }
                sb.append(";'>");
                sb.append("<div style='padding: 16px;'>");
                TrendReportFieldExtension extension = ((TrendReportFieldExtension) outcome.getMeasure().getReportFieldExtension());


                if (extension.getDate() != null) {
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
                    sb.append(c.createFormatter().format(v));
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
                sb.append(ExportService.createValue(0, outcome.getMeasure(), outcome.getNow(), md.cal, md.currencySymbol, false));

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
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        return super.toHTML(targetDiv, htmlReportMetadata);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
