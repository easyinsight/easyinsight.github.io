package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.DashboardStackPositions;
import com.easyinsight.dashboard.FilterPositionKey;
import com.easyinsight.html.FilterUtils;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 6/30/14
 * Time: 2:51 PM
 */
public class DashboardPDFServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {

            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            StringWriter writer = new StringWriter();
            IOUtils.copy(req.getInputStream(), writer, "UTF-8");
            String jsonString = writer.toString();
            System.out.println(jsonString);
            JSONObject postObject = (net.minidev.json.JSONObject) parser.parse(jsonString);
            String dashboardIDString = postObject.get("dashboardID").toString();
            JSONObject jo = (JSONObject) postObject.get("configuration");
            Map<String, Integer> stackPositions = new HashMap<>();
            JSONObject stacks = (JSONObject) jo.get("stacks");
            for(Map.Entry<String, Object> e : stacks.entrySet()) {
                stackPositions.put(e.getKey(), (Integer) e.getValue());
            }
            DashboardStackPositions positions = new DashboardStackPositions();
            positions.setPositions(stackPositions);

            Map<FilterPositionKey, FilterDefinition> filters;


            Long dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);


            Map<String, InsightDescriptor> reportList = new HashMap<>();
            for(Map.Entry<String, Object> e : ((JSONObject) jo.get("reports")).entrySet()) {
                InsightResponse ir = new AnalysisService().openAnalysisIfPossible((String) e.getValue());
                if(ir.getInsightDescriptor() != null)
                    reportList.put(e.getKey(), ir.getInsightDescriptor());
            }

            DashboardService ds = new DashboardService();
            filters = ds.getFiltersForDashboard(dashboardIDString, reportList);

            for(Map.Entry<String, Object> e : ((JSONObject) jo.get("filters")).entrySet()) {
                FilterPositionKey filterKey = FilterPositionKey.parseHtmlKey(e.getKey());
                if(filters.containsKey(filterKey)) {
                    FilterUtils.adjustFilter("", false, filters.get(filterKey), (JSONObject) e.getValue());
                } else {
                    System.out.println("Could not find " + filterKey.createURLKey());
                }
            }



            Map<String, FilterDefinition> seriouslyReallyTheRealFilters = new HashMap<>();
            for(Map.Entry<FilterPositionKey, FilterDefinition> e : filters.entrySet()) {
                seriouslyReallyTheRealFilters.put(e.getKey().createURLKey(), e.getValue());
            }

            positions.setFilterMap(seriouslyReallyTheRealFilters);
            @SuppressWarnings("unchecked") Map<String, JSONObject> map = (Map<String, JSONObject>) postObject.get("reportImages");
            Map<String, PDFImageData> images = new HashMap<>();
            for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
                String urlKey = entry.getKey();
                JSONObject val = entry.getValue();
                if (val != null) {
                    Object widthObj = val.get("width");
                    Object heightObj = val.get("height");
                    if (widthObj != null && heightObj != null) {
                        int width = (int) Double.parseDouble(val.get("width").toString());
                        int height = (int) Double.parseDouble(val.get("height").toString());
                        PDFImageData pdfImageData = new PDFImageData();
                        pdfImageData.setWidth(width);
                        pdfImageData.setHeight(height);
                        images.put(urlKey, pdfImageData);
                    }
                }
            }
            Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
            int timezoneOffset = 0;
            if (req.getParameter("timezoneOffset") != null) {
                timezoneOffset = Integer.parseInt(req.getParameter("timezoneOffset"));
            }
            byte[] bytes = new DashboardPDF().createPDF(dashboard, positions, images, timezoneOffset);
            String urlKey = new ExportService().htmlDashboardToPDFAlt(bytes, dashboard, req);
            JSONObject object = new JSONObject();
            object.put("urlKey", urlKey);
            resp.setContentType("application/json");
            resp.getOutputStream().write(object.toString().getBytes());
            resp.getOutputStream().flush();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
