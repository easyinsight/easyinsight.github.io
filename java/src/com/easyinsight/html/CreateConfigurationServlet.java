package com.easyinsight.html;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.dashboard.*;
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
        try {
            DashboardService ds = new DashboardService();
            String dashboardIDString = request.getParameter("dashboardID");
            Long dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
            InputStream is = request.getInputStream();
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject jo = (JSONObject) parser.parse(is);
            System.out.println(jo);
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
            Map<FilterPositionKey, FilterDefinition> filters = ds.getFiltersForDashboard(dashboardIDString);

            for(Map.Entry<String, Object> e : ((JSONObject) jo.get("filters")).entrySet()) {
                FilterPositionKey filterKey = FilterPositionKey.parseHtmlKey(e.getKey());
                System.out.println(filterKey.createURLKey() + ": " + e.getValue().toString());
                if(filters.containsKey(filterKey)) {
                    FilterUtils.adjustFilter("", false, filters.get(filterKey), (JSONObject) e.getValue());
                } else {
                    System.out.println("Could not find " + filterKey.createURLKey());
                }

            }

            Map<String, FilterDefinition> seriouslyReallyTheRealFilters = new HashMap<String, FilterDefinition>();
            for(Map.Entry<FilterPositionKey, FilterDefinition> e : filters.entrySet()) {
                seriouslyReallyTheRealFilters.put(e.getKey().createURLKey(), e.getValue());
            }

            positions.setFilterMap(seriouslyReallyTheRealFilters);

            configuration.setName((String) jo.get("name"));
            configuration.setDashboardStackPositions(positions);
            SavedConfiguration c = new DashboardService().saveConfigurationForDashboard(configuration, dashboardID);
            JSONObject target = new JSONObject();
            target.put("target", "/app/html/dashboard/" + dashboardIDString + "/config/" + c.getUrlKey());
            response.setContentType("application/json");
            response.getOutputStream().write(target.toString().getBytes());
            response.getOutputStream().flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
