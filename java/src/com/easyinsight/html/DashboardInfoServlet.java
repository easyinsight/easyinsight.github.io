package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/21/13
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardInfoServlet extends JSONServlet {
    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest req) throws Exception {
        String reportIDString = req.getParameter("dashboardID");
        ExportMetadata md = ExportService.createExportMetadata(conn);
        long dashboardID = new DashboardService().canAccessDashboard(reportIDString);
        JSONObject jo = new JSONObject();
        jo.put("configurations", new JSONArray(new DashboardService().getConfigurationsForDashboard(dashboardID, conn).stream().map((a) -> {
            try {
                return a.toJSON();
            } catch (JSONException e) {
               throw new RuntimeException(e);
            }
        }).collect(Collectors.toList())));
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());

    }


    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
