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

    @Override
    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata) {
        StringBuilder sb = new StringBuilder();

        try {
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
            TrendDataResults results = DataService.getTrendDataResults(this, insightRequestMetadata, conn);
            for (TrendOutcome outcome : results.getTrendOutcomes()) {
                String fontColor = ExportService.getFontColor(outcome);
                double v = ((outcome.getNow().toDouble() / outcome.getHistorical().toDouble()) - 1.0) * 100.0;
                Link l = outcome.getMeasure().defaultLink();
                String clickEvent = "";
                if(l != null && l instanceof DrillThrough) {
                    JSONObject j = new JSONObject();
                    j.put("embedded", "false");
                    j.put("id", l.getLinkID());
                    j.put("reportID", getUrlKey());
                    j.put("source", outcome.getMeasure().getAnalysisItemID());
                    clickEvent = "drillThroughParameterized(" + j.toString() + ")";
                }
                sb.append("<div style='width: 250px;height:100px;'><div style='float: right;padding-top: 20px; padding-bottom: 20px; width: 80px;'> <div style='color:");
                sb.append(fontColor);
                sb.append(";'>");
                sb.append("<img src='/app/assets/icons/32x32/");
                sb.append(ExportService.getIconImage(outcome));
                sb.append("' /> ");
                FormattingConfiguration c = new FormattingConfiguration();
                c.setFormattingType(FormattingConfiguration.PERCENTAGE);

                sb.append(c.createFormatter().format(v));

                sb.append("</div><div>");
                sb.append(outcome.getMeasure().getDisplayName());
                sb.append("</div></div> <div style='font-size: 36px;font-weight: bold;padding-top: 34px;padding-bottom: 34px;color:");
                sb.append(fontColor);
                sb.append(";'> ");
                if(!clickEvent.isEmpty()) {
                    sb.append("<a href='#' class='trendDrillthrough' onclick='");
                    sb.append(clickEvent);
                    sb.append("'>");
                }
                sb.append(ExportService.createValue(0, outcome.getMeasure(), outcome.getNow(), md.cal, md.currencySymbol, false));

                if(!clickEvent.isEmpty()) {
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
