package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSStackedBarChartDefinition;
import com.easyinsight.analysis.definitions.WSStackedColumnChartDefinition;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 6/20/12
 * Time: 9:45 AM
 */
public class DrillthroughServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws Exception {
        Long drillthroughID = Long.parseLong(request.getParameter("drillthroughID"));
        Long sourceField = Long.parseLong(request.getParameter("sourceField"));
        String embeddedString = request.getParameter("embedded");
        boolean embedded = false;
        if (embeddedString != null) {
            embedded = Boolean.parseBoolean(embeddedString);
        }
        AnalysisItem linkItem = null;
        Map<String, Object> data = new HashMap<String, Object>();
        for (AnalysisItem analysisItem : report.getAllAnalysisItems()) {
            if (analysisItem.getAnalysisItemID() == sourceField) {
                linkItem = analysisItem;
            }
            String value = request.getParameter("f" + analysisItem.getAnalysisItemID());
            data.put(analysisItem.qualifiedName(), value);
        }
        if (linkItem == null) {
            throw new RuntimeException();
        }
        DrillThrough drillThrough = null;
        for (Link link : linkItem.getLinks()) {
            if (link.getLinkID() == drillthroughID) {
                drillThrough = (DrillThrough) link;
            }
        }
        if (drillThrough == null) {
            throw new RuntimeException();
        }

        String altKey = null;

        if(report.getReportType() == WSAnalysisDefinition.STACKED_BAR) {
            altKey = String.valueOf(((WSStackedBarChartDefinition) report).getStackItem().getAnalysisItemID());
        } else if(report.getReportType() == WSAnalysisDefinition.STACKED_COLUMN) {
            altKey = String.valueOf(((WSStackedColumnChartDefinition) report).getStackItem().getAnalysisItemID());
        }
        DrillThroughResponse drillThroughResponse = new AnalysisService().drillThrough(drillThrough, data, linkItem, report, altKey);
        // return a URL for response redirect?
        JSONObject result = new JSONObject();



        EIDescriptor descriptor = drillThroughResponse.getDescriptor();
        if (descriptor.getType() == EIDescriptor.REPORT) {
            InsightDescriptor insightDescriptor = (InsightDescriptor) descriptor;
            PreparedStatement saveDrillStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_SAVE (REPORT_ID, URL_KEY, SAVE_TIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveDrillStmt.setLong(1, insightDescriptor.getId());
            String urlKey = RandomTextGenerator.generateText(40);
            saveDrillStmt.setString(2, urlKey);
            saveDrillStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            saveDrillStmt.execute();
            long drillID = Database.instance().getAutoGenKey(saveDrillStmt);
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_REPORT_SAVE_FILTER (DRILLTHROUGH_SAVE_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filter : drillThroughResponse.getFilters()) {
                Session session = Database.instance().createSession(conn);
                filter.beforeSave(session);
                session.save(filter);
                session.flush();
                session.close();
                saveStmt.setLong(1, drillID);
                saveStmt.setLong(2, filter.getFilterID());
                saveStmt.execute();
            }
            if (embedded) {
                result.put("url", "/app/embed/drillThroughReport/" + urlKey);
            } else {
                result.put("url", "/app/html/drillThroughReport/" + urlKey);
            }
        } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
            DashboardDescriptor dashboardDescriptor = (DashboardDescriptor) descriptor;
            PreparedStatement saveDrillStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_SAVE (DASHBOARD_ID, URL_KEY, SAVE_TIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveDrillStmt.setLong(1, dashboardDescriptor.getId());
            String urlKey = RandomTextGenerator.generateText(40);
            saveDrillStmt.setString(2, urlKey);
            saveDrillStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            saveDrillStmt.execute();
            long drillID = Database.instance().getAutoGenKey(saveDrillStmt);
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_REPORT_SAVE_FILTER (DRILLTHROUGH_SAVE_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filter : drillThroughResponse.getFilters()) {
                Session session = Database.instance().createSession(conn);
                filter.beforeSave(session);
                session.save(filter);
                session.flush();
                session.close();
                saveStmt.setLong(1, drillID);
                saveStmt.setLong(2, filter.getFilterID());
                saveStmt.execute();
            }
            if (embedded) {
                result.put("url", "/app/embed/drillThroughDashboard/" + urlKey);
            } else {
                result.put("url", "/app/html/drillThroughDashboard/" + urlKey);
            }
        }
        response.setContentType("application/json");
        response.getOutputStream().write(result.toString().getBytes());
        response.getOutputStream().flush();
    }
}
