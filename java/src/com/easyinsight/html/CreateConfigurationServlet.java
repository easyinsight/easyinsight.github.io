package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.DashboardStackPositions;
import com.easyinsight.dashboard.SavedConfiguration;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
            DashboardStackPositions positions = new DashboardStackPositions();
            Map<String, Integer> stackPositions = new HashMap<String, Integer>();
            JSONObject stacks = (JSONObject) jo.get("stacks");
            for(Map.Entry<String, Object> e : stacks.entrySet()) {
                stackPositions.put(e.getKey(), (Integer) e.getValue());
            }

            positions.setPositions(stackPositions);
            SavedConfiguration configuration = new SavedConfiguration();
            configuration.setName((String) jo.get("name"));
            configuration.setDashboardStackPositions(positions);
            new DashboardService().saveConfigurationForDashboard(configuration, dashboardID);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
