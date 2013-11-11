package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.SavedConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/31/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteConfigurationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jo = new JSONObject();
        String configID = request.getParameter("dashboardConfig");
        try {
            SavedConfiguration config =  new DashboardService().getConfiguration(configID);
            new DashboardService().deleteConfiguration(config);
            jo.put("result", "Success!");
        } catch(Exception e) {
            try {
                jo.put("result", "There was an error.");
            } catch (JSONException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        Pattern p = Pattern.compile("^.*/html/dashboard/([A-Za-z0-9]+)/config/([A-Za-z0-9]+)/delete$");
        String referer = request.getHeader("Referer");
        String redirect = null;
        Matcher m = p.matcher(referer);
        if(m.matches()) {
            String config = m.replaceAll("$2");
            System.out.println(config);
            if(configID.equals(config))
                redirect = RedirectUtil.getURL(request, "/app/html/dashboard/" + request.getParameter("dashboardID"));

        }
        if(redirect == null) {
            redirect = referer;
        }
        response.sendRedirect(redirect);
    }

}
