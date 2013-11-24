package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.SavedConfiguration;
import com.easyinsight.logging.LogClass;
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
            LogClass.error(e);
        }
        Pattern p = Pattern.compile("^.*/html/(dashboard|report)/([A-Za-z0-9]+)/config/([A-Za-z0-9]+)$");
        String referer = request.getHeader("Referer");
        String redirect = null;
        Matcher m = p.matcher(referer);
        String target = m.replaceAll("$1");
        if(m.matches()) {
            String config = m.replaceAll("$3");

            if(configID.equals(config))
                redirect = RedirectUtil.getURL(request, "/app/html/" + target + "/" + request.getParameter("dashboardID"));

        }
        if(redirect == null) {
            redirect = referer;
        }
        if(redirect == null) {
            redirect = RedirectUtil.getURL(request, "/app/html/" + target + "/" + request.getParameter("dashboardID"));
        }
        response.sendRedirect(redirect);
    }

}
