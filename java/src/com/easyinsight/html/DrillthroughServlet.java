package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.EIConnection;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        DrillThroughResponse drillThroughResponse = new AnalysisService().drillThrough(drillThrough, data, linkItem, report);
        // return a URL for response redirect?
        JSONObject result = new JSONObject();
        EIDescriptor descriptor = drillThroughResponse.getDescriptor();
        if (descriptor.getType() == EIDescriptor.REPORT) {
            InsightDescriptor insightDescriptor = (InsightDescriptor) descriptor;
            request.getSession().setAttribute("drillthroughFiltersFor"+ insightDescriptor.getId(), drillThroughResponse.getFilters());
            result.put("url", "/app/html/report/" + insightDescriptor.getUrlKey());
        } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
            DashboardDescriptor dashboardDescriptor = (DashboardDescriptor) descriptor;
            request.getSession().setAttribute("drillthroughFiltersFor"+ dashboardDescriptor.getId(), drillThroughResponse.getFilters());
            result.put("url", "/app/html/dashboard/" + dashboardDescriptor.getUrlKey());
        }
        response.setContentType("application/json");
        response.getOutputStream().write(result.toString().getBytes());
        response.getOutputStream().flush();
    }
}
