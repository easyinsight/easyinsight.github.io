package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.*;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/31/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateConfigurationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(request);
        try {
            DashboardService ds = new DashboardService();

            InputStream is = request.getInputStream();
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject jo = (JSONObject) parser.parse(is);
            DashboardStackPositions positions = null;
            SavedConfiguration configuration = null;
            String key = (String) jo.get("key");
            if(key != null) {
                configuration = ds.getConfiguration(key);
                positions = configuration.getDashboardStackPositions();
            } else {
                configuration = new SavedConfiguration();
                positions = new DashboardStackPositions();
            }

            Map<String, Integer> stackPositions = new HashMap<String, Integer>();
            JSONObject stacks = (JSONObject) jo.get("stacks");
            for(Map.Entry<String, Object> e : stacks.entrySet()) {
                stackPositions.put(e.getKey(), (Integer) e.getValue());
            }
            positions.setPositions(stackPositions);

            Map<FilterPositionKey, FilterDefinition> filters = null;
            String dashboardIDString = request.getParameter("dashboardID");
            String reportIDString = request.getParameter("reportID");
            Long dashboardID = null;
            Long reportID = null;
            if(reportIDString != null) {
                InsightResponse id = new AnalysisService().openAnalysisIfPossible(reportIDString);
                reportID = id.getInsightDescriptor().getId();

                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
                List<FilterDefinition> fs = report.getFilterDefinitions();
                filters = new HashMap<FilterPositionKey, FilterDefinition>();
                for(FilterDefinition f : fs) {
                    FilterPositionKey kk = new FilterPositionKey(FilterPositionKey.REPORT, f.getFilterID(), null);
                    filters.put(kk, f);
                }
                for(Map.Entry<String, Object> e : ((JSONObject) jo.get("filters")).entrySet()) {
                    FilterPositionKey filterKey = FilterPositionKey.parseReportKey(e.getKey());
                    if(filters.containsKey(filterKey)) {
                        FilterUtils.adjustFilter("", false, filters.get(filterKey), (JSONObject) e.getValue());
                    } else {
                        System.out.println("Could not find " + filterKey.createURLKey());
                    }

                }
            } else {
                dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);

                Map<String, InsightDescriptor> reportList = new HashMap<String, InsightDescriptor>();
                for(Map.Entry<String, Object> e : ((JSONObject) jo.get("reports")).entrySet()) {
                    InsightResponse ir = new AnalysisService().openAnalysisIfPossible((String) e.getValue());
                    if(ir.getInsightDescriptor() != null)
                    reportList.put(e.getKey(), ir.getInsightDescriptor());
                }


                filters = ds.getFiltersForDashboard(dashboardIDString, reportList);

                for(Map.Entry<String, Object> e : ((JSONObject) jo.get("filters")).entrySet()) {
                    FilterPositionKey filterKey = FilterPositionKey.parseHtmlKey(e.getKey());
                    if(filters.containsKey(filterKey)) {
                        FilterUtils.adjustFilter("", false, filters.get(filterKey), (JSONObject) e.getValue());
                    } else {
                        System.out.println("Could not find " + filterKey.createURLKey());
                    }

                }

                positions.setReports(reportList);


            }

            Map<String, FilterDefinition> seriouslyReallyTheRealFilters = new HashMap<String, FilterDefinition>();
            for(Map.Entry<FilterPositionKey, FilterDefinition> e : filters.entrySet()) {
                seriouslyReallyTheRealFilters.put(e.getKey().createURLKey(), e.getValue());
            }

            positions.setFilterMap(seriouslyReallyTheRealFilters);

            configuration.setName((String) jo.get("name"));
            configuration.setDashboardStackPositions(positions);
            SavedConfiguration c;
            if(dashboardID != null) {
                c = new DashboardService().saveConfigurationForDashboard(configuration, dashboardID);
            } else {
                c = new DashboardService().saveConfigurationForReport(configuration, reportID);
            }
            JSONObject target = new JSONObject();
            if(dashboardIDString != null) {
                target.put("target", "/app/html/dashboard/" + dashboardIDString + "/config/" + c.getUrlKey());
            } else {
                target.put("target", "/app/html/report/" + reportIDString + "/config/" + c.getUrlKey());
            }
            response.setContentType("application/json");
            response.getOutputStream().write(target.toString().getBytes());
            response.getOutputStream().flush();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }

}
